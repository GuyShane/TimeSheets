package com.shane.timesheets.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class WorkDayExistsDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AddWorkdayActivity caller=(AddWorkdayActivity)getActivity();
        return new AlertDialog.Builder(getActivity())
                .setTitle("Workday exists")
                .setMessage("There is already a workday for this date. Would you like to " +
                        "overwrite the existing hours, or add to them?")
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        caller.saveWorkDay(false);
                    }
                })
                .setNegativeButton("OVERWRITE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        caller.saveWorkDay(true);
                    }
                })
                .create();
    }
}
