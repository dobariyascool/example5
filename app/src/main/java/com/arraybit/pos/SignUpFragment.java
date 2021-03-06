package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.arraybit.adapter.SpinnerAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.parser.AreaJSONParser;
import com.arraybit.parser.CustomerJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.util.ArrayList;


@SuppressWarnings("unchecked")
public class SignUpFragment extends Fragment implements View.OnClickListener {

    EditText etFirstName, etLastName, etEmail, etPassword, etPhone;
    RadioGroup rgMain;
    RadioButton rbMale, rbFemale;
    Button btnSignUp;
    AppCompatSpinner spnrArea;
    AreaJSONParser objAreaJSONParser;
    SharePreferenceManage objSharePreferenceManage;
    String status;
    int AreaMasterId;
    View view;
    CustomerMaster objCustomerMaster;

    SpinnerAdapter adapter;
    ArrayList<SpinnerItem> lstSpinnerItem = new ArrayList<>();
    GuestLoginDialogFragment.LoginResponseListener objLoginResponseListener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);
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
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_signup));
        //end
        Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary), ContextCompat.getColor(getActivity(), android.R.color.white));

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
        spnrArea = (AppCompatSpinner) view.findViewById(R.id.spnrArea);
        //

        //compound button
        CompoundButton cbSignIn = (CompoundButton) view.findViewById(R.id.cbSignIn);
        CompoundButton cbPrivacyPolicy = (CompoundButton) view.findViewById(R.id.cbPrivacyPolicy);
        CompoundButton cbTermsofService = (CompoundButton) view.findViewById(R.id.cbTermsofService);
        //end

        //event
        cbSignIn.setOnClickListener(this);
        cbPrivacyPolicy.setOnClickListener(this);
        cbTermsofService.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        //end

        Globals.CustomView(btnSignUp, ContextCompat.getColor(getActivity(),R.color.accent), ContextCompat.getColor(getActivity(),android.R.color.white));
        btnSignUp.setTextColor(ContextCompat.getColor(getActivity(),R.color.primary));

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

        if (Service.CheckNet(getActivity())) {
            new SpinnerLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        view = v;
        Globals.HideKeyBoard(getActivity(), v);
        if (v.getId() == R.id.btnSignUp) {
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
                return;
            }
            if (Service.CheckNet(getActivity())) {
                new SignUpLoadingTask().execute();
            } else {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
        } else if (v.getId() == R.id.cbSignIn) {
            if (getTargetFragment() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
                GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
                guestLoginDialogFragment.setTargetFragment(getTargetFragment(), 0);
                guestLoginDialogFragment.show(getFragmentManager(), "");
            } else {
                getActivity().getSupportFragmentManager().popBackStack();
                GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
                guestLoginDialogFragment.show(getFragmentManager(), "");
            }

        } else if (v.getId() == R.id.cbPrivacyPolicy) {
            ReplaceFragment(new PolicyFragment((short) 1));
        } else if (v.getId() == R.id.cbTermsofService) {
            ReplaceFragment(new PolicyFragment((short) 1));
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.login).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.callWaiter).setVisible(false);
        menu.findItem(R.id.cart_layout).setVisible(false);

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

    //region Private Methods and Interface
    private boolean ValidateControls() {
        boolean IsValid = true;

        if (spnrArea.getSelectedItemId() == 0) {
            Globals.ShowSnackBar(view, "Select Area", getActivity(), 1000);
        }
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
        } else if (!etFirstName.getText().toString().equals("") && !etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
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

    private void ClearControls() {
        etFirstName.setText("");
        etLastName.setText("");
        etPassword.setText("");
        etEmail.setText("");
        etPhone.setText("");
    }

    @SuppressLint("RtlHardcoded")
    private void ReplaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(350);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(R.id.signUpFragment, fragment, getActivity().getResources().getString(R.string.title_fragment_policy));
        fragmentTransaction.addToBackStack(getActivity().getResources().getString(R.string.title_fragment_policy));
        fragmentTransaction.commit();
    }

    private void CreateGuestPreference() {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()) == null) {
            objSharePreferenceManage.CreatePreference("RegistrationPreference", "UserName", objCustomerMaster.getEmail1(), getActivity());
        }
        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "CustomerMasterId", getActivity()) == null) {
            objSharePreferenceManage.CreatePreference("RegistrationPreference", "CustomerMasterId", String.valueOf(status), getActivity());
        }
        if (objCustomerMaster.getCustomerName() != null) {
            if (objSharePreferenceManage.GetPreference("RegistrationPreference", "FullName", getActivity()) == null) {
                objSharePreferenceManage.CreatePreference("RegistrationPreference", "FullName", objCustomerMaster.getCustomerName(), getActivity());
            }
            if (objSharePreferenceManage.GetPreference("RegistrationPreference", "FirstName", getActivity()) == null) {
                objSharePreferenceManage.CreatePreference("RegistrationPreference", "FirstName", objCustomerMaster.getCustomerName(), getActivity());
            }
        }
        Globals.userName = objCustomerMaster.getEmail1();
    }

    //endregion

    //region LoadingTask
    class SignUpLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

            objCustomerMaster = new CustomerMaster();
            objSharePreferenceManage = new SharePreferenceManage();
            objCustomerMaster.setCustomerName(etFirstName.getText().toString().trim() + " " + etLastName.getText().toString().trim());
            objCustomerMaster.setPassword(etPassword.getText().toString().trim());
            if (rbMale.isChecked()) {
                objCustomerMaster.setGender(rbMale.getText().toString());
            }
            if (rbFemale.isChecked()) {
                objCustomerMaster.setGender(rbFemale.getText().toString());
            }
            objCustomerMaster.setEmail1(etEmail.getText().toString().trim());
            objCustomerMaster.setPhone1(etPhone.getText().toString().trim());
            objCustomerMaster.setLinktoAreaMasterId((short) AreaMasterId);
            objCustomerMaster.setlinktoSourceMasterId((short) Globals.sourceMasterId);
            objCustomerMaster.setlinktoBusinessMasterId(Globals.businessMasterId);
            objCustomerMaster.setCustomerType(Globals.customerType);
            objCustomerMaster.setlinktoUserMasterIdCreatedBy(Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity())));
            objCustomerMaster.setIsEnabled(true);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
            status = objCustomerJSONParser.InsertCustomerMaster(objCustomerMaster);
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
                case "-2":
                    Globals.ShowSnackBar(view, getResources().getString(R.string.MsgEmailAlreadyExist), getActivity(), 1000);
                    break;
                default:
                    Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginSucessMsg), getActivity(), 1000);
                    ClearControls();
                    CreateGuestPreference();
                    getActivity().getSupportFragmentManager().popBackStack();
                    if (getTargetFragment() != null) {
                        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.guestFragmentLayout);
                        if (getTargetFragment() == currentFragment) {
                            Globals.ReplaceFragment(new FeedbackFragment(getActivity()), getActivity().getSupportFragmentManager(), null);
                        } else {
                            objLoginResponseListener = (GuestLoginDialogFragment.LoginResponseListener) getTargetFragment();
                            objLoginResponseListener.LoginResponse();
                        }
                    } else {
                        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_home))) {
                            objLoginResponseListener = (GuestLoginDialogFragment.LoginResponseListener) getActivity();
                            objLoginResponseListener.LoginResponse();
                        }
                    }
                    break;
            }
        }
    }

    class SpinnerLoadingTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            objAreaJSONParser = new AreaJSONParser();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            objSharePreferenceManage = new SharePreferenceManage();
            lstSpinnerItem = objAreaJSONParser.SelectAllAreaMaster(String.valueOf(Globals.businessMasterId),objSharePreferenceManage.GetPreference("WaiterPreference", "linktoCityMasterId", getActivity()));
            return lstSpinnerItem;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (lstSpinnerItem != null) {
                SpinnerItem objSpinnerItem = new SpinnerItem();
                objSpinnerItem.setText("--------SELECT AREA--------");
                objSpinnerItem.setValue(0);

                ArrayList<SpinnerItem> alSpinnerItem = new ArrayList<>();
                alSpinnerItem.add(objSpinnerItem);
                lstSpinnerItem.addAll(0, alSpinnerItem);

                adapter = new SpinnerAdapter(getActivity(), lstSpinnerItem, true);
                spnrArea.setAdapter(adapter);
            }
        }
    }
    //endregion
}
