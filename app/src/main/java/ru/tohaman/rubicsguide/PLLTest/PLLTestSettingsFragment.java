package ru.tohaman.rubicsguide.PLLTest;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.tohaman.rubicsguide.R;
import ru.tohaman.rubicsguide.listpager.ListPager;
import ru.tohaman.rubicsguide.listpager.ListPagerLab;

/**
 * Created by Toha on 30.07.2017.
 */

public class PLLTestSettingsFragment extends Fragment {
    private List<ListPager> mListPagers = new ArrayList();
    private ListPagerLab listPagerLab;
    private static final String DIALOG_COMMENT = "DialogComment";  //в этой "паре", передаем значение комментария для редактирования
    private static final int REQUEST_COMMENT = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mainscreen, container, false);

        // начальная инициализация списка для ListView c адаптером MyListAdapter
        // получаем элемент ListView
        ListView mListView = (ListView) v.findViewById(R.id.main_listview2);

        // Получаем синглет
        listPagerLab = ListPagerLab.get(getActivity());

        mListPagers = listPagerLab.getPhaseList("PLLTEST");

        String[] mTitles = getResources().getStringArray(R.array.pll_test_phases);

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
                String st2 = mListPagers.get(position).getTitle();
                PLLTest_SetName_Fragment dialog = PLLTest_SetName_Fragment.newInstance(String.valueOf(st),st2);
                dialog.setTargetFragment(PLLTestSettingsFragment.this, REQUEST_COMMENT);
                dialog.show (manager, DIALOG_COMMENT);
            }
        };
        mListView.setOnItemClickListener(itemListener);

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
            text.setText(listPager.getTitle());
            return convertView;
        }
    }

}
