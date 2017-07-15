package ru.tohaman.rubicsguide.listpager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import ru.tohaman.rubicsguide.R;

import java.util.List;

import static ru.tohaman.rubicsguide.g2f.G2FActivity.RubicPhase;

public class PagerActivity extends AppCompatActivity {

    private static final String EXTRA_ID = "ru.tohaman.rubicsguide.phase_id";

    private ViewPager mViewPager;
    private List<ListPager> mListPagers;


    public static Intent newIntenet(Context packageContext, String Id, String phase) {
        Intent intent = new Intent(packageContext, PagerActivity.class);
        intent.putExtra(EXTRA_ID, Id);
        intent.putExtra(RubicPhase, phase);
        return intent;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        final String Phase = getIntent().getStringExtra(RubicPhase);
        final String Id = getIntent().getStringExtra(EXTRA_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_pager_view_pager);

        mListPagers = ListPagerLab.get(this).getPhaseList(Phase);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem (int position) {
                ListPager listPager = mListPagers.get(position);
                return  PagerFragment.newInstance(String.valueOf(listPager.getId()), Phase);
            }

            @Override
            public int getCount() {
                return mListPagers.size();
            }
        });

        int mId = Integer.parseInt(Id);
        for (int i = 0; i < mListPagers.size(); i++) {
            //ищем номер этапа и передаем номер соответствующей страницы пейджер
            if (mListPagers.get(i).getId() == (mId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
