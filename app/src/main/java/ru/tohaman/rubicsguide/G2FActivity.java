package ru.tohaman.rubicsguide;

import android.support.v4.app.Fragment;

public class G2FActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment () {
        return new G2FFragment();
    }

}

