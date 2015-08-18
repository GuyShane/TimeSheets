package com.shane.timesheets.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.DateFormatter;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Painter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class JobInfoEntryActivity extends Activity {

    protected ListView painterList;
    private AddPainterAdapter adapter;
    protected List<Painter> painters;

    private EditText titleText;
    private EditText addressText;
    private EditText startDateText;
    private EditText endDateText;
    private EditText costText;
    private TextView painterText;

    protected DatabaseHelper dbHelper;
    protected DateFormatter df;
    protected Context ctx;

    protected String title;
    protected String address;
    protected String startDate;
    protected String endDate;
    protected double cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_info_entry_list);

        this.ctx = this;

        painterList=(ListView)findViewById(R.id.list_painters);
        View formView = LayoutInflater.from(ctx).
                inflate(R.layout.activity_job_info_entry_form, painterList, false);
        View footerView=LayoutInflater.from(ctx).
                inflate(R.layout.footer_spacer, painterList, false);
        painters=new ArrayList<>();
        adapter=new AddPainterAdapter(ctx,R.layout.item_add_painter,painters);
        painterList.addHeaderView(formView, null, false);
        painterList.addFooterView(footerView, null, false);
        painterList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        painterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray checked = painterList.getCheckedItemPositions();
                CheckBox check = (CheckBox) view.findViewById(R.id.check_painter);
                for (int i = 0; i < checked.size(); i++) {
                    if (checked.keyAt(i) == position) {
                        check.setChecked(checked.valueAt(i));
                    }
                }
            }
        });
        painterList.setAdapter(adapter);

        titleText = (EditText) formView.findViewById(R.id.edit_title);
        addressText = (EditText) formView.findViewById(R.id.edit_address);
        startDateText = (EditText) formView.findViewById(R.id.edit_start_date);
        endDateText = (EditText) formView.findViewById(R.id.edit_end_date);
        costText = (EditText) formView.findViewById(R.id.edit_cost);

        painterText=(TextView) formView.findViewById(R.id.text_painters);

        this.dbHelper = new DatabaseHelper(ctx);
        this.df = new DateFormatter();

        setupDateListeners();
    }

    private void setupDateListeners() {
        final Calendar cal = Calendar.getInstance();
        final DatePickerDialog startDatePicker = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDateText.setText(df.getShortDateString(year, monthOfYear, dayOfMonth));
                        startDateText.setTag(df.getYMDString(year, monthOfYear, dayOfMonth));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePicker.show();
            }
        });
        final DatePickerDialog endDatePicker = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDateText.setText(df.getShortDateString(year, monthOfYear, dayOfMonth));
                        endDateText.setTag(df.getYMDString(year, monthOfYear, dayOfMonth));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        endDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePicker.show();
            }
        });
    }

    public void setupForm() {
        painters.clear();
        painters.addAll(dbHelper.getAllPainters());
        adapter.notifyDataSetChanged();
        startDateText.setText(df.getShortDateString());
        startDateText.setTag(df.getYMDString());
    }

    public void setupForm(String title, String address, Date startDate, Date endDate, double cost) {
        painters.clear();
        adapter.notifyDataSetChanged();
        painterText.setVisibility(View.GONE);
        if (title!=null) {
            titleText.setText(title);
        }
        if (address!=null) {
            addressText.setText(address);
        }
        if (startDate!=null) {
            startDateText.setText(df.getShortDateString(startDate));
            startDateText.setTag(df.getYMDString(startDate));
        }
        if (endDate!=null) {
            endDateText.setText(df.getShortDateString(endDate));
            endDateText.setTag(df.getYMDString(endDate));
        }
        if (cost!=0) {
            costText.setText(String.valueOf(cost));
        }
    }

    public abstract void onClickCheck(View v);
    public abstract void onClickMenu(View v);

    public String getStartDateString() {
        return (String) startDateText.getTag();
    }

    public String getEndDateString() {
        return (String) endDateText.getTag();
    }

    public boolean validateForm() {
        if (titleText.getText().toString().isEmpty()) {
            makeMessage("You need to set a title, man");
            return false;
        } else {
            title = titleText.getText().toString();
        }

        if (addressText.getText().toString().isEmpty()) {
            address = null;
        } else {
            address = addressText.getText().toString();
        }

        if (startDateText.getText().toString().isEmpty()) {
            startDate = null;
        } else {
            startDate = startDateText.getTag().toString();
        }

        if (endDateText.getText().toString().isEmpty()) {
            endDate = null;
        } else {
            endDate = endDateText.getTag().toString();
        }

        if (costText.getText().toString().isEmpty()) {
            cost = 0;
        } else {
            cost = Double.valueOf(costText.getText().toString());
        }

        return true;
    }

    public void makeMessage(String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

}
