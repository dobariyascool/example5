package com.arraybit.pos;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.BusinessInfoAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessInfoQuestionMaster;
import com.arraybit.parser.BusinessInfoQuestionJSONParser;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class BusinessInformationFragment extends Fragment {

    RecyclerView rvBusinessInfo;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog = new ProgressDialog();
    BusinessInfoAdapter adapter;

    public BusinessInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_information, container, false);
        rvBusinessInfo = (RecyclerView) view.findViewById(R.id.rvBusinessInfo);
        rvBusinessInfo.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        if (Service.CheckNet(getActivity())) {
            new TableMasterLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        rvBusinessInfo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return view;
    }

    //region Loading Task
    class TableMasterLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            BusinessInfoQuestionJSONParser objBusinessInfoQuestionJSONParser = new BusinessInfoQuestionJSONParser();
            return objBusinessInfoQuestionJSONParser.SelectAllBusinessInfoQuestionMaster(Globals.businessTypeMasterId, Globals.businessMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            ArrayList<BusinessInfoQuestionMaster> lstBusinessInfoQuestionMaster = (ArrayList<BusinessInfoQuestionMaster>) result;
            if (lstBusinessInfoQuestionMaster != null && lstBusinessInfoQuestionMaster.size() != 0) {
                adapter = new BusinessInfoAdapter(getActivity(), lstBusinessInfoQuestionMaster);
                rvBusinessInfo.setVisibility(View.VISIBLE);
                rvBusinessInfo.setAdapter(adapter);
                rvBusinessInfo.setLayoutManager(linearLayoutManager);
            }
        }
    }
    //endregion
}
