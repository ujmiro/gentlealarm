package mobi.ii;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

public class WakeUpActivity extends Activity {

	private WakeLock wl;
	private KeyguardLock keyguard;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.w("AlarmManager", "onCreate");
		super.onCreate(savedInstanceState);
	   setContentView(R.layout.wake_up);	    
		unlockScreean();
	}	

	@Override
	public void onDestroy(){
		super.onDestroy();
		lockScreean();
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
