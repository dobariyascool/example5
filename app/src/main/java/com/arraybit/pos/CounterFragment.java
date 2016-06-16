package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.CounterAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
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
    LinearLayout counterLayout, errorLayout;
    short userMasterId, userType;
    boolean isBack;

    public CounterFragment(short userType) {
        this.userType = userType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_counter, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            isBack = bundle.getBoolean("isBack", false);
        }

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            if (isBack) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_counter));

        counterLayout = (LinearLayout) view.findViewById(R.id.counterLayout);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        Globals.SetScaleImageBackground(getActivity(), counterLayout, null, null);

        rvCounter = (RecyclerView) view.findViewById(R.id.rvCounter);
        rvCounter.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        setHasOptionsMenu(true);

        if (Service.CheckNet(getActivity())) {
            new CounterLoadingTask().execute();
        } else {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), rvCounter,R.drawable.wifi_drawable);
        }

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            menu.findItem(R.id.mWaiting).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        } else if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            menu.findItem(R.id.logout).setVisible(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }


    //region LoadingTask
    public class CounterLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

            objSharePreferenceManage = new SharePreferenceManage();
            if (userType != 0) {
                if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()) != null) {
                    userMasterId = Short.parseShort(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()));
                }
            }
            objCounterJSONParser = new CounterJSONParser();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            alCounterMaster = objCounterJSONParser.SelectAllCounterMaster(Globals.businessMasterId, userMasterId);
            return alCounterMaster;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (alCounterMaster == null) {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvCounter,0);
            } else if (alCounterMaster.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvCounter,0);
            } else {
                Globals.SetErrorLayout(errorLayout, false,null, rvCounter,0);
                adapter = new CounterAdapter(getActivity(), alCounterMaster, userType);
                rvCounter.setAdapter(adapter);
                rvCounter.setLayoutManager(linearLayoutManager);
            }
        }
    }
    //endregion
}
