package com.arraybit.pos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

public class ChangeModeDialogFragment extends DialogFragment implements View.OnClickListener {

    EditText etPassword;
    SharePreferenceManage objSharePreferenceManage;
    LinearLayout buttonLayout, modeLayout;
    Button btnSubmit, btnCancel, btnWaiterMode, btnGuestMode;

    public ChangeModeDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_mode, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        etPassword = (EditText) view.findViewById(R.id.etPassword);

        buttonLayout = (LinearLayout) view.findViewById(R.id.buttonLayout);
        modeLayout = (LinearLayout) view.findViewById(R.id.modeLayout);

        btnWaiterMode = (Button) view.findViewById(R.id.btnWaiterMode);
        btnGuestMode = (Button) view.findViewById(R.id.btnGuestMode);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        Globals.ButtonFontTypeFace(btnSubmit,getActivity());
        Globals.ButtonFontTypeFace(btnCancel,getActivity());
        Globals.ButtonFontTypeFace(btnWaiterMode,getActivity());
        Globals.ButtonFontTypeFace(btnGuestMode,getActivity());
        Globals.EditTextFontTypeFace(etPassword, getActivity());

        SetVisibility();

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            CheckPassword();
        } else if (v.getId() == R.id.btnCancel) {
            getDialog().dismiss();
        } else if (v.getId() == R.id.btnWaiterMode) {
            getDialog().dismiss();
            Intent intent = new Intent(getActivity(), WaitingActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            getActivity().finish();
        } else if (v.getId() == R.id.btnGuestMode) {
            getDialog().dismiss();
            AllTablesFragment allTablesFragment = new AllTablesFragment(getActivity(), true, null);
            Bundle bundle = new Bundle();
            bundle.putBoolean("IsVacant", true);
            allTablesFragment.setArguments(bundle);

            Globals.ReplaceFragment(allTablesFragment, getActivity().getSupportFragmentManager(), getResources().getString(R.string.title_fragment_all_tables));
        }
    }

    //region Private Methods and Interface
    private void SetVisibility() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                .equals(getActivity().getResources().getString(R.string.title_fragment_waiter_options))) {
            modeLayout.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);

            btnWaiterMode.setOnClickListener(this);
            btnGuestMode.setOnClickListener(this);
        } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                .equals(getActivity().getResources().getString(R.string.title_fragment_guest_options))) {
            modeLayout.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);

            btnSubmit.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
        }
    }

    private void CheckPassword() {
        if (etPassword.getText().toString().equals("")) {
            etPassword.setError("Enter " + getActivity().getResources().getString(R.string.siPassword));
        } else if (!etPassword.getText().toString().equals("")) {
            etPassword.clearError();
            objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserSecurityCode", getActivity()) != null) {
                if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserSecurityCode", getActivity()).equals(etPassword.getText().toString())) {

                    getDialog().dismiss();
                    if (!(getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                            && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_waiter_options)))) {

                        objSharePreferenceManage.RemovePreference("GuestModePreference", "GuestMode", getActivity());
                        objSharePreferenceManage.ClearPreference("GuestModePreference", getActivity());

                        Globals.ClearPreference(getActivity().getApplicationContext());

                        Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }

                } else {
                    etPassword.setText("");
                    etPassword.setError("Password is invalid");
                }
            }
        }
    }
    //endregion
}
