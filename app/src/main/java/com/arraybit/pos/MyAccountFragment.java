package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.arraybit.adapter.MyAccountAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.parser.CustomerJSONParser;
import com.github.clans.fab.FloatingActionButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class MyAccountFragment extends Fragment implements GuestProfileFragment.UpdateResponseListener, MyAccountAdapter.OptionClickListener {

    public static CustomerMaster objCustomerMaster;
    RecyclerView rvOptions;
    ArrayList<String> alString;
    TextView txtLoginChar, txtFullName, txtEmail;
    FloatingActionButton fabEdit;
    int customerMasterId;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_myaccount));

        setHasOptionsMenu(true);

        RelativeLayout topPanel = (RelativeLayout) view.findViewById(R.id.topPanel);
        if (Build.VERSION.SDK_INT >= 21) {
            topPanel.setElevation(16f);
        }

        txtLoginChar = (TextView) view.findViewById(R.id.txtLoginChar);
        txtFullName = (TextView) view.findViewById(R.id.txtFullName);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);

        fabEdit = (FloatingActionButton) view.findViewById(R.id.fabEdit);

        GetData();
        SetUserName();

        rvOptions = (RecyclerView) view.findViewById(R.id.rvOptions);
        MyAccountAdapter accountAdapter = new MyAccountAdapter(getActivity(), alString, this);
        rvOptions.setAdapter(accountAdapter);
        rvOptions.setLayoutManager(new LinearLayoutManager(getActivity()));

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTargetFragment() != null) {
                    GuestProfileFragment guestProfileFragment = new GuestProfileFragment();
                    guestProfileFragment.setTargetFragment(MyAccountFragment.this, 0);
                    ReplaceFragment(guestProfileFragment, getActivity().getResources().getString(R.string.title_fragment_myprofile));
                } else {
                    GuestProfileFragment guestProfileFragment = new GuestProfileFragment();
                    guestProfileFragment.setTargetFragment(MyAccountFragment.this, 0);
                    ReplaceFragment(guestProfileFragment, getActivity().getResources().getString(R.string.title_fragment_myprofile));
                }
            }
        });

        if (Service.CheckNet(getActivity())) {
            new UserInformationLoading().execute();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getActivity().getResources().getString(R.string.title_fragment_myaccount))) {
                getActivity().getSupportFragmentManager().popBackStack();
                objCustomerMaster = null;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.login).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.shortList).setVisible(false);
    }

    //region Private Methods
    private void GetData() {
        alString = new ArrayList<>();
        for (int i = 0; i < getActivity().getResources().getStringArray(R.array.Option).length; i++) {
            alString.add(getActivity().getResources().getStringArray(R.array.Option)[i]);
        }
    }

    @SuppressLint("RtlHardcoded")
    private void ReplaceFragment(Fragment fragment, String fragmentName) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(350);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(R.id.myAccountFragment, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    private void SetUserName() {
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
    }

    @Override
    public void UpdateResponse() {
        SetUserName();
        if (Service.CheckNet(getActivity())) {
            new UserInformationLoading().execute();
        }
    }

    @Override
    public void OptionClick(int id) {
        if (getTargetFragment() != null) {
            if (id == 0) {
                ReplaceFragment(new NotificationSettingsFragment(), getActivity().getResources().getString(R.string.title_fragment_notification_settings));
            } else if (id == 1) {
                ReplaceFragment(new ChangePasswordFragment(), getActivity().getResources().getString(R.string.title_fragment_change_password));
            } else if (id == 2) {
                Globals.ClearPreference(getActivity().getApplicationContext());
                getActivity().getSupportFragmentManager().popBackStack();
            }
        } else {
            if (id == 0) {
                ReplaceFragment(new NotificationSettingsFragment(), getActivity().getResources().getString(R.string.title_fragment_notification_settings));

            } else if (id == 1) {
                ReplaceFragment(new ChangePasswordFragment(), getActivity().getResources().getString(R.string.title_fragment_change_password));

            } else if (id == 2) {
                Globals.ClearPreference(getActivity().getApplicationContext());
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }
    //endregion

    //region LoadingTask
    class UserInformationLoading extends AsyncTask {

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

    }
    //endregion
}
