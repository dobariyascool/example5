package com.arraybit.pos;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.RegisteredUserMaster;
import com.arraybit.parser.CustomerJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;


@SuppressWarnings({"ConstantConditions", "unchecked"})
public class ChangePasswordFragment extends Fragment implements View.OnClickListener {


    EditText etOldPassword, etNewPassword, etConfirmPassword;
    Button btnChangePassword;
    ToggleButton tbPasswordShowOld, tbPasswordShowNew, tbPasswordShowConfirm;
    View view;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_change_password));

        setHasOptionsMenu(true);


        //edittext
        etOldPassword = (EditText) view.findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) view.findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) view.findViewById(R.id.etConfirmPassword);
        //end

        //button
        btnChangePassword = (Button) view.findViewById(R.id.btnChangePassword);
        //end

        //togglebutton
        tbPasswordShowOld = (ToggleButton) view.findViewById(R.id.tbPasswordShowOld);
        tbPasswordShowNew = (ToggleButton) view.findViewById(R.id.tbPasswordShowNew);
        tbPasswordShowConfirm = (ToggleButton) view.findViewById(R.id.tbPasswordShowConfirm);
        //end

        //onclick event
        btnChangePassword.setOnClickListener(this);
        tbPasswordShowOld.setOnClickListener(this);
        tbPasswordShowNew.setOnClickListener(this);
        tbPasswordShowConfirm.setOnClickListener(this);
        //end


        //region TextChange Event
        etOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tbPasswordShowOld.setVisibility(View.VISIBLE);
                if (etOldPassword.getText().toString().equals("")) {
                    tbPasswordShowOld.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tbPasswordShowNew.setVisibility(View.VISIBLE);
                if (etNewPassword.getText().toString().equals("")) {
                    tbPasswordShowNew.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tbPasswordShowConfirm.setVisibility(View.VISIBLE);
                if (etConfirmPassword.getText().toString().equals("")) {
                    tbPasswordShowConfirm.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //endregion

        return view;
    }

    @Override
    public void onClick(View v) {
        view = v;
        if (v.getId() == R.id.btnChangePassword) {
            Globals.HideKeyBoard(getActivity(), v);
            if (!Validation()) {
                Globals.ShowSnackBar(v, getActivity().getResources().getString(R.string.MsgValidation), getActivity(), 2000);
                return;
            }
            if (Service.CheckNet(getActivity())) {
                new ChangePasswordLoadTask().execute();
            } else {
                Globals.ShowSnackBar(v, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 2000);
            }

        } else if (v.getId() == R.id.tbPasswordShowOld) {
            Globals.HideKeyBoard(getActivity(), v);
            if (tbPasswordShowOld.isChecked()) {
                etOldPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                etOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
            }

        } else if (v.getId() == R.id.tbPasswordShowNew) {
            Globals.HideKeyBoard(getActivity(), v);
            if (tbPasswordShowNew.isChecked()) {
                etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
            }
        } else if (v.getId() == R.id.tbPasswordShowConfirm) {
            Globals.HideKeyBoard(getActivity(), v);
            if (tbPasswordShowConfirm.isChecked()) {
                etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Globals.HideKeyBoard(getActivity(), getView());
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.login).setVisible(false);
        menu.findItem(R.id.registration).setVisible(false);
    }

    //region Private Methods
    private boolean Validation() {
        boolean IsValid = true;
        if (etOldPassword.getText().toString().equals("") && etNewPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            etOldPassword.setError("Enter " + getResources().getString(R.string.cpOldPassword));
            etNewPassword.setError("Enter " + getResources().getString(R.string.cpNewPasssword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.cpConfirmPassword));
            IsValid = false;
        } else if (!etOldPassword.getText().toString().equals("") &&
                etNewPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            etOldPassword.clearError();
            etNewPassword.setError("Enter " + getResources().getString(R.string.cpNewPasssword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.cpConfirmPassword));
            IsValid = false;
        } else if (!etOldPassword.getText().toString().equals("") &&
                !etNewPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            etOldPassword.clearError();
            etNewPassword.clearError();
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.cpConfirmPassword));
            IsValid = false;
        } else if (!etNewPassword.getText().toString().equals("") &&
                etOldPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            etNewPassword.clearError();
            etOldPassword.setError("Enter " + getResources().getString(R.string.cpOldPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.cpConfirmPassword));
            IsValid = false;
        } else if (!etNewPassword.getText().toString().equals("") && !etConfirmPassword.getText().toString().equals("")
                && etOldPassword.getText().toString().equals("")) {
            etNewPassword.clearError();
            etConfirmPassword.clearError();
            etOldPassword.setError("Enter " + getResources().getString(R.string.cpOldPassword));
            if (!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError("New and Confirm Password are not match");
            } else {
                etConfirmPassword.clearError();
            }
            IsValid = false;
        } else if (!etConfirmPassword.getText().toString().equals("")
                && etOldPassword.getText().toString().equals("") && etNewPassword.getText().toString().equals("")) {
            etConfirmPassword.clearError();
            etOldPassword.setError("Enter " + getResources().getString(R.string.cpOldPassword));
            etNewPassword.setError("Enter " + getResources().getString(R.string.cpNewPasssword));
            IsValid = false;
        } else if (!etNewPassword.getText().toString().equals("") &&
                !etConfirmPassword.getText().toString().equals("") &&
                !etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            etNewPassword.clearError();
            etOldPassword.clearError();
            etConfirmPassword.setError("New and Confirm Password are not match");
            IsValid = false;
        }
        if (IsValid) {
            etConfirmPassword.clearError();
            etOldPassword.clearError();
            etNewPassword.clearError();
        }
        return IsValid;
    }
    //endregion

    //region LoadingTask
    class ChangePasswordLoadTask extends AsyncTask {
        com.arraybit.pos.ProgressDialog progressDialog;
        String status;
        SharePreferenceManage objSharePreferenceManage;
        RegisteredUserMaster objRegisteredUserMaster;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

            objSharePreferenceManage = new SharePreferenceManage();
            objRegisteredUserMaster = new RegisteredUserMaster();

            if (objSharePreferenceManage.GetPreference("RegistrationPreference", "RegisteredUserMasterId", getActivity()) != null) {
                objRegisteredUserMaster.setRegisteredUserMasterId(Short.parseShort(objSharePreferenceManage.GetPreference("RegistrationPreference", "RegisteredUserMasterId", getActivity())));
            } else {
                objRegisteredUserMaster.setRegisteredUserMasterId(0);
            }
            objRegisteredUserMaster.setPassword(etNewPassword.getText().toString());
            objRegisteredUserMaster.setOldPassword(etOldPassword.getText().toString());
            objRegisteredUserMaster.setlinktoUserMasterIdUpdatedBy(Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity())));
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            CustomerJSONParser customerJsonParser = new CustomerJSONParser();
            status = customerJsonParser.UpdateRegisteredUserMasterPassword(objRegisteredUserMaster);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            progressDialog.dismiss();
            switch (status) {
                case "-3":
                    Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.cpOldPassword) + " is incorrect", getActivity(), 2000);
                    break;
                case "-1":
                    Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgServerNotResponding), getActivity(), 2000);
                    break;
                case "0":
                    Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgUpdatePassword), getActivity(), 2000);
                    getActivity().getSupportFragmentManager().popBackStack();
                    break;
            }


        }
    }
    //endregion
}
