package ru.tohaman.rubicsguide.blind;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.tohaman.rubicsguide.R;

public class MyGridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<CubeAzbuka> mGridList;
    private int layout;

    // Конструктор
    public MyGridAdapter(Context context, int ViewResourceId, List<CubeAzbuka> mGridList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mGridList = mGridList;
        this.layout = ViewResourceId;       //потом ссылаемся тут inflater.inflate(layout, parent, false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;

        if (convertView == null) {
            grid = new View(mContext);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(layout, parent, false);
        } else {
            grid = convertView;
        }

        TextView textView = (TextView) grid.findViewById(R.id.grid_text);
        textView.setText(mGridList.get(position).getLetter());
        LinearLayout linearLayout = (LinearLayout) grid.findViewById(R.id.grid_main_layout);
        linearLayout.setBackgroundColor(mGridList.get(position).getColor());

        return grid;

    }


    @Override
    public int getCount() {
        return mGridList.size();
    }

    // возвращает содержимое выделенного элемента списка
    public String getItem(int position) {
        return mGridList.get(position).getLetter();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}