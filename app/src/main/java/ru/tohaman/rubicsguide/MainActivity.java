package ru.tohaman.rubicsguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.Toast;

import ru.tohaman.rubicsguide.about.AboutActivity;
import ru.tohaman.rubicsguide.about.AboutFragment;
import ru.tohaman.rubicsguide.blind.BlindMenuActivity;
import ru.tohaman.rubicsguide.g2f.G2FActivity;
import ru.tohaman.rubicsguide.listpager.ListActivity;
import ru.tohaman.rubicsguide.listpager.ListFragment;
import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.timer.TimerActivity;

public class MainActivity extends SingleFragmentActivity implements MainFragment.Callbacks, ListFragment.Callbacks {
    private Intent mIntent;
    private String phase;


    @Override
    protected Fragment createFragment () {
        return new MainFragment();
    }

    @Override
    protected int getLayoutResId () {
        return R.layout.activity_maindetail;
    }

    @Override
    // действие для большого (правого) фрагмента двухфрагментной активности
    public void onItemSelected(ListPager listPager) {
        Toast.makeText(this,listPager.getDescription(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            phase = savedInstanceState.getString("phase");
        } else {
            phase = "ABOUT";
        }

    }

    @Override
    public boolean onMyOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_settings:
                Intent mIntent = new Intent(this, SettingsActivity.class);
                startActivity(mIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    // действие для левого фрагмента двухфрагментной активности или основного фрагмента однофрагментной
    public void onMainItemSelected (int id) {
        phase = getResources().getStringArray(R.array.main_phase)[id];
        switch (phase) {
            case "BEGIN":
                mIntent = ListActivity.newIntenet(this, phase);
                startActivity(mIntent);
                break;

            case "G2F":
                mIntent = new Intent(this, G2FActivity.class);
                startActivity(mIntent);
                break;

            case "BLIND":
                mIntent = new Intent(this,BlindMenuActivity.class);
                startActivity(mIntent);
                break;

            case "BASIC":
                if (findViewById(R.id.detail_fragment_container) == null) {
                    // для телефона вызываем новую активность
                    mIntent = ListActivity.newIntenet(this, phase);
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

            case "ABOUT":
                if (findViewById(R.id.detail_fragment_container) == null) {
                    // для телефона вызываем новую активность
                    mIntent = new Intent(this, AboutActivity.class);
                    startActivity(mIntent);
                    break;
                } else {
                    // для планшета открываем в правом фрагменте
                    Fragment newDetail = new AboutFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detail_fragment_container, newDetail)
                            .commit();
                }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("phase",phase);
    }

    @Override
    protected void onStart () {
        super.onStart();
        if (!phase.equals("BASIC")) { phase="ABOUT"; }
        if (findViewById(R.id.detail_fragment_container) != null) {
            if (phase.equals("ABOUT")) {
                Fragment newDetail = new AboutFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, newDetail)
                        .commit();
            } else { // Если не About, значит открываем окно
                Fragment newDetail = ListFragment.newInstance(phase);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, newDetail)
                        .commit();
            }
        }
    }

}
