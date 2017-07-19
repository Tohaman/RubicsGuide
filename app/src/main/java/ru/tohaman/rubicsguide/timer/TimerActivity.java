package ru.tohaman.rubicsguide.timer;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.tohaman.rubicsguide.MainFragment;
import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.SingleFragmentActivity;

public class TimerActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment () {
        return new TimerFragment();
    }

}
