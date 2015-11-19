package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.CounterAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CounterMaster;
import com.arraybit.parser.CounterJSONParser;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class CounterFragment extends Fragment {

    CounterAdapter adapter;
    RecyclerView rvCounter;
    ArrayList<CounterMaster> alCounterMaster;
    LinearLayoutManager linearLayoutManager;
    SharePreferenceManage objSharePreferenceManage;


    CounterMaster objCounterMaster;
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

        rvCounter = (RecyclerView) view.findViewById(R.id.rvCounter);
        linearLayoutManager = new LinearLayoutManager(getActivity());

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
            if (alCounterMaster.size() == 1) {
                objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("CounterPreference", "CounterMasterId", String.valueOf(objCounterMaster.getCounterMasterId()), getActivity());

                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                getActivity().startActivity(intent);
            } else {
                adapter = new CounterAdapter(getActivity(), alCounterMaster);
                rvCounter.setAdapter(adapter);
                rvCounter.setLayoutManager(linearLayoutManager);

            }
        }
    }
}
