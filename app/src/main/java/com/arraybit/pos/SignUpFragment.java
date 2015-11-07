package com.arraybit.pos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.arraybit.adapter.SpinnerAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.RegisteredUserMaster;
import com.arraybit.parser.AreaJSONParser;
import com.arraybit.parser.RegisteredUserJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.util.ArrayList;


@SuppressWarnings("unchecked")
public class SignUpFragment extends Fragment implements View.OnClickListener {

    EditText etFirstName, etLastName, etEmail, etPassword, etPhone;
    RadioGroup rgMain;
    RadioButton rbMale, rbFemale;
    Button btnSignUp;
    Spinner spnrArea;
    ProgressDialog pDialog;
    RegisteredUserJSONParser objRegisteredUserJSONParser;
    RegisteredUserMaster objRegisteredUserMaster;
    AreaJSONParser objAreaJSONParser;
    SharePreferenceManage objSharePreferenceManage;
    String status;
    int AreaMasterId;

    SpinnerAdapter adapter;
    ArrayList<SpinnerItem> lstSpinnerItem = new ArrayList<>();

    public SignUpFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_signup));
        //end

        //EditText
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        //end

        //Radiogroup
        rgMain = (RadioGroup) view.findViewById(R.id.rgMain);
        //

        //RadioButton
        rbMale = (RadioButton) view.findViewById(R.id.rbMale);
        rbFemale = (RadioButton) view.findViewById(R.id.rbFemale);
        //end

        //button
        btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        //end

        //Spinner
        spnrArea = (Spinner) view.findViewById(R.id.spnrArea);
        //

        //compound button
        CompoundButton cbSignIn = (CompoundButton) view.findViewById(R.id.cbSignIn);
        //end

        //event
        cbSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        //end

        setHasOptionsMenu(true);

        spnrArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View v = parent.getAdapter().getView(position, view, parent);
                AreaMasterId = v.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new SpinnerLoadingTask().execute();

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnSignUp) {
            if (!ValidateControls()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgValidation), Toast.LENGTH_SHORT).show();
                return;
            }
            if (Service.CheckNet(getActivity())) {
                new SignUpLoadingTask().execute();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.cbSignIn) {
            getActivity().getSupportFragmentManager().popBackStack();
            GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
            guestLoginDialogFragment.show(getFragmentManager(), "");
        }
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

    boolean ValidateControls() {
        boolean IsValid = true;

        if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.clearError();
            IsValid = false;

        } else if (etEmail.getText().toString().equals("")
                && !etFirstName.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.clearError();
            etFirstName.clearError();
            IsValid = false;
        } else if (etPassword.getText().toString().equals("")
                && !etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etFirstName.clearError();
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")) {
            etPassword.clearError();
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            IsValid = false;
        } else if (etEmail.getText().toString().equals("")
                && !etFirstName.getText().toString().equals("")
                && etPassword.getText().toString().equals("")) {
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etFirstName.clearError();
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            IsValid = false;
        } else if(!etFirstName.getText().toString().equals("") && !etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("")){
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
                IsValid = false;
            }
            etFirstName.clearError();
            etPassword.clearError();
        }
        if (!etPhone.getText().toString().equals("") && etPhone.getText().length() != 10) {
            etPhone.setError("Enter 10 digit " + getResources().getString(R.string.suPhone));
            IsValid = false;
        } else {
            etPhone.clearError();
        }
        return IsValid;
    }

    void ClearControls() {
        etFirstName.setText("");
        etLastName.setText("");
        etPassword.setText("");
        etEmail.setText("");
        etPhone.setText("");
    }

    public class SignUpLoadingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.MsgLoading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            objRegisteredUserMaster = new RegisteredUserMaster();
            objSharePreferenceManage = new SharePreferenceManage();
            objRegisteredUserMaster.setFirstName(etFirstName.getText().toString());
            objRegisteredUserMaster.setLastName(etLastName.getText().toString());
            objRegisteredUserMaster.setPassword(etPassword.getText().toString());
            if (rbMale.isChecked()) {
                objRegisteredUserMaster.setGender(rbMale.getText().toString());
            }
            if (rbFemale.isChecked()) {
                objRegisteredUserMaster.setGender(rbFemale.getText().toString());
            }
            objRegisteredUserMaster.setEmail(etEmail.getText().toString());
            objRegisteredUserMaster.setPhone(etPhone.getText().toString());
            objRegisteredUserMaster.setlinktoAreaMasterId((short) AreaMasterId);
            objRegisteredUserMaster.setlinktoSourceMasterId((short) Globals.sourceMasterId);
            objRegisteredUserMaster.setlinktoUserMasterIdCreatedBy(Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity())));
            objRegisteredUserMaster.setIsEnabled(true);

            objRegisteredUserJSONParser = new RegisteredUserJSONParser();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            status = objRegisteredUserJSONParser.InsertRegisteredUserMaster(objRegisteredUserMaster);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            switch (status) {
                case "-1":
                    Toast.makeText(getActivity(), getResources().getString(R.string.MsgServerNotResponding), Toast.LENGTH_LONG).show();
                    break;
                case "-2":
                    Toast.makeText(getActivity(), getResources().getString(R.string.MsgAlreadyExist), Toast.LENGTH_LONG).show();
                    ClearControls();
                    break;
                case "0":
                    Toast.makeText(getActivity(), getResources().getString(R.string.MsgInsertSuccess), Toast.LENGTH_LONG).show();
                    ClearControls();

                    getActivity().getSupportFragmentManager().popBackStack();
                    break;
            }
            pDialog.dismiss();
        }
    }

    public class SpinnerLoadingTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            objAreaJSONParser = new AreaJSONParser();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            lstSpinnerItem = objAreaJSONParser.SelectAllAreaMaster();
            return lstSpinnerItem;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            adapter = new SpinnerAdapter(getActivity(), lstSpinnerItem);
            spnrArea.setAdapter(adapter);
        }
    }
}
