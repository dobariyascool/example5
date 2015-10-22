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

import com.arraybit.modal.SectionMaster;
import com.arraybit.parser.SectionJSONParser;

import java.util.ArrayList;
import java.util.List;


public class AllTablesFragment extends Fragment {

    TabLayout tabLayout1;
    ViewPager viewPager1;
    ArrayList<SectionMaster> alSectionMaster;

    public AllTablesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_all_tables, container, false);
        tabLayout1=(TabLayout)view.findViewById(R.id.tableTabLayout);
        viewPager1=(ViewPager)view.findViewById(R.id.tableViewPager);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(TableTabFragment.createInstance(20),"All");
        pagerAdapter.addFragment(TableTabFragment.createInstance(10), "Non AC");
        pagerAdapter.addFragment(TableTabFragment.createInstance(5), "AC");
        viewPager1.setAdapter(pagerAdapter);
        tabLayout1.setupWithViewPager(viewPager1);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    static class PagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> fragmentList1 = new ArrayList<>();
        private final List<String> fragmentTitleList1 = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList1.add(fragment);
            fragmentTitleList1.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList1.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList1.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList1.get(position);
        }
    }

    //region Loading Task

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

            SectionJSONParser objSectionJSONParser = new SectionJSONParser();
            alSectionMaster = objSectionJSONParser.SelectAllSectionMaster();
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            if (alSectionMaster == null) {
                //Globals.SetErrorLayout(error_layout, true, getResources().getString(R.string.MsgSelectFail));
            } else if (alSectionMaster.size() == 0) {
                //Globals.SetErrorLayout(error_layout, true, getResources().getString(R.string.MsgNoRecord));
            } else {
                //Globals.SetErrorLayout(error_layout, false, null);

                //pagerAdapter = new PagerAdapter(getChildFragmentManager());

                //new WaitingMasterLoadingTask().execute();
            }

            progressDialog.dismiss();
        }
    }

    //endregion

}
