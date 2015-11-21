package com.arraybit.pos;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CounterMaster;
import com.arraybit.parser.CounterJSONParser;

import java.util.ArrayList;


@SuppressWarnings("unchecked")
public class SplashScreenActivity extends AppCompatActivity {
    public static short counter = 0;
    SharePreferenceManage objSharePreferenceManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("ServerPreference", "ServerName", SplashScreenActivity.this) == null) {
                    Globals.InitializeFragment(new ServerNameFragment(), getSupportFragmentManager());
                } else {

                    if ((objSharePreferenceManage.GetPreference("WaitingPreference", "UserName", SplashScreenActivity.this) == null) && (objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", SplashScreenActivity.this) == null)) {
                        Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }

                new CounterLoadingTask().execute();

            }
        }, 800);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class CounterLoadingTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            CounterJSONParser objCounterJSONParser = new CounterJSONParser();
            return objCounterJSONParser.SelectAllCounterMaster(Globals.BusinessMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            ArrayList<CounterMaster> alCounter=(ArrayList<CounterMaster>) result;

            counter = (short) alCounter.size();
        }
    }
}
