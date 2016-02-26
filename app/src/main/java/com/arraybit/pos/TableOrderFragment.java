package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.TableOrderAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TaxMaster;
import com.arraybit.parser.OrderJOSNParser;
import com.arraybit.parser.TaxJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

@SuppressLint("ValidFragment")
@SuppressWarnings({"unchecked", "ConstantConditions"})
public class TableOrderFragment extends Fragment {


    RecyclerView rvTableOrder;
    LinearLayout tableOrderFragment;
    TextView txtMsg;
    ArrayList<OrderMaster> alOrderMaster;
    int counterMasterId;
    ArrayList<TaxMaster> alTaxMaster;
    TableOrderAdapter orderDetailAdapter;

    public TableOrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_order, container, false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_table_order));
        }
        //end

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        Globals.TextViewFontTypeFace(txtMsg,getActivity());

        rvTableOrder = (RecyclerView) view.findViewById(R.id.rvTableOrder);
        rvTableOrder.setVisibility(View.GONE);

        tableOrderFragment = (LinearLayout) view.findViewById(R.id.tableOrderFragment);
        Globals.SetScaleImageBackground(getActivity(), tableOrderFragment, null, null);

        setHasOptionsMenu(true);

        if (Service.CheckNet(getActivity())) {
            new OrdersLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }


        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(), tableOrderFragment, null, null);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(false);
    }

    //region LoadingTask
    class OrdersLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
                counterMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
            }

        }

        @Override
        protected Object doInBackground(Object[] params) {
            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            return objOrderJOSNParser.SelectAllOrderMasterByFromDate(counterMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            alOrderMaster = (ArrayList<OrderMaster>) result;

            if (alOrderMaster == null) {
                Globals.SetError(txtMsg, rvTableOrder, getActivity().getResources().getString(R.string.MsgSelectFail), true);
            } else if (alOrderMaster.size() == 0) {
                Globals.SetError(txtMsg, rvTableOrder, getActivity().getResources().getString(R.string.MsgNoRecord), true);
            } else {
                Globals.SetError(txtMsg, rvTableOrder, null, false);

                if (Service.CheckNet(getActivity())) {
                    new TaxLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(tableOrderFragment, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        }
    }

    class TaxLoadingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TaxJSONParser objTaxJSONParser = new TaxJSONParser();
            return objTaxJSONParser.SelectAllTaxMaster(Globals.businessMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            alTaxMaster = (ArrayList<TaxMaster>) result;
            if (alTaxMaster != null && alTaxMaster.size() != 0) {
                orderDetailAdapter = new TableOrderAdapter(getActivity(), alOrderMaster, alTaxMaster, getActivity().getSupportFragmentManager());
            } else {
                orderDetailAdapter = new TableOrderAdapter(getActivity(), alOrderMaster, null, getActivity().getSupportFragmentManager());
            }
            rvTableOrder.setVisibility(View.VISIBLE);
            ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(orderDetailAdapter);
            rvTableOrder.setAdapter(animationAdapter);
            rvTableOrder.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
    }
    //endregion
}
