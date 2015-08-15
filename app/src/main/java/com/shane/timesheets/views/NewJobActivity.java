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

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.DateFormatter;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Job;

import java.util.Calendar;


public class NewJobActivity extends JobInfoEntryActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupForm();
    }

    @Override
    public void onClickCheck(View v) {
        if (validateForm()) {
            Job newJob = new Job(title, address, startDate, endDate, cost);
            if (dbHelper.insertJob(newJob)) {
                makeMessage("Job saved");
                finish();
            } else {
                makeMessage("Save failed");
            }
        }
    }

    @Override
    public void onClickMenu(View v) {

    }
}
