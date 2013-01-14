package mobi.ii;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import DB.AlarmScheduler;
import DB.OrmManager;
import DB.POCO.Alarm;
import Singletons.Common;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class SetAlarmActivity extends OrmLiteBaseActivity<OrmManager> {
	
	private TimePicker timePicker;
	public final static int BREAK_TIME = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm_layout);

        setTimePicker();
        addListnerToDisableButton();
        addListnerToEnableButton();
    }
    
    private void setTimePicker(){
    	timePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        timePicker.setIs24HourView(true);
        if (new GregorianCalendar().get(Calendar.HOUR_OF_DAY) > 12)
        	timePicker.setCurrentHour(timePicker.getCurrentHour() + 12);
        
        try {
        	 FileInputStream savedTime = openFileInput("AlarmDateTime");
        	 byte[] text = new byte[100000];
        	 savedTime.read(text);
        	 String time = new String(text);
        	 timePicker.setCurrentHour(Integer.valueOf(time.substring(0, time.indexOf(":"))));
        	 timePicker.setCurrentMinute(Integer.valueOf(time.substring(time.indexOf(":") + 1, time.indexOf(";"))));
        	 savedTime.close();
		} catch (Exception e) {
			GregorianCalendar calendar = new GregorianCalendar();
			timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
			Log.w("Exception", "Create main view.");
		}
    }
    
    private void addListnerToEnableButton(){
    	Button startButton = (Button) findViewById(R.id.enableAlarmButton);
        startButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				timePicker.clearFocus();
				
				GregorianCalendar calendar = new GregorianCalendar();
				if (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE) >= timePicker.getCurrentHour() * 60 + timePicker.getCurrentMinute()){
					calendar.add(Calendar.DAY_OF_YEAR, 1);
				}
				calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
				calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
				calendar.set(Calendar.SECOND, 0);
				AlarmScheduler scheduler = new AlarmScheduler(SetAlarmActivity.this);
				scheduler.scheduleAlarms(calendar);
				scheduler.broadCastAlarm();
				
				String time =  timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
				Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.alarmEnableTogle) + time, Toast.LENGTH_SHORT);
				toast.show();
				
				try {
					FileOutputStream savedTime = openFileOutput("AlarmDateTime", MODE_PRIVATE);
					savedTime.write((time + ";").getBytes());
					savedTime.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
    
    public void addListnerToDisableButton(){
    	 Button stopButton = (Button) findViewById(R.id.disableAlarmButton);
         stopButton.setOnClickListener(new OnClickListener() {
 			
 			public void onClick(View v) {
 				AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

 			    Intent updateServiceIntent = new Intent(SetAlarmActivity.this, WakeUpReceiver.class);
 			    PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(SetAlarmActivity.this, 0, updateServiceIntent, 0);

 			    try {
 			        alarmManager.cancel(pendingUpdateIntent);
 			        Log.w("AlarmManager update was not canceled. ",  "called");
				} catch (Exception e) {
					Log.w("AlarmManager update was not canceled. ", e.toString());
				}
				Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.alarmDisableTogle), Toast.LENGTH_SHORT);
				toast.show();
				
				Alarm alarm = Common.getInstance().getAlarms()[0];
				alarm.setExecuted(true);
				try {
					SetAlarmActivity.this.getHelper().getAlarmDao().update(alarm);
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
 		});
    }
}














