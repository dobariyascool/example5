package com.arraybit.pos;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;


@SuppressWarnings("unchecked")
public class SplashScreenActivity extends AppCompatActivity {
    SharePreferenceManage objSharePreferenceManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView ivLogo = (ImageView) findViewById(R.id.ivCompanyLogo);
//        Glide.with(SplashScreenActivity.this).load(R.drawable.arraybit).asBitmap().into(ivLogo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                objSharePreferenceManage = new SharePreferenceManage();

                if (objSharePreferenceManage.GetPreference("ServerPreference", "ServerName", SplashScreenActivity.this) == null) {
                    Globals.InitializeFragment(new ServerNameFragment(), getSupportFragmentManager());
                } else {

                    if ((objSharePreferenceManage.GetPreference("WaitingPreference", "UserName", SplashScreenActivity.this) == null) && (objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", SplashScreenActivity.this) == null)) {

                        if (Build.VERSION.SDK_INT < 21) {
                            Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            finish();
                        } else {
                            ActivityOptions options =
                                    ActivityOptions.
                                            makeSceneTransitionAnimation(SplashScreenActivity.this);
                            Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                    }
                    //get server name
                    objSharePreferenceManage = new SharePreferenceManage();
                    Globals.serverName = objSharePreferenceManage.GetPreference("ServerPreference", "ServerName", SplashScreenActivity.this);
                    //end

                }

            }
        }, 3000);
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

}
