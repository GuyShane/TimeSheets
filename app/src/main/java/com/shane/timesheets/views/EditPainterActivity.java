package com.shane.timesheets.views;

import android.os.Bundle;
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
        setupForm(getString(R.string.string_edit_painter_name_prompt),
                getString(R.string.string_edit_painter_number_prompt),
                painter.getName(), painter.getWage());
        setHeaderText(getString(R.string.header_edit_painter));
    }

    @Override
    public void onClickCheck(View v) {
        if (validateForm()) {
            dbHelper.updatePainter(painterId,name,number);
            makeMessage(getString(R.string.toast_painter_updated));
            finish();
        }
    }

    @Override
    public void onClickMenu(View v) {

    }

    @Override
    public String getEmptyNameString() {
        return getString(R.string.toast_edit_painter_name_empty);
    }

    @Override
    public String getEmptyNumberString() {
        return getString(R.string.toast_edit_painter_number_empty);
    }
}
