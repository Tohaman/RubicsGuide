package ru.tohaman.rubicsguide.timer;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.tohaman.rubicsguide.R;

/**
 * Created by anton on 19.07.17.
 */

public class TimerFragment extends Fragment {
    TextView timerTextView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int sotki = (int) (millis%1000)/10;
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

        LinearLayout mRightHand = (LinearLayout) view.findViewById(R.id.rigth_hand);
        LinearLayout mLeftHand = (LinearLayout) view.findViewById(R.id.left_hand);

        mRightHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                timerHandler.removeCallbacks(timerRunnable);
            }
        });

        mLeftHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
            }
        });

//        StartStopButton = (Button) view.findViewById(R.id.button_start);
//        StartStopButton.setText("start");
//        StartStopButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Button b = (Button) v;
//                if (b.getText().equals("stop")) {
//                    timerHandler.removeCallbacks(timerRunnable);
//                    b.setText("start");
//                } else {
//                    startTime = System.currentTimeMillis();
//                    timerHandler.postDelayed(timerRunnable, 0);
//                    b.setText("stop");
//                }
//            }
//        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
//        StartStopButton.setText("start");
    }


}
