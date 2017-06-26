package ru.tohaman.rubicsguide.blind;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.tohaman.rubicsguide.R;

/**
 * Created by anton on 25.06.17.
 */

public class AzbukaFragment extends Fragment {
    private TextView mAzbukaField;
    private MyGridAdapter mAdapter;
    private List<String> mGridList = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_azbuka, container, false);

        // Немного преобразуем текст для корректного отображения.
        String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
        String description = String.format(text,getString(R.string.azbuka));
        description = description.replace("%%", "%25");
        description = "Выбранный элемент: ";                        //потом убрать

        Spanned spanresult;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanresult = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY, imgGetter, null);
        } else {
            spanresult = Html.fromHtml(description, imgGetter, null);
        }

        mAzbukaField = (TextView) view.findViewById(R.id.azbuka_textView);
        mAzbukaField.setText(spanresult);
        mAzbukaField.setMovementMethod(LinkMovementMethod.getInstance());

        for (int i=0; i<108; i++) {
            mGridList.add(String.valueOf(i));
        }

        GridView mGridView = (GridView) view.findViewById(R.id.azbuka_gridView);
        mAdapter = new MyGridAdapter(view.getContext(),R.layout.grid_item,mGridList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAzbukaField.setText("Выбранный элемент: " + mAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAzbukaField.setText("Ничего не выбрано");
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                mAzbukaField.setText("Выбранный элемент: "
                        + mAdapter.getItem(position));
            }
        });

        return view;
    }

    @SuppressWarnings("deprecation")
    private Html.ImageGetter imgGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            source = source.replace(".png", "");
            int resID = getResources().getIdentifier(source , "drawable", getActivity().getPackageName());
            //если картинка в drawable не найдена, то подсовываем заведомо существующую картинку
            if (resID == 0 ) {
                resID = getResources().getIdentifier("noscr" , "drawable", getActivity().getPackageName());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                drawable = getResources().getDrawable(resID, null);
            }else {
                drawable = getResources().getDrawable(resID);}

            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight());

            return drawable;
        }
    };

}
