package com.arraybit.pos;


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

import java.util.ArrayList;
import java.util.List;


public class AllTablesFragment extends Fragment {

    TabLayout tabLayout1;
    ViewPager viewPager1;

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

        PagerAdapter1 pagerAdapter = new PagerAdapter1(getChildFragmentManager());
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

    static class PagerAdapter1 extends FragmentStatePagerAdapter {

        private final List<Fragment> fragmentList1 = new ArrayList<>();
        private final List<String> fragmentTitleList1 = new ArrayList<>();

        public PagerAdapter1(FragmentManager fragmentManager) {
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
}
