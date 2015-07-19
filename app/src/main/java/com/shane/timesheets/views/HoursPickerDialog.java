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

import com.shane.timesheets.R;

public class HoursPickerDialog extends DialogFragment {

    private ListView list;
    private View view;
    private int pos;
    private CheckBox check;
    private TextView hours;

    public HoursPickerDialog(){}

    @SuppressLint("ValidFragment")
    public HoursPickerDialog(ListView parent, View view, int position) {
        this.list=parent;
        this.view=view;
        this.pos=position;
        check = (CheckBox) view.findViewById(R.id.check_present);
        hours = (TextView) view.findViewById(R.id.text_hours);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_hours_picker, null, false);
        final EditText hourPicker=(EditText)v.findViewById(R.id.edit_hours);
        builder.setView(v);
        builder.setTitle("Hours worked");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (hourPicker.getText().toString().isEmpty()) {
                    SparseBooleanArray checked = list.getCheckedItemPositions();
                    int index = checked.indexOfKey(pos);
                    list.setItemChecked(pos, !checked.valueAt(index));
                } else {
                    double value = Double.valueOf(hourPicker.getText().toString());
                    AddWorkdayActivity caller = (AddWorkdayActivity) getActivity();
                    caller.setHours(pos, value);
                    check.setChecked(true);
                    hours.setText("Hours: " + value);
                    hours.setVisibility(View.VISIBLE);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SparseBooleanArray checked = list.getCheckedItemPositions();
                int index = checked.indexOfKey(pos);
                list.setItemChecked(pos, !checked.valueAt(index));
            }
        });
        return builder.create();
    }

}
