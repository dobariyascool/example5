package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.UserMaster;
import com.arraybit.parser.BusinessJSONParser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"ConstantConditions", "unchecked"})
@SuppressLint("ValidFragment")
public class HotelProfileFragment extends Fragment {

    Toolbar app_bar;
    ViewPager viewPager;
    ImageView ivLogo;
    TabLayout tabLayout;
    BusinessMaster objBusinessMaster;
    PageAdapter pageAdapter;
    UserMaster objUserMaster;
    BusinessJSONParser objBusinessJSONParser;
    Activity activityName;
    CoordinatorLayout hotelProfileFragment;

    public HotelProfileFragment(Activity activityName) {
        this.activityName = activityName;
    }

    public HotelProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_profile, container, false);

        app_bar = (Toolbar) view.findViewById(R.id.app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getResources().getString(R.string.hfProfile));

        setHasOptionsMenu(true);

        hotelProfileFragment = (CoordinatorLayout) view.findViewById(R.id.hotelProfileFragment);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        ivLogo = (ImageView) view.findViewById(R.id.ivLogo);

        pageAdapter = new PageAdapter(getFragmentManager());
        objUserMaster = new UserMaster();

        if (Service.CheckNet(getActivity())) {
            new HotelLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            if(activityName==null){
                getActivity().onBackPressed();
            }else{
            getActivity().getSupportFragmentManager().popBackStack();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (activityName != null) {
            if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
                menu.findItem(R.id.mWaiting).setVisible(false);
            } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName()!=null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName().equals(getActivity().getResources().getString(R.string.title_fragment_guest_options))) {
                menu.findItem(R.id.login).setVisible(false);
                menu.findItem(R.id.registration).setVisible(false);
            } else if (activityName.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
                menu.findItem(R.id.action_search).setVisible(false);
                menu.findItem(R.id.viewChange).setVisible(false);
            }
        }else{
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
        }
    }

    //region Page Adapter
    static class PageAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
    //endregion

    //region LoadingTask
    class HotelLoadingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            objBusinessJSONParser = new BusinessJSONParser();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            objBusinessMaster = objBusinessJSONParser.SelectBusinessMasterByUniqueId(Globals.businessMasterId);
            return objBusinessMaster;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (objBusinessMaster == null) {
                Globals.ShowSnackBar(hotelProfileFragment, getResources().getString(R.string.MsgSelectFail), getActivity(), 1000);
            } else {

                Picasso.with(ivLogo.getContext()).load(objBusinessMaster.getImageName()).into(ivLogo);

                pageAdapter.addFragment(new InformationFragment(objBusinessMaster), "Information");
                pageAdapter.addFragment(new GalleryFragment(objBusinessMaster.getBusinessMasterId()), "Gallery");

                viewPager.setAdapter(pageAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        }
    }
    //endregion
}
