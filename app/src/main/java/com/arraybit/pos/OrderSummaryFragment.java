package com.arraybit.pos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.OrderSummaryAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TableMaster;
import com.arraybit.parser.ItemJSONParser;
import com.arraybit.parser.OrderJOSNParser;
import com.rey.material.widget.Button;

import java.util.ArrayList;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class OrderSummaryFragment extends Fragment implements View.OnClickListener {


    RecyclerView rvOrderItemSummery;
    LinearLayout orderSummeryLayout;
    OrderMaster objOrderMaster;
    Button btnAddMore, btnCheckOut;
    short counterMasterId;
    TableMaster objTableMaster;
    SharePreferenceManage objSharePreferenceManage;
    ArrayList<OrderMaster> lstOrderMaster;

    public OrderSummaryFragment(OrderMaster objOrderMaster) {
        this.objOrderMaster = objOrderMaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_summary, container, false);

        setHasOptionsMenu(true);

        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
            objTableMaster = MenuActivity.objTableMaster;
        } else if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            Bundle bundle = getArguments();
            if (bundle != null && bundle.getParcelable("TableMaster") != null) {
                objTableMaster = bundle.getParcelable("TableMaster");
            }
        }

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            if (objTableMaster != null && objTableMaster.getTableName() != null) {
                app_bar.setTitle(objTableMaster.getTableName() + "  Summary");
            } else {
                app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_order_detail));
            }
        }
        //end

        orderSummeryLayout = (LinearLayout) view.findViewById(R.id.orderSummeryLayout);
        Globals.SetScaleImageBackground(getActivity(), orderSummeryLayout, null, null);

        rvOrderItemSummery = (RecyclerView) view.findViewById(R.id.rvOrderItemSummery);
        rvOrderItemSummery.setVisibility(View.GONE);

        btnAddMore = (Button) view.findViewById(R.id.btnAddMore);
        btnCheckOut = (Button) view.findViewById(R.id.btnCheckOut);

        SetVisibility();

        //Globals.alOrderMasterId.add(objOrderMaster.getOrderMasterId());

        btnAddMore.setOnClickListener(this);
        btnCheckOut.setOnClickListener(this);

        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
            counterMasterId = Short.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
        }

        //new OrderSummeryLoadingTask().execute();

        new AllOrdersLoadingTask().execute();

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            menu.findItem(R.id.home).setVisible(true);
            menu.findItem(R.id.action_search).setVisible(false);
        } else if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
            menu.findItem(R.id.home).setVisible(true);
            menu.findItem(R.id.action_search).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.home) {
            Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddMore) {
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("TableMaster", objTableMaster);
                startActivity(intent);
            } else {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_order_summary))) {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        } else if (v.getId() == R.id.btnCheckOut) {
            if (MenuActivity.parentActivity) {
                Globals.counter = 0;
                Globals.alOrderItemTran.clear();
                Globals.alOrderItemSummery = new ArrayList<>();
                Globals.alOrderMasterId = new ArrayList<>();
                Globals.InitializeFragment(new ThankYouFragment(), getActivity().getSupportFragmentManager());
            } else {
                Globals.counter = 0;
                Globals.alOrderItemTran.clear();
                Globals.selectTableMasterId = 0;
                Globals.alOrderItemSummery = new ArrayList<>();
                Globals.alOrderMasterId = new ArrayList<>();
                Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }

    private void SetVisibility() {
        if (lstOrderMaster == null || lstOrderMaster.size() == 0) {
            rvOrderItemSummery.setVisibility(View.GONE);
            btnAddMore.setVisibility(View.GONE);
            btnCheckOut.setVisibility(View.GONE);
        } else {
            rvOrderItemSummery.setVisibility(View.VISIBLE);
            btnAddMore.setVisibility(View.VISIBLE);
            btnCheckOut.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(), orderSummeryLayout, null, null);
    }

    public class AllOrdersLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            return objOrderJOSNParser.SelectAllOrderMaster(counterMasterId, 0, String.valueOf(objTableMaster.getTableMasterId()), null);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            lstOrderMaster = (ArrayList<OrderMaster>) result;

            if (lstOrderMaster != null) {

                new OrderSummeryLoadingTask().execute();
                //Globals.alOrderItemSummery.addAll(lstOrderItemTran);
                //OrderSummaryAdapter orderSummeryAdapter = new OrderSummaryAdapter(getActivity(), Globals.alOrderItemSummery,lstOrderMaster);
                //SetVisibility();
                //rvOrderItemSummery.setAdapter(orderSummeryAdapter);
                //rvOrderItemSummery.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }
    }

    public class OrderSummeryLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            ItemJSONParser objItemJSONParser = new ItemJSONParser();
            return objItemJSONParser.SelectAllOrderItemByTableMasterIds(String.valueOf(objTableMaster.getTableMasterId()));
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            ArrayList<ItemMaster> lstOrderItemTran = (ArrayList<ItemMaster>) result;

            if (lstOrderItemTran != null) {

                //Globals.alOrderItemSummery.addAll(lstOrderItemTran);
                OrderSummaryAdapter orderSummeryAdapter = new OrderSummaryAdapter(getActivity(), lstOrderItemTran, lstOrderMaster);
                SetVisibility();
                rvOrderItemSummery.setAdapter(orderSummeryAdapter);
                rvOrderItemSummery.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }
    }


//    public class OrderSummeryLoadingTask extends AsyncTask {
//
//        ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
//            progressDialog.setIndeterminate(false);
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected Object doInBackground(Object[] params) {
//            OrderItemJSONParser objOrderItemJSONParser = new OrderItemJSONParser();
//            return objOrderItemJSONParser.SelectAllOrderItemTran(objOrderMaster.getOrderMasterId());
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
//
//            progressDialog.dismiss();
//
//            ArrayList<OrderItemTran> lstOrderItemTran = (ArrayList<OrderItemTran>) result;
//
//            if (lstOrderItemTran != null) {
//
//                Globals.alOrderItemSummery.addAll(lstOrderItemTran);
//                OrderSummaryAdapter orderSummeryAdapter = new OrderSummaryAdapter(getActivity(), Globals.alOrderItemSummery, Globals.alOrderMasterId);
//                SetVisibility();
//                rvOrderItemSummery.setAdapter(orderSummeryAdapter);
//                rvOrderItemSummery.setLayoutManager(new LinearLayoutManager(getActivity()));
//            }
//        }
//    }
}
