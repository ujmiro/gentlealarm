package mobi.ii;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WakeUpReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context ctx, Intent inte) {
		Log.w("AlarmManager", "onReceive");
		Intent intent = new Intent(ctx, WakeUpActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(intent);
	}
}
