package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.TableMaster;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings("ConstantConditions")
public class WelcomeActivity extends AppCompatActivity {

    SharePreferenceManage objSharePreferenceManage;
    boolean isGuestScreen;
    short count = 0;
    DisplayMetrics displayMetrics;
    ImageView ivLeft, ivRight, ivLogo, ivText, ivSwipe;
    DrawerLayout mainLayout;
    TableMaster objTableMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //get server name
        objSharePreferenceManage = new SharePreferenceManage();
        Globals.serverName = objSharePreferenceManage.GetPreference("ServerPreference", "ServerName", WelcomeActivity.this);
        //end
        if (Globals.objAppThemeMaster != null) {
            new ImageLoadingTask(getResources().getString(R.string.guestEncodedImage1), Globals.objAppThemeMaster.getBackImageName1()).execute();
            new ImageLoadingTask(getResources().getString(R.string.guestEncodedImage2), Globals.objAppThemeMaster.getBackImageName2()).execute();
            new ImageLoadingTask(getResources().getString(R.string.encodedLogoImage), Globals.objAppThemeMaster.getLogoImageName()).execute();
            new ImageLoadingTask(getResources().getString(R.string.encodedProfileImage), Globals.objAppThemeMaster.getProfileImageName()).execute();
        }

        mainLayout = (DrawerLayout) findViewById(R.id.mainLayout);
        displayMetrics = getResources().getDisplayMetrics();

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.welcome_background_full);
//        Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels);

        mainLayout.setBackground(new BitmapDrawable(getResources(), originalBitmap));



        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivText = (ImageView) findViewById(R.id.ivText);
        ivSwipe = (ImageView) findViewById(R.id.ivSwipe);

//        if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
//            Picasso.with(WelcomeActivity.this).load(R.drawable.left_design).resize((displayMetrics.widthPixels * 35) / 100, (displayMetrics.heightPixels * 30) / 100).into(ivLeft);
//            Picasso.with(WelcomeActivity.this).load(R.drawable.right_design).resize((displayMetrics.widthPixels * 35) / 100, (displayMetrics.heightPixels * 30) / 100).into(ivRight);
//            Picasso.with(WelcomeActivity.this).load(R.drawable.likeat_logo).resize((displayMetrics.widthPixels * 20) / 100, (displayMetrics.heightPixels * 14) / 100).into(ivLogo);
//            Picasso.with(WelcomeActivity.this).load(R.drawable.welcome_text).resize((displayMetrics.widthPixels * 70) / 100, (displayMetrics.heightPixels * 12) / 100).into(ivText);
//            Glide.with(WelcomeActivity.this).load(R.drawable.swipe).asGif().into(ivSwipe);
//        } else {
//            Picasso.with(WelcomeActivity.this).load(R.drawable.left_design).resize((displayMetrics.widthPixels * 50) / 100, (displayMetrics.heightPixels * 20) / 100).into(ivLeft);
//            Picasso.with(WelcomeActivity.this).load(R.drawable.right_design).resize((displayMetrics.widthPixels * 50) / 100, (displayMetrics.heightPixels * 20) / 100).into(ivRight);
//            Picasso.with(WelcomeActivity.this).load(R.drawable.likeat_logo).resize((displayMetrics.widthPixels * 25) / 100, (displayMetrics.heightPixels * 8) / 100).into(ivLogo);
//            Picasso.with(WelcomeActivity.this).load(R.drawable.welcome_text).resize((displayMetrics.widthPixels * 80) / 100, (displayMetrics.heightPixels * 6) / 100).into(ivText);
            Glide.with(WelcomeActivity.this).load(R.drawable.swipe).asGif().into(ivSwipe);
