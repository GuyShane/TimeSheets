package com.shane.timesheets.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.DateFormatter;
import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Job;
import com.shane.timesheets.models.PainterDay;
import com.shane.timesheets.models.WorkDay;

import java.text.DecimalFormat;
import java.util.List;

public class DayInfoActivity extends Activity {
    private int jobId;
    private Job job;
    private DatabaseHelper dbHelper;
    private List<WorkDay> days;
    private ExpandableListView dayList;
    private DayInfoAdapter adapter;
    private TextView daysText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_info);

        TextView jobText=(TextView)findViewById(R.id.text_job_name);
        daysText=(TextView)findViewById(R.id.text_days_worked);

        jobId=getIntent().getIntExtra(IntentExtra.JOB_ID,0);
        dbHelper=new DatabaseHelper(this);
        days=dbHelper.getWorkDays(jobId);
        job=dbHelper.getJobById(jobId);

        jobText.setText(job.getTitle());

        dayList=(ExpandableListView)findViewById(R.id.list_days);
        adapter=new DayInfoAdapter(this,R.layout.item_day_date,R.layout.item_day_info,days);
        dayList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        days.clear();
        days.addAll(dbHelper.getWorkDays(jobId));
        adapter.notifyDataSetChanged();
        String endText=getString(R.string.string_days_worked);
        if (days.size()==1) {
            endText=getString(R.string.string_day_worked);
        }
        daysText.setText(days.size() + endText);
    }

    public void onClickMenu(View v) {
        PopupMenu menu = new PopupMenu(getApplicationContext(), v, Gravity.END);
        menu.getMenuInflater().inflate(R.menu.menu_day_info, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                        HelpDialog help=new HelpDialog();
                        Bundle args=new Bundle();
                        args.putString(IntentExtra.HELP_MESSGAE,
                                getString(R.string.action_help_day_info));
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
