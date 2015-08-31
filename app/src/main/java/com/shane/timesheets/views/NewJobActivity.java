package com.shane.timesheets.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.DateFormatter;
import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Job;
import com.shane.timesheets.models.Painter;

import java.util.Calendar;


public class NewJobActivity extends JobInfoEntryActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupForm();
        setHeaderText(getString(R.string.header_new_job));
    }

    @Override
    public void onClickCheck(View v) {
        if (validateForm()) {
            Job newJob = new Job(title, address, startDate, endDate, cost);
            if (dbHelper.insertJob(newJob)) {
                int jobId=dbHelper.getJobId(title);
                SparseBooleanArray checked = painterList.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    if (checked.valueAt(i)) {
                        int pos = checked.keyAt(i)-painterList.getHeaderViewsCount();
                        Painter p = painters.get(pos);
                        dbHelper.insertJobPainter(jobId, p);
                    }
                }
                makeMessage(getString(R.string.toast_job_saved));
                finish();
            } else {
                makeMessage(getString(R.string.toast_save_failed));
            }
        }
    }

    @Override
    public void onClickMenu(View v) {
        PopupMenu menu = new PopupMenu(getApplicationContext(), v, Gravity.END);
        menu.getMenuInflater().inflate(R.menu.menu_new_job, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                        HelpDialog help=new HelpDialog();
                        Bundle args=new Bundle();
                        args.putString(IntentExtra.HELP_MESSGAE,
                                getString(R.string.action_help_new_job));
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
