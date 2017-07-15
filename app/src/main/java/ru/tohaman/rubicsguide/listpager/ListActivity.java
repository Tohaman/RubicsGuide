package ru.tohaman.rubicsguide.listpager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.SingleFragmentActivity;

import static ru.tohaman.rubicsguide.g2f.G2FActivity.RubicPhase;

/**
 * Created by Toha on 09.04.2017.
 */

public class ListActivity extends SingleFragmentActivity implements ListFragment.Callbacks{
    String Phase;

    public static Intent newIntenet(Context packageContext, String phase) {
        Intent intent = new Intent(packageContext, ListActivity.class);
        intent.putExtra(RubicPhase, phase);
        return intent;
    }

    @Override
    protected Fragment createFragment () {
        Phase = getIntent().getStringExtra(RubicPhase);
        return ListFragment.newInstance(Phase);
    }

    @Override
    protected int getLayoutResId () {
        return R.layout.activity_masterdetail;
    }   //стр.332

    @Override
    public void onItemSelected (ListPager listPager) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            if (listPager.getPhase().equals("BASIC")){
                Toast.makeText(this,listPager.getDescription(), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = PagerActivity.newIntenet(this, String.valueOf(listPager.getId()), listPager.getPhase());
                startActivity(intent);
            }
        } else {
            Fragment newDetail = PagerFragment.newInstance(String.valueOf(listPager.getId()), listPager.getPhase());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    protected void onStart () {
        super.onStart();
        if (findViewById(R.id.detail_fragment_container) != null) {
            Phase = getIntent().getStringExtra(RubicPhase);
            Fragment newDetail = PagerFragment.newInstance(String.valueOf(0), Phase);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }
}
