package ru.tohaman.rubicsguide.blind;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.SingleFragmentActivity;

public class ScrambleActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment () {
        return new ScambleFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Не выключаем экран в данной активности.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


}