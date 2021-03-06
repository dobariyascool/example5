package com.arraybit.pos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.NotificationReceiver;
import com.arraybit.global.POSApplication;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CounterMaster;
import com.arraybit.parser.CounterJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"unchecked", "ResourceType"})
@SuppressLint("InflateParams")
public class WaiterHomeActivity extends AppCompatActivity implements NotificationReceiver.NotificationListener {

    public static boolean isWaiterMode = false;
    final private int requestAskPermission = 123;
    ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout waiterHomeMainLayout, llNavHeader;
    SharePreferenceManage objSharePreferenceManage;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView txtNotificationNumber;
    Toolbar app_bar;
    boolean isRestart, isShowMessage, isCheckOutMessage;
    String tableName;
    RelativeLayout notificationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_home);

        isWaiterMode = true;
        getApplication().setTheme(R.style.AppThemeGuest);

        //app_bar
//        app_bar = (Toolbar) findViewById(R.id.app_bar);
//        setSupportActionBar(app_bar);
//        if (getSupportActionBar() != null) {
//            if (Build.VERSION.SDK_INT >= 21) {
//                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
//            }
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setLogo(R.drawable.central_logo);
//        }
//        //end


//        waiterHomeMainLayout = (LinearLayout) findViewById(R.id.waiterHomeMainLayout);
//
//
//        //navigationView
//        View headerView = LayoutInflater.from(WaiterHomeActivity.this).inflate(R.layout.navigation_header, null);
//        llNavHeader = (LinearLayout) headerView.findViewById(R.id.llNavHeader);
//        ImageView ivLogo = (ImageView) headerView.findViewById(R.id.ivLogo);
//        ivLogo.setVisibility(View.GONE);
//        TextView txtLetter = (TextView) headerView.findViewById(R.id.txtLetter);
//        TextView txtName = (TextView) headerView.findViewById(R.id.txtName);
//        CompoundButton cbLogout = (CompoundButton) headerView.findViewById(R.id.cbLogout);
//        cbLogout.setVisibility(View.VISIBLE);
//        txtLetter.setTextColor(ContextCompat.getColor(this, android.R.color.white));
//
//        navigationView = (NavigationView) findViewById(R.id.navigationView);
//        SetWaiterName(txtName, txtLetter, navigationView);
//        navigationView.addHeaderView(headerView);
//        navigationView.setNavigationItemSelectedListener(this);
//        //end

//        Globals.SetToolBarBackground(this, app_bar, ContextCompat.getColor(this, R.color.primary_black), ContextCompat.getColor(this, android.R.color.white));
//        llNavHeader.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.primary_black)));
//
//        //drawerlayout and actionbardrawertoggle
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        Globals.SetNavigationDrawer(actionBarDrawerToggle, WaiterHomeActivity.this, drawerLayout, app_bar, getSupportFragmentManager());
        //end

//        AddFragmentInLayout(new WaiterOptionListFragment());


