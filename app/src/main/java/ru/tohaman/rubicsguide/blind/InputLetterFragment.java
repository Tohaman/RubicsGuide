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
    public static final String EXTRA_Letter = "ru.tohaman.rubicsguide.letter";
    public static final String EXTRA_Position = "ru.tohaman.rubicsguide.letter2";
    //стр.247
    private static final String ARG_Letter = "ru.tohaman.rubicsguide.letter";
    private static final String ARG_Position = "ru.tohaman.rubicsguide.letter2";

    private TextView mTextView;

    public static InputLetterFragment newInstance (String letter, int position) {
        Bundle args = new Bundle();
        //передаем данные в диалог
        args.putSerializable(ARG_Letter, letter);
        args.putSerializable(ARG_Position, position);

        InputLetterFragment fragment = new InputLetterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //стр.241-244
    @Override
    public Dialog onCreateDialog (Bundle savedInstaceState) {
        //принимаем данные из активности при создании диалога
        final String letter = (String) getArguments().getSerializable(ARG_Letter);
        final int position = (int) getArguments().getSerializable(ARG_Position);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_inputletter, null);

        mTextView = (TextView) v.findViewById(R.id.textView_letter);
        mTextView.setText(letter);

        Button mButton_plus = (Button) v.findViewById(R.id.button_plus2);
        mButton_plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                String st = mTextView.getText().toString();
                char ch = st.charAt(0);
                ch++;
                if (ch > 'Я') { ch = 'А';}
                if (ch == 'Ж') { ch = 'Ё';}
                if (ch < 'А' && ch > 'Ё') { ch = 'Ж';}
                mTextView.setText(Character.toString(ch));
            }
        });

        Button mButton_minus = (Button) v.findViewById(R.id.button_minus2);
        mButton_minus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Обработка нажатия
                String st = mTextView.getText().toString();
                char ch = st.charAt(0);
                if (ch == 'Ж') { ch = 'Ё'; ch++;}
                ch--;
                if (ch < 'Ё') { ch = 'Е';}
                if (ch < 'А' && ch != 'Ё') { ch = 'Я';}

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
                                sendResult(Activity.RESULT_OK,string,position);
                            }
                        })
                .create();
    }

    //стр.250 возвращаем результат
    private void sendResult (int resultCode, String letter, int position) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_Letter, letter);
        intent.putExtra(EXTRA_Position, position);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(),resultCode, intent);
    }
}
