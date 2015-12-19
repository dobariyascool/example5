package com.arraybit.pos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;

public class GuestOptionListFragment extends Fragment implements View.OnClickListener{


    public GuestOptionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_option_list, container, false);

        CardView cvOrder = (CardView) view.findViewById(R.id.cvOrder);
        CardView cvFeedback = (CardView) view.findViewById(R.id.cvFeedback);
        CardView cvMenu = (CardView) view.findViewById(R.id.cvMenu);
        CardView cvOffers = (CardView) view.findViewById(R.id.cvOffers);

        cvOrder.setOnClickListener(this);
        cvFeedback.setOnClickListener(this);
        cvMenu.setOnClickListener(this);
        cvOffers.setOnClickListener(this);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cvOrder) {
            Globals.InitializeFragment(new AllOrdersFragment(null), getFragmentManager());
        } else if (v.getId() == R.id.cvFeedback) {
            Globals.InitializeFragment(new FeedbackFragment(getActivity()),getFragmentManager());
        } else if (v.getId() == R.id.cvMenu) {
            Intent intent = new Intent(getActivity(),MenuActivity.class);
            intent.putExtra("TableMasterId", GuestHomeActivity.tableMasterId);
            startActivity(intent);
        } else if (v.getId() == R.id.cvOffers) {
            Globals.InitializeFragment(new OfferFragment(getActivity()), getFragmentManager());
        }

    }
}
