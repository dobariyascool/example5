package com.arraybit.pos;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotelFragment extends Fragment {

    Toolbar app_bar;
    ViewPager viewPager;
    ImageView ivLogo;
    FragmentTransaction fragmentTransaction;
    TabLayout tabLayout;
    String imageRetrivePath;

    public HotelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_hotel,container,false);

        tabLayout=(TabLayout)view.findViewById(R.id.tabLayout);
        viewPager=(ViewPager)view.findViewById(R.id.viewPager);
        ivLogo=(ImageView)view.findViewById(R.id.ivLogo);

        return view;
    }


}
