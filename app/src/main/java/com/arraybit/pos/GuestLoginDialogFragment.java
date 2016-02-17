package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

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
    LoginResponseListener objLoginResponseListener;

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

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {

                CompoundButton cbSignUp = (CompoundButton) getDialog().findViewById(R.id.cbSignUp);
                CompoundButton cbSkip = (CompoundButton) getDialog().findViewById(R.id.cbSkip);
                Button positive = (alertDialog).getButton(DialogInterface.BUTTON_POSITIVE);

                if (getTargetFragment() != null) {
                    Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.guestFragmentLayout);
                    if (getTargetFragment() == currentFragment) {
                        cbSkip.setVisibility(View.VISIBLE);
                    }else{
                        cbSkip.setVisibility(View.GONE);
                    }
                }
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etUserName = (EditText) getDialog().findViewById(R.id.etUserName);
                        etPassword = (EditText) getDialog().findViewById(R.id.etPassword);
                        view = v;
                        if (!ValidateControls()) {
                            Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
                            return;
                        }
                        if (Service.CheckNet(getActivity())) {
                            new SignInLodingTask().execute();

                        } else {
                            Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                        }
                    }
                });

                Button negative = (alertDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getTargetFragment() != null) {
                            Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.guestFragmentLayout);
                            if (getTargetFragment() == currentFragment) {
                                dismiss();
                            } else {
                                dismiss();
                                objLoginResponseListener = (LoginResponseListener) getTargetFragment();
                                objLoginResponseListener.LoginResponse();
                            }
                        } else {
                            dismiss();
                        }
                    }
                });

                cbSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getTargetFragment() != null) {
                            FeedbackFragment currentFragment = (FeedbackFragment) getActivity().getSupportFragmentManager().findFragmentByTag(getActivity().getResources().getString(R.string.title_fragment_feedback));
                            if(getTargetFragment() == currentFragment){
                                currentFragment.RemoveFragment();
                                SignUpFragment signUpFragment = new SignUpFragment();
                                signUpFragment.setTargetFragment(getTargetFragment(), 0);
                                Globals.ReplaceFragment(signUpFragment, getActivity().getSupportFragmentManager(), null);
                            }
                            else if(getTargetFragment() == getActivity().getSupportFragmentManager().findFragmentByTag(getActivity().getResources().getString(R.string.title_fragment_offer_detail))){
                                ReplaceFragment(new SignUpFragment(), null);
                            }
                            else if(MenuActivity.parentActivity && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1).getName()==null) {
                                //first remove current tab and then open signup fragment
                                objLoginResponseListener = (LoginResponseListener)getTargetFragment();
                                objLoginResponseListener.LoginResponse();
                            }
                            else{
                                SignUpFragment signUpFragment = new SignUpFragment();
                                signUpFragment.setTargetFragment(getTargetFragment(), 0);
                                Globals.ReplaceFragment(signUpFragment, getActivity().getSupportFragmentManager(), null);

                            }
                        } else {
                            Globals.ReplaceFragment(new SignUpFragment(), getActivity().getSupportFragmentManager(), null);
                        }
                        dismiss();
                    }
                });

                cbSkip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dismiss();
                        Globals.ReplaceFragment(new FeedbackFragment(getActivity()), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_feedback));
                    }
                });
            }
        });

        return alertDialog;
    }

    //region Private Methods and Interface
    @SuppressLint("RtlHardcoded")
    private void ReplaceFragment(Fragment fragment,String fragmentName){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(500);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(R.id.offerDetailFragment, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    private boolean ValidateControls() {
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

    private void CreateGuestPreference() {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()) == null) {
            objSharePreferenceManage.CreatePreference("RegistrationPreference", "UserName", etUserName.getText().toString(), getActivity());
        }
        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "RegisteredUserMasterId", getActivity()) == null) {
            objSharePreferenceManage.CreatePreference("RegistrationPreference", "RegisteredUserMasterId", String.valueOf(objRegisteredUserMaster.getRegisteredUserMasterId()), getActivity());
        }
        if(!objRegisteredUserMaster.getFirstName().equals("")) {
            if (objSharePreferenceManage.GetPreference("RegistrationPreference", "FullName", getActivity()) == null) {
                objSharePreferenceManage.CreatePreference("RegistrationPreference", "FullName", String.valueOf(objRegisteredUserMaster.getFirstName()) + " " + String.valueOf(objRegisteredUserMaster.getLastName()), getActivity());
            }
            if (objSharePreferenceManage.GetPreference("RegistrationPreference", "FirstName", getActivity()) == null) {
                objSharePreferenceManage.CreatePreference("RegistrationPreference", "FirstName", String.valueOf(objRegisteredUserMaster.getFirstName()), getActivity());
            }
        }
    }

    void ClearControls() {
        etUserName.setText("");
        etPassword.setText("");
    }

    public interface LoginResponseListener {
        void LoginResponse();
    }
    //endregion

    //region LoadingTask
    class SignInLodingTask extends AsyncTask {

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
            objRegisteredUserMaster = objRegisteredUserJSONParser.SelectRegisteredUserMasterUserName(strUserName, strPassword,0);
            return objRegisteredUserMaster;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (result == null) {
                Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginFailedMsg), getActivity(), 1000);
            } else {
                if (getTargetFragment() != null) {
                    Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.guestFragmentLayout);
                    if (getTargetFragment() == currentFragment) {
                        CreateGuestPreference();
                        ClearControls();
                        Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginSucessMsg), getActivity(), 1000);
                        objSharePreferenceManage = new SharePreferenceManage();
                        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()) != null) {
                            Globals.userName = objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity());
                        }
                        dismiss();
                        Globals.ReplaceFragment(new FeedbackFragment(getActivity()), getFragmentManager(),getActivity().getResources().getString(R.string.title_fragment_feedback));
                    }
                    else if(getTargetFragment() == getActivity().getSupportFragmentManager().findFragmentByTag(getActivity().getResources().getString(R.string.title_fragment_offer_detail))){
                        CreateGuestPreference();
                        ClearControls();
                        Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginSucessMsg), getActivity(), 1000);
                        objSharePreferenceManage = new SharePreferenceManage();
                        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()) != null) {
                            Globals.userName = objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity());
                        }
                        dismiss();
                    }
                    else if(MenuActivity.parentActivity && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1).getName()==null){
                        CreateGuestPreference();
                        ClearControls();
                        Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginSucessMsg), getActivity(), 1000);
                        objSharePreferenceManage = new SharePreferenceManage();
                        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()) != null) {
                            Globals.userName = objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity());
                        }
                        dismiss();
                    }
                    else {
                        CreateGuestPreference();
                        ClearControls();
                        Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginSucessMsg), getActivity(), 1000);
                        objSharePreferenceManage = new SharePreferenceManage();
                        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()) != null) {
                            Globals.userName = objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity());
                        }
                        dismiss();
                        objLoginResponseListener = (LoginResponseListener) getTargetFragment();
                        objLoginResponseListener.LoginResponse();
                    }

                } else {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                            && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                            .equals(getActivity().getString(R.string.title_fragment_guest_options))) {
                        CreateGuestPreference();
                        ClearControls();
                        Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginSucessMsg), getActivity(), 1000);
                        objSharePreferenceManage = new SharePreferenceManage();
                        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()) != null) {
                            Globals.userName = objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity());
                        }
                        dismiss();
                        objLoginResponseListener = (LoginResponseListener) getActivity();
                        objLoginResponseListener.LoginResponse();
                    } else {
                        CreateGuestPreference();
                        ClearControls();
                        Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginSucessMsg), getActivity(), 1000);
                        objSharePreferenceManage = new SharePreferenceManage();
                        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()) != null) {
                            Globals.userName = objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity());
                        }
                        dismiss();
                    }
                }
            }
        }
    }
    //endregion
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

