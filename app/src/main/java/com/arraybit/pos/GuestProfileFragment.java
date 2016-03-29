package com.arraybit.pos;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;

import com.arraybit.adapter.SpinnerAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.modal.RegisteredUserMaster;
import com.arraybit.parser.AreaJSONParser;
import com.arraybit.parser.CustomerJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class GuestProfileFragment extends Fragment {


    TextView txtLoginChar, txtFullName, txtEmail;
    EditText etFirstName, etLastName, etPhone, etDateOfBirth;
    RadioButton rbMale, rbFemale;
    AppCompatSpinner spnrArea;
    int AreaMasterId = 0;
    Button btnUpdateProfile;
    Date birthDate;
    View view;
    UpdateResponseListener objUpdateResponseListener;


    public GuestProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_profile, container, false);
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_myprofile));

        setHasOptionsMenu(true);

        txtLoginChar = (TextView) view.findViewById(R.id.txtLoginChar);
        txtFullName = (TextView) view.findViewById(R.id.txtFullName);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);

        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        etDateOfBirth = (EditText) view.findViewById(R.id.etDateOfBirth);
        //hide keyboard
        etDateOfBirth.setInputType(InputType.TYPE_NULL);

        rbMale = (RadioButton) view.findViewById(R.id.rbMale);
        rbFemale = (RadioButton) view.findViewById(R.id.rbFemale);

        spnrArea = (AppCompatSpinner) view.findViewById(R.id.spnrArea);

        btnUpdateProfile = (Button) view.findViewById(R.id.btnUpdateProfile);

        SetUserName(container);

        if (Service.CheckNet(getActivity())) {
            new SpinnerLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = v;
                if (!ValidateControls()) {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
                    return;
                }
                if (Service.CheckNet(getActivity())) {
                    new UpdateLoadingTask().execute();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        });

        etDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Globals.ShowDatePickerDialog(etDateOfBirth, getActivity());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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

    public void EditTextOnClick() {
        Globals.ShowDatePickerDialog(etDateOfBirth, getActivity());
    }

    //region Private Methods
    private void SetUserName(View view) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()) != null) {
            txtEmail.setText(objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()));
            txtLoginChar.setText(objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()).substring(0, 1).toUpperCase());
        }
        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "FullName", getActivity()) != null) {
            txtFullName.setVisibility(View.VISIBLE);
            txtFullName.setText(objSharePreferenceManage.GetPreference("RegistrationPreference", "FullName", getActivity()));
        } else {
            txtFullName.setVisibility(View.GONE);
        }

        if (MyAccountFragment.objCustomerMaster != null) {
            etFirstName.setText(MyAccountFragment.objCustomerMaster.getCustomerName());
            etLastName.setText(MyAccountFragment.objCustomerMaster.getCustomerName());
            etPhone.setText(MyAccountFragment.objCustomerMaster.getPhone1());
            if (MyAccountFragment.objCustomerMaster.getGender().equals(rbFemale.getText().toString())) {
                rbFemale.setChecked(true);
            } else {
                rbMale.setChecked(true);
            }
            if (MyAccountFragment.objCustomerMaster.getBirthDate() != null) {
                try {
                    birthDate = new SimpleDateFormat(Globals.DateFormat, Locale.US).parse(MyAccountFragment.objCustomerMaster.getBirthDate());
                    etDateOfBirth.setText(new SimpleDateFormat(Globals.DateFormat, Locale.US).format(birthDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else{
            if (Service.CheckNet(getActivity())) {
                new UserInformationLoading().execute();
            } else {
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }

        }
    }

    private void CreateGuestPreference(RegisteredUserMaster objRegisteredUserMaster) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        objSharePreferenceManage.RemovePreference("RegistrationPreference", "FullName", getActivity());
        objSharePreferenceManage.RemovePreference("RegistrationPreference", "FirstName", getActivity());
        if (!etFirstName.getText().toString().isEmpty()) {
            objSharePreferenceManage.CreatePreference("RegistrationPreference", "FullName", String.valueOf(objRegisteredUserMaster.getFirstName()) + " " + String.valueOf(objRegisteredUserMaster.getLastName()), getActivity());
            objSharePreferenceManage.CreatePreference("RegistrationPreference", "FirstName", String.valueOf(objRegisteredUserMaster.getFirstName()), getActivity());
        }
    }

    private boolean ValidateControls() {
        boolean IsValid = true;
        if (!etPhone.getText().toString().equals("") && etPhone.getText().length() != 10) {
            etPhone.setError("Enter 10 digit " + getResources().getString(R.string.suPhone));
            IsValid = false;
        } else {
            etPhone.clearError();
        }
        return IsValid;
    }

    interface UpdateResponseListener {
        void UpdateResponse();
    }
    //endregion

    //region LoadingTask
    class UserInformationLoading extends AsyncTask {
         int customerMasterId;
        CustomerMaster objCustomerMaster;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("RegistrationPreference", "CustomerMasterId", getActivity()) != null) {
                customerMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("RegistrationPreference", "CustomerMasterId", getActivity()));
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
            objCustomerMaster = objCustomerJSONParser.SelectCustomerMaster(null, null,customerMasterId);
            return objCustomerMaster;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(objCustomerMaster!=null){
                MyAccountFragment.objCustomerMaster = objCustomerMaster;
            }
        }

    }
    class UpdateLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;
        SharePreferenceManage objSharePreferenceManage;
        RegisteredUserMaster objRegisteredUserMaster;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

            objRegisteredUserMaster = new RegisteredUserMaster();
            //objRegisteredUserMaster.setRegisteredUserMasterId(MyAccountFragment.objRegisteredUserMaster.getRegisteredUserMasterId());
            objRegisteredUserMaster.setFirstName(etFirstName.getText().toString());
            objRegisteredUserMaster.setLastName(etLastName.getText().toString());
            if (rbMale.isChecked()) {
                objRegisteredUserMaster.setGender(rbMale.getText().toString());
            }
            if (rbFemale.isChecked()) {
                objRegisteredUserMaster.setGender(rbFemale.getText().toString());
            }
            objRegisteredUserMaster.setPhone(etPhone.getText().toString());
            if (AreaMasterId != 0) {
                objRegisteredUserMaster.setlinktoAreaMasterId((short) AreaMasterId);
            }
            if (!etDateOfBirth.getText().toString().isEmpty()) {
                try {
                    birthDate = new SimpleDateFormat("d/M/yyyy", Locale.US).parse(etDateOfBirth.getText().toString());
                    objRegisteredUserMaster.setBirthDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(birthDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            objSharePreferenceManage = new SharePreferenceManage();
            objRegisteredUserMaster.setlinktoUserMasterIdUpdatedBy(Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity())));
        }

        @Override
        protected Object doInBackground(Object[] params) {
            CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
            status = objCustomerJSONParser.UpdateRegisteredUserMaster(objRegisteredUserMaster);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            switch (status) {
                case "-1":
                    Globals.ShowSnackBar(view, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                    break;
                case "0":
                    Globals.ShowSnackBar(view, getResources().getString(R.string.MsgUpdateProfile), getActivity(), 1000);
                    CreateGuestPreference(objRegisteredUserMaster);
                    if (getTargetFragment() != null) {
                        objUpdateResponseListener = (UpdateResponseListener) getTargetFragment();
                        objUpdateResponseListener.UpdateResponse();
                    }
                    getActivity().getSupportFragmentManager().popBackStack();
            }

        }
    }

    class SpinnerLoadingTask extends AsyncTask {

        ArrayList<SpinnerItem> lstSpinnerItem = new ArrayList<>();

        @Override
        protected Object doInBackground(Object[] params) {
            AreaJSONParser objAreaJSONParser = new AreaJSONParser();
            lstSpinnerItem = objAreaJSONParser.SelectAllAreaMaster();
            return lstSpinnerItem;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (lstSpinnerItem != null) {
                //SpinnerItem objSpinnerItem = new SpinnerItem();
                //objSpinnerItem.setText("--------SELECT AREA--------");
                //objSpinnerItem.setValue(0);

                //ArrayList<SpinnerItem> alSpinnerItem = new ArrayList<>();
                //alSpinnerItem.add(objSpinnerItem);
                //lstSpinnerItem.addAll(0, alSpinnerItem);

                SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), lstSpinnerItem, false);
                spnrArea.setAdapter(adapter);
                for (int i = 0; i < lstSpinnerItem.size(); i++) {
                    //if (MyAccountFragment.objRegisteredUserMaster.getlinktoAreaMasterId() == lstSpinnerItem.get(i).getValue()) {
                        spnrArea.setSelection(i);
                        break;
                    //}
                }
            }
        }
    }
    //endregion
}
