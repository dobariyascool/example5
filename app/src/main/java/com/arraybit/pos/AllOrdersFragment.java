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

import com.arraybit.global.Globals;

import java.util.ArrayList;
import java.util.List;

public class AllOrdersFragment extends Fragment{

    TabLayout orderTabLayout;
    ViewPager orderViewPager;
    OrderPagerAdapter orderPagerAdapter;

    public AllOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_orders, container, false);

        orderTabLayout = (TabLayout) view.findViewById(R.id.orderTabLayout);
        orderViewPager = (ViewPager) view.findViewById(R.id.orderViewPager);

        //fragmentLayout=(LinearLayout)view.findViewById(R.id.fragmentLayout);
        orderPagerAdapter = new OrderPagerAdapter(getChildFragmentManager());

        SetTabLayout();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //region Method
    public void SetTabLayout() {

        orderPagerAdapter.AddFragment(OrdersTabFragment.createInstance(Globals.OrderStatus.Cooking.toString()), Globals.OrderStatus.Cooking.toString());
        orderPagerAdapter.AddFragment(OrdersTabFragment.createInstance(Globals.OrderStatus.Ready.toString()), Globals.OrderStatus.Ready.toString());
        orderPagerAdapter.AddFragment(OrdersTabFragment.createInstance(Globals.OrderStatus.Served.toString()), Globals.OrderStatus.Served.toString());
        orderPagerAdapter.AddFragment(OrdersTabFragment.createInstance(Globals.OrderStatus.Cancelled.toString()), Globals.OrderStatus.Cancelled.toString());

        orderViewPager.setAdapter(orderPagerAdapter);
        orderTabLayout.setupWithViewPager(orderViewPager);

        orderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                orderViewPager.setCurrentItem(position);
                //load data when tab is change
                OrdersTabFragment ordersTabFragment = (OrdersTabFragment) orderPagerAdapter.GetCurrentFragment(position);
                ordersTabFragment.LoadOrderData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    //endregion

    static class OrderPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> orderFragmentList = new ArrayList<>();
        private final List<String> orderFragmentTitleList = new ArrayList<>();

        public OrderPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void AddFragment(Fragment fragment, String title) {
            orderFragmentList.add(fragment);
            orderFragmentTitleList.add(title);
        }

        public Fragment GetCurrentFragment(int position) {
            return orderFragmentList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return orderFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return orderFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return orderFragmentTitleList.get(position);
        }
    }
}
