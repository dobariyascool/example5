package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.OrderItemTran;
import com.arraybit.modal.OrderMaster;
import com.arraybit.parser.OrderItemJSONParser;
import com.arraybit.parser.OrderJOSNParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

@SuppressLint("ValidFragment")
@SuppressWarnings("unchecked")
public class OrderStatusDialogFragment extends DialogFragment implements View.OnClickListener {

    Button btnCooking, btnServed, btnReady, btnCancel;
    OrderItemTran objOrderItemTran, objOrderItem;
    OrderMaster objOrderMaster, objOrder;
    boolean isOrder;
    short userMasterId, waiterMasterId;
    UpdateStatusListener objUpdateStatusListener;

    public OrderStatusDialogFragment(OrderItemTran objOrderItemTran, OrderMaster objOrderMaster, boolean isOrder) {
        this.objOrderItemTran = objOrderItemTran;
        this.isOrder = isOrder;
        this.objOrderMaster = objOrderMaster;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_status_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        TextView txtTableName = (TextView) view.findViewById(R.id.txtTableName);
        TextView txtItemName = (TextView) view.findViewById(R.id.txtItemName);

        LinearLayout nameLayout = (LinearLayout) view.findViewById(R.id.nameLayout);
        if (Build.VERSION.SDK_INT >= 21) {
            nameLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
            nameLayout.setElevation(8f);
            nameLayout.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        } else {
            nameLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.card_view_with_border));
        }

        if (isOrder) {
            if (Build.VERSION.SDK_INT >= 21) {
                nameLayout.setPadding(16, 10, 16, 20);
                txtTableName.setPadding(16, 10, 16, 0);
            } else {
                txtTableName.setPadding(24, 20, 24, 20);
            }
            txtTableName.setText(objOrderMaster.getTableName() + " [ " + objOrderMaster.getOrderNumber() + " ]");
            txtTableName.setTypeface(null, Typeface.BOLD);
            txtItemName.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                nameLayout.setPadding(16, 10, 16, 20);
                txtTableName.setPadding(16, 10, 16, 0);
                txtItemName.setPadding(16, 0, 16, 0);
            } else {
                txtTableName.setPadding(24, 20, 24, 0);
                txtItemName.setPadding(24, 0, 24, 20);
            }
            txtItemName.setVisibility(View.VISIBLE);
            txtTableName.setText(objOrderMaster.getTableName() + " [ " + objOrderMaster.getOrderNumber() + " ]");
            txtItemName.setTypeface(null, Typeface.BOLD);
            txtItemName.setText(objOrderItemTran.getItem());
        }

        btnCooking = (Button) view.findViewById(R.id.btnCooking);
        btnServed = (Button) view.findViewById(R.id.btnServed);
        btnReady = (Button) view.findViewById(R.id.btnReady);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        SetButtonVisibility();
        GetValueFromPreference();

        objUpdateStatusListener = (UpdateStatusListener) getTargetFragment();

        btnServed.setOnClickListener(this);
        btnCooking.setOnClickListener(this);
        btnReady.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCooking) {
            if (isOrder) {
                objOrder = new OrderMaster();
                objOrder.setlinktoOrderStatusMasterId((short) Globals.OrderStatus.Cooking.getValue());
                objOrder.setOrderMasterId(objOrderMaster.getOrderMasterId());
                objOrder.setlinktoWaiterMasterId(waiterMasterId);
                objOrder.setlinktoUserMasterIdUpdatedBy(userMasterId);
            } else {
                objOrderItem = new OrderItemTran();
                objOrderItem.setOrderItemTranIds(objOrderItemTran.getOrderItemTranIds());
                objOrderItem.setlinktoOrderStatusMasterId((short) Globals.OrderStatus.Cooking.getValue());
                objOrderItem.setlinktoUserMasterIdUpdatedBy(userMasterId);
            }
        } else if (v.getId() == R.id.btnReady) {
            if (isOrder) {
                objOrder = new OrderMaster();
                objOrder.setlinktoOrderStatusMasterId((short) Globals.OrderStatus.Ready.getValue());
                objOrder.setOrderMasterId(objOrderMaster.getOrderMasterId());
                objOrder.setlinktoWaiterMasterId(waiterMasterId);
                objOrder.setlinktoUserMasterIdUpdatedBy(userMasterId);
            } else {
                objOrderItem = new OrderItemTran();
                objOrderItem.setOrderItemTranIds(objOrderItemTran.getOrderItemTranIds());
                objOrderItem.setlinktoOrderStatusMasterId((short) Globals.OrderStatus.Ready.getValue());
                objOrderItem.setlinktoUserMasterIdUpdatedBy(userMasterId);
            }
        } else if (v.getId() == R.id.btnServed) {
            if (isOrder) {
                objOrder = new OrderMaster();
                objOrder.setlinktoOrderStatusMasterId((short) Globals.OrderStatus.Served.getValue());
                objOrder.setOrderMasterId(objOrderMaster.getOrderMasterId());
                objOrder.setlinktoWaiterMasterId(waiterMasterId);
                objOrder.setlinktoUserMasterIdUpdatedBy(userMasterId);
            } else {
                objOrderItem = new OrderItemTran();
                objOrderItem.setOrderItemTranIds(objOrderItemTran.getOrderItemTranIds());
                objOrderItem.setlinktoOrderStatusMasterId((short) Globals.OrderStatus.Served.getValue());
                objOrderItem.setlinktoUserMasterIdUpdatedBy(userMasterId);
            }
        } else if (v.getId() == R.id.btnCancel) {
            if (isOrder) {
                objOrder = new OrderMaster();
                objOrder.setlinktoOrderStatusMasterId((short) Globals.OrderStatus.Cancelled.getValue());
                objOrder.setOrderMasterId(objOrderMaster.getOrderMasterId());
                objOrder.setlinktoWaiterMasterId(waiterMasterId);
                objOrder.setlinktoUserMasterIdUpdatedBy(userMasterId);
            } else {
                objOrderItem = new OrderItemTran();
                objOrderItem.setOrderItemTranIds(objOrderItemTran.getOrderItemTranIds());
                objOrderItem.setlinktoOrderStatusMasterId((short) Globals.OrderStatus.Cancelled.getValue());
                objOrderItem.setlinktoUserMasterIdUpdatedBy(userMasterId);
            }
        }
        if (Service.CheckNet(getActivity())) {
            new UpdateOrderStatusLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }
        dismiss();
    }

    //region Private Methods and Interface
    private void SetButtonVisibility() {
        if (isOrder) {
            if (objOrderMaster.getlinktoOrderStatusMasterId() != null) {
                if (objOrderMaster.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Cooking.getValue()) {
                    btnCooking.setSelected(true);
                } else if (objOrderMaster.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Cancelled.getValue()) {
                    btnCancel.setSelected(true);
                } else if (objOrderMaster.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Ready.getValue()) {
                    btnReady.setSelected(true);
                } else if (objOrderMaster.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Served.getValue()) {
                    btnServed.setSelected(true);
                }
            }
        } else {
            if (objOrderItemTran.getlinktoOrderStatusMasterId() != null) {
                if (objOrderItemTran.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Cooking.getValue()) {
                    btnCooking.setSelected(true);
                } else if (objOrderItemTran.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Cancelled.getValue()) {
                    btnCancel.setSelected(true);
                } else if (objOrderItemTran.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Ready.getValue()) {
                    btnReady.setSelected(true);
                } else if (objOrderItemTran.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Served.getValue()) {
                    btnServed.setSelected(true);
                }
            }
        }
    }

    private void GetValueFromPreference() {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()) != null) {
            userMasterId = Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()));
        }
        if (objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", getActivity()) != null) {
            waiterMasterId = Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", getActivity()));
        }
    }

    interface UpdateStatusListener {
        void UpdateStatus(boolean flag, boolean isOrder, OrderItemTran objOrderItemTran, short linktoOrderStatusMasterId, boolean isTotalUpdate);
    }
    //endregion

    //region LoadingTask
    @SuppressWarnings("ConstantConditions")
    class UpdateOrderStatusLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;
        String status;
        Short linktoOrderMasterId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

            if (!isOrder) {
                if (objOrderItemTran.getlinktoOrderStatusMasterId() != null) {
                    linktoOrderMasterId = objOrderItemTran.getlinktoOrderStatusMasterId();
                }
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isOrder) {
                OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
                status = objOrderJOSNParser.UpdateOrderMasterStatus(objOrder, false);
            } else {
                OrderItemJSONParser objOrderItemJSONParser = new OrderItemJSONParser();
                status = objOrderItemJSONParser.UpdateOrderItemTranStatus(objOrderItem);
            }

            return status;
        }

        @Override
        protected void onPostExecute(Object result) {
            progressDialog.dismiss();
            if (status.equals("-1")) {
                objUpdateStatusListener.UpdateStatus(false, isOrder, null, (short) 0, false);
            } else if (status.equals("0")) {
                if (isOrder) {
                    objUpdateStatusListener.UpdateStatus(true, isOrder, null, objOrder.getlinktoOrderStatusMasterId(), false);
                } else {
                    if (objOrderItem.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Cancelled.getValue()) {
                        new UpdateOrderMasterTotalLoadingTask().execute();
                    } else if (linktoOrderMasterId != null && linktoOrderMasterId == Globals.OrderStatus.Cancelled.getValue()) {
                        new UpdateOrderMasterTotalLoadingTask().execute();
                    } else {
                        objUpdateStatusListener.UpdateStatus(true, isOrder, objOrderItem, (short) 0, false);
                    }
                }
            }
        }
    }

    class UpdateOrderMasterTotalLoadingTask extends AsyncTask {
        String status;
        OrderMaster objOrderFilter;
        double totalModifier;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            objOrderFilter = new OrderMaster();
            objOrderFilter.setOrderMasterId(objOrderMaster.getOrderMasterId());
            if (!objOrderItemTran.getModifierRates().equals("")) {
                String[] strModifier = objOrderItemTran.getModifierRates().split(",");
                for (String strModifierPrice : strModifier) {
                    totalModifier = totalModifier + (Double.valueOf(strModifierPrice) * objOrderItemTran.getQuantity());
                }
            }
            if (objOrderItem.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Cancelled.getValue()) {
                objOrderFilter.setTotalAmount((objOrderMaster.getTotalAmount()) - (objOrderItemTran.getRate() * objOrderItemTran.getQuantity()) - totalModifier);
            } else {
                objOrderFilter.setTotalAmount(objOrderMaster.getTotalAmount() + (objOrderItemTran.getRate() * objOrderItemTran.getQuantity()) + totalModifier);
            }
            objOrderFilter.setlinktoWaiterMasterId(waiterMasterId);
            objOrderFilter.setlinktoUserMasterIdUpdatedBy(userMasterId);
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            status = objOrderJOSNParser.UpdateOrderMasterStatus(objOrderFilter, true);
            return status;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (status.equals("0")) {
                objOrderItemTran.setlinktoOrderStatusMasterId(objOrderItem.getlinktoOrderStatusMasterId());
                objUpdateStatusListener.UpdateStatus(true, isOrder, objOrderItemTran, (short) 0, true);
            }
        }
    }
    //endregion
}
