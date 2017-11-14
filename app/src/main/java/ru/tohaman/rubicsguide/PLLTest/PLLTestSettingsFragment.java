package ru.tohaman.rubicsguide.PLLTest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.tohaman.rubicsguide.CommentFragment;
import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.listpager.ListPagerLab;

import static ru.tohaman.rubicsguide.PLLTest.PLLTestFragment.sPLLTest_row;

/**
 * Created by Toha on 30.07.2017.
 */

public class PLLTestSettingsFragment extends Fragment {
    private List<ListPager> mListPagers = new ArrayList();
    private ListPagerLab listPagerLab;
    private ListPager mListPager;
    private SharedPreferences sp;
    private final String DIALOG_COMMENT = "DialogComment";  //в этой "паре", передаем значение комментария для редактирования
    private final int REQUEST_COMMENT = 0;
    private int guessRows; // количество строк с кнопками для вывода кнопок

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_pll_test_list, container, false);

        // начальная инициализация списка для ListView c адаптером MyListAdapter
        // получаем элемент ListView
        ListView mListView = (ListView) v.findViewById(R.id.main_listview2);
        final TextView guessRowsText = (TextView) v.findViewById(R.id.pll_test_textview_row);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Button plus_button = (Button) v.findViewById(R.id.pll_test_button_plus);
        plus_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                int i = Integer.parseInt(guessRowsText.getText().toString());
                i++;
                if (i>4) {
                    i = 4;
                } else {
                    guessRowsText.setText(String.valueOf(i));
                    SharedPreferences.Editor e = sp.edit();
                    e.putString(sPLLTest_row, String.valueOf(i*2));
                    e.commit(); // подтверждаем изменения

                }
            }
        });

        Button minus_button = (Button) v.findViewById(R.id.pll_test_button_minus);
        minus_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                int i = Integer.parseInt(guessRowsText.getText().toString());
                i--;
                if (i<1) {
                    i = 1;
                } else {
                    guessRowsText.setText(String.valueOf(i));
                    SharedPreferences.Editor e = sp.edit();
                    e.putString(sPLLTest_row, String.valueOf(i*2));
                    e.commit(); // подтверждаем изменения

                }
            }
        });

        // Получаем синглет
        listPagerLab = ListPagerLab.get(getActivity());
        mListPagers = listPagerLab.getPhaseList("PLLTEST");
        ListAdapter mListAdapter  = new PLLListAdapter(v.getContext(), R.layout.list_item, mListPagers);
        // устанавливаем адаптер
        mListView.setAdapter(mListAdapter);

        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //вызов окна редактирования комментария
                FragmentManager manager = getFragmentManager();
                int st = mListPagers.get(position).getIcon();
                String st2 = GetParamFromBase(mListPagers.get(position).getTitle());
                mListPager = mListPagers.get(position);
                PLLTest_SetName_Fragment dialog = PLLTest_SetName_Fragment.newInstance(String.valueOf(st),st2);
                dialog.setTargetFragment(PLLTestSettingsFragment.this, REQUEST_COMMENT);
                dialog.show (manager, DIALOG_COMMENT);
            }
        };
        mListView.setOnItemClickListener(itemListener);
        // Записываем количество строк в текст на экране
        String rows = sp.getString(sPLLTest_row, "6");
        guessRows = Integer.parseInt(rows) / 2;
        guessRowsText.setText(String.valueOf(guessRows));

        // возвращаем сформированный View в активность
        return v;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // возвращает массив целых числел, являющихся ссылками на ресурсы входящего массива (заданного тк же ссылкой)
    // т.е. на входе ссылка на <string-array name="ххх"> , а на выходе массив ссылок на элементы этого массива
    public int[] getResID(int mId) {
        TypedArray tArray = getContext().getResources().obtainTypedArray(mId);
        int count = tArray.length();
        int[] idx = new int[count];
        for (int i =0; i < idx.length; i++) {
            idx[i] = tArray.getResourceId(i,0);
        }
        tArray.recycle();
        return idx;
    }

    private class PLLListAdapter extends ArrayAdapter<ListPager> {

        public PLLListAdapter(Context context, int list_item, List<ListPager> listPlls) {
            super(context, list_item, listPlls);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) { convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);}

            ImageView icon = (ImageView) convertView.findViewById(R.id.list_item_image);
            TextView text = (TextView) convertView.findViewById(R.id.list_item_title_text);

            ListPager listPager = mListPagers.get(position);

            icon.setImageResource(listPager.getIcon());
            text.setText(GetParamFromBase(listPager.getTitle()));
            return convertView;
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        //Если все таки была нажата кнопка ОК
        if (requestCode == REQUEST_COMMENT) {
            // Получаем значение из EXTRA_Comment
            String string = (String) data.getSerializableExtra(CommentFragment.EXTRA_Comment);
            mListPager.setComment(string);
            ListPagerLab.get(getActivity()).updateListPager(mListPager);
        }
    }


    private String GetParamFromBase (String param) {
        String st = "";
        for (int i = 0; i < mListPagers.size(); i++) {
            if (mListPagers.get(i).getTitle().equals(param)) {
                st = GetNameFromListPagers(mListPagers, i);
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

    public static String GetNameFromListPagers(List<ListPager> ListPagers, int i) {
        String st;
        if (ListPagers.get(i).getComment().equals("")) {
            st = ListPagers.get(i).getTitle();
        } else {
            st = ListPagers.get(i).getComment();
        }
        return st;
    }

}

