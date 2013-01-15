package mobi.ii;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import DB.AlarmScheduler;
import DB.OrmManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class SetAlarmActivity extends OrmLiteBaseActivity<OrmManager> {
	
	private TimePicker timePicker;
	private ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
	private int selected;
	public final static int BREAK_TIME = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm_layout);

        selected = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);
        
        setTimePicker();
        addListnerToEnableButton();
        createCheckBoxList();
    }
    
    private void createCheckBoxList(){
    	checkBoxes.add((CheckBox)findViewById(R.id.sundayCheckBox));
    	checkBoxes.add((CheckBox)findViewById(R.id.mondayCheckBox));
    	checkBoxes.add((CheckBox)findViewById(R.id.tuesdayCheckBox));
    	checkBoxes.add((CheckBox)findViewById(R.id.wednesdayCheckBox));
    	checkBoxes.add((CheckBox)findViewById(R.id.thursdayCheckBox));
    	checkBoxes.add((CheckBox)findViewById(R.id.fridayCheckBox));
    	checkBoxes.add((CheckBox)findViewById(R.id.saturdayCheckBox));
    	
    	for (int i = 1; i < 8; ++i){
    		addCheckBoxListener(checkBoxes.get(i - 1), i);
    	}
    }
    
    private void addCheckBoxListener(CheckBox checkBox, final int position){
    	checkBox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (((CheckBox)v).isChecked()){
					for (CheckBox box : checkBoxes){
						box.setChecked(false);
					}
					((CheckBox)v).setChecked(true);
					selected = position;
				} else {
					selected = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);
				}
			}
		});
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
    
    private int calculateDayShift(TimePicker timePicker){
    	int shift = 0;
    	GregorianCalendar calendar = new GregorianCalendar();
		if (calendar.get(Calendar.DAY_OF_WEEK) < selected){
			shift = selected - calendar.get(Calendar.DAY_OF_WEEK); 
		} else if (calendar.get(Calendar.DAY_OF_WEEK) > selected){
			shift = 7 - calendar.get(Calendar.DAY_OF_WEEK) + selected;
		} else if (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE) >= timePicker.getCurrentHour() * 60 + timePicker.getCurrentMinute()){
			shift = 7;
		}
		return shift;
    }
    
    private void addListnerToEnableButton(){
    	Button startButton = (Button) findViewById(R.id.enableAlarmButton);
        startButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				timePicker.clearFocus();
				
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
				calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
				calendar.set(Calendar.SECOND, 0);
				calendar.add(Calendar.DAY_OF_YEAR, calculateDayShift(timePicker));
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
		        finish();
			}
		});
    }
}














