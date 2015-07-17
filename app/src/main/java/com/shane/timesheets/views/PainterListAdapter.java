package com.shane.timesheets.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shane.timesheets.R;
import com.shane.timesheets.models.Painter;

import java.text.NumberFormat;
import java.util.List;

/**
 * This is an adapter to display a list of painters. It is used in conjunction
 * with PainterListActivity to display all saved painters
 */
public class PainterListAdapter extends ArrayAdapter<Painter> {
    private Context ctx;
    private int res;
    private List<Painter> painters;

    public PainterListAdapter(Context context, int resource, List<Painter> items) {
        super(context,resource,items);
        this.ctx=context;
        this.res=resource;
        this.painters=items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Painter item=painters.get(position);
        NumberFormat cf=NumberFormat.getCurrencyInstance();

        if (convertView==null) {
            convertView= LayoutInflater.from(ctx).inflate(res,parent,false);
        }

        TextView nameText=(TextView)convertView.findViewById(R.id.text_painter);
        TextView wageText=(TextView)convertView.findViewById(R.id.text_wage);

        nameText.setText(item.getName());
        wageText.setText(cf.format(item.getWage()));

        return convertView;
    }
}
