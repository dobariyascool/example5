package com.arraybit.pos;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

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

        TextView txtCancel = (TextView)view.findViewById(R.id.txtCancel);
        TextView txtDone = (TextView)view.findViewById(R.id.txtDone);

        txtCancel.setOnClickListener(this);
        txtDone.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.txtCancel){
            dismiss();
            strRemark = etRemark.getText().toString();
            objRemarkResponseListener = (RemarkResponseListener)getTargetFragment();
            objRemarkResponseListener.RemarkResponse();
        }else if(v.getId()==R.id.txtDone){
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
