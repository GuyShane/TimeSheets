package com.shane.timesheets.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shane.timesheets.R;
import com.shane.timesheets.models.Painter;

import java.util.List;

/**
 * This is an adapter to display a list of apinters currently on a given job.
 * It is used from JobInfoActivity
 */
public class PaintersAdapter extends ArrayAdapter<Painter> {
    private List<Painter> painters;
    private Context ctx;
    private int res;

    public PaintersAdapter(Context context, int resource, List<Painter> items) {
        super(context,resource,items);
        painters=items;
        ctx=context;
        res=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Painter item=painters.get(position);
        if (convertView==null) {
            convertView= LayoutInflater.from(ctx).inflate(res,parent,false);
        }
        TextView painter=(TextView)convertView.findViewById(R.id.text_painter);
        painter.setText(item.getName());
        return convertView;
    }
}
