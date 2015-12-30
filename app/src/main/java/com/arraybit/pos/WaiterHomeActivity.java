package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressLint("InflateParams")
public class WaiterHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout waiterHomeMainLayout;
    boolean isDualPanel;
    SharePreferenceManage objSharePreferenceManage;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_home);

        //app_bar
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setIcon(R.drawable.likeat_logo);
            getSupportActionBar().setLogo(R.mipmap.app_logo);
        }
        //end

        //linearlayout
        waiterHomeMainLayout = (LinearLayout) findViewById(R.id.waiterHomeMainLayout);
        //LinearLayout waiterFragmentLayout = (LinearLayout) findViewById(R.id.waiterFragmentLayout);
        //Globals.SetScaleImageBackground(WaiterHomeActivity.this, waiterHomeMainLayout, null, null);
        //end

        //navigationView
        View headerView = LayoutInflater.from(WaiterHomeActivity.this).inflate(R.layout.navigation_header, null);
        TextView txtLetter = (TextView) headerView.findViewById(R.id.txtLetter);
        TextView txtName = (TextView) headerView.findViewById(R.id.txtName);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        SetWaiterName(txtName, txtLetter, navigationView);
        navigationView.addHeaderView(headerView);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //drawerlayout and actionbardrawertoggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Globals.SetNavigationDrawer(actionBarDrawerToggle, WaiterHomeActivity.this, drawerLayout, app_bar);
        //end

        // if (waiterHomeMainLayout.findViewById(R.id.fragment_waiter_option_list) == null) {

        // isDualPanel = false;
        AddFragmentInLayout(new WaiterOptionListFragment());
        // } else {
        //  isDualPanel = true;
        //}
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) {
        //Globals.SetScaleImageBackground(WaiterHomeActivity.this, waiterHomeMainLayout, null, null);
        //}
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.viewChange).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waiter_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.logout) {
            Globals.ClearPreference(WaiterHomeActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    //selected event
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.wChangeCounter) {
            objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", WaiterHomeActivity.this) != null) {
                drawerLayout.closeDrawer(navigationView);
                Globals.InitializeFragment(new CounterFragment(), getSupportFragmentManager());
            }
        } else if (menuItem.getItemId() == R.id.wChangeMode) {
            drawerLayout.closeDrawer(navigationView);
            ChangeModeDialogFragment changeModeDialogFragment = new ChangeModeDialogFragment();
            changeModeDialogFragment.show(getSupportFragmentManager(), "");
        } else if (menuItem.getItemId() == R.id.wHotelProfile) {
            drawerLayout.closeDrawer(navigationView);
            Globals.InitializeFragment(new HotelProfileFragment(WaiterHomeActivity.this), getSupportFragmentManager());
        } else if (menuItem.getItemId() == R.id.wOffers) {
            drawerLayout.closeDrawer(navigationView);
            Globals.InitializeFragment(new OfferFragment(WaiterHomeActivity.this), getSupportFragmentManager());
        } else if (menuItem.getItemId() == R.id.wFeedback) {
            drawerLayout.closeDrawer(navigationView);
            Globals.InitializeFragment(new FeedbackFragment(WaiterHomeActivity.this), getSupportFragmentManager());
        } else if (menuItem.getItemId() == R.id.wRate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));

            }
        } else if (menuItem.getItemId() == R.id.wExit) {
            System.exit(0);
        }
        return false;
    }


    //add fragment
    void AddFragmentInLayout(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.waiterFragmentLayout, fragment, getResources().getString(R.string.title_fragment_waiter_options));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.title_fragment_waiter_options));
        fragmentTransaction.commit();
    }
    //end

    //prevent backPressed
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_order_detail))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_order_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_detail))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_all_orders))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_all_orders), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_cart_item))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_order_summary))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_all_tables), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName()!=null
                        &&getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_all_tables))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_all_tables), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else{
                    CategoryItemFragment.i = 0;
                    CategoryItemFragment.isViewChange = false;
                    getSupportFragmentManager().popBackStack();
                    Globals.counter = 0;
                    Globals.alOrderItemTran = new ArrayList<>();
                    Globals.targetFragment = null;
                }
            }
        }
    }
    //end

    private void SetWaiterName(TextView txtName, TextView txtLetter, NavigationView navigationView) {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", WaiterHomeActivity.this) != null) {
            txtName.setText(objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", WaiterHomeActivity.this));
            txtLetter.setText(objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", WaiterHomeActivity.this).substring(0, 1));
        }
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterName", WaiterHomeActivity.this) != null) {
            if (SplashScreenActivity.counter == 1) {
                navigationView.getMenu().findItem(R.id.wChangeCounter).setVisible(false);
            } else {
                navigationView.getMenu().findItem(R.id.wChangeCounter).setTitle(objSharePreferenceManage.GetPreference("CounterPreference", "CounterName", WaiterHomeActivity.this));
            }

        }
        navigationView.getMenu().findItem(R.id.wChangeMode).setVisible(true);
    }
}