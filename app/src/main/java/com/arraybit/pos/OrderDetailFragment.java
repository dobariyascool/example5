package com.arraybit.pos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.OrderDetailAdapter;
import com.arraybit.adapter.OrdersAdapter;
import com.arraybit.modal.OrderItemTran;
import com.arraybit.parser.OrderItemJSONParser;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class OrderDetailFragment extends Fragment {


    RecyclerView rvOrderItem;
    LinearLayout headerLayout;
    int orderMasterId;

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        rvOrderItem = (RecyclerView)view.findViewById(R.id.rvOrderItem);
        rvOrderItem.setVisibility(View.GONE);

        headerLayout = (LinearLayout)view.findViewById(R.id.headerLayout);

        this.orderMasterId = OrdersAdapter.orderMasterId;

        new OrderDetailLoadingTask().execute();

        return view;
    }

    public class OrderDetailLoadingTask extends AsyncTask {

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
            OrderItemJSONParser objOrderItemJSONParser = new OrderItemJSONParser();
            return objOrderItemJSONParser.SelectAllOrderItemTran(orderMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            ArrayList<OrderItemTran> lstOrderItemTran = (ArrayList<OrderItemTran>) result;

            if (lstOrderItemTran != null) {
                OrderDetailAdapter orderDetailAdapter=new OrderDetailAdapter(getActivity(),lstOrderItemTran);
                rvOrderItem.setVisibility(View.VISIBLE);
                rvOrderItem.setAdapter(orderDetailAdapter);
                rvOrderItem.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }
    }
}
