package com.shane.timesheets.views;

import android.os.Bundle;
import android.view.View;

import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Job;

public class EditJobActivity extends JobInfoEntryActivity {
    private int jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobId=getIntent().getIntExtra(IntentExtra.JOB_ID,0);
        Job job=dbHelper.getJobById(jobId);
        setupForm(job.getTitle(),job.getAddress(),
                job.getStartDate(),job.getEndDate(),job.getCost());
        setHeaderText(getString(R.string.header_edit_job));
    }

    @Override
    public void onClickCheck(View v) {
        if (validateForm()) {
            dbHelper.updateJob(jobId,title,address,getStartDateString(),getEndDateString(),cost);
            makeMessage(getString(R.string.toast_job_updated));
            finish();
        }
    }

    @Override
    public void onClickMenu(View v) {

    }
}
