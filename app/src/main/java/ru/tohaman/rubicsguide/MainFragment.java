package ru.tohaman.rubicsguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.listpager.ListPagerLab;
import ru.tohaman.rubicsguide.listpager.MyListAdapter;

import static ru.tohaman.rubicsguide.util.Util.getResID;

/**
 * Created by Toha on 12.06.2017. Фрагмент главного меню программы
 */

public class MainFragment extends Fragment {
    private Callbacks mCallbacks;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences sp;
    int ver,cur_ver,count;

    //Обязательный интерфейс для активности-хоста
    public interface Callbacks {
        void onMainItemSelected (int id);
        boolean onMyOptionsItemSelected (MenuItem item);
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
        //return inflater.inflate(R.layout.fragment_about,null);
        //Инициализируем синглет ListPagerLab, в котором будет база.
        ListPagerLab listPagerLab = ListPagerLab.get();

        View v = inflater.inflate(R.layout.fragment_mainscreen, container, false);

        // начальная инициализация списка для ListView c адаптером MyListAdapter
        // получаем элемент ListView
        ListView mListView = (ListView) v.findViewById(R.id.main_listview2);

        // создаем адаптер и задаем массивы к адаптеру
        List<ListPager> mListPagers = new ArrayList<>();
        String[] mTitles = getResources().getStringArray(R.array.main_title);
        int [] resID = getResID(R.array.main_icon, getContext());
        for (int i = 0; i < mTitles.length; i++) {
            mListPagers.add (new ListPager("MAIN", i+1, mTitles[i], resID[i]));
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

        mConstraintLayout = (ConstraintLayout) v.findViewById(R.id.hint_main);
        cur_ver = BuildConfig.VERSION_CODE;

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        count = sp.getInt("startcount", 1);
        // Увеличиваем число запусков программы на 1 и сохраняем результат.
        count++;
        SharedPreferences.Editor e = sp.edit();
        e.putInt("startcount", count);
        e.apply(); // подтверждаем изменения

        // проверяем версию программы в файле настроек, если она отлична от текущей, то выводим окно с описанием обновлений
        ver = sp.getInt("version", 1);
        if (cur_ver!=ver) { //если версии разные
            mConstraintLayout.setVisibility(View.VISIBLE);
            Button hintbutton = (Button) v.findViewById(R.id.hint_mainbutton);
            mConstraintLayout.setVisibility(View.VISIBLE);
            hintbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        SharedPreferences.Editor e = sp.edit();
                        e.putInt("version", cur_ver);
                        e.apply(); // подтверждаем изменения
                        mConstraintLayout.setVisibility(View.INVISIBLE);
                    }
            });
        } else {    // если не первый, то убираем подсказку
            mConstraintLayout.setVisibility(View.INVISIBLE);
        }

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
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        return mCallbacks.onMyOptionsItemSelected (item);
    }

    @Override
    public void onDetach () {
        super.onDetach();
        mCallbacks = null;
    }
}
