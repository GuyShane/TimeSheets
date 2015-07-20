package com.shane.timesheets.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Painter;

import java.util.List;

public class PainterListActivity extends Activity {
    private ListView paintersList;
    private PainterListAdapter adapter;
    private List<Painter> painters;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painter_list);

        dbHelper = new DatabaseHelper(this);

        paintersList = (ListView) findViewById(R.id.list_painters);
        painters = dbHelper.getAllPainters();
        adapter = new PainterListAdapter(this, R.layout.item_painter_list, painters);
        View footer = LayoutInflater.from(this).inflate(R.layout.footer_spacer, paintersList, false);
        paintersList.addFooterView(footer, null, false);
        paintersList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        painters.clear();
        painters.addAll(dbHelper.getAllPainters());
        adapter.notifyDataSetChanged();
    }

    public void onClickAddPainter(View v) {
        Intent i = new Intent(PainterListActivity.this, NewPainterActivity.class);
        startActivity(i);
    }
}
