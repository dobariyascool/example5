package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
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

import com.arraybit.adapter.OfferAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.OfferMaster;
import com.arraybit.parser.OfferJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

@SuppressWarnings({"ConstantConditions", "unchecked"})
@SuppressLint("ValidFragment")
public class OfferFragment extends Fragment {

    Activity activityName;
    FrameLayout offerFragment;
    OfferAdapter offerAdapter;
    ArrayList<OfferMaster> alOfferMaster;
    RecyclerView rvOffer;
    TextView txtMsg;

    public OfferFragment(Activity activityName) {
        this.activityName = activityName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.title_fragment_offer));
        }

        offerFragment = (FrameLayout) view.findViewById(R.id.offerFragment);
        Globals.SetScaleImageBackground(getActivity(),null, null,  offerFragment);

        rvOffer = (RecyclerView)view.findViewById(R.id.rvOffer);
        txtMsg = (TextView)view.findViewById(R.id.txtMsg);

        setHasOptionsMenu(true);

        if (Service.CheckNet(getActivity())) {
            new OfferLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            menu.findItem(R.id.mWaiting).setVisible(false);
        } else if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
        }

        if(getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName()!=null
                &&getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName()
                .equals(getActivity().getResources().getString(R.string.title_fragment_guest_options))){
            Globals.SetOptionMenu(Globals.userName,getActivity(),menu);
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
            getActivity().getSupportFragmentManager().popBackStack();
            Globals.HideKeyBoard(getActivity(), getView());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SetupRecyclerView() {

        offerAdapter = new OfferAdapter(getActivity(),alOfferMaster,getActivity().getSupportFragmentManager());
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(offerAdapter);
        rvOffer.setAdapter(scaleInAnimationAdapter);
        rvOffer.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //region LoadingTask
    class OfferLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OfferJSONParser objOfferJSONParser = new OfferJSONParser();
            return objOfferJSONParser.SelectAllOfferMaster();
        }

        @Override
        protected void onPostExecute(Object result) {
            progressDialog.dismiss();
            ArrayList<OfferMaster> lstOfferMaster = (ArrayList<OfferMaster>) result;
            if (lstOfferMaster == null) {
                Globals.SetError(txtMsg, rvOffer, getActivity().getResources().getString(R.string.MsgSelectFail), true);
            } else if (lstOfferMaster.size() == 0) {
                Globals.SetError(txtMsg, rvOffer, getActivity().getResources().getString(R.string.MsgOffer), true);
            } else {
                Globals.SetError(txtMsg, rvOffer, null, false);
                alOfferMaster = lstOfferMaster;
                SetupRecyclerView();
            }
        }
    }
    //endregion
}
