package ru.tohaman.rubicsguide.listpager;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.SingleFragmentActivity;

/**
 * Created by Toha on 09.04.2017.
 */

public class ListActivity extends SingleFragmentActivity implements ListFragment.Callbacks{
    @Override
    protected Fragment createFragment() {
        return new ListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onItemSelected(ListPager listPager) {
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
}
