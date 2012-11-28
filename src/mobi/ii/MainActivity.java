package mobi.ii;

import java.util.List;

import DB.OrmManager;
import DB.POCO.User;
import android.os.Bundle;
import android.widget.TimePicker;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class MainActivity extends OrmLiteBaseActivity<OrmManager> {
	
	private TimePicker timePicker;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        
        timePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        timePicker.setIs24HourView(true);
    }
}