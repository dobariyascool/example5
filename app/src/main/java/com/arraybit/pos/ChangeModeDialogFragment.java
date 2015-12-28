package com.arraybit.pos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.arraybit.global.SharePreferenceManage;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

public class ChangeModeDialogFragment extends DialogFragment implements View.OnClickListener {

    EditText etPassword;
    SharePreferenceManage objSharePreferenceManage;
    LinearLayout modeLayout, buttonLayout;

    public ChangeModeDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_mode, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        etPassword = (EditText) view.findViewById(R.id.etPassword);

        modeLayout = (LinearLayout) view.findViewById(R.id.modeLayout);
        buttonLayout = (LinearLayout) view.findViewById(R.id.buttonLayout);

        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);

        Button btnWaiterMode = (Button) view.findViewById(R.id.btnWaiterMode);
        Button btnGuestMode = (Button) view.findViewById(R.id.btnGuestMode);

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnWaiterMode.setOnClickListener(this);
        btnGuestMode.setOnClickListener(this);

        SetVisibility();

        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnWaiterMode) {
            modeLayout.setVisibility(View.GONE);
            etPassword.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.btnGuestMode) {
            getDialog().dismiss();
            if(!(getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1).getName()!=null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1).getName().equals(getResources().getString(R.string.title_fragment_guest_options))))
            {
                AllTablesFragment allTablesFragment = new AllTablesFragment(getActivity(), true, null);
                Bundle bundle = new Bundle();
                bundle.putBoolean("IsVacant", true);
                allTablesFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(android.R.id.content,allTablesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        if (v.getId() == R.id.btnSubmit) {
            CheckPassword();
        } else if (v.getId() == R.id.btnCancel) {
            getDialog().dismiss();
        }
    }

    private void SetVisibility() {
        etPassword.setVisibility(View.GONE);
        buttonLayout.setVisibility(View.GONE);
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
                    if(!(getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1).getName()!=null
                            && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1).getName().equals(getResources().getString(R.string.title_fragment_waiter_options)))) {
                        Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                } else {
                    etPassword.setText("");
                    etPassword.setError("Password is invalid");
                }
            }
        }
    }
}
