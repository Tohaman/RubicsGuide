package ru.tohaman.rubicsguide.plltestgame;

import android.support.v4.app.Fragment;

import ru.tohaman.rubicsguide.SingleFragmentActivity;

public class PLLTestSettingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PLLTestSettingsFragment();
    }
}
