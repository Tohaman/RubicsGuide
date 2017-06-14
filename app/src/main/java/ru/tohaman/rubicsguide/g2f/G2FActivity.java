package ru.tohaman.rubicsguide.g2f;

import android.support.v4.app.Fragment;

import ru.tohaman.rubicsguide.SingleFragmentActivity;

public class G2FActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment () {
        return new G2FFragment();
    }

}

