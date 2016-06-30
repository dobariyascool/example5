package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
@SuppressWarnings({"unchecked", "ConstantConditions"})
public class TableOrderFragment extends Fragment implements View.OnClickListener{


    RecyclerView rvTableOrder;
    CoordinatorLayout tableOrderFragment;
    LinearLayout errorLayout;
    TextView txtMsg;
    ArrayList<OrderMaster> alOrderMaster,alFilterOrderMaster;
    int counterMasterId;
    ArrayList<TaxMaster> alTaxMaster;
    TableOrderAdapter orderDetailAdapter;
    FloatingActionMenu famRoot;

    public TableOrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_order, container, false);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_table_order));
        }
        //end

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);

        rvTableOrder = (RecyclerView) view.findViewById(R.id.rvTableOrder);
        rvTableOrder.setVisibility(View.GONE);

        tableOrderFragment = (CoordinatorLayout) view.findViewById(R.id.tableOrderFragment);
        Globals.SetScaleImageBackground(getActivity(), tableOrderFragment);

        famRoot = (FloatingActionMenu) view.findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);

        FloatingActionButton fabDineIn = (FloatingActionButton) view.findViewById(R.id.fabDineIn);
        FloatingActionButton fabTakeAway = (FloatingActionButton) view.findViewById(R.id.fabTakeAway);
        FloatingActionButton fabAll = (FloatingActionButton) view.findViewById(R.id.fabAll);

        setHasOptionsMenu(true);

        fabDineIn.setOnClickListener(this);
        fabTakeAway.setOnClickListener(this);
        fabAll.setOnClickListener(this);

        if (Service.CheckNet(getActivity())) {
            new OrdersLoadingTask().execute();
        } else {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), rvTableOrder,R.drawable.wifi_drawable);
        }


        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() == null) {
                Globals.isWishListShow = 0;
                Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fabDineIn){
            famRoot.close(true);
            OrderMasterFilter(Globals.OrderType.DineIn.getValue());
        }else if(v.getId()==R.id.fabTakeAway){
            famRoot.close(true);
            OrderMasterFilter(Globals.OrderType.TakeAway.getValue());
        }else if(v.getId()==R.id.fabAll){
            famRoot.close(true);
            OrderMasterFilter(0);
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.notification_layout).setVisible(false);
    }

    private void OrderMasterFilter(int linktoOrderTypeMasterId){
        alFilterOrderMaster=new ArrayList<>();
        if(linktoOrderTypeMasterId==0) {
            alFilterOrderMaster.addAll(alOrderMaster);
        }else{
            for (OrderMaster objOrderMaster : alOrderMaster) {
                if (objOrderMaster.getlinktoOrderTypeMasterId() == linktoOrderTypeMasterId) {
                    alFilterOrderMaster.add(objOrderMaster);
                }
            }
        }
        if(alFilterOrderMaster.size()==0){
            Globals.SetErrorLayout(errorLayout, true, String.format(getActivity().getResources().getString(R.string.MsgNoRecordFound),getActivity().getResources().getString(R.string.MsgBillGenerate)), rvTableOrder,0);
        }else {
            Globals.SetErrorLayout(errorLayout, false, null, rvTableOrder,0);
            if (alTaxMaster != null && alTaxMaster.size() != 0) {
                orderDetailAdapter = new TableOrderAdapter(getActivity(), alFilterOrderMaster, alTaxMaster, getActivity().getSupportFragmentManager(), false);
            } else {
                orderDetailAdapter = new TableOrderAdapter(getActivity(), alFilterOrderMaster, null, getActivity().getSupportFragmentManager(), false);
            }

            rvTableOrder.setAdapter(orderDetailAdapter);
            rvTableOrder.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            rvTableOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!orderDetailAdapter.isItemAnimate) {
                        orderDetailAdapter.isItemAnimate = true;
                    }
                }
            });
        }
    }

    //region LoadingTask
    class OrdersLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
                counterMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
            }

        }

        @Override
        protected Object doInBackground(Object[] params) {
            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            return objOrderJOSNParser.SelectAllOrderMasterByFromDate(counterMasterId, Globals.businessMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            alOrderMaster = (ArrayList<OrderMaster>) result;

            if (alOrderMaster == null) {
                famRoot.setVisibility(View.GONE);
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgSelectFail), rvTableOrder,0);
            } else if (alOrderMaster.size() == 0) {
                famRoot.setVisibility(View.GONE);
                Globals.SetErrorLayout(errorLayout, true, String.format(getActivity().getResources().getString(R.string.MsgNoRecordFound),getActivity().getResources().getString(R.string.MsgBillGenerate)), rvTableOrder,0);
            } else {
                if (Service.CheckNet(getActivity())) {
                    new TaxLoadingTask().execute();
                } else {
                    Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), rvTableOrder,0);
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

            famRoot.setVisibility(View.VISIBLE);
            alTaxMaster = (ArrayList<TaxMaster>) result;
            if (alTaxMaster != null && alTaxMaster.size() != 0) {
                orderDetailAdapter = new TableOrderAdapter(getActivity(), alOrderMaster, alTaxMaster, getActivity().getSupportFragmentManager(), false);
            } else {
                orderDetailAdapter = new TableOrderAdapter(getActivity(), alOrderMaster, null, getActivity().getSupportFragmentManager(), false);
            }
            Globals.SetErrorLayout(errorLayout, false, null, rvTableOrder,0);
            rvTableOrder.setAdapter(orderDetailAdapter);
            rvTableOrder.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            rvTableOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!orderDetailAdapter.isItemAnimate) {
                        orderDetailAdapter.isItemAnimate = true;
                    }
                }
            });
        }
    }
    //endregion
}
