package com.arraybit.pos;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.WaitingMaster;
import com.arraybit.parser.WaitingJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

public class AddFragment extends Fragment {

    EditText etName, etMobileNo, etPersons;
    Button btnAdd;
    ProgressDialog pDialog;
    Context context;
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
        app_bar.setLogo(R.mipmap.app_logo);

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
                // getActivity().getSupportFragmentManager().popBackStack();
                if (!ValidateControls()) {
                    Toast.makeText(getActivity(), "Please correct the errors", Toast.LENGTH_LONG).show();
                    return;
                }
                new AddLodingTask().execute();
               /* if (Service.CheckNet(getActivity())) {
                    new AddLodingTask().execute();
                } else {
                    Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_LONG).show();
                }*/

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem mWaiting = menu.findItem(R.id.mWaiting);
        mWaiting.setVisible(false);


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
            etName.setError("Enter " + "Person Name");
            etMobileNo.setError("");
            etPersons.setError("");
            IsValid = false;
        }
        if (etMobileNo.getText().toString().equals("")) {
            etMobileNo.setError("Enter " + "Mobile No.");
            etPersons.setError("");
            IsValid = false;
        }
        if (etName.getText().toString().equals("")
                && etMobileNo.getText().toString().equals("")
                && etPersons.getText().toString().equals("")) {
            etName.setError("Enter " + "Person Name");
            etMobileNo.setError("Enter " + "Mobile No.");
            etPersons.setError("Enter " + "No. of Persons");
            IsValid = false;
        }
        if (!etMobileNo.getText().toString().equals("")) {
            if (etMobileNo.getText().length() != 10) {
                etMobileNo.setError("Enter 10 digit " + "Mobile Number" + "number");
                IsValid = false;
            }
        }
        return IsValid;
    }

    public class AddLodingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

            short i = 1;
            objWaitingMaster = new WaitingMaster();
            objWaitingMaster.setPersonName(etName.getText().toString());
            objWaitingMaster.setPersonMobile(etMobileNo.getText().toString());
            objWaitingMaster.setNoOfPersons(Short.valueOf(etPersons.getText().toString()));
            objWaitingMaster.setlinktoWaitingStatusMasterId(i);
            objWaitingMaster.setlinktoUserMasterIdCreatedBy(i);

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
                Toast.makeText(getActivity(), "Server not responding,Try again later", Toast.LENGTH_LONG).show();
            } else if (status.equals("-2")) {
                Toast.makeText(getActivity(), "Record already exist", Toast.LENGTH_LONG).show();
                ClearControls();
            } else if (!status.equals("0")) {
                objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("AddPreference", "PersonName", objWaitingMaster.getPersonName(), getActivity());

                Toast.makeText(getActivity(), "Record added successfully", Toast.LENGTH_LONG).show();
                ClearControls();

                getActivity().getSupportFragmentManager().popBackStack();
            }
            pDialog.dismiss();
        }
    }
}
