package com.shane.timesheets.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class CompletedDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final JobInfoActivity caller=(JobInfoActivity)getActivity();
        return new AlertDialog.Builder(getActivity())
                .setTitle("Job completed")
                .setMessage("Mark this job as complete?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        caller.markComplete();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }
}
