package mobi.ii;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import DB.AlarmScheduler;
import DB.OrmManager;
import DB.POCO.Alarm;
import Singletons.Common;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class MainActivity extends OrmLiteBaseActivity<OrmManager> {
	
	public List<Alarm> alarmsList;
	private AlarmAdapter alarmAdapter;
	
	@Override
	public void onResume(){
		super.onResume();
		refreashListView();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		try {
		 	Common.getInstance().setUser(getHelper().getUserDao().queryBuilder().where().eq("Name", "DefoultUSer").query().get(0));
		} catch (SQLException e) {
			finish();
		}
		
		fullFillAlarmsList();
		addListenerToSettingsButton();
		addListenerToAddAlarmButton();
		createAlarmsList();
	}
	
	private void refreashListView(){
		fullFillAlarmsList();
		if (alarmAdapter != null)
			alarmAdapter.notifyDataSetChanged();
	}
	
	private void fullFillAlarmsList(){
		try {
			alarmsList = getHelper().getAlarmDao().queryBuilder().where().eq("UserId", Common.getInstance().getUser().getId()).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createAlarmsList(){
		ListView listView = (ListView) findViewById(R.id.alarmsList);
		alarmAdapter = new AlarmAdapter();
		listView.setAdapter(alarmAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View arg1, final int chosen, long arg3) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
				alertDialogBuilder.setTitle(getResources().getString(R.string.deleteAlarmAlertTitle));
				alertDialogBuilder.setMessage(getResources().getString(R.string.deleteAlarmAlertMessage))
								  .setCancelable(false).setNegativeButton(getResources().getString(R.string.cancelButtonLabel), new DialogInterface.OnClickListener() {
									
									  public void onClick(DialogInterface dialog, int which) {
									  }
								}).setPositiveButton(getResources().getString(R.string.deleteAlarmButtonLabel), new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface dialog, int which) {
										new AlarmScheduler(MainActivity.this).finishAlarm(alarmsList.get(chosen));
										refreashListView();
									}
								} );
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});
	}
	
	private void addListenerToAddAlarmButton(){
		Button add = (Button) findViewById(R.id.addAlarmButton);
		add.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SetAlarmActivity.class);
				startActivity(intent);
			}
		});
	}
	
	public void addListenerToSettingsButton() {
		Button settings = (Button) findViewById(R.id.settingButton);
		settings.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity(intent);
			}
		});
	}
	
	public class AlarmAdapter extends BaseAdapter {
		
		public int getCount() {
			return alarmsList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView != null)
				return (TextView) convertView;

			TextView tv = new TextView(getApplicationContext());
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(alarmsList.get(position).getAlarmAt());
			tv.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + 
					"    " + getResources().getStringArray(R.array.dayOfWeek)[calendar.get(Calendar.DAY_OF_WEEK)]);
			return tv;
		}
	}
}
