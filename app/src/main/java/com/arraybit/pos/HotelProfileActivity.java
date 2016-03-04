package com.arraybit.pos;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
public class HotelProfileActivity extends AppCompatActivity {

    short mode;
    ViewPager viewPager;
    ImageView ivLogo;
    TabLayout tabLayout;
    BusinessMaster objBusinessMaster;
    PageAdapter pageAdapter;
    UserMaster objUserMaster;
    BusinessJSONParser objBusinessJSONParser;
    CoordinatorLayout hotelProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hotel_profile);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent getData = getIntent();
        mode = getData.getShortExtra("Mode", (short) 0);

        hotelProfileFragment = (CoordinatorLayout) findViewById(R.id.hotelProfileFragment);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);

        pageAdapter = new PageAdapter(getSupportFragmentManager());
        objUserMaster = new UserMaster();

        if (Service.CheckNet(this)) {
            new HotelLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(hotelProfileFragment, getResources().getString(R.string.MsgCheckConnection), this, 1000);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mode == 1) {
            menu.findItem(R.id.viewChange).setVisible(false);
        } else if (mode == 2) {
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
        } else if (mode == 3) {
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.registration).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (mode == 1 || mode == 2) {
            getMenuInflater().inflate(R.menu.menu_waiter_home, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_home, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            GalleryFragment.alBusinessGalleryTran = null;
            InformationFragment.lstBusinessHoursTran = null;
            overridePendingTransition(0, R.anim.right_exit);
        }

        if (mode == 1 || mode == 2) {
            if (id == R.id.logout) {
                Globals.ClearPreference(HotelProfileActivity.this);
            }
        } else {
            Globals.OptionMenuItemClick(item, HotelProfileActivity.this, getSupportFragmentManager());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
            GalleryFragment.alBusinessGalleryTran = null;
            InformationFragment.lstBusinessHoursTran = null;
            overridePendingTransition(0, R.anim.right_exit);
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
                Globals.ShowSnackBar(hotelProfileFragment, getResources().getString(R.string.MsgSelectFail), HotelProfileActivity.this, 1000);
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

    //region Animation Commented Code
//    @SuppressLint("RtlHardcoded")
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void setupWindowAnimations() {
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            Transition slideTransition = new Slide(Gravity.RIGHT);
//            slideTransition.setDuration(500);
//            slideTransition.setInterpolator(new AccelerateDecelerateInterpolator());
//            getWindow().setEnterTransition(slideTransition);
//        }
//    }
    //endregion

}
