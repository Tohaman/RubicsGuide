package ru.tohaman.rubicsguide.timer;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.tohaman.rubicsguide.CommentFragment;
import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.listpager.PagerFragment;

import static android.content.ContentValues.TAG;

/**
 * Created by anton on 19.07.17.
 */

public class TimerFragment extends Fragment {
    TextView timerTextView;
    long startTime = 0;
    boolean leftHandDown = false;
    boolean rightHandDown = false;
    boolean timerReady = false;
    boolean timerStart = false;
    View mLeftLight;
    View mRightLight;

    //TODO Сделать новую картинку для таймера

    //TODO Сделать, чтобы на данной активности экран не гас.

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int sotki = (int) (millis%1000)/10;             // сотые доли секунды
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            timerTextView.setText(String.format("%d:%02d:%02d", minutes, seconds, sotki));
            timerHandler.postDelayed(this, 50);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        timerTextView = (TextView) view.findViewById(R.id.texttime);
        timerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                timerTextView.setText(R.string.init_start_time);
            }
        });

        LinearLayout mRightHand = (LinearLayout) view.findViewById(R.id.rigth_hand);
        LinearLayout mLeftHand = (LinearLayout) view.findViewById(R.id.left_hand);
        mLeftLight = (View) view.findViewById(R.id.left_light);
        mRightLight = (View) view.findViewById(R.id.right_light);

        // Обработка прикосновения к левому лэйауту
        mLeftHand.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event) {
                int action = event.getActionMasked();
                leftHandDown = OnTouchAction(leftHandDown,action,mLeftLight);
                timerReady = leftHandDown & rightHandDown;
                return true;
            }
        });

        // Обработка прикосновения к правому лэйауту
        mRightHand.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event) {
                int action = event.getActionMasked();
         //       Log.d(TAG, String.valueOf(action));
                rightHandDown = OnTouchAction(rightHandDown,action,mRightLight);
                timerReady = leftHandDown & rightHandDown;
                return true;
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    private boolean OnTouchAction (boolean hand, int action, View handLight) {
        if (action == MotionEvent.ACTION_DOWN) {  //
            hand = true;
            handLight.setBackground(getDrawable(R.drawable.green_oval));
            if (leftHandDown & rightHandDown) {     //если обе руки положили, то останавливаем таймер, если нет, ждем вторую руку
                timerStart = false;
                StopTimer();
            }
        }
        if (action == MotionEvent.ACTION_UP) {    // Если убрали все пальцы с лэйаута (отпустили)
            hand = false;
            handLight.setBackground(getDrawable(R.drawable.red_oval));
            if (timerReady) {
                StartTimer();
                timerStart = true;
            }
        }
        return hand;
    }


    private void StartTimer() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void StopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }


    @SuppressWarnings("deprecation")
    private Drawable getDrawable(int ResID) {
        Drawable drawable = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            drawable = getResources().getDrawable(ResID, null);
        }else {
            drawable = getResources().getDrawable(ResID);
        }
        return drawable;
    }

}
