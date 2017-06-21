package ru.tohaman.rubicsguide.blind;

import android.support.v4.app.Fragment;

import ru.tohaman.rubicsguide.SingleFragmentActivity;

public class BlindMenuActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment () {
        return new BlindMenuFragment();
    }

}