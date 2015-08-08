package com.shane.timesheets.views;

import android.os.Bundle;
import android.view.View;

import com.shane.timesheets.models.Painter;

public class NewPainterActivity extends NameNumberActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupForm("Name","Wage");
    }

    @Override
    public void onClickCheck(View v) {
        if (validateForm()) {
            Painter newPainter = new Painter(name, number);
            if (dbHelper.insertPainter(newPainter)) {
                makeMessage("Painter saved");
                finish();
            } else {
                makeMessage("Save failed");
            }
        }
    }

    @Override
    public void onClickMenu(View v) {

    }

    @Override
    public String getEmptyNameString() {
        return "They gotta have a name";
    }

    @Override
    public String getEmptyNumberString() {
        return "You gotta pay them something...";
    }
}
