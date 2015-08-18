package com.shane.timesheets.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;

public class HoursPickerDialog extends DialogFragment {

    private int pos;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        pos = getArguments().getInt(IntentExtra.PAINTER_POSITION, 0);
        final AddWorkdayActivity caller = (AddWorkdayActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_hours_picker, null, false);
        final EditText hourPicker = (EditText) v.findViewById(R.id.edit_hours);
        builder.setView(v);
        builder.setTitle(getString(R.string.title_hours_picker));
        builder.setPositiveButton(getString(R.string.string_button_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (hourPicker.getText().toString().isEmpty()) {
                    caller.toggleChecked(pos);
                } else {
                    double value = Double.valueOf(hourPicker.getText().toString());
                    caller.setHours(pos, value);
                }
            }
        });
        builder.setNegativeButton(getString(R.string.string_button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                caller.toggleChecked(pos);
            }
        });
        return builder.create();
    }

}
