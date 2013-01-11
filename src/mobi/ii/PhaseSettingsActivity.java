package mobi.ii;

import java.sql.SQLException;
import java.util.Arrays;

import DB.OrmManager;
import DB.POCO.Setting;

import Singletons.Common;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class PhaseSettingsActivity extends OrmLiteBaseActivity<OrmManager>{

	private Setting setting;
	private SeekBar seekBar;
	private Spinner spinner;
	
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.phase_settings_layout);
		
		Bundle extras = getIntent().getExtras();
		setting = Common.getInstance().getSettings()[extras.getInt("phaseAmount") - extras.getInt("buttonNumber")];
		
		setSeekBar();
		setSpinner();
		addSaveButtonOnClickListener();
		addTetsButtonOnClickListener();
	}
	
	private void addTetsButtonOnClickListener(){
		Button test = (Button) findViewById(R.id.testButton);
		test.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				showDialog();
			}
		});
		
	}
	
	private void showDialog(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle("Testing");
			alertDialogBuilder.setMessage("Naciœnij ok aby zakoñczyæ.")
							  .setCancelable(false)
							  .setPositiveButton("Zakoñcz",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										
									}
								  });
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
	}
	
	private void addSaveButtonOnClickListener(){
		Button save = (Button)findViewById(R.id.saveButton);
		final PhaseSettingsActivity current = this;
		save.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setting.setVolume(seekBar.getProgress());
				setting.setTimeInSeconds((String)spinner.getSelectedItem());
				try {
					getHelper().getSettingDao().update(setting);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				current.finish();
			}
		});
	}
	
	private void setSeekBar(){
		seekBar = (SeekBar) findViewById(R.id.volumeValue);
		seekBar.setMax(100);
		seekBar.setProgress(setting.getVolume());
	}
	
	private void setSpinner(){
		spinner = (Spinner) findViewById(R.id.timeSpinner);
		spinner.setSelection(Arrays.asList(getResources().getStringArray(R.array.timeArrays)).indexOf(setting.getTimeInSecondsAsString()));
	}
}
