package ru.tohaman.rubicsguide.g2f;

import android.content.Intent;
import android.support.v4.app.Fragment;

import ru.tohaman.rubicsguide.plltestgame.PLLTestActivity;
import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.SingleFragmentActivity;
import ru.tohaman.rubicsguide.listpager.ListActivity;

public class G2FActivity extends SingleFragmentActivity implements G2FFragment.Callbacks {
    public final static String RubicPhase = "ru.tohaman.rubicsguide.PHASE";
    Intent mIntent;

    @Override
    protected Fragment createFragment () {
        return new G2FFragment();
    }

    @Override
    protected int getLayoutResId () {
        return R.layout.activity_masterdetail;
    }   //стр.332

    @Override
    public void onG2FItemSelected(int id) {
        // вызываем активность List(RecycleView)->PagerView с параметром (ACCEL,PLL,OLL,CROSS и т.д. заданным в массиве строк g2f_phase)
        String st = getResources().getStringArray(R.array.g2f_phase)[id];
        switch (st.toUpperCase()) {
            case "PLLTEST":
                Intent mIntent2 = new Intent(this, PLLTestActivity.class);
                startActivity(mIntent2);
                break;
            default:
                mIntent = new Intent(this, ListActivity.class);
                mIntent.putExtra(RubicPhase, st);
                startActivity(mIntent);
        }
    }
}

