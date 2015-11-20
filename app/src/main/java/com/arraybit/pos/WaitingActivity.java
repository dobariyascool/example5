package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import com.arraybit.global.SharePreferenceManage;

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
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //relativelayout
        waitingMainLayout = (LinearLayout) findViewById(R.id.waitingMainLayout);
        Globals.SetScaleImageBackground(WaitingActivity.this, waitingMainLayout, null);
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
        Globals.SetScaleImageBackground(WaitingActivity.this, waitingMainLayout, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waiting, menu);
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

        if (id == R.id.mWaiting) {
            if (item.getTitle().equals("T")) {
                item.setTitle("W");
                item.setIcon(R.mipmap.waiting_person);
                ReplaceFragment(new AllTablesFragment());
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
            finish();
        }
        if (menuItem.getItemId() == R.id.wChangeMode) {
            Intent intent = new Intent(WaitingActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
        if (menuItem.getItemId() == R.id.wChangeCounter) {
            objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", WaitingActivity.this) != null) {
                objSharePreferenceManage.RemovePreference("CounterPreference", "CounterMasterId", WaitingActivity.this);
                objSharePreferenceManage.ClearPreference("CounterPreference", WaitingActivity.this);

                Globals.InitializeFragment(new CounterFragment(), getSupportFragmentManager());
            }
        }
        if (menuItem.getItemId() == R.id.wFeedback) {
            drawerLayout.closeDrawer(navigationView);
            navigationView.setVisibility(View.INVISIBLE);

            Globals.InitializeFragment(new FeedbackFragment(), getSupportFragmentManager());
        }
        return false;
    }

    //prevent backPressed
    @Override
    public void onBackPressed() {
        //fragment backPressed
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            Globals.HideKeyBoard(WaitingActivity.this, getCurrentFocus());
            getSupportFragmentManager().popBackStack();

        }
    }
    //end
}
