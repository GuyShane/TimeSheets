package com.shane.timesheets.views;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.shane.timesheets.R;


public class MainActivity extends FragmentActivity implements JobListFragment.OnFragmentInteractionListener {

    private ViewPager pager;
    private JobListFragmentAdapter fragAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragAdapter = new JobListFragmentAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(fragAdapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
