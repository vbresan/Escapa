package biz.binarysolutions.escapa;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * 
 *
 */
public class SoundManager {
	
	private static final int MAX_STREAMS = 10;
	
	private static SoundManager instance = null; 
	
	private SoundPool soundPool;
	
	private float volume;
	private boolean shouldPlay = false;
	
	private int crashId;
	private int bounceId;
	
	/**
	 * @param context
	 * 
	 */
	private void setVolume(Context context) {
		
		AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		float currentVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);  
		float maxVolume     = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		volume = currentVolume / maxVolume;  		
	}

	/**
	 * 
	 */
	private void play(int soundId) {
		soundPool.play(soundId, volume, volume, 1, 0, 1f); 
	}

	/**
	 * @param context 
	 * 
	 */
	private SoundManager(Context context) {
		
		soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		
		crashId  = soundPool.load(context, R.raw.crash, 1);
		bounceId = soundPool.load(context, R.raw.bounce, 1);
		
		setVolume(context);
	}
	
	/**
	 * 
	 */
	public void playCrash() {
		
		if (shouldPlay) {
			play(crashId);
		}
		
	}
	
	/**
	 * 
	 */
	public void playBounce() {
		
		if (shouldPlay) {
			play(bounceId);
		}
	}

	/**
	 * 
	 * @param playSounds
	 */
	public void setShouldPlay(boolean shouldPlay) {
		this.shouldPlay = shouldPlay;
	}

	/**
	 * 
	 * @param context
	 */
	public static void createInstance(Context context) {

		if (instance == null) {
			instance = new SoundManager(context);
		}
	}

	/**
	 * 
	 * @return
	 */
	public static SoundManager getInstance() {
		return instance;
	}	
}
