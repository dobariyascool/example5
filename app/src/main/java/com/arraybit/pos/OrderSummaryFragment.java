package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.arraybit.adapter.OrderSummaryAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.DiscountMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.SalesMaster;
import com.arraybit.modal.TableMaster;
import com.arraybit.modal.TaxMaster;
import com.arraybit.parser.ItemJSONParser;
import com.arraybit.parser.OrderJOSNParser;
import com.arraybit.parser.SalesJSONParser;
import com.arraybit.parser.TableJSONParser;
import com.arraybit.parser.TaxJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings({"unchecked", "ConstantConditions"})
@SuppressLint("ValidFragment")
public class OrderSummaryFragment extends Fragment implements View.OnClickListener, AddDiscountDialogFragment.DiscountSelectionListener, GuestLoginDialogFragment.LoginResponseListener {


    RecyclerView rvOrderItemSummery;
    LinearLayout headerLayout, amountLayout, taxLayout;
    FrameLayout orderSummeryLayout;
    OrderMaster objOrderMaster;
    TableMaster objTableMaster;
    SharePreferenceManage objSharePreferenceManage;
    ArrayList<OrderMaster> lstOrderMaster;
    ArrayList<ItemMaster> alOrderItemTran;
    short counterMasterId, userMasterId, waiterMasterId;
    double totalAmount, totalTaxPercentage, totalTaxAmount, totalTax, totalDiscount;
    String billNumber;
    ArrayList<TaxMaster> alTaxMaster;
    TextView txtTotalAmount, txtTotalDiscount, txtNetAmount, txtHeaderDiscount;
    ProgressDialog progressDialog;


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

        orderSummeryLayout = (FrameLayout) view.findViewById(R.id.orderSummeryLayout);
        Globals.SetScaleImageBackground(getActivity(), null, null, orderSummeryLayout);

        headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);
        amountLayout = (LinearLayout) view.findViewById(R.id.amountLayout);
        taxLayout = (LinearLayout) view.findViewById(R.id.taxLayout);

        txtTotalAmount = (TextView) view.findViewById(R.id.txtTotalAmount);
        txtTotalDiscount = (TextView) view.findViewById(R.id.txtTotalDiscount);
        txtNetAmount = (TextView) view.findViewById(R.id.txtNetAmount);
        txtHeaderDiscount = (TextView) view.findViewById(R.id.txtHeaderDiscount);

        rvOrderItemSummery = (RecyclerView) view.findViewById(R.id.rvOrderItemSummery);
        rvOrderItemSummery.setVisibility(View.GONE);

        Button btnAddMore = (Button) view.findViewById(R.id.btnAddMore);
        Button btnCheckOut = (Button) view.findViewById(R.id.btnCheckOut);

        SetVisibility();

        btnAddMore.setOnClickListener(this);
        btnCheckOut.setOnClickListener(this);

        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getString(R.string.title_fragment_guest_options))) {
            txtHeaderDiscount.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
        } else {
            txtHeaderDiscount.setTextColor(ContextCompat.getColor(getActivity(), R.color.accent_secondary));
            txtHeaderDiscount.setOnClickListener(this);
        }


        GetValueFromSharePreference();

        if (Service.CheckNet(getActivity())) {
            new AllOrdersLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }


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
        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                .equals(getActivity().getResources().getString(R.string.title_fragment_guest_options))) {
            Globals.SetOptionMenu(Globals.userName, getActivity(), menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName()
                    .equals(getActivity().getResources().getString(R.string.title_fragment_guest_options))) {
                Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
                intent.putExtra("ParentActivity", true);
                intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else if (MenuActivity.parentActivity) {
                Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
                intent.putExtra("ParentActivity", true);
                intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
            Globals.objDiscountMaster = null;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddMore) {
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
                if (MenuActivity.parentActivity) {
                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    intent.putExtra("ParentActivity", true);
                    intent.putExtra("TableMaster", objTableMaster);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                            && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_order_summary))) {
                        getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
//                    Intent intent = new Intent(getActivity(), MenuActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra("TableMaster", objTableMaster);
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

            } else {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_order_summary))) {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        } else if (v.getId() == R.id.btnCheckOut) {
            if (MenuActivity.parentActivity && Globals.userName == null) {
                GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
                guestLoginDialogFragment.setTargetFragment(this, 0);
                guestLoginDialogFragment.show(getActivity().getSupportFragmentManager(), "");
            } else {
                if (Service.CheckNet(getActivity())) {
                    new BillNumberLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        } else if (v.getId() == R.id.txtHeaderDiscount) {
            AddDiscountDialogFragment addDiscountDialogFragment = new AddDiscountDialogFragment();
            addDiscountDialogFragment.setTargetFragment(this, 0);
            addDiscountDialogFragment.show(getFragmentManager(), "");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(), null, null, orderSummeryLayout);
    }

    @Override
    public void DiscountCount(DiscountMaster objDiscountMaster) {
        Globals.objDiscountMaster = objDiscountMaster;
        CalculateDiscount(totalAmount, totalTax);
    }

    @Override
    public void LoginResponse() {
        if (Service.CheckNet(getActivity())) {
            new BillNumberLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(orderSummeryLayout, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }
    }

    //region Private Methods and Interface
    private void SetVisibility() {
        if (lstOrderMaster == null || lstOrderMaster.size() == 0) {
            rvOrderItemSummery.setVisibility(View.GONE);
            headerLayout.setVisibility(View.GONE);
            amountLayout.setVisibility(View.GONE);
        } else {
            rvOrderItemSummery.setVisibility(View.VISIBLE);
            headerLayout.setVisibility(View.VISIBLE);
            amountLayout.setVisibility(View.VISIBLE);
        }
    }


    private void SetSalesList(ArrayList<OrderMaster> lstOrderMaster, ArrayList<ItemMaster> lstOrderItemTran) {
        alOrderItemTran = new ArrayList<>();
        ArrayList<ItemMaster> alOrderItemModifierTran;
        ArrayList<ItemMaster> alOrderItem;
        ItemMaster objItemMaster;
        for (int i = 0; i < lstOrderMaster.size(); i++) {
            alOrderItemModifierTran = new ArrayList<>();
            objItemMaster = new ItemMaster();
            alOrderItem = new ArrayList<>();
            for (int j = 0; j < lstOrderItemTran.size(); j++) {
                if (lstOrderMaster.get(i).getOrderMasterId() == lstOrderItemTran.get(j).getLinktoOrderMasterId()) {
                    alOrderItem.add(lstOrderItemTran.get(j));
                }
            }
            for (int k = 0; k < alOrderItem.size(); k++) {
                if (alOrderItem.get(k).getItemModifierIds().equals("0")) {
                    alOrderItemModifierTran = new ArrayList<>();
                    objItemMaster = alOrderItem.get(k);
                    if (k == alOrderItem.size() - 1) {
                        objItemMaster.setAlOrderItemModifierTran(alOrderItemModifierTran);
                        alOrderItemTran.add(objItemMaster);
                    } else if (k != alOrderItem.size() - 1) {
                        if (alOrderItem.get(k + 1).getItemModifierIds().equals("0")) {
                            objItemMaster.setAlOrderItemModifierTran(alOrderItemModifierTran);
                            alOrderItemTran.add(objItemMaster);
                        }
                    }
                } else {
                    alOrderItemModifierTran.add(alOrderItem.get(k));
                    if (k == alOrderItem.size() - 1) {
                        objItemMaster.setAlOrderItemModifierTran(alOrderItemModifierTran);
                        alOrderItemTran.add(objItemMaster);
                    } else if (k != alOrderItem.size() - 1) {
                        if (alOrderItem.get(k + 1).getItemModifierIds().equals("0")) {
                            objItemMaster.setAlOrderItemModifierTran(alOrderItemModifierTran);
                            alOrderItemTran.add(objItemMaster);
                        }
                    }
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

    private void CalculateAmount() {
        if (lstOrderMaster.size() != 0) {
            for (int i = 0; i < lstOrderMaster.size(); i++) {
                totalAmount = totalAmount + lstOrderMaster.get(i).getTotalAmount();
            }
        }
        txtTotalAmount.setText(Globals.dfWithPrecision.format(totalAmount));
        SetTextLayout();
        totalTax = ((totalAmount * totalTaxPercentage) / 100) + totalTaxAmount;
        CalculateDiscount(totalAmount, totalTax);
    }

    private void CalculateDiscount(double totalAmount, double totalTax) {
        if (Globals.objDiscountMaster != null) {
            if (Globals.objDiscountMaster.getIsPercentage()) {
                totalDiscount = (totalAmount * Globals.objDiscountMaster.getDiscount()) / 100;
            } else {
                totalDiscount = Globals.objDiscountMaster.getDiscount();
            }

            if (totalDiscount <= totalAmount) {
                txtTotalDiscount.setText(Globals.dfWithPrecision.format(totalDiscount));
                txtNetAmount.setText(Globals.dfWithPrecision.format((totalAmount + totalTax) - totalDiscount));
            } else {
                txtTotalDiscount.setText(Globals.dfWithPrecision.format(0.00));
            }
        } else {
            txtNetAmount.setText(Globals.dfWithPrecision.format(totalAmount + totalTax));
        }
    }

    @SuppressLint("SetTextI18n")
    private void SetTextLayout() {
        LinearLayout[] linearLayout = new LinearLayout[alTaxMaster.size()];
        TextView[] txtTaxName = new TextView[alTaxMaster.size()];
        TextView[] txtTaxRate = new TextView[alTaxMaster.size()];

        for (int i = 0; i < alTaxMaster.size(); i++) {

            linearLayout[i] = new LinearLayout(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout[i].setOrientation(LinearLayout.HORIZONTAL);
            linearLayout[i].setLayoutParams(layoutParams);
            linearLayout[i].setPadding(8, 0, 8, 0);

            txtTaxName[i] = new TextView(getActivity());
            LinearLayout.LayoutParams txtTaxNameParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtTaxNameParams.weight = 0.5f;
            txtTaxName[i].setLayoutParams(txtTaxNameParams);
            txtTaxName[i].setGravity(Gravity.START);
            txtTaxName[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
            txtTaxName[i].setTextSize(10f);

            txtTaxRate[i] = new TextView(getActivity());
            LinearLayout.LayoutParams txtTaxRateParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtTaxRateParams.weight = 0.5f;
            txtTaxName[i].setLayoutParams(txtTaxRateParams);
            txtTaxRate[i].setGravity(Gravity.END);
            txtTaxRate[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
            txtTaxRate[i].setTextSize(10f);

            if (alTaxMaster.get(i).getIsPercentage()) {
                String str = String.valueOf(alTaxMaster.get(i).getTaxRate());
                String precision = str.substring(str.lastIndexOf(".") + 1);

                if (Integer.valueOf(precision) > 0) {
                    txtTaxName[i].setText(alTaxMaster.get(i).getTaxName() + "  [" + " " + str + " % ]");
                } else {
                    txtTaxName[i].setText(alTaxMaster.get(i).getTaxName() + "  [" + " " + str.substring(0, str.lastIndexOf(".")) + " % ]");
                }
                txtTaxRate[i].setText(Globals.dfWithPrecision.format((totalAmount * alTaxMaster.get(i).getTaxRate()) / 100));
                totalTaxPercentage = totalTaxPercentage + alTaxMaster.get(i).getTaxRate();
            } else {
                txtTaxName[i].setText(alTaxMaster.get(i).getTaxName());
                txtTaxRate[i].setText(Globals.dfWithPrecision.format(alTaxMaster.get(i).getTaxRate()));
                totalTaxAmount = totalTaxAmount + alTaxMaster.get(i).getTaxRate();
            }

            linearLayout[i].addView(txtTaxName[i]);
            linearLayout[i].addView(txtTaxRate[i]);
            taxLayout.addView(linearLayout[i]);
        }

    }
    //endregion

    //region LoadingTask
    class AllOrdersLoadingTask extends AsyncTask {

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

                new TaxLoadingTask().execute();
                new OrderSummeryLoadingTask().execute();
            }
        }
    }

    class OrderSummeryLoadingTask extends AsyncTask {

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
                OrderSummaryAdapter orderSummeryAdapter = new OrderSummaryAdapter(getActivity(), lstOrderItemTran, lstOrderMaster);
                SetVisibility();
                rvOrderItemSummery.setAdapter(orderSummeryAdapter);
                rvOrderItemSummery.setLayoutManager(new LinearLayoutManager(getActivity()));
                SetSalesList(lstOrderMaster, lstOrderItemTran);
                CalculateAmount();
            }
        }
    }

    class BillNumberLoadingTask extends AsyncTask {

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
        protected Object doInBackground(Object[] objects) {

            SalesJSONParser objSalesJSONParser = new SalesJSONParser();
            billNumber = objSalesJSONParser.SelectBillNumber();
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            new InsertSalesMasterLodingTask().execute();
        }
    }

    class InsertSalesMasterLodingTask extends AsyncTask {

        SalesMaster objSalesMaster;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objSalesMaster = new SalesMaster();
            objSalesMaster.setBillNumber(billNumber);
            objSalesMaster.setlinktoCounterMasterId(counterMasterId);
            objSalesMaster.setlinktoTableMasterIds(String.valueOf(objTableMaster.getTableMasterId()));
            objSalesMaster.setlinktoWaiterMasterId(waiterMasterId);
            objSalesMaster.setlinktoCustomerMasterId(0);
            objSalesMaster.setlinktoOrderTypeMasterId(objTableMaster.getlinktoOrderTypeMasterId());
            objSalesMaster.setlinktoOrderStatusMasterId((short) Globals.OrderStatus.Ready.getValue());
            objSalesMaster.setTotalAmount(totalAmount);
            objSalesMaster.setTotalTax(totalTax);
            objSalesMaster.setDiscountAmount(totalDiscount);
            objSalesMaster.setDiscountPercentage(0.00);
            objSalesMaster.setExtraAmount(0.00);
            objSalesMaster.setTotalItemDiscount(0.00);
            objSalesMaster.setTotalItemTax(0.00);
            objSalesMaster.setNetAmount(Double.valueOf(txtNetAmount.getText().toString()));
            objSalesMaster.setPaidAmount(0.00);
            objSalesMaster.setBalanceAmount(0.00);
            objSalesMaster.setRemark("");
            objSalesMaster.setIscomplimentary(false);
            objSalesMaster.setTotalItemPoint((short) 0);
            objSalesMaster.setTotalDeductedPoint((short) 0);
            objSalesMaster.setlinktoBusinessMasterId(Globals.businessMasterId);
            objSalesMaster.setlinktoUserMasterIdCreatedBy(userMasterId);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            SalesJSONParser objSalesJSONParser = new SalesJSONParser();
            status = objSalesJSONParser.InsertSalesMaster(objSalesMaster, alOrderItemTran, alTaxMaster, lstOrderMaster);
            return status;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (status.equals("-1")) {
                Globals.ShowSnackBar(orderSummeryLayout, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
            } else if (status.equals("0")) {
                new TableStatusLoadingTask().execute();
            }
        }
    }

    class TableStatusLoadingTask extends AsyncTask {

        TableMaster objTable;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objTable = new TableMaster();
            objTable.setTableMasterId(objTableMaster.getTableMasterId());
            objTable.setlinktoTableStatusMasterId((short) Globals.TableStatus.Dirty.getValue());
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TableJSONParser objTableJSONParser = new TableJSONParser();
            status = objTableJSONParser.UpdateTableStatus(objTable);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (status.equals("0")) {
                if (MenuActivity.parentActivity) {
                    Globals.counter = 0;
                    Globals.alOrderItemTran.clear();
                    Globals.alOrderItemSummery = new ArrayList<>();
                    Globals.alOrderMasterId = new ArrayList<>();
                    Globals.ReplaceFragment(new ThankYouFragment(), getActivity().getSupportFragmentManager(), null);
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
    }

    class TaxLoadingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TaxJSONParser objTaxJSONParser = new TaxJSONParser();
            return objTaxJSONParser.SelectAllTaxMaster();
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            alTaxMaster = (ArrayList<TaxMaster>) result;
        }
    }
    //endregion
}
