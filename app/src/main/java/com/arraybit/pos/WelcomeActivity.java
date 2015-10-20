package com.arraybit.pos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class WelcomeActivity extends Activity implements GestureDetector.OnGestureListener {

    GestureDetector gestureDetector;
    String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        gestureDetector=new GestureDetector(this,this);

        Intent intent = getIntent();
        if(intent.getStringExtra("username")!=null)
        {
            UserName=intent.getStringExtra("username");
        }
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

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getX() > e2.getX() && velocityY < 1000)
        {
            switch (UserName) {
                case "g": {
                    Intent intent = new Intent(WelcomeActivity.this, GuestHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("username", UserName);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                    break;
                }
                case "w": {
                    Intent intent = new Intent(WelcomeActivity.this, WaiterHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                    break;
                }
                case "wl": {
                    Intent intent = new Intent(WelcomeActivity.this, WaitingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                    break;
                }
                default: {
                    Intent intent = new Intent(WelcomeActivity.this, WaiterHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                    break;
                }
            }

            return true;
        }
        return false;
    }
}
