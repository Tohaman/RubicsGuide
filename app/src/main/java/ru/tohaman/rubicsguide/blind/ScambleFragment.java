package ru.tohaman.rubicsguide.blind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import ru.tohaman.rubicsguide.CommentFragment;
import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.listpager.ListPagerLab;


/**
 * Created by Toha on 21.06.2017.
 */

public class ScambleFragment extends Fragment {
    Intent mIntent;
    GridLayout mGridLayout;
    TextView solvetext,ScrambleLength,Scramble;
    int red,blue,white,orange,green,yellow,back,black;
    int[] CompleteCube = new int[54];
    int[] viewCube = new int[108];
    int[] cubeColor = new int[6];
    int[] MainRebro = new int[66];
    int[] DopRebro = new int[54];
    int[] MainUgol = new int[66];
    int[] DopUgol = new int[54];
    int[] SpisReber = new int[24];
    int[] SpisUglov = new int[24];
    LinearLayout[] mLinearLayouts = new LinearLayout[108];
    LinearLayout[] mLinearLayouts1 = new LinearLayout[108];
    final Random random = new Random();
    List<ListPager> mListPagers;
    ListPagerLab listPagerLab;

    private static final int REQUEST_SCRAMBLE = 0;
    private static final String DIALOG_SCRAMBLE = "DialogScramble";

    public ScambleFragment (){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scramble_gen, container, false);

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