//        if (Globals.totalCounter > 0) {
//            if (Globals.totalCounter > 1) {
//                navigationView.getMenu().findItem(R.id.wChangeCounter).setEnabled(true);
//            } else {
//                navigationView.getMenu().findItem(R.id.wChangeCounter).setEnabled(false);
//            }
//        } else {
//            if (Service.CheckNet(WaiterHomeActivity.this)) {
//                new CounterLoadingTask().execute();
//            } else {
//                Globals.ShowSnackBar(waiterHomeMainLayout, getResources().getString(R.string.MsgCheckConnection), WaiterHomeActivity.this, 1000);
//            }
//        }
//
//        cbLogout.setOnClickListener(this);

        //schedule to call notification class on time
        int hasWriteContactsPermission = 0;
        if (!Globals.ReceiverStart) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                hasWriteContactsPermission = checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                            requestAskPermission);
                } else {
                    Globals.ReceiverStart = true;
                    POSApplication.setAppCompatActivity(WaiterHomeActivity.this);
                    Globals.CallNotificationReceiver(WaiterHomeActivity.this);
                }
            } else {
                Globals.ReceiverStart = true;
                POSApplication.setAppCompatActivity(WaiterHomeActivity.this);
                Globals.CallNotificationReceiver(WaiterHomeActivity.this);
            }
        }

        waiterHomeMainLayout = (LinearLayout) findViewById(R.id.waiterHomeFragment);

        tableName = getIntent().getStringExtra("TableName");
        isShowMessage = getIntent().getBooleanExtra("ShowMessage", false);
        isCheckOutMessage = getIntent().getBooleanExtra("IsCheckOutMessage", false);
        Fragment fragment = new WaiterHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TableName", tableName);
        bundle.putBoolean("ShowMessage", isShowMessage);
        bundle.putBoolean("IsCheckOutMessage", isCheckOutMessage);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.waiterHomeFragment, fragment, "Waiter Home");
        fragmentTransaction.addToBackStack("Waiter Home");
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isShowMessage) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (tableName != null && !tableName.equals("")) {
                        ShowSnackBarWithAction(String.format(getResources().getString(R.string.MsgConfirmOrderPlace), " of " + tableName));
                    } else {
                        ShowSnackBarWithAction(String.format(getResources().getString(R.string.MsgConfirmOrderPlace), " successfully"));
                    }
                    isShowMessage = false;
                }
            }, 1000);
        }
        if (isCheckOutMessage) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Globals.ShowSnackBar(waiterHomeMainLayout, getResources().getString(R.string.MsgBillGenerateSuccess), WaiterHomeActivity.this, 2000);
                    isCheckOutMessage = false;
                }
            }, 1000);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.viewChange).setVisible(false);
