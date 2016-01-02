package com.arraybit.pos;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.arraybit.adapter.CartItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TableMaster;
import com.arraybit.parser.OrderJOSNParser;
import com.arraybit.parser.TableJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.TextView;


@SuppressWarnings({"ConstantConditions", "unchecked"})
public class CartItemFragment extends Fragment implements CartItemAdapter.CartItemOnClickListener, View.OnClickListener {

    TextView txtMsg;
    CompoundButton cbMenu;
    LinearLayout headerLayout, cartItemFragment;
    RecyclerView rvCartItem;
    Button btnAddMore, btnConfirmOrder;
    CartItemAdapter adapter;
    OrderMaster objOrderMaster;
    String orderNumber,orderMasterId;
    SharePreferenceManage objSharePreferenceManage;
    short counterMasterId, userMasterId, waiterMasterId;
    double totalAmount;

    public CartItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_item, container, false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (MenuActivity.objTableMaster != null && MenuActivity.objTableMaster.getTableName() != null) {
                app_bar.setTitle(MenuActivity.objTableMaster.getTableName() + "  Orders");
            } else {
                app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_cart_item));
            }
        }
        //end

        cartItemFragment = (LinearLayout) view.findViewById(R.id.cartItemFragment);
        Globals.SetScaleImageBackground(getActivity(), cartItemFragment, null, null);

        setHasOptionsMenu(true);

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        cbMenu = (CompoundButton) view.findViewById(R.id.cbMenu);

        headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);

        rvCartItem = (RecyclerView) view.findViewById(R.id.rvCartItem);

        btnAddMore = (Button) view.findViewById(R.id.btnAddMore);
        btnConfirmOrder = (Button) view.findViewById(R.id.btnConfirmOrder);

        SetRecyclerView();

        cbMenu.setOnClickListener(this);
        btnAddMore.setOnClickListener(this);
        btnConfirmOrder.setOnClickListener(this);

        new OrderNumberLoadingTask().execute();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                getFragmentManager().popBackStack();
            }
