package ru.tohaman.rubicsguide.timer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.tohaman.rubicsguide.R;

/**
 * Created by anton on 19.07.17.
 */

public class TimerFragment extends Fragment {
    private TextView timerTextView, hintText1, hintText2, hintTextTime;
    long startTime = 0;
    boolean leftHandDown = false;
    boolean rightHandDown = false;
    boolean timerReady = false;
    boolean timerStart = false;
    int a;
    View mLeftLight;
    View mRightLight;
    private GestureDetector mGestureDetector;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences sp;
    ImageView hintLeftHand, hintRightHand;

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
            timerHandler.postDelayed(this, 20);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        timerTextView = (TextView) view.findViewById(R.id.texttime);

        mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                timerTextView.setText(R.string.init_start_time);    // по даблтапу обнуляем таймер
                return true;
            }
        });

        LinearLayout mRightHand = (LinearLayout) view.findViewById(R.id.rigth_hand);
        LinearLayout mLeftHand = (LinearLayout) view.findViewById(R.id.left_hand);
        mLeftLight = view.findViewById(R.id.left_light);
        mRightLight = view.findViewById(R.id.right_light);  // равносильно mRightLight = (View) view.findViewById(R.id.right_light);

        // Обработка прикосновения к левому лэйауту
        mLeftHand.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);           // даблтап
                int action = event.getActionMasked();
                leftHandDown = OnTouchAction(leftHandDown, rightHandDown, action, mLeftLight);
                return true;
            }
        });

        // Обработка прикосновения к правому лэйауту
        mRightHand.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);           // даблтап
                int action = event.getActionMasked();
                rightHandDown = OnTouchAction(rightHandDown, leftHandDown, action, mRightLight);
                return true;
            }
        });

        mConstraintLayout = (ConstraintLayout) view.findViewById(R.id.hint_background);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // проверяем, первый ли раз открывается таймер
        boolean hintTimer = sp.getBoolean("hint_Timer", true);
        if (hintTimer) { //если первый, то выводим подсказку
            hintText1 = (TextView) view.findViewById(R.id.hint_text);
            hintText2 = (TextView) view.findViewById(R.id.hint_text2);
            hintTextTime = (TextView) view.findViewById(R.id.hint_texttime);
            hintLeftHand = (ImageView) view.findViewById(R.id.hint_imageView);
            hintRightHand = (ImageView) view.findViewById(R.id.hint_imageView2);

            mConstraintLayout.setVisibility(View.VISIBLE);
            a = 0;
            hintText1.setVisibility(View.VISIBLE);
            hintTextTime.setVisibility(View.INVISIBLE);
            hintText2.setVisibility(View.INVISIBLE);
            hintRightHand.setVisibility(View.VISIBLE);
            hintLeftHand.setVisibility(View.VISIBLE);
            mConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (a == 0) {
                        hintText1.setVisibility(View.INVISIBLE);
                        hintText2.setVisibility(View.VISIBLE);
                        hintRightHand.setVisibility(View.INVISIBLE);
                        hintLeftHand.setVisibility(View.INVISIBLE);
                        hintTextTime.setVisibility(View.VISIBLE);
                        a++;
                    } else {
                        SharedPreferences.Editor e = sp.edit();
                        e.putBoolean("hint_Timer", false);
                        e.commit(); // подтверждаем изменения
                        mConstraintLayout.setVisibility(View.INVISIBLE);
                    }
                }
            });
        } else {    // если не первый, то убираем подсказку
            mConstraintLayout.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void onResume (){
        super.onResume();

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