        // Получаем синглет
        listPagerLab = ListPagerLab.get(getActivity());
        mGridLayout = (GridLayout) view.findViewById(R.id.grid);
        mListPagers = ListPagerLab.get(getActivity()).getPhaseList("SCRAMBLEGEN");
        InitArrays();
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
                mIntent = new Intent(getActivity(), AzbukaActivity.class);
                startActivity(mIntent);
            }
        });

        Button reset_button = (Button) view.findViewById(R.id.button_reset);
        reset_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                Initialize(CompleteCube);
                solvetext.setText("Решение: ");
                cube2view();
                }
            });

        Button gen_button = (Button) view.findViewById(R.id.button_generate);
        gen_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                Initialize(CompleteCube);
                GenerateScramble();
                BlindMoves.Scram(CompleteCube,String.valueOf(Scramble.getText()));
                cube2view();
                SetParamToBase("Scramble", Scramble.getText().toString());
            }
        });

        Button do_button = (Button) view.findViewById(R.id.button_do);
        do_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                BlindMoves.Scram(CompleteCube,String.valueOf(Scramble.getText()));
                cube2view();
            }
        });

        Button minus_button = (Button) view.findViewById(R.id.button_minus);
        minus_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                int i = Integer.parseInt(ScrambleLength.getText().toString());
                i--;
                ScrambleLength.setText(String.valueOf(i));
                cube2view();
            }
        });

        ScrambleLength = (TextView) view.findViewById(R.id.scrambleLength);
        ScrambleLength.setText(GetParamFromBase("ScrambleLength"));

        Button plus_button = (Button) view.findViewById(R.id.button_plus);
        plus_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                int i = Integer.parseInt(ScrambleLength.getText().toString());
                i++;
                ScrambleLength.setText(String.valueOf(i));
                cube2view();
            }
        });


        Scramble = (TextView) view.findViewById(R.id.scramble);
        Scramble.setText(GetParamFromBase("Scramble"));
        Scramble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                FragmentManager manager = getFragmentManager();
                CommentFragment dialog = CommentFragment.newInstance(String.valueOf(Scramble.getText()), getResources().getString(R.string.scramble));
                dialog.setTargetFragment(ScambleFragment.this, REQUEST_SCRAMBLE);
                dialog.show (manager, DIALOG_SCRAMBLE);
            }
        });


        Button gran_button = (Button) view.findViewById(R.id.button_gran);
        gran_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                int a = CompleteCube[23] + 1;       //смотрим что в буфере ребер
                int b = CompleteCube[30] + 1;
                int c = MainRebro[(a*10)+b];
                BufferRebroSolve(CompleteCube, c);
                cube2view();
            }
        });

        Button ugol_button = (Button) view.findViewById(R.id.button_ugol);
        ugol_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                int a = CompleteCube[18] + 1;       //смотрим что в буфере углов
                int b = CompleteCube[11] + 1;
                int c = MainUgol[(a*10)+b];
                BufferUgolSolve(CompleteCube, c);
                cube2view();

            }
        });

        solvetext = (TextView) view.findViewById(R.id.solve_text);
        solvetext.setText("Решение: ");
        return view;

    }

    // Обрабатываем результат вызова редактирования скрамбла
    // была ли нажата кнопка ОК, если да, то обновляем скрамбл
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        //Если все таки была нажата кнопка ОК
        if (requestCode == REQUEST_SCRAMBLE) {
            // Получаем значение из EXTRA_Comment (прописан в CommentFragment)
            String string = (String) data.getSerializableExtra(CommentFragment.EXTRA_Comment);
            Scramble.setText(string);               //обновляем текст во фрагменте
            SetParamToBase("Scramble", string);     //сохраняем в базу
        }
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

    private void InitArrays () {        //Инициализируем таблицы соответствий
        for (int i = 0; i < 66; i++) {
            MainRebro[i] = 0;
        }
        //Создаем табличку номеров основных ребер, для определенных сочетаний цветов, остальные элементы равны 0
        MainRebro[12] = 3;      //для сине-оранжевого ребра
        MainRebro[13] = 7;      //для сине-белого ребра
        MainRebro[14] = 5;      //для сине-красного ребра
        MainRebro[15] = 1;      //для сине-желтого ребра
        MainRebro[21] = 10;     //для оранжево-синей ребра
        MainRebro[23] = 14;     //для оранжево-белого ребра
        MainRebro[25] = 12;     //для оранжево-желтого ребра
        MainRebro[26] = 16;     //для оранжево-зеленого ребра
        MainRebro[31] = 19;     //для бело-синей ребра
        MainRebro[32] = 21;     //для бело-оранжевого ребра
        MainRebro[34] = 23;     //для бело-красного ребра
        MainRebro[36] = 25;     //для бело-зеленого ребра
        MainRebro[41] = 28;     //для красно-синей ребра
        MainRebro[43] = 30;     //для красно-белого ребра
        MainRebro[45] = 32;     //для красно-желтого ребра
        MainRebro[46] = 34;     //для красно-зеленого ребра
        MainRebro[51] = 37;     //для желто-синей ребра
        MainRebro[52] = 41;     //для желто-оранжевого ребра
        MainRebro[54] = 39;     //для желто-красного ребра
        MainRebro[56] = 43;     //для желто-зеленого ребра
        MainRebro[62] = 48;     //для зелено-оранжевого ребра
        MainRebro[63] = 46;     //для зелено-белого ребра
        MainRebro[64] = 50;     //для зелено-красного ребра
        MainRebro[65] = 52;     //для зелено-желтого ребра
        for (int i = 0; i < 54; i++) {
            DopRebro[i] = 0;
        }
        //Создаем табличку соответствия основного цвета и дополнительного цвета ребра [где искать второй цвет)
        DopRebro[1] = 37;           //сине-желтое
        DopRebro[3] = 10;           //сине-оранжевое
        DopRebro[5] = 28;           //сине-красное
        DopRebro[7] = 19;           //сине-белое
        DopRebro[10] = 3;           //оранжево-синяя
        DopRebro[12] = 41;          //оранжево-желтое
        DopRebro[14] = 21;          //оранжево-белое
        DopRebro[16] = 48;          //оранжево-зеленое
        DopRebro[19] = 7;           //бело-синяя
        DopRebro[21] = 14;          //бело-оранжевое
        DopRebro[23] = 30;          //бело-красное
        DopRebro[25] = 46;          //бело-зеленое
        DopRebro[28] = 5;           //красно-синяя
        DopRebro[30] = 23;          //красно-белое
        DopRebro[32] = 39;          //красно-желтое
        DopRebro[34] = 50;          //красно-зеленое
        DopRebro[37] = 1;           //желто-синяя
        DopRebro[39] = 32;          //желто-красное
        DopRebro[41] = 12;          //желто-оранжевое
        DopRebro[43] = 52;          //желто-зеленое
        DopRebro[46] = 25;          //зелено-белое
        DopRebro[48] = 16;          //зелено-оранжевое
        DopRebro[50] = 34;          //зелено-красное
        DopRebro[52] = 43;          //зелено-желтое

        for (int i = 0; i < 54; i++) {
            MainUgol[i] = 0;
        }
        //Создаем табличку номеров основных углов, для определенных сочетаний цветов (по цвету его место)
        MainUgol[12] = 0;      //для сине-оранжево-желтого угла
        MainUgol[13] = 6;      //для сине-бело-оранжевого угла
        MainUgol[14] = 8;      //для сине-красно-белого угла
        MainUgol[15] = 2;      //для сине-желто-красного угла
        MainUgol[21] = 11;     //для оранжево-сине-белого угла
        MainUgol[23] = 17;     //для оранжево-бело-зеленого угла
        MainUgol[25] = 9;     //для оранжево-желто-синего угла
        MainUgol[26] = 15;     //для оранжево-зелено-желтого угла
        MainUgol[31] = 20;     //для бело-сине-красного угла
        MainUgol[32] = 18;     //для бело-оранжево-синего угла
        MainUgol[34] = 26;     //для бело-красно-зеленого угла
        MainUgol[36] = 24;     //для бело-зелено-оранжевого угла
        MainUgol[41] = 29;     //для красно-сине-желтого угла
        MainUgol[43] = 27;     //для красно-бело-синего угла
        MainUgol[45] = 35;     //для красно-желто-зеленого угла
        MainUgol[46] = 33;     //для красно-зелено-белого угла
        MainUgol[51] = 38;     //для желто-сине-оранжевого угла
        MainUgol[52] = 44;     //для желто-оранжево-зеленого угла
        MainUgol[54] = 36;     //для желто-красно-синего угла
        MainUgol[56] = 42;     //для желто-зелено-красного угла
        MainUgol[62] = 45;     //для зелено-оранжево-белого угла
        MainUgol[63] = 47;     //для зелено-бело-красного угла
        MainUgol[64] = 53;     //для зелено-красно-желтого угла
        MainUgol[65] = 51;     //для зелено-желто-оранжевого угла

        for (int i = 0; i < 54; i++) {
            DopUgol[i] = 0;
        }
        //Создаем табличку соответствия основного и дополнительного угла [где искать второй цвет]
        //углы рассматриваем по часовой стрелке, поэтому достаточно первых двух цветов, чтобы пределить угол
        DopUgol[0] = 9;       //сине-оранжево-желтый Л
        DopUgol[2] = 36;       //сине-желто-красный К
        DopUgol[6] = 18;       //сине-бело-оранжевый М
        DopUgol[8] = 27;       //сине-красно-белый И
        DopUgol[9] = 38;      //оранжево-желто-синий Р
        DopUgol[11] = 6;       //оранжево-сине-белый Н
        DopUgol[15] = 51;      //оранжево-зелено-желтый П
        DopUgol[17] = 24;      //оранжево-бело-зеленый О
        DopUgol[18] = 11;      //бело-оранжево-синий А
        DopUgol[20] = 8 ;      //бело-сине-красный Б
        DopUgol[24] = 45;      //бело-зелено-оранжевый Г
        DopUgol[26] = 33;      //бело-красно-зеленый В
        DopUgol[27] = 22;      //красно-бело-синяя Ф
        DopUgol[29] = 2 ;      //красно-сине-желтая У
        DopUgol[33] = 47;      //красно-зелено-белая С
        DopUgol[35] = 42;      //красно-желто-зеленая Т
        DopUgol[36] = 29;      //желто-красно-синяя Ц
        DopUgol[38] = 0 ;      //желто-сине-оранжевая Х
        DopUgol[42] = 53;      //желто-зелено-красная Ч
        DopUgol[44] = 15;      //желто-оранжево-зеленая Ш
        DopUgol[45] = 17;      //зелено-оранжево-белая Д
        DopUgol[47] = 26;      //зелено-бело-красная Е
        DopUgol[51] = 44;      //зелено-желто-оранжевая З
        DopUgol[53] = 35;      //зелено-красно-желтая Ж
    }

    private int[] BufferRebroSolve (int[] cube, int c) {
        if (!(c==23 | c == 30)) {           //если с != 23 или 30, то буфер не на месте, и добоавляем букву к решению
            solvetext.setText(solvetext.getText() + FindLetter(c) + " ");
        }
        switch (c) {
            case 1:
                BlindMoves.Blinde1 (cube);
                break;
            case 3:
                BlindMoves.Blinde3 (cube);
                break;
            case 5:
                BlindMoves.Blinde5 (cube);
                break;
            case 7:
                BlindMoves.Blinde7 (cube);
                break;
            case 10:
                BlindMoves.Blinde10 (cube);
                break;
            case 12:
                BlindMoves.Blinde12 (cube);
                break;
            case 14:
                BlindMoves.Blinde14 (cube);
                break;
            case 16:
                BlindMoves.Blinde16 (cube);
                break;
            case 19:
                BlindMoves.Blinde19 (cube);
                break;
            case 21:
                BlindMoves.Blinde21 (cube);
                break;
            case 23:                      // для бело-красного ребра
                if (!CheckRebro(cube)) {
                    int i = 0;
                    do {
                        i++;
                    } while (SpisReber[i] != 0);
                    c = SpisReber[i-1];
                    if (c == 30) { c = SpisReber[i-2];}
                    if (c == 23) { c = SpisReber[i-3];}
                    BufferRebroSolve(cube,c);
                } else {
                    //Если все ребра на месте, то преобразуем буквы в слова
                }
                break;
            case 25:
                BlindMoves.Blinde25 (cube);
                break;
            case 28:
                BlindMoves.Blinde28 (cube);
                break;
            case 30:                        //для красно-белого ребра
                if (!CheckRebro(cube)) {
                    int i = 0;
                    do {
                        i++;
                    } while (SpisReber[i] != 0);
                    c = SpisReber[i-1];
                    if (c == 30) { c = SpisReber[i-2];}
                    if (c == 23) { c = SpisReber[i-3];}
                    BufferRebroSolve(cube,c);
                }
                break;
            case 32:
                BlindMoves.Blinde32 (cube);
                break;
            case 34:
                BlindMoves.Blinde34 (cube);
                break;
            case 37:
                BlindMoves.Blinde37 (cube);
                break;
            case 39:
                BlindMoves.Blinde39 (cube);
                break;
            case 41:
                BlindMoves.Blinde41 (cube);
                break;
            case 43:
                BlindMoves.Blinde43 (cube);
                break;
            case 46:
                BlindMoves.Blinde46 (cube);
                break;
            case 48:
                BlindMoves.Blinde48 (cube);
                break;
            case 50:
                BlindMoves.Blinde50 (cube);
                break;
            case 52:
                BlindMoves.Blinde52 (cube);
                break;
            default:
                Toast.makeText(getView().getContext(),"Странное ребро в буфере",Toast.LENGTH_SHORT).show();
        }
        return cube;
    }


    private Boolean CheckRebro (int[] cube){    //проверяем все ли грани на своих местах
        Boolean Check = true;           //предположим что все на местах

        for (int i = 0; i<24; i++) {    //Обнуляем список ребер на местах
            SpisReber[i] = 0;
        }
        int j = 0;
        for (int i =0; i<53; i++) {
            if (DopRebro[i] != 0) {
                int a = cube[i] + 1;
                int b = cube[DopRebro[i]] + 1;
                int c = ((a*10) + b);
                if (MainRebro[c] != i) {
                    SpisReber[j] = i;
                    j++;
                    Check = false;
                }
            }
        }
        return Check;
    }

    private int[] BufferUgolSolve (int[] cube, int c) {
        if (!(c==18 || c == 11 || c == 6)) {           //если с не равно 18,11 или 6, то буфер не на месте и добавляем букву к решению.
            solvetext.setText(solvetext.getText() + FindLetter(c) + " ");
        }
        switch (c) {
            case 0:
                BlindMoves.Blinde0 (cube);
                break;
            case 2:
                BlindMoves.Blinde2 (cube);
                break;
            case 6:
                if (!CheckUgol(cube)) {
                    int i = 0;
                    do {                //то ищем угол с макимальным номером не на своем месте
                        i++;            //т.е. в приоритет граней такой: зеленая, желтая, красная, белая, оранжевая, синяя
                    } while (SpisUglov[i] != 0);
                    c = SpisUglov[i-1];
                    if (c == 18) { c = SpisUglov[i-2];}
                    if (c == 11) { c = SpisUglov[i-3];}
                    if (c == 6) { c = SpisUglov[i-3];}
                    BufferUgolSolve(cube,c);
                } else {
                    //Если все ребра на месте, то преобразуем буквы в слова
                }
                break;
            case 8:
                BlindMoves.Blinde8 (cube);
                break;
            case 9:
                BlindMoves.Blinde9 (cube);
                break;
            case 11:
                if (!CheckUgol(cube)) {
                    int i = 0;
                    do {                //то ищем угол с макимальным номером не на своем месте
                        i++;            //т.е. в приоритет граней такой: зеленая, желтая, красная, белая, оранжевая, синяя
                    } while (SpisUglov[i] != 0);
                    c = SpisUglov[i-1];
                    if (c == 18) { c = SpisUglov[i-2];}
                    if (c == 11) { c = SpisUglov[i-3];}
                    if (c == 6) { c = SpisUglov[i-3];}
                    BufferUgolSolve(cube,c);
                } else {
                    //Если все ребра на месте, то преобразуем буквы в слова
                }
                break;
            case 15:
                BlindMoves.Blinde15 (cube);
                break;
            case 17:
                BlindMoves.Blinde17 (cube);
                break;
            case 18:
                if (!CheckUgol(cube)) {
                    int i = 0;
                    do {                //то ищем угол с макимальным номером не на своем месте
                        i++;            //т.е. в приоритет граней такой: зеленая, желтая, красная, белая, оранжевая, синяя
                    } while (SpisUglov[i] != 0);
                    c = SpisUglov[i-1];
                    if (c == 18) { c = SpisUglov[i-2];}
                    if (c == 11) { c = SpisUglov[i-3];}
                    if (c == 6) { c = SpisUglov[i-3];}
                    BufferUgolSolve(cube,c);
                } else {
                    //Если все ребра на месте, то преобразуем буквы в слова
                }
                break;
            case 20:
                BlindMoves.Blinde20 (cube);
                break;
            case 24:
                BlindMoves.Blinde24 (cube);
                break;
            case 26:
                BlindMoves.Blinde26 (cube);
                break;
            case 27:
                BlindMoves.Blinde27 (cube);
                break;
            case 29:
                BlindMoves.Blinde29 (cube);
                break;
            case 33:
                BlindMoves.Blinde33 (cube);
                break;
            case 35:
                BlindMoves.Blinde35 (cube);
                break;
            case 36:
                BlindMoves.Blinde36 (cube);
                break;
            case 38:
                BlindMoves.Blinde38 (cube);
                break;
            case 42:
                BlindMoves.Blinde42 (cube);
                break;
            case 44:
                BlindMoves.Blinde44 (cube);
                break;
            case 45:
                BlindMoves.Blinde45 (cube);
                break;
            case 47:
                BlindMoves.Blinde47 (cube);
                break;
            case 51:
                BlindMoves.Blinde51 (cube);
                break;
            case 53:
                BlindMoves.Blinde53 (cube);
                break;
            default:
                Toast.makeText(getView().getContext(),"Страннай угол в буфере",Toast.LENGTH_SHORT).show();
        }
        return cube;
    }


    private Boolean CheckUgol (int[] cube) {    //проверяем все ли углы на своих местах
        Boolean Check = true;           //предположим что все на местах
        for (int i = 0; i<24; i++) {    //Обнуляем список углов на местах
            SpisUglov[i] = 0;
        }
        int j = 0;
        for (int i =0; i<53; i++) {
            if (DopUgol[i] != 0) {
                int a = cube[i] + 1;
                int b = cube[DopUgol[i]] + 1;
                int c = ((a*10) + b);
                if (MainUgol[c] != i) {
                    SpisUglov[j] = i;
                    j++;
                    Check = false;
                }
            }
        }
        return Check;
    }

    private String FindLetter (int c) {     //Доработать функцию поиска буквы из азбуки, пока просто цифра
        String[] azbuka = listPagerLab.getCustomAzbuka();
        return azbuka[c];
    }

    private String GetParamFromBase (String param) {
        String st = "";
        for (int i = 0; i < mListPagers.size(); i++) {
            if (mListPagers.get(i).getTitle().equals(param)) {
                if (mListPagers.get(i).getComment().equals("")) {
                    st = mListPagers.get(i).getUrl();
                } else {
                    st = mListPagers.get(i).getComment();
                }
            }
        }
        return st;
    }

    private void SetParamToBase (String param, String value) {
        for (int i = 0; i < mListPagers.size(); i++) {
            if (mListPagers.get(i).getTitle().equals(param)) {
                mListPagers.get(i).setComment(value);
                ListPagerLab.get(getActivity()).updateListPager(mListPagers.get(i));
            }
        }
    }

}
