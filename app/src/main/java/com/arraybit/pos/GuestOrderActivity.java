package com.arraybit.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.github.clans.fab.FloatingActionButton;
import com.rey.material.widget.Button;


public class GuestOrderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    ActionBarDrawerToggle actionBarDrawerToggle;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_order);

        //app_bar
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.app_logo);
        }
        //end

        //linearlayout
        LinearLayout guestOrderMainLayout = (LinearLayout) findViewById(R.id.guestOrderMainLayout);
        Globals.SetScaleImageBackground(GuestOrderActivity.this, guestOrderMainLayout, null);
        //end

        //navigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //floating action button
        FloatingActionButton fabAddMore = (FloatingActionButton) findViewById(R.id.fabAddMore);
        //end

        //drawerlayout and actionbardrawertoggle
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Globals.SetNavigationDrawer(actionBarDrawerToggle, GuestOrderActivity.this, drawerLayout, app_bar);
        //end

        //button
        Button btnCancle = (Button) findViewById(R.id.btnCancle);
        //btnAddMore = (Button)findViewById(R.id.btnAddMore);
        Button btnCheckout = (Button) findViewById(R.id.btnCheckOut);
        // btnLogin = (Button)findViewById(R.id.btnLogin);
        //end

        //get intent
        Intent intent = getIntent();
        if (intent.getStringExtra("username") != null) {
            userName = intent.getStringExtra("username");
        }
        //end

        //event
        btnCancle.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);
        fabAddMore.setOnClickListener(this);

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GuestLoginDialogFragment guestLoginDialogFragment=new GuestLoginDialogFragment();
//                guestLoginDialogFragment.show(getSupportFragmentManager(),"");
//            }
//        });
        //end

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guest_order, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Globals.SetOptionMenu(userName, GuestOrderActivity.this, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        Globals.OptionMenuItemClick(item, GuestOrderActivity.this, getSupportFragmentManager());

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        if (menuItem.isChecked()) {
            menuItem.setChecked(false);
        } else {
            menuItem.setChecked(true);
        }
        switch (menuItem.getItemId()) {

            case R.id.home:
                return true;

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancle) {
            finish();
        } else if (v.getId() == R.id.btnCheckOut) {
            ThankYouFragment thankYouFragment = new ThankYouFragment();
            Globals.InitializeFragment(thankYouFragment, getSupportFragmentManager());
        } else if (v.getId() == R.id.fabAddMore) {
            finish();
        }
    }
}
