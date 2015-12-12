package com.arraybit.pos;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.rey.material.widget.TextView;

public class WaiterHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout waiterHomeMainLayout;
    boolean isDualPanel;
    SharePreferenceManage objSharePreferenceManage;

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
        LinearLayout waiterFragmentLayout = (LinearLayout) findViewById(R.id.waiterFragmentLayout);
        Globals.SetScaleImageBackground(WaiterHomeActivity.this, waiterHomeMainLayout, null,null);
        //end

        //navigationView
        View headerView = LayoutInflater.from(WaiterHomeActivity.this).inflate(R.layout.navigation_header,null);
        TextView txtLetter = (TextView)headerView.findViewById(R.id.txtLetter);
        TextView txtName = (TextView)headerView.findViewById(R.id.txtName);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        SetWaiterName(txtName,txtLetter,navigationView);
        navigationView.addHeaderView(headerView);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //drawerlayout and actionbardrawertoggle
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
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
        Globals.SetScaleImageBackground(WaiterHomeActivity.this, waiterHomeMainLayout, null,null);
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

        if (menuItem.getItemId() == R.id.home) {
            return true;
        }
        if (menuItem.getItemId() == R.id.login) {
            return true;
        }
        if (menuItem.getItemId() == R.id.wChangeCounter) {
            objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", WaiterHomeActivity.this) != null) {
                objSharePreferenceManage.RemovePreference("CounterPreference", "CounterMasterId", WaiterHomeActivity.this);
                objSharePreferenceManage.ClearPreference("CounterPreference", WaiterHomeActivity.this);

                Globals.InitializeFragment(new CounterFragment(), getSupportFragmentManager());
            }
        }
        return false;
    }

    //add fragment
    void AddFragmentInLayout(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.waiterFragmentLayout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    //end

    //prevent backPressed
    @Override
    public void onBackPressed() {
            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

                    if(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName()!=null){

                        if(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName().equals(getResources().getString(R.string.title_fragment_order_detail)))
                        {
                            getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_order_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                        else if(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName().equals(getResources().getString(R.string.title_fragment_detail))){

                            getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    }
                    else
                    {
                        CategoryItemFragment.i=0;
                        CategoryItemFragment.isViewChange=false;
                        getSupportFragmentManager().popBackStack();

                    }
                }
            }
        }
    //end

    private void SetWaiterName(TextView txtName,TextView txtLetter,NavigationView navigationView){
        objSharePreferenceManage = new SharePreferenceManage();
        if(objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", WaiterHomeActivity.this)!=null){
            txtName.setText(objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", WaiterHomeActivity.this));
            txtLetter.setText(objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", WaiterHomeActivity.this).substring(0,1));
        }
        if(objSharePreferenceManage.GetPreference("CounterPreference", "CounterName", WaiterHomeActivity.this)!=null){
            if(SplashScreenActivity.counter==1){
                navigationView.getMenu().findItem(R.id.wChangeCounter).setVisible(false);
            }
            else {
                navigationView.getMenu().findItem(R.id.wChangeCounter).setTitle(objSharePreferenceManage.GetPreference("CounterPreference", "CounterName", WaiterHomeActivity.this));
            }

        }
    }
}


//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public void WindowAnimation(){
//        Fade fade = new Fade();
//        fade.setDuration(1000);
//        getWindow().setEnterTransition(fade);
//    }

