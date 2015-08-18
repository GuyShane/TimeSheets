package com.shane.timesheets.views;

import android.os.Bundle;
import android.view.View;

import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Expense;

public class NewExpenseActivity extends NameNumberActivity {
    private int jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupForm(getString(R.string.string_expense_name_prompt),
                getString(R.string.string_expense_number_prompt));
        jobId=getIntent().getIntExtra(IntentExtra.JOB_ID,0);
        setHeaderText(getString(R.string.header_new_expense));
    }

    @Override
    public String getEmptyNameString() {
        return getString(R.string.toast_expense_name_empty);
    }

    @Override
    public String getEmptyNumberString() {
        return getString(R.string.toast_expense_number_empty);
    }

    @Override
    public void onClickCheck(View v) {
        if (validateForm()) {
            Expense newExpense=new Expense(name, number);
            if (dbHelper.insertExpense(newExpense,jobId)) {
                makeMessage(getString(R.string.toast_expense_saved));
                finish();
            } else {
                makeMessage(getString(R.string.toast_save_failed));
            }
        }
    }

    @Override
    public void onClickMenu(View v) {

    }
}
