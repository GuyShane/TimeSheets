package com.shane.timesheets.views;

import android.app.Activity;
import android.os.Bundle;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.DateFormatter;
import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.PainterDay;
import com.shane.timesheets.models.WorkDay;

import java.text.DecimalFormat;
import java.util.List;

public class DayInfoActivity extends Activity {
    private int jobId;
    private DatabaseHelper dbHelper;
    private List<WorkDay> days;
    private DateFormatter df;
    private DecimalFormat nf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_info);

        df=new DateFormatter();
        nf=new DecimalFormat();
        nf.setDecimalSeparatorAlwaysShown(false);

        jobId=getIntent().getIntExtra(IntentExtra.JOB_ID,0);
        dbHelper=new DatabaseHelper(this);
        days=dbHelper.getWorkDays(jobId);

        for (WorkDay day:days) {
            System.out.println(df.getLongDateString(day.getDate()));
            for (PainterDay pday:day.getPainterDays()) {
                System.out.println("\t"+pday.getPainter().getName()+
                        " worked "+nf.format(pday.getHours())+" hours");
            }
        }
    }
}
