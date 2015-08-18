package com.shane.timesheets.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Job;
import com.shane.timesheets.models.Painter;
import com.shane.timesheets.models.PainterDay;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class PainterDayInfoActivity extends Activity {

    private int jobId;
    private int painterId;
    private DatabaseHelper dbHelper;
    private List<PainterDay> painterDays;
    private PainterDayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painter_day_info);

        NumberFormat cf = NumberFormat.getCurrencyInstance();
        DecimalFormat nf = new DecimalFormat();
        nf.setDecimalSeparatorAlwaysShown(false);

        TextView jobName=(TextView) findViewById(R.id.text_job_name);
        TextView painterName=(TextView)findViewById(R.id.text_painter);
        TextView painterWage=(TextView)findViewById(R.id.text_wage);
        TextView hoursWorked=(TextView)findViewById(R.id.text_hours_worked);
        TextView amountDue=(TextView)findViewById(R.id.text_amount_due);

        Bundle extras=getIntent().getExtras();
        jobId=extras.getInt(IntentExtra.JOB_ID);
        painterId=extras.getInt(IntentExtra.PAINTER_ID);

        dbHelper=new DatabaseHelper(this);
        Job job = dbHelper.getJobById(jobId);
        Painter painter = dbHelper.getPainterById(painterId);
        painterDays=dbHelper.getPainterDays(jobId, painterId);

        ListView dayList = (ListView) findViewById(R.id.list_days);
        View footer= LayoutInflater.from(this).inflate(R.layout.footer_spacer, dayList,false);
        dayList.addFooterView(footer, null, false);
        adapter=new PainterDayAdapter(this,R.layout.item_painter_day,painterDays);
        dayList.setAdapter(adapter);

        double totalHours=0;
        double owed=0;
        for (PainterDay day:painterDays) {
            totalHours+=day.getHours();
            owed+= painter.getWage()*day.getHours();
        }

        jobName.setText(job.getTitle());
        painterName.setText(painter.getName());
        painterWage.setText(cf.format(painter.getWage()));
        hoursWorked.setText(nf.format(totalHours)+
                getString(R.string.string_painter_info_hours_worked));
        amountDue.setText(cf.format(owed)+getString(R.string.string_painter_info_owed));
    }

    @Override
    protected void onResume() {
        super.onResume();
        painterDays.clear();
        painterDays.addAll(dbHelper.getPainterDays(jobId, painterId));
        adapter.notifyDataSetChanged();
    }
}
