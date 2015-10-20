package com.arraybit.pos;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.OptionListAdapter;
import com.rey.material.widget.ListView;

import java.util.ArrayList;

public class WaiterOptionListFragment extends Fragment {

    ListView listView;
    ArrayList<String> alString;

    public WaiterOptionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiter_option_list, container, false);

        listView=(ListView)view.findViewById(android.R.id.list);

        setArrayList();

        listView.setAdapter(new OptionListAdapter(getActivity(),alString));

        return view;
    }

    public void setArrayList()
    {
        alString=new ArrayList<>();

        alString.add(0,"Orders");
        alString.add(1,"Tables");
        alString.add(2,"Menu");

    }
}
