package com.shane.timesheets.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.shane.timesheets.DatabaseContract;
import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.DateFormatter;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class NewJobActivity extends Activity {


    private EditText titleText;
    private EditText addressText;
    private EditText startDateText;
    private EditText endDateText;
    private EditText costText;

    private DatabaseHelper dbHelper;
    private DateFormatter df;
    private Context ctx;

    private String title;
    private String address;
    private String startDate;
    private String endDate;
    private double cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);

        this.ctx=this;
        titleText =(EditText) findViewById(R.id.edit_title);
        addressText =(EditText) findViewById(R.id.edit_address);
        startDateText =(EditText) findViewById(R.id.edit_start_date);
        endDateText =(EditText) findViewById(R.id.edit_end_date);
        costText =(EditText) findViewById(R.id.edit_cost);

        this.dbHelper=new DatabaseHelper(ctx);
        this.df=new DateFormatter();

        setupForm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_job, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupForm() {
        final Calendar cal=Calendar.getInstance();
        startDateText.setText(df.getShortDateString());
        startDateText.setTag(df.getDMYString());
        final DatePickerDialog startDatePicker=new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDateText.setText(df.getShortDateString(year, monthOfYear, dayOfMonth));
                        startDateText.setTag(df.getDMYString(year,monthOfYear,dayOfMonth));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePicker.show();
            }
        });
        final DatePickerDialog endDatePicker=new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDateText.setText(df.getShortDateString(year, monthOfYear, dayOfMonth));
                        endDateText.setTag(df.getDMYString(year,monthOfYear,dayOfMonth));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        endDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePicker.show();
            }
        });
    }

    public void onClickCheck(View v) {
        if (validateForm()) {
            Job newJob=new Job(title,address,startDate,endDate,cost);
            if (dbHelper.insertJob(newJob)) {
                makeMessage("Job saved");
                finish();
            }
            else {
                makeMessage("Save failed");
            }
        }
    }

    public void onClickCancel(View v) {
        makeMessage("New job cancelled");
        finish();
    }

    public void onClickMenu(View v) {

    }

    private boolean validateForm() {
        if (titleText.getText().toString().isEmpty()) {
            makeMessage("You need to set a title, man");
            return false;
        }
        else {
            title=titleText.getText().toString();
        }

        if (addressText.getText().toString().isEmpty()) {
            address=null;
        }
        else {
            address=addressText.getText().toString();
        }

        if (startDateText.getText().toString().isEmpty()) {
            startDate=null;
        }
        else {
            startDate=startDateText.getTag().toString();
        }

        if (endDateText.getText().toString().isEmpty()) {
            endDate=null;
        } else {
            endDate=endDateText.getTag().toString();
        }

        if (costText.getText().toString().isEmpty()) {
            cost=0;
        } else {
            cost=Double.valueOf(costText.getText().toString());
        }

        return true;
    }

    private void makeMessage(String message) {
        Toast.makeText(ctx,message,Toast.LENGTH_SHORT).show();
    }
}
