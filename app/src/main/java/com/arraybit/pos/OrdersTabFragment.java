package com.arraybit.pos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.OrdersAdapter;

import java.util.ArrayList;
import java.util.List;


public class OrdersTabFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "OrdersTabFragment$ItemsCount";
    RecyclerView rvOdrers;


    public OrdersTabFragment() {
        // Required empty public constructor
    }

    public static OrdersTabFragment createInstance(int itemsCount) {

        OrdersTabFragment orderTabFragment = new OrdersTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        orderTabFragment.setArguments(bundle);
        return orderTabFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders_tab, container, false);
        rvOdrers = (RecyclerView) view.findViewById(R.id.rvOdrers);

        setupRecyclerView(rvOdrers);
        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        OrdersAdapter ordersAdapter = new OrdersAdapter(createItemList());
        rvOdrers.setAdapter(ordersAdapter);
        rvOdrers.setLayoutManager(new GridLayoutManager(getActivity(),2));
    }

    private List<String> createItemList() {
        List<String> itemList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            int itemsCount = bundle.getInt(ITEMS_COUNT_KEY);
            for (int i = 0; i < itemsCount; i++) {
                itemList.add("Item " + i);
            }
        }
        return itemList;
    }
}
