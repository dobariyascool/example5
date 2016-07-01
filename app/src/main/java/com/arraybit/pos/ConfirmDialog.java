package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.rey.material.widget.TextView;


public class ConfirmDialog extends DialogFragment implements View.OnClickListener {

    ConfirmationResponseListener objConfirmationResponseListener;
    boolean isDeleteConfirm;
    String message;
    boolean btnTextChange;

    public ConfirmDialog() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public ConfirmDialog(String message,boolean btnTextChange) {
        // Required empty public constructor
        this.message = message;
        this.btnTextChange = btnTextChange;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        TextView txtHeader = (TextView) view.findViewById(R.id.txtHeader);
        TextView txtOrderMessage = (TextView) view.findViewById(R.id.txtOrderMessage);
        TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);

        TextView txtConfirm = (TextView)view.findViewById(R.id.txtConfirm);
        TextView txtCancel = (TextView)view.findViewById(R.id.txtCancel);

        if(btnTextChange){
            txtConfirm.setText(getActivity().getResources().getString(R.string.cdfYes));
            txtCancel.setText(getActivity().getResources().getString(R.string.cdfNo));
        }else{
            txtConfirm.setText(getActivity().getResources().getString(R.string.cdfConfirm));
            txtCancel.setText(getActivity().getResources().getString(R.string.cdfCancel));
        }

        txtHeader.setText(message);

        txtCancel.setOnClickListener(this);
        txtConfirm.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtConfirm) {
            if(getTargetFragment()!=null){
                objConfirmationResponseListener = (ConfirmationResponseListener) getTargetFragment();
                objConfirmationResponseListener.ConfirmResponse();
            }else {
                objConfirmationResponseListener = (ConfirmationResponseListener) getActivity();
                objConfirmationResponseListener.ConfirmResponse();
            }
            dismiss();
        } else if (v.getId() == R.id.txtCancel) {
            dismiss();
        }
    }

    public interface ConfirmationResponseListener {
        void ConfirmResponse();
    }
}
