package com.arraybit.pos;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;


@SuppressWarnings("ConstantConditions")
public class WelcomeActivity extends Activity implements GestureDetector.OnGestureListener {

    GestureDetectorCompat gestureDetector;

    SharePreferenceManage objSharePreferenceManage;
    short count=0;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //get server name
        objSharePreferenceManage = new SharePreferenceManage();
        Globals.serverName = objSharePreferenceManage.GetPreference("ServerPreference", "ServerName", WelcomeActivity.this);
        //end

        DrawerLayout mainLayout = (DrawerLayout) findViewById(R.id.mainLayout);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(count==0) {
                    count++;
                    objSharePreferenceManage = new SharePreferenceManage();
                    String userTypeMasterId = objSharePreferenceManage.GetPreference("WaiterPreference", "UserTypeMasterId", WelcomeActivity.this);
                    if (userTypeMasterId != null && userTypeMasterId.equals(String.valueOf(Globals.UserType.valueOf("Waiter").getValue()))) {
                        Intent i = new Intent(WelcomeActivity.this, WaiterHomeActivity.class);
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                    } else {
                        Intent i = new Intent(WelcomeActivity.this, WaitingActivity.class);
                        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                    }
                }
                return false;
            }
        });


        //gestureDetector = new GestureDetectorCompat(this, this);
//        gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
//            @Override
//            public boolean onSingleTapConfirmed(MotionEvent e) {
//                Toast.makeText(WelcomeActivity.this,"Single Tap Confirm",Toast.LENGTH_LONG).show();
//                return false;
//            }
//
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                Toast.makeText(WelcomeActivity.this,"Double Tap",Toast.LENGTH_LONG).show();
//                return false;
//            }
//
//            @Override
//            public boolean onDoubleTapEvent(MotionEvent e) {
//                Toast.makeText(WelcomeActivity.this,"Double Tap Event",Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Registering TouchEvent with GestureDetector
        return gestureDetector.onTouchEvent(event);
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

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @SuppressWarnings("StringEquality")
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        objSharePreferenceManage = new SharePreferenceManage();
        String userTypeMasterId = objSharePreferenceManage.GetPreference("WaiterPreference", "UserTypeMasterId", WelcomeActivity.this);
        if (userTypeMasterId != null && userTypeMasterId.equals("1")) {
            Intent i = new Intent(WelcomeActivity.this, WaiterHomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(WelcomeActivity.this, WaitingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
        return false;
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void setupWindowAnimations() {
//        Slide slideTransition = new Slide();
//        slideTransition.setSlideEdge(Gravity.LEFT);
//        slideTransition.setDuration(1000);
//        getWindow().setReenterTransition(slideTransition);
//        getWindow().setExitTransition(slideTransition);
//    }
}
