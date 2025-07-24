package biz.binarysolutions.escapa;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.Random;

public class SoundManager {
	
	private static final int MAX_STREAMS = 10;
	
	private static SoundManager instance = null; 
	
	private final SoundPool soundPool;
	
	private float volume;
	private boolean shouldPlay = false;
	
	private final int crashId;
	private final int bounceId;
	
	private void setVolume(Context context) {
		
		AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		float currentVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);  
		float maxVolume     = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		volume = currentVolume / maxVolume;  		
	}

	private void play(int soundId) {
		float rate = 0.95f + (1.05f - 0.95f) * new Random().nextFloat();
		soundPool.play(soundId, volume, volume, 1, 0, rate);
	}

	private SoundManager(Context context) {

		if (Build.VERSION.SDK_INT >= 21) {
			soundPool = new SoundPool.Builder().setMaxStreams(MAX_STREAMS).build();
		} else {
			soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		}

		crashId  = soundPool.load(context, R.raw.crash, 1);
		bounceId = soundPool.load(context, R.raw.bounce, 1);
		
		setVolume(context);
	}
	
	public void playCrash() {
		
		if (shouldPlay) {
			play(crashId);
		}
	}
	
	public void playBounce() {
		
		if (shouldPlay) {
			play(bounceId);
		}
	}

	public void setShouldPlay(boolean shouldPlay) {
		this.shouldPlay = shouldPlay;
	}

	public static void createInstance(Context context) {

		if (instance == null) {
			instance = new SoundManager(context);
		}
	}

	public static SoundManager getInstance() {
		return instance;
	}	
}
