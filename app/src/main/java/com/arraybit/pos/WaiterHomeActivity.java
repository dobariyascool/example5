package com.arraybit.pos;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.arraybit.adapter.OptionListAdapter;
import com.arraybit.global.Globals;

public class WaiterHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OptionListAdapter.OptionListClickListener {

    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_home);

        //app_bar
        Toolbar app_bar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setIcon(R.drawable.likeat_logo);
            getSupportActionBar().setLogo(R.mipmap.app_logo);
        }
        //end

        //linearlayout
        LinearLayout waiterHomeMainLayout=(LinearLayout)findViewById(R.id.waiterHomeMainLayout);
        Globals.SetScaleImageBackground(WaiterHomeActivity.this,waiterHomeMainLayout,null);
        //end

        //navigationView
        NavigationView  navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        //end


        //drawerlayout and actionbardrawertoggle
        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        Globals.SetNavigationDrawer(actionBarDrawerToggle,WaiterHomeActivity.this,drawerLayout,app_bar);
        //end

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

        if(id == R.id.logout){
            Globals.ClearPreference(WaiterHomeActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    //selected event
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

//        if(menuItem.isChecked())
//        {
//            menuItem.setChecked(false);
//        }
//        else
//        {
//            menuItem.setChecked(true);
//        }
        switch(menuItem.getItemId()){

            case R.id.home:
                //Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.login:
                //Toast.makeText(getApplicationContext(), "Starred Selected", Toast.LENGTH_SHORT).show();
                return true;

        }
        return false;
    }

    @Override
    public void onClick(int position) {
        AllOrdersFragment ordersFragment=(AllOrdersFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_all_orders);
        //AllOrdersFragment ordersFragment=new AllOrdersFragment();
        ordersFragment.setFragment(position);
    }
    //end

    //prevent backPressed
    @Override
    public void onBackPressed() {}
    //end
}
