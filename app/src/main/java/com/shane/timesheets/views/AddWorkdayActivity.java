package com.shane.timesheets.views;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Painter;

import java.util.ArrayList;
import java.util.List;

public class AddWorkdayActivity extends Activity {
    private int jobId;
    private List<Painter> painters;
    private DatabaseHelper dbHelper;
    private ListView painterList;
    private AddWorkdayAdapter adapter;
    private List<Double> hours;
    private CheckBox check;
    private TextView hoursText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workday);

        jobId = getIntent().getIntExtra(IntentExtra.JOB_ID, 0);

        dbHelper = new DatabaseHelper(this);
        painters = dbHelper.getPaintersOnJob(jobId);
        hours = new ArrayList<>();
        for (int i = 0; i < painters.size(); i++) {
            hours.add(Double.valueOf(0));
        }
        painterList = (ListView) findViewById(R.id.list_painters_present);
        painterList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        View footer = LayoutInflater.from(this).inflate(R.layout.footer_spacer, painterList, false);
        painterList.addFooterView(footer, null, false);
        painterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray checked = painterList.getCheckedItemPositions();
                check = (CheckBox) view.findViewById(R.id.check_present);
                hoursText = (TextView) view.findViewById(R.id.text_hours);
                for (int i = 0; i < checked.size(); i++) {
                    if (checked.keyAt(i) == position) {
                        if (checked.valueAt(i)) {
                            Bundle args = new Bundle();
                            args.putInt(IntentExtra.PAINTER_POSITION, position);
                            HoursPickerDialog hoursPicker = new
                                    HoursPickerDialog();
                            hoursPicker.setArguments(args);
                            hoursPicker.show(getFragmentManager(), "Hour picker dialog");
                        } else {
                            check.setChecked(false);
                            hoursText.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        adapter = new AddWorkdayAdapter(this, R.layout.item_painter_present, painters);
        painterList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        painters.clear();
        painters.addAll(dbHelper.getPaintersOnJob(jobId));
        adapter.notifyDataSetChanged();
    }

    public void onClickCheck(View v) {
        if (!dbHelper.isWorkDayToday(jobId)) {
            if (dbHelper.insertNewWorkDay(jobId)) {
                //TODO handle error
            }
        }
        int workDay = dbHelper.getWorkDayId(jobId);
        SparseBooleanArray checked = painterList.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            if (checked.valueAt(i)) {
                Painter p = painters.get(checked.keyAt(i));
                double h = hours.get(checked.keyAt(i));
                if (dbHelper.insertPainterDay(p, workDay, h)) {
                    //TODO handle error
                }
            }
        }
        finish();
    }

    public void setHours(int position, Double value) {
        hours.set(position, value);
        check.setChecked(true);
        hoursText.setText("Hours: " + value);
        hoursText.setVisibility(View.VISIBLE);
    }

    public void toggleChecked(int position) {
        SparseBooleanArray checked = painterList.getCheckedItemPositions();
        int index = checked.indexOfKey(position);
        painterList.setItemChecked(position, !checked.valueAt(index));
    }
}
