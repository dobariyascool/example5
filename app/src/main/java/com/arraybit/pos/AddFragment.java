package com.arraybit.pos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.WaitingMaster;
import com.arraybit.parser.WaitingJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

@SuppressWarnings("unchecked")
public class AddFragment extends Fragment {

    EditText etName, etMobileNo, etPersons;
    Button btnAdd;
    ProgressDialog pDialog;
    WaitingMaster objWaitingMaster;
    WaitingJSONParser objWaitingJSONParser;
    SharePreferenceManage objSharePreferenceManage;
    String status;

    public AddFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_add));

        setHasOptionsMenu(true);

        etName = (EditText) view.findViewById(R.id.etName);
        etMobileNo = (EditText) view.findViewById(R.id.etMobileNo);
        etPersons = (EditText) view.findViewById(R.id.etPersons);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.HideKeyBoard(getActivity(), v);

                if (!ValidateControls()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.MsgValidation), Toast.LENGTH_LONG).show();
                    return;
                }
//                if (Service.CheckNet(getActivity())) {
//                    new AddLodingTask().execute();
//                } else {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
//                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            Globals.HideKeyBoard(getActivity(), getView());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.mWaiting).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
    }

    void ClearControls() {
        etName.setText("");
        etMobileNo.setText("");
        etPersons.setText("");
    }

    boolean ValidateControls() {
        boolean IsValid = true;
        if (etName.getText().toString().equals("")
                && !etMobileNo.getText().toString().equals("")
                && !etPersons.getText().toString().equals("")) {
            if (etMobileNo.getText().length() != 10) {
                etMobileNo.setError("Enter 10 digit " + getResources().getString(R.string.afMobileNo));
            } else {
                etMobileNo.clearError();
            }
            etName.setError("Enter " + getResources().getString(R.string.afName));
            etMobileNo.clearError();
            etPersons.clearError();
            IsValid = false;
        } else if (etMobileNo.getText().toString().equals("")
                && !etName.getText().toString().equals("")
                && !etPersons.getText().toString().equals("")) {
            etMobileNo.setError("Enter " + getResources().getString(R.string.afMobileNo));
            etPersons.clearError();
            etName.clearError();
            IsValid = false;
        } else if (etPersons.getText().toString().equals("")
                && !etName.getText().toString().equals("")
                && !etMobileNo.getText().toString().equals("")) {
            if (etMobileNo.getText().length() != 10) {
                etMobileNo.setError("Enter 10 digit " + getResources().getString(R.string.afMobileNo));
            } else {
                etMobileNo.clearError();
            }
            etPersons.setError("Enter " + getResources().getString(R.string.afPerson));
            etName.clearError();
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etMobileNo.getText().toString().equals("")
                && etPersons.getText().toString().equals("")) {
            etPersons.setError("Enter " + getResources().getString(R.string.afPerson));
            etMobileNo.setError("Enter " + getResources().getString(R.string.afMobileNo));
            etName.setError("Enter " + getResources().getString(R.string.afName));
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etMobileNo.getText().toString().equals("")
                && !etPersons.getText().toString().equals("")) {
            etPersons.clearError();
            etMobileNo.setError("Enter " + getResources().getString(R.string.afMobileNo));
            etName.setError("Enter " + getResources().getString(R.string.afName));
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && !etMobileNo.getText().toString().equals("")
                && etPersons.getText().toString().equals("")) {
            if (etMobileNo.getText().length() != 10) {
                etMobileNo.setError("Enter 10 digit " + getResources().getString(R.string.afMobileNo));
            } else {
                etMobileNo.clearError();
            }
            etPersons.setError("Enter " + getResources().getString(R.string.afPerson));
            etName.setError("Enter " + getResources().getString(R.string.afName));
            IsValid = false;
        } else if (etMobileNo.getText().toString().equals("")
                && !etName.getText().toString().equals("")
                && etPersons.getText().toString().equals("")) {
            etPersons.setError("Enter " + getResources().getString(R.string.afPerson));
            etMobileNo.setError("Enter " + getResources().getString(R.string.afMobileNo));
            etName.clearError();
            IsValid = false;
        } else if(!etMobileNo.getText().toString().equals("")
                && !etName.getText().toString().equals("") && !etPersons.getText().toString().equals("")){
            if (etMobileNo.getText().length() != 10) {
                etMobileNo.setError("Enter 10 digit " + getResources().getString(R.string.afMobileNo));
            } else {
                etMobileNo.clearError();
            }
            etName.clearError();
            etPersons.clearError();
            IsValid = false;
        }
        return IsValid;
    }

    public class AddLodingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.MsgLoading));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

            objWaitingMaster = new WaitingMaster();
            objWaitingMaster.setPersonName(etName.getText().toString());
            objWaitingMaster.setPersonMobile(etMobileNo.getText().toString());
            objWaitingMaster.setNoOfPersons(Short.valueOf(etPersons.getText().toString()));
            objWaitingMaster.setlinktoWaitingStatusMasterId((short) Globals.WaitingStatus.valueOf("Waiting").getValue());

            objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("WaitingPreference", "UserMasterId", getActivity()) != null) {
                objWaitingMaster.setlinktoUserMasterIdCreatedBy(Short.valueOf(objSharePreferenceManage.GetPreference("WaitingPreference", "UserMasterId", getActivity())));
            }
            objWaitingJSONParser = new WaitingJSONParser();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            status = objWaitingJSONParser.InsertWaitingMaster(objWaitingMaster);
            return status;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (status.equals("-1")) {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgServerNotResponding), Toast.LENGTH_LONG).show();
            } else if (status.equals("0")) {

                Toast.makeText(getActivity(), getResources().getString(R.string.MsgInsertSuccess), Toast.LENGTH_LONG).show();
                ClearControls();

                Intent intent = new Intent(getActivity(), WaitingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
            }
            pDialog.dismiss();
        }
    }
}
