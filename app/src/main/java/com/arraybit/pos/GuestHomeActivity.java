package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.TableMaster;
import com.google.gson.Gson;
import com.rey.material.widget.TextView;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@SuppressWarnings({"RedundantIfStatement", "ResourceType"})
public class GuestHomeActivity extends AppCompatActivity implements GuestLoginDialogFragment.LoginResponseListener, NavigationView.OnNavigationItemSelectedListener {

    public static TableMaster objTableMaster;
    public static String userName;
    public static boolean isMenuMode;
    public static boolean isGuestMode = false;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar app_bar;
    View headerView;
    ImageView imageView, ivLogo;
    TextView txtLetter, txtName;
    LinearLayout nameLayout;
    LinearLayout llNavHeader;
    boolean isShowMessage;
    LinearLayout errorLayout;
    RelativeLayout footerLayout;
    com.rey.material.widget.TextView txtCartNumber;
    RelativeLayout relativeLayout;
    boolean isBackPressed = false, isRestart;

    SharePreferenceManage objSharePreferenceManage;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        //app_bar
        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);

        this.setTheme(R.style.AppThemeGuest);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.drawable.central_logo);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        //end

        footerLayout = (RelativeLayout) findViewById(R.id.footerLayout);
        footerLayout.setVisibility(View.GONE);

        Intent intent = getIntent();
        isMenuMode = intent.getBooleanExtra("IsMenuMode", false);
        if (isMenuMode) {
            Globals.orderTypeMasterId = (short) intent.getIntExtra("linktoOrderTypeMasterId", 0);
            if (app_bar != null) {
                Globals.SetToolBarBackground(this, app_bar, ContextCompat.getColor(this, R.color.primary), ContextCompat.getColor(this, android.R.color.white));
                getSupportActionBar().setTitle(Globals.orderTypeMasterId == Globals.OrderType.DineIn.getValue() ? getResources().getString(R.string.title_activity_home) + " - Dine In" : getResources().getString(R.string.title_activity_home) + " - Take Away");
            }
        } else {
            isGuestMode = true;
            objTableMaster = intent.getParcelableExtra("TableMaster");
            MenuActivity.objTableMaster = GuestHomeActivity.objTableMaster;
            if (objTableMaster != null && objTableMaster.getlinktoOrderTypeMasterId() != 0) {
                Globals.orderTypeMasterId = objTableMaster.getlinktoOrderTypeMasterId();
                if (app_bar != null) {
//                    if (isGuestMode) {
                    if (Globals.objAppThemeMaster != null) {
//                            Globals.SetToolBarBackground(this, app_bar, Globals.objAppThemeMaster.getColorPrimary(), ContextCompat.getColor(this, android.R.color.white));
//                        } else {
                        Globals.SetToolBarBackground(this, app_bar, ContextCompat.getColor(this, R.color.primary), ContextCompat.getColor(this, android.R.color.white));
//                        }
                    } else {
                        Globals.SetToolBarBackground(this, app_bar, ContextCompat.getColor(this, R.color.primary), ContextCompat.getColor(this, android.R.color.white));
                    }
                    getSupportActionBar().setTitle(Globals.orderTypeMasterId == Globals.OrderType.DineIn.getValue() ? getResources().getString(R.string.title_activity_home) + " - Dine In" : getResources().getString(R.string.title_activity_home) + " - Take Away");
                }
            }
        }
        //navigationView
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        if (isMenuMode) {
            navigationView.getMenu().findItem(R.id.feedback).setVisible(false);
        }
        headerView = LayoutInflater.from(GuestHomeActivity.this).inflate(R.layout.navigation_header, null);
        llNavHeader = (LinearLayout) headerView.findViewById(R.id.llNavHeader);
        nameLayout = (LinearLayout) headerView.findViewById(R.id.nameLayout);
        imageView = (ImageView) headerView.findViewById(R.id.imageView);
        ivLogo = (ImageView) headerView.findViewById(R.id.ivLogo);
        txtLetter = (TextView) headerView.findViewById(R.id.txtLetter);
        txtName = (TextView) headerView.findViewById(R.id.txtName);

        Drawable drawable = imageView.getDrawable();
        drawable.mutate().setColorFilter(ContextCompat.getColor(this,R.color.accent), PorterDuff.Mode.SRC_IN);
        imageView.setImageDrawable(drawable);
        txtLetter.setTextColor(ContextCompat.getColor(this, R.color.primary));

        SetGuestName();

