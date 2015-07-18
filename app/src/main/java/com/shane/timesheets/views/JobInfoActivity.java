package com.shane.timesheets.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
    private int jobId;
    private Job job;
    private DatabaseHelper dbHelper;
    private DateFormatter df;
    private NumberFormat nf;

    private ListView painterList;
    private List<Painter> painters;
    private PaintersAdapter adapter;

    TextView titleText;
    TextView addressText;
    TextView startText;
    TextView endText;
    TextView costEstimateText;
    TextView costTotalText;
    TextView daysText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_info);

        jobId=getIntent().getIntExtra(IntentExtra.JOB_ID,1);

        dbHelper=new DatabaseHelper(this);
        job=dbHelper.getJob(jobId);
        this.df=new DateFormatter();
        this.nf =NumberFormat.getCurrencyInstance();

        painterList=(ListView)findViewById(R.id.list_painters);
        View footer=((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_spacer,null,false);
        //View footer=LayoutInflater.from(this).inflate(R.layout.footer_spacer, null, false);
        painterList.addFooterView(footer, null, false);
        painters=dbHelper.getPainters(jobId);
        adapter=new PaintersAdapter(this,R.layout.item_job_painter,painters);
        painterList.setAdapter(adapter);

        titleText=(TextView)findViewById(R.id.text_title);
        addressText=(TextView)findViewById(R.id.text_address);
        startText=(TextView)findViewById(R.id.text_start_date);
        endText=(TextView)findViewById(R.id.text_end_date);
        costEstimateText=(TextView)findViewById(R.id.text_cost_estimate);
        costTotalText=(TextView)findViewById(R.id.text_cost_total);
        daysText=(TextView)findViewById(R.id.text_days_worked);

        setText(titleText,job.getTitle(),null,null);
        setText(addressText,job.getAddress(),null,null);
        setText(startText,job.getStartDate(),"Start date ",null);
        setText(endText,job.getEndDate(),"End date ",null);
        setText(costEstimateText, job.getCost(), "Estimated cost ", null);
        setText(costTotalText,dbHelper.getTotalCost(jobId),"Total cost ",null);
        setText(daysText,dbHelper.getDaysWorked(jobId),null," days worked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        painters.clear();
        painters.addAll(dbHelper.getPainters(jobId));
        adapter.notifyDataSetChanged();
    }

    public void onClickAddPainter(View v) {
        Intent i=new Intent(JobInfoActivity.this,AddPainterActivity.class);
        i.putExtra(IntentExtra.JOB_ID,jobId);
        startActivity(i);
    }

    private void setText(TextView view, String value, String before, String after) {
        if (value!=null) {
            if (before==null) {
                before="";
            }
            if (after==null) {
                after="";
            }
            view.setVisibility(View.VISIBLE);
            view.setText(before+value+after);
        }
    }

    private void setText(TextView view, Date value, String before, String after) {
        if (value!=null) {
            if (before==null) {
                before="";
            }
            if (after==null) {
                after="";
            }
            view.setVisibility(View.VISIBLE);
            view.setText(before+df.getLongDateString(value)+after);
        }
    }

    private void setText(TextView view, double value, String before, String after) {
        if (value!=0) {
            if (before==null) {
                before="";
            }
            if (after==null) {
                after="";
            }
            view.setVisibility(View.VISIBLE);
            view.setText(before+nf.format(value)+after);
        }
    }

    private void setText(TextView view, int value, String before, String after) {
        if (value>=0) {
            if (before==null) {
                before="";
            }
            if (after==null) {
                after="";
            }
            view.setVisibility(View.VISIBLE);
            view.setText(before+value+after);
        }
    }
}
