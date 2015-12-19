package com.arraybit.pos;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.arraybit.global.SharePreferenceManage;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

public class ChangeModeDialogFragment extends DialogFragment implements View.OnClickListener{

    EditText etPassword;
    SharePreferenceManage objSharePreferenceManage;

    public ChangeModeDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_mode, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        etPassword =(EditText)view.findViewById(R.id.etPassword);

        Button btnSubmit = (Button)view.findViewById(R.id.btnSubmit);
        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSubmit){
            CheckPassword();
        }
        else if(v.getId()==R.id.btnCancel){
            getDialog().dismiss();
        }
    }

    private void CheckPassword(){
        if(etPassword.getText().toString().equals("")){
            etPassword.setError("Enter "+getActivity().getResources().getString(R.string.siPassword));
        }
        else if(!etPassword.getText().toString().equals("")){
            etPassword.clearError();
            objSharePreferenceManage = new SharePreferenceManage();
            if(objSharePreferenceManage.GetPreference("WaiterPreference","UserSecurityCode",getActivity())!=null){
                if(objSharePreferenceManage.GetPreference("WaiterPreference","UserSecurityCode",getActivity()).equals(etPassword.getText().toString())){

                    getDialog().dismiss();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(android.R.id.content,new AllTablesFragment(getActivity(),true));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else
                {
                    etPassword.setText("");
                    etPassword.setError("Password is invalid");
                }
            }
        }
    }
}