//        if (GuestHomeActivity.isGuestMode) {
//            if (Globals.objAppThemeMaster != null) {
//                llNavHeader.setBackground(new ColorDrawable(Globals.objAppThemeMaster.getColorPrimary()));
//            } else {
//                llNavHeader.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.guestTabColor)));
//            }
//        }
        navigationView.addHeaderView(headerView);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        //drawerlayout and actionbardrawertoggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Globals.SetNavigationDrawer(actionBarDrawerToggle, GuestHomeActivity.this, drawerLayout, app_bar, getSupportFragmentManager());
        //end

        if (savedInstanceState == null) {
            AddFragmentInLayout(new GuestOptionListFragment(this));
        }
        SaveObjectInPreference();

        isShowMessage = getIntent().getBooleanExtra("ShowMessage", false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Globals.targetFragment = null;
            if (requestCode == 0) {
                if (data != null) {
                    if (!isMenuMode) {
                        SetCartNumber(txtCartNumber);
                    }
                }
            } else if (requestCode == 100) {
                isRestart = true;
                onRestart();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isShowMessage) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    ShowSnackBarWithAction(String.format(getResources().getString(R.string.MsgConfirmOrderPlace), " successfully"));
                }
            }, 1000);
            isShowMessage = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        if (!isMenuMode) {
            MenuItem cartItem = menu.findItem(R.id.cart_layout);

            relativeLayout = (RelativeLayout) MenuItemCompat.getActionView(cartItem);
            final RelativeLayout cartLayout = (RelativeLayout) relativeLayout.findViewById(R.id.cartLayout);
            txtCartNumber = (TextView) relativeLayout.findViewById(R.id.txtCartNumber);
            SaveCartDataInSharePreference();

            SetCartNumber(txtCartNumber);

            cartLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Globals.HideKeyBoard(GuestHomeActivity.this, v);

                    Intent intent = new Intent(GuestHomeActivity.this, CartItemActivity.class);
                    intent.putExtra("isHome", true);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (isMenuMode) {
            menu.findItem(R.id.callWaiter).setVisible(false);
            menu.findItem(R.id.home).setVisible(false);
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.shortList).setVisible(false);
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
            menu.findItem(R.id.cart_layout).setVisible(false);
        } else {
            Globals.SetOptionMenu(Globals.userName, GuestHomeActivity.this, menu);
            menu.findItem(R.id.home).setVisible(false);
            menu.findItem(R.id.callWaiter).setVisible(true);
            menu.findItem(R.id.cart_layout).setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && !getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                .equals(getResources().getString(R.string.title_fragment_offer_detail))) {
            Globals.OptionMenuItemClick(item, GuestHomeActivity.this, getSupportFragmentManager());
//            SetGuestName();
        } else if (item.getItemId() == R.id.callWaiter) {
            CallWaiterDialog callWaiterDialog = new CallWaiterDialog();
            callWaiterDialog.show(getSupportFragmentManager(), "");
        }
        return super.onOptionsItemSelected(item);
    }

    //navigationview event
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                .equals(getResources().getString(R.string.title_fragment_guest_options))) {
            if (menuItem.getItemId() == R.id.changeMode) {
                drawerLayout.closeDrawer(navigationView);
                ChangeModeDialogFragment changeModeDialogFragment = new ChangeModeDialogFragment();
                changeModeDialogFragment.show(getSupportFragmentManager(), "");
            } else if (menuItem.getItemId() == R.id.profile) {
                drawerLayout.closeDrawer(navigationView);
                Intent intent = new Intent(GuestHomeActivity.this, HotelProfileActivity.class);
                intent.putExtra("Mode", (short) 3);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else if (menuItem.getItemId() == R.id.offers) {
                drawerLayout.closeDrawer(navigationView);
                Intent intent = new Intent(GuestHomeActivity.this, OfferActivity.class);
                intent.putExtra("Mode", (short) 3);
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
                intent.putExtra("Mode", (short) 3);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isRestart) {
            Globals.isWishListShow = 1;
            Intent intent = new Intent(GuestHomeActivity.this, GuestHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if (isMenuMode) {
                intent.putExtra("IsMenuMode", isMenuMode);
                intent.putExtra("linktoOrderTypeMasterId", Globals.orderTypeMasterId);
            } else {
                intent.putExtra("TableMaster", objTableMaster);
            }
            startActivity(intent);
//            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        }
    }

    public void EditTextOnClick(View view) {
        GuestProfileFragment guestProfileFragment = (GuestProfileFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_myprofile));
        guestProfileFragment.EditTextOnClick();
    }

    //prevent backPressed
    @Override
    public void onBackPressed() {
        //fragment backPressed
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_signup))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_signup), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() == null) {
                    getSupportFragmentManager().popBackStack();
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_policy))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_policy), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_about_us))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_about_us), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_myaccount))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_myaccount), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    MyAccountFragment.objCustomerMaster = null;
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_myprofile))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_myprofile), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_notification_settings))) {
                    NotificationSettingsFragment notificationSettingsFragment = (NotificationSettingsFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_notification_settings));
                    notificationSettingsFragment.CreateNotificationPreference();
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_notification_settings), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_change_password))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_change_password), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_order_summary))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_order_summary), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_offer_detail))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_offer_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_offer))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_offer), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_feedback))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_feedback), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_thank_you))) {
//                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_thank_you), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        }
    }
    //end

    //region Private Methods
    private void SaveObjectInPreference() {
        if (!isMenuMode) {
            objSharePreferenceManage = new SharePreferenceManage();
            try {
                JSONStringer jsonStringer = new JSONStringer();
                jsonStringer.object();
                jsonStringer.key("TableMasterId").value(objTableMaster.getTableMasterId());
                jsonStringer.key("TableName").value(objTableMaster.getTableName());
                jsonStringer.key("ShortName").value(objTableMaster.getShortName());
                jsonStringer.key("linktoTableStatusMasterId").value(objTableMaster.getlinktoTableStatusMasterId());
                jsonStringer.key("linktoOrderTypeMasterId").value(objTableMaster.getlinktoOrderTypeMasterId());
                //jsonStringer.key("linktoSectionMasterId").value(objTableMaster.getlinktoSectionMasterId());
                jsonStringer.key("linktoBusinessMasterId").value(objTableMaster.getlinktoBusinessMasterId());
                jsonStringer.endObject();
                objSharePreferenceManage.CreatePreference("GuestModePreference", "GuestMode", jsonStringer.toString(), GuestHomeActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void AddFragmentInLayout(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment.setTargetFragment(Globals.targetFragment, 0);
        fragmentTransaction.replace(R.id.guestFragmentLayout, fragment, getResources().getString(R.string.title_fragment_guest_options));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.title_fragment_guest_options));
        fragmentTransaction.commit();
    }

    private void SetGuestName() {
        if (isMenuMode) {
            ivLogo.setVisibility(View.VISIBLE);
            nameLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            txtName.setVisibility(View.GONE);
            txtLetter.setVisibility(View.GONE);
        } else {
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
    }

    public void ReplaceFragment(Fragment fragment, String fragmentName) {
        if (Build.VERSION.SDK_INT >= 21) {
            Fade fade = new Fade();
            fade.setDuration(500);
            fragment.setEnterTransition(fade);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            RemoveFragment(fragmentTransaction, itemTabLayout.getSelectedTabPosition());
            fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
            fragmentTransaction.addToBackStack(fragmentName);
            fragmentTransaction.commit();
        } else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            RemoveFragment(fragmentTransaction, itemTabLayout.getSelectedTabPosition());
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
            fragmentTransaction.addToBackStack(fragmentName);
            fragmentTransaction.commit();
        }
    }

    private void ShowSnackBarWithAction(final String msg) {
        //getResources().getString(R.string.ybAddBookingSuccessMsg)
        Snackbar snackbar = Snackbar
                .make(drawerLayout, msg, Snackbar.LENGTH_LONG)
                .setAction("View", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("TableMaster", GuestHomeActivity.objTableMaster);
                        OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment();
                        orderSummaryFragment.setArguments(bundle);
                        Globals.ReplaceFragment(orderSummaryFragment, getSupportFragmentManager(), getResources().getString(R.string.title_fragment_order_summary));
                    }
                })
                .setDuration(5000);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.accent));
        View snackView = snackbar.getView();
        if (Build.VERSION.SDK_INT >= 21) {
            snackView.setElevation(R.dimen.snackbar_elevation);
        }
        android.widget.TextView txt = (android.widget.TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        txt.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_grey));
        snackbar.show();
    }

    private void SaveCartDataInSharePreference() {
        Gson gson = new Gson();
        SharePreferenceManage objSharePreferenceManage;
        List<ItemMaster> lstItemMaster;
        try {
            if (Globals.alOrderItemTran.size() == 0) {
                objSharePreferenceManage = new SharePreferenceManage();
                String string = objSharePreferenceManage.GetPreference("CartItemListPreference", "CartItemList", GuestHomeActivity.this);
                if (string != null) {
                    ItemMaster[] objItemMaster = gson.fromJson(string,
                            ItemMaster[].class);

                    lstItemMaster = Arrays.asList(objItemMaster);
                    Globals.alOrderItemTran.addAll(new ArrayList<>(lstItemMaster));
                    Globals.counter = Globals.alOrderItemTran.size();
                    if (objSharePreferenceManage.GetPreference("CartItemListPreference", "OrderRemark", GuestHomeActivity.this) != null) {
                        RemarkDialogFragment.strRemark = objSharePreferenceManage.GetPreference("CartItemListPreference", "OrderRemark", GuestHomeActivity.this);
                    }
                } else {
                    objSharePreferenceManage.RemovePreference("CheckOutDataPreference", "CheckOutData", GuestHomeActivity.this);
                    objSharePreferenceManage.ClearPreference("CheckOutDataPreference", GuestHomeActivity.this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetCartNumber(TextView txtCartNumber) {
        if (Globals.counter > 0) {
            txtCartNumber.setVisibility(View.VISIBLE);
            txtCartNumber.setText(String.valueOf(Globals.counter));
            txtCartNumber.setSoundEffectsEnabled(true);
            txtCartNumber.setBackground(ContextCompat.getDrawable(GuestHomeActivity.this, R.drawable.cart_number));
            txtCartNumber.setAnimation(AnimationUtils.loadAnimation(GuestHomeActivity.this, R.anim.fab_scale_up));
        } else {
            txtCartNumber.setVisibility(View.GONE);
            txtCartNumber.setBackgroundColor(ContextCompat.getColor(GuestHomeActivity.this, android.R.color.transparent));
        }
    }

//endregion

}
