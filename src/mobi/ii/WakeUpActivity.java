package mobi.ii;

import java.sql.SQLException;
import java.util.ArrayList;

import media.managers.SoundManager;
import DB.AlarmScheduler;
import DB.OrmManager;
import DB.POCO.Alarm;
import DB.POCO.Schedule;
import DB.POCO.Setting;
import Singletons.Common;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
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
	private Alarm alarm;
	private Schedule schedule;
	private AlarmScheduler alarmScheduler;
	private boolean finished = false;
	private boolean alarmDisabled = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.wake_up_layout);
	    
	    alarmScheduler =  new AlarmScheduler(WakeUpActivity.this);
	    int scheduleId = getIntent().getIntExtra("scheduleId", 10);
	    try {
	    	schedule = getHelper().getScheduleDao().queryForId(scheduleId);
	    	if (schedule == null){
	    		alarmScheduler.broadCastAlarm();
	    		finish();
	    		return;
	    	}
	    	alarm = getHelper().getAlarmDao().queryForId(schedule.getAlarm().getId());
	    	Common.getInstance().setUser(getHelper().getUserDao().queryForId(alarm.getUser().getId()));
		} catch (SQLException e) {
			finish();
		}
		
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
		try {
			final Setting setting = Common.getInstance().getSettings()[schedule.getPhase()];
			soundManager = new SoundManager(this);
			soundManager.startAlarm(setting.getVolume());
			alarmScheduler.DeleteSchedule(schedule);
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000 * setting.getTimeInSeconds());
						AlarmScheduler scheduler = new AlarmScheduler(WakeUpActivity.this);
						if (setting.getPhase() == 0){
							finishWithSuccess();
						}
						if (!alarmDisabled){
							scheduler.broadCastAlarm();
						}
						finish();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}catch (Exception e){
			finish();
		}
	}
	
	private void finishWithSuccess(){
		alarmScheduler.finishAlarm(alarm);
		alarmDisabled = true;
		finish();
	}
	
	public void finish(){
		if (!finished){
			finished = true;
			super.finish();
		}
	}
	
	private void addImageListner(final ImageView imageView, final int number){
		imageView.setOnClickListener(new OnClickListener() {
			
			private int myNumber = number;
			
			public void onClick(View v) {
				if (check >= correctOrder.size())
					return;
				
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
		if (soundManager != null)
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
		if (keyguard == null || wl == null)
			return;
		keyguard.reenableKeyguard();
		wl.release();
	}
}
