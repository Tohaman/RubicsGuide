package ru.tohaman.rubicsguide.blind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.about.AboutActivity;
import ru.tohaman.rubicsguide.g2f.G2FActivity;
import ru.tohaman.rubicsguide.listpager.ListActivity;
import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.listpager.MyListAdapter;

import static ru.tohaman.rubicsguide.g2f.G2FFragment.RubicPhase;
import static ru.tohaman.rubicsguide.listpager.ListPagerLab.getResID;

/**
 * Created by Toha on 21.06.2017.
 */

public class BlindMenuFragment extends Fragment {
    Intent mIntent;
    public final static String RubicPhase = "ru.tohaman.rubicsguide.PHASE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mainscreen, container, false);

        // начальная инициализация списка для ListView c адаптером MyListAdapter
        // получаем элемент ListView
        ListView mListView = (ListView) v.findViewById(R.id.main_listview);

        // создаем адаптер и задаем массивы к адаптеру
        List<ListPager> mListPagers = new ArrayList();
        String[] mTitles = getResources().getStringArray(R.array.blind_menu_title);
        int [] resID = getResID(R.array.blind_menu_icon);
        for (int i = 0; i < mTitles.length; i++) {
            mListPagers.add (new ListPager("BLINDMENU", i+1, mTitles[i], resID[i],""));
        }
        ListAdapter mListAdapter  = new MyListAdapter(v.getContext(), R.layout.list_item, mListPagers);
        // устанавливаем адаптер
        mListView.setAdapter(mListAdapter);

        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String phase = getResources().getStringArray(R.array.blind_menu_phase)[position];
                switch (phase) { //задаем переменые для каждого этапа
                    case "BLIND":
                        mIntent = new Intent(getActivity(),ListActivity.class);
                        mIntent.putExtra(RubicPhase,phase);
                        break;
                    case "BLINDACC":
                        mIntent = new Intent(getActivity(),ListActivity.class);
                        mIntent.putExtra(RubicPhase,phase);
                        break;
                    case "SCRAMBLE":
                        mIntent = new Intent(getActivity(), ScrambleActivity.class);
                        break;
                }
                startActivity(mIntent);

            }
        };
        mListView.setOnItemClickListener(itemListener);

        return v;
    }



}