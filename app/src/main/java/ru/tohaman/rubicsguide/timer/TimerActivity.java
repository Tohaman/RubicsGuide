package ru.tohaman.rubicsguide.timer;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
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

        IntentFilter batteryIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryIntent = registerReceiver(null, batteryIntentFilter);

        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        if (status == BatteryManager.BATTERY_STATUS_CHARGING | status == BatteryManager.BATTERY_STATUS_FULL){
            // Если заряжается или заряжен на 100%, то не выключаем экран в данной активности.
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

}
