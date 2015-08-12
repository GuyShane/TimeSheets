package com.shane.timesheets.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.DateFormatter;
import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Painter;
import com.shane.timesheets.models.PainterDay;
import com.shane.timesheets.models.WorkDay;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private EditText editDate;
    private DecimalFormat nf;
    private DateFormatter df;
    private Context ctx;
    private String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workday);

        ctx=this;

        nf=new DecimalFormat();
        nf.setDecimalSeparatorAlwaysShown(false);

        df=new DateFormatter();

        jobId = getIntent().getIntExtra(IntentExtra.JOB_ID, 0);

        final Calendar cal= Calendar.getInstance();
        editDate=(EditText)findViewById(R.id.edit_date);
        editDate.setText(df.getLongDateString());
        editDate.setTag(df.getYMDString());
        final DatePickerDialog datePicker = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editDate.setText(df.getLongDateString(year, monthOfYear, dayOfMonth));
                        editDate.setTag(df.getYMDString(year, monthOfYear, dayOfMonth));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });

        dbHelper = new DatabaseHelper(this);
        painters = dbHelper.getPaintersOnJob(jobId);
        hours = new ArrayList<>();
        for (int i = 0; i < painters.size(); i++) {
            hours.add(0.0);
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
        dateString=(String)editDate.getTag();
        if (!dbHelper.isWorkDay(jobId,dateString)) {
            dbHelper.insertNewWorkDay(jobId,dateString);
            saveWorkDay(false);
        }
        else {
            WorkDayExistsDialog dialog=new WorkDayExistsDialog();
            dialog.show(getFragmentManager(),"Work day exists dialog");
        }
    }

    public void saveWorkDay(boolean overwrite) {
        if (overwrite) {
            dbHelper.deleteWorkDays(dateString);
            dbHelper.insertNewWorkDay(jobId,dateString);
        }
        int workDay = dbHelper.getWorkDayId(jobId,dateString);
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
        hoursText.setText("Hours: " + nf.format(value));
        hoursText.setVisibility(View.VISIBLE);
    }

    public void toggleChecked(int position) {
        SparseBooleanArray checked = painterList.getCheckedItemPositions();
        int index = checked.indexOfKey(position);
        painterList.setItemChecked(position, !checked.valueAt(index));
    }
}
