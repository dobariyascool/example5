package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.arraybit.parser.WaitingJSONParser;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class WaitingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WaitingStatusFragment.UpdateStatusListener, ConfirmDialog.ConfirmationResponseListener {

    public static boolean isTableAssign = false;
    LinearLayout fragmentLayout, waitingMainLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SharePreferenceManage objSharePreferenceManage;
    Toolbar app_bar;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        //app_bar
        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.drawable.central_logo);
        }
        //end

        if (Service.CheckNet(WaitingActivity.this)) {
            new SettingLoadingTask().execute();
        }

        Globals.userType = Globals.UserType.Waiting.getValue();

        //naviagtionview
        @SuppressLint("InflateParams") View headerView = LayoutInflater.from(WaitingActivity.this).inflate(R.layout.navigation_header, null);
        LinearLayout llNavHeader = (LinearLayout) headerView.findViewById(R.id.llNavHeader);
        llNavHeader.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.primary_black)));
        ImageView ivLogo = (ImageView) headerView.findViewById(R.id.ivLogo);
        ivLogo.setVisibility(View.GONE);
        TextView txtLetter = (TextView) headerView.findViewById(R.id.txtLetter);
        TextView txtName = (TextView) headerView.findViewById(R.id.txtName);
        CompoundButton cbLogout = (CompoundButton) headerView.findViewById(R.id.cbLogout);
        cbLogout.setVisibility(View.VISIBLE);
        txtLetter.setTextColor(ContextCompat.getColor(this, android.R.color.white));

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        SetWaiterName(txtName, txtLetter, navigationView);
        navigationView.addHeaderView(headerView);
        navigationView.getMenu().findItem(R.id.wFeedback).setVisible(false);
        navigationView.getMenu().findItem(R.id.wNotification).setVisible(false);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //relativelayout
        waitingMainLayout = (LinearLayout) findViewById(R.id.waitingMainLayout);
        waitingMainLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.background_img));
//        Globals.SetScaleImageBackground(WaitingActivity.this, waitingMainLayout, null, null);
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

        cbLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.ClearPreference(WaitingActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waiting, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.mWaiting).setVisible(true);
        menu.findItem(R.id.logout).setVisible(false);
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
                item.setIcon(R.mipmap.waiting_list);
//                Drawable drawable = item.getIcon();
//                drawable = DrawableCompat.wrap(drawable);
//                DrawableCompat.setTint(drawable, ContextCompat.getColor(this, android.R.color.white));
//                item.setIcon(drawable);
                ReplaceFragment(new AllTablesFragment(WaitingActivity.this, false, null));
            } else {
                item.setTitle("T");
                item.setIcon(R.mipmap.view_table);
                ReplaceFragment(new WaitingListFragment());
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.wExit) {
            System.exit(0);
        } else if (menuItem.getItemId() == R.id.wChangeMode) {
            ConfirmDialog confirmDialog = new ConfirmDialog(getResources().getString(R.string.MsgChangeMode), true);
            confirmDialog.show(getSupportFragmentManager(), "");
        } else if (menuItem.getItemId() == R.id.wChangeCounter) {
            objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", WaitingActivity.this) != null) {
                drawerLayout.closeDrawer(navigationView);
                CounterFragment counterFragment = new CounterFragment((short) Globals.UserType.Waiting.getValue());
                Bundle bundle = new Bundle();
                bundle.putBoolean("isBack", true);
                counterFragment.setArguments(bundle);
                Globals.ReplaceFragment(counterFragment, getSupportFragmentManager(), null);
            }
        } else if (menuItem.getItemId() == R.id.wHotelProfile) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(WaitingActivity.this, HotelProfileActivity.class);
            intent.putExtra("Mode", (short) 1);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else if (menuItem.getItemId() == R.id.wOffers) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(WaitingActivity.this, OfferActivity.class);
            intent.putExtra("Mode", (short) 1);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
            Intent intent = new Intent(WaitingActivity.this, AboutUsActivity.class);
            intent.putExtra("Mode", (short) 1);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        return false;
    }

    @Override
    public void UpdateStatus(boolean flag) {
        if (flag) {
            app_bar.getMenu().findItem(R.id.mWaiting).setTitle("W").setIcon(R.mipmap.waiting_list);
        } else {
            app_bar.getMenu().findItem(R.id.mWaiting).setTitle("T").setIcon(R.mipmap.view_table);
            ReplaceFragment(new WaitingListFragment());
            Globals.ShowSnackBar(waitingMainLayout, getResources().getString(R.string.MsgTableAssign), WaitingActivity.this, 3000);
        }
    }

    @Override
    public void ConfirmResponse() {
        Globals.EnableBroadCastReceiver(WaitingActivity.this);
        Intent intent = new Intent(WaitingActivity.this, WaiterHomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }

    //prevent backPressed
    @Override
    public void onBackPressed() {
        //fragment backPressed
        Globals.HideKeyBoard(WaitingActivity.this, getCurrentFocus());
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_all_tables))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_all_tables), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            if (getSupportFragmentManager().findFragmentById(R.id.fragmentLayout) instanceof AllTablesFragment) {
                menu.getItem(0).setIcon(ContextCompat.getDrawable(WaitingActivity.this,R.mipmap.view_table));
                menu.getItem(0).setTitle("T");
                ReplaceFragment(new WaitingListFragment());
            }
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
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        fragmentTransaction.replace(R.id.fragmentLayout, fragment);
        fragmentTransaction.commit();
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

    private void ConfirmMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WaitingActivity.this);
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.MsgChangeMode))
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Globals.EnableBroadCastReceiver(WaitingActivity.this);
                                Globals.CallNotificationReceiver(WaitingActivity.this);
                                Intent intent = new Intent(WaitingActivity.this, WaiterHomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }


    //endregion

    //region LoadingTask
    class CounterLoadingTask extends AsyncTask {
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

    class SettingLoadingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objSharePreferenceManage = new SharePreferenceManage();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            WaitingJSONParser objWaitingJSONParser = new WaitingJSONParser();
            isTableAssign = objWaitingJSONParser.SelectTableAssignmentSettingById(String.valueOf(Globals.businessMasterId), String.valueOf(Globals.SettingMasterId));
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            objSharePreferenceManage.CreatePreference("Waiting", "IsTableAssign", String.valueOf(isTableAssign), WaitingActivity.this);
        }
    }
    //endregion
}
