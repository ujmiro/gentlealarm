package mobi.ii;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import media.managers.SoundManager;
import DB.OrmManager;
import DB.POCO.Alarm;
import DB.POCO.Setting;
import Singletons.Common;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class WakeUpActivity extends OrmLiteBaseActivity<OrmManager> {

	private SoundManager soundManager;
	private WakeLock wl;
	private KeyguardLock keyguard;
	private ArrayList<Integer> correctOrder = new ArrayList<Integer>();
	private int check = 0;
	private ArrayList<ImageView> images = new ArrayList<ImageView>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.wake_up_layout);
	    
	    manageImages();
		unlockScreean();
		startAlarm(savedInstanceState);
	}
	
	@Override
	public void onBackPressed() {}
	
	private void manageImages(){
		images.add((ImageView)findViewById(R.id.imageView1));
		images.add((ImageView)findViewById(R.id.imageView2));
		images.add((ImageView)findViewById(R.id.imageView3));
		images.add((ImageView)findViewById(R.id.imageView4));
		images.add((ImageView)findViewById(R.id.imageView5));
		images.add((ImageView)findViewById(R.id.imageView6));
		images.add((ImageView)findViewById(R.id.imageView7));
		images.add((ImageView)findViewById(R.id.imageView8));
		images.add((ImageView)findViewById(R.id.imageView9));
		
		for (int i = 1; i<= images.size(); ++i)
			addImageListner(images.get(i - 1), i);
		
		correctOrder.add(4);
		correctOrder.add(5);
		correctOrder.add(8);
		correctOrder.add(9);
	}
	
	private void startAlarm(Bundle bundle){
		int startFrom = getIntent().getIntExtra("startFromPhase", 10);
		int userId = getIntent().getIntExtra("userId", 10);
		try {
		 	Common.getInstance().setUser(getHelper().getUserDao().queryBuilder().where().eq("Id", userId).query().get(0));
		} catch (SQLException e) {
			finish();
		}
		
		final Setting setting = Common.getInstance().getSettings()[startFrom];
		soundManager = new SoundManager(this);
		soundManager.startAlarm(setting.getVolume());
		
		new Thread(new Runnable() {
			public void run() {
				GregorianCalendar calendar = new GregorianCalendar();
				try {
					Thread.sleep(1000 * setting.getTimeInSeconds());
					if (setting.getPhase() != 0 && check != correctOrder.size()){
						calendar.add(Calendar.MINUTE, MainActivity.BREAK_TIME);
						
						AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
						Intent intent = new Intent(WakeUpActivity.this, WakeUpReceiver.class);
					    intent.putExtra("userId", Common.getInstance().getUser().getId());
						intent.putExtra("startFromPhase", setting.getPhase() - 1);
						
						PendingIntent pendingIntent = PendingIntent.getBroadcast(WakeUpActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
						alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
					} else if (setting.getPhase() == 0){
						finishWithSuccess();
					}
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void finishWithSuccess(){
		Alarm alarm = Common.getInstance().getAlarms()[0];
		alarm.setExecuted(true);
		try {
			getHelper().getAlarmDao().update(alarm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finish();
	}
	
	private void addImageListner(final ImageView imageView, final int number){
		imageView.setOnClickListener(new OnClickListener() {
			
			private int myNumber = number;
			
			public void onClick(View v) {
				if (correctOrder.get(check) == myNumber){
					imageView.setImageResource(R.drawable.clicked_dot);
					++check;
				} else {
					check = 0;
					for (int i = 1; i <= images.size(); ++i)
						if (correctOrder.contains(i)){
							images.get(i - 1).setImageResource(R.drawable.cliek_dot);
						} else {
							images.get(i - 1).setImageResource(R.drawable.notclicked_dot);
						}
				}
				
				if (correctOrder.size() == check){
					finishWithSuccess();
				}
			}
		});
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		lockScreean();
		soundManager.stopAlarm();
	}
	
	private void unlockScreean(){
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "mobi.ii.WakeUpActivity");
		wl.acquire();
		
		KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
	    keyguard = km.newKeyguardLock("MyApp");
	    keyguard.disableKeyguard();
	}
	
	private void lockScreean(){
		keyguard.reenableKeyguard();
		wl.release();
	}
}
