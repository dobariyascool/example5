package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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

public class WaitingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout fragmentLayout, waitingMainLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SharePreferenceManage objSharePreferenceManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        //app_bar
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.app_logo);
        }
        //end

        //naviagtionview
        View headerView = LayoutInflater.from(WaitingActivity.this).inflate(R.layout.navigation_header, null);
        TextView txtLetter = (TextView) headerView.findViewById(R.id.txtLetter);
        TextView txtName = (TextView) headerView.findViewById(R.id.txtName);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        SetWaiterName(txtName, txtLetter, navigationView);
        navigationView.addHeaderView(headerView);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //relativelayout
        waitingMainLayout = (LinearLayout) findViewById(R.id.waitingMainLayout);
        Globals.SetScaleImageBackground(WaitingActivity.this, waitingMainLayout, null, null);
        //end

        //drawerlayout and actionbardrawertoggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Globals.SetNavigationDrawer(actionBarDrawerToggle, WaitingActivity.this, drawerLayout, app_bar);
        //end

        //linear layout
        fragmentLayout = (LinearLayout) findViewById(R.id.fragmentLayout);
        //end

        //add fragment
        AddFragment(new WaitingListFragment());
        //end

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) {
        Globals.SetScaleImageBackground(WaitingActivity.this, waitingMainLayout, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waiting, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.mWaiting).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
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

        if (id == R.id.mWaiting) {
            if (item.getTitle().equals("T")) {
                item.setTitle("W");
                item.setIcon(R.mipmap.waiting_person);
                ReplaceFragment(new AllTablesFragment(WaitingActivity.this, false));
            } else {
                item.setTitle("T");
                item.setIcon(R.mipmap.view_table);
                ReplaceFragment(new WaitingListFragment());
            }
        }

        if (id == R.id.logout) {
            Globals.ClearPreference(WaitingActivity.this);

           /* objSharePreferenceManage=new SharePreferenceManage();
            objSharePreferenceManage.RemovePreference("WaitingPreference", "UserName",WaitingActivity.this);
            objSharePreferenceManage.ClearPreference("WaitingPreference",WaitingActivity.this);*/
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("CommitTransaction")
    public void AddFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("CommitTransaction")
    public void ReplaceFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.wExit) {
            System.exit(0);
        } else if (menuItem.getItemId() == R.id.wChangeMode) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(WaitingActivity.this, SignInActivity.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.wChangeCounter) {
            drawerLayout.closeDrawer(navigationView);
            objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", WaitingActivity.this) != null) {
                Globals.InitializeFragment(new CounterFragment(), getSupportFragmentManager());
            }
        } else if (menuItem.getItemId() == R.id.wHotelProfile) {
            drawerLayout.closeDrawer(navigationView);
            Globals.InitializeFragment(new HotelProfileFragment(WaitingActivity.this), getSupportFragmentManager());
        } else if (menuItem.getItemId() == R.id.wFeedback) {
            drawerLayout.closeDrawer(navigationView);
            Globals.InitializeFragment(new FeedbackFragment(WaitingActivity.this), getSupportFragmentManager());
        } else if (menuItem.getItemId() == R.id.wOffers) {
            drawerLayout.closeDrawer(navigationView);
            Globals.InitializeFragment(new OfferFragment(WaitingActivity.this), getSupportFragmentManager());
        } else if (menuItem.getItemId() == R.id.wRate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));

            }
        }
        return false;
    }

    private void SetWaiterName(TextView txtName, TextView txtLetter, NavigationView navigationView) {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("WaitingPreference", "UserName", WaitingActivity.this) != null) {
            txtName.setText(objSharePreferenceManage.GetPreference("WaitingPreference", "UserName", WaitingActivity.this));
            txtLetter.setText(objSharePreferenceManage.GetPreference("WaitingPreference", "UserName", WaitingActivity.this).substring(0, 1));
        }
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterName", WaitingActivity.this) != null) {
            if (SplashScreenActivity.counter == 1) {
                navigationView.getMenu().findItem(R.id.wChangeCounter).setVisible(false);
            } else {
                navigationView.getMenu().findItem(R.id.wChangeCounter).setTitle(objSharePreferenceManage.GetPreference("CounterPreference", "CounterName", WaitingActivity.this));
            }

        }
    }

    //prevent backPressed
    @Override
    public void onBackPressed() {
        //fragment backPressed
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            Globals.HideKeyBoard(WaitingActivity.this, getCurrentFocus());
            getSupportFragmentManager().popBackStack();

        }
        //end
    }
    //end
}
