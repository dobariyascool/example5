package com.arraybit.pos;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.OrdersAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.OrderMaster;
import com.arraybit.parser.OrderJOSNParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


@SuppressWarnings("unchecked")
public class OrdersTabFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "OrdersTabFragment$ItemsCount";
    RecyclerView rvOrder;
    TextView txtMsg;
    ArrayList<OrderMaster> alOrderMaster;
    int currentPage = 1, counterMasterId;
    String orderStatus;
    GridLayoutManager gridLayoutManager;
    SharePreferenceManage objSharePreferenceManage;
    OrdersAdapter ordersAdapter;
    int id;

    public OrdersTabFragment() {
        // Required empty public constructor
    }

    public static OrdersTabFragment createInstance(String orderStatus) {
        OrdersTabFragment ordersTabFragment = new OrdersTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ITEMS_COUNT_KEY, orderStatus);
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

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);

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

        if (orderStatus.equals(Globals.OrderStatus.Cooking.toString())) {
            LoadOrderData();
        }
        //end

        return view;
    }

    private void SetupRecyclerView(RecyclerView rvOrder) {

        ordersAdapter = new OrdersAdapter(getActivity(),alOrderMaster,getActivity().getSupportFragmentManager());
        rvOrder.setAdapter(ordersAdapter);
        rvOrder.setLayoutManager(gridLayoutManager);
    }

    public void LoadOrderData() {

        alOrderMaster = new ArrayList<>();

        new OrderMasterLoadingTask().execute();

        if (rvOrder != null) {
            rvOrder.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {

                    if (current_page > currentPage) {
                        currentPage = current_page;
                        if (Service.CheckNet(getActivity())) {
                            new OrderMasterLoadingTask().execute();
                        }
                    }
                }
            });
        }
    }

    //region LoadingTask
    @SuppressWarnings("ResourceType")
    class OrderMasterLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (currentPage > 2 && alOrderMaster.size() != 0) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            if (currentPage > 1) {
                if (gridLayoutManager.canScrollVertically() && alOrderMaster.size() == 0) {
                    currentPage = 1;
                }
            }
            return objOrderJOSNParser.SelectAllOrderMaster(currentPage, counterMasterId, Globals.OrderStatus.valueOf(orderStatus).getValue());
        }

        @Override
        protected void onPostExecute(Object result) {
            if (currentPage > 2) {
                progressDialog.dismiss();
            }
            ArrayList<OrderMaster> lstOrderMaster = (ArrayList<OrderMaster>) result;
            if (lstOrderMaster == null) {
                if (currentPage == 1) {
                    Globals.SetError(txtMsg, rvOrder, getResources().getString(R.string.MsgSelectFail), true);
                }
            } else if (lstOrderMaster.size() == 0) {
                if (currentPage == 1) {
                    Globals.SetError(txtMsg, rvOrder, getResources().getString(R.string.MsgNoRecord), true);
                }
            } else {
                if (currentPage > 1) {
                    ordersAdapter.OrderDataChanged(lstOrderMaster);
                    return;
                } else if (lstOrderMaster.size() < 10) {
                    currentPage += 1;
                }

                Globals.SetError(txtMsg, rvOrder, null, false);
                alOrderMaster = lstOrderMaster;
                SetupRecyclerView(rvOrder);
            }
        }
    }
    //endregion
}
