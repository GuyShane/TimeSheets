package com.shane.timesheets.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shane.timesheets.R;
import com.shane.timesheets.models.Painter;

import java.util.List;

public class AddPainterAdapter extends ArrayAdapter<Painter> {
    private List<Painter> painters;
    private int res;
    private Context ctx;

    public AddPainterAdapter(Context context, int resource, List<Painter> items) {
        super(context,resource,items);
        this.painters=items;
        this.res=resource;
        this.ctx=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Painter item=painters.get(position);
        if (convertView==null) {
            convertView= LayoutInflater.from(ctx).inflate(res,parent,false);
        }

        CheckBox check=(CheckBox)convertView.findViewById(R.id.check_painter);
        TextView name=(TextView)convertView.findViewById(R.id.text_name);

        name.setText(item.getName());

        return convertView;
    }
}
