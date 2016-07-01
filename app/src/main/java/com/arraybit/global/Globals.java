package com.arraybit.global;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.transition.Fade;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.arraybit.modal.DiscountMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderItemTran;
import com.arraybit.pos.CategoryItemFragment;
import com.arraybit.pos.FeedbackFragment;
import com.arraybit.pos.GuestLoginDialogFragment;
import com.arraybit.pos.MenuActivity;
import com.arraybit.pos.MyAccountFragment;
import com.arraybit.pos.R;
import com.arraybit.pos.SignInActivity;
import com.arraybit.pos.SignUpFragment;
import com.arraybit.pos.WishListActivity;
import com.rey.material.util.TypefaceUtil;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"ResourceType", "unchecked"})
public class Globals {


    public static final String AboutUs="About Us";
    public static final String PrivacyPolicy="Privacy Policy";
    public static final String TermsOfService="Terms Of Service";
    public static String activityName;
    public static String serverName = null;
    public static String DateFormat = "d/M/yyyy";
    public static String TimeFormat = "hh:mm";
    public static String DateTimeFormat = "d/M/yyyy/H/m";
    public static String DisplayTimeFormat = "h:mm a";
    public static String FullTimeFormat = "H:mm a";
    public static int sourceMasterId = 3;
    public static short customerType = 2;
    public static short businessMasterId;
    public static short businessTypeMasterId;
    public static short itemType = 2;
    public static DecimalFormat dfWithPrecision = new DecimalFormat("0.00");
    public static int counter = 0;
    public static ArrayList<ItemMaster> alOrderItemTran = new ArrayList<>();
    public static ArrayList<OrderItemTran> alOrderItemSummery = new ArrayList<>();
    public static ArrayList<Long> alOrderMasterId = new ArrayList<>();
    public static short orderTypeMasterId=0;
    public static short isWishListShow = 0;
    public static short selectTableMasterId;
    public static Fragment targetFragment;
    public static DiscountMaster objDiscountMaster;
    public static String userName;
    static FragmentManager fragmentManager;
    static int y, M, d, H, m;


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

