package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CounterMaster;
import com.arraybit.parser.CounterJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
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
        @SuppressLint("InflateParams") View headerView = LayoutInflater.from(WaitingActivity.this).inflate(R.layout.navigation_header, null);
        ImageView ivLogo = (ImageView) headerView.findViewById(R.id.ivLogo);
        ivLogo.setVisibility(View.GONE);
        TextView txtLetter = (TextView) headerView.findViewById(R.id.txtLetter);
        TextView txtName = (TextView) headerView.findViewById(R.id.txtName);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        SetWaiterName(txtName, txtLetter, navigationView);
        navigationView.addHeaderView(headerView);
        navigationView.getMenu().findItem(R.id.wFeedback).setVisible(false);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //relativelayout
        waitingMainLayout = (LinearLayout) findViewById(R.id.waitingMainLayout);
        Globals.SetScaleImageBackground(WaitingActivity.this, waitingMainLayout, null, null);
        //end

        //drawerlayout and actionbardrawertoggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Globals.SetNavigationDrawer(actionBarDrawerToggle, WaitingActivity.this, drawerLayout, app_bar, getSupportFragmentManager());
        //end

        //linear layout
        fragmentLayout = (LinearLayout) findViewById(R.id.fragmentLayout);
        //end

        //add fragment
        AddFragment(new WaitingListFragment());
        //end

        if (Service.CheckNet(WaitingActivity.this)) {
            new CounterLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(waitingMainLayout, getResources().getString(R.string.MsgCheckConnection), WaitingActivity.this, 1000);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
                ReplaceFragment(new AllTablesFragment(WaitingActivity.this, false, null));
            } else {
                item.setTitle("T");
                item.setIcon(R.mipmap.view_table);
                ReplaceFragment(new WaitingListFragment());
            }
        }

        if (id == R.id.logout) {
            Globals.ClearPreference(WaitingActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.wExit) {
            System.exit(0);
        } else if (menuItem.getItemId() == R.id.wChangeMode) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(WaitingActivity.this, WaiterHomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        } else if (menuItem.getItemId() == R.id.wChangeCounter) {
            objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", WaitingActivity.this) != null) {
                drawerLayout.closeDrawer(navigationView);
                Globals.ReplaceFragment(new CounterFragment((short) Globals.UserType.Waiting.getValue()), getSupportFragmentManager(), null);
            }
        } else if (menuItem.getItemId() == R.id.wHotelProfile) {
            drawerLayout.closeDrawer(navigationView);
            Globals.ReplaceFragment(new HotelProfileFragment(WaitingActivity.this), getSupportFragmentManager(), null);
        } else if (menuItem.getItemId() == R.id.wOffers) {
            drawerLayout.closeDrawer(navigationView);
            Globals.ReplaceFragment(new OfferFragment(WaitingActivity.this), getSupportFragmentManager(), null);
        } else if (menuItem.getItemId() == R.id.wRate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        } else if (menuItem.getItemId() == R.id.wAbout) {
            drawerLayout.closeDrawer(navigationView);
            Globals.ReplaceFragment(new AboutUsFragment(), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_about_us));
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
        //end
    }
    //end

    //region Private Methods and Interface
    @SuppressLint("CommitTransaction")
    private void AddFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("CommitTransaction")
    private void ReplaceFragment(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= 21) {
            Fade fade = new Fade();
            fade.setDuration(500);
            fragment.setEnterTransition(fade);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, fragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.fragmentLayout, fragment);
            fragmentTransaction.commit();
        }
    }

    private void SetWaiterName(TextView txtName, TextView txtLetter, NavigationView navigationView) {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", WaitingActivity.this) != null) {
            txtName.setText(objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", WaitingActivity.this));
            txtLetter.setText(objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", WaitingActivity.this).substring(0, 1).toUpperCase());
        }
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterName", WaitingActivity.this) != null) {
            navigationView.getMenu().findItem(R.id.wChangeCounter).setTitle(objSharePreferenceManage.GetPreference("CounterPreference", "CounterName", WaitingActivity.this));
        }
    }
    //endregion

    //region LoadingTask
    class CounterLoadingTask extends AsyncTask {
        ProgressDialog progressDialog;
        short userMasterId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", WaitingActivity.this) != null) {
                userMasterId = Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", WaitingActivity.this));
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            CounterJSONParser objCounterJSONParser = new CounterJSONParser();
            return objCounterJSONParser.SelectAllCounterMaster(Globals.businessMasterId, userMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            ArrayList<CounterMaster> lstCounterMaster = (ArrayList<CounterMaster>) result;
            if (lstCounterMaster != null && lstCounterMaster.size() != 0) {
                if (lstCounterMaster.size() > 1) {
                    navigationView.getMenu().findItem(R.id.wChangeCounter).setEnabled(true);
                } else {
                    navigationView.getMenu().findItem(R.id.wChangeCounter).setEnabled(false);
                }
            }
        }
    }
    //endregion
}
