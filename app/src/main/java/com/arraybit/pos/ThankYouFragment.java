package com.arraybit.pos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;


public class ThankYouFragment extends Fragment {


    public ThankYouFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thank_you, container, false);

        TextView txtTitle =(TextView)view.findViewById(R.id.txtTitle);

        Button btnFeedback = (Button) view.findViewById(R.id.btnFeedback);
        Button btnSkip = (Button) view.findViewById(R.id.btnSkip);


        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.ReplaceFragment(new FeedbackFragment(getActivity()), getActivity().getSupportFragmentManager(), null);
            }
        });


        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.userName = null;
                getActivity().getSupportFragmentManager().popBackStack();
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("GuestScreen", true);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in,R.anim.left_out);
                getActivity().finish();
            }
        });

        return view;
    }

}
