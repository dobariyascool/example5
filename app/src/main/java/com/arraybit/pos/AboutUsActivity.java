package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.rey.material.widget.TextView;

@SuppressWarnings("ConstantConditions")
public class AboutUsActivity extends AppCompatActivity {

    CardView cardPolicy, cardTerms;
    short mode;

    @SuppressLint("SetTextI18n")
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

        Intent getData = getIntent();
        mode = getData.getShortExtra("Mode", (short) 0);

        LinearLayout versionLayout = (LinearLayout) findViewById(R.id.versionLayout);

        cardTerms = (CardView) findViewById(R.id.cardTerms);
        cardPolicy = (CardView) findViewById(R.id.cardPolicy);

        TextView txtCardPolicy = (TextView) findViewById(R.id.txtCardPolicy);
        TextView txtCardTerms = (TextView) findViewById(R.id.txtCardTerms);
        TextView txtVersionCode = (TextView) findViewById(R.id.txtVersionCode);

//        if (Build.VERSION.SDK_INT >= 17 && Build.VERSION.SDK_INT < 19) {
//            versionLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.card_view_with_border));
//            txtCardPolicy.setBackground(ContextCompat.getDrawable(this, R.drawable.card_view_with_border));
//            txtCardTerms.setBackground(ContextCompat.getDrawable(this, R.drawable.card_view_with_border));
//        }

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
                ReplaceFragment(new PolicyFragment((short) 1), getResources().getString(R.string.title_fragment_policy));
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mode == 1) {
            menu.findItem(R.id.viewChange).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        } else if (mode == 2) {
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.viewChange).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        } else if (mode == 3) {
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.registration).setVisible(false);
            menu.findItem(R.id.shortList).setVisible(false);
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
        super.onBackPressed();
        overridePendingTransition(0, R.anim.right_exit);
    }
}