//            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
//                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {
//                getActivity().getFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            } else {
//                getFragmentManager().popBackStack();
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.viewChange).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.cart_layout).setVisible(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(), cartItemFragment, null, null);
    }

    private void SetRecyclerView() {
        if (Globals.alOrderItemTran.size() == 0) {
            SetVisibility();
            txtMsg.setText(getActivity().getResources().getString(R.string.MsgCart));
        } else {
            SetVisibility();
            adapter = new CartItemAdapter(getActivity(), Globals.alOrderItemTran, this);
            rvCartItem.setAdapter(adapter);
            rvCartItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private void SetVisibility() {
        if (Globals.alOrderItemTran.size() == 0) {
            txtMsg.setVisibility(View.VISIBLE);
            cbMenu.setVisibility(View.VISIBLE);
            rvCartItem.setVisibility(View.GONE);
            headerLayout.setVisibility(View.GONE);
            btnAddMore.setVisibility(View.GONE);
            btnConfirmOrder.setVisibility(View.GONE);
        } else {
            txtMsg.setVisibility(View.GONE);
            cbMenu.setVisibility(View.GONE);
            headerLayout.setVisibility(View.VISIBLE);
            rvCartItem.setVisibility(View.VISIBLE);
            btnAddMore.setVisibility(View.VISIBLE);
            btnConfirmOrder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void ImageViewOnClick(int position) {
        adapter.RemoveData(position);
        if (Globals.alOrderItemTran.size() == 0) {
            SetRecyclerView();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddMore) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else if (v.getId() == R.id.btnConfirmOrder) {
            System.out.println("count " + getActivity().getSupportFragmentManager().getBackStackEntryAt(1).getName());
            if (MenuActivity.parentActivity) {
                GetValueFromSharePreference();
                new OrderLoadingTask().execute();

                if (!AllTablesFragment.isRefresh) {
                    new TableStatusLoadingTask().execute();
                }
            } else {
                GetValueFromSharePreference();
                new OrderLoadingTask().execute();

                if (!AllTablesFragment.isRefresh) {
                    new TableStatusLoadingTask().execute();
                }
            }
        } else if (v.getId() == R.id.cbMenu) {
            if(MenuActivity.parentActivity){
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
            else{
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        }
    }

    private void GetValueFromSharePreference() {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
            counterMasterId = Short.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
        }
        if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()) != null) {
            userMasterId = Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()));
        }
        if (objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", getActivity()) != null) {
            waiterMasterId = Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", getActivity()));
        }
    }

    //region LoadingTask
    class OrderLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            if (Globals.alOrderItemTran.size() != 0) {
                for (int i = 0; i < Globals.alOrderItemTran.size(); i++) {
                    if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() != 0) {
                        totalAmount = totalAmount + Globals.alOrderItemTran.get(i).getTotalAmount();
                    } else {
                        totalAmount = totalAmount + Globals.alOrderItemTran.get(i).getSellPrice();
                    }
                }
            }

            objOrderMaster = new OrderMaster();
            objOrderMaster.setOrderNumber(orderNumber);
            objOrderMaster.setlinktoCounterMasterId(counterMasterId);
            objOrderMaster.setlinktoTableMasterIds(String.valueOf(MenuActivity.objTableMaster.getTableMasterId()));
            objOrderMaster.setlinktoOrderTypeMasterId(MenuActivity.objTableMaster.getlinktoOrderTypeMasterId());
            objOrderMaster.setlinktoOrderStatusMasterId((short) Globals.OrderStatus.Cooking.getValue());
            objOrderMaster.setTotalAmount(totalAmount);
            objOrderMaster.setTotalTax(0.00);
            objOrderMaster.setDiscount(0.00);
            objOrderMaster.setExtraAmount(0.00);
            objOrderMaster.setTotalItemPoint((short) 0);
            objOrderMaster.setTotalDeductedPoint((short) 0);
            objOrderMaster.setlinktoWaiterMasterId(waiterMasterId);
            objOrderMaster.setlinktoUserMasterIdCreatedBy(userMasterId);
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            orderMasterId = objOrderJOSNParser.InsertOrderMaster(objOrderMaster, Globals.alOrderItemTran);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            progressDialog.dismiss();
            if (orderMasterId.equals("-1")) {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgServerNotResponding), Toast.LENGTH_LONG).show();
            } else if (!orderMasterId.equals("0")) {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgInsertSuccess), Toast.LENGTH_LONG).show();
                //getActivity().getSupportFragmentManager().popBackStack();
                if(MenuActivity.parentActivity)
                {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    OrderMaster objOrderMaster = new OrderMaster();
                    objOrderMaster.setOrderMasterId(Long.valueOf(orderMasterId));
                    fragmentTransaction.replace(android.R.id.content, new OrderSummaryFragment(objOrderMaster), getActivity().getResources().getString(R.string.title_fragment_order_summary));
                    fragmentTransaction.addToBackStack(getActivity().getResources().getString(R.string.title_fragment_order_summary));
                    fragmentTransaction.commit();
                    Globals.counter = 0;
                    Globals.alOrderItemTran.clear();
                }
                else {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    OrderMaster objOrderMaster = new OrderMaster();
                    objOrderMaster.setOrderMasterId(Long.valueOf(orderMasterId));
                    fragmentTransaction.replace(android.R.id.content, new OrderSummaryFragment(objOrderMaster), getActivity().getResources().getString(R.string.title_fragment_order_summary));
                    fragmentTransaction.addToBackStack(getActivity().getResources().getString(R.string.title_fragment_order_summary));
                    fragmentTransaction.commit();
                    Globals.counter = 0;
                    Globals.alOrderItemTran.clear();
                    //Globals.selectTableMasterId = 0;
                }
            }
        }
    }

    class OrderNumberLoadingTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            orderNumber = objOrderJOSNParser.SelectOrderNumber();
            return null;
        }
    }

    class TableStatusLoadingTask extends AsyncTask {

        TableMaster objTableMaster;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objTableMaster = new TableMaster();
            objTableMaster.setTableMasterId(MenuActivity.objTableMaster.getTableMasterId());
            objTableMaster.setlinktoTableStatusMasterId((short) Globals.TableStatus.Occupied.getValue());
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TableJSONParser objTableJSONParser = new TableJSONParser();
            status = objTableJSONParser.UpdateTableStatus(objTableMaster);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            AllTablesFragment.isRefresh = true;
        }
    }
    //endregion
}
