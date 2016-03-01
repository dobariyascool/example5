package com.arraybit.pos;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

@SuppressWarnings("unchecked")
public class ServerNameFragment extends Fragment {
    EditText etServerName;
    Button btnSave;
    LinearLayout mainLayout;


    public ServerNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_server_name, container, false);

        //linearlayout
        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
        Globals.SetScaleImageBackground(getContext(), mainLayout, null, null);
        //end

        etServerName = (EditText) view.findViewById(R.id.etServerName);
        btnSave = (com.rey.material.widget.Button) view.findViewById(R.id.btnSave);


        return view;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getContext(), mainLayout, null, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etServerName.getText().toString().equals("")) {
                    etServerName.setError("Enter " + getResources().getString(R.string.sfName));
                } else {
                    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();

                    objSharePreferenceManage.CreatePreference("ServerPreference", "ServerName", etServerName.getText().toString(), getActivity());

                    Globals.serverName = etServerName.getText().toString();
                    Globals.ChangeUrl();

                    if (Build.VERSION.SDK_INT >= 21) {
                        ActivityOptions options =
                                ActivityOptions.makeSceneTransitionAnimation(getActivity());
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getActivity().startActivity(intent, options.toBundle());
                    } else {
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                }
            }
        });
    }

}
