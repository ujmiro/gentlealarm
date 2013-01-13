package mobi.ii;

import java.sql.SQLException;
import java.util.Arrays;

import media.managers.SoundManager;

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
				SoundManager soundManager = new SoundManager(PhaseSettingsActivity.this);
				soundManager.startAlarm(seekBar.getProgress());
				showDialog(soundManager);
			}
		});
	}
	
	private void showDialog(final SoundManager soundManager){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle(getResources().getString(R.string.testDialogTitle));
			alertDialogBuilder.setMessage(getResources().getString(R.string.testDialogMessage))
							  .setCancelable(false)
							  .setPositiveButton(getResources().getString(R.string.closeDialogButtonLabel),new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										soundManager.stopAlarm();
									}
								  });
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
	}
	
	private void addSaveButtonOnClickListener(){
		Button save = (Button)findViewById(R.id.saveButton);
		save.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setting.setVolume(seekBar.getProgress());
				setting.setTimeInSeconds((String)spinner.getSelectedItem());
				try {
					getHelper().getSettingDao().update(setting);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				PhaseSettingsActivity.this.finish();
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
