package com.arraybit.pos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;

public class WaiterOptionListFragment extends Fragment implements View.OnClickListener {


    public WaiterOptionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiter_option_list, container, false);

        CardView cvOrders = (CardView) view.findViewById(R.id.cvOrders);
        CardView cvTables = (CardView) view.findViewById(R.id.cvTables);
        CardView cvMenu = (CardView) view.findViewById(R.id.cvMenu);
        CardView cvOffers = (CardView) view.findViewById(R.id.cvOffers);

        cvOrders.setOnClickListener(this);
        cvTables.setOnClickListener(this);
        cvMenu.setOnClickListener(this);
        cvOffers.setOnClickListener(this);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cvOrders) {
            Globals.InitializeFragment(new AllOrdersFragment(null), getFragmentManager());
        } else if (v.getId() == R.id.cvTables) {
            //Intent intent = new Intent(getActivity(),MenuActivity.class);
            //startActivity(intent);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content,new AllTablesFragment(getActivity(),false));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
           //Globals.InitializeFragment(new AllTablesFragment(getActivity(),false), getFragmentManager());
        } else if (v.getId() == R.id.cvMenu) {

            AllTablesFragment allTablesFragment = new AllTablesFragment(getActivity(),false);
            Bundle bundle = new Bundle();
            bundle.putBoolean("IsMenu",true);
            allTablesFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content,allTablesFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            //Globals.InitializeFragment(new CategoryItemFragment(), getFragmentManager());
        } else if (v.getId() == R.id.cvOffers) {
            Globals.InitializeFragment(new OfferFragment(getActivity()), getFragmentManager());
        }

    }
}
