package com.arraybit.pos;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.rey.material.widget.Switch;
import com.rey.material.widget.TextView;

@SuppressWarnings("ConstantConditions")
public class NotificationSettingsFragment extends Fragment {


    Switch sPushNotificationOnOff;
    TextView txtOnTime, txtOffTime;

    SharePreferenceManage objSharePreferenceManage;
    String strOnOff;


    public NotificationSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_settings, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_notification_settings));

        setHasOptionsMenu(true);

        txtOnTime = (TextView) view.findViewById(R.id.txtOnTime);
        txtOffTime = (TextView) view.findViewById(R.id.txtOffTime);

        sPushNotificationOnOff = (Switch) view.findViewById(R.id.sPushNotificationOnOff);
        objSharePreferenceManage = new SharePreferenceManage();

        if (objSharePreferenceManage.GetPreference("NotificationOnTimePreference", "OnTime", getActivity()) == null) {
            objSharePreferenceManage.CreatePreference("NotificationOnTimePreference", "OnTime", "12:00 AM", getActivity());
            txtOnTime.setText(objSharePreferenceManage.GetPreference("NotificationOnTimePreference", "OnTime", getActivity()));
        } else {
            txtOnTime.setText(objSharePreferenceManage.GetPreference("NotificationOnTimePreference", "OnTime", getActivity()));
        }

        if (objSharePreferenceManage.GetPreference("NotificationOffTimePreference", "OffTime", getActivity()) == null) {
            objSharePreferenceManage.CreatePreference("NotificationOffTimePreference", "OffTime", "12:00 PM", getActivity());
            txtOffTime.setText(objSharePreferenceManage.GetPreference("NotificationOffTimePreference", "OffTime", getActivity()));
        } else {
            txtOffTime.setText(objSharePreferenceManage.GetPreference("NotificationOffTimePreference", "OffTime", getActivity()));
        }

        txtOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.ShowTimePickerDialog(txtOnTime, getActivity());
            }

        });

        txtOffTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.ShowTimePickerDialog(txtOffTime, getActivity());
            }
        });

        if (objSharePreferenceManage.GetPreference("NotificationPreference", "Push", getActivity()) == null) {
            objSharePreferenceManage.CreatePreference("NotificationPreference", "Push", "true", getActivity());

            sPushNotificationOnOff.setChecked(true);
        } else {

            strOnOff = objSharePreferenceManage.GetPreference("NotificationPreference", "Push", getActivity());
            if (strOnOff.equals("false")) {
                sPushNotificationOnOff.setChecked(false);

            } else {
                sPushNotificationOnOff.setChecked(true);
            }

        }

        sPushNotificationOnOff.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch aSwitch, boolean b) {
                if (sPushNotificationOnOff.isChecked()) {
                    objSharePreferenceManage.RemovePreference("NotificationPreference", "Push", getActivity());
                    objSharePreferenceManage.CreatePreference("NotificationPreference", "Push", "true", getActivity());
                } else {

                    ConfirmNotificationSettings();
                }
            }
        });
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.login).setVisible(false);
        menu.findItem(R.id.registration).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            CreateNotificationPreference();
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    public void CreateNotificationPreference() {
        objSharePreferenceManage.CreatePreference("NotificationOnTimePreference", "OnTime", txtOnTime.getText().toString(), getActivity());
        objSharePreferenceManage.CreatePreference("NotificationOffTimePreference", "OffTime", txtOffTime.getText().toString(), getActivity());
    }

    private void ConfirmNotificationSettings() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setMessage(getActivity().getResources().getString(R.string.MsgConfirmSettings))
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                objSharePreferenceManage.RemovePreference("NotificationPreference", "Push", getActivity());
                                objSharePreferenceManage.CreatePreference("NotificationPreference", "Push", "false", getActivity());
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                sPushNotificationOnOff.setChecked(true);
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

}


