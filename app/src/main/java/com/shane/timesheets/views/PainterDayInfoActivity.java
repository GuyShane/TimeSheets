package com.shane.timesheets.views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Job;
import com.shane.timesheets.models.Painter;

import java.text.NumberFormat;

public class PainterDayInfoActivity extends Activity {

    private int jobId;
    private Job job;
    private int painterId;
    private Painter painter;
    private DatabaseHelper dbHelper;
    private NumberFormat cf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painter_day_info);

        cf=NumberFormat.getCurrencyInstance();

        TextView jobName=(TextView) findViewById(R.id.text_job_name);
        TextView painterName=(TextView)findViewById(R.id.text_painter);
        TextView painterWage=(TextView)findViewById(R.id.text_wage);
        TextView hoursWorked=(TextView)findViewById(R.id.text_hours_worked);
        TextView amountDue=(TextView)findViewById(R.id.text_amount_due);

        Bundle extras=getIntent().getExtras();
        jobId=extras.getInt(IntentExtra.JOB_ID);
        painterId=extras.getInt(IntentExtra.PAINTER_ID);

        dbHelper=new DatabaseHelper(this);
        job=dbHelper.getJobById(jobId);
        painter=dbHelper.getPainterById(painterId);

        jobName.setText(job.getTitle());
        painterName.setText(painter.getName());
        painterWage.setText(cf.format(painter.getWage()));
    }
}
