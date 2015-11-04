package com.arraybit.pos;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.parser.CategoryJSONParser;
import com.github.clans.fab.FloatingActionMenu;
import com.rey.material.widget.Button;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class GuestHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static boolean isCheck = false;
    ActionBarDrawerToggle actionBarDrawerToggle;
    String userName;
    LinearLayout guestHomeMainLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CategoryJSONParser objCategoryJSONParser;
    ItemPagerAdapter itemPagerAdapter;

    ViewPager itemViewPager;
    TabLayout itemTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        //app_bar
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.app_logo);
        }
        //end

        //linearlayout
        guestHomeMainLayout = (LinearLayout) findViewById(R.id.guestHomeMainLayout);
        Globals.SetScaleImageBackground(GuestHomeActivity.this, guestHomeMainLayout, null);
        //end

        //navigationView
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //drawerlayout and actionbardrawertoggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Globals.SetNavigationDrawer(actionBarDrawerToggle, GuestHomeActivity.this, drawerLayout, app_bar);
        //end

        //floating action menu
        FloatingActionMenu famRoot = (FloatingActionMenu) findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);
        //end

        //tab layout
        itemTabLayout = (TabLayout) findViewById(R.id.itemTabLayout);
        //end

        //view page
        itemViewPager = (ViewPager) findViewById(R.id.itemViewPager);

        //imagebutton
        ImageButton ibViewChange = (ImageButton) findViewById(R.id.ibViewChange);
        //end

        //get username
        Intent intent = getIntent();
        if (intent.getStringExtra("username") != null) {
            userName = intent.getStringExtra("username");
        }
//      else {
//            navigationView.getMenu().findItem(R.id.registration).setVisible(true);
//            navigationView.getMenu().findItem(R.id.login).setVisible(true);
//        }
        //end

        //button
        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
        //end

        //event
        btnConfirm.setOnClickListener(this);
        ibViewChange.setOnClickListener(this);
        //end

        new GuestHomeCategoryLodingTask().execute();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) {
        Globals.SetScaleImageBackground(GuestHomeActivity.this, guestHomeMainLayout, null);
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Globals.SetOptionMenu(userName, GuestHomeActivity.this, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        Globals.OptionMenuItemClick(item, GuestHomeActivity.this, getSupportFragmentManager());

        return super.onOptionsItemSelected(item);
    }

    //navigationview event
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

//        if (menuItem.isChecked()) {
//            menuItem.setChecked(false);
//        } else {
//            menuItem.setChecked(true);
//        }
        switch (menuItem.getItemId()) {

            case R.id.home:
                break;

            case R.id.profile:
                Globals.InitializeFragment(new HotelFragment(), getSupportFragmentManager());
                drawerLayout.closeDrawer(navigationView);
                navigationView.setVisibility(View.INVISIBLE);
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnConfirm) {
            Intent intent = new Intent(GuestHomeActivity.this, GuestOrderActivity.class);
            intent.putExtra("username", userName);
            startActivity(intent);
        } else if (v.getId() == R.id.ibViewChange) {
            if (isCheck) {
                v.setSelected(false);
                isCheck = false;
            } else {
                v.setSelected(true);
                isCheck = true;
            }
        }
    }
    //end

    //prevent backPressed
    @Override
    public void onBackPressed() {
        //fragment backPressed
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
        }
    }
    //end

    //pager adapter
    static class ItemPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ItemPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void AddFragment(Fragment fragment, String title) {
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
    //end

    public class GuestHomeCategoryLodingTask extends AsyncTask {
        ArrayList<CategoryMaster> alCategoryMaster;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objCategoryJSONParser = new CategoryJSONParser();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            alCategoryMaster = new ArrayList<CategoryMaster>();

            alCategoryMaster = objCategoryJSONParser.SelectAllCategoryMaster();
            return alCategoryMaster;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            itemPagerAdapter = new ItemPagerAdapter(getSupportFragmentManager());

            for (int i = 0; i < alCategoryMaster.size(); i++) {
                itemPagerAdapter.AddFragment(com.arraybit.pos.ItemTabFragment.createInstance((CategoryMaster) alCategoryMaster.get(i)), alCategoryMaster.get(i).getCategoryName());
            }

            itemViewPager.setAdapter(itemPagerAdapter);
            itemTabLayout.setupWithViewPager(itemViewPager);
        }
    }

}
