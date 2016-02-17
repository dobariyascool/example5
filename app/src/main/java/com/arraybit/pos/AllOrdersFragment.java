package com.arraybit.pos;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class AllOrdersFragment extends Fragment implements View.OnClickListener {

    TabLayout orderTabLayout;
    ViewPager orderViewPager;
    OrderPagerAdapter orderPagerAdapter;
    CoordinatorLayout allOrdersFragment;
    String linktoTableMasterIds;
    FloatingActionMenu famRoot;
    boolean isRefresh;

    public AllOrdersFragment(String linktoTableMasterIds) {
        this.linktoTableMasterIds = linktoTableMasterIds;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_orders, container, false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_all_orders));
        }
        //end

        allOrdersFragment = (CoordinatorLayout) view.findViewById(R.id.allOrdersFragment);
        Globals.SetScaleImageBackground(getActivity(), allOrdersFragment);

        setHasOptionsMenu(true);

        Globals.targetFragment = AllOrdersFragment.this;

        //floating action menu
        famRoot = (FloatingActionMenu) view.findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);
        //end

        //floating action button
        FloatingActionButton fabDineIn = (FloatingActionButton) view.findViewById(R.id.fabDineIn);
        FloatingActionButton fabTakeAway = (FloatingActionButton) view.findViewById(R.id.fabTakeAway);
        FloatingActionButton fabAll = (FloatingActionButton) view.findViewById(R.id.fabAll);
        //end

        //event
        fabDineIn.setOnClickListener(this);
        fabTakeAway.setOnClickListener(this);
        fabAll.setOnClickListener(this);
        //end

        orderTabLayout = (TabLayout) view.findViewById(R.id.orderTabLayout);
        orderViewPager = (ViewPager) view.findViewById(R.id.orderViewPager);

        SetTabLayout();

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.home).setVisible(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Globals.SetScaleImageBackground(getActivity(), allOrdersFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
                getActivity().getSupportFragmentManager().popBackStack();
            } else if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_all_orders))) {
                getActivity().finish();
            } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null &&
                    getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_all_orders))) {
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_all_orders), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    //region Method
    public void SetTabLayout() {

        orderPagerAdapter = new OrderPagerAdapter(getFragmentManager());
        orderPagerAdapter.AddFragment(OrdersTabFragment.createInstance(Globals.OrderStatus.All.toString(), linktoTableMasterIds), Globals.OrderStatus.All.toString());
        orderPagerAdapter.AddFragment(OrdersTabFragment.createInstance(Globals.OrderStatus.Cooking.toString(), linktoTableMasterIds), Globals.OrderStatus.Cooking.toString());
        orderPagerAdapter.AddFragment(OrdersTabFragment.createInstance(Globals.OrderStatus.Ready.toString(), linktoTableMasterIds), Globals.OrderStatus.Ready.toString());
        orderPagerAdapter.AddFragment(OrdersTabFragment.createInstance(Globals.OrderStatus.Served.toString(), linktoTableMasterIds), Globals.OrderStatus.Served.toString());
        orderPagerAdapter.AddFragment(OrdersTabFragment.createInstance(Globals.OrderStatus.Cancelled.toString(), linktoTableMasterIds), Globals.OrderStatus.Cancelled.toString());

        orderViewPager.setAdapter(orderPagerAdapter);
        orderTabLayout.setupWithViewPager(orderViewPager);
        OrdersTabFragment ordersTabFragment = (OrdersTabFragment) orderPagerAdapter.GetCurrentFragment(0);

        orderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (famRoot.isMenuButtonHidden()) {
                    famRoot.showMenuButton(true);
                }
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

    @Override
    public void onClick(View v) {
        OrdersTabFragment ordersTabFragment = (OrdersTabFragment) orderPagerAdapter.GetCurrentFragment(orderTabLayout.getSelectedTabPosition());
        String orderStatus = null;
        if (orderTabLayout.getSelectedTabPosition() == 0) {
            orderStatus = Globals.OrderStatus.All.toString();
        } else if (orderTabLayout.getSelectedTabPosition() == 1) {
            orderStatus = Globals.OrderStatus.Cooking.toString();
        } else if (orderTabLayout.getSelectedTabPosition() == 2) {
            orderStatus = Globals.OrderStatus.Ready.toString();
        } else if (orderTabLayout.getSelectedTabPosition() == 3) {
            orderStatus = Globals.OrderStatus.Served.toString();
        } else if (orderTabLayout.getSelectedTabPosition() == 4) {
            orderStatus = Globals.OrderStatus.Cancelled.toString();
        }

        if (v.getId() == R.id.fabDineIn) {
            famRoot.close(true);
            ordersTabFragment.OrderDataFilter(String.valueOf(Globals.OrderType.DineIn.getValue()));
        } else if (v.getId() == R.id.fabTakeAway) {
            famRoot.close(true);
            ordersTabFragment.OrderDataFilter(String.valueOf(Globals.OrderType.TakeAway.getValue()));
        } else if (v.getId() == R.id.fabAll) {
            famRoot.close(true);
            ordersTabFragment.OrderDataFilter(null);
        }
    }

    private void RemoveFragment(FragmentTransaction fragmentTransaction, int selectedPosition) {
        if (selectedPosition == 0) {
            OrdersTabFragment CurrentOrdersTabFragment = (OrdersTabFragment) orderPagerAdapter.GetCurrentFragment(orderTabLayout.getSelectedTabPosition());
            OrdersTabFragment NextOrdersTabFragment = (OrdersTabFragment) orderPagerAdapter.GetCurrentFragment(orderTabLayout.getSelectedTabPosition() + 1);
            fragmentTransaction.remove(CurrentOrdersTabFragment);
            fragmentTransaction.remove(NextOrdersTabFragment);
        } else if (selectedPosition == orderPagerAdapter.getCount() - 1) {
            OrdersTabFragment CurrentOrdersTabFragment = (OrdersTabFragment) orderPagerAdapter.GetCurrentFragment(orderTabLayout.getSelectedTabPosition());
            OrdersTabFragment PreviousOrdersTabFragment = (OrdersTabFragment) orderPagerAdapter.GetCurrentFragment(orderTabLayout.getSelectedTabPosition() - 1);
            fragmentTransaction.remove(CurrentOrdersTabFragment);
            fragmentTransaction.remove(PreviousOrdersTabFragment);
        } else {
            OrdersTabFragment CurrentOrdersTabFragment = (OrdersTabFragment) orderPagerAdapter.GetCurrentFragment(orderTabLayout.getSelectedTabPosition());
            OrdersTabFragment NextOrdersTabFragment = (OrdersTabFragment) orderPagerAdapter.GetCurrentFragment(orderTabLayout.getSelectedTabPosition() + 1);
            OrdersTabFragment PreviousOrdersTabFragment = (OrdersTabFragment) orderPagerAdapter.GetCurrentFragment(orderTabLayout.getSelectedTabPosition() - 1);
            fragmentTransaction.remove(CurrentOrdersTabFragment);
            fragmentTransaction.remove(NextOrdersTabFragment);
            fragmentTransaction.remove(PreviousOrdersTabFragment);
        }
    }

    private void RefreshCurrentFragment(FragmentTransaction fragmentTransaction, int selectedPosition) {
    }

    //endregion

    //region Pager Adapter
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
    //endregion

}
