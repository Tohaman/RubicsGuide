package ru.tohaman.rubicsguide;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by anton on 17.08.17.
 */

public class FiveStarFragment extends DialogFragment {
    AlertDialog.Builder ad;
    @Override
    public Dialog onCreateDialog (Bundle savedInstaceState) {
        ad = new AlertDialog.Builder(getActivity());
        ad.setTitle(R.string.fivestartitle);
        ad.setPositiveButton(android.R.string.ok, null);
        return ad.create();
    }
}
