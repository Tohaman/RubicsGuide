package ru.tohaman.rubicsguide.g2f.f2l;

import android.content.Intent;
import android.support.v4.app.Fragment;

import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.SingleFragmentActivity;
import ru.tohaman.rubicsguide.g2f.G2FFragment;
import ru.tohaman.rubicsguide.listpager.ListActivity;

public class F2LActivity extends SingleFragmentActivity implements F2LFragment.Callbacks {
    public final static String RubicPhase = "ru.tohaman.rubicsguide.PHASE";
    Intent mIntent;

    @Override
    protected Fragment createFragment () {
        return new F2LFragment();
    }

    @Override
    protected int getLayoutResId () {
        return R.layout.activity_masterdetail;
    }   //стр.332

    @Override
    public void onF2LItemSelected(int id) {
        // вызываем активность List(RecycleView)->PagerView с параметром (ACCEL,PLL,OLL,CROSS и т.д. заданным в массиве строк g2f_phase)
        mIntent = new Intent(this,ListActivity.class);
        mIntent.putExtra(RubicPhase,getResources().getStringArray(R.array.f2l_list_phase)[id]);
        startActivity(mIntent);
    }
}

