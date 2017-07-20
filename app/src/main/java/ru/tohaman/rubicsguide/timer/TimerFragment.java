package ru.tohaman.rubicsguide.timer;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
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
    protected static final String TAG = "GestureDetectorMain";
    private GestureDetector mGestureDetector;

    //TODO Сделать, чтобы на данной активности экран не гас.

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int sotki = (int) (millis % 1000) / 10;             // сотые доли секунды
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

        mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //Log.i(TAG, "onDoubleTap");
                timerTextView.setText(R.string.init_start_time);
                return true;
            }
        });

        LinearLayout mRightHand = (LinearLayout) view.findViewById(R.id.rigth_hand);
        LinearLayout mLeftHand = (LinearLayout) view.findViewById(R.id.left_hand);
        mLeftLight = (View) view.findViewById(R.id.left_light);
        mRightLight = (View) view.findViewById(R.id.right_light);

        // Обработка прикосновения к левому лэйауту
        mLeftHand.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                int action = event.getActionMasked();
//                Log.d(TAG, String.valueOf(action));
                leftHandDown = OnTouchAction(leftHandDown, rightHandDown, action, mLeftLight);
//                Log.d(TAG, String.valueOf(leftHandDown));
                return true;
            }
        });

        // Обработка прикосновения к правому лэйауту
        mRightHand.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                int action = event.getActionMasked();
//                Log.d(TAG, String.valueOf(action));
                rightHandDown = OnTouchAction(rightHandDown, leftHandDown, action, mRightLight);
//                Log.d(TAG, String.valueOf(rightHandDown));
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


    private boolean OnTouchAction(boolean action_hand, boolean second_hand, int action, View handLight) {
        if (action == MotionEvent.ACTION_DOWN){  //если что-то нажато (только первое нажатие)
            action_hand = true;
            handLight.setBackground(getDrawable(R.drawable.green_oval));
            if (second_hand){     //если обе руки положили (action_hand = true), то что-то делаем, если нет, ждем вторую руку
                if (!timerStart){              // если таймер не запущен, ставим признак, что таймер готов к запуску
                    timerReady = true;
                } else {                        // если запущен, то останавливаем таймер
                    StopTimer();
                    timerStart = false;
                }
            }
        }
        if (action == MotionEvent.ACTION_UP){    // Если убрали все пальцы с лэйаута (отпустили)
            action_hand = false;                    // поставили, что текущая рука убрана
            handLight.setBackground(getDrawable(R.drawable.red_oval));  //окрасили кружок красным
            if (timerReady){                           // если была готовность таймер, то его запустили
                StartTimer();
                timerStart = true;                      // поставили признак, что таймер запущен
                timerReady = false;                     // сняли признак, что готов к старту
            }
        }
        return action_hand;                             // вернули текущее значение активной руки
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
        } else {
            drawable = getResources().getDrawable(ResID);
        }
        return drawable;
    }

}
