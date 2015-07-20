package com.shane.timesheets.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shane.timesheets.DateFormatter;
import com.shane.timesheets.R;
import com.shane.timesheets.models.PainterDay;

import java.text.DecimalFormat;
import java.util.List;

public class PainterDayAdapter extends ArrayAdapter<PainterDay> {
    private Context ctx;
    private int res;
    private List<PainterDay> days;

    public PainterDayAdapter(Context context, int resource, List<PainterDay> items) {
        super(context,resource,items);
        this.ctx=context;
        this.res=resource;
        this.days=items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PainterDay item=days.get(position);
        DateFormatter df=new DateFormatter();
        DecimalFormat nf=new DecimalFormat();
        nf.setDecimalSeparatorAlwaysShown(false);

        if (convertView==null) {
            convertView= LayoutInflater.from(ctx).inflate(res,parent,false);
        }

        TextView dateText=(TextView)convertView.findViewById(R.id.text_date);
        TextView hoursText=(TextView)convertView.findViewById(R.id.text_hours);

        dateText.setText(df.getShortDateString(item.getDate()));
        hoursText.setText(nf.format(item.getHours()));

        return convertView;
    }
}
