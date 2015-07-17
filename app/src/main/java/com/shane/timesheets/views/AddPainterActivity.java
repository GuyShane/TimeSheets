package com.shane.timesheets.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

        jobId=getIntent().getIntExtra(IntentExtra.JOB_ID,0);

        dbHelper=new DatabaseHelper(this);
        painters=dbHelper.getPaintersNotOnJob(jobId);

        painterList=(ListView)findViewById(R.id.list_painters);
        adapter=new AddPainterAdapter(this,R.layout.item_add_painter,painters);
        painterList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        painterList.setAdapter(adapter);
    }
}
