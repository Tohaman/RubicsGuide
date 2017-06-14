package ru.tohaman.rubicsguide.about;

import android.support.v4.app.Fragment;

import ru.tohaman.rubicsguide.SingleFragmentActivity;

/**
 * Created by anton on 14.06.17.
 */

public class AboutActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new AboutFragment();
    }

}
