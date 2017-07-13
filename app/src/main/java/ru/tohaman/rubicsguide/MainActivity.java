package ru.tohaman.rubicsguide;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment () {
        return new MainFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_maindetail;
    }

}
