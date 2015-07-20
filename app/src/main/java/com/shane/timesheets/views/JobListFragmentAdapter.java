package com.shane.timesheets.views;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class JobListFragmentAdapter extends FragmentPagerAdapter {
    private static final int PAGES = 2;

    public JobListFragmentAdapter(FragmentManager manager) {
        super(manager);
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
                return "COMPLETED";
            default:
                return "IN PROGRESS";
        }
    }
}
