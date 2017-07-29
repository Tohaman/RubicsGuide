package ru.tohaman.rubicsguide.timer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.WindowManager;

import ru.tohaman.rubicsguide.SingleFragmentActivity;

public class TimerActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment () {
        return new TimerFragment();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            // не выключаем экран в данной активности.
           getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

}
