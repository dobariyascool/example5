package com.arraybit.global;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arraybit.modal.DiscountMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderItemTran;
import com.arraybit.pos.CategoryItemFragment;
import com.arraybit.pos.FeedbackFragment;
import com.arraybit.pos.GuestLoginDialogFragment;
import com.arraybit.pos.R;
import com.arraybit.pos.SignInActivity;
import com.arraybit.pos.SignUpFragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ResourceType")
public class Globals {


    public static String activityName;
    public static String serverName = null;
    public static String DateFormat = "d/M/yyyy";
    public static String TimeFormat = "hh:mm";
    public static String DisplayTimeFormat = "h:mm a";
    public static int sourceMasterId = 2;
    public static short businessMasterId = 1;
    public static short itemType = 2;
    public static DecimalFormat dfWithPrecision = new DecimalFormat("0.00");
    public static int counter = 0;
    public static ArrayList<ItemMaster> alOrderItemTran = new ArrayList<>();
    public static ArrayList<OrderItemTran> alOrderItemSummery = new ArrayList<>();
    public static ArrayList<Long> alOrderMasterId = new ArrayList<>();
    public static short selectTableMasterId;
    public static Fragment targetFragment;
    public static DiscountMaster objDiscountMaster;
    public static String userName;
    static FragmentManager fragmentManager;

    public static float ConvertDp(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float pixel = dp * (dm.densityDpi) / 160.0F;
        return pixel;
    }

    public static float ConvertPixcel(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float dp = px * (dm.densityDpi) / 160.0F;
        return dp;
    }

    public static void ChangeUrl() {
        Service.Url = "http://" + Globals.serverName + "/Service.svc/";
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

    @SuppressLint("SimpleDateFormat")
    public static String GetCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("h/m");
        return sdf.format(calendar.getTime());
    }

