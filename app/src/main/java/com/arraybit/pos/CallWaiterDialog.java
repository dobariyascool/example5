package com.arraybit.pos;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.arraybit.modal.CategoryMaster;
import com.arraybit.modal.WaiterNotificationMaster;
import com.arraybit.parser.WaiterNotificationJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.util.ArrayList;

public class CallWaiterDialog extends DialogFragment {

    EditText etMessage;
    String status;
    Button btnCall;

    public CallWaiterDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_waiter_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        etMessage = (EditText) view.findViewById(R.id.etMessage);
        btnCall = (Button) view.findViewById(R.id.btnCall);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InsertLodingTask().execute();
            }
        });
        return view;
    }

    //region Loading Task
    @SuppressWarnings("ConstantConditions")
    public class InsertLodingTask extends AsyncTask {
        ArrayList<CategoryMaster> alCategoryMaster;
        com.arraybit.pos.ProgressDialog progressDialog;
        WaiterNotificationMaster objWaiterNotificationMaster;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
            objWaiterNotificationMaster = new WaiterNotificationMaster();
            if (GuestHomeActivity.objTableMaster != null) {
                objWaiterNotificationMaster.setlinktoTableMasterId(GuestHomeActivity.objTableMaster.getTableMasterId());
            }
            if (etMessage.getText() != null && !etMessage.getText().toString().equals("")) {
                objWaiterNotificationMaster.setMessage(etMessage.getText().toString());
            }
            objWaiterNotificationMaster.setMessage(etMessage.getText().toString());

        }

        @Override
        protected Object doInBackground(Object[] params) {
            WaiterNotificationJSONParser objWaiterNotificationJSONParser = new WaiterNotificationJSONParser();
            status = objWaiterNotificationJSONParser.InsertWaiterNotification(objWaiterNotificationMaster);
            return status;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            if (status.equals("-1")) {
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.MsgServerNotResponding),Toast.LENGTH_LONG).show();
            } else if (status.equals("0")) {
                Toast.makeText(getActivity(),"Done",Toast.LENGTH_LONG).show();
                dismiss();
            }
        }
    }
    //endregion

}
