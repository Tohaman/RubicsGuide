package ru.tohaman.rubicsguide;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Toha on 30.07.2017.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
