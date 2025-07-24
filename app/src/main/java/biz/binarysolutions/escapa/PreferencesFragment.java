package biz.binarysolutions.escapa;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceFragmentCompat;

public class PreferencesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences
        (
            @Nullable Bundle savedInstanceState,
            @Nullable String rootKey
        ) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    /**
     * This code blob is here just to prevent preferences being shown under the
     * title bar. Thanks to Android for enforcing edge-to-edge layout.
     */
    @Override
    public void onViewCreated
        (
            @NonNull View view,
            @Nullable Bundle savedInstanceState
        ) {
        super.onViewCreated(view, savedInstanceState);

        View listView = getListView();

        ViewCompat.setOnApplyWindowInsetsListener(listView, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }
}
