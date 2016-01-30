package com.arraybit.pos;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;

public class GuestOptionListFragment extends Fragment implements View.OnClickListener {


    LinearLayout guestOptionLayout;


    public GuestOptionListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_option_list, container, false);

        guestOptionLayout = (LinearLayout) view.findViewById(R.id.guestOptionLayout);
        Globals.SetHomePageBackground(getActivity(), guestOptionLayout, null, null);

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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Globals.SetOptionMenu(Globals.userName, getActivity(), menu);
    }

    @Override
    public void onClick(View v) {
        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_guest_options))) {
            if (v.getId() == R.id.layoutOrders) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("TableMaster", GuestHomeActivity.objTableMaster);
                OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment(null);
                orderSummaryFragment.setArguments(bundle);
                Globals.ReplaceFragment(orderSummaryFragment, getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_order_summary));
            } else if (v.getId() == R.id.layoutFeedback) {
                if (Globals.userName == null) {
                    GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
                    guestLoginDialogFragment.setTargetFragment(this, 0);
                    guestLoginDialogFragment.show(getActivity().getSupportFragmentManager(), "");
                } else {
                    Globals.ReplaceFragment(new FeedbackFragment(getActivity()), getActivity().getSupportFragmentManager(), null);
                }
            } else if (v.getId() == R.id.layoutMenu) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ParentActivity", true);
                intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else if (v.getId() == R.id.layoutOffers) {
                Globals.ReplaceFragment(new OfferFragment(getActivity()), getActivity().getSupportFragmentManager(), null);
            }
        }
    }
}