    public static void SetBusinessMasterId(Context context) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("WaiterPreference", "linktoBusinessMasterId", context) != null) {
            String str = objSharePreferenceManage.GetPreference("WaiterPreference", "linktoBusinessMasterId", context);
            if (str != null && !str.equals("")) {
                Globals.businessMasterId = Short.parseShort(str);
            }
            if (objSharePreferenceManage.GetPreference("WaiterPreference", "linktoBusinessTypeMasterId", context) != null) {
                String strBusinessTypeMaster = objSharePreferenceManage.GetPreference("WaiterPreference", "linktoBusinessTypeMasterId", context);
                if (str != null && !str.equals("")) {
                    Globals.businessTypeMasterId = Short.parseShort(strBusinessTypeMaster);
                }
            }

        } else {
            Globals.businessMasterId = 1;
        }
    }

    public static void ChangeUrl() {
        Service.Url = "http://" + Globals.serverName + "/Service.svc/";
    }

    public static void TextViewFontTypeFace(com.rey.material.widget.TextView textView, Context context) {
        Typeface roboto = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Regular.ttf"); //use this.getAssets if you are calling from an Activity
        textView.setTypeface(roboto);
    }

    public static void ButtonFontTypeFace(Button button, Context context) {
        Typeface roboto = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Regular.ttf"); //use this.getAssets if you are calling from an Activity
        button.setTypeface(roboto);
    }

    public static void EditTextFontTypeFace(EditText editText, Context context) {
        Typeface roboto = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Regular.ttf"); //use this.getAssets if you are calling from an Activity
        editText.setTypeface(roboto);
    }

    public static void NavigationViewFontTypeFace(NavigationView navigationView, Context context) {
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            MenuItem menuItem = navigationView.getMenu().getItem(i);
            SpannableString spannableString = new SpannableString(menuItem.getTitle());
            spannableString.setSpan(TypefaceUtil.load(context, "fonts/Roboto-Regular.ttf", 0), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            menuItem.setTitle(spannableString);
        }
    }

    public static void CallNotificationReceiver(Activity activity){
        Calendar calendar = Calendar.getInstance();

        //intent registerd the broadcast receiver
        Intent myIntent = new Intent(activity, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, myIntent, 0);

        //set the repeating alarm
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 20 * 1000, pendingIntent);
    }

    public static void SetAppBarPadding(Toolbar app_bar) {
        if (Build.VERSION.SDK_INT == 19) {
            app_bar.setPadding(0, 16, 0, 0);
        } else if (Build.VERSION.SDK_INT >= 21) {
            app_bar.setPadding(0, 50, 0, 0);
        }
    }

    public static void ShowDatePickerDialog(final EditText txtView, Context context) {
        final Calendar c = Calendar.getInstance();

        if (!txtView.getText().toString().equals("")) {
            SimpleDateFormat sdfControl = new SimpleDateFormat(DateFormat, Locale.US);
            try {
                Date dt = sdfControl.parse(String.valueOf(txtView.getText()));
                c.setTime(dt);
            } catch (ParseException ignored) {
            }
        }

        y = c.get(Calendar.YEAR);
        M = c.get(Calendar.MONTH);
        d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        y = year;
                        M = monthOfYear;
                        d = dayOfMonth;

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);

                        SimpleDateFormat sdfControl = new SimpleDateFormat(DateFormat, Locale.US);
                        txtView.setText(sdfControl.format(cal.getTime()));
                    }

                }, y, M, d);

        dp.hide();
        dp.show();
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
        if (Build.VERSION.SDK_INT >= 21) {
            snackView.setElevation(R.dimen.snackbar_elevation);
        }
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
        MenuItem mLogout = menu.findItem(R.id.logout);

        if (userName == null) {
            mLogin.setTitle(context.getResources().getString(R.string.navLogin)).setVisible(true);
            mLogout.setVisible(false);
        } else {
            mLogin.setTitle(context.getResources().getString(R.string.wmMyAccount)).setVisible(true);
            mLogout.setVisible(true);
        }
    }

    public static void ShowTimePickerDialog(final TextView txtView, final Context context) {
        final Calendar c = Calendar.getInstance();

        if (!txtView.getText().toString().equals("")) {
            SimpleDateFormat sdfControl = new SimpleDateFormat(TimeFormat, Locale.US);
            try {
                Date dt = sdfControl.parse(String.valueOf(txtView.getText()));
                c.setTime(dt);
            } catch (ParseException ignored) {
            }
        }

        H = c.get(Calendar.HOUR_OF_DAY);
        m = c.get(Calendar.MINUTE);

        TimePickerDialog dp = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        H = hourOfDay;
                        m = minute;

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, 0);
                        cal.set(Calendar.MONTH, 0);
                        cal.set(Calendar.DAY_OF_MONTH, 0);
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);

                        SimpleDateFormat sdfControl = new SimpleDateFormat(TimeFormat, Locale.US);
                        txtView.setText(sdfControl.format(cal.getTime()));

                    }

                }, H, m, true);

        dp.hide();
        dp.show();
    }


    public static void OptionMenuItemClick(MenuItem menuItem, Activity activity, FragmentManager fragmentManager) {
        activityName = activity.getTitle().toString();
        if (menuItem.getItemId() != android.R.id.home) {
            if (activityName.equals(activity.getResources().getString(R.string.title_fragment_about_us))) {
                if (menuItem.getTitle() == activity.getResources().getString(R.string.navLogin)) {
                    GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
                    guestLoginDialogFragment.show(fragmentManager, "");
                } else if (menuItem.getTitle() == activity.getResources().getString(R.string.navRegistration)) {
                    Globals.ReplaceAnimatedFragment(new SignUpFragment(), fragmentManager, activity.getResources().getString(R.string.title_fragment_signup));
                } else if (menuItem.getTitle() == activity.getResources().getString(R.string.wmLogout)) {
                    ClearPreference(activity.getApplication());
                    Globals.userName = null;
                } else if (menuItem.getTitle() == activity.getResources().getString(R.string.wmMyAccount)) {
                    Globals.ReplaceAnimatedFragment(new MyAccountFragment(), fragmentManager, activity.getResources().getString(R.string.title_fragment_myaccount));
                }else if(menuItem.getTitle()==activity.getResources().getString(R.string.navShortList)){
                    Intent intent = new Intent(activity, WishListActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            } else if (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName() != null &&
                    fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(activity.getResources().getString(R.string.title_fragment_feedback))) {
                if (menuItem.getTitle() == activity.getResources().getString(R.string.navLogin)) {
                    GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
                    Fragment currentFragment = fragmentManager.findFragmentByTag(activity.getResources().getString(R.string.title_fragment_feedback));
                    guestLoginDialogFragment.setTargetFragment(currentFragment, 0);
                    guestLoginDialogFragment.show(fragmentManager, "");
                } else if (menuItem.getTitle() == activity.getResources().getString(R.string.navRegistration)) {
                    FeedbackFragment currentFragment = (FeedbackFragment) fragmentManager.findFragmentByTag(activity.getResources().getString(R.string.title_fragment_feedback));
                    currentFragment.ReplaceFragment(new SignUpFragment(), activity.getResources().getString(R.string.title_fragment_signup));
                } else if (menuItem.getTitle() == activity.getResources().getString(R.string.wmLogout)) {
                    ClearPreference(activity.getApplication());
                    Globals.userName = null;
                    FeedbackFragment currentFragment = (FeedbackFragment) fragmentManager.findFragmentByTag(activity.getResources().getString(R.string.title_fragment_feedback));
                    currentFragment.LoginResponse();
                } else if (menuItem.getTitle() == activity.getResources().getString(R.string.wmMyAccount)) {
                    FeedbackFragment currentFragment = (FeedbackFragment) fragmentManager.findFragmentByTag(activity.getResources().getString(R.string.title_fragment_feedback));
                    currentFragment.ReplaceFragment(new MyAccountFragment(), activity.getResources().getString(R.string.title_fragment_myaccount));
                }else if(menuItem.getTitle()==activity.getResources().getString(R.string.navShortList)){
                    Intent intent = new Intent(activity, WishListActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            } else if (MenuActivity.parentActivity && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName() == null) {
            } else {
                if (menuItem.getTitle() == activity.getResources().getString(R.string.navLogin)) {
                    GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
                    guestLoginDialogFragment.show(fragmentManager, "");
                } else if (menuItem.getTitle() == activity.getResources().getString(R.string.navRegistration)) {
                    Globals.ReplaceAnimatedFragment(new SignUpFragment(), fragmentManager, activity.getResources().getString(R.string.title_fragment_signup));
                } else if (menuItem.getTitle() == activity.getResources().getString(R.string.wmLogout)) {
                    ClearPreference(activity.getApplication());
                    Globals.userName = null;
                } else if (menuItem.getTitle() == activity.getResources().getString(R.string.wmMyAccount)) {
                    Globals.ReplaceAnimatedFragment(new MyAccountFragment(), fragmentManager, activity.getResources().getString(R.string.title_fragment_myaccount));
                }else if(menuItem.getTitle()==activity.getResources().getString(R.string.navShortList)){
                    Intent intent = new Intent(activity, WishListActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        }
    }

    public static void SetRecyclerLayoutAnimation(boolean isGridLayoutManager, RecyclerView recyclerView, Context context) {
        if (!isGridLayoutManager) {
            Animation animation = android.view.animation.AnimationUtils.loadAnimation(context,
                    R.anim.slide_in_down);
            LayoutAnimationController controller = new LayoutAnimationController(animation);
            recyclerView.setLayoutAnimation(controller);
        } else {
            Animation animation = android.view.animation.AnimationUtils.loadAnimation(context,
                    R.anim.slide_in_down_grid);
            LayoutAnimationController controller = new LayoutAnimationController(animation);
            recyclerView.setLayoutAnimation(controller);
        }
    }

    public static void SetItemAnimator(RecyclerView.ViewHolder holder) {
        //slide from bottom
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", 200, 0);
        animatorTranslateY.setDuration(500);
        animatorTranslateY.start();
    }

    public static void ClearPreference(Context context) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        objSharePreferenceManage.RemovePreference("RegistrationPreference", "UserName", context);
        objSharePreferenceManage.RemovePreference("RegistrationPreference", "CustomerMasterId", context);
        objSharePreferenceManage.RemovePreference("RegistrationPreference", "FullName", context);
        objSharePreferenceManage.RemovePreference("RegistrationPreference", "FirstName", context);
        objSharePreferenceManage.RemovePreference("NotificationOnTimePreference", "OnTime", context);
        objSharePreferenceManage.RemovePreference("NotificationOffTimePreference", "OffTime", context);
        objSharePreferenceManage.RemovePreference("NotificationSettingPreference", "Push", context);
        objSharePreferenceManage.ClearPreference("RegistrationPreference", context);
        objSharePreferenceManage.ClearPreference("NotificationOnTimePreference", context);
        objSharePreferenceManage.ClearPreference("NotificationOffTimePreference", context);
        objSharePreferenceManage.ClearPreference("NotificationSettingPreference", context);
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

    public static void ReplaceAnimatedFragment(Fragment fragment, FragmentManager fragmentManager, String fragmentName) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Fade fade = new Fade();
            fade.setDuration(500);
            fragment.setEnterTransition(fade);
        } else {
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }


    public static void ReplaceFragment(Fragment fragment, FragmentManager fragmentManager, String fragmentName) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(350);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    public static void SetErrorLayout(LinearLayout layout, boolean isShow, String errorMsg, RecyclerView recyclerView, int errorIcon) {
        TextView txtMsg = (TextView) layout.findViewById(R.id.txtMsg);
        ImageView ivErrorIcon = (ImageView) layout.findViewById(R.id.ivErrorIcon);
        if (errorIcon != 0) {
            ivErrorIcon.setImageResource(errorIcon);
        }
        if (isShow) {
            layout.setVisibility(View.VISIBLE);
            txtMsg.setText(errorMsg);
            if (recyclerView != null) {
                recyclerView.setVisibility(View.GONE);
            }
        } else {
            layout.setVisibility(View.GONE);
            if (recyclerView != null) {
                recyclerView.setVisibility(View.VISIBLE);
            }
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

        // if (Build.VERSION.SDK_INT < 21) {
        Intent intent = new Intent(activity, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        activity.finish();
        //  } else {
//            ActivityOptions options =
//                    ActivityOptions.
//                            makeSceneTransitionAnimation(activity);
//            Intent intent = new Intent(activity, SignInActivity.class);
//            activity.startActivity(intent, options.toBundle());
//            activity.finish();
//        }

        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        objSharePreferenceManage.RemovePreference("WaiterPreference", "UserName", activity);
        objSharePreferenceManage.RemovePreference("WaiterPreference", "UserMasterId", activity);
        objSharePreferenceManage.RemovePreference("WaiterPreference", "UserTypeMasterId", activity);
        objSharePreferenceManage.RemovePreference("WaiterPreference", "UserSecurityCode", activity);
        objSharePreferenceManage.RemovePreference("WaiterPreference", "WaiterMasterId", activity);
        objSharePreferenceManage.RemovePreference("NotificationPreference", "NotificationList", activity);

        Globals.counter = 0;
        Globals.alOrderItemTran.clear();
        Globals.selectTableMasterId = 0;

        objSharePreferenceManage.ClearPreference("WaiterPreference", activity);
        objSharePreferenceManage.ClearPreference("NotificationPreference", activity);
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

    public static void EnableBroadCastReceiver(Activity activity){
        ComponentName receiver = new ComponentName(activity, NotificationReceiver.class);
        PackageManager pm = activity.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void DisableBroadCastReceiver(Activity activity){
        ComponentName receiver = new ComponentName(activity, NotificationReceiver.class);
        PackageManager pm = activity.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
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
        Dirty("FF9800"),
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
        Veg(3),
        NonVeg(4),
        Jain(5),
        Spicy(2),
        Sweet(1),
        DoubleSpicy(8);


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





