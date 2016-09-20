package com.arraybit.pos;


import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.WaitingStatusMaster;
import com.arraybit.parser.WaitingStatusJSONParser;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"unchecked"})
public class WaitingListFragment extends Fragment {

    TabLayout waitingTabLayout;
    ViewPager waitingViewPager;
    FloatingActionButton fabAdd;

    ArrayList<WaitingStatusMaster> alWaitingStatusMaster;
    WaitingListPagerAdapter waitingListPagerAdapter;
    LinearLayout errorLayout,headerLayout;

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

        //floating action button
        fabAdd = (FloatingActionButton) view.findViewById(R.id.fabAdd);
        //end

        errorLayout = (LinearLayout)view.findViewById(R.id.errorLayout);
        headerLayout = (LinearLayout)view.findViewById(R.id.headerLayout);

        Drawable fabAddIcon;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            fabAddIcon = VectorDrawableCompat.create(getResources(), R.drawable.plus_white, getContext().getTheme());
        } else {
            fabAddIcon = getResources().getDrawable(R.drawable.plus_white, getContext().getTheme());
        }

        fabAdd.setImageDrawable(fabAddIcon);

        if (Service.CheckNet(getActivity())) {
            new WaitingStatusLoadingTask().execute();
        } else {
            SetErrorLayout(true,getResources().getString(R.string.MsgCheckConnection));
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    Globals.InitializeAnimatedFragment(new AddFragment(), getFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_add));
                }
            }
        });
    }

    //region Private Methods
    private void SetErrorLayout(boolean isShow, String errorMsg) {
        TextView txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);
        if (isShow) {
            errorLayout.setVisibility(View.VISIBLE);
            txtMsg.setText(errorMsg);
            waitingTabLayout.setVisibility(View.GONE);
            waitingViewPager.setVisibility(View.GONE);
            fabAdd.setVisibility(View.GONE);
            headerLayout.setVisibility(View.GONE);

        } else {
            errorLayout.setVisibility(View.GONE);
            waitingTabLayout.setVisibility(View.VISIBLE);
            waitingViewPager.setVisibility(View.VISIBLE);
            fabAdd.setVisibility(View.VISIBLE);
            headerLayout.setVisibility(View.VISIBLE);
        }
    }

    private void SetTabLayout(ArrayList<WaitingStatusMaster> alWaitingStatusMaster, final WaitingListPagerAdapter waitingListPagerAdapter) {

        for (int i = 0; i < alWaitingStatusMaster.size(); i++) {
            if (alWaitingStatusMaster.get(i).getWaitingStatus().equals(Globals.WaitingStatus.Assign.toString())) {
                waitingListPagerAdapter.AddFragment(WaitingTabFragment.createInstance(alWaitingStatusMaster.get(i)), alWaitingStatusMaster.get(i).getWaitingStatus() + "ed");
            } else if (alWaitingStatusMaster.get(i).getWaitingStatus().equals(Globals.WaitingStatus.Cancel.toString())) {
                waitingListPagerAdapter.AddFragment(WaitingTabFragment.createInstance(alWaitingStatusMaster.get(i)), alWaitingStatusMaster.get(i).getWaitingStatus() + "led");
            } else {
                waitingListPagerAdapter.AddFragment(WaitingTabFragment.createInstance(alWaitingStatusMaster.get(i)), alWaitingStatusMaster.get(i).getWaitingStatus());
            }
        }
        waitingViewPager.setAdapter(waitingListPagerAdapter);
        waitingTabLayout.setupWithViewPager(waitingViewPager);

        //set first tab
        WaitingTabFragment waitingTabFragment = (WaitingTabFragment) waitingListPagerAdapter.GetCurrentFragment(0);
        waitingTabFragment.LoadWaitingListData(getActivity(),getActivity().getSupportFragmentManager());
        //end

        waitingViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (fabAdd.isHidden()) {
                    fabAdd.show(true);
                }
                waitingViewPager.setCurrentItem(position);
                //load data when tab is change
                WaitingTabFragment waitingTabFragment = (WaitingTabFragment) waitingListPagerAdapter.GetCurrentFragment(position);
                waitingTabFragment.LoadWaitingListData(getActivity(),getActivity().getSupportFragmentManager());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //endregion

    //region Pager Adapter
    static class WaitingListPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> WaitingFragmentList = new ArrayList<>();
        private final List<String> WaitingTitleList = new ArrayList<>();

        public WaitingListPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void AddFragment(Fragment fragment, String title) {
            WaitingFragmentList.add(fragment);
            WaitingTitleList.add(title);
        }

        public Fragment GetCurrentFragment(int position) {
            return WaitingFragmentList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return WaitingFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return WaitingFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return WaitingTitleList.get(position);
        }
    }
    //endregion

    //region LoadingTask
    class WaitingStatusLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            WaitingStatusJSONParser objWaitingStatusJSONParser = new WaitingStatusJSONParser();
            alWaitingStatusMaster = objWaitingStatusJSONParser.SelectAllWaitingStatusMaster();
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            progressDialog.dismiss();
            if(alWaitingStatusMaster==null){
                SetErrorLayout(true,getResources().getString(R.string.MsgSelectFail));
            }
            else if(alWaitingStatusMaster.size()==0){
                SetErrorLayout(true,String.format(getResources().getString(R.string.MsgNoRecordFound),getResources().getString(R.string.MsgWaitingPerson)));
            }else{
                SetErrorLayout(false,null);
                waitingListPagerAdapter = new WaitingListPagerAdapter(getActivity().getSupportFragmentManager());
                SetTabLayout(alWaitingStatusMaster, waitingListPagerAdapter);
            }
        }
    }
    //endregion
}
