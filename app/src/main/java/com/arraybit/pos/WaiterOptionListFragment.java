package com.arraybit.pos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.OptionListAdapter;

import java.util.ArrayList;

public class WaiterOptionListFragment extends Fragment {

    ArrayList<String> alString;

    public WaiterOptionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiter_option_list, container, false);

        RecyclerView rvOptions=(RecyclerView)view.findViewById(R.id.rvOptions);

        setArrayList();

        rvOptions.setAdapter(new OptionListAdapter(getActivity(), alString));
        if(getActivity().getResources().getBoolean(R.bool.isTablet)){
            rvOptions.setLayoutManager(new GridLayoutManager(getActivity(),4));
        }
        else
        {
            rvOptions.setLayoutManager(new GridLayoutManager(getActivity(),2));
        }


        return view;
    }

    public void setArrayList()
    {
        alString=new ArrayList<>();

        alString.add(0,"Orders");
        alString.add(1,"Tables");
        alString.add(2,"Menu");
        alString.add(3,"Offers");
    }
}
