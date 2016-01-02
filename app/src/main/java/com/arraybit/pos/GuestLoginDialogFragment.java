package com.arraybit.pos;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.RegisteredUserMaster;
import com.arraybit.parser.RegisteredUserJSONParser;
import com.rey.material.widget.EditText;

@SuppressWarnings("unchecked")
public class GuestLoginDialogFragment extends DialogFragment {

    EditText etUserName = null, etPassword = null;
    View view;
    SharePreferenceManage objSharePreferenceManage;

    RegisteredUserJSONParser objRegisteredUserJSONParser;
    RegisteredUserMaster objRegisteredUserMaster;

    public GuestLoginDialogFragment() {
        // Required empty public constructor

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.fragment_guest_login_dialog);
        builder.setPositiveButton(getResources().getString(R.string.ldLogin), null);
        builder.setNegativeButton(getResources().getString(R.string.ldCancel), null);
        builder.setCancelable(false);
        //setRetainInstance(false);

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button positive = (alertDialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etUserName = (EditText) getDialog().findViewById(R.id.etUserName);
                        etPassword = (EditText) getDialog().findViewById(R.id.etPassword);
                        if (!ValidateControls()) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.MsgValidation), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (Service.CheckNet(getActivity())) {
                            new SignInLodingTask().execute();

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                Button negative = (alertDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

            }
        });
        return alertDialog;
    }

    boolean ValidateControls() {
        boolean IsValid = true;

        if (etUserName.getText().toString().equals("")) {
            etUserName.setError("Enter" + getResources().getString(R.string.siUserName));
            IsValid = false;
        }
        if (etPassword.getText().toString().equals("")) {
            etPassword.setError("Enter " + getResources().getString(R.string.siPassword));
            IsValid = false;
        }
        if (!Globals.IsValidEmail(etUserName.getText().toString())) {
            etUserName.setError("Enter " + getResources().getString(R.string.siUserName));
            IsValid = false;
        }

        return IsValid;
    }

    public void CreateGuestPreference() {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()) == null) {
            objSharePreferenceManage.CreatePreference("RegistrationPreference", "UserName", etUserName.getText().toString(), getActivity());
        }
        if (objSharePreferenceManage.GetPreference("RegisteredUserMasterIdPreference", "RegisteredUserMasterId", getActivity()) == null) {
            objSharePreferenceManage.CreatePreference("RegisteredUserMasterIdPreference", "RegisteredUserMasterId", String.valueOf(objRegisteredUserMaster.getRegisteredUserMasterId()), getActivity());
        }
        if (objSharePreferenceManage.GetPreference("RegistrationPreferenceFullName", "FullName", getActivity()) == null) {
            objSharePreferenceManage.CreatePreference("RegistrationPreferenceFullName", "FullName", String.valueOf(objRegisteredUserMaster.getFirstName()) + " " + String.valueOf(objRegisteredUserMaster.getLastName()), getActivity());
        }

    }

    void ClearControls() {
        etUserName.setText("");
        etPassword.setText("");
    }

    public class SignInLodingTask extends AsyncTask {

        String strUserName, strPassword;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            strUserName = etUserName.getText().toString();
            strPassword = etPassword.getText().toString();
            objRegisteredUserJSONParser = new RegisteredUserJSONParser();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            objRegisteredUserMaster = objRegisteredUserJSONParser.SelectRegisteredUserMasterUserName(strUserName, strPassword);
            return objRegisteredUserMaster;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (result == null) {
                Toast.makeText(getActivity(), getResources().getString(R.string.siLoginFailedMsg), Toast.LENGTH_LONG).show();
            } else {

                CreateGuestPreference();
                ClearControls();
                Toast.makeText(getActivity(), getResources().getString(R.string.siLoginSucessMsg), Toast.LENGTH_LONG).show();
                dismiss();
                Intent intent = new Intent(getActivity(),GuestHomeActivity.class);
                intent.putExtra("TableMaster",GuestHomeActivity.objTableMaster);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    //region Comment Code
    //    @Override
    // @NonNull
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.fragment_guest_login_dialog, container,false);
//        getDialog().setTitle(getResources().getString(R.string.title_activity_login));
//
//        etUserName=(EditText)view.findViewById(R.id.etUserName);
//        etPassword=(EditText)view.findViewById(R.id.etPassword);
//
//        btnSignIn=(Button)view.findViewById(R.id.btnSignIn);
//        btnCancle=(Button)view.findViewById(R.id.btnCancle);
//
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(getActivity(),GuestHomeActivity.class);
//                intent.putExtra("username",etUserName.getText().toString());
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                dismiss();
//            }
//        });
//
//        btnCancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//    }
    //endregion
}
