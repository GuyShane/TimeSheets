package com.shane.timesheets.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.shane.timesheets.R;

public class CompletedDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final JobInfoDisplayActivity caller=(JobInfoDisplayActivity)getActivity();
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.title_completed_dialog))
                .setMessage(getString(R.string.string_completed_dialog_message))
                .setPositiveButton(getString(R.string.string_button_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        caller.markComplete();
                    }
                })
                .setNegativeButton(getString(R.string.string_button_cancel), null)
                .create();
    }
}
