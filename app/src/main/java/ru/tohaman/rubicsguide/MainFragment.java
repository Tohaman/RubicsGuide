package ru.tohaman.rubicsguide;

import android.content.Context;
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

import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.listpager.ListPagerLab;
import ru.tohaman.rubicsguide.listpager.MyListAdapter;

import static ru.tohaman.rubicsguide.listpager.ListPagerLab.getResID;

/**
 * Created by Toha on 12.06.2017.
 */

public class MainFragment extends Fragment {
    private Callbacks mCallbacks;

    //Обязательный интерфейс для активности-хоста
    public interface Callbacks {
        void onMainItemSelected (int id);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callbacks) {
            mCallbacks = (Callbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callbacks");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Инициализируем синглет ListPagerLab, в котором будет база.
        ListPagerLab listPagerLab = ListPagerLab.get(getActivity());

        View v = inflater.inflate(R.layout.fragment_mainscreen, container, false);

        // начальная инициализация списка для ListView c адаптером MyListAdapter
        // получаем элемент ListView
        ListView mListView = (ListView) v.findViewById(R.id.main_listview);

        // создаем адаптер и задаем массивы к адаптеру
        List<ListPager> mListPagers = new ArrayList();
        String[] mTitles = getResources().getStringArray(R.array.main_title);
        int [] resID = getResID(R.array.main_icon);
        for (int i = 0; i < mTitles.length; i++) {
            mListPagers.add (new ListPager("MAIN", i+1, mTitles[i], resID[i],""));
        }
        ListAdapter mListAdapter  = new MyListAdapter(v.getContext(), R.layout.list_item, mListPagers);
        // устанавливаем адаптер
        mListView.setAdapter(mListAdapter);

        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mCallbacks.onMainItemSelected(position);
            }
        };
        mListView.setOnItemClickListener(itemListener);

        // возвращаем сформированный View в активность
        return v;
    }

    @Override
    public void onDetach () {
        super.onDetach();
        mCallbacks = null;
    }
}