//        menu.findItem(R.id.notification_layout).setVisible(true);
//        menu.findItem(R.id.logout).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_waiter_home, menu);
//        MenuItem cartItem = menu.findItem(R.id.notification_layout);
//        RelativeLayout relativeLayout = (RelativeLayout) MenuItemCompat.getActionView(cartItem);
//        notificationLayout = (RelativeLayout) relativeLayout.findViewById(R.id.notificationLayout);
//        txtNotificationNumber = (TextView) relativeLayout.findViewById(R.id.txtNotificationNumber);
//
//        SetNotificationNumber(txtNotificationNumber);
//        notificationLayout.setOnClickListener(this);
        return true;
    }

    @SuppressLint("ShortAlarm")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case requestAskPermission:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Globals.ReceiverStart = true;
                    POSApplication.setAppCompatActivity(WaiterHomeActivity.this);
                    Globals.CallNotificationReceiver(WaiterHomeActivity.this);

                } else {
                    // Permission Denied
                    Toast.makeText(WaiterHomeActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    //selected event
//    @Override
//    public boolean onNavigationItemSelected(MenuItem menuItem) {
//        if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
//                && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
//                .equals(getResources().getString(R.string.title_fragment_waiter_options))) {
//            if (menuItem.getItemId() == R.id.wChangeCounter) {
//                objSharePreferenceManage = new SharePreferenceManage();
//                if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", WaiterHomeActivity.this) != null) {
//                    drawerLayout.closeDrawer(navigationView);
//                    CounterFragment counterFragment = new CounterFragment((short) Globals.UserType.Waiter.getValue());
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean("isBack", true);
//                    counterFragment.setArguments(bundle);
//                    Globals.ReplaceFragment(counterFragment, getSupportFragmentManager(), null);
//                }
//            } else if (menuItem.getItemId() == R.id.wChangeMode) {
//                drawerLayout.closeDrawer(navigationView);
//                ChangeModeDialogFragment changeModeDialogFragment = new ChangeModeDialogFragment();
//                changeModeDialogFragment.show(getSupportFragmentManager(), "");
//            } else if (menuItem.getItemId() == R.id.wHotelProfile) {
//                drawerLayout.closeDrawer(navigationView);
//                Intent intent = new Intent(WaiterHomeActivity.this, HotelProfileActivity.class);
//                intent.putExtra("Mode", (short) 2);
//                startActivityForResult(intent, 0);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//
//            } else if (menuItem.getItemId() == R.id.wOffers) {
//                drawerLayout.closeDrawer(navigationView);
//                Intent intent = new Intent(WaiterHomeActivity.this, OfferActivity.class);
//                intent.putExtra("Mode", (short) 2);
//                startActivityForResult(intent, 0);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            } else if (menuItem.getItemId() == R.id.wFeedback) {
//                drawerLayout.closeDrawer(navigationView);
//                Globals.ReplaceFragment(new FeedbackFragment(WaiterHomeActivity.this), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_feedback));
//            } else if (menuItem.getItemId() == R.id.wRate) {
//                Uri uri = Uri.parse("market://details?id=" + getPackageName());
//                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                try {
//                    startActivity(goToMarket);
//                } catch (ActivityNotFoundException e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
//
//                }
//
//            } else if (menuItem.getItemId() == R.id.wAbout) {
//                drawerLayout.closeDrawer(navigationView);
//                Intent intent = new Intent(WaiterHomeActivity.this, AboutUsActivity.class);
//                intent.putExtra("Mode", (short) 2);
//                startActivityForResult(intent, 0);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            } else if (menuItem.getItemId() == R.id.wExit) {
//                System.exit(0);
//            } else if (menuItem.getItemId() == R.id.wNotification) {
//                drawerLayout.closeDrawer(navigationView);
//                NotificationReceiver.notificationCount = 0;
//                Intent intent = new Intent(WaiterHomeActivity.this, NotificationDetailActivity.class);
//                startActivityForResult(intent, 0);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            }
//        }
//        return false;
//    }

    @Override
    public void ShowNotificationCount() {

        for (int entry = 0; entry < getSupportFragmentManager().getBackStackEntryCount(); entry++) {
            Log.e("fragment", "Found fragment: " + getSupportFragmentManager().getBackStackEntryAt(entry).getName() + " " + getSupportFragmentManager().getBackStackEntryAt(entry).getId());
            if (getSupportFragmentManager().getBackStackEntryAt(entry).getName().equals("Waiter Home")) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("Waiter Home");
                Log.e("fragment"," "+fragment);
                FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                fragTransaction.detach(fragment);
                fragTransaction.attach(fragment);
                fragTransaction.commitAllowingStateLoss();
            }
        }

//        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Waiter Home");
//        fragment.SetNotificationNumber();
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.waiterHomeFragment);
//        if (currentFragment instanceof WaiterHomeFragment) {
//            FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
//            fragTransaction.detach(currentFragment);
//            fragTransaction.attach(currentFragment);
//            fragTransaction.commitAllowingStateLoss();
//        }
//        SetNotificationNumber(txtNotificationNumber);
    }

//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.cbLogout) {
//            Globals.ClearPreference(WaiterHomeActivity.this);
////        } else if (v.getId() == R.id.notificationLayout) {
////            NotificationReceiver.notificationCount = 0;
////            Intent intent = new Intent(WaiterHomeActivity.this, NotificationDetailActivity.class);
////            startActivityForResult(intent, 0);
////            overridePendingTransition(R.anim.right_in, R.anim.left_out);
//        }
//    }

