package ru.tohaman.rubicsguide.blind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.SingleFragmentActivity;
import ru.tohaman.rubicsguide.listpager.ListActivity;
import ru.tohaman.rubicsguide.listpager.ListFragment;
import ru.tohaman.rubicsguide.timer.TimerActivity;

public class BlindMenuActivity extends SingleFragmentActivity implements BlindMenuFragment.Callbacks {
    Intent mIntent;
    public final static String RubicPhase = "ru.tohaman.rubicsguide.PHASE";

    @Override
    protected Fragment createFragment () {
        return new BlindMenuFragment();
    }

    @Override
    protected int getLayoutResId () {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onBlindItemSelected(int id) {
        String phase = getResources().getStringArray(R.array.blind_menu_phase)[id];
        switch (phase) { //задаем переменые для каждого этапа
            case "SCRAMBLE":
                if (findViewById(R.id.detail_fragment_container) == null) {
                    // для телефона вызываем новую активность
                    mIntent = new Intent(this, ScrambleActivity.class);
                    startActivity(mIntent);
                } else {
                    // для планшета открываем во фрагменте
                    Fragment newDetail = new ScambleFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detail_fragment_container, newDetail)
                            .commit();
                }
                break;

            default:
                mIntent = ListActivity.newIntenet(this, phase);
                startActivity(mIntent);
        }
    }

    @Override
    protected void onStart () {
        super.onStart();

        if (findViewById(R.id.detail_fragment_container) != null) {
            // Если активность двухфрагментная, то выводим во второй фрагмент генератор скрамблов
            // и устанавливаем гашение экрана в соответствии с настройкой программы

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            boolean  sleep_scram_gen= sp.getBoolean("sleep_scram_gen", false);
            if (sleep_scram_gen) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }  else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }

            Fragment newDetail = new ScambleFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }
}