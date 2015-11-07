package com.arraybit.global;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arraybit.pos.GuestLoginDialogFragment;
import com.arraybit.pos.R;
import com.arraybit.pos.SignInActivity;
import com.arraybit.pos.SignUpFragment;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ResourceType")
public class Globals {


    public static String activityName;
    public static String DateFormat = "d/M/yyyy";
    public static String TimeFormat = "HH:mm";
    public static int sourceMasterId = 2;
    public static DecimalFormat dfWithPrecision = new DecimalFormat("0.00");
    static FragmentManager fragmentManager;
    //public static Bitmap bitmap1;

    public static float ConvertDp(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float pixel = dp * (dm.densityDpi) / 160.0F;
        return pixel;
    }

    public static void SetAppBarPadding(Toolbar app_bar) {
        if (Build.VERSION.SDK_INT == 19) {
            app_bar.setPadding(0, 16, 0, 0);
        } else if (Build.VERSION.SDK_INT >= 21) {
            app_bar.setPadding(0, 50, 0, 0);
        }
    }

    public static boolean IsValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void SetNavigationDrawer(ActionBarDrawerToggle actionBarDrawerToggle, Context context, DrawerLayout drawerLayout, Toolbar app_bar) {
        actionBarDrawerToggle = new ActionBarDrawerToggle((Activity) context, drawerLayout, app_bar,
                R.string.navOpen, R.string.navClose) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        final ActionBarDrawerToggle finalActionBarDrawerToggle = actionBarDrawerToggle;
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                finalActionBarDrawerToggle.syncState();
            }
        });
    }

    public static void SetOptionMenu(String userName, Context context, Menu menu) {
        MenuItem mLogin = menu.findItem(R.id.login);
        MenuItem mRegistration = menu.findItem(R.id.registration);

        if (userName == null) {
            mLogin.setTitle(context.getResources().getString(R.string.navLogin));
            mRegistration.setTitle(context.getResources().getString(R.string.navRegistration));
        } else {
            mLogin.setTitle(context.getResources().getString(R.string.wmMyAccount));
            mRegistration.setTitle(context.getResources().getString(R.string.wmLogout));
        }
    }

    public static void OptionMenuItemClick(MenuItem menuItem, Activity activity, FragmentManager fragmentManager) {
        activityName = activity.getTitle().toString();
        if (menuItem.getTitle() == activity.getResources().getString(R.string.navLogin)) {
            GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
            guestLoginDialogFragment.show(fragmentManager, "");

        } else if (menuItem.getTitle() == activity.getResources().getString(R.string.navRegistration)) {
            SignUpFragment signUpFragment = new SignUpFragment();
            Globals.InitializeFragment(signUpFragment, fragmentManager);
//            GuestRegistrationDialogFragment guestRegistrationDialogFragment = new GuestRegistrationDialogFragment();
//            guestRegistrationDialogFragment.show(fragmentManager, "");
        }
    }

    public static void SetScaleImageBackground(final Context context, final LinearLayout linearLayout, final RelativeLayout relativeLayout) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mainbackground);
        Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels);

        if (relativeLayout == null) {
            linearLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));

        } else {
            relativeLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        }
    }

    public static void InitializeFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.animator.slide_up,R.animator.slide_down,R.animator.slide_up,R.animator.slide_down);
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
        //fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.slide_up,R.anim.slide_down);
        //fragmentTransaction.setCustomAnimations(R.anim.jump_from_down,R.anim.jump_to_down,R.anim.jump_from_down,R.anim.jump_to_down);
        //fragmentTransaction.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right,android.R.anim.slide_in_left);
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void InitializeAnimatedFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void SetErrorLayout(LinearLayout layout, boolean isShow, String errorMsg) {
        TextView txtMsg = (TextView) layout.findViewById(R.id.txtMsg);
        ImageView ivErrorIcon = (ImageView) layout.findViewById(R.id.ivErrorIcon);
        if (isShow) {
            layout.setVisibility(View.VISIBLE);
            txtMsg.setText(errorMsg);
        } else {
            layout.setVisibility(View.GONE);

        }
    }

    public static void SetError(TextView txtMsg, RecyclerView recyclerView, String errorMsg, boolean isErrorShow) {
        if (isErrorShow) {
            txtMsg.setVisibility(View.VISIBLE);
            txtMsg.setText(errorMsg);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtMsg.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public static void HideKeyBoard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void ClearPreference(Activity activity) {

        activityName = activity.getTitle().toString();

        if (activityName.equals(activity.getResources().getString(R.string.title_activity_waiting))) {
            Intent intent = new Intent(activity, SignInActivity.class);
            activity.startActivity(intent);
            activity.finish();

            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            objSharePreferenceManage.RemovePreference("WaitingPreference", "UserName", activity);
            objSharePreferenceManage.RemovePreference("WaitingPreference", "UserMasterId", activity);
            objSharePreferenceManage.RemovePreference("WaitingPreference", "UserTypeMasterId", activity);

            objSharePreferenceManage.ClearPreference("WaitingPreference", activity);
        } else if (activityName.equals(activity.getResources().getString(R.string.title_activity_waiter_home))) {
            Intent intent = new Intent(activity, SignInActivity.class);
            activity.startActivity(intent);
            activity.finish();

            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            objSharePreferenceManage.RemovePreference("WaiterPreference", "UserName", activity);
            objSharePreferenceManage.RemovePreference("WaiterPreference", "UserMasterId", activity);
            objSharePreferenceManage.RemovePreference("WaiterPreference", "UserTypeMasterId", activity);

            objSharePreferenceManage.ClearPreference("WaiterPreference", activity);
        }
    }

    //set runtime orientation for mobile and tablet
    public static void RuntimeChangeOrientation(Activity activity){
        if (activity.getResources().getBoolean(R.bool.isTablet)) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        else{
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    //region Enum

    public enum WaitingStatus {
        Waiting(1),
        Assign(2),
        Cancel(3),
        NA(4);

        private int intValue;

        WaitingStatus(int value) {
            intValue = value;

        }

        public int getValue() {
            return intValue;
        }
    }

    public enum Days {
        Day0("Sunday"),
        Day1("Monday"),
        Day2("Tuesday"),
        Day3("Wednesday"),
        Day4("Thursday"),
        Day5("Friday"),
        Day6("Saturday");

        private String intValue;

        Days(String value) {
            intValue = value;

        }

        public String getValue() {
            return intValue;
        }
    }


    public enum FeedbackType {
        OtherQuery(1),
        BugReport(2),
        Suggestion(3);

        private int intValue;

        FeedbackType(int value) {
            intValue = value;

        }

        public int getValue() {
            return intValue;
        }

    }

    public enum TableStatus {
        Vacant(1),
        Busy(2);

        private int intValue;

        TableStatus(int value) {
            intValue = value;

        }

        public int getValue() {
            return intValue;
        }

    }

    public enum ItemType{
        Veg(1),
        NonVeg(2),
        Jain(3);

        private int intValue;

        ItemType(int value) {
            intValue = value;

        }

        public int getValue() {
            return intValue;
        }
    }

    //endregion

    //region CommentCode

    //load image
    //        Picasso.with(context)
//                .load(R.drawable.mainbackground)
//                .resize(displayMetrics.widthPixels, displayMetrics.heightPixels)
//                .centerCrop()
//                .into(new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        if (bitmap != null) {
//                            if(relativeLayout==null){
//                                linearLayout.setBackground(new BitmapDrawable(context.getResources(), bitmap));
//                            }
//                            else {
//                                relativeLayout.setBackground(new BitmapDrawable(context.getResources(),bitmap));
//                            }
//
//                        }
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable errorDrawable) {
//
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });
    //end

    //endregion
}





