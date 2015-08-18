package com.shane.timesheets.views;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shane.timesheets.R;


public class JobListFragmentAdapter extends FragmentPagerAdapter {
    private static final int PAGES = 2;
    private Context ctx;

    public JobListFragmentAdapter(FragmentManager manager, Context context) {
        super(manager);
        ctx=context;
    }

    @Override
    public Fragment getItem(int i) {
        return JobListFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 1:
                return ctx.getString(R.string.tab_completed);
            default:
                return ctx.getString(R.string.tab_in_progress);
        }
    }
}
