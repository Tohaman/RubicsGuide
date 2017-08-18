package ru.tohaman.rubicsguide;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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

            }
        });
        ad.setNeutralButton("Напомнить позже", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return ad.create();
    }
}
