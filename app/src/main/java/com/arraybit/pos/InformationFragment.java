package com.arraybit.pos;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arraybit.adapter.WorkingHoursAdapter;
import com.arraybit.modal.BusinessHoursTran;
import com.arraybit.parser.BusinessHoursJSONParser;

import java.util.ArrayList;


public class InformationFragment extends Fragment {

    static ArrayList<BusinessHoursTran> lstBusinessHoursTran;
    RecyclerView rvWorkingHours;
    TextView txtDescription;
    LinearLayoutManager linearLayoutManager;
    String description;
    int BusinessMasterId;
    WorkingHoursAdapter adapter;

    public InformationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_information, container, false);

        txtDescription = (TextView) rootView.findViewById(R.id.txtDescription);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        rvWorkingHours = (RecyclerView) rootView.findViewById(R.id.rvWorkingHours);
        rvWorkingHours.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (description == null) {
            txtDescription.setText(description);
        }
        if (lstBusinessHoursTran == null) {
            new WorkingHoursLoadingTask().execute();
        } else {
            SetWorkingHoursRecyclerView(lstBusinessHoursTran);
        }
    }

    public void SetWorkingHoursRecyclerView(ArrayList<BusinessHoursTran> lstBusinessHoursTran) {
        rvWorkingHours.setVisibility(View.VISIBLE);
        adapter = new WorkingHoursAdapter(getActivity(), lstBusinessHoursTran);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        rvWorkingHours.setItemAnimator(animator);
        rvWorkingHours.setAdapter(adapter);
        rvWorkingHours.setLayoutManager(linearLayoutManager);

    }

    public class WorkingHoursLoadingTask extends AsyncTask {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            SignInActivity.ServerName = "10.0.3.2:6122";
        }

        @Override
        protected Object doInBackground(Object[] params) {
            BusinessHoursJSONParser objBusinessHoursJSONParser = new BusinessHoursJSONParser();
            return objBusinessHoursJSONParser.SelectAllBusinessHoursTranById(1);

        }

        @Override
        protected void onPostExecute(Object o) {

            progressDialog.dismiss();
            lstBusinessHoursTran = (ArrayList<BusinessHoursTran>) o;
            if (lstBusinessHoursTran == null) {
                rvWorkingHours.setVisibility(View.GONE);
                return;
            } else if (lstBusinessHoursTran.size() == 0) {
                rvWorkingHours.setVisibility(View.GONE);
                return;
            } else {
                SetWorkingHoursRecyclerView(lstBusinessHoursTran);
            }
        }
    }
}
