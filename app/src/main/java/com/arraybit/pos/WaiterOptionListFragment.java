package com.arraybit.pos;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.rey.material.widget.TextView;

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

        CardView cvOrders = (CardView)view.findViewById(R.id.cvOrders);
        CardView cvDineIn = (CardView)view.findViewById(R.id.cvDineIn);
        CardView cvTakeAway = (CardView)view.findViewById(R.id.cvTakeAway);
        CardView cvBill = (CardView)view.findViewById(R.id.cvBill);

        TextView txtOrders = (TextView)view.findViewById(R.id.txtOrders);
        TextView txtDineIn = (TextView)view.findViewById(R.id.txtDineIn);
        TextView txtTakeAway = (TextView)view.findViewById(R.id.txtTakeAway);
        TextView txtBill = (TextView)view.findViewById(R.id.txtBill);

        Globals.TextViewFontTypeFace(txtOrders,getActivity());
        Globals.TextViewFontTypeFace(txtDineIn,getActivity());
        Globals.TextViewFontTypeFace(txtTakeAway,getActivity());
        Globals.TextViewFontTypeFace(txtBill,getActivity());

        cvOrders.setOnClickListener(this);
        cvDineIn.setOnClickListener(this);
        cvTakeAway.setOnClickListener(this);
        cvBill.setOnClickListener(this);

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
            if (v.getId() == R.id.cvOrders) {
                Globals.ReplaceFragment(new AllOrdersFragment(null), getActivity().getSupportFragmentManager(), null);
            } else if (v.getId() == R.id.cvDineIn) {
                Globals.ReplaceFragment(new AllTablesFragment(getActivity(), false, String.valueOf(Globals.OrderType.DineIn.getValue())), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_all_tables));
            } else if (v.getId() == R.id.cvTakeAway) {
                Globals.ReplaceFragment(new AllTablesFragment(getActivity(), false, String.valueOf(Globals.OrderType.TakeAway.getValue())), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_all_tables));
            } else if (v.getId() == R.id.cvBill) {
                Globals.ReplaceFragment(new TableOrderFragment(),getActivity().getSupportFragmentManager(),null);
            }
        }
    }
}
