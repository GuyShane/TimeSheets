package com.shane.timesheets.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.DateFormatter;
import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Job;
import com.shane.timesheets.models.Painter;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class JobInfoActivity extends Activity {
    private int fromCompleted;
    private int jobId;
    private Job job;
    private DatabaseHelper dbHelper;
    private DateFormatter df;
    private NumberFormat nf;

    private ListView painterList;
    private List<Painter> painters;
    private PaintersAdapter adapter;

    private ImageButton buttonCheck;
    private ImageButton buttonAdd;
    private ImageButton buttonAddPainter;

    private TextView titleText;
    private TextView addressText;
    private TextView startText;
    private TextView endText;
    private TextView costEstimateText;
    private TextView costTotalText;
    private TextView daysText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_info);

        Bundle extras=getIntent().getExtras();
        fromCompleted=extras.getInt(IntentExtra.FROM_COMPLETED);
        jobId = extras.getInt(IntentExtra.JOB_ID);

        dbHelper = new DatabaseHelper(this);
        job = dbHelper.getJobById(jobId);
        this.df = new DateFormatter();
        this.nf = NumberFormat.getCurrencyInstance();

        painterList = (ListView) findViewById(R.id.list_painters);
        View footer = LayoutInflater.from(this).inflate(R.layout.footer_spacer, painterList, false);
        painterList.addFooterView(footer, null, false);
        painters = dbHelper.getPaintersOnJob(jobId);
        adapter = new PaintersAdapter(this, R.layout.item_job_painter, painters);
        painterList.setAdapter(adapter);
        painterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(JobInfoActivity.this, PainterDayInfoActivity.class);
                i.putExtra(IntentExtra.JOB_ID, jobId);
                i.putExtra(IntentExtra.PAINTER_ID, painters.get(position).getId());
                startActivity(i);
            }
        });

        buttonCheck=(ImageButton)findViewById(R.id.button_complete);
        buttonAdd=(ImageButton)findViewById(R.id.button_add_work_day);
        buttonAddPainter=(ImageButton)findViewById(R.id.button_add_painter);

        if (fromCompleted==1) {
            buttonsBeGone();
        }

        titleText = (TextView) findViewById(R.id.text_title);
        addressText = (TextView) findViewById(R.id.text_address);
        startText = (TextView) findViewById(R.id.text_start_date);
        endText = (TextView) findViewById(R.id.text_end_date);
        costEstimateText = (TextView) findViewById(R.id.text_cost_estimate);
        costTotalText = (TextView) findViewById(R.id.text_cost_total);
        daysText = (TextView) findViewById(R.id.text_days_worked);

        setText(titleText, job.getTitle(), null, null);
        setText(addressText, job.getAddress(), null, null);
        setText(startText, job.getStartDate(), "Start date ", null);
        setText(endText, job.getEndDate(), "End date ", null);
        setText(costEstimateText, job.getCost(), "Estimated cost ", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        painters.clear();
        painters.addAll(dbHelper.getPaintersOnJob(jobId));
        adapter.notifyDataSetChanged();
        setTextZero(costTotalText, dbHelper.getJobTotalCost(jobId), "Total cost ", null);
        int daysWorked=dbHelper.getDaysWorked(jobId);
        String endText=" days worked";
        if (daysWorked==1) {
            endText=" day worked";
        }
        setText(daysText, daysWorked, null, endText);
    }

    public void onClickCompleted(View v) {
        CompletedDialog conf=new CompletedDialog();
        conf.show(getFragmentManager(), "Completed dialog");
    }

    public void onClickAddWorkday(View v) {
        Intent i = new Intent(JobInfoActivity.this, AddWorkdayActivity.class);
        i.putExtra(IntentExtra.JOB_ID, jobId);
        startActivity(i);
    }

    public void onClickAddPainter(View v) {
        Intent i = new Intent(JobInfoActivity.this, AddPainterActivity.class);
        i.putExtra(IntentExtra.JOB_ID, jobId);
        startActivity(i);
    }

    public void onClickDayInfo(View v) {
        Intent i=new Intent(JobInfoActivity.this,DayInfoActivity.class);
        i.putExtra(IntentExtra.JOB_ID, jobId);
        startActivity(i);
    }

    public void onClickExpenses(View v) {
        Intent i=new Intent(JobInfoActivity.this,ExpenseListActivity.class);
        i.putExtra(IntentExtra.JOB_ID, jobId);
        i.putExtra(IntentExtra.FROM_COMPLETED,fromCompleted);
        startActivity(i);
    }

    public void markComplete() {
        dbHelper.markJobCompleted(jobId);
        finish();
    }

    private void buttonsBeGone() {
        buttonCheck.setVisibility(View.GONE);
        buttonAdd.setVisibility(View.GONE);
        buttonAddPainter.setVisibility(View.GONE);
    }

    private void setText(TextView view, String value, String before, String after) {
        if (value != null) {
            if (before == null) {
                before = "";
            }
            if (after == null) {
                after = "";
            }
            view.setVisibility(View.VISIBLE);
            view.setText(before + value + after);
        }
    }

    private void setText(TextView view, Date value, String before, String after) {
        if (value != null) {
            if (before == null) {
                before = "";
            }
            if (after == null) {
                after = "";
            }
            view.setVisibility(View.VISIBLE);
            view.setText(before + df.getLongDateString(value) + after);
        }
    }

    private void setText(TextView view, double value, String before, String after) {
        if (value != 0) {
            if (before == null) {
                before = "";
            }
            if (after == null) {
                after = "";
            }
            view.setVisibility(View.VISIBLE);
            view.setText(before + nf.format(value) + after);
        }
    }

    private void setTextZero(TextView view, double value, String before, String after) {
        if (value >= 0) {
            if (before == null) {
                before = "";
            }
            if (after == null) {
                after = "";
            }
            view.setVisibility(View.VISIBLE);
            view.setText(before + nf.format(value) + after);
        }
    }

    private void setText(TextView view, int value, String before, String after) {
        if (value >= 0) {
            if (before == null) {
                before = "";
            }
            if (after == null) {
                after = "";
            }
            view.setVisibility(View.VISIBLE);
            view.setText(before + value + after);
        }
    }
}
