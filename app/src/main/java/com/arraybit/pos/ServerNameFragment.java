package com.arraybit.pos;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.parser.AppThemeJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import org.json.JSONObject;

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
//        Globals.SetScaleImageBackground(getContext(), mainLayout, null, null);
//        Bitmap originalBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.splash_screen_background);
//        mainLayout.setBackground(new BitmapDrawable(getActivity().getResources(), originalBitmap));
        //end

        etServerName = (EditText) view.findViewById(R.id.etServerName);
        btnSave = (com.rey.material.widget.Button) view.findViewById(R.id.btnSave);

        Globals.CustomView(btnSave, ContextCompat.getColor(getActivity(), R.color.accent_red), ContextCompat.getColor(getActivity(), android.R.color.transparent));
        btnSave.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));

        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
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

//                    Intent intent1= new Intent(getActivity(),AppThemeIntentService.class);
//                    getActivity().startService(intent1);
//                    try {
//                        SharePreferenceManage sharePreferenceManage = new SharePreferenceManage();
//                        int linktoBusinessMasterId;
//                        if (sharePreferenceManage.GetPreference("WaiterPreference", "linktoBusinessMasterId", getActivity()) != null &&
//                                !sharePreferenceManage.GetPreference("WaiterPreference", "linktoBusinessMasterId", getActivity()).equals("")) {
//                            linktoBusinessMasterId = Integer.parseInt(sharePreferenceManage.GetPreference("WaiterPreference", "linktoBusinessMasterId", getActivity()));
//                            AppThemeJSONParser appThemeJSONParser = new AppThemeJSONParser();
//                            final JSONObject jsonObject = appThemeJSONParser.SelectAppThemeMaster(linktoBusinessMasterId);
//                            if(jsonObject!=null) {
//                                sharePreferenceManage.CreatePreference("GuestAppTheme", "AppThemeJson", jsonObject.toString(), getActivity());
//                                Globals.objAppThemeMaster = appThemeJSONParser.SetClassPropertiesFromJSONObject(jsonObject);
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
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
