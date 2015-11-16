package com.arraybit.pos;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.SectionMaster;
import com.arraybit.parser.SectionJSONParser;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked"})
public class AllTablesFragment extends Fragment implements View.OnClickListener {

    TabLayout tableTabLayout;
    ViewPager tableViewPager;
    ArrayList<SectionMaster> alSectionMaster;
    TablePagerAdapter tablePagerAdapter;
    FloatingActionMenu famRoot;

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

        //floating action menu
        famRoot = (FloatingActionMenu) view.findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);
        //end

        //floating action button
        FloatingActionButton fabVacant=(FloatingActionButton)view.findViewById(R.id.fabVacant);
        FloatingActionButton fabBusy=(FloatingActionButton)view.findViewById(R.id.fabBusy);
        FloatingActionButton fabAll=(FloatingActionButton)view.findViewById(R.id.fabAll);
        //end

        //event
        fabVacant.setOnClickListener(this);
        fabBusy.setOnClickListener(this);
        fabAll.setOnClickListener(this);
        //end

        if (Service.CheckNet(getActivity())) {
            new TableSectionLoadingTask().execute();
        } else {
            Toast.makeText(getActivity(),getResources().getString(R.string.MsgCheckConnection),Toast.LENGTH_LONG).show();
            //Globals.SetErrorLayout(error_layout, true, getResources().getString(R.string.MsgCheckConnection));
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        TableTabFragment tableTabFragment = (TableTabFragment) tablePagerAdapter.GetCurrentFragment(tableTabLayout.getSelectedTabPosition());
        SectionMaster objSectionMaster = tablePagerAdapter.GetCurrentSection(tableTabLayout.getSelectedTabPosition());

        if(v.getId()==R.id.fabVacant){
            tableTabFragment.TableDataFilter(objSectionMaster.getSectionMasterId(),String.valueOf(Globals.TableStatus.Vacant.getValue()));
            famRoot.close(true);
        }
        else if(v.getId()==R.id.fabBusy){
            tableTabFragment.TableDataFilter(objSectionMaster.getSectionMasterId(),String.valueOf(Globals.TableStatus.Busy.getValue()));
            famRoot.close(true);
        }
        else if(v.getId()==R.id.fabAll){
            tableTabFragment.TableDataFilter(objSectionMaster.getSectionMasterId(),null);
            famRoot.close(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    static class TablePagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> tableFragmentList = new ArrayList<>();
        private final List<SectionMaster> tableFragmentTitleList = new ArrayList<>();

        public TablePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void AddFragment(Fragment fragment, SectionMaster title) {
            tableFragmentList.add(fragment);
            tableFragmentTitleList.add(title);
        }

        public Fragment GetCurrentFragment(int position){
            return tableFragmentList.get(position);
        }

        public SectionMaster GetCurrentSection(int position){
            return tableFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return tableFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return tableFragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return tableFragmentTitleList.get(position).getSectionName();
        }
    }

    //region Loading Task

    class TableSectionLoadingTask extends AsyncTask {

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
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgSelectFail), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else if (alSectionMaster.size() == 0) {
                Toast.makeText(getActivity(),getResources().getString(R.string.MsgNoRecord),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else {

                tablePagerAdapter = new TablePagerAdapter(getChildFragmentManager());

                for(int i=0;i<alSectionMaster.size();i++){
                    tablePagerAdapter.AddFragment(TableTabFragment.createInstance(alSectionMaster.get(i)),alSectionMaster.get(i));
                }
                tableViewPager.setAdapter(tablePagerAdapter);
                tableTabLayout.setupWithViewPager(tableViewPager);
                progressDialog.dismiss();
            }
        }
    }
}
