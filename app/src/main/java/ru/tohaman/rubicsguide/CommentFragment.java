package ru.tohaman.rubicsguide;

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

/**
 * Created by Toha on 12.04.2017.
 */

public class CommentFragment extends DialogFragment {

    //стр.250
    public static final String EXTRA_Comment = "com.maximchechnev.rubicsguide.comment";
    //стр.247
    private static final String ARG_Comment = "comment";

    private EditText mEditText;

    public static CommentFragment newInstance (String string) {
        Bundle args = new Bundle();
        //передаем данные в диалог
        args.putSerializable(ARG_Comment, string);

        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //стр.241-244
    @Override
    public Dialog onCreateDialog (Bundle savedInstaceState) {
        //принимаем данные из активности при создании диалога
        String string = (String) getArguments().getSerializable(ARG_Comment);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_comment, null);
        mEditText = (EditText) v.findViewById(R.id.comment_edittext);
        mEditText.setText(string);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.edit_comment_label)
                .setPositiveButton(android.R.string.ok,
                        //стр.251
                        new DialogInterface.OnClickListener(){
                            @Override
                            //переопределяем клик по кнопке ОК, если она нажата, то передаем обратно новое значение строки
                            public void onClick (DialogInterface dialog, int which) {
                                // В string надо передать новое значение текста в mEditText

                                String string = mEditText.getText().toString();
                                sendResult(Activity.RESULT_OK,string);
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
