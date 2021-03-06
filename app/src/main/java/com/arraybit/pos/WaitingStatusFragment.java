package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.arraybit.modal.TableMaster;
import com.arraybit.modal.WaitingMaster;
import com.arraybit.parser.TableJSONParser;
import com.arraybit.parser.WaitingJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

@SuppressWarnings("unchecked")
@SuppressLint("ValidFragment")
public class WaitingStatusFragment extends DialogFragment implements View.OnClickListener {

    Button btnServe, btnNot, btnCancel, btnCall, btnWaiting;
    UpdateStatusListener objUpdateStatusListener;
    FragmentTransaction fragmentTransaction;
    boolean isOrderPlace;
    int btnId;
    View focusView;


    WaitingMaster objWaitingMaster = null, objWaiting;

    public WaitingStatusFragment(WaitingMaster objWaitingMaster) {
        objWaiting = new WaitingMaster();
        objWaiting = objWaitingMaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_status, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        LinearLayout nameLayout = (LinearLayout) view.findViewById(R.id.nameLayout);
        if (Build.VERSION.SDK_INT >= 21) {
            nameLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
            nameLayout.setElevation(8f);
            nameLayout.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        } else {
            nameLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.card_view_with_border));
        }

        btnWaiting = (Button) view.findViewById(R.id.btnWaiting);
        btnServe = (Button) view.findViewById(R.id.btnServe);
        btnNot = (Button) view.findViewById(R.id.btnNot);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCall = (Button) view.findViewById(R.id.btnCall);

        SetButtonVisibility();

        btnWaiting.setOnClickListener(this);
        btnServe.setOnClickListener(this);
        btnNot.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnCall.setOnClickListener(this);

        Drawable[] drawable = btnWaiting.getCompoundDrawablesRelative();
        drawable[1].mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.waitingTitleIconColor), PorterDuff.Mode.SRC_IN);

        objUpdateStatusListener = (UpdateStatusListener) getTargetFragment();

        TextView txtPersonName = (TextView) view.findViewById(R.id.txtPersonName);
        TextView txtPersonNo = (TextView) view.findViewById(R.id.txtPersonNo);

        txtPersonName.setText(objWaiting.getPersonName());
        if (objWaiting.getPersonMobile() != null) {
            txtPersonNo.setVisibility(View.VISIBLE);
            txtPersonNo.setText(objWaiting.getPersonMobile());

        } else {
            txtPersonNo.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        objWaitingMaster = new WaitingMaster();
        focusView = v;
        if (v.getId() == R.id.btnWaiting) {
            if (objWaiting.getlinktoWaitingStatusMasterId() == Globals.WaitingStatus.Assign.getValue()) {
                if (Service.CheckNet(getActivity())) {
                    btnId = R.id.btnWaiting;
                    new OrderLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            } else {
                objWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.valueOf(btnWaiting.getText().toString()).getValue());
                objWaitingMaster.setWaitingMasterId(objWaiting.getWaitingMasterId());

                if (Service.CheckNet(getActivity())) {
                    new UpdateWaitingStatusLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
                dismiss();
            }
        }
        if (v.getId() == R.id.btnServe) {
            if (WaitingActivity.isTableAssign) {
                objWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.valueOf(btnServe.getText().toString()).getValue());
                objWaitingMaster.setWaitingMasterId(objWaiting.getWaitingMasterId());
                dismiss();
                AllTablesFragment allTablesFragment = new AllTablesFragment(getActivity(), false, null);
                Bundle bundle = new Bundle();
                bundle.putParcelable("WaitingMaster", objWaiting);
                bundle.putBoolean("IsClick", true);
                allTablesFragment.setArguments(bundle);
                ReplaceFragment(allTablesFragment);
                objUpdateStatusListener = (UpdateStatusListener) getActivity();
                objUpdateStatusListener.UpdateStatus(true);
            } else {
                objWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.valueOf(btnServe.getText().toString()).getValue());
                objWaitingMaster.setWaitingMasterId(objWaiting.getWaitingMasterId());
                if (Service.CheckNet(getActivity())) {
                    new UpdateWaitingStatusLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
                dismiss();
            }
        }
        if (v.getId() == R.id.btnNot) {
            if (objWaiting.getlinktoWaitingStatusMasterId() == Globals.WaitingStatus.Assign.getValue()) {
                if (Service.CheckNet(getActivity())) {
                    btnId = R.id.btnNot;
                    new OrderLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            } else {
                objWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.valueOf("NA").getValue());
                objWaitingMaster.setWaitingMasterId(objWaiting.getWaitingMasterId());

                if (Service.CheckNet(getActivity())) {
                    new UpdateWaitingStatusLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
                dismiss();
            }

        }
        if (v.getId() == R.id.btnCancel) {
            if (objWaiting.getlinktoWaitingStatusMasterId() == Globals.WaitingStatus.Assign.getValue()) {
                if (Service.CheckNet(getActivity())) {
                    btnId = R.id.btnCancel;
                    new OrderLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            } else {
                objWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.valueOf(btnCancel.getText().toString()).getValue());
                objWaitingMaster.setWaitingMasterId(objWaiting.getWaitingMasterId());

                if (Service.CheckNet(getActivity())) {
                    new UpdateWaitingStatusLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
                dismiss();
            }

        }
        if (v.getId() == R.id.btnCall) {

            //call the calling interface
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0" + objWaiting.getPersonMobile()));
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            dismiss();
        }
    }

    //region Private Methods and Interface
    void SetButtonVisibility() {
        if (objWaiting.getWaitingStatus().equals(btnWaiting.getText().toString())) {
            btnWaiting.setVisibility(View.GONE);
            btnServe.setVisibility(View.VISIBLE);
            btnNot.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        } else if (objWaiting.getWaitingStatus().equals(btnServe.getText().toString())) {
            btnWaiting.setVisibility(View.VISIBLE);
            btnServe.setVisibility(View.GONE);
            btnNot.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        } else if (objWaiting.getWaitingStatus().equals(btnCancel.getText().toString())) {
            btnWaiting.setVisibility(View.VISIBLE);
            btnServe.setVisibility(View.VISIBLE);
            btnNot.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.GONE);
        } else if (objWaiting.getWaitingStatus().equals(btnNot.getText().toString())) {
            btnWaiting.setVisibility(View.VISIBLE);
            btnServe.setVisibility(View.VISIBLE);
            btnNot.setVisibility(View.GONE);
            btnCancel.setVisibility(View.VISIBLE);
//        } else {
//            btnWaiting.setVisibility(View.VISIBLE);
//            btnServe.setVisibility(View.VISIBLE);
//            btnNot.setVisibility(View.VISIBLE);
//            btnCancel.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("CommitTransaction")
    private void ReplaceFragment(Fragment fragment) {
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        fragmentTransaction.replace(R.id.fragmentLayout, fragment);
        fragmentTransaction.commit();
    }

    interface UpdateStatusListener {
        void UpdateStatus(boolean flag);
    }
    //endregion

    //region LoadingTask
    class UpdateWaitingStatusLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()) != null) {
                objWaitingMaster.setLinktoUserMasterIdUpdatedBy(Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity())));
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            WaitingJSONParser objWaitingJSONParser = new WaitingJSONParser();
            status = objWaitingJSONParser.UpdateWaitingStatus(objWaitingMaster);

            return status;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (status.equals("-1")) {
                objUpdateStatusListener.UpdateStatus(false);
            } else if (status.equals("0")) {
                if (isOrderPlace) {
                    objUpdateStatusListener.UpdateStatus(true);
                } else {
                    new UpdateTableStatusLoadingTask().execute();
                }
            }
            progressDialog.dismiss();
        }
    }

    class OrderLoadingTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            WaitingJSONParser objWaitingJSONParser = new WaitingJSONParser();
            isOrderPlace = objWaitingJSONParser.CheckOrderPlaceForTableMasterId(Globals.businessMasterId, String.valueOf(objWaiting.getLinktoTableMasterId()));

            return isOrderPlace;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (isOrderPlace) {
                Globals.ShowSnackBar(focusView, getResources().getString(R.string.MsgOrderPlace), getActivity(), 2000);
            } else {
                if (btnId == R.id.btnWaiting) {
                    objWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.valueOf(btnWaiting.getText().toString()).getValue());
                    objWaitingMaster.setWaitingMasterId(objWaiting.getWaitingMasterId());
                } else if (btnId == R.id.btnNot) {
                    objWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.valueOf("NA").getValue());
                    objWaitingMaster.setWaitingMasterId(objWaiting.getWaitingMasterId());
                } else if (btnId == R.id.btnCancel) {
                    objWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.valueOf(btnCancel.getText().toString()).getValue());
                    objWaitingMaster.setWaitingMasterId(objWaiting.getWaitingMasterId());
                }
                if (Service.CheckNet(getActivity())) {
                    new UpdateWaitingStatusLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(focusView, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        }
    }

    class UpdateTableStatusLoadingTask extends AsyncTask {

        String status;
        TableMaster objTable;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            objTable = new TableMaster();
            objTable.setlinktoTableStatusMasterId((short) Globals.TableStatus.Vacant.getValue());
            objTable.setTableMasterId(objWaiting.getLinktoTableMasterId());
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TableJSONParser objTableJSONParser = new TableJSONParser();
            status = objTableJSONParser.UpdateTableStatus(objTable);

            return status;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            objUpdateStatusListener.UpdateStatus(true);
            dismiss();
        }
    }
    //endregion
}
