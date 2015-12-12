package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

import com.arraybit.adapter.OrderDetailAdapter;
import com.arraybit.global.Globals;
import com.arraybit.modal.OrderItemTran;
import com.arraybit.parser.OrderItemJSONParser;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
@SuppressWarnings({"unchecked", "ConstantConditions"})
public class OrderDetailFragment extends Fragment {


    RecyclerView rvOrderItem;
    LinearLayout headerLayout;
    int orderMasterId;
    LinearLayout orderDetailLayout;

    public OrderDetailFragment(int orderMasterId) {
        // Required empty public constructor
        this.orderMasterId = orderMasterId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_order_detail));
        }
        //end

        rvOrderItem = (RecyclerView)view.findViewById(R.id.rvOrderItem);
        rvOrderItem.setVisibility(View.GONE);

        headerLayout = (LinearLayout)view.findViewById(R.id.headerLayout);
        orderDetailLayout = (LinearLayout)view.findViewById(R.id.orderDetailLayout);
        Globals.SetScaleImageBackground(getActivity(),orderDetailLayout,null,null);

        setHasOptionsMenu(true);

        new OrderDetailLoadingTask().execute();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            if(getActivity().getSupportFragmentManager().getBackStackEntryCount() > 2) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName() != null &&
                            getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName().equals(getActivity().getResources().getString(R.string.title_fragment_order_detail))) {

                        getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_order_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    else if(getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName() != null
                            &&getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName().equals(getActivity().getResources().getString(R.string.title_fragment_all_orders))){
                        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(3).getName() !=null && getActivity().getSupportFragmentManager().getBackStackEntryAt(3).getName().equals(getActivity().getResources().getString(R.string.title_fragment_order_detail))) {

                            getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_order_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(), orderDetailLayout, null,null);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(false);
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
