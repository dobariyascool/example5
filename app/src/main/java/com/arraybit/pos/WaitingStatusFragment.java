package com.arraybit.pos;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.rey.material.widget.Button;


public class WaitingStatusFragment extends DialogFragment implements View.OnClickListener {

    Button btnServe, btnNot, btnCancle, btn;

    public WaitingStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_status, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        btnServe = (Button) view.findViewById(R.id.btnServe);
        btnNot = (Button) view.findViewById(R.id.btnNot);
        btnCancle = (Button) view.findViewById(R.id.btnCancle);


        btnServe.setOnClickListener(this);
        btnNot.setOnClickListener(this);
        btnCancle.setOnClickListener(this);


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
