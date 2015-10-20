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
import com.arraybit.modal.WaitingPerson;
import com.arraybit.modal.WaitingStatusMaster;
import com.arraybit.parser.WaitingStatusJSONParser;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked"})
public class WaitingListFragment extends Fragment {

    TabLayout waitingTabLayout;
    ViewPager viewPager;
    LinearLayout error_layout;
    ArrayList<WaitingPerson> alWaitingPerson;
    ArrayList<Integer> alInteger;
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



        //SetStatusList();
//        for (int j = 0; j < alWaitingStatusMaster.size(); j++) {
//
//            alWaitingPerson = SetList(alWaitingStatusMaster.get(j).getWaitingStatusMasterId());
//            if (j == 0) {
//                pagerAdapter.addFragment(WaitingTabFragment.createInstance(alWaitingPerson.size()),alWaitingStatusMaster.get(j).getWaitingStatus());
//            } else if (j == 1) {
//                pagerAdapter.addFragment(WaitingTabFragment.createInstance(alWaitingPerson.size()),alWaitingStatusMaster.get(j).getWaitingStatus());
//            }
//        }
//
//        //pagerAdapter.addFragment(WaitingTabFragment.createInstance(3),"Waiting");
//        //pagerAdapter.addFragment(WaitingTabFragment.createInstance(5),"Served");
//        viewPager.setAdapter(pagerAdapter);
//        waitingTabLayout.setupWithViewPager(viewPager);
    }

    //region WaitingStatusLoadingTask

    public void SetStatusList() {
        alInteger = new ArrayList<>();
        alInteger.add(new Integer(1));
        alInteger.add(new Integer(2));
        alInteger.add(new Integer(3));
        alInteger.add(new Integer(4));
        alInteger.add(new Integer(5));
    }

    //endregion

    public ArrayList<WaitingPerson> SetList(int id) {
        alWaitingPerson = new ArrayList<>();
        if (id == 1) {
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 1));
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 1));
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 1));
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 1));
            return alWaitingPerson;
        } else if (id == 2) {
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 4));
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 4));
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 4));
            return alWaitingPerson;
        } else if (id == 3) {
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 3));
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 3));
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 3));
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 3));
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 3));
            return alWaitingPerson;
        } else if (id == 4) {
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 4));
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 4));
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 4));
            return alWaitingPerson;
        } else {
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 5));
            alWaitingPerson.add(new WaitingPerson(1, "abc", "598437583", 7, 5));
            return alWaitingPerson;
        }

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
                Toast.makeText(getActivity(),getResources().getString(R.string.title_fragment_profile),Toast.LENGTH_LONG).show();
            } else if (alWaitingStatusMaster.size() == 0) {
                Globals.SetErrorLayout(error_layout, true, getResources().getString(R.string.MsgNoRecord));
            } else {
                Globals.SetErrorLayout(error_layout, false, null);

                PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
                for (int j = 0; j < alWaitingStatusMaster.size(); j++) {

                    alWaitingPerson = SetList(alWaitingStatusMaster.get(j).getWaitingStatusMasterId());
                    if (j == 0) {
                        pagerAdapter.addFragment(WaitingTabFragment.createInstance(alWaitingPerson.size()), alWaitingStatusMaster.get(j).getWaitingStatus());
                    } else if (j == 1) {
                        pagerAdapter.addFragment(WaitingTabFragment.createInstance(alWaitingPerson.size()), alWaitingStatusMaster.get(j).getWaitingStatus());
                    }
                }
                viewPager.setAdapter(pagerAdapter);
                waitingTabLayout.setupWithViewPager(viewPager);
            }

            progressDialog.dismiss();
        }
    }

}
