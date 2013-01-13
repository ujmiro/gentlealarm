package media.managers;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class SoundManager {

	private MediaPlayer mediaPlayer;
	private int tempVolumValue;
	private AudioManager audioManager;
	private Activity activity;
	
	public SoundManager(Activity activity){
		this.activity = activity;
	}
	
	public void startAlarm(int volume){
		audioManager = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE); 
		tempVolumValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volume / 100.) , AudioManager.FLAG_PLAY_SOUND);
		
		mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
        });
        mediaPlayer.setVolume(1, 1);
        mediaPlayer.setLooping(false);
        
        AssetFileDescriptor afd;
		try {
			afd = activity.getAssets().openFd("sound.mp3");
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
	
	public void stopAlarm(){
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, tempVolumValue , AudioManager.FLAG_PLAY_SOUND);
		mediaPlayer.release();
	}
}
