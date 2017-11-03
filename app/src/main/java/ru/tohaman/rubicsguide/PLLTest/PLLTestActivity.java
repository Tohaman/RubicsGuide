package ru.tohaman.rubicsguide.PLLTest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.SingleFragmentActivity;

public class PLLTestActivity extends SingleFragmentActivity {
    private boolean preferencesChanged = true; // did preferences change?

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // register listener for SharedPreferences changes
//        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferencesChangeListener);

    }

    @Override
    protected Fragment createFragment() {
        return new PLLTestFragment();
    }
//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pll_test_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.plltest_settings:
                Intent mIntent = new Intent(this, PLLTestSettingsActivity.class);
                startActivity(mIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item) ;
        }
    }

    // Слушатель изменений в настройках программы
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                // вызывается когда изменены настройки
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    preferencesChanged = true; // user changed app setting

                    PLLTestFragment sFragment = (PLLTestFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                    if (key.equals("pll_test_row")){ // # of choices to display changed
                        sFragment.updateGuessRows(sharedPreferences);
                        sFragment.LoadNextPLL();
                    }

                    // Toast.makeText(this, R.string.restarting_app, Toast.LENGTH_SHORT).show();
                }

            };

}
