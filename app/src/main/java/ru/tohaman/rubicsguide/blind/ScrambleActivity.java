package ru.tohaman.rubicsguide.blind;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.WindowManager;

import ru.tohaman.rubicsguide.SingleFragmentActivity;

public class ScrambleActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment () {
        return new ScambleFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean  sleep_scram_gen= sp.getBoolean("videoscreen_on", false);
        if (sleep_scram_gen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }  else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }

}