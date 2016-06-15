package com.arraybit.pos;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.CartItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TableMaster;
import com.arraybit.modal.TaxMaster;
import com.arraybit.parser.OrderJOSNParser;
import com.arraybit.parser.TableJSONParser;
import com.arraybit.parser.TaxJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


@SuppressWarnings({"ConstantConditions", "unchecked"})
public class CartItemFragment extends Fragment implements CartItemAdapter.CartItemOnClickListener, View.OnClickListener, RemarkDialogFragment.RemarkResponseListener, AddItemQtyDialogFragment.QtyRemarkDialogResponseListener {

    TextView txtMsg,txtEditMessage;
    CompoundButton cbMenu;
    LinearLayout headerLayout, cartItemFragment;
    RecyclerView rvCartItem;
    CardView cvRemark;
    Button btnAddMore, btnConfirmOrder;
    CartItemAdapter adapter;
    OrderMaster objOrderMaster;
    String orderNumber, orderMasterId;
    SharePreferenceManage objSharePreferenceManage;
    short counterMasterId, userMasterId, waiterMasterId;
    double totalAmount, totalTax, netAmount;
    View view;
    TextView txtRemark;
    ArrayList<TaxMaster> alTaxMaster;
    int position;

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
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        //end

        txtRemark = (TextView) view.findViewById(R.id.txtRemark);

        cvRemark = (CardView) view.findViewById(R.id.cvRemark);

        cartItemFragment = (LinearLayout) view.findViewById(R.id.cartItemFragment);
        Globals.SetScaleImageBackground(getActivity(), cartItemFragment, null, null);

        setHasOptionsMenu(true);

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        txtEditMessage = (TextView) view.findViewById(R.id.txtEditMessage);

        cbMenu = (CompoundButton) view.findViewById(R.id.cbMenu);

        headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);

        rvCartItem = (RecyclerView) view.findViewById(R.id.rvCartItem);

        btnAddMore = (Button) view.findViewById(R.id.btnAddMore);
        btnConfirmOrder = (Button) view.findViewById(R.id.btnConfirmOrder);
        Button btnRemark = (Button) view.findViewById(R.id.btnRemark);

        SetRecyclerView();

        cbMenu.setOnClickListener(this);
        btnAddMore.setOnClickListener(this);
        btnConfirmOrder.setOnClickListener(this);
        btnRemark.setOnClickListener(this);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                RemarkDialogFragment.strRemark = null;
            } else {
                RemarkDialogFragment.strRemark = null;
                getFragmentManager().popBackStack();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.viewChange).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.cart_layout).setVisible(false);
        if (MenuActivity.parentActivity) {
            Globals.SetOptionMenu(Globals.userName, getActivity(), menu);
        }
        if(Globals.isWishListShow==0){
            menu.findItem(R.id.logout).setVisible(false);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(), cartItemFragment, null, null);
    }

    @Override
    public void ImageViewOnClick(int position) {
        adapter.RemoveData(position);
        if (Globals.alOrderItemTran.size() == 0) {
            SetRecyclerView();
        }
    }

    @Override
    public void EditCartItem(ItemMaster objItemMaster,int position) {
        this.position = position;
        AddItemQtyDialogFragment addItemQtyDialogFragment = new AddItemQtyDialogFragment(objItemMaster);
        Bundle bundle = new Bundle();
        bundle.putBoolean("IsEdit",true);
        addItemQtyDialogFragment.setArguments(bundle);
        addItemQtyDialogFragment.setTargetFragment(this,0);
        addItemQtyDialogFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddMore) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else if (v.getId() == R.id.btnConfirmOrder) {
            if (MenuActivity.parentActivity) {
                GetValueFromSharePreference();

                if (Service.CheckNet(getActivity())) {
                    view = v;
                    new TaxLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(v, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }

            } else {
                GetValueFromSharePreference();
                if (Service.CheckNet(getActivity())) {
                    view = v;
                    new TaxLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(v, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        } else if (v.getId() == R.id.cbMenu) {
            RemarkDialogFragment.strRemark = null;
            if (MenuActivity.parentActivity) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            } else {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        } else if (v.getId() == R.id.btnRemark) {
            RemarkDialogFragment remarkDialogFragment = new RemarkDialogFragment();
            remarkDialogFragment.setTargetFragment(this, 0);
            remarkDialogFragment.show(getActivity().getSupportFragmentManager(), "");
        }
    }

    @Override
    public void RemarkResponse() {
        if (RemarkDialogFragment.strRemark != null) {
            txtRemark.setVisibility(View.VISIBLE);
            txtRemark.setText(RemarkDialogFragment.strRemark);
        } else {
            txtRemark.setVisibility(View.GONE);
            txtRemark.setText("");
        }
    }

    @Override
    public void UpdateQtyRemarkResponse(ItemMaster objOrderItem) {
        adapter.UpdateData(position,objOrderItem);
    }

    //region Private Methods and Interface
    private void SetRecyclerView() {
        if (Globals.alOrderItemTran.size() == 0) {
            SetVisibility();
            txtMsg.setText(getActivity().getResources().getString(R.string.MsgCart));
        } else {
            SetVisibility();
            adapter = new CartItemAdapter(getActivity(), Globals.alOrderItemTran, this, false);
            rvCartItem.setAdapter(adapter);
            rvCartItem.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvCartItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Globals.HideKeyBoard(getActivity(), recyclerView);
                    if (!adapter.isItemAnimate) {
                        adapter.isItemAnimate = true;
                        adapter.isModifierChanged = false;
                    }
                }
            });
        }
    }

    private void SetVisibility() {
        if (Globals.alOrderItemTran.size() == 0) {
            txtMsg.setVisibility(View.VISIBLE);
            cbMenu.setVisibility(View.VISIBLE);
            cvRemark.setVisibility(View.GONE);
            rvCartItem.setVisibility(View.GONE);
            headerLayout.setVisibility(View.GONE);
            btnAddMore.setVisibility(View.GONE);
            btnConfirmOrder.setVisibility(View.GONE);
            txtEditMessage.setVisibility(View.GONE);
        } else {
            txtMsg.setVisibility(View.GONE);
            cbMenu.setVisibility(View.GONE);
            cvRemark.setVisibility(View.VISIBLE);
            headerLayout.setVisibility(View.VISIBLE);
            rvCartItem.setVisibility(View.VISIBLE);
            btnAddMore.setVisibility(View.VISIBLE);
            btnConfirmOrder.setVisibility(View.VISIBLE);
            txtEditMessage.setVisibility(View.VISIBLE);
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
    //endregion

    //region LoadingTask
    class OrderLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

            if (Globals.alOrderItemTran.size() != 0) {
                for (int i = 0; i < Globals.alOrderItemTran.size(); i++) {
                    if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() != 0) {
                        totalAmount = totalAmount + Globals.alOrderItemTran.get(i).getTotalAmount();
                        totalTax = totalTax + Globals.alOrderItemTran.get(i).getTotalTax();
                        netAmount = totalAmount + totalTax;
                    } else {
                        totalAmount = totalAmount + Globals.alOrderItemTran.get(i).getSellPrice();
                        totalTax = totalTax + Globals.alOrderItemTran.get(i).getTotalTax();
                        netAmount = totalAmount + totalTax;
                    }
                }
            }

            objOrderMaster = new OrderMaster();
            objOrderMaster.setOrderNumber(orderNumber);
            objOrderMaster.setlinktoCounterMasterId(counterMasterId);
            objOrderMaster.setlinktoTableMasterIds(String.valueOf(MenuActivity.objTableMaster.getTableMasterId()));
            objOrderMaster.setlinktoOrderTypeMasterId(MenuActivity.objTableMaster.getlinktoOrderTypeMasterId());
            objOrderMaster.setTotalAmount(totalAmount);
            objOrderMaster.setTotalTax(totalTax);
            objOrderMaster.setDiscount(0.00);
            objOrderMaster.setExtraAmount(0.00);
            objOrderMaster.setNetAmount(netAmount);
            objOrderMaster.setTotalItemPoint((short) 0);
            objOrderMaster.setTotalDeductedPoint((short) 0);
            objOrderMaster.setlinktoWaiterMasterId(waiterMasterId);
            objOrderMaster.setlinktoUserMasterIdCreatedBy(userMasterId);
            objOrderMaster.setRateIndex(Globals.alOrderItemTran.get(0).getRateIndex());
            if (Globals.userName != null) {
                objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("RegistrationPreference", "CustomerMasterId", getActivity()) != null) {
                    int customerMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("RegistrationPreference", "CustomerMasterId", getActivity()));
                    objOrderMaster.setlinktoCustomerMasterId((short) customerMasterId);
                }
            }
            objOrderMaster.setLinktoBusinessMasterId(Globals.businessMasterId);
            if (RemarkDialogFragment.strRemark != null) {
                objOrderMaster.setRemark(RemarkDialogFragment.strRemark);
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            orderMasterId = objOrderJOSNParser.InsertOrderMaster(objOrderMaster, Globals.alOrderItemTran, alTaxMaster);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            progressDialog.dismiss();
            if (orderMasterId.equals("-1")) {
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
            } else if (!orderMasterId.equals("0")) {
                if (!AllTablesFragment.isRefresh) {
                    new TableStatusLoadingTask().execute();
                }
                RemarkDialogFragment.strRemark = null;
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgConfirmOrder), getActivity(), 1000);
                if (MenuActivity.parentActivity) {

                    Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
                    intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    Globals.counter = 0;
                    Globals.alOrderItemTran.clear();
                } else {

                    Globals.ReplaceFragment(new OrderSummaryFragment(), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_order_summary));
                    Globals.counter = 0;
                    Globals.alOrderItemTran.clear();
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
            orderNumber = objOrderJOSNParser.SelectOrderNumber(Globals.businessMasterId);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (orderNumber != null) {
                new OrderLoadingTask().execute();
            }
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

            alTaxMaster = (ArrayList<TaxMaster>) result;
            new OrderNumberLoadingTask().execute();
        }
    }

    //endregion
}
