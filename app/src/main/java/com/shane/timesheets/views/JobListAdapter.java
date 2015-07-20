package com.shane.timesheets.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shane.timesheets.DateFormatter;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Job;

import java.text.NumberFormat;
import java.util.List;

public class JobListAdapter extends ArrayAdapter<Job> {
    private Context ctx;
    private List<Job> jobs;
    private DateFormatter df;
    private int res;
    private NumberFormat currencyFormatter;

    public JobListAdapter(Context context, int resource, List<Job> items) {
        super(context, resource, items);
        this.ctx = context;
        this.jobs = items;
        this.df = new DateFormatter();
        this.res = resource;
        this.currencyFormatter = NumberFormat.getCurrencyInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Job item = jobs.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(res, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.text_title);
        TextView address = (TextView) convertView.findViewById(R.id.text_address);
        TextView startDate = (TextView) convertView.findViewById(R.id.text_start_date);
        TextView endDate = (TextView) convertView.findViewById(R.id.text_end_date);
        TextView cost = (TextView) convertView.findViewById(R.id.text_cost);

        title.setText(item.getTitle());
        if (item.getAddress() != null) {
            address.setVisibility(View.VISIBLE);
            address.setText(item.getAddress());
        }
        if (item.getStartDate() != null) {
            startDate.setVisibility(View.VISIBLE);
            startDate.setText("Start date " + df.getLongDateString(item.getStartDate()));
        }
        if (item.getEndDate() != null) {
            endDate.setVisibility(View.VISIBLE);
            endDate.setText("End date " + df.getLongDateString(item.getEndDate()));
        }
        if (item.getCost() != 0) {
            cost.setVisibility(View.VISIBLE);
            cost.setText(currencyFormatter.format(item.getCost()));
        }

        return convertView;
    }
}
