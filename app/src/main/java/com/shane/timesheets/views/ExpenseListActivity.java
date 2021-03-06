package com.shane.timesheets.views;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;

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
        expenseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(ExpenseListActivity.this,EditExpenseActivity.class);
                i.putExtra(IntentExtra.EXPENSE_ID,expenses.get(position).getId());
                startActivity(i);
            }
        });
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

    public void onClickMenu(View v) {
        PopupMenu menu = new PopupMenu(getApplicationContext(), v, Gravity.END);
        menu.getMenuInflater().inflate(R.menu.menu_expense_list, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                        HelpDialog help;
                        Bundle args=new Bundle();
                        if (fromCompleted==1) {
                            help=new HelpDialog();
                            args.putString(IntentExtra.HELP_MESSGAE,
                                    getString(R.string.action_help_expenses_completed));
                        }
                        else {
                            help=new HelpDialog();
                            args.putString(IntentExtra.HELP_MESSGAE,
                                    getString(R.string.action_help_expenses_not_completed));
                        }
                        help.setArguments(args);
                        help.show(getFragmentManager(),"Help Dialog");
                        break;
                }
                return true;
            }
        });
        menu.show();
    }

    private void buttonBeGone() {
        addButton.setVisibility(View.GONE);
    }
}
