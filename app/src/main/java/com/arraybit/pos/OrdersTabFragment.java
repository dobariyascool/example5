package com.arraybit.pos;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.OrdersAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.OrderMaster;
import com.arraybit.parser.OrderJOSNParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


@SuppressWarnings("unchecked")
public class OrdersTabFragment extends Fragment implements SearchView.OnQueryTextListener{

    public final static String ITEMS_COUNT_KEY = "OrdersTabFragment$ItemsCount";
    public TextView txtMsg;
    RecyclerView rvOrder;
    ArrayList<OrderMaster> alOrderMaster;
    int counterMasterId;
    String orderStatus, linktoTableMasterIds;
    GridLayoutManager gridLayoutManager;
    SharePreferenceManage objSharePreferenceManage;
    OrdersAdapter ordersAdapter;
    int id;
    DisplayMetrics displayMetrics;
    String orderTypeMasterId;

    public OrdersTabFragment() {
        // Required empty public constructor
    }

    public static OrdersTabFragment createInstance(String orderStatus, String linktoTableMasterIds) {
        OrdersTabFragment ordersTabFragment = new OrdersTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ITEMS_COUNT_KEY, orderStatus);
        bundle.putString("TableMasterIds", linktoTableMasterIds);
        ordersTabFragment.setArguments(bundle);
        return ordersTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders_tab, container, false);

        rvOrder = (RecyclerView) view.findViewById(R.id.rvOrder);
        rvOrder.setVisibility(View.GONE);

        setHasOptionsMenu(true);

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);

        displayMetrics = getActivity().getResources().getDisplayMetrics();

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        //get counterMasterId
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
            counterMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
        }
        //end

        //first tab load
        Bundle bundle = getArguments();
        orderStatus = bundle.getString(ITEMS_COUNT_KEY);
        linktoTableMasterIds = bundle.getString("TableMasterIds");

        if (orderStatus.equals(Globals.OrderStatus.All.toString())) {
            LoadOrderData();
        }
        //end

        return view;
    }

    public void OrderDataFilter(String orderStatus, String orderTypeMasterId) {
        this.orderTypeMasterId = orderTypeMasterId;
        this.orderStatus = orderStatus;
        alOrderMaster = new ArrayList<>();

        if (Service.CheckNet(getActivity())) {
            new OrderMasterLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(rvOrder, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        mSearchView.setMaxWidth(displayMetrics.widthPixels);
        mSearchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        if (alOrderMaster.size() != 0 && alOrderMaster != null) {
                            ordersAdapter.SetSearchFilter(alOrderMaster);
                            Globals.HideKeyBoard(getActivity(), MenuItemCompat.getActionView(searchItem));
                        }
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });

    }

    public void LoadOrderData() {
        alOrderMaster = new ArrayList<>();
        if (Service.CheckNet(getActivity())) {
            new OrderMasterLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(rvOrder, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (alOrderMaster.size() != 0 && alOrderMaster != null) {
            final ArrayList<OrderMaster> filteredList = Filter(alOrderMaster, newText);
            ordersAdapter.SetSearchFilter(filteredList);
        }
        return false;
    }

    //region Private Methods
    private ArrayList<OrderMaster> Filter(ArrayList<OrderMaster> lstOrderMaster, String filterName) {
        filterName = filterName.toLowerCase();
        final ArrayList<OrderMaster> filteredList = new ArrayList<>();
        for (OrderMaster objOrderMaster : lstOrderMaster) {
            if (objOrderMaster.getOrderNumber().length() >= filterName.length()) {
                final String strItem = objOrderMaster.getOrderNumber().substring(0, filterName.length());
                if (strItem.contains(filterName)) {
                    filteredList.add(objOrderMaster);
                }
            }
        }
        return filteredList;
    }

    private void SetupRecyclerView(RecyclerView rvOrder) {

        ordersAdapter = new OrdersAdapter(getActivity(), alOrderMaster);
        rvOrder.setAdapter(ordersAdapter);
        rvOrder.setLayoutManager(gridLayoutManager);
        rvOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Globals.HideKeyBoard(getActivity(), recyclerView);
            }
        });
    }
    //endregion

    //region LoadingTask
    class OrderMasterLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            return objOrderJOSNParser.SelectAllOrderMaster(counterMasterId, Globals.OrderStatus.valueOf(orderStatus).getValue(), linktoTableMasterIds, orderTypeMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            progressDialog.dismiss();
            ArrayList<OrderMaster> lstOrderMaster = (ArrayList<OrderMaster>) result;
            if (lstOrderMaster == null) {
                Globals.SetError(txtMsg, rvOrder, getActivity().getResources().getString(R.string.MsgSelectFail), true);
            } else if (lstOrderMaster.size() == 0) {
                Globals.SetError(txtMsg, rvOrder, getActivity().getResources().getString(R.string.MsgNoRecord), true);
            } else {
                Globals.SetError(txtMsg, rvOrder, null, false);
                alOrderMaster = lstOrderMaster;
                SetupRecyclerView(rvOrder);
            }
        }
    }
    //endregion
}
