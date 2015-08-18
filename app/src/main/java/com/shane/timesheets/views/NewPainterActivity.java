package com.shane.timesheets.views;

import android.os.Bundle;
import android.view.View;

import com.shane.timesheets.R;
import com.shane.timesheets.models.Painter;

public class NewPainterActivity extends NameNumberActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupForm(getString(R.string.string_new_painter_name_prompt),
                getString(R.string.string_new_painter_number_prompt));
        setHeaderText(getString(R.string.header_edit_painter));
    }

    @Override
    public void onClickCheck(View v) {
        if (validateForm()) {
            Painter newPainter = new Painter(name, number);
            if (dbHelper.insertPainter(newPainter)) {
                makeMessage(getString(R.string.toast_painter_saved));
                finish();
            } else {
                makeMessage(getString(R.string.toast_save_failed));
            }
        }
    }

    @Override
    public void onClickMenu(View v) {

    }

    @Override
    public String getEmptyNameString() {
        return getString(R.string.toast_new_painter_name_empty);
    }

    @Override
    public String getEmptyNumberString() {
        return getString(R.string.toast_new_painter_number_empty);
    }
}
