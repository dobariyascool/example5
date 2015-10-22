package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.rey.material.widget.Button;

@SuppressLint("ValidFragment")
public class WaitingStatusFragment extends DialogFragment implements View.OnClickListener {

    Button btnServe, btnNot, btnCancel;
    String waitingStatus;


    public WaitingStatusFragment(String waitingStatus) {
        this.waitingStatus = waitingStatus;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_status, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        btnServe = (Button) view.findViewById(R.id.btnServe);
        btnNot = (Button) view.findViewById(R.id.btnNot);
        btnCancel = (Button) view.findViewById(R.id.btnCancle);

        btnServe.setOnClickListener(this);
        btnNot.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if(waitingStatus.equals("Waiting")) {
            btnServe.setVisibility(View.VISIBLE);
            btnNot.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        }
        else if(waitingStatus.equals("Served")){
            btnServe.setVisibility(View.GONE);
            btnNot.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        }
        else if(waitingStatus.equals("Cancel")){
            btnServe.setVisibility(View.VISIBLE);
            btnNot.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.GONE);
        }
        else{
            btnServe.setVisibility(View.VISIBLE);
            btnNot.setVisibility(View.GONE);
            btnCancel.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnServe) {
            dismiss();
        } else if (v.getId() == R.id.btnNot) {
            dismiss();
        } else if (v.getId() == R.id.btnCancle) {
            dismiss();
        }

    }

    //    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(R.layout.fragment_waiting_status);

//        builder.setPositiveButton("done", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dismiss();
//            }
//        });
//
//        builder.setNegativeButton(getResources().getString(R.string.fdCancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dismiss();
//
//            }
//        });

//        return builder.create();
//    }

}
