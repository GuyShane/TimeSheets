package com.shane.timesheets.views;

import android.os.Bundle;
import android.view.View;

import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Expense;

public class EditExpenseActivity extends NameNumberActivity {
    private int expenseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expenseId=getIntent().getIntExtra(IntentExtra.EXPENSE_ID,0);
        Expense expense=dbHelper.getExpense(expenseId);
        setupForm(getString(R.string.string_edit_expense_name_prompt),
                getString(R.string.string_edit_expense_number_prompt),expense.getName(),expense.getCost());
        setHeaderText(getString(R.string.header_edit_expense));
    }

    @Override
    public String getEmptyNameString() {
        return getString(R.string.toast_edit_expense_name_empty);
    }

    @Override
    public String getEmptyNumberString() {
        return getString(R.string.toast_edit_expense_number_empty);
    }

    @Override
    public void onClickCheck(View v) {
        if (validateForm()) {
            dbHelper.updateExpense(expenseId,name,number);
            makeMessage(getString(R.string.toast_expense_updated));
            finish();
        }
    }

    @Override
    public void onClickMenu(View v) {

    }
}
