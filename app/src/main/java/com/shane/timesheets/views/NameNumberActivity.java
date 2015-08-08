package com.shane.timesheets.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.R;

public abstract class NameNumberActivity extends Activity {
    protected DatabaseHelper dbHelper;
    protected String name;
    protected double number;

    protected EditText nameText;
    protected EditText numberText;
    protected TextView namePrompt;
    protected TextView numberPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_number);
        dbHelper = new DatabaseHelper(this);

        nameText = (EditText) findViewById(R.id.edit_name);
        numberText = (EditText) findViewById(R.id.edit_number);
        namePrompt=(TextView) findViewById(R.id.text_name);
        numberPrompt=(TextView)findViewById(R.id.text_number);
    }

    public void setupForm(String namePrompt, String numberPrompt) {
        this.namePrompt.setText(namePrompt);
        this.numberPrompt.setText(numberPrompt);
    }

    public void setupForm(String namePrompt, String numberPrompt, String name, double wage) {
        this.namePrompt.setText(namePrompt);
        this.numberPrompt.setText(numberPrompt);
        this.nameText.setText(name);
        this.numberText.setText(String.valueOf(wage));
    }

    protected boolean validateForm() {
        if (nameText.getText().toString().isEmpty()) {
            makeMessage(getEmptyNameString());
            return false;
        } else {
            name = nameText.getText().toString();
        }
        if (numberText.getText().toString().isEmpty()) {
            makeMessage(getEmptyNumberString());
            return false;
        } else {
            number = Double.valueOf(numberText.getText().toString());
        }
        return true;
    }

    public abstract String getEmptyNameString();
    public abstract String getEmptyNumberString();
    public abstract void onClickCheck(View v);
    public abstract void onClickMenu(View v);

    protected void makeMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
