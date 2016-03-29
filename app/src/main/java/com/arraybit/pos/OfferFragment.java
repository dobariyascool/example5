package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.LinearLayout;

import com.arraybit.adapter.OfferAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
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
        Globals.SetScaleImageBackground(getActivity(), null, null, offerFragment);

        rvOffer = (RecyclerView) view.findViewById(R.id.rvOffer);
        setHasOptionsMenu(true);

        if (Service.CheckNet(getActivity())) {
            new OfferLoadingTask().execute();
        } else {
            Globals.SetErrorLayout(errorLayout,true,getResources().getString(R.string.MsgCheckConnection),rvOffer);
        }

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if ((activityName != null && activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting)))
                || (strActivityName != null && strActivityName.equals(getActivity().getResources().getString(R.string.title_activity_waiting)))) {
            menu.findItem(R.id.viewChange).setVisible(false);
        } else if ((activityName != null && activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))
                || (strActivityName != null && strActivityName.equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))))) {
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
        }

        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName()
                .equals(getActivity().getResources().getString(R.string.title_fragment_guest_options))) {
            Globals.SetOptionMenu(Globals.userName, getActivity(), menu);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(), null, null, offerFragment);
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
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgSelectFail), rvOffer);
            } else if (lstOfferMaster.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgOffer), rvOffer);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvOffer);
                alOfferMaster = lstOfferMaster;
                SetupRecyclerView();
            }
        }
    }
    //endregion
}
