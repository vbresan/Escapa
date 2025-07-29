package biz.binarysolutions.escapa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		SoundManager.createInstance(this);

		new FlavorSpecific(this).initialize();
    }
    
    
	@Override
	protected void onResume() {
		super.onResume();
		
		SharedPreferences preferences = 
			PreferenceManager.getDefaultSharedPreferences(this);
		
		String key          = getString(R.string.preferences_playSounds_key);
		String defaultValue = getString(R.string.preferences_playSounds_default_value);
		
		boolean playSounds = preferences.getBoolean(key, Boolean.getBoolean(defaultValue));
		
		SoundManager soundManager = SoundManager.getInstance();
		if (soundManager != null) {
			soundManager.setShouldPlay(playSounds);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	 	if (item.getItemId() == R.id.menuItemSettings) {
	    	startActivity(new Intent(this, PreferencesActivity.class));
	    	return true;
	    }
		
		return super.onOptionsItemSelected(item);
	}
}