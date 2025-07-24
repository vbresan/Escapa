package biz.binarysolutions.escapa;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 
 *
 */
public class PreferencesActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportFragmentManager()
			.beginTransaction()
				.replace(android.R.id.content, new PreferencesFragment())
					.commit();
	}
}
