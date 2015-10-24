package com.arraybit.pos;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.RegisteredUserMaster;
import com.arraybit.parser.RegisteredUserJSONParser;
import com.rey.material.widget.EditText;

public class GuestLoginDialogFragment extends DialogFragment {

    EditText etUserName, etPassword;
    View view;
    SharePreferenceManage objSharePreferenceManage;

    RegisteredUserJSONParser objRegisteredUserJSONParser;
    RegisteredUserMaster objRegisteredUserMaster;

    public GuestLoginDialogFragment() {
        // Required empty public constructor

    }

//    @Override
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


    @NonNull


    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.fragment_guest_login_dialog);
        setRetainInstance(true);

        builder.setPositiveButton(getResources().getString(R.string.ldLogin), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etUserName = (EditText) getDialog().findViewById(R.id.etUserName);
                etPassword = (EditText) getDialog().findViewById(R.id.etPassword);


               /* if(Globals.activityName.equals(getActivity().getResources().getString(R.string.title_activity_home)))
                {
                    Intent intent = new Intent(getActivity(),GuestHomeActivity.class);
                    intent.putExtra("username",etUserName.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getActivity(),GuestOrderActivity.class);
                    intent.putExtra("username",etUserName.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }*/
//
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

        builder.setNegativeButton(getResources().getString(R.string.ldCancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
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

    public void CreateGuestPreference(){
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

        ProgressDialog pDialog;
        String strUserName, strPassword;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new ProgressDialog(getActivity());
//            pDialog.setMessage(getResources().getString(R.string.MsgLoading));
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();

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
                //pDialog.dismiss();

            } else {

                CreateGuestPreference();

                ClearControls();
                Toast.makeText(getActivity(), getResources().getString(R.string.siLoginSucessMsg), Toast.LENGTH_LONG).show();
                dismiss();
            }
            //pDialog.dismiss();
        }
    }

}
