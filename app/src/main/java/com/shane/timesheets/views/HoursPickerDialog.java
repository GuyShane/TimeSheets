package com.shane.timesheets.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;

public class HoursPickerDialog extends DialogFragment {

    private int pos;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        pos=getArguments().getInt(IntentExtra.PAINTER_POSITION,0);
        final AddWorkdayActivity caller = (AddWorkdayActivity) getActivity();
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_hours_picker, null, false);
        final EditText hourPicker=(EditText)v.findViewById(R.id.edit_hours);
        builder.setView(v);
        builder.setTitle("Hours worked");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (hourPicker.getText().toString().isEmpty()) {
                    caller.toggleChecked(pos);
                } else {
                    double value = Double.valueOf(hourPicker.getText().toString());
                    caller.setHours(pos,value);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                caller.toggleChecked(pos);
            }
        });
        return builder.create();
    }

}
