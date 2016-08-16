package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.adapter.OfferAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.OfferMaster;
import com.arraybit.parser.OfferJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


@SuppressWarnings({"ConstantConditions", "unchecked"})
@SuppressLint("ValidFragment")
public class OfferFragment extends Fragment {

    Activity activityName;
    FrameLayout offerFragment;
    OfferAdapter offerAdapter;
    ArrayList<OfferMaster> alOfferMaster;
    RecyclerView rvOffer;
    TextView txtMsg;
    String strActivityName;
    LinearLayout errorLayout;
    SharePreferenceManage objSharePreferenceManage;
    ImageView ivErrorIcon;

    public OfferFragment(Activity activityName) {
        this.activityName = activityName;
    }

    public OfferFragment(String strActivityName) {
        this.strActivityName = strActivityName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer, container, false);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);
        ivErrorIcon = (ImageView) errorLayout.findViewById(R.id.ivErrorIcon);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.title_fragment_offer));
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        offerFragment = (FrameLayout) view.findViewById(R.id.offerFragment);
        if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {
            if (Globals.objAppThemeMaster != null) {
                Globals.SetToolBarBackground(getActivity(), app_bar, Globals.objAppThemeMaster.getColorPrimary(), Globals.objAppThemeMaster.getColorCardText());
                objSharePreferenceManage = new SharePreferenceManage();
                String encodedImage = objSharePreferenceManage.GetPreference("GuestAppTheme", getActivity().getString(R.string.guestEncodedImage1), getActivity());
                if (encodedImage != null && !encodedImage.equals("")) {
                    Globals.SetPageBackground(getActivity(), encodedImage, null, null, offerFragment, null);
                } else {
                    Globals.SetScaleImageBackground(getActivity(), null, null, offerFragment);
                }
            } else {
                Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary), ContextCompat.getColor(getActivity(), android.R.color.white));
                Globals.SetScaleImageBackground(getActivity(), null, null, offerFragment);
            }
            ivErrorIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.errorIconColor), PorterDuff.Mode.SRC_IN);
            txtMsg.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
        } else {
            Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary_black), ContextCompat.getColor(getActivity(), android.R.color.white));
            offerFragment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_img));
        }

        rvOffer = (RecyclerView) view.findViewById(R.id.rvOffer);
        setHasOptionsMenu(true);

        if (Service.CheckNet(getActivity())) {
            new OfferLoadingTask().execute();
        } else {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), rvOffer, R.drawable.wifi_drawable);
        }

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if ((activityName != null && activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting)))
                || (strActivityName != null && strActivityName.equals(getActivity().getResources().getString(R.string.title_activity_waiting)))) {
            menu.findItem(R.id.viewChange).setVisible(false);
            menu.findItem(R.id.notification_layout).setVisible(false);
        } else if ((activityName != null && activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))
                || (strActivityName != null && strActivityName.equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))))) {
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
            if (Globals.isWishListShow == 1) {
                menu.findItem(R.id.shortList).setVisible(false);
                menu.findItem(R.id.login).setVisible(false);
                menu.findItem(R.id.logout).setVisible(false);
                menu.findItem(R.id.callWaiter).setVisible(false);
                menu.findItem(R.id.cart_layout).setVisible(false);
            } else if (!GuestHomeActivity.isMenuMode && !GuestHomeActivity.isGuestMode && Globals.isWishListShow == 0) {
                menu.findItem(R.id.notification_layout).setVisible(false);
            }
        }

//        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName() != null
//                && getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName()
//                .equals(getActivity().getResources().getString(R.string.title_fragment_guest_options))) {
//            Globals.SetOptionMenu(Globals.userName, getActivity(), menu);
//        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (strActivityName != null) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                        .equals(getActivity().getResources().getString(R.string.title_fragment_offer))) {
                    getActivity().finish();
                }
            } else {
                getActivity().getSupportFragmentManager().popBackStack();
            }
            Globals.HideKeyBoard(getActivity(), getView());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //region Private Methods
    private void SetupRecyclerView() {

        offerAdapter = new OfferAdapter(getActivity(), alOfferMaster, getActivity().getSupportFragmentManager(), false);
        rvOffer.setAdapter(offerAdapter);
        rvOffer.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOffer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!offerAdapter.isItemAnimate) {
                    offerAdapter.isItemAnimate = true;
                }
            }
        });
    }
    //endregion

    //region LoadingTask
    class OfferLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OfferJSONParser objOfferJSONParser = new OfferJSONParser();
            return objOfferJSONParser.SelectAllOfferMaster(Globals.businessMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            progressDialog.dismiss();
            ArrayList<OfferMaster> lstOfferMaster = (ArrayList<OfferMaster>) result;
            if (lstOfferMaster == null) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgSelectFail), rvOffer, 0);
            } else if (lstOfferMaster.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgOffer), rvOffer, 0);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvOffer, 0);
                alOfferMaster = lstOfferMaster;
                SetupRecyclerView();
            }
        }
    }
    //endregion
}
