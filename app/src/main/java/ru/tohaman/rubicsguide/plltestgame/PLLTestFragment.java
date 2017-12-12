package ru.tohaman.rubicsguide.plltestgame;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.listpager.ListPagerLab;


/**
 * Created by Toha on 29.10.2017.
 */

public class PLLTestFragment extends Fragment {
    public static final String sPLLTest_row = "pll_test_row";

    private SharedPreferences sp;
    private List<ListPager> mListPagers = new ArrayList<>();

    private int red,blue,white,orange,green,yellow,black;
    private int[] cubeColor = new int[8];
    private String[] pll = new String[21];
    private List<String> pllrnd = new ArrayList<>();
    private int guessRows; // количество строк с кнопками для вывода кнопок
    private int correctAnswer; // номер правильного алгоритма
    private ImageView img;
    private final Random random = new Random();
    private LinearLayout[] guessLinearLayouts; // строки с кнопками ответов

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        black = ContextCompat.getColor(getContext(), R.color.black);
        red = ContextCompat.getColor(getContext(), R.color.cube_red);
        blue = ContextCompat.getColor(getContext(), R.color.cube_blue);
        white = ContextCompat.getColor(getContext(), R.color.cube_white);
        orange = ContextCompat.getColor(getContext(), R.color.cube_orange);
        green = ContextCompat.getColor(getContext(), R.color.cube_green);
        yellow = ContextCompat.getColor(getContext(), R.color.cube_yellow);


        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        InitArrays();

        View v = inflater.inflate(R.layout.fragment_pll_test, container, false);

        img = v.findViewById(R.id.test_image);

        guessLinearLayouts = new LinearLayout[4];
        guessLinearLayouts[0] = v.findViewById(R.id.row1LinearLayout);
        guessLinearLayouts[1] = v.findViewById(R.id.row2LinearLayout);
        guessLinearLayouts[2] = v.findViewById(R.id.row3LinearLayout);
        guessLinearLayouts[3] = v.findViewById(R.id.row4LinearLayout);

        // configure listeners for the guess Buttons
        for (LinearLayout row : guessLinearLayouts) {
            for (int column = 0; column < row.getChildCount(); column++) {
                Button button = (Button) row.getChildAt(column);
                button.setOnClickListener(guessButtonListener);
            }
        }


        // считываем кол-во ответов в файле настроек и сохраняем значение defValue, если оно не было определено
        String rows = sp.getString(sPLLTest_row, "6");
        SharedPreferences.Editor e = sp.edit();
        e.putString(sPLLTest_row,rows);
        e.apply(); // подтверждаем изменения

        updateGuessRows(sp);
        LoadNextPLL();

        // Записываем количество строк в текст на экране
//        guessRowsText.setText(String.valueOf(guessRows));

