package com.arraybit.pos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Button;


public class ThankYouFragment extends Fragment {


    public ThankYouFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_thank_you, container, false);

        Button btnFeedback=(Button)view.findViewById(R.id.btnFeedback);
        Button btnSkip=(Button)view.findViewById(R.id.btnSkip);

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager=getFragmentManager();
                /*FeedbackFragment feedbackDialogFragment=new FeedbackFragment();
                feedbackDialogFragment.show(fragmentManager,"");*/

            }
        });


        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStack();
                Intent intent=new Intent(getActivity(),GuestHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
    
}
