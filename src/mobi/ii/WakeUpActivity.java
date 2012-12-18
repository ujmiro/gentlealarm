package mobi.ii;

import java.io.IOException;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class WakeUpActivity extends Activity {

	private WakeLock wl;
	private KeyguardLock keyguard;
	private MediaPlayer mediaPlayer;
	private int tempVolumValue;
	private AudioManager audioManager;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.wake_up_layout);
	    
		unlockScreean();
		startAlarm();
	}	
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		lockScreean();
		stopAlarm();
	}
	
	private void startAlarm(){
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE); 
		tempVolumValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) , AudioManager.FLAG_PLAY_SOUND);
		
		mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
        });
        mediaPlayer.setVolume(1, 1);
        
        AssetFileDescriptor afd;
		try {
			afd = getAssets().openFd("sound.mp3");
			mediaPlayer.setDataSource(afd.getFileDescriptor());
			mediaPlayer.prepare();
		
			//TODO: add chandling this exceptions
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        mediaPlayer.start();
	}
	
	private void stopAlarm(){
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, tempVolumValue , AudioManager.FLAG_PLAY_SOUND);
		mediaPlayer.release();
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
