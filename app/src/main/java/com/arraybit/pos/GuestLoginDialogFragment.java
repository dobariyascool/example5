package com.arraybit.pos;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

public class GuestLoginDialogFragment extends DialogFragment {

    View view;

    public GuestLoginDialogFragment() {
        // Required empty public constructor
    }


//    @Override
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.fragment_guest_login_dialog);

        builder.setPositiveButton(getResources().getString(R.string.ldLogin), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText etUserName=(EditText)getDialog().findViewById(R.id.etUserName);

                if(Globals.activityName.equals(getActivity().getResources().getString(R.string.title_activity_home)))
                {
                    Intent intent = new Intent(getActivity(),GuestHomeActivity.class);
                    intent.putExtra("username",etUserName.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getActivity(),GuestOrderActivity.class);
                    intent.putExtra("username",etUserName.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                dismiss();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.ldCancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }
}
