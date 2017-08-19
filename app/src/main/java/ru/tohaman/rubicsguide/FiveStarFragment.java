package ru.tohaman.rubicsguide;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

/**
 * Created by anton on 17.08.17.
 */

public class FiveStarFragment extends DialogFragment {
    AlertDialog.Builder ad;
    @Override
    public Dialog onCreateDialog (Bundle savedInstaceState) {
        ad = new AlertDialog.Builder(getActivity());
        ad.setTitle(R.string.fivestartitle);
        ad.setMessage(R.string.fivestar_text);
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(getContext(),"Вы ничего не выбрали",Toast.LENGTH_SHORT).show();
            }
        });
        ad.setPositiveButton("Конечно, перейти в GooglePlay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                e.putBoolean("neverAskRate", true); //ставим флаг - Больше не спрашивать "Оцените приложение"... надеемся, что после перехода в googleplay, оценка будет поставлена
                e.commit(); // подтверждаем изменения
                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        ad.setNegativeButton("Больше не напоминать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                e.putBoolean("neverAskRate", true); //ставим флаг - Больше не спрашивать "Оцените приложение"
                e.commit(); // подтверждаем изменения
            }
        });
        // Напомнить через 30 заупсков
        ad.setNeutralButton("Напомнить позже", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor e = sp.edit();
                e.putBoolean("neverAskRate", false); //на всякий случай сбрасываем флаг - Больше не спрашивать "Оцените приложение"
                e.putInt("startcount",sp.getInt("startcount", 1)-30); //уменьшаем счетчик количество запусков приложения на 30
                e.commit(); // подтверждаем изменения
            }
        });
        return ad.create();
    }

}
