package com.arraybit.pos;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.TableMaster;


@SuppressWarnings("RedundantIfStatement")
public class GuestHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static TableMaster objTableMaster;
    ActionBarDrawerToggle actionBarDrawerToggle;
    String userName;
    LinearLayout guestHomeMainLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    boolean isDualPanel;
    Toolbar app_bar;
    SharePreferenceManage objSharePreferenceManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        //app_bar
        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.app_logo);
        }
        //end

        //linearlayout
        //guestHomeMainLayout = (LinearLayout) findViewById(R.id.guestHomeMainLayout);
        //LinearLayout guestFragmentLayout = (LinearLayout) findViewById(R.id.guestFragmentLayout);
        //Globals.SetScaleImageBackground(GuestHomeActivity.this,guestHomeMainLayout,null,null);
        //end

        //navigationView
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //drawerlayout and actionbardrawertoggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Globals.SetNavigationDrawer(actionBarDrawerToggle, GuestHomeActivity.this, drawerLayout, app_bar);
        //end

        //get username
        ///Intent intent = getIntent();
        //if (intent.getStringExtra("username") != null) {
            //userName = intent.getStringExtra("username");
        //}
        objSharePreferenceManage=new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName",GuestHomeActivity.this) != null){
            userName = objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName",GuestHomeActivity.this);
        }
        else{
            userName = null;
        }

        Intent intent = getIntent();
        objTableMaster = intent.getParcelableExtra("TableMaster");

        AddFragmentInLayout(new GuestOptionListFragment());

        //check layout run in mobile or tablet
//        if (findViewById(R.id.categoryItemFragment) == null) {
//            isDualPanel = false;
//        } else {
//            isDualPanel = true;
//        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) {
        //Globals.SetScaleImageBackground(GuestHomeActivity.this, guestHomeMainLayout, null, null);
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Globals.SetOptionMenu(userName, GuestHomeActivity.this, menu);
        menu.findItem(R.id.home).setVisible(false);
//        if(getSupportFragmentManager().getBackStackEntryCount()!=0){
//            if(getSupportFragmentManager().getBackStackEntryAt(0).getName()!=null) {
//                if (getSupportFragmentManager().getBackStackEntryAt(0).getName().equals(getResources().getString(R.string.title_fragment_detail))) {
//                    app_bar.getMenu().findItem(R.id.action_search).setVisible(false);
//                }
//            }
//        }
//        else
//        {
//            //app_bar.getMenu().findItem(R.id.action_search).setVisible(true);
//        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        Globals.OptionMenuItemClick(item, GuestHomeActivity.this, getSupportFragmentManager());

        return super.onOptionsItemSelected(item);
    }

    //navigationview event
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.changeMode) {
            drawerLayout.closeDrawer(navigationView);
            ChangeModeDialogFragment changeModeDialogFragment = new ChangeModeDialogFragment();
            changeModeDialogFragment.show(getSupportFragmentManager(), "");
        } else if (menuItem.getItemId() == R.id.profile) {
            drawerLayout.closeDrawer(navigationView);
            Globals.InitializeFragment(new HotelProfileFragment(GuestHomeActivity.this), getSupportFragmentManager());
        } else if (menuItem.getItemId() == R.id.offers) {
            drawerLayout.closeDrawer(navigationView);
            Globals.InitializeFragment(new OfferFragment(GuestHomeActivity.this), getSupportFragmentManager());
        } else if (menuItem.getItemId() == R.id.feedback) {
            drawerLayout.closeDrawer(navigationView);
            Globals.InitializeFragment(new FeedbackFragment(GuestHomeActivity.this), getSupportFragmentManager());
        } else if (menuItem.getItemId() == R.id.rate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        }
//        else if (menuItem.getItemId() == R.id.wExit) {
//            System.exit(0);
//        }
        return false;
    }


    //add fragment
    void AddFragmentInLayout(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.guestFragmentLayout, fragment,getResources().getString(R.string.title_fragment_guest_options));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.title_fragment_guest_options));
        fragmentTransaction.commit();
    }
    //end

    //prevent backPressed
    @Override
    public void onBackPressed() {
        //fragment backPressed
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if(getSupportFragmentManager().getBackStackEntryCount() > 1){
                if(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName()!=null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName().equals(getResources().getString(R.string.title_fragment_signup)))
                {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_signup), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                else{
                    getSupportFragmentManager().popBackStack();
                }
            }
        }
    }
    //end
}
