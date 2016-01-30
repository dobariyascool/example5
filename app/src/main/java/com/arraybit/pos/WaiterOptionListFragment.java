package com.arraybit.pos;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;

public class WaiterOptionListFragment extends Fragment implements View.OnClickListener {

    LinearLayout waiterOptionLayout;

    public WaiterOptionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiter_option_list, container, false);

        waiterOptionLayout = (LinearLayout) view.findViewById(R.id.waiterOptionLayout);
        Globals.SetHomePageBackground(getActivity(), waiterOptionLayout, null, null);

        LinearLayout layoutOrders = (LinearLayout) view.findViewById(R.id.layoutOrders);
        LinearLayout layoutDineIn = (LinearLayout) view.findViewById(R.id.layoutDineIn);
        LinearLayout layoutTakeAway = (LinearLayout) view.findViewById(R.id.layoutTakeAway);
        LinearLayout layoutOffers = (LinearLayout) view.findViewById(R.id.layoutOffers);

        layoutOrders.setOnClickListener(this);
        layoutDineIn.setOnClickListener(this);
        layoutTakeAway.setOnClickListener(this);
        layoutOffers.setOnClickListener(this);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Globals.SetHomePageBackground(getActivity(), waiterOptionLayout, null, null);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        if (getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_waiter_options))) {
            if (v.getId() == R.id.layoutOrders) {
                Globals.ReplaceFragment(new AllOrdersFragment(null), getActivity().getSupportFragmentManager(), null);
            } else if (v.getId() == R.id.layoutDineIn) {
                Globals.ReplaceFragment(new AllTablesFragment(getActivity(), false, String.valueOf(Globals.OrderType.DineIn.getValue())), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_all_tables));
            } else if (v.getId() == R.id.layoutTakeAway) {
                Globals.ReplaceFragment(new AllTablesFragment(getActivity(), false, String.valueOf(Globals.OrderType.TakeAway.getValue())), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_all_tables));
            } else if (v.getId() == R.id.layoutOffers) {
                Globals.ReplaceFragment(new OfferFragment(getActivity()), getActivity().getSupportFragmentManager(), null);
            }
        }
    }
}
