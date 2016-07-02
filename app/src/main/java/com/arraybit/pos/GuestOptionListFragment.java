package com.arraybit.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;

public class GuestOptionListFragment extends Fragment implements View.OnClickListener {


    LinearLayout guestOptionLayout, menuModeLayout, guestModeLayout;
    GuestLoginDialogFragment.LoginResponseListener objLoginResponseListener;


    public GuestOptionListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_option_list, container, false);

        guestOptionLayout = (LinearLayout) view.findViewById(R.id.guestOptionLayout);
//        if (!getActivity().getResources().getBoolean(R.bool.isTablet)) {
        menuModeLayout = (LinearLayout) view.findViewById(R.id.menuModeLayout);
        guestModeLayout = (LinearLayout) view.findViewById(R.id.guestModeLayout);
//        }

        Globals.SetHomePageBackground(getActivity(), guestOptionLayout, null, null);

        CardView cvMenu = (CardView) view.findViewById(R.id.cvMenu);
        CardView cvOrders = (CardView) view.findViewById(R.id.cvOrders);
        CardView cvOffers = (CardView) view.findViewById(R.id.cvOffers);
        CardView cvFeedback = (CardView) view.findViewById(R.id.cvFeedback);
        CardView cvMenuModeMenu = (CardView) view.findViewById(R.id.cvMenuModeMenu);
        CardView cvMenuModeOffers = (CardView) view.findViewById(R.id.cvMenuModeOffers);

        cvMenu.setOnClickListener(this);
        cvOrders.setOnClickListener(this);
        cvOffers.setOnClickListener(this);
        cvFeedback.setOnClickListener(this);
        cvMenuModeMenu.setOnClickListener(this);
        cvMenuModeOffers.setOnClickListener(this);


        if (GuestHomeActivity.isMenuMode) {
            menuModeLayout.setVisibility(View.VISIBLE);
            guestModeLayout.setVisibility(View.GONE);
        } else {
            menuModeLayout.setVisibility(View.GONE);
            guestModeLayout.setVisibility(View.VISIBLE);
        }

//        if (!getActivity().getResources().getBoolean(R.bool.isTablet)) {
//
//
//
//        } else {
//            if (GuestHomeActivity.isMenuMode) {
//                cvOrders.setVisibility(View.GONE);
//                cvFeedback.setVisibility(View.GONE);
//            }else{
//                cvOrders.setVisibility(View.VISIBLE);
//                cvFeedback.setVisibility(View.VISIBLE);
//            }
//        }

        setHasOptionsMenu(true);


        return view;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (!GuestHomeActivity.isMenuMode) {
            Globals.SetOptionMenu(Globals.userName, getActivity(), menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!GuestHomeActivity.isMenuMode) {
            if (item.getItemId() == R.id.callWaiter) {
                CallWaiterDialog callWaiterDialog = new CallWaiterDialog();
                callWaiterDialog.show(getActivity().getSupportFragmentManager(), "");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!GuestHomeActivity.isMenuMode) {
            objLoginResponseListener = (GuestLoginDialogFragment.LoginResponseListener) getActivity();
            objLoginResponseListener.LoginResponse();
        }
    }

    @Override
    public void onClick(View v) {
        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_guest_options))) {
            if (v.getId() == R.id.cvOrders) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("TableMaster", GuestHomeActivity.objTableMaster);
                OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment();
                orderSummaryFragment.setArguments(bundle);
                Globals.ReplaceFragment(orderSummaryFragment, getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_order_summary));
            } else if (v.getId() == R.id.cvFeedback) {
                if (Globals.userName == null) {
                    GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
                    guestLoginDialogFragment.setTargetFragment(this, 0);
                    guestLoginDialogFragment.show(getActivity().getSupportFragmentManager(), "");
                } else {
                    Globals.ReplaceFragment(new FeedbackFragment(getActivity()), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_feedback));
                }
            } else if (v.getId() == R.id.cvMenu || v.getId() == R.id.cvMenuModeMenu) {
                if (v.getId() == R.id.cvMenu && !GuestHomeActivity.isMenuMode) {
                    Globals.isWishListShow = 1;
                    Globals.orderTypeMasterId = GuestHomeActivity.objTableMaster.getlinktoOrderTypeMasterId();
                } else {
                    Globals.isWishListShow = 0;
                }
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                if (v.getId() == R.id.cvMenu && !GuestHomeActivity.isMenuMode) {
                    intent.putExtra("ParentActivity", true);
                    intent.putExtra("IsFavoriteShow", false);
                    intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
                }
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else if (v.getId() == R.id.cvOffers || v.getId() == R.id.cvMenuModeOffers) {
                Globals.ReplaceFragment(new OfferFragment(getActivity()), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_offer));
            }
        }
    }
}
