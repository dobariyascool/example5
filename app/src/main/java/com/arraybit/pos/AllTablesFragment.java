package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.SectionMaster;
import com.arraybit.parser.SectionJSONParser;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "ConstantConditions"})
@SuppressLint("ValidFragment")
public class AllTablesFragment extends Fragment implements View.OnClickListener {

    TabLayout tableTabLayout;
    ViewPager tableViewPager;
    ArrayList<SectionMaster> alSectionMaster;
    TablePagerAdapter tablePagerAdapter;
    FloatingActionMenu famRoot;
    Activity activityName;
    RelativeLayout allTablesFragment;
    boolean isChangeMode;


    public AllTablesFragment(Activity activityName,boolean isChangeMode) {
        this.activityName = activityName;
        this.isChangeMode = isChangeMode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_all_tables, container, false);

        if (this.activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            view.findViewById(R.id.app_bar).setVisibility(View.GONE);
            setHasOptionsMenu(false);
        } else {

            Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
            app_bar.setVisibility(View.VISIBLE);

            if (app_bar != null) {
                ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_all_tables));
            }

            allTablesFragment = (RelativeLayout) view.findViewById(R.id.allTablesFragment);
            Globals.SetScaleImageBackground(getActivity(), null, allTablesFragment, null);

            setHasOptionsMenu(true);
        }


        tableTabLayout = (TabLayout) view.findViewById(R.id.tableTabLayout);
        tableViewPager = (ViewPager) view.findViewById(R.id.tableViewPager);

        //floating action menu
        famRoot = (FloatingActionMenu) view.findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);
        //end

        //floating action button
        FloatingActionButton fabVacant = (FloatingActionButton) view.findViewById(R.id.fabVacant);
        FloatingActionButton fabBusy = (FloatingActionButton) view.findViewById(R.id.fabBusy);
        FloatingActionButton fabAll = (FloatingActionButton) view.findViewById(R.id.fabAll);
        //end

        //event
        fabVacant.setOnClickListener(this);
        fabBusy.setOnClickListener(this);
        fabAll.setOnClickListener(this);
        //end

        if (Service.CheckNet(getActivity())) {
            new TableSectionLoadingTask().execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
            //Globals.SetErrorLayout(error_layout, true, getResources().getString(R.string.MsgCheckConnection));
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            menu.findItem(R.id.action_search).setVisible(true);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(), null, allTablesFragment, null);
    }

    @Override
    public void onClick(View v) {

        TableTabFragment tableTabFragment = (TableTabFragment) tablePagerAdapter.GetCurrentFragment(tableTabLayout.getSelectedTabPosition());
        SectionMaster objSectionMaster = tablePagerAdapter.GetCurrentSection(tableTabLayout.getSelectedTabPosition());

        if (v.getId() == R.id.fabVacant) {
            famRoot.close(true);
            tableTabFragment.TableDataFilter(objSectionMaster.getSectionMasterId(), String.valueOf(Globals.TableStatus.Vacant.getValue()));
        } else if (v.getId() == R.id.fabBusy) {
            famRoot.close(true);
            tableTabFragment.TableDataFilter(objSectionMaster.getSectionMasterId(), String.valueOf(Globals.TableStatus.Occupied.getValue()));
        } else if (v.getId() == R.id.fabAll) {
            famRoot.close(true);
            tableTabFragment.TableDataFilter(objSectionMaster.getSectionMasterId(), null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //region PagerAdapter
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

        public Fragment GetCurrentFragment(int position) {
            return tableFragmentList.get(position);
        }

        public SectionMaster GetCurrentSection(int position) {
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
    //endregion

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

            progressDialog.dismiss();
            if (alSectionMaster == null) {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgSelectFail), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else if (alSectionMaster.size() == 0) {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgNoRecord), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else {

                tablePagerAdapter = new TablePagerAdapter(getChildFragmentManager());

                SectionMaster objSectionMaster = new SectionMaster();
                objSectionMaster.setSectionMasterId((short) 0);
                objSectionMaster.setSectionName("All");
                ArrayList<SectionMaster> alSection = new ArrayList<>();
                alSection.add(objSectionMaster);

                alSectionMaster.addAll(0, alSection);
                for (int i = 0; i < alSectionMaster.size(); i++) {
                    tablePagerAdapter.AddFragment(TableTabFragment.createInstance(alSectionMaster.get(i),true), alSectionMaster.get(i));
                }
                tableViewPager.setAdapter(tablePagerAdapter);
                tableTabLayout.setupWithViewPager(tableViewPager);

                tableViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        tableViewPager.setCurrentItem(position);
                        //load data when tab is change
                        TableTabFragment tableTabFragment = (TableTabFragment) tablePagerAdapter.GetCurrentFragment(position);
                        tableTabFragment.LoadTableData();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        }
    }
    //endregion
}
