package com.arraybit.pos;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

public class ChangeModeDialogFragment extends DialogFragment implements View.OnClickListener {

    EditText etPassword;
    SharePreferenceManage objSharePreferenceManage;
    LinearLayout buttonLayout, modeLayout;
    TextView txtSubmit, txtCancel;
    Button btnWaiterMode, btnGuestMode, btnMenuMode;

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
        btnMenuMode = (Button) view.findViewById(R.id.btnMenuMode);

        txtSubmit = (TextView) view.findViewById(R.id.txtSubmit);
        txtCancel = (TextView) view.findViewById(R.id.txtCancel);
        Drawable[] drawable = btnWaiterMode.getCompoundDrawablesRelative();
        drawable[1].mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.waitingTitleIconColor), PorterDuff.Mode.SRC_IN);
        SetVisibility();

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtSubmit) {
            CheckPassword();
        } else if (v.getId() == R.id.txtCancel) {
            getDialog().dismiss();
        } else if (v.getId() == R.id.btnWaiterMode) {
            WaiterHomeActivity.isWaiterMode = false;
            GuestHomeActivity.isGuestMode = false;
            getDialog().dismiss();
            Globals.isWishListShow = 0;
            Globals.DisableBroadCastReceiver(getActivity());
            Intent intent = new Intent(getActivity(), WaitingActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            getActivity().finish();
        } else if (v.getId() == R.id.btnGuestMode) {

            getDialog().dismiss();
            Bundle bundle = new Bundle();
            bundle.putBoolean("IsMenuMode", false);
            BottomDialogFragment bottomDialogFragment = new BottomDialogFragment();
            bottomDialogFragment.setArguments(bundle);
            bottomDialogFragment.show(getActivity().getSupportFragmentManager(), getResources().getString(R.string.title_fragment_bottom_dialog));
        } else if (v.getId() == R.id.btnMenuMode) {
            getDialog().dismiss();
            Bundle bundle = new Bundle();
            bundle.putBoolean("IsMenuMode", true);
            BottomDialogFragment bottomDialogFragment = new BottomDialogFragment();
            bottomDialogFragment.setArguments(bundle);
            bottomDialogFragment.show(getActivity().getSupportFragmentManager(), getResources().getString(R.string.title_fragment_bottom_dialog));
        }
    }

    //region Private Methods and Interface
    private void SetVisibility() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                .equals(getActivity().getResources().getString(R.string.title_fragment_waiter_options))) {
            modeLayout.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.GONE);
            txtSubmit.setVisibility(View.GONE);
            txtCancel.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);

            btnWaiterMode.setOnClickListener(this);
            btnGuestMode.setOnClickListener(this);
            btnMenuMode.setOnClickListener(this);
        } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                .equals(getActivity().getResources().getString(R.string.title_fragment_guest_options))) {
            Globals.isWishListShow = 1;
            modeLayout.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            txtSubmit.setVisibility(View.VISIBLE);
            txtCancel.setVisibility(View.VISIBLE);

            txtSubmit.setOnClickListener(this);
            txtCancel.setOnClickListener(this);
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
                        objSharePreferenceManage.RemovePreference("WishListPreference", "WishList", getActivity());
                        objSharePreferenceManage.ClearPreference("WishListPreference", getActivity());
                        objSharePreferenceManage.RemovePreference("CartItemListPreference", "CartItemList", getActivity());
                        objSharePreferenceManage.ClearPreference("CartItemListPreference", getActivity());

                        Globals.ClearPreference(getActivity().getApplicationContext());
                        MenuActivity.parentActivity = false;
                        Globals.ClearData();
                        Globals.isWishListShow = 0;
                        Globals.EnableBroadCastReceiver(getActivity());
                        Globals.CallNotificationReceiver(getActivity());
                        GuestHomeActivity.isMenuMode = false;
                        GuestHomeActivity.isGuestMode = false;

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
