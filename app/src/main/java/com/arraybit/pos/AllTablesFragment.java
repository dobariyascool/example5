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
import android.widget.Toast;

import com.arraybit.global.Service;
import com.arraybit.modal.SectionMaster;
import com.arraybit.modal.TableMaster;
import com.arraybit.parser.SectionJSONParser;
import com.arraybit.parser.TableJSONParser;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked"})
public class AllTablesFragment extends Fragment {

    TabLayout tableTabLayout;
    ViewPager tableViewPager;
    ArrayList<SectionMaster> alSectionMaster;
    PagerAdapter pagerAdapter;
    ProgressDialog progressDialog;

    public AllTablesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_all_tables, container, false);
        tableTabLayout=(TabLayout)view.findViewById(R.id.tableTabLayout);
        tableViewPager=(ViewPager)view.findViewById(R.id.tableViewPager);

        if (Service.CheckNet(getActivity())) {
            new TableSectionLoadingTask().execute();
        } else {
            Toast.makeText(getActivity(),getResources().getString(R.string.MsgCheckConnection),Toast.LENGTH_LONG).show();
            //Globals.SetErrorLayout(error_layout, true, getResources().getString(R.string.MsgCheckConnection));
        }

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
//        pagerAdapter.addFragment(TableTabFragment.createInstance(20),"All");
//        pagerAdapter.addFragment(TableTabFragment.createInstance(10), "Non AC");
//        pagerAdapter.addFragment(TableTabFragment.createInstance(5), "AC");
//        tableViewPager.setAdapter(pagerAdapter);
//        tableTabLayout.setupWithViewPager(tableViewPager);
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

    class TableSectionLoadingTask extends AsyncTask {

        //ProgressDialog progressDialog;

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
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgSelectFail), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else if (alSectionMaster.size() == 0) {
                Toast.makeText(getActivity(),getResources().getString(R.string.MsgNoRecord),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else {

                pagerAdapter = new PagerAdapter(getChildFragmentManager());

                new TableMasterLoadingTask().execute();
            }

            progressDialog.dismiss();
        }
    }

    class TableMasterLoadingTask extends AsyncTask {

        //ProgressDialog progressDialog;
        ArrayList<TableMaster>[] alTableMaster;
        String[] SectionName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
//            progressDialog.setIndeterminate(true);
//            progressDialog.setCancelable(false);
//            progressDialog.show();

            alTableMaster = new ArrayList[alSectionMaster.size()];
            SectionName = new String[alSectionMaster.size()];

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TableJSONParser objTableJSONParser = new TableJSONParser();
            for(int j=0;j<alSectionMaster.size();j++) {

                SectionName[j] = alSectionMaster.get(j).getSectionName();
                alTableMaster[j] = objTableJSONParser.SelectAllTableMasterBySectionMasterId(1, alSectionMaster.get(j).getSectionMasterId());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            if (alTableMaster == null) {
                Toast.makeText(getActivity(),getResources().getString(R.string.MsgSelectFail),Toast.LENGTH_LONG).show();
            } else if (alTableMaster.length == 0) {
                Toast.makeText(getActivity(),getResources().getString(R.string.MsgNoRecord),Toast.LENGTH_LONG).show();
            } else {

                for(int k=0;k<alTableMaster.length;k++) {
                    pagerAdapter.addFragment(TableTabFragment.createInstance(alTableMaster[k]),SectionName[k]);
                }
            }
            tableViewPager.setAdapter(pagerAdapter);
            tableTabLayout.setupWithViewPager(tableViewPager);
            //progressDialog.dismiss();
        }

    }

    //endregion

}
