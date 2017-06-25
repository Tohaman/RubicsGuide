package ru.tohaman.rubicsguide.blind;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tohaman.rubicsguide.R;

/**
 * Created by anton on 25.06.17.
 */

public class AzbukaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_azbuka, container, false);

        return view;
    }
}
