package com.arraybit.pos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.TableMaster;
import com.arraybit.parser.AppThemeJSONParser;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings({"unchecked", "NullArgumentToVariableArgMethod"})
public class SplashScreenActivity extends AppCompatActivity {
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
    RelativeLayout splashScreenLayout;
    TableMaster objTableMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try {
            splashScreenLayout = (RelativeLayout) findViewById(R.id.splashScreenLayout);

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.central_splash);
            Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels);
            splashScreenLayout.setBackground(new BitmapDrawable(getResources(),resizeBitmap));
//        Glide.with(SplashScreenActivity.this).load(R.drawable.arraybit).asBitmap().into(ivLogo);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (objSharePreferenceManage.GetPreference("ServerPreference", "ServerName", SplashScreenActivity.this) == null) {
                        Globals.InitializeFragment(new ServerNameFragment(), getSupportFragmentManager());
                    } else {

                        if ((objSharePreferenceManage.GetPreference("WaitingPreference", "UserName", SplashScreenActivity.this) == null) && (objSharePreferenceManage.GetPreference("WaiterPreference", "UserName", SplashScreenActivity.this) == null)) {
                            RedirectActivity(SignInActivity.class);

                        } else {
                            Globals.SetBusinessMasterId(SplashScreenActivity.this);
                            String obj = objSharePreferenceManage.GetPreference("GuestModePreference", "GuestMode", SplashScreenActivity.this);
                            if (obj == null && GetObjectFromPreference() == null) {
                                if (objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", SplashScreenActivity.this) != null) {
                                    Globals.isWishListShow = 0;
                                    Intent i = new Intent(SplashScreenActivity.this, WaiterHomeActivity.class);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    finish();
                                }
                            }
                            else {
                                RedirectActivity(WelcomeActivity.class);
                            }
                        }
                        //get server name
                        objSharePreferenceManage = new SharePreferenceManage();
                        Globals.serverName = objSharePreferenceManage.GetPreference("ServerPreference", "ServerName", SplashScreenActivity.this);
                        //end
//                    Intent intent = new Intent(SplashScreenActivity.this, AppThemeIntentService.class);
//                    startService(intent);
                        if (Service.CheckNet(SplashScreenActivity.this)) {
                            SharePreferenceManage sharePreferenceManage = new SharePreferenceManage();
                            int linktoBusinessMasterId;
                            if (sharePreferenceManage.GetPreference("WaiterPreference", "linktoBusinessMasterId", SplashScreenActivity.this) != null &&
                                    !sharePreferenceManage.GetPreference("WaiterPreference", "linktoBusinessMasterId", SplashScreenActivity.this).equals("")) {
                                linktoBusinessMasterId = Integer.parseInt(sharePreferenceManage.GetPreference("WaiterPreference", "linktoBusinessMasterId", SplashScreenActivity.this));
                                AppThemeJSONParser appThemeJSONParser = new AppThemeJSONParser();
                                try {
                                    final JSONObject jsonObject = appThemeJSONParser.SelectAppThemeMaster(linktoBusinessMasterId);
                                    if (jsonObject != null) {
                                        Globals.objAppThemeMaster = appThemeJSONParser.SetClassPropertiesFromJSONObject(jsonObject);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }
            }, 3000);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
//        finish();
//        overridePendingTransition(0, R.anim.right_exit);
    }

    private void RedirectActivity(Class<?> activity) {
        Intent intent = new Intent(SplashScreenActivity.this, activity);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }

    private TableMaster GetObjectFromPreference() {
        JSONObject jsonObject;
        try {
            if(objSharePreferenceManage.GetPreference("GuestModePreference", "GuestMode", SplashScreenActivity.this)!=null) {
                jsonObject = new JSONObject(objSharePreferenceManage.GetPreference("GuestModePreference", "GuestMode", SplashScreenActivity.this));
                objTableMaster = new TableMaster();
                objTableMaster.setTableMasterId((short) jsonObject.getInt("TableMasterId"));
                objTableMaster.setTableName(jsonObject.getString("TableName"));
                objTableMaster.setShortName(jsonObject.getString("ShortName"));
                objTableMaster.setlinktoTableStatusMasterId((short) jsonObject.getInt("linktoTableStatusMasterId"));
                objTableMaster.setlinktoOrderTypeMasterId((short) jsonObject.getInt("linktoOrderTypeMasterId"));
                //objTableMaster.setlinktoSectionMasterId((short) jsonObject.getInt("linktoSectionMasterId"));
                objTableMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
            }
            else
            {
                objTableMaster = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            objTableMaster = null;
        }
        return objTableMaster;
    }
}
