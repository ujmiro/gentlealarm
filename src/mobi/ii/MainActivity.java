package mobi.ii;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import DB.OrmManager;
import DB.POCO.User;
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

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class MainActivity extends OrmLiteBaseActivity<OrmManager> {
	
	private TimePicker timePicker;
	private PendingIntent pendingIntent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        
        timePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        timePicker.setIs24HourView(true);
        
        Button startButton = (Button) findViewById(R.id.enableAlarmButton);
        startButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
				Intent intent = new Intent(MainActivity.this, WakeUpReceiver.class);
				if (pendingIntent == null)
					pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);// PendingIntent.FLAG_UPDATE_CURRENT);
				
				GregorianCalendar calendar = new GregorianCalendar();
				if (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE) >= timePicker.getCurrentHour() * 60 + timePicker.getCurrentMinute()){
					calendar.add(Calendar.DAY_OF_YEAR, 1);
				}
				calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
				calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
						       		           
				alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
			}
		});
        
        Button stopButton = (Button) findViewById(R.id.disableAlarmButton);
        stopButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

			    Intent updateServiceIntent = new Intent(MainActivity.this, WakeUpReceiver.class);
			    PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(MainActivity.this, 0, updateServiceIntent, 0);

			    // Cancel alarms
			    try {
			        alarmManager.cancel(pendingUpdateIntent);
			        Log.w("AlarmManager update was not canceled. ",  "called");
			    } catch (Exception e) {
			        Log.w("AlarmManager update was not canceled. ",  e.toString());
			    }
				
			}
		});
    }
}














