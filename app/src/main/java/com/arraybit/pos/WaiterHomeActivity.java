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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.arraybit.adapter.OptionListAdapter;
import com.arraybit.adapter.OrdersAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;

public class WaiterHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OptionListAdapter.OptionListClickListener,OrdersAdapter.OrderLayoutClickListener{

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
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
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

    //all options list click event
    @Override
    public void onClick(int position) {
        //if (isDualPanel) {
        //    AllOrdersFragment ordersFragment = (AllOrdersFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_all_orders);
            //AllOrdersFragment ordersFragment=new AllOrdersFragment();
         //   ordersFragment.setFragment(position);
        //} else {
            if(getSupportFragmentManager().getBackStackEntryCount() == 1) {
                if (position == 0) {
                    Globals.InitializeFragment(new AllOrdersFragment(), getSupportFragmentManager());
                    //AddFragmentInLayout(new AllOrdersFragment());
                } else if (position == 1) {
                    Globals.InitializeFragment(new AllTablesFragment(WaiterHomeActivity.this), getSupportFragmentManager());
                    //AddFragmentInLayout(new AllTablesFragment());
                } else if (position == 2) {
                    Globals.InitializeFragment(new CategoryItemFragment(WaiterHomeActivity.this), getSupportFragmentManager());
                }else if(position == 3){
//                    Intent intent = new Intent(WaiterHomeActivity.this,GuestHomeActivity.class);
//                    startActivity(intent);
                }
            }
       //}
    }
    //end

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
        //if (!isDualPanel) {
            System.out.println("count "+getSupportFragmentManager().getBackStackEntryCount());
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
                        getSupportFragmentManager().popBackStack();
                    }
                }
            }
        }

    //particular order click event
    @Override
    public void OrderLayoutClick(int orderMasterId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.allOrdersFragment, new OrderDetailFragment(orderMasterId),getResources().getString(R.string.title_fragment_order_detail));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.title_fragment_order_detail));
        fragmentTransaction.commit();
        //Globals.InitializeFragment(new OrderDetailFragment(),getSupportFragmentManager());
    }
    //end
}


//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public void WindowAnimation(){
//        Fade fade = new Fade();
//        fade.setDuration(1000);
//        getWindow().setEnterTransition(fade);
//    }

