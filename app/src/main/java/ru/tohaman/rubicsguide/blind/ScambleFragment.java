package ru.tohaman.rubicsguide.blind;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.tohaman.rubicsguide.CommentFragment;
import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.listpager.ListPagerLab;

import static ru.tohaman.rubicsguide.blind.BlindMoves.Ekvator;


/**
 * Created by Toha on 21.06.2017. Фрагмент для отображения генератора скрамблов
 */

public class ScambleFragment extends Fragment {
    private Intent mIntent;

    private MyGridAdapter mAdapter;
    private List<CubeAzbuka> mGridList = new ArrayList<>();

    private TextView solvetext,ScrambleLength,Scramble,progressText;
    private CheckBox mChBoxRebro,mChBoxUgol,mChBoxSolve;
    private Button gen_button;
    private ProgressBar mProgressBar;
    private int back;
    private int[] cubeColor = new int[6];
    private int[] MainCube = new int [54];
    private int[] MainRebro = new int[66];
    private int[] DopRebro = new int[54];
    private int[] MainUgol = new int[66];
    private int[] DopUgol = new int[54];
    private int[] SpisReber = new int[25];
    private int[] SpisUglov = new int[25];
    private int[] RebroPriority = new int [11];
    private int[] UgolPriority = new int [7];
    private final Random random = new Random();
    private List<ListPager> mListPagers;
    private ListPagerLab listPagerLab;
    private String solve;

    private static final int REQUEST_SCRAMBLE = 0;
    private static final String DIALOG_SCRAMBLE = "DialogScramble";
    private static final String Scram_Param = "scram";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Uri uri = getActivity().getIntent().getData();

        View view = inflater.inflate(R.layout.fragment_scramble_gen, container, false);

        String scram;
        back = ContextCompat.getColor(view.getContext(), R.color.transparent);
        int black = ContextCompat.getColor(view.getContext(), R.color.black);
        int red = ContextCompat.getColor(view.getContext(), R.color.red);
        int blue = ContextCompat.getColor(view.getContext(), R.color.blue);
        int white = ContextCompat.getColor(view.getContext(), R.color.white);
        int orange = ContextCompat.getColor(view.getContext(), R.color.orange);
        int green = ContextCompat.getColor(view.getContext(), R.color.green);
        int yellow = ContextCompat.getColor(view.getContext(), R.color.yellow);

        cubeColor[0] = blue;
        cubeColor[1] = orange;
        cubeColor[2] = white;
        cubeColor[3] = red;
        cubeColor[4] = yellow;
        cubeColor[5] = green;


        // Инициализируем переменные
        listPagerLab = ListPagerLab.get(getContext());
        mListPagers = listPagerLab.getPhaseList("SCRAMBLEGEN");
        InitArrays();
        MainCube = Initialize();
        InitGridList(MainCube);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        progressText = (TextView) view.findViewById(R.id.progressText);
        progressText.setText(getString(R.string.scram_waiting));
        progressText.setBackgroundColor(white);
        progressText.setTextSize(12);
        progressText.setVisibility(View.INVISIBLE);

        GridView gridView = (GridView) view.findViewById(R.id.scram_gridView);
        mAdapter = new MyGridAdapter(view.getContext(),R.layout.grid_item2,mGridList);
        gridView.setAdapter(mAdapter);

