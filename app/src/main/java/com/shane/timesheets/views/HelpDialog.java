package com.shane.timesheets.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.shane.timesheets.R;

public class HelpDialog  extends DialogFragment {
    private String message;

    public HelpDialog(String message) {
        this.message=message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Help")
                .setMessage(message)
                .setNegativeButton("DISMISS", null)
                .create();
    }
}
