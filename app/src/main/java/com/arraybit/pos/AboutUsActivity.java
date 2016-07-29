package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessDescription;
import com.arraybit.parser.BusinessDescriptionJSONParser;
import com.rey.material.widget.TextView;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class AboutUsActivity extends AppCompatActivity {

    CardView cardPolicy, cardTerms;
    short mode;
    WebView wvAbout;
    BusinessDescription objBusinessDescription;

    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        FrameLayout aboutFragment = (FrameLayout) findViewById(R.id.aboutFragment);

        Intent getData = getIntent();
        mode = getData.getShortExtra("Mode", (short) 0);


        cardTerms = (CardView) findViewById(R.id.cardTerms);
        cardPolicy = (CardView) findViewById(R.id.cardPolicy);

        TextView txtCardPolicy = (TextView) findViewById(R.id.txtCardPolicy);
        TextView txtCardTerms = (TextView) findViewById(R.id.txtCardTerms);
        TextView txtVersionCode = (TextView) findViewById(R.id.txtVersionCode);

        if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {
            //                if(Globals.objAppThemeMaster!=null) {
//                    Globals.SetToolBarBackground(getActivity(), app_bar, Globals.objAppThemeMaster.getColorPrimary(), ContextCompat.getColor(getActivity(), android.R.color.white));
//                }
//                else
//                {
            Globals.SetToolBarBackground(this, app_bar, ContextCompat.getColor(this, R.color.primary), ContextCompat.getColor(this, android.R.color.white));
//                }
            txtCardPolicy.setTextColor(ContextCompat.getColor(this, R.color.accent_dark));
            txtCardTerms.setTextColor(ContextCompat.getColor(this, R.color.accent_dark));
        } else {
            Globals.SetToolBarBackground(this, app_bar, ContextCompat.getColor(this, R.color.primary_black), ContextCompat.getColor(this, android.R.color.white));

        }

        wvAbout = (WebView) findViewById(R.id.wvAbout);
        wvAbout.getSettings().setJavaScriptEnabled(true);
        wvAbout.getSettings().setDatabaseEnabled(true);
        wvAbout.getSettings().setDomStorageEnabled(true);
        wvAbout.getSettings().setAppCacheEnabled(true);
        wvAbout.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvAbout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        if (Service.CheckNet(this)) {
            new DescriptionLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(aboutFragment, getResources().getString(R.string.MsgCheckConnection), this, 1000);
        }

        txtVersionCode.setText(getResources().getString(R.string.abVersionCode) + "  " + BuildConfig.VERSION_CODE + "\n" +
                getResources().getString(R.string.abVersionName) + " " + BuildConfig.VERSION_NAME);

        txtCardPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplaceFragment(new PolicyFragment((short) 1), getResources().getString(R.string.title_fragment_policy));
            }
        });

        txtCardTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplaceFragment(new PolicyFragment((short) 2), getResources().getString(R.string.title_fragment_policy));
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mode == 1) {
            menu.findItem(R.id.viewChange).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.notification_layout).setVisible(false);
        } else if (mode == 2) {
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.notification_layout).setVisible(false);
        } else if (mode == 3) {
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.shortList).setVisible(false);
            menu.findItem(R.id.callWaiter).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (mode == 1 || mode == 2) {
            getMenuInflater().inflate(R.menu.menu_waiter_home, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_home, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(0, R.anim.right_exit);
            }

        }
        if (mode == 1 || mode == 2) {
            if (item.getItemId() == R.id.logout) {
                Globals.ClearPreference(AboutUsActivity.this);
            }
        } else if (mode == 3) {
            Globals.OptionMenuItemClick(item, AboutUsActivity.this, getSupportFragmentManager());
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("RtlHardcoded")
    private void ReplaceFragment(Fragment fragment, String fragmentName) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(350);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(R.id.aboutFragment, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
        overridePendingTransition(0, R.anim.right_exit);
    }

    //region Loading Task
    class DescriptionLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog();
            progressDialog.show(getSupportFragmentManager(), "");
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            BusinessDescriptionJSONParser objBusinessDescriptionJSONParser = new BusinessDescriptionJSONParser();
            return objBusinessDescriptionJSONParser.SelectBusinessDescription(AboutUsActivity.this, String.valueOf(Globals.businessMasterId), Globals.AboutUs);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            objBusinessDescription = (BusinessDescription) result;
            if (objBusinessDescription != null && objBusinessDescription.getDescription() != null && !objBusinessDescription.getDescription().equals("")) {
                wvAbout.setVisibility(View.VISIBLE);
                wvAbout.loadData(objBusinessDescription.getDescription(), "text/html; charset=UTF-8", null);
            }
        }
    }
    //endregion
}
