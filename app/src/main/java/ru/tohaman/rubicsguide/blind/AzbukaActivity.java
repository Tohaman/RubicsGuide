package ru.tohaman.rubicsguide.blind;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.SingleFragmentActivity;

public class AzbukaActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment () {
        return new AzbukaFragment();
    }

}