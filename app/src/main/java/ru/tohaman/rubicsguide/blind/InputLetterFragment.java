package ru.tohaman.rubicsguide.blind;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.tohaman.rubicsguide.R;

import static android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS;

/**
 * Created by Toha on 12.04.2017.
 */

public class InputLetterFragment extends DialogFragment {

    //стр.250
    public static final String EXTRA_Comment = "ru.tohaman.rubicsguide.letter";
    //стр.247
    private static final String ARG_Comment = "ru.tohaman.rubicsguide.letter";

    private TextView mTextView;
    private Button mButton_minus;
    private Button mButton_plus;

    public static InputLetterFragment newInstance (String string) {
        Bundle args = new Bundle();
        //передаем данные в диалог
        args.putSerializable(ARG_Comment, string);

        InputLetterFragment fragment = new InputLetterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //стр.241-244
    @Override
    public Dialog onCreateDialog (Bundle savedInstaceState) {
        //принимаем данные из активности при создании диалога
        String string = (String) getArguments().getSerializable(ARG_Comment);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_inputletter, null);

        mTextView = (TextView) v.findViewById(R.id.textView_letter);
        mTextView.setText(string);

        mButton_plus = (Button) v.findViewById(R.id.button_plus2);
        mButton_plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                String st = mTextView.getText().toString();
                char ch = st.charAt(0);
                ch++;
                mTextView.setText(Character.toString(ch));
            }
        });

        mButton_minus = (Button) v.findViewById(R.id.button_minus2);
        mButton_minus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                String st = mTextView.getText().toString();
                char ch = st.charAt(0);
                ch--;
                mTextView.setText(Character.toString(ch));
            }
        });

        mButton_minus = (Button) v.findViewById(R.id.button_minus2);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Выберите букву:")
                .setPositiveButton(android.R.string.ok,
                        //стр.251
                        new DialogInterface.OnClickListener(){
                            @Override
                            //переопределяем клик по кнопке ОК, если она нажата, то передаем обратно новое значение строки
                            public void onClick (DialogInterface dialog, int which) {
                                // В string надо передать новое значение текста в mEditText
                                String string = mTextView.getText().toString();
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
