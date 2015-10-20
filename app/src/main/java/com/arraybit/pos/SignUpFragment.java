package com.arraybit.pos;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.rey.material.widget.Button;


public class SignUpFragment extends Fragment implements View.OnClickListener {

    public SignUpFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sign_up, container, false);

        //app_bar
        Toolbar app_bar=(Toolbar)view.findViewById(R.id.app_bar);
        if(app_bar!=null)
        {
            ((AppCompatActivity)getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_signup));
        //app_bar.setLogo(R.mipmap.home);
        //end

        //button
        Button btnSignUp=(Button)view.findViewById(R.id.btnSignUp);
        //end

        //compound button
        CompoundButton cbSignIn=(CompoundButton)view.findViewById(R.id.cbSignIn);
        //end

        //event
        cbSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        //end

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btnSignUp)
        {
            Intent intent=new Intent(getActivity(),SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(v.getId()==R.id.cbSignIn)
        {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