        Button azb_button = (Button) view.findViewById(R.id.button_azbuka);
        azb_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обработка нажатия кнопки Азбука
                mIntent = new Intent(getActivity(), AzbukaActivity.class);
                startActivity(mIntent);
            }
        });

        gen_button = (Button) view.findViewById(R.id.button_generate);
        gen_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                GenerateScrambl();
            }
        });

        Button minus_button = (Button) view.findViewById(R.id.button_minus);
        minus_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                int i = Integer.parseInt(ScrambleLength.getText().toString());
                i--;
                if (i<1) {i = 1;}
                ScrambleLength.setText(String.valueOf(i));
                SetParamToBase("ScrambleLength",String.valueOf(i));
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
                if (i>30) {i = 30;}
                ScrambleLength.setText(String.valueOf(i));
                SetParamToBase("ScrambleLength",String.valueOf(i));
            }
        });

        //Поле Скрамбл и клик по этому полю

        Scramble = (TextView) view.findViewById(R.id.scramble);

        if (uri != null) {
            scram = uri.getQueryParameter(Scram_Param);
            scram = scram.replace("_"," ");
        } else {
            scram = GetParamFromBase("Scramble");
        }

        Scramble.setText(scram);
        Scramble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                FragmentManager manager = getFragmentManager();
                CommentFragment dialog = CommentFragment.newInstance(String.valueOf(Scramble.getText()), getResources().getString(R.string.scramble));
                dialog.setTargetFragment(ScambleFragment.this, REQUEST_SCRAMBLE);
                dialog.show (manager, DIALOG_SCRAMBLE);
            }
        });

        // чекбокс переплавки буфера ребер
        mChBoxRebro = (CheckBox) view.findViewById(R.id.checkBox_rebro);
        String a = GetParamFromBase("ChkBufRebro");
        if (a.equals("1")) {
            mChBoxRebro.setChecked(true);
        } else {
            mChBoxRebro.setChecked(false);
        }
        mChBoxRebro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SetParamToBase("ChkBufRebro", "1");
                } else {
                    SetParamToBase("ChkBufRebro", "0");
                }
            }
        });

        // чекбокс переплавки буфера углов
        mChBoxUgol = (CheckBox) view.findViewById(R.id.checkBox_ugol);
        if (GetParamFromBase("ChkBufUgol").equals("1")) {
            mChBoxUgol.setChecked(true);
        } else {
            mChBoxUgol.setChecked(false);
        }
        mChBoxUgol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SetParamToBase("ChkBufUgol", "1");
                } else {
                    SetParamToBase("ChkBufUgol", "0");
                }
            }
        });

        // чекбокс показывать или нет решения
        mChBoxSolve = (CheckBox) view.findViewById(R.id.checkBox_solve);
        if (GetParamFromBase("ChkSolve").equals("1")) {
            mChBoxSolve.setChecked(true);
        } else {
            mChBoxSolve.setChecked(false);
        }
        mChBoxSolve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String st = GetSolve(MainCube);
                    solvetext.setText(st);
                    SetParamToBase("ChkSolve", "1");    //поскольку данные храним в текством виде, то true = 1
                } else {
                    SetParamToBase("ChkSolve", "0");    //а false = 0;
                    String st = GetSolve(MainCube);
                    int a = st.split(" ").length;
                    solvetext.setText(String.valueOf(a));
                }
            }
        });

        MainCube = BlindMoves.Scram(MainCube, String.valueOf(Scramble.getText()));

        solvetext = (TextView) view.findViewById(R.id.solve_text);
        if(mChBoxSolve.isChecked()){
            solvetext.setText(GetSolve(MainCube));
        } else {
            String st = GetSolve(MainCube);
            solvetext.setText(String.valueOf(st.split(" ").length));
        }

        cube2view(MainCube);

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
            MainCube = BlindMoves.Scram(Initialize(), String.valueOf(Scramble.getText()));
            cube2view(MainCube);
            if(mChBoxSolve.isChecked()){
                solvetext.setText(GetSolve(MainCube));
            } else {
                String st = GetSolve(MainCube);
                solvetext.setText(String.valueOf(st.split(" ").length));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mChBoxSolve.isChecked()){
            solvetext.setText(GetSolve(MainCube));
        } else {
            String st = GetSolve(MainCube);
            solvetext.setText(String.valueOf(st.split(" ").length));
        }
    }

    private void cube2view (int[] cube) {
        InitGridList(cube);
        mAdapter.notifyDataSetChanged();
    }

    private void InitGridList(int[] cube) {
        // 108 элементов GridList делаем пустыми и прозрачными back = transparent
        if (mGridList.size()==0) {                  //только при первом запуске
            for (int i=0; i<108; i++) {
                mGridList.add(new CubeAzbuka(back, ""));
            }
        }

        // это если соберусь сделать элементы gridview кликабельными для задания скрамбла
        // String [] st = listPagerLab.getScrambleManagement();

        // если буква элемента = пробелу, то это элемент куба, если остается = "" то фона
        for (int i = 0; i < 9; i++) {
            mGridList.set((i/3)*12+3+(i%3), new CubeAzbuka(cubeColor[cube[i]]," "));
            mGridList.set((i/3+3)*12+(i%3), new CubeAzbuka(cubeColor[cube[i+9]]," "));
            mGridList.set((i/3+3)*12+3+(i%3), new CubeAzbuka(cubeColor[cube[i+18]]," "));
            mGridList.set((i/3+3)*12+6+(i%3), new CubeAzbuka(cubeColor[cube[i+27]]," "));
            mGridList.set((i/3+3)*12+9+(i%3), new CubeAzbuka(cubeColor[cube[i+36]]," "));
            mGridList.set((i/3+6)*12+3+(i%3), new CubeAzbuka(cubeColor[cube[i+45]]," "));
        }

    }


    public static int[] Initialize () {
        int[] cube = new int[54];
        for (int i = 0 ; i < cube.length; i++) {
            cube[i] = (i / 9);
        }
        return cube;
    }

    private String GetSolve (int[] maincube) {
        String st = "(";
        int [] cube = maincube.clone();
        do {
            int a = cube[23] + 1;       //смотрим что в буфере ребер
            int b = cube[30] + 1;
            int c = MainRebro[(a*10)+b];
            SolveCube sc = BufferRebroSolve(cube,c, st);
            st = sc.getSolve();
            cube = sc.getCube();
        } while (!CheckRebro(cube));

        st = st.trim();
        st = st + ") ";
        int j = st.split(" ").length;
        if (j%2 != 0) {
            st = st + "Эк ";
            cube = Ekvator(cube);
        }

        st = st + ("(");
        do {
            int a = cube[18] + 1;       //смотрим что в буфере углов
            int b = cube[11] + 1;
            int c = MainUgol[(a*10)+b];
            SolveCube sc = BufferUgolSolve(cube, c, st);
            st = sc.getSolve();
            cube = sc.getCube();
        } while (!CheckUgol(cube));

        st = st.trim();
        st = st + ") ";
        return st;
    }

    private String GenerateScrambleWithParam (boolean chRebro, boolean chUgol,int length) {
        String scramble;
        int[] CurCube = new int[54];
        int j = 0;                                  //счетчик количества попыток найти скрмбл
        boolean plavRebro;
        boolean plavUgol;
        boolean resault;

        do {
            CurCube = Initialize();
            j++;
            scramble = GenerateScramble(length);     //сгенерировать скрамбл длинны указанной в поле ScrambleLength
            BlindMoves.Scram(CurCube, scramble);
            plavRebro = false;
            plavUgol = false;
            resault = true;
            solve = "";
            do {
                int a = CurCube[23] + 1;       //смотрим что в буфере ребер
                int b = CurCube[30] + 1;
                int c = MainRebro[(a*10)+b];
                if ((c == 23)|(c == 30)) { plavRebro = true; }
                SolveCube sc = BufferRebroSolve(CurCube,c, solve);
                solve = sc.getSolve();
                CurCube = sc.getCube();
            } while (!CheckRebro(CurCube));

            int d = solve.split(" ").length;
            if (d%2 != 0) {
                solve = solve + "Эк ";
                CurCube = Ekvator(CurCube);
            }

            do {
                int a = CurCube[18] + 1;       //смотрим что в буфере углов
                int b = CurCube[11] + 1;
                int c = MainUgol[(a*10)+b];
                if ((c == 18)|(c == 11)|(c == 6)) { plavUgol = true; }
                SolveCube sc = BufferUgolSolve(CurCube, c, solve);
                solve = sc.getSolve();
                CurCube = sc.getCube();
            } while (!CheckUgol(CurCube));

            if (plavRebro && chRebro) { resault = false;}
            if (plavUgol && chUgol) { resault = false;}
        } while (!resault);
        //solve = solve + j;            //добавить к решению количество попыток решения
        return scramble;
    }


    private String GenerateScramble (int length){
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
        } while (i <= length);
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
        scramble = scramble.trim();                 //убираем лишние пробелы
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
            MainUgol[i] = 255;
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
            DopUgol[i] = 255;
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
        DopUgol[27] = 20;      //красно-бело-синяя Ф
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

        // Порядок поиска свободной корзины для переплавки ребра
        RebroPriority [0] = 21;     // в первую очередь проверяем не занята ли бело-оранжевое ребро
        RebroPriority [1] = 25;     // бело-зеленое
        RebroPriority [2] = 48;     // зелено-оранжевое
        RebroPriority [3] = 3;      // сине-оранжевое
        RebroPriority [4] = 41;     // желто-оранжевое
        RebroPriority [5] = 43;     // желто-зеленое
        RebroPriority [6] = 37;     // желто-синее
        RebroPriority [7] = 39;     // желто-красное
        RebroPriority [8] = 7;      // сине-белое
        RebroPriority [9] = 34;     // красно-зеленое
        RebroPriority [10] = 28;    // красно-синее

        // Порядок поиска свободной корзины для переплавки угла
        UgolPriority [0] = 26;     // в первую очередь проверяем не занят ли бело-красно-зеленый угол
        UgolPriority [1] = 44;     // желто-зелено-оранжевый
        UgolPriority [2] = 36;     // желто-красно-синий
        UgolPriority [3] = 42;     // желто-красно-зеленый
        UgolPriority [4] = 38;     // желто-сине-оранжевый
        UgolPriority [5] = 20;     // бело-сине-красный
        UgolPriority [6] = 24;     // бело-зелено-оранжевый
    }

    private SolveCube BufferRebroSolve (int[] cube, int c, String solv) {
        if (!(c==23 | c == 30)) {           //проверяем, не буфер ли?, если нет, то добоавляем букву к решению
            solv = solv + FindLetter(c) + " ";        //если буфер, то будем его переплавлять и букву уже
        }                                               //подставим в рекурсии
        switch (c) {
            case 1:
                cube = BlindMoves.Blinde1 (cube);
                break;
            case 3:
                cube = BlindMoves.Blinde3 (cube);
                break;
            case 5:
                cube = BlindMoves.Blinde5 (cube);
                break;
            case 7:
                cube = BlindMoves.Blinde7 (cube);
                break;
            case 10:
                cube = BlindMoves.Blinde10 (cube);
                break;
            case 12:
                cube = BlindMoves.Blinde12 (cube);
                break;
            case 14:
                cube = BlindMoves.Blinde14 (cube);
                break;
            case 16:
                cube = BlindMoves.Blinde16 (cube);
                break;
            case 19:
                cube = BlindMoves.Blinde19 (cube);
                break;
            case 21:
                cube = BlindMoves.Blinde21 (cube);
                break;
            case 23:                      // для бело-красного ребра
                if (!CheckRebro(cube)) {
                    c = 0;
                    // цикл поиска свободной корзины
                    for (int j = 0; c == 0; j++) {
                        int i = 0;
                        do {
                            if (RebroPriority[j] == SpisReber[i]) { c = RebroPriority[j]; } //ищем ребра на своем месте по приоритету RebroPriority
                            i++;
                        } while (SpisReber[i] != 0);
                    }
                    //переплавляем буфер (рекурсия)
                    SolveCube sc = BufferRebroSolve(cube, c, solv);
                    solv = sc.getSolve();
                    cube = sc.getCube();
                } else {
                    //Если все ребра на месте, то преобразуем буквы в слова
                }
                break;
            case 25:
                cube = BlindMoves.Blinde25 (cube);
                break;
            case 28:
                cube = BlindMoves.Blinde28 (cube);
                break;
            case 30:                        //для красно-белого ребра
                if (!CheckRebro(cube)) {
                    c = 0;
                    // цикл поиска свободной корзины
                    for (int j = 0; c == 0; j++) {
                        int i = 0;
                        do {
                            if (RebroPriority[j] == SpisReber[i]) { c = RebroPriority[j]; } //ищем ребра на своем месте по приоритету RebroPriority
                            i++;
                        } while (SpisReber[i] != 0);
                    }
                    //переплавляем буфер (рекурсия)
                    SolveCube sc = BufferRebroSolve(cube,c, solv);
                    solv = sc.getSolve();
                    cube = sc.getCube();
                }
                break;
            case 32:
                cube = BlindMoves.Blinde32 (cube);
                break;
            case 34:
                cube = BlindMoves.Blinde34 (cube);
                break;
            case 37:
                cube = BlindMoves.Blinde37 (cube);
                break;
            case 39:
                cube = BlindMoves.Blinde39 (cube);
                break;
            case 41:
                cube = BlindMoves.Blinde41 (cube);
                break;
            case 43:
                cube = BlindMoves.Blinde43 (cube);
                break;
            case 46:
                cube = BlindMoves.Blinde46 (cube);
                break;
            case 48:
                cube = BlindMoves.Blinde48 (cube);
                break;
            case 50:
                cube = BlindMoves.Blinde50 (cube);
                break;
            case 52:
                cube = BlindMoves.Blinde52 (cube);
                break;
            default:
                //Toast.makeText(getView().getContext(),"Странное ребро в буфере",Toast.LENGTH_SHORT).show();
        }
        return new SolveCube(cube,solv);
    }


    private Boolean CheckRebro (int[] cube){    //проверяем все ли грани на своих местах
        Boolean Check = true;           //предположим что все на местах

        for (int i = 0; i < 25; i++) {    //Обнуляем список ребер на местах
            SpisReber[i] = 0;
        }
        int j = 0;
        for (int i =0; i < 53; i++) {
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

    private SolveCube BufferUgolSolve (int[] cube, int c, String solv) {
        if (!(c==18 || c == 11 || c == 6)) {           //если с не равно 18,11 или 6, то буфер не на месте и добавляем букву к решению.
            solv = solv + FindLetter(c) + " ";
        }
        switch (c) {
            case 0:
                cube = BlindMoves.Blinde0 (cube);
                break;
            case 2:
                cube = BlindMoves.Blinde2 (cube);
                break;
            case 6:
                if (!CheckUgol(cube)) {
                    c = 0;
                    // цикл поиска свободной корзины
                    for (int j = 0; c == 0; j++) {
                        int i = 0;
                        do {
                            if (UgolPriority[j] == SpisUglov[i]) { c = UgolPriority[j]; } //ищем ребра на своем месте по приоритету RebroPriority
                            i++;
                        } while (SpisUglov[i] != 255);
                    }
                    //переплавляем буфер (рекурсия)
                    SolveCube sc = BufferUgolSolve(cube,c, solv);
                    solv = sc.getSolve();
                    cube = sc.getCube();
                }
                break;
            case 8:
                cube = BlindMoves.Blinde8 (cube);
                break;
            case 9:
                cube = BlindMoves.Blinde9 (cube);
                break;
            case 11:
                if (!CheckUgol(cube)) {
                    c = 0;
                    // цикл поиска свободной корзины
                    for (int j = 0; c == 0; j++) {
                        int i = 0;
                        do {
                            if (UgolPriority[j] == SpisUglov[i]) { c = UgolPriority[j]; } //ищем ребра на своем месте по приоритету RebroPriority
                            i++;
                        } while (SpisUglov[i] != 255);
                    }
                    SolveCube sc = BufferUgolSolve(cube,c, solv);
                    solv = sc.getSolve();
                    cube = sc.getCube();
                }
                break;
            case 15:
                cube = BlindMoves.Blinde15 (cube);
                break;
            case 17:
                cube = BlindMoves.Blinde17 (cube);
                break;
            case 18:
                if (!CheckUgol(cube)) {
                    c = 0;
                    // цикл поиска свободной корзины
                    for (int j = 0; c == 0; j++) {
                        int i = 0;
                        do {
                            if (UgolPriority[j] == SpisUglov[i]) { c = UgolPriority[j]; } //ищем ребра на своем месте по приоритету RebroPriority
                            i++;
                        } while (SpisUglov[i] != 255);
                    }
                    SolveCube sc = BufferUgolSolve(cube,c, solv);
                    solv = sc.getSolve();
                    cube = sc.getCube();
                }
                break;
            case 20:
                cube = BlindMoves.Blinde20 (cube);
                break;
            case 24:
                cube = BlindMoves.Blinde24 (cube);
                break;
            case 26:
                cube = BlindMoves.Blinde26 (cube);
                break;
            case 27:
                cube = BlindMoves.Blinde27 (cube);
                break;
            case 29:
                cube = BlindMoves.Blinde29 (cube);
                break;
            case 33:
                cube = BlindMoves.Blinde33 (cube);
                break;
            case 35:
                cube = BlindMoves.Blinde35 (cube);
                break;
            case 36:
                cube = BlindMoves.Blinde36 (cube);
                break;
            case 38:
                cube = BlindMoves.Blinde38 (cube);
                break;
            case 42:
                cube = BlindMoves.Blinde42 (cube);
                break;
            case 44:
                cube = BlindMoves.Blinde44 (cube);
                break;
            case 45:
                cube = BlindMoves.Blinde45 (cube);
                break;
            case 47:
                cube = BlindMoves.Blinde47 (cube);
                break;
            case 51:
                cube = BlindMoves.Blinde51 (cube);
                break;
            case 53:
                cube = BlindMoves.Blinde53 (cube);
                break;
            default:
            //    Toast.makeText(getView().getContext(),"Страннай угол в буфере",Toast.LENGTH_SHORT).show();
        }
        return new SolveCube(cube,solv);
    }


    private Boolean CheckUgol (int[] cube) {    //проверяем все ли углы на своих местах
        Boolean Check = true;           //предположим что все на местах
        for (int i = 0; i < 25; i++) {    //Обнуляем список углов на местах
            SpisUglov[i] = 255;
        }
        int j = 0;
        for (int i =0; i<53; i++) {
            if (DopUgol[i] != 255) {
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
        String[] azbuka = listPagerLab.getCurrentAzbuka();
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
                listPagerLab.updateListPager(mListPagers.get(i));
            }
        }
    }

    class SolveCube {
        int [] cube;    // куб [54]
        String solve;   // решение

        SolveCube(int [] cube, String solve){
            this.cube = cube;
            this.solve = solve;
        }

        public int[] getCube() {
            return cube;
        }

        public void setCube(int[] cube) {
            this.cube = cube;
        }

        public String getSolve() {
            return solve;
        }

        public void setSolve(String solve) {
            this.solve = solve;
        }
    }

    private void GenerateScrambl () {
        GenTask genTask = new GenTask();
        genTask.execute();
    }

    class GenTask extends AsyncTask<Void, Void, String> {
        Boolean ChReb, ChUgol;
        int lenScr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // берем собранный куб и обнуляем решение
            MainCube =Initialize();
            // выводим MainCube на экран
            cube2view(MainCube);
            solvetext.setText("");
            // делаем кнопку "Генерерировать" не активной, а прогресбар активным
            gen_button.setEnabled(false);
            mProgressBar.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);
            ChReb = mChBoxRebro.isChecked();
            ChUgol = mChBoxUgol.isChecked();
            lenScr = Integer.parseInt(String.valueOf(ScrambleLength.getText()));
        }

        @Override
        protected String doInBackground(Void... params) {
            String st = GenerateScrambleWithParam(ChReb,ChUgol,lenScr);
            return st;
        }

        @Override
        protected void onPostExecute(String st) {
            super.onPostExecute(st);
            //gen_button.setVisibility(View.VISIBLE);
            gen_button.setEnabled(true);
            mProgressBar.setVisibility(View.INVISIBLE);
            progressText.setVisibility(View.INVISIBLE);

            // выводим скрамбл на экран
            Scramble.setText(st);
            // перемешиваем куб по скрамблу
            BlindMoves.Scram(MainCube, st);
            // выводим решение или длинну решения на экран
            solve = GetSolve(MainCube);
            if (mChBoxSolve.isChecked()) {
                solvetext.setText(solve);
            } else {
                solvetext.setText(String.valueOf(solve.length()/2));
            }
            // выводим MainCube на экран
            cube2view(MainCube);
            // Сохраняем скрамбл в базе
            SetParamToBase("Scramble", st);
        }

        @Override
        protected void onProgressUpdate (Void... values) {
            super.onProgressUpdate(values);
        }

    }

}
