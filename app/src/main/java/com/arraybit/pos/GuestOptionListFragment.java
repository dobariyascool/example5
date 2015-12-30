package com.arraybit.pos;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;

public class GuestOptionListFragment extends Fragment implements View.OnClickListener{


    LinearLayout guestOptionLayout;

    public GuestOptionListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_option_list, container, false);

        guestOptionLayout = (LinearLayout)view.findViewById(R.id.guestOptionLayout);
        Globals.SetHomePageBackground(getActivity(),guestOptionLayout,null,null);

        LinearLayout layoutMenu = (LinearLayout) view.findViewById(R.id.layoutMenu);
        LinearLayout layoutOrders = (LinearLayout) view.findViewById(R.id.layoutOrders);
        LinearLayout layoutOffers = (LinearLayout) view.findViewById(R.id.layoutOffers);
        LinearLayout layoutFeedback = (LinearLayout) view.findViewById(R.id.layoutFeedback);

        layoutMenu.setOnClickListener(this);
        layoutOrders.setOnClickListener(this);
        layoutOffers.setOnClickListener(this);
        layoutFeedback.setOnClickListener(this);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetHomePageBackground(getActivity(), guestOptionLayout, null, null);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layoutOrders) {
//            Globals.InitializeFragment(new AllOrdersFragment(null), getFragmentManager());
            Bundle bundle = new Bundle();
            bundle.putParcelable("TableMaster",GuestHomeActivity.objTableMaster);
            OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment(null);
            orderSummaryFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(android.R.id.content, orderSummaryFragment, getActivity().getResources().getString(R.string.title_fragment_order_summary));
            fragmentTransaction.addToBackStack(getActivity().getResources().getString(R.string.title_fragment_order_summary));
            fragmentTransaction.commit();
        } else if (v.getId() == R.id.layoutFeedback) {
            Globals.InitializeFragment(new FeedbackFragment(getActivity()),getFragmentManager());
        } else if (v.getId() == R.id.layoutMenu) {
            Intent intent = new Intent(getActivity(),MenuActivity.class);
            intent.putExtra("ParentActivity",true);
            intent.putExtra("TableMaster",GuestHomeActivity.objTableMaster);
            startActivity(intent);
        } else if (v.getId() == R.id.layoutOffers) {
            Globals.InitializeFragment(new OfferFragment(getActivity()), getFragmentManager());
        }
    }
}
