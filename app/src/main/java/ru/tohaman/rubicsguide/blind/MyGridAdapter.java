package ru.tohaman.rubicsguide.blind;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.tohaman.rubicsguide.R;

public class MyGridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mGridList;
    private int layout;

    // Конструктор
    public MyGridAdapter(Context context, int ViewResourceId, List<String> mGridList) {
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
            grid = (View) convertView;
        }

        TextView textView = (TextView) grid.findViewById(R.id.grid_text);
        textView.setText(mGridList.get(position));

        return grid;

    }

/*

        ImageView icon = (ImageView) v.findViewById(R.id.list_item_image);
        TextView text = (TextView) v.findViewById(R.id.list_item_title_text);

        ListPager listPager = pagerLists.get(position);

        icon.setImageResource(listPager.getIcon());
        text.setText(listPager.getTitle());

            for (int i = 0; i < 108; i++) {
        View v = View.inflate(view.getContext(),R.layout.grid_item,null);
        TextView textView=(TextView) v.findViewById(R.id.grid_text);
        String st = String.valueOf(i%10);
        textView.setText("");
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.grid_layout);
        LinearLayout linearLayout1 = (LinearLayout) v.findViewById(R.id.grid_main_layout);
        linearLayout.setBackgroundColor(back);
        mLinearLayouts[i] = linearLayout;
        mLinearLayouts1[i] = linearLayout1;
        addViewToGrid(mGridLayout,v);
    }
*/


    @Override
    public int getCount() {
        return mGridList.size();
    }

    // возвращает содержимое выделенного элемента списка
    public String getItem(int position) {
        return mGridList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}