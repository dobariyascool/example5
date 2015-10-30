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
    ProgressDialog progressDialog;

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

        if (Service.CheckNet(getActivity())) {
            new WaitingStatusLoadingTask().execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
            //Globals.SetErrorLayout(error_layout, true, getResources().getString(R.string.MsgCheckConnection));
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.InitializeAnimatedFragment(new AddFragment(), getFragmentManager());
            }
        });

    }

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

    //region LoadingTask

    class WaitingStatusLoadingTask extends AsyncTask {

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
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgSelectFail), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else if (alWaitingStatusMaster.size() == 0) {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgNoRecord), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else {

                waitingListPagerAdapter = new WaitingListPagerAdapter(getChildFragmentManager());

                for (int i = 0; i < alWaitingStatusMaster.size(); i++) {
                    waitingListPagerAdapter.AddFragment(WaitingTabFragment.createInstance((WaitingStatusMaster) alWaitingStatusMaster.get(i)), alWaitingStatusMaster.get(i).getWaitingStatus());
                }
                waitingViewPager.setAdapter(waitingListPagerAdapter);
                waitingTabLayout.setupWithViewPager(waitingViewPager);
                progressDialog.dismiss();
            }
        }
    }

    //endregion

}