//    @Override
//    public void TableOnClick(TableMaster objTableMaster) {
//        Intent intent = new Intent(WaiterHomeActivity.this, MenuActivity.class);
//        if (!GuestHomeActivity.isMenuMode) {
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.putExtra("IsFavoriteShow", true);
//            intent.putExtra("TableMaster", objTableMaster);
//        }
////                startActivity(intent);
//        startActivityForResult(intent, 100);
//        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//    }

    @Override
    protected void onRestart() {
        super.onRestart();

//        if (isRestart) {
//            Globals.isWishListShow = 0;
//            Intent intent = new Intent(WaiterHomeActivity.this, WaiterHomeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            overridePendingTransition(R.anim.right_in, R.anim.left_out);
//            finish();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 1) {
////                onRestart();
//            } else if (requestCode == 0) {
////                WaiterHomeFragment fragment = (WaiterHomeFragment) getSupportFragmentManager().findFragmentByTag("Waiter Home");
////                fragment.SetNotificationNumber();
//////                SetNotificationNumber(txtNotificationNumber);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    //prevent backPressed
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_order_detail))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_order_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_detail))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_all_orders))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_all_orders), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_cart_item))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_order_summary))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_order_summary), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Globals.objDiscountMaster = null;
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_all_tables))) {
//                    isRestart = true;
//                    onRestart();
                    isWaiterMode = true;
                    GuestHomeActivity.isGuestMode = false;
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_all_tables), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_policy))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_policy), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_about_us))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_about_us), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_feedback))) {

                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals("Waiter Home")) {

                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_waiter_options))) {

                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_thank_you))) {
//                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_thank_you), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
//                    isRestart = true;
//                    onRestart();
//                    CategoryItemFragment.i = 0;
//                    CategoryItemFragment.isViewChange = false;
                    getSupportFragmentManager().popBackStack();
                    Globals.counter = 0;
                    Globals.alOrderItemTran = new ArrayList<>();
                    Globals.targetFragment = null;
                }
            }
        }
    }
    //end

    //region Private Methods and Interface

    private void AddFragmentInLayout(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.waiterHomeFragment, fragment, "Waiter Home");
        fragmentTransaction.addToBackStack("Waiter Home");
        fragmentTransaction.commit();
    }

    private void SetWaiterName(TextView txtName, TextView txtLetter, NavigationView navigationView) {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", WaiterHomeActivity.this) != null) {
            txtName.setText(objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", WaiterHomeActivity.this));
            txtLetter.setText(objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", WaiterHomeActivity.this).substring(0, 1).toUpperCase());
        }
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterName", WaiterHomeActivity.this) != null) {
            navigationView.getMenu().findItem(R.id.wChangeCounter).setTitle(objSharePreferenceManage.GetPreference("CounterPreference", "CounterName", WaiterHomeActivity.this));
        }
    }

    private void SetNotificationNumber(final TextView txtNotificationNumber) {
        if (NotificationReceiver.notificationCount > 0) {
            txtNotificationNumber.setVisibility(View.VISIBLE);
            txtNotificationNumber.setText(String.valueOf(NotificationReceiver.notificationCount));
            txtNotificationNumber.setSoundEffectsEnabled(true);
            notificationLayout.setSoundEffectsEnabled(true);
            txtNotificationNumber.setAnimation(AnimationUtils.loadAnimation(WaiterHomeActivity.this, R.anim.fab_scale_up));
        } else {
            txtNotificationNumber.setVisibility(View.GONE);
        }
    }

    private void ShowSnackBarWithAction(final String msg) {
        Snackbar snackbar = Snackbar
                .make(waiterHomeMainLayout, msg, Snackbar.LENGTH_LONG)
                .setAction("View", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Globals.ReplaceFragment(new AllOrdersFragment(null), getSupportFragmentManager(), null);
                    }
                })
                .setDuration(5000);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.accent_red_dark));
        View snackView = snackbar.getView();
        if (Build.VERSION.SDK_INT >= 21) {
            snackView.setElevation(R.dimen.snackbar_elevation);
        }
        android.widget.TextView txt = (android.widget.TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        txt.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_grey));
        snackbar.show();
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
            if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", WaiterHomeActivity.this) != null) {
                userMasterId = Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", WaiterHomeActivity.this));
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