package ru.tohaman.rubicsguide.basiclist;

import android.support.v4.app.Fragment;

import ru.tohaman.rubicsguide.SingleFragmentActivity;

/**
 * Created by Toha on 09.04.2017.
 */

public class BasicListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new BasicListFragment();
    }
}
