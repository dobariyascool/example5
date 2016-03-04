package com.arraybit.pos;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

public class RemarkDialogFragment extends DialogFragment implements View.OnClickListener{


    public static String strRemark;
    EditText etRemark;
    RemarkResponseListener objRemarkResponseListener;


    public RemarkDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_remark_dialog, container, false);
        getDialog().getWindow().requestFeature(STYLE_NO_TITLE);

        etRemark =(EditText)view.findViewById(R.id.etRemark);
        if(strRemark!=null){
            etRemark.setText(strRemark);
        }else{
            etRemark.setText("");
        }

        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
        Button btnDone = (Button)view.findViewById(R.id.btnDone);

        btnCancel.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnCancel){
            dismiss();
            strRemark = etRemark.getText().toString();
            objRemarkResponseListener = (RemarkResponseListener)getTargetFragment();
            objRemarkResponseListener.RemarkResponse();
        }else if(v.getId()==R.id.btnDone){
            strRemark = etRemark.getText().toString();
            objRemarkResponseListener = (RemarkResponseListener)getTargetFragment();
            objRemarkResponseListener.RemarkResponse();
            dismiss();
        }
    }

    public interface RemarkResponseListener{
        void RemarkResponse();
    }
}
