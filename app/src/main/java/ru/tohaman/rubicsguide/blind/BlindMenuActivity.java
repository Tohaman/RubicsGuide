package ru.tohaman.rubicsguide.blind;

import android.content.Intent;
import android.support.v4.app.Fragment;

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
                    Fragment newDetail = ListFragment.newInstance(phase);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detail_fragment_container, newDetail)
                            .commit();
                }
                break;

            case "TIMER":
                mIntent = new Intent(this, TimerActivity.class);
                startActivity(mIntent);
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
            Fragment newDetail = new ScambleFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }
}