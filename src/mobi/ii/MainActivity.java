package mobi.ii;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
import android.widget.ToggleButton;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class MainActivity extends OrmLiteBaseActivity<OrmManager> {
	
	private TimePicker timePicker;
	private PendingIntent pendingIntent;
	public final static int BREAK_TIME = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        
		try {
		 	Common.getInstance().setUser(getHelper().getUserDao().queryBuilder().where().eq("Name", "DefoultUSer").query().get(0));
		} catch (SQLException e) {
			finish();
		}
		
		setStatusToggleButton();
        setTimePicker();
        addListnerToDisableButton();
        addListnerToEnableButton();
        addListnerToSettingsButton();
    }
    
    private void setStatusToggleButton(){
    	ToggleButton toggle = (ToggleButton) findViewById(R.id.onOfToggle);
    	toggle.setEnabled(false);
    	toggle.setChecked(!Common.getInstance().getAlarms()[0].isExecuted());
    }
    
    private void setTimePicker(){
    	timePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        timePicker.setIs24HourView(true);
        if (new GregorianCalendar().get(Calendar.HOUR_OF_DAY) > 12)
        	timePicker.setCurrentHour(timePicker.getCurrentHour() + 12);
        
        try {
        	Alarm alarm = Common.getInstance().getAlarms()[0];
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(alarm.getAlarmAt());
			timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		} catch (Exception e) {
			Log.w("Exception", "Create main view.");
		}
    }
    
    private void addListnerToEnableButton(){
    	Button startButton = (Button) findViewById(R.id.enableAlarmButton);
        startButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				timePicker.clearFocus();
				AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
				
				GregorianCalendar calendar = new GregorianCalendar();
				if (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE) >= timePicker.getCurrentHour() * 60 + timePicker.getCurrentMinute()){
					calendar.add(Calendar.DAY_OF_YEAR, 1);
				}
				calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
				calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
				calendar.set(Calendar.SECOND, 0);
				
				Alarm alarm = Common.getInstance().getAlarms()[0];
				alarm.setAlarmAt(calendar);
				alarm.setExecuted(false);
				try {
					MainActivity.this.getHelper().getAlarmDao().update(alarm);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				int startFromPhase = 0;
				for (int i = 1; i <= Common.getInstance().getUser().getPhaseAmount(); ++i){
					if (calendar.getTimeInMillis() - BREAK_TIME * 60 * 1000 < new GregorianCalendar().getTimeInMillis())
						break;
					
					calendar.add(Calendar.MINUTE, -BREAK_TIME);
					startFromPhase = i;
				}
				
				Intent intent = new Intent(MainActivity.this, WakeUpReceiver.class);
			    intent.putExtra("userId", Common.getInstance().getUser().getId());
				intent.putExtra("startFromPhase", startFromPhase);
				
				pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
				
				
				alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
				String time =  timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
				
				try {
					FileOutputStream saveTime = openFileOutput("AlarmDateTime", Context.MODE_PRIVATE);
					saveTime.write((time + ";").getBytes());
					saveTime.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.alarmEnableTogle) + time, Toast.LENGTH_SHORT);
				toast.show();
				setStatusToggleButton();
			}
		});
    }
    
    public void addListnerToSettingsButton(){
    	Button settings = (Button) findViewById(R.id.settingsButton);
    	settings.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity(intent);
			}
		});
    }
    
    public void addListnerToDisableButton(){
    	 Button stopButton = (Button) findViewById(R.id.disableAlarmButton);
         stopButton.setOnClickListener(new OnClickListener() {
 			
 			public void onClick(View v) {
 				AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

 			    Intent updateServiceIntent = new Intent(MainActivity.this, WakeUpReceiver.class);
 			    PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(MainActivity.this, 0, updateServiceIntent, 0);

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
					MainActivity.this.getHelper().getAlarmDao().update(alarm);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				setStatusToggleButton();
	        }
 		});
    }
}














