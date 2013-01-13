package mobi.ii;

import java.io.IOException;
import java.util.ArrayList;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class WakeUpActivity extends Activity {

	private WakeLock wl;
	private KeyguardLock keyguard;
	private MediaPlayer mediaPlayer;
	private int tempVolumValue;
	private AudioManager audioManager;
	private ArrayList<Integer> correctOrder = new ArrayList<Integer>();
	private int check = 0;
	private ArrayList<ImageView> images = new ArrayList<ImageView>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.wake_up_layout);
	    
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
	    
		unlockScreean();
		startAlarm();
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
				
				if (correctOrder.size() == check)
					finish();
			}
		});
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
