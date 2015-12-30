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
import com.arraybit.modal.OrderMaster;
import com.arraybit.parser.OrderItemJSONParser;
import com.rey.material.widget.Button;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
@SuppressWarnings({"unchecked", "ConstantConditions"})
public class OrderDetailFragment extends Fragment {


    RecyclerView rvOrderItem;
    LinearLayout headerLayout;
    OrderMaster objOrderMaster;
    LinearLayout orderDetailFragment;
    Button btnAddMore,btnCheckOut;
    ArrayList<OrderItemTran> lstOrderItemTran;

    public OrderDetailFragment(OrderMaster objOrderMaster) {
        this.objOrderMaster = objOrderMaster;
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
            if(objOrderMaster!=null && objOrderMaster.getTableName()!=null){
                app_bar.setTitle(objOrderMaster.getTableName()+" Summary");
            }
            else {
                app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_order_detail));
            }
        }
        //end

        rvOrderItem = (RecyclerView)view.findViewById(R.id.rvOrderItem);
        rvOrderItem.setVisibility(View.GONE);

        btnAddMore = (Button)view.findViewById(R.id.btnAddMore);
        btnCheckOut = (Button)view.findViewById(R.id.btnCheckOut);

        headerLayout = (LinearLayout)view.findViewById(R.id.headerLayout);
        orderDetailFragment = (LinearLayout)view.findViewById(R.id.orderDetailFragment);
        Globals.SetScaleImageBackground(getActivity(),orderDetailFragment,null,null);

        lstOrderItemTran = new ArrayList<>();
        SetVisibility();

        setHasOptionsMenu(true);

        new OrderDetailLoadingTask().execute();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            if(getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1).getName()!=null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_order_detail)))
            {
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_order_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
//            if(getActivity().getSupportFragmentManager().getBackStackEntryCount() > 2) {
//                    if (getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName() != null &&
//                            getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName().equals(getActivity().getResources().getString(R.string.title_fragment_order_detail))) {
//
//                        getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_order_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    }
//                    else if(getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName() != null
//                            &&getActivity().getSupportFragmentManager().getBackStackEntryAt(2).getName().equals(getActivity().getResources().getString(R.string.title_fragment_all_orders))){
//                        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(3).getName() !=null && getActivity().getSupportFragmentManager().getBackStackEntryAt(3).getName().equals(getActivity().getResources().getString(R.string.title_fragment_order_detail))) {
//
//                            getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_order_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                        }
//                    }
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void SetVisibility() {
        if (lstOrderItemTran.size() == 0) {
            rvOrderItem.setVisibility(View.GONE);
            btnAddMore.setVisibility(View.GONE);
            btnCheckOut.setVisibility(View.GONE);
        } else {
            rvOrderItem.setVisibility(View.VISIBLE);
            btnAddMore.setVisibility(View.VISIBLE);
            btnCheckOut.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(), orderDetailFragment, null,null);
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
            return objOrderItemJSONParser.SelectAllOrderItemTran((int) objOrderMaster.getOrderMasterId());
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            lstOrderItemTran = (ArrayList<OrderItemTran>) result;

            if (lstOrderItemTran != null) {
                OrderDetailAdapter orderDetailAdapter=new OrderDetailAdapter(getActivity(),lstOrderItemTran);
                SetVisibility();
                rvOrderItem.setAdapter(orderDetailAdapter);
                rvOrderItem.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }
    }
}
