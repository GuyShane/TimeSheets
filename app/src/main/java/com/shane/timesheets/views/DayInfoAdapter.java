package com.shane.timesheets.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.shane.timesheets.DateFormatter;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Painter;
import com.shane.timesheets.models.PainterDay;
import com.shane.timesheets.models.WorkDay;

import java.text.DecimalFormat;
import java.util.List;

public class DayInfoAdapter extends BaseExpandableListAdapter {
    private Context ctx;
    private int groupRes;
    private int childRes;
    private List<WorkDay> days;
    private DateFormatter df;
    private DecimalFormat nf;

    public DayInfoAdapter(Context contex, int groupResource, int childResource, List<WorkDay> items) {
        this.ctx=contex;
        this.groupRes=groupResource;
        this.childRes=childResource;
        this.days=items;
        df=new DateFormatter();
        nf=new DecimalFormat();
        nf.setDecimalSeparatorAlwaysShown(false);
    }

    @Override
    public int getGroupCount() {
        return days.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return days.get(groupPosition).getPainterDays().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return days.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return days.get(groupPosition).getPainterDays().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition*childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        WorkDay item=days.get(groupPosition);
        if (convertView==null) {
            convertView= LayoutInflater.from(ctx).inflate(groupRes,parent,false);
        }

        TextView dateText=(TextView)convertView.findViewById(R.id.text_date);

        dateText.setText(df.getLongDateString(item.getDate()));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        PainterDay item=days.get(groupPosition).getPainterDays().get(childPosition);
        Painter p=item.getPainter();
        if (convertView==null) {
            convertView= LayoutInflater.from(ctx).inflate(childRes,parent,false);
        }

        TextView nameText=(TextView)convertView.findViewById(R.id.text_painter);
        TextView hoursText=(TextView)convertView.findViewById(R.id.text_hours);

        nameText.setText(p.getName());
        hoursText.setText(nf.format(item.getHours()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
