package ru.tohaman.rubicsguide.blind;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import ru.tohaman.rubicsguide.R;

import static android.R.color.black;


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

        MyTextView textView = (MyTextView) grid.findViewById(R.id.grid_text);
        textView.setText(mGridList.get(position).getLetter());
        textView.setBackgroundColor(mGridList.get(position).getColor());
        MyRelativeLayout myRelativeLayout = (MyRelativeLayout) grid.findViewById(R.id.grid_main_layout);

        // Если символ не задан, значит клетку делаем прозрачной (цвета, который задан в mGridList
        // если клетка кубика, то лэйаут делаем черным.
        if (mGridList.get(position).getLetter().equals("")) {
            myRelativeLayout.setBackgroundColor(mGridList.get(position).getColor());
        } else {
            myRelativeLayout.setBackgroundColor(ContextCompat.getColor(grid.getContext(), R.color.black));
        }


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