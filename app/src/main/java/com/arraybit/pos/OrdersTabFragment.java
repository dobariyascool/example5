package com.arraybit.pos;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.OrdersAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.OrderItemTran;
import com.arraybit.modal.OrderMaster;
import com.arraybit.parser.OrderItemJSONParser;
import com.arraybit.parser.OrderJOSNParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


@SuppressWarnings("unchecked")
public class OrdersTabFragment extends Fragment implements SearchView.OnQueryTextListener, OrdersAdapter.LayoutClickListener, OrderStatusDialogFragment.UpdateStatusListener {

    public final static String ITEMS_COUNT_KEY = "OrdersTabFragment$ItemsCount";
    public TextView txtMsg;
    RecyclerView rvOrder;
    ArrayList<OrderMaster> alOrderMaster;
    int counterMasterId;
    String orderStatus, linktoTableMasterIds;
    GridLayoutManager gridLayoutManager;
    SharePreferenceManage objSharePreferenceManage;
    OrdersAdapter ordersAdapter;
    int id, itemPosition, orderPosition;
    DisplayMetrics displayMetrics;
    String orderTypeMasterId;
    StringBuilder sbOrderMasterIds;
    ArrayList<OrderItemTran> alOrderItemTran;
    LinearLayout errorLayout;


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

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);

        rvOrder = (RecyclerView) view.findViewById(R.id.rvOrder);
        rvOrder.setVisibility(View.GONE);

        setHasOptionsMenu(true);

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

    public void OrderDataFilter(String orderTypeMasterId) {
        ArrayList<OrderMaster> alOrderMasterFilter = new ArrayList<>();
        if (orderTypeMasterId != null) {
            for (int i = 0; i < alOrderMaster.size(); i++) {
                if (alOrderMaster.get(i).getlinktoOrderTypeMasterId() == Short.valueOf(orderTypeMasterId)) {
                    alOrderMasterFilter.add(alOrderMaster.get(i));
                }
            }
            if (alOrderMasterFilter.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvOrder);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvOrder);
                SetupRecyclerView(rvOrder, alOrderMasterFilter, alOrderItemTran);
            }
        } else {
            if (alOrderMaster.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvOrder);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvOrder);
                SetupRecyclerView(rvOrder, alOrderMaster, alOrderItemTran);
            }
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
            Globals.SetErrorLayout(errorLayout,true,getResources().getString(R.string.MsgCheckConnection),rvOrder);
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

    @Override
    public void ChangeOrderItemStatusClick(OrderItemTran objOrderItemTran, OrderMaster objOrderMaster, int itemPosition, int orderPosition, boolean isOrder) {
        this.itemPosition = itemPosition;
        this.orderPosition = orderPosition;
        OrderStatusDialogFragment orderStatusDialogFragment = new OrderStatusDialogFragment(objOrderItemTran, objOrderMaster, isOrder);
        orderStatusDialogFragment.setTargetFragment(this, 0);
        orderStatusDialogFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void UpdateStatus(boolean flag, boolean isOrder, OrderItemTran objOrderItemTran, short linktoOrderStatusMasterId, boolean isTotalUpdate) {
        if (isOrder) {
            if (flag) {
                if (orderStatus.equals(Globals.OrderStatus.All.toString())) {
                    if (linktoOrderStatusMasterId != 0) {
                        ordersAdapter.UpdateOrder(orderPosition, linktoOrderStatusMasterId);
                    }
                } else {
                    ordersAdapter.RemoveOrder(orderPosition);
                }
            }

        } else {
            if (flag) {
                ordersAdapter.UpdateOrderItemTran(itemPosition, orderPosition, objOrderItemTran, isTotalUpdate);
            }
        }
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

    private void SetupRecyclerView(RecyclerView rvOrder, ArrayList<OrderMaster> alOrderMaster, ArrayList<OrderItemTran> alOrderItemTran) {

        ordersAdapter = new OrdersAdapter(getActivity(), alOrderMaster, alOrderItemTran, getActivity().getSupportFragmentManager(), this, false);
        rvOrder.setAdapter(ordersAdapter);
        rvOrder.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Globals.HideKeyBoard(getActivity(), recyclerView);
                if (!ordersAdapter.isItemAnimate) {
                    ordersAdapter.isItemAnimate = true;
                    ordersAdapter.isViewFilter = true;
                }
            }
        });
    }
    //endregion

    //region LoadingTask
    class OrderMasterLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            return objOrderJOSNParser.SelectAllOrderMaster(counterMasterId, Globals.OrderStatus.valueOf(orderStatus).getValue(), linktoTableMasterIds, orderTypeMasterId,Globals.businessMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            progressDialog.dismiss();
            ArrayList<OrderMaster> lstOrderMaster = (ArrayList<OrderMaster>) result;
            if (lstOrderMaster == null) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgSelectFail), rvOrder);
            } else if (lstOrderMaster.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvOrder);
            } else {
                alOrderMaster = lstOrderMaster;
                sbOrderMasterIds = new StringBuilder();
                for (int i = 0; i < alOrderMaster.size(); i++) {
                    sbOrderMasterIds.append(alOrderMaster.get(i).getOrderMasterId()).append(",");
                }
                if (!sbOrderMasterIds.toString().equals("")) {
                    new OrderItemTranLoadingTask().execute();
                }
            }
        }
    }

    class OrderItemTranLoadingTask extends AsyncTask {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OrderItemJSONParser objOrderItemJSONParser = new OrderItemJSONParser();
            return objOrderItemJSONParser.SelectAllOrderItemTran(sbOrderMasterIds.toString());
        }

        @Override
        protected void onPostExecute(Object result) {
            alOrderItemTran = (ArrayList<OrderItemTran>) result;
            if (alOrderItemTran != null && alOrderItemTran.size() != 0) {
                Globals.SetErrorLayout(errorLayout, false, null, rvOrder);
                SetupRecyclerView(rvOrder, alOrderMaster, alOrderItemTran);
            }
        }
    }
    //endregion
}
