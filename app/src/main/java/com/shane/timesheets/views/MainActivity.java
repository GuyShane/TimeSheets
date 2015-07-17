package com.shane.timesheets.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.shane.timesheets.R;


public class MainActivity extends FragmentActivity implements JobListFragment.OnFragmentInteractionListener {

    private ViewPager pager;
    private JobListFragmentAdapter fragAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragAdapter=new JobListFragmentAdapter(getSupportFragmentManager());
        pager=(ViewPager)findViewById(R.id.pager);
        pager.setAdapter(fragAdapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
