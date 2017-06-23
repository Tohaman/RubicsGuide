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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import ru.tohaman.rubicsguide.R;


/**
 * Created by Toha on 21.06.2017.
 */

public class ScambleFragment extends Fragment {
    Intent mIntent;
    GridLayout mGridLayout;
    EditText Scramble,ScrambleLength;
    int red,blue,white,orange,green,yellow,back,black;
    int[] CompleteCube = new int[54];
    int[] viewCube = new int[108];
    int[] cubeColor = new int[6];
    LinearLayout[] mLinearLayouts = new LinearLayout[108];
    LinearLayout[] mLinearLayouts1 = new LinearLayout[108];
    final Random random = new Random();

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
        Initialize(CompleteCube);

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

        cube2view();    //переносим куб на gridlayout

        Button azb_button = (Button) view.findViewById(R.id.button_azbuka);
        azb_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия кнопки Азбука
                // вызов активности редактирования азбуки
                // пока "едем в Лондон"
                BasicMoves.MoveF (CompleteCube);
                cube2view();
            }
        });

        Button reset_button = (Button) view.findViewById(R.id.button_reset);
        reset_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                Initialize(CompleteCube);
                cube2view();
                }
            });

        Button gen_button = (Button) view.findViewById(R.id.button_generate);
        gen_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                GenerateScramble();
                BlindMoves.Scram(CompleteCube,String.valueOf(Scramble.getText()));
                cube2view();
            }
        });

        ScrambleLength = (EditText) view.findViewById(R.id.scrambleLength);
        ScrambleLength.setText("14");
        Scramble = (EditText) view.findViewById(R.id.scramble);

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

        //задаем элементам gridlayout цвет заданный в массиве viewCube
        for (int i = 0; i < 108; i++) {
            mLinearLayouts[i].setBackgroundColor(viewCube[i]);
            if (viewCube[i] == back) {
                mLinearLayouts1[i].setBackgroundColor(back);
            } else {
                mLinearLayouts1[i].setBackgroundColor(black);
            }
        }
    }

    public static int[] Initialize (int[] cube) {
        for (int i = 0 ; i < cube.length; i++) {
            cube[i] = (i / 9);
        }
        return cube;
    }

    private void addViewToGrid(GridLayout field, View view) {
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.width  = 0;
        lp.height = 0;                                                  //ViewGroup.LayoutParams.WRAP_CONTENT MATCH_PARENT
        lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED    , 1f);  // позиция и вес кнопки по горизонтали
        lp.rowSpec    = GridLayout.spec(GridLayout.ALIGN_MARGINS, 1f);  // позиция и вес кнопки по вертикали
        field.addView(view, lp);
    }

    private String GenerateScramble (){
        String scramble = " ";
        String old_scarmble = scramble;
        int i = 1;
        int a = 0;
        int b = 0;
        boolean c = false;

        do {
            a = random.nextInt(6) + 1;                      //генерируем число от 1 до 6
            if (a != b) {                                   //если предыдущее значение не равно текущему
                i++;
                old_scarmble = scramble;
                if (random.nextInt(2) == 1) {                   // 50/50 ход будет по часовой или против
                    scramble = scramble + a + " ";              //если по часовой то просто добавим число
                } else {
                    scramble = scramble + "-" + a + " ";        //если против часовой то добавим число с минусом
                }
                b = a;
                c = false;
            } else {                                            //если перыдущее равно текущему
                if (!c) {                                       //если предыдущий ход не двойной
                    scramble = old_scarmble + a + "2 " ;        //то пишем двойной
                    c = true;                                   //ставим признак, что был двойной
                    b = a;
                    old_scarmble = scramble;
                }
            }
        } while (i <= Integer.parseInt(String.valueOf(ScrambleLength.getText())));
        scramble = scramble.replace(" 1"," R");
        scramble = scramble.replace(" 2"," U");
        scramble = scramble.replace(" 3"," F");
        scramble = scramble.replace(" 4"," L");
        scramble = scramble.replace(" 5"," D");
        scramble = scramble.replace(" 6"," B");
        scramble = scramble.replace(" -1"," R'");
        scramble = scramble.replace(" -2"," U'");
        scramble = scramble.replace(" -3"," F'");
        scramble = scramble.replace(" -4"," L'");
        scramble = scramble.replace(" -5"," D'");
        scramble = scramble.replace(" -6"," B'");
        scramble = scramble.replaceFirst(" ","");
        Scramble.setText(scramble);
        return scramble;
    }



}
