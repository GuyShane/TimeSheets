package com.shane.timesheets.views;

import android.os.Bundle;
import android.view.View;

import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.models.Expense;

public class NewExpenseActivity extends NameNumberActivity {
    private int jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupForm("Name", "Cost");
        jobId=getIntent().getIntExtra(IntentExtra.JOB_ID,0);
    }

    @Override
    public String getEmptyNameString() {
        return "The expense must have a title";
    }

    @Override
    public String getEmptyNumberString() {
        return "It had to cost something, no?";
    }

    @Override
    public void onClickCheck(View v) {
        if (validateForm()) {
            Expense newExpense=new Expense(name, number);
            if (dbHelper.insertExpense(newExpense,jobId)) {
                makeMessage("Expense saved");
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
