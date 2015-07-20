package com.shane.timesheets.views;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.shane.timesheets.DatabaseHelper;
import com.shane.timesheets.IntentExtra;
import com.shane.timesheets.R;
import com.shane.timesheets.models.Job;

import java.util.List;


public class JobListFragment extends Fragment {
    private static final String PAGE = "page";

    private DatabaseHelper dbHelper;

    private int num;
    private OnFragmentInteractionListener mListener;

    private View.OnClickListener onClickMenu;
    private View.OnClickListener onClickAdd;

    private ImageButton addButton;
    private ImageButton menuButton;
    private ListView jobList;
    private List<Job> jobs;
    private JobListAdapter adapter;


    public static JobListFragment newInstance(int num) {
        JobListFragment fragment = new JobListFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE, num);
        fragment.setArguments(args);
        return fragment;
    }

    public JobListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        num = getArguments().getInt(PAGE, 0);

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());

        jobs = dbHelper.getAllJobs(num);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_job_list, container, false);

        setupClickListeners();

        addButton = (ImageButton) v.findViewById(R.id.button_add);
        menuButton = (ImageButton) v.findViewById(R.id.button_menu);

        addButton.setOnClickListener(onClickAdd);
        menuButton.setOnClickListener(onClickMenu);

        if (num == 1) {
            addButton.setVisibility(View.GONE);
        }

        jobList = (ListView) v.findViewById(R.id.list_jobs);
        adapter = new JobListAdapter(getActivity().getApplicationContext(), R.layout.item_job, jobs);
        jobList.setAdapter(adapter);
        jobList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= jobList.getHeaderViewsCount();
                Intent i = new Intent(getActivity(), JobInfoActivity.class);
                i.putExtra(IntentExtra.JOB_ID, jobs.get(position).getId());
                i.putExtra(IntentExtra.FROM_COMPLETED,num);
                startActivity(i);
            }
        });
        View footer = inflater.inflate(R.layout.footer_spacer, container, false);
        jobList.addFooterView(footer, null, false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        jobs.clear();
        jobs.addAll(dbHelper.getAllJobs(num));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setupClickListeners() {
        onClickMenu = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(getActivity().getApplicationContext(), v, Gravity.END);
                menu.getMenuInflater().inflate(R.menu.menu_main, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_painters:
                                Intent i = new Intent(getActivity(), PainterListActivity.class);
                                startActivity(i);
                        }
                        return true;
                    }
                });
                menu.show();
            }
        };

        onClickAdd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewJobActivity.class);
                startActivity(i);
            }
        };
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
