package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.arraybit.global.Globals;
import com.arraybit.modal.WaitingMaster;
import com.arraybit.parser.WaitingJSONParser;
import com.rey.material.widget.Button;

@SuppressLint("ValidFragment")
public class WaitingStatusFragment extends DialogFragment implements View.OnClickListener {

    Button btnServe, btnNot, btnCancel;
    String waitingStatus;
    short WaitingMasterId;
    UpdateStatusListener objUpdateStatusListener;

    WaitingMaster objWaitingMaster = null;

    public WaitingStatusFragment(WaitingMaster objWaitingMaster) {
        WaitingMasterId = (short) objWaitingMaster.getWaitingMasterId();
        waitingStatus = objWaitingMaster.getWaitingStatus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_status, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        btnServe = (Button) view.findViewById(R.id.btnServe);
        btnNot = (Button) view.findViewById(R.id.btnNot);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        SetButtonVisibility();

        btnServe.setOnClickListener(this);
        btnNot.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        objUpdateStatusListener = (UpdateStatusListener) getTargetFragment();

        return view;
    }

    @Override
    public void onClick(View v) {

        objWaitingMaster = new WaitingMaster();
        if (v.getId() == R.id.btnServe) {
            objWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.valueOf(btnServe.getText().toString()).getValue());
            objWaitingMaster.setWaitingMasterId(WaitingMasterId);

            new UpdateWaitingStatusLoadingTask().execute();
            dismiss();

        }
        if (v.getId() == R.id.btnNot) {
            objWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.valueOf("Not").getValue());
            objWaitingMaster.setWaitingMasterId(WaitingMasterId);


            new UpdateWaitingStatusLoadingTask().execute();
            dismiss();
        }
        if (v.getId() == R.id.btnCancel) {

            objWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.valueOf(btnCancel.getText().toString()).getValue());
            objWaitingMaster.setWaitingMasterId(WaitingMasterId);

            new UpdateWaitingStatusLoadingTask().execute();
            dismiss();
        }
    }

    void SetButtonVisibility() {
        if (waitingStatus.equals(btnServe.getText().toString())) {
            btnServe.setVisibility(View.GONE);
            btnNot.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        } else if (waitingStatus.equals(btnCancel.getText().toString())) {
            btnServe.setVisibility(View.VISIBLE);
            btnNot.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.GONE);
        } else if (waitingStatus.equals("Not")) {
            btnServe.setVisibility(View.VISIBLE);
            btnNot.setVisibility(View.GONE);
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            btnServe.setVisibility(View.VISIBLE);
            btnNot.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        }
    }

    public interface UpdateStatusListener {
        void UpdateStatus(boolean flag);
    }

    class UpdateWaitingStatusLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
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
                objUpdateStatusListener.UpdateStatus(true);
            }
            progressDialog.dismiss();
        }
    }
}
