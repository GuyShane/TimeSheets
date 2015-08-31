package com.shane.timesheets.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.IntentExtra;
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
        paintersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(PainterListActivity.this,EditPainterActivity.class);
                i.putExtra(IntentExtra.PAINTER_ID, painters.get(position).getId());
                startActivity(i);
            }
        });
        paintersList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        painters.clear();
        painters.addAll(dbHelper.getAllPainters());
        adapter.notifyDataSetChanged();
    }

    public void onClickNewPainter(View v) {
        Intent i = new Intent(PainterListActivity.this, NewPainterActivity.class);
        startActivity(i);
    }

    public void onClickMenu(View v) {
        PopupMenu menu = new PopupMenu(getApplicationContext(), v, Gravity.END);
        menu.getMenuInflater().inflate(R.menu.menu_painter_list, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                        HelpDialog help=new HelpDialog();
                        Bundle args=new Bundle();
                        args.putString(IntentExtra.HELP_MESSGAE,
                                getString(R.string.action_help_painter_list));
                        help.setArguments(args);
                        help.show(getFragmentManager(),"Help Dialog");
                        break;
                }
                return true;
            }
        });
        menu.show();
    }
}
