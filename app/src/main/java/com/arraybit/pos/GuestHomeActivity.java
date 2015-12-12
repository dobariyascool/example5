package com.arraybit.pos;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;


@SuppressWarnings("RedundantIfStatement")
public class GuestHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle actionBarDrawerToggle;
    String userName;
    LinearLayout guestHomeMainLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    boolean isDualPanel;
    Toolbar app_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        //app_bar
        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.app_logo);
        }
        //end

        //linearlayout
        guestHomeMainLayout = (LinearLayout) findViewById(R.id.guestHomeMainLayout);
        //LinearLayout guestFragmentLayout = (LinearLayout) findViewById(R.id.guestFragmentLayout);
        Globals.SetScaleImageBackground(GuestHomeActivity.this,guestHomeMainLayout,null,null);
        //end

        //navigationView
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //drawerlayout and actionbardrawertoggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Globals.SetNavigationDrawer(actionBarDrawerToggle, GuestHomeActivity.this, drawerLayout, app_bar);
        //end

        //get username
        Intent intent = getIntent();
        if (intent.getStringExtra("username") != null) {
            userName = intent.getStringExtra("username");
        }

        AddFragmentInLayout(new GuestOptionListFragment());

        //check layout run in mobile or tablet
//        if (findViewById(R.id.categoryItemFragment) == null) {
//            isDualPanel = false;
//        } else {
//            isDualPanel = true;
//        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) {
        Globals.SetScaleImageBackground(GuestHomeActivity.this, guestHomeMainLayout, null, null);
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
        if(getSupportFragmentManager().getBackStackEntryCount()!=0){
            if(getSupportFragmentManager().getBackStackEntryAt(0).getName()!=null) {
                if (getSupportFragmentManager().getBackStackEntryAt(0).getName().equals(getResources().getString(R.string.title_fragment_detail))) {
                    app_bar.getMenu().findItem(R.id.action_search).setVisible(false);
                }
            }
        }
        else
        {
            //app_bar.getMenu().findItem(R.id.action_search).setVisible(true);
        }
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
        switch (menuItem.getItemId()) {

            case R.id.home:
                break;

            case R.id.profile:
                Globals.InitializeFragment(new HotelProfileFragment(GuestHomeActivity.this), getSupportFragmentManager());
                drawerLayout.closeDrawer(navigationView);
                navigationView.setVisibility(View.INVISIBLE);
                break;
        }

        return false;
    }


    //add fragment
    void AddFragmentInLayout(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.guestFragmentLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    //end

    //prevent backPressed
    @Override
    public void onBackPressed() {
        //fragment backPressed
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if(getSupportFragmentManager().getBackStackEntryCount() > 1){
                if(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName()!=null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName().equals(getResources().getString(R.string.title_fragment_signup)))
                {
                        getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_signup), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        }
    }
    //end
}
