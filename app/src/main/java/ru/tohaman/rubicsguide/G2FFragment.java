package ru.tohaman.rubicsguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import ru.tohaman.rubicsguide.listpager.ListActivity;
import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.listpager.ListPagerLab;
import ru.tohaman.rubicsguide.listpager.MyListAdapter;

import java.util.ArrayList;
import java.util.List;

import static ru.tohaman.rubicsguide.listpager.ListPagerLab.getResID;


/**
 * Created by Toha on 20.05.2017.
 */
public class G2FFragment extends Fragment {
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
        String[] mTitles = getResources().getStringArray(R.array.g2f_title);
        int [] resID = getResID(R.array.g2f_icon);
        for (int i = 0; i < mTitles.length; i++) {
            mListPagers.add (new ListPager("G2F", i+1, mTitles[i], resID[i],""));
        }
        ListAdapter mListAdapter  = new MyListAdapter(v.getContext(), R.layout.list_item, mListPagers);
        // устанавливаем адаптер
        mListView.setAdapter(mListAdapter);

        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // вызываем активность List(RecycleView)->PagerView с параметром (ACCEL,PLL,OLL,CROSS и т.д. заданным в массиве строк g2f_phase)
                mIntent = new Intent(getActivity(),ListActivity.class);
                mIntent.putExtra(RubicPhase,getResources().getStringArray(R.array.g2f_phase)[position]);
                startActivity(mIntent);
            }
        };
        mListView.setOnItemClickListener(itemListener);

/*        // получаем список заголовков
        final String[] mMainTitle= getResources().getStringArray(R.array.g2f_title);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_list_item_1,mMainTitle);

        // устанавливаем для списка адаптер
        mMainList.setAdapter(adapter);

        // процедура слушателя в выборе списка
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener(){
          @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
              switch (position){
                  case 0:
                      Toast.makeText(getActivity(),"Ускорения для начинающих",Toast.LENGTH_SHORT).show();
                      break;
                  case 1:
 //                     Toast.makeText(getActivity(),"Максимкин PLL",Toast.LENGTH_SHORT).show();
                      mIntent = new Intent(getActivity(),ListActivity.class);
                      mIntent.putExtra(RubicPhase,"PLL");
                      startActivity(mIntent);
                      break;
                  case 2:
//                      Toast.makeText(getActivity(),"Долгожданный OLL",Toast.LENGTH_SHORT).show();
                      mIntent = new Intent(getActivity(),ListActivity.class);
                      mIntent.putExtra(RubicPhase,"OLL");
                      startActivity(mIntent);
                      break;
                  default:
                  Toast.makeText(getActivity(),"Был выбран пункт" + position,Toast.LENGTH_SHORT).show();
              }
          }
        };
        // назначаем слушателя
        mMainList.setOnItemClickListener(itemListener);*/

        // возвращаем сформированный View в активность
        return v;
    }


}
