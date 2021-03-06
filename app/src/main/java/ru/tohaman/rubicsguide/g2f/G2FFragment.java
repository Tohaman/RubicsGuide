package ru.tohaman.rubicsguide.g2f;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.listpager.ListPagerLab;
import ru.tohaman.rubicsguide.listpager.MyListAdapter;

import java.util.ArrayList;
import java.util.List;

import static ru.tohaman.rubicsguide.util.Util.getResID;


/**
 * Created by Toha on 20.05.2017. ListView для меню переходим на Фридрих
 */
public class G2FFragment extends Fragment {

    private Callbacks mCallbacks;

    //Обязательный интерфейс для активности-хоста
    public interface Callbacks {
        void onG2FItemSelected (int id);
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
    public void onDetach () {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListPagerLab listPagerLab = ListPagerLab.get(getContext());
        View v = inflater.inflate(R.layout.fragment_mainlist, container, false);

        // начальная инициализация списка для ListView c адаптером MyListAdapter
        // получаем элемент ListView
        ListView mListView = v.findViewById(R.id.main_listview);

        // создаем адаптер и задаем массивы к адаптеру
        ArrayList<ListPager> mListPagers = new ArrayList<>();

        String[] mTitles = getResources().getStringArray(R.array.g2f_title);
        int [] resID = getResID(R.array.g2f_icon,getContext());
        for (int i = 0; i < mTitles.length; i++) {
            mListPagers.add (new ListPager("G2F", i+1, mTitles[i], resID[i]));
        }
        ListAdapter mListAdapter  = new MyListAdapter(v.getContext(), R.layout.list_item, mListPagers);
        // устанавливаем адаптер
        mListView.setAdapter(mListAdapter);

        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mCallbacks.onG2FItemSelected(position);

            }
        };
        mListView.setOnItemClickListener(itemListener);


        // возвращаем сформированный View в активность
        return v;
    }


}
