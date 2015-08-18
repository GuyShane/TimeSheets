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

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Painter;

import java.util.List;

public class AddPainterActivity extends Activity {
    private int jobId;

    private DatabaseHelper dbHelper;
    private List<Painter> painters;

    private ListView painterList;
    private AddPainterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_painter);

        jobId = getIntent().getIntExtra(IntentExtra.JOB_ID, 0);

        dbHelper = new DatabaseHelper(this);
        painters = dbHelper.getPaintersNotOnJob(jobId);

        painterList = (ListView) findViewById(R.id.list_painters);
        View footerView= LayoutInflater.from(this).
                inflate(R.layout.footer_spacer, painterList, false);
        painterList.addFooterView(footerView,null,false);
        adapter = new AddPainterAdapter(this, R.layout.item_add_painter, painters);
        painterList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        painterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray checked = painterList.getCheckedItemPositions();
                CheckBox check = (CheckBox) view.findViewById(R.id.check_painter);
                for (int i = 0; i < checked.size(); i++) {
                    if (checked.keyAt(i) == position) {
                        check.setChecked(checked.valueAt(i));
                    }
                }
            }
        });
        painterList.setAdapter(adapter);
    }

    public void onClickCheck(View v) {
        SparseBooleanArray checked = painterList.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            if (checked.valueAt(i)) {
                int pos = checked.keyAt(i);
                Painter p = painters.get(pos);
                dbHelper.insertJobPainter(jobId, p);
            }
        }
        finish();
    }
}
