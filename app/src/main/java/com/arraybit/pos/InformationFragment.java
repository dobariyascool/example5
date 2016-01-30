package com.arraybit.pos;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;

import com.arraybit.adapter.WorkingHoursAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessHoursTran;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.parser.BusinessHoursJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
@SuppressWarnings("unchecked")
public class InformationFragment extends Fragment {

    static ArrayList<BusinessHoursTran> lstBusinessHoursTran;
    RecyclerView rvWorkingHours;
    TextView txtAddress, txtPhone, txtEmail, txtWebSite;
    LinearLayoutManager linearLayoutManager;
    WorkingHoursAdapter adapter;
    BusinessMaster objBusinessMaster;
    LinearLayout phoneLayout, emailLayout, siteLayout;

    public InformationFragment(BusinessMaster objBusinessMaster) {
        this.objBusinessMaster = objBusinessMaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        txtPhone = (TextView) view.findViewById(R.id.txtPhone);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtWebSite = (TextView) view.findViewById(R.id.txtWebSite);

        phoneLayout = (LinearLayout) view.findViewById(R.id.phoneLayout);
        emailLayout = (LinearLayout) view.findViewById(R.id.emailLayout);
        siteLayout = (LinearLayout) view.findViewById(R.id.siteLayout);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        rvWorkingHours = (RecyclerView) view.findViewById(R.id.rvWorkingHours);
        rvWorkingHours.setNestedScrollingEnabled(false);
        rvWorkingHours.setVisibility(View.GONE);

        if (lstBusinessHoursTran == null) {
            SetContactDetails();
            if (Service.CheckNet(getActivity())) {
                new WorkingHoursLoadingTask().execute();
            } else {
                Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }

        } else {
            SetContactDetails();
            SetWorkingHoursRecyclerView(lstBusinessHoursTran);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    //region Private Methods
    private void SetWorkingHoursRecyclerView(ArrayList<BusinessHoursTran> lstBusinessHoursTran) {
        rvWorkingHours.setVisibility(View.VISIBLE);
        adapter = new WorkingHoursAdapter(getActivity(), lstBusinessHoursTran);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        rvWorkingHours.setItemAnimator(animator);
        rvWorkingHours.setAdapter(adapter);
        rvWorkingHours.setLayoutManager(linearLayoutManager);
    }

    private void SetContactDetails() {
        if (objBusinessMaster.getAddress() == null) {
            txtAddress.setVisibility(View.GONE);
        } else {
            txtAddress.setVisibility(View.VISIBLE);
            txtAddress.setText(objBusinessMaster.getAddress());
        }
        if (objBusinessMaster.getPhone1() == null) {
            phoneLayout.setVisibility(View.GONE);
        } else {
            phoneLayout.setVisibility(View.VISIBLE);
            txtPhone.setText(objBusinessMaster.getPhone1());
        }
        if (objBusinessMaster.getWebsite() == null) {
            siteLayout.setVisibility(View.GONE);
        } else {
            siteLayout.setVisibility(View.VISIBLE);
            txtWebSite.setText(objBusinessMaster.getWebsite());
        }
        if (objBusinessMaster.getEmail() == null) {
            emailLayout.setVisibility(View.GONE);
        } else {
            emailLayout.setVisibility(View.VISIBLE);
            txtEmail.setText(objBusinessMaster.getEmail());
        }
    }
    //endregion

    //region LoadingTask
    class WorkingHoursLoadingTask extends AsyncTask {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            BusinessHoursJSONParser objBusinessHoursJSONParser = new BusinessHoursJSONParser();
            return objBusinessHoursJSONParser.SelectAllBusinessHoursTranById(1);

        }

        @Override
        protected void onPostExecute(Object result) {

            progressDialog.dismiss();
            lstBusinessHoursTran = (ArrayList<BusinessHoursTran>) result;
            if (lstBusinessHoursTran == null) {
                rvWorkingHours.setVisibility(View.GONE);
            } else if (lstBusinessHoursTran.size() == 0) {
                rvWorkingHours.setVisibility(View.GONE);
            } else {
                SetWorkingHoursRecyclerView(lstBusinessHoursTran);
            }
        }
    }
    //endregion
}
