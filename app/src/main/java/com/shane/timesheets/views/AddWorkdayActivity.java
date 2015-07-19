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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workday);

        jobId=getIntent().getIntExtra(IntentExtra.JOB_ID,0);

        dbHelper=new DatabaseHelper(this);
        painters=dbHelper.getPainters(jobId);
        hours=new ArrayList<>();
        for (int i=0;i<painters.size();i++) {
            hours.add(Double.valueOf(0));
        }
        painterList=(ListView)findViewById(R.id.list_painters_present);
        painterList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        View footer= LayoutInflater.from(this).inflate(R.layout.footer_spacer,painterList,false);
        painterList.addFooterView(footer);
        painterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray checked = painterList.getCheckedItemPositions();
                CheckBox check = (CheckBox) view.findViewById(R.id.check_present);
                TextView hours = (TextView)view.findViewById(R.id.text_hours);
                for (int i = 0; i < checked.size(); i++) {
                    if (checked.keyAt(i) == position) {
                        if (checked.valueAt(i)) {
                            HoursPickerDialog hoursPicker=new
                                    HoursPickerDialog(painterList,view,position);
                            hoursPicker.show(getFragmentManager(), "Hour picker dialog");
                        } else {
                            check.setChecked(false);
                            hours.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        adapter=new AddWorkdayAdapter(this,R.layout.item_painter_present,painters);
        painterList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        painters.clear();
        painters.addAll(dbHelper.getPainters(jobId));
        adapter.notifyDataSetChanged();
    }

    public void onClickCheck(View v) {
        for (Double d: hours) {
            System.out.println(d);
        }
    }

    public void setHours(int painter, Double value) {
        hours.set(painter,value);
    }
}
