package mobi.ii;

import java.util.List;

import DB.OrmManager;
import DB.POCO.User;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class MainActivity extends OrmLiteBaseActivity<OrmManager> {
	
	private TimePicker timePicker;
	private Button startButton;
	private PendingIntent pendingIntent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        
        timePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        timePicker.setIs24HourView(true);
        
        startButton = (Button) findViewById(R.id.enableAlarmButton);
        startButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
				Intent intent = new Intent(MainActivity.this, WakeUpActivity.class);
				if (pendingIntent == null)
					pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				
			}
		});
    }
}