        // возвращаем сформированный View в активность
        return v;
    }


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume (){
        super.onResume();
        updateGuessRows(sp);
        LoadNextPLL();
    }

    private void InitArrays () {
        cubeColor[0] = red;
        cubeColor[1] = green;
        cubeColor[2] = orange;
        cubeColor[3] = blue;
        cubeColor[4] = red;
        cubeColor[5] = green;
        cubeColor[6] = orange;
        cubeColor[7] = blue;

        pll[0] = "111232343424";    //смежные окошки
        pll[1] = "111242323434";    //противоположные окошки
        pll[2] = "121212343434";    //рельсы
        pll[3] = "131242313424";    //шахматы
        pll[4] = "131223412344";    //запад
        pll[5] = "111233422344";    //юг
        pll[6] = "111243432324";    //светофор
        pll[7] = "341224133412";    //австралия
        pll[8] = "411224132343";    //смежный треугольник
        pll[9] = "311223434142";    //противоположный треугольник
        pll[10] = "214123432341";    //черепаха
        pll[11] = "311224143432";    //угол
        pll[12] = "133422311244";    //правые кирпичи
        pll[13] = "113442331224";    //левые кирпичи
        pll[14] = "411232324143";    //убийца
        pll[15] = "141223432314";    //экватор
        pll[16] = "324141233412";    //встречная машинка
        pll[17] = "243412334121";    //попутная машинка
        pll[18] = "424142331213";    //ближняя улитка
        pll[19] = "242314133421";    //дальняя улитка
        pll[20] = "111223442334";    //север

        ListPagerLab listPagerLab = ListPagerLab.get();
        mListPagers = listPagerLab.getPhaseList("PLLTEST");

        pllrnd.clear();
        for (int i=0; i < mListPagers.size(); i++) {
            pllrnd.add(String.valueOf(i));
        }
    }

    private View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Button guessButton = ((Button) v);
        String guess = guessButton.getText().toString();
        if (guess.equals(PLLTestSettingsFragment.GetNameFromListPagers(mListPagers, correctAnswer))) {   //верный ответ
            LoadNextPLL();
        } else {    //неправильный ответ
            guessButton.setEnabled(false);
        }

        }
    };

    public void LoadNextPLL() {
        // выбираем случайный алгоритм
        correctAnswer = random.nextInt(21);
        // перемешиваем алгоритмы
        Collections.shuffle(pllrnd); // shuffle file names

        // находим наш случаный в перемешенном списке и помещаем его в конец списка
        int correct = pllrnd.indexOf(String.valueOf(correctAnswer));
        pllrnd.add(pllrnd.remove(correct));

        // add 2, 4, 6 or 8 кнопок в зависимости от значения guessRows
        // и заполняем эти кнопки случайными заведомо неверными названиями алгоритмов,
        // т.к. верное название у нас последнее в pllrnd (списке)
        for (int row = 0; row < guessRows; row++) {
            // place Buttons in currentTableRow
            for (int column = 0;
                 column < guessLinearLayouts[row].getChildCount();
                 column++) {
                // получить ссылку на Button для еонфигурации
                Button newGuessButton = (Button) guessLinearLayouts[row].getChildAt(column);
                newGuessButton.setEnabled(true);  // активируем кнопку
                // пишем текст а названием алгоритма на кнопку
                int numOfalg = Integer.parseInt(pllrnd.get((row * 2) + column));
                newGuessButton.setText(PLLTestSettingsFragment.GetNameFromListPagers(mListPagers,numOfalg));
            }
        }

        // заменяем случайную кнопку (текст) на правильный
        int row = random.nextInt(guessRows);
        int column = random.nextInt(2);
        LinearLayout randomRow = guessLinearLayouts[row]; // получить строку
        String AlgName = PLLTestSettingsFragment.GetNameFromListPagers(mListPagers, correctAnswer);
        ((Button) randomRow.getChildAt(column)).setText(AlgName);

//        SettingButton.setText(AlgName);
        Drawable drw0 = GenDrawable3sidePll(pll[correctAnswer]);
        img.setImageDrawable(drw0);
    }

    // update guessRows based on value in SharedPreferences
    public void updateGuessRows(SharedPreferences sharedPreferences) {
        // get the number of guess buttons that should be displayed
        String rows = sharedPreferences.getString(sPLLTest_row, "8");
        guessRows = Integer.parseInt(rows) / 2;

        // hide all quess button LinearLayouts
        for (LinearLayout layout : guessLinearLayouts)
            layout.setVisibility(View.GONE);

        // display appropriate guess button LinearLayouts
        for (int row = 0; row < guessRows; row++)
            guessLinearLayouts[row].setVisibility(View.VISIBLE);
    }

    private LayerDrawable GenDrawable3sidePll (String st_pll) {
        Drawable drw0 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_background);
        Drawable drw1 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_up);
        Drawable drw2 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_left);
        Drawable drw3 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_front);
        Drawable drw4 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_right);

        Drawable drw5 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_left_1);
        Drawable drw6 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_left_2);
        Drawable drw7 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_left_3);
        Drawable drw8 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_front_1);
        Drawable drw9 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_front_2);
        Drawable drw10 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_front_3);
        Drawable drw11 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_right_1);
        Drawable drw12 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_right_2);
        Drawable drw13 = ContextCompat.getDrawable(getContext(),R.drawable.z_3s_right_3);

        int a = random.nextInt(4);                      //генерируем число от 0 до 3 смещение цвета для боковых граней
        int b = random.nextInt(4);                      //генерируем число от 0 до 3 смещение цвета для 3-го этажа
        int c = random.nextInt(4);                      //генерируем число от 0 до 3 смещение грани для 3-го этажа

        String st = st_pll + st_pll;
        int[] cube3 = new int[9];

        for (int i = 0; i < 9; i++) {
            char temp = st.charAt(3*c+i);
            cube3[i] = Integer.parseInt(Character.toString(temp));
        }

        DrawableCompat.setTint(drw1, yellow);
        DrawableCompat.setTint(drw2, cubeColor[a]);
        DrawableCompat.setTint(drw3, cubeColor[1+a]);
        DrawableCompat.setTint(drw4, cubeColor[2+a]);

        DrawableCompat.setTint(drw5, cubeColor[cube3[0]+b]);
        DrawableCompat.setTint(drw6, cubeColor[cube3[1]+b]);
        DrawableCompat.setTint(drw7, cubeColor[cube3[2]+b]);
        DrawableCompat.setTint(drw8, cubeColor[cube3[3]+b]);
        DrawableCompat.setTint(drw9, cubeColor[cube3[4]+b]);
        DrawableCompat.setTint(drw10, cubeColor[cube3[5]+b]);
        DrawableCompat.setTint(drw11, cubeColor[cube3[6]+b]);
        DrawableCompat.setTint(drw12, cubeColor[cube3[7]+b]);
        DrawableCompat.setTint(drw13, cubeColor[cube3[8]+b]);

        Drawable drawableArray[] = new Drawable[] { drw0, drw1, drw2, drw3, drw4, drw5, drw6, drw7, drw8, drw9, drw10, drw11, drw12, drw13};
        return new LayerDrawable(drawableArray);
    }

    private LayerDrawable GenDrawable2sidePll (String st_pll) {

        int a = random.nextInt(4);                      //генерируем число от 0 до 3 смещение цвета для боковых граней
        int b = random.nextInt(4);                      //генерируем число от 0 до 3 смещение цвета для 3-го этажа
        int c = random.nextInt(4);                      //генерируем число от 0 до 3 смещение грани для 3-го этажа

        Drawable drw0 = ContextCompat.getDrawable(getContext(),R.drawable.z_cube_back_black);
        Drawable drw1 = ContextCompat.getDrawable(getContext(),R.drawable.z_cube_up_y);
        Drawable drw2 = ContextCompat.getDrawable(getContext(),R.drawable.z_cube_left_r);
        Drawable drw3 = ContextCompat.getDrawable(getContext(),R.drawable.z_cube_right_g);

        Drawable drw4 = ContextCompat.getDrawable(getContext(),R.drawable.z_3d_up_41_y);
        Drawable drw5 = ContextCompat.getDrawable(getContext(),R.drawable.z_3d_up_42_y);
        Drawable drw6 = ContextCompat.getDrawable(getContext(),R.drawable.z_3d_up_43_y);
        Drawable drw7 = ContextCompat.getDrawable(getContext(),R.drawable.z_3d_up_44_y);
        Drawable drw8 = ContextCompat.getDrawable(getContext(),R.drawable.z_3d_up_45_y);
        Drawable drw9 = ContextCompat.getDrawable(getContext(),R.drawable.z_3d_up_46_y);

        String st = st_pll + st_pll;
        int[] cube3 = new int[6];

        for (int i = 0; i < 6; i++) {
            char temp = st.charAt(3*c+i);
            cube3[i] = Integer.parseInt(Character.toString(temp));
        }

        DrawableCompat.setTint(drw1, yellow);
        DrawableCompat.setTint(drw2, cubeColor[a]);
        DrawableCompat.setTint(drw3, cubeColor[a+1]);

        DrawableCompat.setTint(drw4, cubeColor[cube3[0]+b]);
        DrawableCompat.setTint(drw5, cubeColor[cube3[1]+b]);
        DrawableCompat.setTint(drw6, cubeColor[cube3[2]+b]);
        DrawableCompat.setTint(drw7, cubeColor[cube3[3]+b]);
        DrawableCompat.setTint(drw8, cubeColor[cube3[4]+b]);
        DrawableCompat.setTint(drw9, cubeColor[cube3[5]+b]);

        Drawable drawableArray[] = new Drawable[] { drw0, drw1, drw2, drw3, drw4, drw5, drw6, drw7, drw8, drw9};

        return new LayerDrawable(drawableArray);
    }
}
