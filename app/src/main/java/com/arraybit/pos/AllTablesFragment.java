package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

    static boolean isRefresh = false;
    TabLayout tableTabLayout;
    ViewPager tableViewPager;
    ArrayList<SectionMaster> alSectionMaster;
    TablePagerAdapter tablePagerAdapter;
    FloatingActionMenu famRoot;
    Activity activityName;
    CoordinatorLayout allTablesFragment;
    boolean isChangeMode, isVacant = false;
    String linktoOrderTypeMasterId;


    public AllTablesFragment(Activity activityName, boolean isChangeMode, String linktoOrderTypeMasterId) {
        this.activityName = activityName;
        this.isChangeMode = isChangeMode;
        this.linktoOrderTypeMasterId = linktoOrderTypeMasterId;
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

            allTablesFragment = (CoordinatorLayout) view.findViewById(R.id.allTablesFragment);
            Globals.SetScaleImageBackground(getActivity(),allTablesFragment);

            setHasOptionsMenu(true);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            isVacant = bundle.getBoolean("IsVacant");
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
            Globals.ShowSnackBar(getActivity().getCurrentFocus(), getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
                getActivity().getSupportFragmentManager().popBackStack();
            } else {
                getActivity().finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            menu.findItem(R.id.action_search).setVisible(true);
            menu.findItem(R.id.cart_layout).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
            menu.findItem(R.id.home).setVisible(false);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(),allTablesFragment);
    }

    @Override
    public void onClick(View v) {

        TableTabFragment tableTabFragment = (TableTabFragment) tablePagerAdapter.GetCurrentFragment(tableTabLayout.getSelectedTabPosition());
        if (v.getId() == R.id.fabVacant) {
            famRoot.close(true);
            tableTabFragment.TableDataFilter(String.valueOf(Globals.TableStatus.Vacant.getValue()));
        } else if (v.getId() == R.id.fabBusy) {
            famRoot.close(true);
            tableTabFragment.TableDataFilter(String.valueOf(Globals.TableStatus.Occupied.getValue()));
        } else if (v.getId() == R.id.fabAll) {
            famRoot.close(true);
            tableTabFragment.TableDataFilter(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            isRefresh = false;
        }
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

//        public SectionMaster GetCurrentSection(int position) {
//            return tableFragmentTitleList.get(position);
//        }

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
            progressDialog.setMessage(getActivity().getResources().getString(R.string.MsgLoading));
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
                progressDialog.dismiss();
                Globals.ShowSnackBar(allTablesFragment, getActivity().getResources().getString(R.string.MsgSelectFail), getActivity(), 1000);

            } else if (alSectionMaster.size() == 0) {
                progressDialog.dismiss();
                Globals.ShowSnackBar(allTablesFragment, getActivity().getResources().getString(R.string.MsgNoRecord), getActivity(), 1000);

            } else {

                if (isVacant) {
                    famRoot.setVisibility(View.GONE);
                } else {
                    famRoot.setVisibility(View.VISIBLE);
                }

                tablePagerAdapter = new TablePagerAdapter(getFragmentManager());

                SectionMaster objSectionMaster = new SectionMaster();
                objSectionMaster.setSectionMasterId((short) 0);
                objSectionMaster.setSectionName("All");
                ArrayList<SectionMaster> alSection = new ArrayList<>();
                alSection.add(objSectionMaster);

                alSectionMaster.addAll(0, alSection);
                for (int i = 0; i < alSectionMaster.size(); i++) {
                    tablePagerAdapter.AddFragment(TableTabFragment.createInstance(alSectionMaster.get(i), isChangeMode, linktoOrderTypeMasterId), alSectionMaster.get(i));
                }
                tableViewPager.setAdapter(tablePagerAdapter);
                tableTabLayout.setupWithViewPager(tableViewPager);

                TableTabFragment tableTabFragment = (TableTabFragment) tablePagerAdapter.GetCurrentFragment(0);
                if (isVacant) {
                    tableTabFragment.LoadTableData(String.valueOf(Globals.TableStatus.Vacant.getValue()));
                } else {
                    tableTabFragment.LoadTableData(null);
                }


                tableViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        if(famRoot.isMenuButtonHidden()){
                            famRoot.showMenuButton(true);
                        }
                        tableViewPager.setCurrentItem(position);
                        //load data when tab is change
                        TableTabFragment tableTabFragment = (TableTabFragment) tablePagerAdapter.GetCurrentFragment(position);
                        if (isVacant) {
                            tableTabFragment.LoadTableData(String.valueOf(Globals.TableStatus.Vacant.getValue()));
                        } else {
                            tableTabFragment.LoadTableData(null);
                        }
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
