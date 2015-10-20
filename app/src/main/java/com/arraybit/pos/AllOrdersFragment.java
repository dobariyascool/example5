package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class AllOrdersFragment extends Fragment {

    FragmentTransaction fragmentTransaction;
    TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayout fragmentLayout;

    public AllOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_orders, container, false);
        tabLayout=(TabLayout)view.findViewById(R.id.tabLayout);
        viewPager=(ViewPager)view.findViewById(R.id.viewPager);

        fragmentLayout=(LinearLayout)view.findViewById(R.id.fragmentLayout);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(OrdersTabFragment.createInstance(20),"All");
        pagerAdapter.addFragment(OrdersTabFragment.createInstance(10),"Cooking");
        pagerAdapter.addFragment(OrdersTabFragment.createInstance(5),"Ready");
        pagerAdapter.addFragment(OrdersTabFragment.createInstance(9),"Served");
        pagerAdapter.addFragment(OrdersTabFragment.createInstance(2),"Completed");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void setFragment(int position)
    {
        if(position==0)
        {
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            fragmentLayout.setVisibility(View.GONE);
            AllOrdersFragment ordersFragment=new AllOrdersFragment();
            ReplaceFragment(ordersFragment);
        }
        else if(position==1)
        {
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            fragmentLayout.setVisibility(View.VISIBLE);
            AllTablesFragment tablesFragment=new AllTablesFragment();
            ReplaceFragment(tablesFragment);
        }
        else if(position==2)
        {
            Intent intent=new Intent(getActivity(),GuestHomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("CommitTransaction")
    public void ReplaceFragment(Fragment fragment) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, fragment);
        fragmentTransaction.commit();
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
}
