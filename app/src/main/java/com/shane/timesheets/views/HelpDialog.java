package com.shane.timesheets.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;

public class HelpDialog  extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Help")
                .setMessage(getArguments().getString(IntentExtra.HELP_MESSGAE))
                .setNegativeButton(getString(R.string.string_button_dismiss), null)
                .create();
    }
}
