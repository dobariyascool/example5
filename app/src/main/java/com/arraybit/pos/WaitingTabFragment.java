package com.arraybit.pos;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.WaitingListAdapter;
import com.arraybit.modal.Person;

import java.util.ArrayList;


public class WaitingTabFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "WaitingTabFragment$ItemsCount";
    RecyclerView rvWaiting;

    public WaitingTabFragment() {
        // Required empty public constructor
    }

    public static WaitingTabFragment createInstance(int itemsCount) {

        WaitingTabFragment waitingTabFragment = new WaitingTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        waitingTabFragment.setArguments(bundle);
        return waitingTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_tab, container, false);

        rvWaiting = (RecyclerView) view.findViewById(R.id.rvWaiting);

        setupRecyclerView(rvWaiting);
        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        WaitingListAdapter waitingListAdapter = new WaitingListAdapter(getActivity(), createItemList());
        rvWaiting.setAdapter(waitingListAdapter);
        rvWaiting.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private ArrayList<Person> createItemList() {
        ArrayList<Person> itemList = new ArrayList<>();
        Person objPerson;
        Bundle bundle = getArguments();
        if (bundle != null) {
            int itemsCount = bundle.getInt(ITEMS_COUNT_KEY);
            for (int i = 0; i < itemsCount; i++) {

                itemList.add(new Person(i + 1, "abc", "9923124512", 5));
            }
        }
        return itemList;
    }
}
