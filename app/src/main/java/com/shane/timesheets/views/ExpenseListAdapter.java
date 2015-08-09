package com.shane.timesheets.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shane.timesheets.R;
import com.shane.timesheets.models.Expense;

import java.text.NumberFormat;
import java.util.List;

public class ExpenseListAdapter extends ArrayAdapter<Expense> {
    private Context ctx;
    private int res;
    private List<Expense> expenses;
    private NumberFormat cf;

    public ExpenseListAdapter(Context context, int resource, List<Expense> items) {
        super(context,resource,items);
        this.ctx=context;
        this.res=resource;
        this.expenses=items;
        this.cf=NumberFormat.getCurrencyInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Expense item=expenses.get(position);
        if (convertView==null) {
            convertView= LayoutInflater.from(ctx).inflate(res,parent,false);
        }

        TextView name=(TextView) convertView.findViewById(R.id.text_expense_name);
        TextView cost=(TextView) convertView.findViewById(R.id.text_expense_cost);

        name.setText(item.getName());
        cost.setText(cf.format(item.getCost()));

        return convertView;
    }
}
