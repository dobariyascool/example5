package com.arraybit.pos;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;

public class WaiterOrderDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    LinearLayout waiterOrderMainLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_order_detail);

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
        waiterOrderMainLayout = (LinearLayout) findViewById(R.id.waiterOrderMainLayout);
        Globals.SetScaleImageBackground(WaiterOrderDetailActivity.this, waiterOrderMainLayout, null);
        //end

        //navigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //drawerlayout and actionbardrawertoggle
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Globals.SetNavigationDrawer(actionBarDrawerToggle, WaiterOrderDetailActivity.this, drawerLayout, app_bar);
        //end


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) {
        Globals.SetScaleImageBackground(WaiterOrderDetailActivity.this, waiterOrderMainLayout, null);
        //}
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
            Globals.ClearPreference(WaiterOrderDetailActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    //selected event
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.home) {
            return true;
        }
        if (menuItem.getItemId() == R.id.login) {
            return true;
        }
        if (menuItem.getItemId() == R.id.wChangeCounter) {
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", WaiterOrderDetailActivity.this) != null) {
                objSharePreferenceManage.RemovePreference("CounterPreference", "CounterMasterId", WaiterOrderDetailActivity.this);
                objSharePreferenceManage.ClearPreference("CounterPreference", WaiterOrderDetailActivity.this);

                Globals.InitializeFragment(new CounterFragment(), getSupportFragmentManager());
            }
        }
        return false;
    }

}
