package ru.tohaman.rubicsguide.blind;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.tohaman.rubicsguide.CommentFragment;
import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.listpager.ListPagerLab;

import static ru.tohaman.rubicsguide.blind.ScambleFragment.Initialize;

/**
 * Created by anton on 25.06.17.
 */

public class AzbukaFragment extends Fragment {
    private TextView mAzbukaField;
    private MyGridAdapter mAdapter;
    private GridView mGridView;
    private List<CubeAzbuka> mGridList = new ArrayList();
    ListPagerLab listPagerLab;
    int[] cubeColor = new int[6];
    int red,blue,white,orange,green,yellow,back,black;

    private static final int REQUEST_AZBUKA = 0;
    private static final String DIALOG_AZBUKA = "DialogAzbuka";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_azbuka, container, false);

        back = ContextCompat.getColor(view.getContext(), R.color.transparent);
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
        // Немного преобразуем текст для корректного отображения.
        String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
        String description = String.format(text,getString(R.string.azbuka2));
        description = description.replace("%%", "%25");

        Spanned spanresult;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanresult = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY, imgGetter, null);
        } else {
            spanresult = Html.fromHtml(description, imgGetter, null);
        }

        mAzbukaField = (TextView) view.findViewById(R.id.azbuka_textView);
        mAzbukaField.setText(spanresult);
        mAzbukaField.setMovementMethod(LinkMovementMethod.getInstance());

        InitGridList();

        mGridView = (GridView) view.findViewById(R.id.azbuka_gridView);
        mAdapter = new MyGridAdapter(view.getContext(),R.layout.grid_item2,mGridList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mAzbukaField.setText("Выбранный элемент: " + mAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                mAzbukaField.setText("Ничего не выбрано");
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String letter = mAdapter.getItem(position);
                if (!(letter.equals("") | letter.equals("-"))){
                    FragmentManager manager = getFragmentManager();
                    InputLetterFragment dialog = InputLetterFragment.newInstance(mAdapter.getItem(position).toString(), position);
                    dialog.setTargetFragment(AzbukaFragment.this, REQUEST_AZBUKA);
                    dialog.show(manager, DIALOG_AZBUKA);
                }
            }
        });


        Button but_myazbuka = (Button) view.findViewById(R.id.button_myazbuka);
        but_myazbuka.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                String[] azbuka = listPagerLab.getMyAzbuka();
                listPagerLab.setCustomAzbuka(azbuka);
                InitGridList();
                mAdapter.notifyDataSetChanged();
            }
        });

        Button but_maxazbuka = (Button) view.findViewById(R.id.button_maxazbuka);
        but_maxazbuka.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                String[] azbuka = listPagerLab.getMaximAzbuka();
                listPagerLab.setCustomAzbuka(azbuka);
                InitGridList();
                mAdapter.notifyDataSetChanged();
            }
        });

        Button but_saveazbuka = (Button) view.findViewById(R.id.button_saveazbuka);
        but_saveazbuka.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listPagerLab.saveCustomAzbuka();
            }
        });

        Button but_loadazbuka = (Button) view.findViewById(R.id.button_loadazbuka);
        but_loadazbuka.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String[] azbuka = listPagerLab.loadCustomAzbuka();
                listPagerLab.setCustomAzbuka(azbuka);
                InitGridList();
                mAdapter.notifyDataSetChanged();
            }
        });



        return view;
    }

    // Обрабатываем результат вызова редактирования буквы азбуки
    // была ли нажата кнопка ОК, если да, то обновляем азбуку
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        //Если все таки была нажата кнопка ОК
        if (requestCode == REQUEST_AZBUKA) {
            // Получаем значение из диалога
            String letter = (String) data.getSerializableExtra(InputLetterFragment.EXTRA_Letter);
            int position = (int) data.getSerializableExtra(InputLetterFragment.EXTRA_Position);
            CubeAzbuka cubeAzbuka = mGridList.get(position);
            cubeAzbuka.setLetter(letter);
            mGridList.set(position,cubeAzbuka);
            mAdapter.notifyDataSetChanged();
            String[] azbuka = GetCurrentAzbuka();
            listPagerLab.setCustomAzbuka(azbuka);
        }
    }


    @SuppressWarnings("deprecation")
    private Html.ImageGetter imgGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            source = source.replace(".png", "");
            int resID = getResources().getIdentifier(source , "drawable", getActivity().getPackageName());
            //если картинка в drawable не найдена, то подсовываем заведомо существующую картинку
            if (resID == 0 ) {
                resID = getResources().getIdentifier("noscr" , "drawable", getActivity().getPackageName());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                drawable = getResources().getDrawable(resID, null);
            }else {
                drawable = getResources().getDrawable(resID);}

            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight());

            return drawable;
        }
    };

    private String[] GetCurrentAzbuka (){
        String[] azbuka = new String[54];
        for (int i = 0; i < 9; i++) {
            azbuka[i] = mGridList.get((i/3)*12+3+(i%3)).getLetter();
            azbuka[i+9] = mGridList.get((i/3+3)*12+(i%3)).getLetter();
            azbuka[i+18] = mGridList.get((i/3+3)*12+3+(i%3)).getLetter();
            azbuka[i+27] = mGridList.get((i/3+3)*12+6+(i%3)).getLetter();
            azbuka[i+36] = mGridList.get((i/3+3)*12+9+(i%3)).getLetter();
            azbuka[i+45] = mGridList.get((i/3+6)*12+3+(i%3)).getLetter();
        }
        return azbuka;
    }


    private void InitGridList() {
        if (mGridList.size()==0) {
            for (int i=0; i<108; i++) {
                mGridList.add(new CubeAzbuka(back, ""));
            }
        }

        int[] cube = new int[54];
        Initialize (cube);
        String[] azbuka = listPagerLab.getCustomAzbuka();

        for (int i = 0; i < 9; i++) {
            mGridList.set((i/3)*12+3+(i%3), new CubeAzbuka(cubeColor[cube[i]],azbuka [i]));
            mGridList.set((i/3+3)*12+(i%3), new CubeAzbuka(cubeColor[cube[i+9]],azbuka [i+9]));
            mGridList.set((i/3+3)*12+3+(i%3), new CubeAzbuka(cubeColor[cube[i+18]],azbuka [i+18]));
            mGridList.set((i/3+3)*12+6+(i%3), new CubeAzbuka(cubeColor[cube[i+27]],azbuka [i+27]));
            mGridList.set((i/3+3)*12+9+(i%3), new CubeAzbuka(cubeColor[cube[i+36]],azbuka [i+36]));
            mGridList.set((i/3+6)*12+3+(i%3), new CubeAzbuka(cubeColor[cube[i+45]],azbuka [i+45]));
        }

    }

}