//        }



        mainLayout.setOnTouchListener(new View.OnTouchListener() {
                                          @SuppressLint("ShortAlarm")
                                          @Override
                                          public boolean onTouch(View v, MotionEvent event) {

                                              if (count == 0) {
                                                  count++;
                                                  objSharePreferenceManage = new SharePreferenceManage();
                                                  String userTypeMasterId = objSharePreferenceManage.GetPreference("WaiterPreference", "UserTypeMasterId", WelcomeActivity.this);
                                                  Globals.SetBusinessMasterId(WelcomeActivity.this);
                                                  if (userTypeMasterId != null && (userTypeMasterId.equals(String.valueOf(Globals.UserType.valueOf("Waiter").getValue())) ||
                                                          (userTypeMasterId.equals(String.valueOf(Globals.UserType.valueOf("Captain").getValue()))))) {
                                                      Intent intent = getIntent();
                                                      isGuestScreen = intent.getBooleanExtra("GuestScreen", false);
                                                      objTableMaster = intent.getParcelableExtra("TableMaster");
                                                      if (isGuestScreen) {
                                                          if (objTableMaster != null) {
                                                              Globals.isWishListShow = 1;
                                                              Globals.selectTableMasterId = objTableMaster.getTableMasterId();
                                                              Intent i = new Intent(WelcomeActivity.this, GuestHomeActivity.class);
                                                              i.putExtra("TableMaster", intent.getParcelableExtra("TableMaster"));
                                                              startActivity(i);
                                                              overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                                              finish();
                                                          }
                                                      } else {
                                                          String obj = objSharePreferenceManage.GetPreference("GuestModePreference", "GuestMode", WelcomeActivity.this);
                                                          if (obj != null && GetObjectFromPreference() != null) {
                                                              Globals.isWishListShow = 1;
                                                              Globals.selectTableMasterId = objTableMaster.getTableMasterId();
                                                              Intent i = new Intent(WelcomeActivity.this, GuestHomeActivity.class);
                                                              i.putExtra("TableMaster", objTableMaster);
                                                              startActivity(i);
                                                              overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                                              finish();
                                                          } else {
                                                              if (intent.getShortExtra("UserType", (short) 0) == Globals.UserType.Waiting.getValue()) {
                                                                  Globals.isWishListShow = 0;
                                                                  Intent i = new Intent(WelcomeActivity.this, WaitingActivity.class);
                                                                  startActivity(i);
                                                                  overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                                                  finish();
                                                              } else {
                                                                  if (objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", WelcomeActivity.this) != null) {
                                                                      Globals.isWishListShow = 0;
                                                                      Intent i = new Intent(WelcomeActivity.this, WaiterHomeActivity.class);
                                                                      startActivity(i);
                                                                      overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                                                      finish();
                                                                  }
                                                              }
                                                          }
                                                      }
                                                  }
                                              }
                                              return false;
                                          }
                                      }

        );
    }

    class ImageLoadingTask extends AsyncTask {
        String imagePreferenceName, urlImage;
        SharePreferenceManage sharePreferenceManage = new SharePreferenceManage();
        String encodedImage;

        public ImageLoadingTask(String imagePreferenceName, String urlImage) {
            this.imagePreferenceName = imagePreferenceName;
            this.urlImage = urlImage;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                URL aURL = new URL(urlImage);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                Log.e("image"," "+encodedImage);
                sharePreferenceManage.CreatePreference("GuestAppTheme", imagePreferenceName, encodedImage,WelcomeActivity.this);
            } catch (Exception e) {
                Log.d("ImageManager", "Error: " + e.toString());
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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


    //region Private Methods

    private TableMaster GetObjectFromPreference() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(objSharePreferenceManage.GetPreference("GuestModePreference", "GuestMode", WelcomeActivity.this));
            objTableMaster = new TableMaster();
            objTableMaster.setTableMasterId((short) jsonObject.getInt("TableMasterId"));
            objTableMaster.setTableName(jsonObject.getString("TableName"));
            objTableMaster.setShortName(jsonObject.getString("ShortName"));
            objTableMaster.setlinktoTableStatusMasterId((short) jsonObject.getInt("linktoTableStatusMasterId"));
            objTableMaster.setlinktoOrderTypeMasterId((short) jsonObject.getInt("linktoOrderTypeMasterId"));
            //objTableMaster.setlinktoSectionMasterId((short) jsonObject.getInt("linktoSectionMasterId"));
            objTableMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));

        } catch (JSONException e) {
            e.printStackTrace();
            objTableMaster = null;
        }
        return objTableMaster;
    }
    //endregion


}
