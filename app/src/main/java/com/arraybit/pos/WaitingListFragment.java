package com.arraybit.pos;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.WaitingMaster;
import com.arraybit.modal.WaitingStatusMaster;
import com.arraybit.parser.WaitingJSONParser;
import com.arraybit.parser.WaitingStatusJSONParser;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked"})
public class WaitingListFragment extends Fragment {

    TabLayout waitingTabLayout;
    ViewPager waitingViewPager;
    LinearLayout error_layout;

    ArrayList<WaitingStatusMaster> alWaitingStatusMaster;
    PagerAdapter pagerAdapter;
    String[] WaitingStatus;

    public WaitingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_list, container, false);

        //error_layout = (LinearLayout) view.findViewById(R.id.error_layout);
        waitingTabLayout = (TabLayout) view.findViewById(R.id.waitingTabLayout);
        waitingViewPager = (ViewPager) view.findViewById(R.id.waitingViewPager);

        if (Service.CheckNet(getActivity())) {
            new WaitingStatusLoadingTask().execute();
        } else {
            Globals.SetErrorLayout(error_layout, true, getResources().getString(R.string.MsgCheckConnection));
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    static class PagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    //region LoadingTask

    class WaitingStatusLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            WaitingStatusJSONParser objWaitingStatusJSONParser = new WaitingStatusJSONParser();
            alWaitingStatusMaster = objWaitingStatusJSONParser.SelectAllWaitingStatusMaster();
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            if (alWaitingStatusMaster == null) {
                Toast.makeText(getActivity(),getResources().getString(R.string.MsgSelectFail),Toast.LENGTH_LONG).show();
            } else if (alWaitingStatusMaster.size() == 0) {
                Toast.makeText(getActivity(),getResources().getString(R.string.MsgNoRecord),Toast.LENGTH_LONG).show();
            } else {

                pagerAdapter = new PagerAdapter(getChildFragmentManager());

                new WaitingMasterLoadingTask().execute();
            }

            progressDialog.dismiss();
        }
    }

    class WaitingMasterLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;
        ArrayList<WaitingMaster>[] alWaitingMaster;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            alWaitingMaster = new ArrayList[alWaitingStatusMaster.size()];
            WaitingStatus = new String[alWaitingStatusMaster.size()];

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            WaitingJSONParser objWaitingJSONParser = new WaitingJSONParser();
            for(int j=0;j<alWaitingStatusMaster.size();j++) {

                WaitingStatus[j] = alWaitingStatusMaster.get(j).getWaitingStatus();
                alWaitingMaster[j] = objWaitingJSONParser.SelectAllWaitingMasterByWaitingStatusMasterId(1, alWaitingStatusMaster.get(j).getWaitingStatusMasterId());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            if (alWaitingMaster == null) {
                Toast.makeText(getActivity(),getResources().getString(R.string.MsgSelectFail),Toast.LENGTH_LONG).show();
            } else if (alWaitingMaster.length == 0) {
                Toast.makeText(getActivity(),getResources().getString(R.string.MsgNoRecord),Toast.LENGTH_LONG).show();
            } else {

                for(int k=0;k<alWaitingMaster.length;k++) {
                    pagerAdapter.addFragment(WaitingTabFragment.createInstance(alWaitingMaster[k]),WaitingStatus[k]);
                }
            }
            waitingViewPager.setAdapter(pagerAdapter);
            waitingTabLayout.setupWithViewPager(waitingViewPager);
            progressDialog.dismiss();
        }

    }

    //endregion


}