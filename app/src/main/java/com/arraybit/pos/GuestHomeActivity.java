package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.TableMaster;
import com.rey.material.widget.TextView;

import org.json.JSONException;
import org.json.JSONStringer;


@SuppressWarnings("RedundantIfStatement")
public class GuestHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GuestLoginDialogFragment.LoginResponseListener {

    public static TableMaster objTableMaster;
    public static String userName;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar app_bar;
    View headerView;
    ImageView imageView,ivLogo;
    TextView txtLetter, txtName;
    LinearLayout nameLayout;
    SharePreferenceManage objSharePreferenceManage;

    @SuppressLint("InflateParams")
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
            if(Build.VERSION.SDK_INT >=21){
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        //end

        Intent intent = getIntent();
        objTableMaster = intent.getParcelableExtra("TableMaster");

        //navigationView
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        headerView = LayoutInflater.from(GuestHomeActivity.this).inflate(R.layout.navigation_header, null);
        nameLayout = (LinearLayout)headerView.findViewById(R.id.nameLayout);
        imageView = (ImageView) headerView.findViewById(R.id.imageView);
        ivLogo = (ImageView) headerView.findViewById(R.id.ivLogo);
        txtLetter = (TextView) headerView.findViewById(R.id.txtLetter);
        txtName = (TextView) headerView.findViewById(R.id.txtName);
        SetGuestName();
        navigationView.addHeaderView(headerView);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //drawerlayout and actionbardrawertoggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Globals.SetNavigationDrawer(actionBarDrawerToggle, GuestHomeActivity.this, drawerLayout, app_bar,getSupportFragmentManager());
        //end

        AddFragmentInLayout(new GuestOptionListFragment());
        SaveObjectInPreference();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Globals.SetOptionMenu(Globals.userName, GuestHomeActivity.this, menu);
        menu.findItem(R.id.home).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName()!=null
                && !getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName()
                .equals(getResources().getString(R.string.title_fragment_offer_detail))) {
            Globals.OptionMenuItemClick(item, GuestHomeActivity.this, getSupportFragmentManager());
            SetGuestName();
        }
        return super.onOptionsItemSelected(item);
    }

    //navigationview event
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName()!=null
                && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName()
                .equals(getResources().getString(R.string.title_fragment_guest_options))) {
            if (menuItem.getItemId() == R.id.changeMode) {
                drawerLayout.closeDrawer(navigationView);
                ChangeModeDialogFragment changeModeDialogFragment = new ChangeModeDialogFragment();
                changeModeDialogFragment.show(getSupportFragmentManager(), "");
            } else if (menuItem.getItemId() == R.id.profile) {
                drawerLayout.closeDrawer(navigationView);
                Intent intent = new Intent(GuestHomeActivity.this, HotelProfileActivity.class);
                intent.putExtra("Mode",(short)3);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else if (menuItem.getItemId() == R.id.offers) {
                drawerLayout.closeDrawer(navigationView);
                Intent intent = new Intent(GuestHomeActivity.this, OfferActivity.class);
                intent.putExtra("Mode",(short) 3);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else if (menuItem.getItemId() == R.id.feedback) {
                drawerLayout.closeDrawer(navigationView);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.guestFragmentLayout);
                if (Globals.userName != null) {
                    Globals.ReplaceFragment(new FeedbackFragment(GuestHomeActivity.this), getSupportFragmentManager(), null);
                } else {
                    GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
                    guestLoginDialogFragment.setTargetFragment(currentFragment, 0);
                    guestLoginDialogFragment.show(getSupportFragmentManager(), "");
                }

            } else if (menuItem.getItemId() == R.id.rate) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            } else if (menuItem.getItemId() == R.id.aboutus) {
                drawerLayout.closeDrawer(navigationView);
                Intent intent = new Intent(GuestHomeActivity.this, AboutUsActivity.class);
                intent.putExtra("Mode",(short) 3);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }
        return false;
    }

    @Override
    public void LoginResponse() {
        SetGuestName();
    }

    //prevent backPressed
    @Override
    public void onBackPressed() {
        //fragment backPressed
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_signup))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_signup), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() == null) {
                    getSupportFragmentManager().popBackStack();
                }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_policy))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_policy), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_about_us))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_about_us), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_myaccount))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_myaccount), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    MyAccountFragment.objCustomerMaster = null;
                }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_myprofile))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_myprofile), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_notification_settings))) {
                    NotificationSettingsFragment notificationSettingsFragment = (NotificationSettingsFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_notification_settings));
                    notificationSettingsFragment.CreateNotificationPreference();
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_notification_settings), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_change_password))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_change_password), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_order_summary))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_order_summary), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_offer_detail))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_offer_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_offer))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_offer), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        }
    }
    //end

    //region Private Methods
    private void SaveObjectInPreference() {
        objSharePreferenceManage = new SharePreferenceManage();
        try {
            JSONStringer jsonStringer = new JSONStringer();
            jsonStringer.object();
            jsonStringer.key("TableMasterId").value(objTableMaster.getTableMasterId());
            jsonStringer.key("TableName").value(objTableMaster.getTableName());
            jsonStringer.key("ShortName").value(objTableMaster.getShortName());
            jsonStringer.key("linktoTableStatusMasterId").value(objTableMaster.getlinktoTableStatusMasterId());
            jsonStringer.key("linktoOrderTypeMasterId").value(objTableMaster.getlinktoOrderTypeMasterId());
            jsonStringer.key("linktoSectionMasterId").value(objTableMaster.getlinktoSectionMasterId());
            jsonStringer.key("linktoBusinessMasterId").value(objTableMaster.getlinktoBusinessMasterId());
            jsonStringer.endObject();
            objSharePreferenceManage.CreatePreference("GuestModePreference", "GuestMode", jsonStringer.toString(), GuestHomeActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void AddFragmentInLayout(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.guestFragmentLayout, fragment, getResources().getString(R.string.title_fragment_guest_options));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.title_fragment_guest_options));
        fragmentTransaction.commit();
    }

    private void SetGuestName() {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", GuestHomeActivity.this) != null) {
            Globals.userName = objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", GuestHomeActivity.this);
        } else {
            Globals.userName = null;
        }
        if (Globals.userName != null) {
            ivLogo.setVisibility(View.GONE);
            nameLayout.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            txtName.setVisibility(View.VISIBLE);
            txtLetter.setVisibility(View.VISIBLE);

            txtName.setText(Globals.userName);
            txtLetter.setText(Globals.userName.substring(0, 1).toUpperCase());
        } else {
            ivLogo.setVisibility(View.VISIBLE);
            nameLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            txtName.setVisibility(View.GONE);
            txtLetter.setVisibility(View.GONE);
        }
    }

    public void EditTextOnClick(View view) {
        GuestProfileFragment guestProfileFragment = (GuestProfileFragment)getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_myprofile));
        guestProfileFragment.EditTextOnClick();
    }
    //endregion
}
