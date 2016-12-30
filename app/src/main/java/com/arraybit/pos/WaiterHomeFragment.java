package com.arraybit.pos;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.NotificationReceiver;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CounterMaster;
import com.arraybit.parser.CounterJSONParser;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaiterHomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Toolbar app_bar;
    RelativeLayout notificationLayout;
    TextView txtNotificationNumber;
    ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout waiterHomeMainLayout, llNavHeader;
    SharePreferenceManage objSharePreferenceManage;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    boolean isRestart, isShowMessage, isCheckOutMessage;
    String tableName;

    public WaiterHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_waiter_home, container, false);

        //app_bar
        app_bar = (Toolbar) rootView.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.central_logo);
            setHasOptionsMenu(true);
        }
        //end

        waiterHomeMainLayout = (LinearLayout) rootView.findViewById(R.id.waiterHomeMainLayout);

        Bundle bundle = getArguments();
        if (bundle != null) {
            tableName = bundle.getString("TableName");
            isShowMessage = bundle.getBoolean("isShowMessage", false);
            isCheckOutMessage = bundle.getBoolean("IsCheckOutMessage", false);
        }

        //navigationView
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.navigation_header, null);
        llNavHeader = (LinearLayout) headerView.findViewById(R.id.llNavHeader);
        ImageView ivLogo = (ImageView) headerView.findViewById(R.id.ivLogo);
        ivLogo.setVisibility(View.GONE);
        TextView txtLetter = (TextView) headerView.findViewById(R.id.txtLetter);
        TextView txtName = (TextView) headerView.findViewById(R.id.txtName);
        CompoundButton cbLogout = (CompoundButton) headerView.findViewById(R.id.cbLogout);
        cbLogout.setVisibility(View.VISIBLE);
        txtLetter.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));

        navigationView = (NavigationView) rootView.findViewById(R.id.navigationView);
        SetWaiterName(txtName, txtLetter, navigationView);
        navigationView.addHeaderView(headerView);
        navigationView.setNavigationItemSelectedListener(this);
        //end

        Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary_black), ContextCompat.getColor(getActivity(), android.R.color.white));
        llNavHeader.setBackground(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.primary_black)));

        //drawerlayout and actionbardrawertoggle
        drawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawerLayout);
        Globals.SetNavigationDrawer(actionBarDrawerToggle, getActivity(), drawerLayout, app_bar, getActivity().getSupportFragmentManager());
        //end

        AddFragmentInLayout(new WaiterOptionListFragment());

        if (Globals.totalCounter > 0) {
            if (Globals.totalCounter > 1) {
                navigationView.getMenu().findItem(R.id.wChangeCounter).setEnabled(true);
            } else {
                navigationView.getMenu().findItem(R.id.wChangeCounter).setEnabled(false);
            }
        } else {
//            if (Service.CheckNet(getActivity())) {
//                new CounterLoadingTask().execute();
//            } else {
//                Globals.ShowSnackBar(waiterHomeMainLayout, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
//            }
            navigationView.getMenu().findItem(R.id.wChangeCounter).setEnabled(false);
        }

        cbLogout.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onStart() {
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
                    Globals.ShowSnackBar(drawerLayout, getResources().getString(R.string.MsgBillGenerateSuccess), getActivity(), 2000);
                    isCheckOutMessage = false;
                }
            }, 1000);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_waiter_home, menu);
        MenuItem cartItem = menu.findItem(R.id.notification_layout);
        RelativeLayout relativeLayout = (RelativeLayout) MenuItemCompat.getActionView(cartItem);
        notificationLayout = (RelativeLayout) relativeLayout.findViewById(R.id.notificationLayout);
        txtNotificationNumber = (TextView) relativeLayout.findViewById(R.id.txtNotificationNumber);

        SetNotificationNumber(txtNotificationNumber);
        notificationLayout.setOnClickListener(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.viewChange).setVisible(false);
        menu.findItem(R.id.notification_layout).setVisible(true);
        menu.findItem(R.id.logout).setVisible(false);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            SetNotificationNumber(txtNotificationNumber);
            if (requestCode == 100) {
//                SetNotificationNumber(txtNotificationNumber);
//                onRestart();
            } else if (requestCode == 0) {
//                SetNotificationNumber(txtNotificationNumber);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //selected event
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                .equals(getResources().getString(R.string.title_fragment_waiter_options))) {
            if (menuItem.getItemId() == R.id.wChangeCounter) {
                objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
                    drawerLayout.closeDrawer(navigationView);
                    CounterFragment counterFragment = new CounterFragment((short) Globals.UserType.Waiter.getValue());
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isBack", true);
                    counterFragment.setArguments(bundle);
                    Globals.ReplaceFragment(counterFragment, getActivity().getSupportFragmentManager(), null);
                }
            } else if (menuItem.getItemId() == R.id.wChangeMode) {
                drawerLayout.closeDrawer(navigationView);
                ChangeModeDialogFragment changeModeDialogFragment = new ChangeModeDialogFragment();
                changeModeDialogFragment.show(getActivity().getSupportFragmentManager(), "");
            } else if (menuItem.getItemId() == R.id.wHotelProfile) {
                drawerLayout.closeDrawer(navigationView);
                Intent intent = new Intent(getActivity(), HotelProfileActivity.class);
                intent.putExtra("Mode", (short) 2);
                startActivityForResult(intent, 0);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else if (menuItem.getItemId() == R.id.wOffers) {
                drawerLayout.closeDrawer(navigationView);
                Intent intent = new Intent(getActivity(), OfferActivity.class);
                intent.putExtra("Mode", (short) 2);
                startActivityForResult(intent, 0);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else if (menuItem.getItemId() == R.id.wFeedback) {
                drawerLayout.closeDrawer(navigationView);
                Globals.ReplaceFragment(new FeedbackFragment(getActivity()), getActivity().getSupportFragmentManager(), getResources().getString(R.string.title_fragment_feedback));
            } else if (menuItem.getItemId() == R.id.wRate) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            } else if (menuItem.getItemId() == R.id.wAbout) {
                drawerLayout.closeDrawer(navigationView);
                Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                intent.putExtra("Mode", (short) 2);
                startActivityForResult(intent, 0);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else if (menuItem.getItemId() == R.id.wExit) {
                System.exit(0);
            } else if (menuItem.getItemId() == R.id.wNotification) {
                drawerLayout.closeDrawer(navigationView);
                NotificationReceiver.notificationCount = 0;
                Intent intent = new Intent(getActivity(), NotificationDetailActivity.class);
                startActivityForResult(intent, 0);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cbLogout) {
            Globals.ClearPreference(getActivity());
        } else if (v.getId() == R.id.notificationLayout) {
            NotificationReceiver.notificationCount = 0;
            Intent intent = new Intent(getActivity(), NotificationDetailActivity.class);
            getActivity().startActivityForResult(intent, 0);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    public void SetNotificationNumber() {
        SetNotificationNumber(txtNotificationNumber);
    }

    //region Private Methods and Interface

    private void AddFragmentInLayout(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment mFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.waiterFragmentLayout);
        if (mFragment instanceof WaiterOptionListFragment) {
            fragmentTransaction.replace(R.id.waiterFragmentLayout, fragment, getResources().getString(R.string.title_fragment_waiter_options));
        } else {
            fragmentTransaction.replace(R.id.waiterFragmentLayout, fragment, getResources().getString(R.string.title_fragment_waiter_options));
            fragmentTransaction.addToBackStack(getResources().getString(R.string.title_fragment_waiter_options));
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void SetNotificationNumber(final TextView txtNotificationNumber) {
        if (NotificationReceiver.notificationCount > 0) {
            txtNotificationNumber.setVisibility(View.VISIBLE);
            txtNotificationNumber.setText(String.valueOf(NotificationReceiver.notificationCount));
            txtNotificationNumber.setSoundEffectsEnabled(true);
            notificationLayout.setSoundEffectsEnabled(true);
            txtNotificationNumber.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fab_scale_up));
        } else {
            txtNotificationNumber.setVisibility(View.GONE);
        }
    }

    private void SetWaiterName(TextView txtName, TextView txtLetter, NavigationView navigationView) {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", getActivity()) != null) {
            txtName.setText(objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", getActivity()));
            txtLetter.setText(objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", getActivity()).substring(0, 1).toUpperCase());
        }
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterName", getActivity()) != null) {
            navigationView.getMenu().findItem(R.id.wChangeCounter).setTitle(objSharePreferenceManage.GetPreference("CounterPreference", "CounterName", getActivity()));
        }
    }

    private void ShowSnackBarWithAction(final String msg) {
        Snackbar snackbar = Snackbar
                .make(drawerLayout, msg, Snackbar.LENGTH_LONG)
                .setAction("View", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Globals.ReplaceFragment(new AllOrdersFragment(null), getActivity().getSupportFragmentManager(), null);
                    }
                })
                .setDuration(5000);
        snackbar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.accent_red_dark));
        View snackView = snackbar.getView();
        if (Build.VERSION.SDK_INT >= 21) {
            snackView.setElevation(R.dimen.snackbar_elevation);
        }
        android.widget.TextView txt = (android.widget.TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        txt.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        snackView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blue_grey));
        snackbar.show();
    }

    //endregion

    //region LoadingTask
    class CounterLoadingTask extends AsyncTask {
        android.app.ProgressDialog progressDialog;
        short userMasterId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()) != null) {
                userMasterId = Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()));
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
