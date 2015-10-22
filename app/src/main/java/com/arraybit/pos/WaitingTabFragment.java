package com.arraybit.pos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.WaitingListAdapter;
import com.arraybit.modal.WaitingMaster;

import java.util.ArrayList;


public class WaitingTabFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "WaitingTabFragment$ItemsCount";
    RecyclerView rvWaiting;
    ArrayList<WaitingMaster> alWaitingMaster;


    public WaitingTabFragment() {
    }

    public static WaitingTabFragment createInstance(ArrayList<WaitingMaster> alWaitingMaster) {

        WaitingTabFragment waitingTabFragment = new WaitingTabFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ITEMS_COUNT_KEY, alWaitingMaster);
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
        WaitingListAdapter waitingListAdapter = new WaitingListAdapter(getActivity(),createItemList());
        rvWaiting.setAdapter(waitingListAdapter);
        rvWaiting.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private ArrayList<WaitingMaster> createItemList() {
        ArrayList<WaitingMaster> itemList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            itemList = bundle.getParcelableArrayList(ITEMS_COUNT_KEY);
        }
        return itemList;
    }
}
