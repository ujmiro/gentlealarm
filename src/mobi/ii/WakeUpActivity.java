package mobi.ii;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

public class WakeUpActivity extends Activity {


		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.w("AlarmManager", "onCreate");
		super.onCreate(savedInstanceState);
	   setContentView(R.layout.wake_up);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.w("AlarmManager", "releasing");
	}
}
