package com.shane.timesheets.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Painter;

public class NewPainterActivity extends Activity {
    private DatabaseHelper dbHelper;
    private String name;
    private double wage;

    private EditText nameText;
    private EditText wageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_painter);
        dbHelper = new DatabaseHelper(this);

        nameText = (EditText) findViewById(R.id.edit_name);
        wageText = (EditText) findViewById(R.id.edit_wage);
    }

    public void onClickCheck(View v) {
        if (validateForm()) {
            Painter newPainter = new Painter(name, wage);
            if (dbHelper.insertPainter(newPainter)) {
                makeMessage("Painter saved");
                finish();
            } else {
                makeMessage("Save failed");
            }
        }
    }

    public void onClickCancel(View v) {
        makeMessage("New painter cancelled");
        finish();
    }

    public void onClickMenu(View v) {

    }

    private boolean validateForm() {
        if (nameText.getText().toString().isEmpty()) {
            makeMessage("The painter needs a name...");
            return false;
        } else {
            name = nameText.getText().toString();
        }
        if (wageText.getText().toString().isEmpty()) {
            makeMessage("You gotta pay them something");
            return false;
        } else {
            wage = Double.valueOf(wageText.getText().toString());
        }
        return true;
    }

    private void makeMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
