package com.shane.timesheets.views;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Painter;

public class EditPainterActivity extends NameNumberActivity {
    private int painterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras=getIntent().getExtras();
        painterId=extras.getInt(IntentExtra.PAINTER_ID);
        Painter painter = dbHelper.getPainterById(painterId);
        setupForm("Name","Wage", painter.getName(), painter.getWage());
    }

    @Override
    public void onClickCheck(View v) {
        if (validateForm()) {
            dbHelper.updatePainter(painterId,name,number);
            makeMessage("Painter updated");
            finish();
        }
    }

    @Override
    public void onClickMenu(View v) {

    }

    @Override
    public String getEmptyNameString() {
        return "You can't take away their name";
    }

    @Override
    public String getEmptyNumberString() {
        return "Not going to pay them anymore?";
    }
}
