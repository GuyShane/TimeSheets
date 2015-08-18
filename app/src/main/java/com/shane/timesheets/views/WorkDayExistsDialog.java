package com.shane.timesheets.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.shane.timesheets.R;

public class WorkDayExistsDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AddWorkdayActivity caller=(AddWorkdayActivity)getActivity();
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.title_workday_exists_dialog))
                .setMessage(getString(R.string.string_workday_exists_dialog_message))
                .setPositiveButton(getString(R.string.string_button_add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        caller.saveWorkDay(false);
                    }
                })
                .setNegativeButton(getString(R.string.string_button_overwrite), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        caller.saveWorkDay(true);
                    }
                })
                .create();
    }
}
