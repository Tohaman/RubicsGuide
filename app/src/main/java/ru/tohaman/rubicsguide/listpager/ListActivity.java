package ru.tohaman.rubicsguide.listpager;

import android.support.v4.app.Fragment;

import ru.tohaman.rubicsguide.SingleFragmentActivity;

/**
 * Created by Toha on 09.04.2017.
 */

public class ListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ListFragment();
    }
}
