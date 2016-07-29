package com.arraybit.pos;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.UserMaster;
import com.arraybit.parser.BusinessJSONParser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class HotelProfileActivity extends AppCompatActivity {

    short mode;
    ViewPager viewPager;
    ImageView ivLogo, ivBackground;
    TabLayout tabLayout;
    BusinessMaster objBusinessMaster;
    PageAdapter pageAdapter;
    UserMaster objUserMaster;
    BusinessJSONParser objBusinessJSONParser;
    CoordinatorLayout hotelProfileFragment;
    CollapsingToolbarLayout collapsingToolbar;
    SharePreferenceManage sharePreferenceManage;

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
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivBackground = (ImageView) findViewById(R.id.ivBackground);

        pageAdapter = new PageAdapter(getSupportFragmentManager());
        objUserMaster = new UserMaster();

        if (Globals.objAppThemeMaster != null) {
            sharePreferenceManage = new SharePreferenceManage();
            String encodedImage = sharePreferenceManage.GetPreference("GuestAppTheme", getString(R.string.encodedProfileImage), HotelProfileActivity.this);
            byte[] decodedString = Base64.decode(encodedImage.getBytes(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivBackground.setImageDrawable(new BitmapDrawable(getResources(), decodedByte));
            ivBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            Picasso.with(HotelProfileActivity.this).load().fit().centerCrop().into(ivBackground);
        } else {
            Picasso.with(HotelProfileActivity.this).load(R.drawable.profile_background).fit().centerCrop().into(ivBackground);
        }

        if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {
            collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.primary));
            tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.accent));
            tabLayout.setTabTextColors(ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.white)));
        }

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
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.notification_layout).setVisible(false);
        } else if (mode == 2) {
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.notification_layout).setVisible(false);
        } else if (mode == 3) {
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.shortList).setVisible(false);
            menu.findItem(R.id.callWaiter).setVisible(false);
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
            setResult(RESULT_OK);
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
            setResult(RESULT_OK);
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

                Glide.with(HotelProfileActivity.this).load(objBusinessMaster.getImageName()).asBitmap().override(150, 150).centerCrop().into(new BitmapImageViewTarget(ivLogo) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ivLogo.setImageDrawable(circularBitmapDrawable);
                    }
                });

                pageAdapter.addFragment(new InformationFragment(objBusinessMaster), "Information");
                pageAdapter.addFragment(new GalleryFragment(objBusinessMaster.getBusinessMasterId()), "Gallery");
                pageAdapter.addFragment(new BusinessInformationFragment(), "BusinessInformation");

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
