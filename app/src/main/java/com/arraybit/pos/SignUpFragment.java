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
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.RegisteredUserMaster;
import com.arraybit.parser.RegisteredUserJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;


public class SignUpFragment extends Fragment implements View.OnClickListener {

    EditText etFirstName, etLastName, etEmail, etPassword, etPhone;
    RadioGroup rgMain;
    RadioButton rbMale, rbFemale;
    Button btnSignUp;
    ProgressDialog pDialog;
    RegisteredUserJSONParser objRegisteredUserJSONParser;
    RegisteredUserMaster objRegisteredUserMaster;
    String status;


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
        //app_bar.setLogo(R.mipmap.home);
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

        //compound button
        CompoundButton cbSignIn = (CompoundButton) view.findViewById(R.id.cbSignIn);
        //end

        //event
        cbSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        //end

        setHasOptionsMenu(true);

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

            // Intent intent = new Intent(getActivity(), SignInActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //startActivity(intent);
        } else if (v.getId() == R.id.cbSignIn) {
            getActivity().getSupportFragmentManager().popBackStack();
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
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("");
            etPassword.setError("");
            IsValid = false;
        }

        if (etPassword.getText().toString().equals("")) {
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etEmail.setError("");
            etFirstName.setError("");
            IsValid = false;
        }
        if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etEmail.setError("Enter" + getResources().getString(R.string.suEmail));

            IsValid = false;
        }

        if (!Globals.IsValidEmail(etEmail.getText().toString())) {
            if (etFirstName.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")
                    && !etPassword.getText().toString().equals("")) {
                etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
                etFirstName.setError("");
                etPassword.setError("");
                IsValid = false;
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
                IsValid = false;
            }
        }

        if (!etPhone.getText().toString().equals("")) {
            if (etPhone.getText().length() != 10) {
                etPhone.setError("Enter 10 digit " + getResources().getString(R.string.suPhone) + "number");
                IsValid = false;
            }
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

            short i = 1;
            objRegisteredUserMaster = new RegisteredUserMaster();
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
            objRegisteredUserMaster.setlinktoAreaMasterId(i);
            objRegisteredUserMaster.setlinktoSourceMasterId((short) Globals.sourceMasterId);
            objRegisteredUserMaster.setlinktoUserMasterIdCreatedBy(i);
            objRegisteredUserMaster.setlinktoUserMasterIdUpdatedBy(i);
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

            if (status.equals("-1")) {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgServerNotResponding), Toast.LENGTH_LONG).show();
            } else if (status.equals("-2")) {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgAlreadyExist), Toast.LENGTH_LONG).show();
                ClearControls();
            } else if (status.equals("0")) {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgInsertSuccess), Toast.LENGTH_LONG).show();
                ClearControls();

                getActivity().getSupportFragmentManager().popBackStack();
            }
            pDialog.dismiss();
        }
    }

}
