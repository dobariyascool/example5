package com.arraybit.pos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.TablesAdapter;
import com.arraybit.modal.TableMaster;

import java.util.ArrayList;
import java.util.List;


public class TableTabFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "TableTabFragment$ItemsCount";
    RecyclerView rvTables;
    TablesAdapter tablesAdapter;
    ArrayList<TableMaster> alTableMaster;

    public TableTabFragment() {
        // Required empty public constructor
    }

    public static TableTabFragment createInstance(ArrayList<TableMaster> alTableMaster) {

        TableTabFragment tableTabFragment = new TableTabFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ITEMS_COUNT_KEY, alTableMaster);
        tableTabFragment.setArguments(bundle);
        return tableTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_tab, container, false);
        rvTables = (RecyclerView) view.findViewById(R.id.rvTables);

        Bundle bundle = getArguments();
        alTableMaster = bundle.getParcelableArrayList(ITEMS_COUNT_KEY);

        setupRecyclerView(rvTables);
        return view;
    }



    private void setupRecyclerView(RecyclerView recyclerView) {

        tablesAdapter = new TablesAdapter(getActivity(),alTableMaster);
        recyclerView.setAdapter(tablesAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