    public static void ShowSnackBar(View view, String message, Context context, int duration) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        View snackView = snackbar.getView();
        TextView txt = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        txt.setGravity(Gravity.CENTER);
        txt.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_grey));
        snackbar.show();
    }

    public static void SetNavigationDrawer(ActionBarDrawerToggle actionBarDrawerToggle, final Context context, final DrawerLayout drawerLayout, Toolbar app_bar, final FragmentManager fragmentManager) {
        actionBarDrawerToggle = new ActionBarDrawerToggle((Activity) context, drawerLayout, app_bar,
                R.string.navOpen, R.string.navClose) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                if ((((Activity) context).getTitle().equals(context.getResources().getString(R.string.title_activity_waiting)) && fragmentManager.getBackStackEntryCount() == 0) || (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName() != null
                        && ((fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().
                        equals(context.getResources().getString(R.string.title_fragment_guest_options)) ||
                        (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().
                                equals(context.getResources().getString(R.string.title_fragment_waiter_options))))))) {
                    super.onDrawerClosed(drawerView);
                } else {
                    drawerLayout.closeDrawer(drawerView);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                if ((((Activity) context).getTitle().equals(context.getResources().getString(R.string.title_activity_waiting)) && fragmentManager.getBackStackEntryCount() == 0)
                        || (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName() != null
                        && ((fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().
                        equals(context.getResources().getString(R.string.title_fragment_guest_options)) ||
                        (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().
                                equals(context.getResources().getString(R.string.title_fragment_waiter_options))))))) {
                    super.onDrawerOpened(drawerView);
                } else {
                    drawerLayout.closeDrawer(drawerView);
                }
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
            mLogin.setTitle(context.getResources().getString(R.string.navLogin)).setVisible(true);
            mRegistration.setTitle(context.getResources().getString(R.string.navRegistration));
        } else {
            mLogin.setTitle(context.getResources().getString(R.string.wmMyAccount)).setVisible(false);
            mRegistration.setTitle(context.getResources().getString(R.string.wmLogout));
        }
    }

    public static void OptionMenuItemClick(MenuItem menuItem, Activity activity, FragmentManager fragmentManager) {
        activityName = activity.getTitle().toString();
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName() != null &&
                fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(activity.getResources().getString(R.string.title_fragment_feedback))) {
            if (menuItem.getTitle() == activity.getResources().getString(R.string.navLogin)) {
                GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
                Fragment currentFragment = fragmentManager.findFragmentByTag(activity.getResources().getString(R.string.title_fragment_feedback));
                guestLoginDialogFragment.setTargetFragment(currentFragment, 0);
                guestLoginDialogFragment.show(fragmentManager, "");
            } else if (menuItem.getTitle() == activity.getResources().getString(R.string.navRegistration)) {
                FeedbackFragment currentFragment = (FeedbackFragment) fragmentManager.findFragmentByTag(activity.getResources().getString(R.string.title_fragment_feedback));
                currentFragment.RemoveFragment();
                Globals.ReplaceFragment(new SignUpFragment(), fragmentManager, activity.getResources().getString(R.string.title_fragment_signup));

            } else if (menuItem.getTitle() == activity.getResources().getString(R.string.wmLogout)) {
                objSharePreferenceManage.RemovePreference("RegistrationPreference", "UserName", activity);
                objSharePreferenceManage.RemovePreference("RegistrationPreference", "RegisteredUserMasterId", activity);
                objSharePreferenceManage.RemovePreference("RegistrationPreference", "FullName", activity);
                objSharePreferenceManage.ClearPreference("RegistrationPreference", activity);
                Globals.userName = null;
                FeedbackFragment currentFragment = (FeedbackFragment) fragmentManager.findFragmentByTag(activity.getResources().getString(R.string.title_fragment_feedback));
                currentFragment.LoginResponse();
            }
        } else {
            if (menuItem.getTitle() == activity.getResources().getString(R.string.navLogin)) {
                GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
                guestLoginDialogFragment.show(fragmentManager, "");

            } else if (menuItem.getTitle() == activity.getResources().getString(R.string.navRegistration)) {
                Globals.ReplaceFragment(new SignUpFragment(), fragmentManager, activity.getResources().getString(R.string.title_fragment_signup));
            } else if (menuItem.getTitle() == activity.getResources().getString(R.string.wmLogout)) {
                objSharePreferenceManage.RemovePreference("RegistrationPreference", "UserName", activity);
                objSharePreferenceManage.RemovePreference("RegistrationPreference", "RegisteredUserMasterId", activity);
                objSharePreferenceManage.RemovePreference("RegistrationPreference", "FullName", activity);
                objSharePreferenceManage.ClearPreference("RegistrationPreference", activity);
                Globals.userName = null;
            }
        }
    }

    public static void ClearPreference(Context context) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        objSharePreferenceManage.RemovePreference("RegistrationPreference", "UserName", context);
        objSharePreferenceManage.RemovePreference("RegistrationPreference", "RegisteredUserMasterId", context);
        objSharePreferenceManage.RemovePreference("RegistrationPreference", "FullName", context);
        objSharePreferenceManage.ClearPreference("RegistrationPreference", context);
        Globals.userName = null;
    }

    public static void SetScaleImageBackground(final Context context, final LinearLayout linearLayout, final RelativeLayout relativeLayout, final FrameLayout frameLayout) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mainbackground);
        Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels);

        if (relativeLayout != null) {
            relativeLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        } else if (frameLayout != null) {
            frameLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        } else if (linearLayout != null) {
            linearLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        }
    }

    public static void SetScaleImageBackground(final Context context, final CoordinatorLayout coordinatorLayout) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mainbackground);
        Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels);

        coordinatorLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
    }

    public static void SetHomePageBackground(final Context context, final LinearLayout linearLayout, final RelativeLayout relativeLayout, final FrameLayout frameLayout) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels);

        if (relativeLayout != null) {
            relativeLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        } else if (frameLayout != null) {
            frameLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        } else if (linearLayout != null) {
            linearLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        }
    }

    public static void InitializeFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.animator.slide_up,R.animator.slide_down,R.animator.slide_up,R.animator.slide_down);
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
        //fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.slide_up,R.anim.slide_down);
        //fragmentTransaction.setCustomAnimations(R.anim.jump_from_down,R.anim.jump_to_down,R.anim.jump_from_down,R.anim.jump_to_down);
        //fragmentTransaction.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_out_left,android.R.anim.slide_out_right,android.R.anim.slide_out_left);
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void InitializeAnimatedFragment(Fragment fragment, FragmentManager fragmentManager, String fragmentName) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    public static void ReplaceFragment(Fragment fragment, FragmentManager fragmentManager, String fragmentName) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(500);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
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

        if (Build.VERSION.SDK_INT < 21) {
            Intent intent = new Intent(activity, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            activity.finish();
        } else {
            ActivityOptions options =
                    ActivityOptions.
                            makeSceneTransitionAnimation(activity);
            Intent intent = new Intent(activity, SignInActivity.class);
            activity.startActivity(intent, options.toBundle());
            activity.finish();
        }

        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        objSharePreferenceManage.RemovePreference("WaiterPreference", "UserName", activity);
        objSharePreferenceManage.RemovePreference("WaiterPreference", "UserMasterId", activity);
        objSharePreferenceManage.RemovePreference("WaiterPreference", "UserTypeMasterId", activity);
        objSharePreferenceManage.RemovePreference("WaiterPreference", "UserSecurityCode", activity);
        objSharePreferenceManage.RemovePreference("WaiterPreference", "WaiterMasterId", activity);

        Globals.counter = 0;
        Globals.alOrderItemTran.clear();
        Globals.selectTableMasterId = 0;

        objSharePreferenceManage.ClearPreference("WaiterPreference", activity);
    }

    public static void ClearData() {
        Globals.counter = 0;
        Globals.alOrderItemTran.clear();
        Globals.selectTableMasterId = 0;
        Globals.alOrderItemSummery = new ArrayList<>();
        Globals.targetFragment = null;
    }


    public static void HideNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 17 && Build.VERSION.SDK_INT < 19) {
            View v = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            v.setSystemUiVisibility(uiOptions);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.FOCUS_DOWN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    //set runtime orientation for mobile and tablet
    public static void RuntimeChangeOrientation(Activity activity) {
        if (activity.getResources().getBoolean(R.bool.isTablet)) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public static void CategoryItemFragmentResetStaticVariable() {
        CategoryItemFragment.isViewChange = false;
        Globals.targetFragment = null;
        CategoryItemFragment.i = 0;
        CategoryItemFragment.objCategoryMaster = null;
        CategoryItemFragment.sbItemTypeMasterId = new StringBuilder();
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
        Occupied(2),
        Dirty(3),
        Block(4);


        private int intValue;

        TableStatus(int value) {
            intValue = value;

        }

        public int getValue() {
            return intValue;
        }

    }

    public enum TableStatusColor {
        Vacant("4CAF50"),
        Occupied("2196F3"),
        Dirty("795548"),
        Block("F44336");

        private String strValue;

        TableStatusColor(String value) {
            strValue = value;
        }

        public String getValue() {
            return strValue;
        }

    }

    public enum OrderStatus {
        All(0),
        Cooking(1),
        Ready(2),
        Served(3),
        Cancelled(4);

        private int intValue;

        OrderStatus(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }

    public enum OrderType {
        DineIn(1),
        TakeAway(2),
        HomeDelivery(3);

        private int intValue;

        OrderType(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }

    public enum UserType {
        Captain(1),
        Waiter(2),
        Delivery_Person(3),
        Waiting(4);

        private int intValue;

        UserType(int value) {
            intValue = value;

        }

        public int getValue() {
            return intValue;
        }
    }

    public enum FeedbackQuestionType {
        Input(1),
        Rating(2),
        Single_Select(3),
        Multi_Select(4),
        Simple_Feedback(5),
        Null_Group(6);

        private int intValue;

        FeedbackQuestionType(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }

    public enum OptionValue {
        Veg(10),
        NonVeg(11),
        Jain(12);

        private int intValue;

        OptionValue(int value) {
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





