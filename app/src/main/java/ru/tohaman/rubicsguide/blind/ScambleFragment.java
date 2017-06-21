package ru.tohaman.rubicsguide.blind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.tohaman.rubicsguide.R;

import static ru.tohaman.rubicsguide.blind.BasicMoves.MoveRb;

/**
 * Created by Toha on 21.06.2017.
 */

public class ScambleFragment extends Fragment {
    Intent mIntent;
    GridLayout mGridLayout;
    int red,blue,white,orange,green,yellow,back,black;
    int[] CompleteCube = new int[54];
    int[] viewCube = new int[108];
    int[] cubeColor = new int[6];
    LinearLayout[] mLinearLayouts = new LinearLayout[108];
    LinearLayout[] mLinearLayouts1 = new LinearLayout[108];
    public final static String RubicPhase = "ru.tohaman.rubicsguide.PHASE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.scramble_gen, container, false);

    back = ContextCompat.getColor(view.getContext(), R.color.gray);
    black = ContextCompat.getColor(view.getContext(), R.color.black);
    red = ContextCompat.getColor(view.getContext(), R.color.red);
    blue = ContextCompat.getColor(view.getContext(), R.color.blue);
    white = ContextCompat.getColor(view.getContext(), R.color.white);
    orange = ContextCompat.getColor(view.getContext(), R.color.orange);
    green = ContextCompat.getColor(view.getContext(), R.color.green);
    yellow = ContextCompat.getColor(view.getContext(), R.color.yellow);

    cubeColor[0] = blue;
    cubeColor[1] = orange;
    cubeColor[2] = white;
    cubeColor[3] = red;
    cubeColor[4] = yellow;
    cubeColor[5] = green;

    mGridLayout = (GridLayout) view.findViewById(R.id.grid);
        for (int i = 0 ; i < CompleteCube.length; i++) {
        CompleteCube[i] = (i / 9);
    }

        for (int i = 0; i < 108; i++) {
        View v = View.inflate(view.getContext(),R.layout.grid_item,null);
        TextView textView=(TextView) v.findViewById(R.id.grid_text);
        String st = String.valueOf(i%10);
        textView.setText("");
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.grid_layout);
        LinearLayout linearLayout1 = (LinearLayout) v.findViewById(R.id.grid_main_layout);
        linearLayout.setBackgroundColor(back);
        mLinearLayouts[i] = linearLayout;
        mLinearLayouts1[i] = linearLayout1;
        addViewToGrid(mGridLayout,v);
    }

    Button button = (Button) view.findViewById(R.id.button2);
        button.setText("Change");
        button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            // Обработка нажатия
            MoveRb (CompleteCube);
            cube2view();
        }
    });
    return view;
}

    private void cube2view () {
        //всем элемнтам задаем цвет фона
        for (int i = 0; i <108; i++) {
            viewCube[i] = back;
        }
        //задаем элементам grid (viewCube) цвета куба
        //grid это 108 квадратиков, а куб это 54 эемента
        for (int i = 0; i < 9; i++) {
            viewCube[(i/3)*12+3+(i%3)] = cubeColor[CompleteCube[i]];
            viewCube[(i/3+3)*12+(i%3)] = cubeColor[CompleteCube[i+9]];
            viewCube[(i/3+3)*12+3+(i%3)] = cubeColor[CompleteCube[i+18]];
            viewCube[(i/3+3)*12+6+(i%3)] = cubeColor[CompleteCube[i+27]];
            viewCube[(i/3+3)*12+9+(i%3)] = cubeColor[CompleteCube[i+36]];
            viewCube[(i/3+6)*12+3+(i%3)] = cubeColor[CompleteCube[i+45]];
        }
        for (int i = 0; i < 108; i++) {
            mLinearLayouts[i].setBackgroundColor(viewCube[i]);
            if (viewCube[i] == back) {
                mLinearLayouts1[i].setBackgroundColor(back);
            } else {
                mLinearLayouts1[i].setBackgroundColor(black);
            }
        }
    }

    private void addViewToGrid(GridLayout field, View view) {
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.width  = 0;
        lp.height = 0;//ViewGroup.LayoutParams.WRAP_CONTENT MATCH_PARENT
        lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED    , 1f);// позиция и вес кнопки по горизонтали
        lp.rowSpec    = GridLayout.spec(GridLayout.ALIGN_MARGINS, 1f);// позиция и вес кнопки по вертикали
        field.addView(view, lp);
    }
}
