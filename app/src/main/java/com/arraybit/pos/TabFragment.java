package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;

import com.arraybit.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;


public class TabFragment extends Fragment{

    public final static String ITEMS_COUNT_KEY = "TabFragment$ItemsCount";
    RecyclerView recyclerView;

    public TabFragment() {

    }

    public static TabFragment createInstance(int itemsCount) {

        TabFragment tabFragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        setupRecyclerView(recyclerView);


//        recyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager=(ViewPager)getActivity().findViewById(R.id.viewPager);
//                viewPager.setVisibility(View.GONE);
//                DetailFragment detailFragment=new DetailFragment();
//                fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentLayout,detailFragment);
//                fragmentTransaction.commit();
//            }
//        });

        return view;
    }

    public void setupRecyclerView(RecyclerView recyclerView) {

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(createItemList(),getFragmentManager(),GuestHomeActivity.isCheck);
        recyclerView.setAdapter(recyclerAdapter);
        if(GuestHomeActivity.isCheck){
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

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

//    @Override
//    public void ItemDetail() {
//        //recyclerView.setVisibility(View.GONE);
//        //DetailFragment detailFragment = new DetailFragment();
//        //fragmentTransaction = getFragmentManager().beginTransaction();
//        //fragmentTransaction.replace(R.id.fragmentLayout, detailFragment);
//        //fragmentTransaction.commit();
//        //objFilterOptionSelectedListener.onFilterOptionSelected();
//    }

    @SuppressLint("CommitTransaction")
    public void SetFragment()
    {

        //DetailFragment detailFragment=new DetailFragment();
        //FragmentManager fragmentManager=getFragmentManager();
        //FragmentTransaction ft=fragmentManager.beginTransaction();
        //recyclerView.setVisibility(View.GONE);
        //fragmentLayout.setVisibility(View.VISIBLE);
        //DetailFragment detailFragment=new DetailFragment();

        //DetailFragment detailFragment=(DetailFragment)getFragmentManager().findFragmentById(R.id.fragment_detail);
        //FragmentTransaction ft=detailFragment.getFragmentManager().beginTransaction();;
        //if(ft==null)
        //{
            //ft=getFragmentManager().beginTransaction();
        //}
//  fragmentTransaction.replace(R.id.tabfragment, detailFragment);
//        fragmentTransaction.commit();
        //if(detailFragment.isHidden())
        //{
       //     ft.show(detailFragment);
        //}
    }
}
