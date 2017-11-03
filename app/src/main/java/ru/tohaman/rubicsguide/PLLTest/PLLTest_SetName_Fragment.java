package ru.tohaman.rubicsguide.PLLTest;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import ru.tohaman.rubicsguide.R;

/**
 * Created by Toha on 12.04.2017.
 */

public class PLLTest_SetName_Fragment extends DialogFragment {

    //стр.250
    public static final String EXTRA_Comment = "ru.tohaman.rubicsguide.comment";
    //стр.247
    private static final String ARG_Comment = "ru.tohaman.rubicsguide.comment";
    private static final String ARG_Comment2 = "ru.tohaman.rubicsguide.comment2";

    private EditText mEditText;
    private ImageView mImageView;

    public static PLLTest_SetName_Fragment newInstance (String string, String string2) {
        Bundle args = new Bundle();
        //передаем данные в диалог
        args.putSerializable(ARG_Comment, string);
        args.putSerializable(ARG_Comment2, string2);

        PLLTest_SetName_Fragment fragment = new PLLTest_SetName_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    //стр.241-244
    @Override
    public Dialog onCreateDialog (Bundle savedInstaceState) {
        //принимаем данные из активности при создании диалога
        String string = (String) getArguments().getSerializable(ARG_Comment);
        String string2 = (String) getArguments().getSerializable(ARG_Comment2);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_pll_set_name, null);
        mEditText = (EditText) v.findViewById(R.id.pll_test_setname_editText);
        mImageView = (ImageView) v.findViewById(R.id.pll_test_setname_image);
        mImageView.setImageResource (Integer.parseInt(string));

        mEditText.setText(string2);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.plltest_setname_title)
                .setPositiveButton(android.R.string.ok,
                        //стр.251
                        new DialogInterface.OnClickListener(){
                            @Override
                            //переопределяем клик по кнопке ОК, если она нажата, то передаем обратно новое значение строки
                            public void onClick (DialogInterface dialog, int which) {
                                // В string надо передать новое значение текста в mEditText

                                String text = mEditText.getText().toString();
                                sendResult(Activity.RESULT_OK,text);
                            }
                        })
                .create();
    }

    //стр.250 возвращаем результат
    private void sendResult (int resultCode, String string) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_Comment, string);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(),resultCode, intent);
    }
}
