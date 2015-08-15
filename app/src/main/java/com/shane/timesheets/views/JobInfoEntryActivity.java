package com.shane.timesheets.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.DateFormatter;
import com.shane.timesheets.R;

import java.util.Calendar;
import java.util.Date;

public abstract class JobInfoEntryActivity extends Activity {

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

        this.ctx = this;
        titleText = (EditText) findViewById(R.id.edit_title);
        addressText = (EditText) findViewById(R.id.edit_address);
        startDateText = (EditText) findViewById(R.id.edit_start_date);
        endDateText = (EditText) findViewById(R.id.edit_end_date);
        costText = (EditText) findViewById(R.id.edit_cost);

        this.dbHelper = new DatabaseHelper(ctx);
        this.df = new DateFormatter();

        setupDateListeners();
    }

    private void setupDateListeners() {
        final Calendar cal = Calendar.getInstance();
        final DatePickerDialog startDatePicker = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDateText.setText(df.getShortDateString(year, monthOfYear, dayOfMonth));
                        startDateText.setTag(df.getYMDString(year, monthOfYear, dayOfMonth));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePicker.show();
            }
        });
        final DatePickerDialog endDatePicker = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDateText.setText(df.getShortDateString(year, monthOfYear, dayOfMonth));
                        endDateText.setTag(df.getYMDString(year, monthOfYear, dayOfMonth));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        endDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePicker.show();
            }
        });
    }

    public void setupForm() {
        startDateText.setText(df.getShortDateString());
        startDateText.setTag(df.getYMDString());
    }

    public void setupForm(String title, String address, Date startDate, Date endDate, double cost) {
        if (title!=null) {
            titleText.setText(title);
        }
        if (address!=null) {
            addressText.setText(address);
        }
        if (startDate!=null) {
            startDateText.setText(df.getShortDateString(startDate));
            startDateText.setTag(df.getYMDString(startDate));
        }
        if (endDate!=null) {
            endDateText.setText(df.getShortDateString(endDate));
            endDateText.setTag(df.getYMDString(endDate));
        }
        if (cost!=0) {
            costText.setText(String.valueOf(cost));
        }
    }

    public abstract void onClickCheck(View v);
    public abstract void onClickMenu(View v);

    public boolean validateForm() {
        if (titleText.getText().toString().isEmpty()) {
            makeMessage("You need to set a title, man");
            return false;
        } else {
            title = titleText.getText().toString();
        }

        if (addressText.getText().toString().isEmpty()) {
            address = null;
        } else {
            address = addressText.getText().toString();
        }

        if (startDateText.getText().toString().isEmpty()) {
            startDate = null;
        } else {
            startDate = startDateText.getTag().toString();
        }

        if (endDateText.getText().toString().isEmpty()) {
            endDate = null;
        } else {
            endDate = endDateText.getTag().toString();
        }

        if (costText.getText().toString().isEmpty()) {
            cost = 0;
        } else {
            cost = Double.valueOf(costText.getText().toString());
        }

        return true;
    }

    public void makeMessage(String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

}
