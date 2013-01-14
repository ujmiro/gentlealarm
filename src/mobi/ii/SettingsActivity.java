package mobi.ii;

import java.sql.SQLException;

import DB.OrmManager;
import DB.POCO.User;
import Singletons.Common;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class SettingsActivity extends OrmLiteBaseActivity<OrmManager> {
	
	private Button firstButton;
	private Button secondButton;
	private Button thirdButton;
	private Button fourthButton;
	private int phaseAmount;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		
		addSetUpMusicButtonListener();
		firstButton = (Button) findViewById(R.id.setFirstPhaseButton);
		addButtonListner(1, firstButton);
		secondButton = (Button) findViewById(R.id.setSecondPhaseButton);
		addButtonListner(2, secondButton);
		thirdButton = (Button) findViewById(R.id.setThirdPhaseButton);
		addButtonListner(3, thirdButton);
		fourthButton = (Button) findViewById(R.id.setFourthPhaseButton);
		addButtonListner(4, fourthButton);
				
		setSpinner();
	}
	
	private void addSetUpMusicButtonListener(){
		Button button = (Button) findViewById(R.id.setAlarmAudionButton);
		button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), PickUpMusicActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void addButtonListner(final int buttonNumber, Button button){
		button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), PhaseSettingsActivity.class);
				intent.putExtra("buttonNumber", buttonNumber);
				intent.putExtra("phaseAmount", phaseAmount);
				startActivity(intent);
			}
		});
	}
	
	private void setSpinner(){
		Spinner spinner = (Spinner) findViewById(R.id.phaseSpinner);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> spinner, View arg1, int chosen, long arg3) {
				phaseAmount = Integer.valueOf((String)spinner.getItemAtPosition(chosen));
				setButtonVisibility(phaseAmount, 2, secondButton);
				setButtonVisibility(phaseAmount, 3, thirdButton);
				setButtonVisibility(phaseAmount, 4, fourthButton);
				
				User user = Common.getInstance().getUser();
				if (user.getPhaseAmount() != phaseAmount){
					user.setPhaseAmount(phaseAmount - 1);
					try {
						getHelper().getUserDao().update(user);
					} catch (SQLException e) {
						//TODO: show pop up
						e.printStackTrace();
					}
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
			private void setButtonVisibility(int nr, int buttonNumber, Button button){
				if (buttonNumber > nr)
					button.setVisibility(View.INVISIBLE);
				else
					button.setVisibility(View.VISIBLE);
			}
			
		});
		
		spinner.setSelection(Common.getInstance().getUser().getPhaseAmount());
	}
}
