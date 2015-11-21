package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.CounterAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CounterMaster;
import com.arraybit.parser.CounterJSONParser;

import java.util.ArrayList;

@SuppressWarnings({"ConstantConditions", "unchecked"})
@SuppressLint("ValidFragment")
public class CounterFragment extends Fragment {

    CounterAdapter adapter;
    RecyclerView rvCounter;
    ArrayList<CounterMaster> alCounterMaster;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    SharePreferenceManage objSharePreferenceManage;
    CounterJSONParser objCounterJSONParser;

    public CounterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_counter, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_counter));

        LinearLayout counterLayout=(LinearLayout)view.findViewById(R.id.counterLayout);
        Globals.SetScaleImageBackground(getActivity(),counterLayout, null);

        rvCounter = (RecyclerView) view.findViewById(R.id.rvCounter);
        rvCounter.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        gridLayoutManager = new GridLayoutManager(getActivity(),2);

        new CounterLoadingTask().execute();
        return view;
    }

    public class CounterLoadingTask extends AsyncTask {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            objCounterJSONParser = new CounterJSONParser();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            alCounterMaster = objCounterJSONParser.SelectAllCounterMaster(Globals.BusinessMasterId);
            return alCounterMaster;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if(alCounterMaster != null && alCounterMaster.size()!=0) {

                rvCounter.setVisibility(View.VISIBLE);
                adapter = new CounterAdapter(getActivity(), alCounterMaster);
                rvCounter.setAdapter(adapter);
                rvCounter.setLayoutManager(linearLayoutManager);
                //rvCounter.setLayoutManager(gridLayoutManager);
            }
        }
    }
}
