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
    ViewPager viewPager;
    LinearLayout error_layout;
    ArrayList<WaitingMaster> alWaitingMaster;
    ArrayList<WaitingStatusMaster> alWaitingStatusMaster;

    public WaitingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_list, container, false);

        error_layout = (LinearLayout) view.findViewById(R.id.error_layout);
        waitingTabLayout = (TabLayout) view.findViewById(R.id.waitingTabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.waitingViewPager);

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

    void AddTab(){

        ArrayList<WaitingMaster> alWaiting=null;
        WaitingMaster objWaitingMaster;
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        for (int i = 0; i < alWaitingStatusMaster.size(); i++) {

            alWaiting=new ArrayList<>();
            for(int j=0;j<alWaitingMaster.size();j++) {


                if(alWaitingMaster.get(j).getlinktoWaitingStatusMasterId()==alWaitingStatusMaster.get(i).getWaitingStatusMasterId())
                {
                    objWaitingMaster=new WaitingMaster();
                    objWaitingMaster.setWaitingMasterId(alWaitingMaster.get(j).getWaitingMasterId());
                    objWaitingMaster.setPersonName(alWaitingMaster.get(j).getPersonName());
                    objWaitingMaster.setPersonMobile(alWaitingMaster.get(j).getPersonMobile());
                    objWaitingMaster.setNoOfPersons(alWaitingMaster.get(j).getNoOfPersons());
                    alWaiting.add(objWaitingMaster);
                }

            }

            pagerAdapter.addFragment(WaitingTabFragment.createInstance(alWaiting), alWaitingStatusMaster.get(i).getWaitingStatus());
        }

        viewPager.setAdapter(pagerAdapter);
        waitingTabLayout.setupWithViewPager(viewPager);

    }

    //region LoadingTask

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
                Globals.SetErrorLayout(error_layout, true, getResources().getString(R.string.MsgSelectFail));
            } else if (alWaitingStatusMaster.size() == 0) {
                Globals.SetErrorLayout(error_layout, true, getResources().getString(R.string.MsgNoRecord));
            } else {
                Globals.SetErrorLayout(error_layout, false, null);

                new WaitingMasterLoadingTask().execute();
            }

            progressDialog.dismiss();
        }
    }

    //endregion

    class WaitingMasterLoadingTask extends AsyncTask {

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

            WaitingJSONParser objWaitingJSONParser = new WaitingJSONParser();
            alWaitingMaster = objWaitingJSONParser.SelectAllWaitingMasterByWaitingStatusMasterId(1);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            if (alWaitingMaster == null) {
                Globals.SetErrorLayout(error_layout, true, getResources().getString(R.string.MsgSelectFail));
            } else if (alWaitingMaster.size() == 0) {
                Globals.SetErrorLayout(error_layout, true, getResources().getString(R.string.MsgNoRecord));
            } else {
                Globals.SetErrorLayout(error_layout, false, null);
                AddTab();
            }
            progressDialog.dismiss();
        }
    }
}
