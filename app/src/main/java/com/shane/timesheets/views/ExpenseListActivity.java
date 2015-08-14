package com.shane.timesheets.views;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Expense;

import java.util.List;

public class ExpenseListActivity extends Activity {
    private ListView expenseList;
    private ExpenseListAdapter adapter;
    private List<Expense> expenses;
    private DatabaseHelper dbHelper;
    private int jobId;
    private int fromCompleted;
    private ImageButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        addButton=(ImageButton) findViewById(R.id.button_add_expense);

        dbHelper=new DatabaseHelper(this);
        jobId=getIntent().getIntExtra(IntentExtra.JOB_ID,0);
        fromCompleted=getIntent().getIntExtra(IntentExtra.FROM_COMPLETED,0);
        if (fromCompleted==1) {
            buttonBeGone();
        }

        expenseList=(ListView)findViewById(R.id.list_expenses);
        expenses=dbHelper.getExpenses(jobId);
        adapter=new ExpenseListAdapter(this,R.layout.item_expense,expenses);
        View footer = LayoutInflater.from(this).inflate(R.layout.footer_spacer, expenseList, false);
        expenseList.addFooterView(footer,null,false);
        expenseList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        expenses.clear();
        expenses.addAll(dbHelper.getExpenses(jobId));
        adapter.notifyDataSetChanged();
    }

    public void onClickAddExpense(View v) {
        Intent i=new Intent(ExpenseListActivity.this,NewExpenseActivity.class);
        i.putExtra(IntentExtra.JOB_ID, jobId);
        startActivity(i);
    }

    private void buttonBeGone() {
        addButton.setVisibility(View.GONE);
    }
}
