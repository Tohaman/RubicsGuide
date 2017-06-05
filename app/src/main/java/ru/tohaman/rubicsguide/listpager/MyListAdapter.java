package ru.tohaman.rubicsguide.listpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ru.tohaman.rubicsguide.R;

import java.util.List;

/**
 * Created by User on 22.05.2017.
 */

public class MyListAdapter extends ArrayAdapter<ListPager> {
    private LayoutInflater inflater;
    private int layout;
    private List<ListPager> pagerLists;

    public MyListAdapter(Context context, int resource, List<ListPager> pagerLists){
        super(context,resource,pagerLists);
        this.pagerLists = pagerLists;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(this.layout, parent, false);
        ImageView icon = (ImageView) v.findViewById(R.id.list_item_image);
        TextView text = (TextView) v.findViewById(R.id.list_item_title_text);

        ListPager listPager = pagerLists.get(position);

        icon.setImageResource(listPager.getIcon());
        text.setText(listPager.getTitle());
        return v;
    }
}
