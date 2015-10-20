package com.arraybit.global;


import android.app.Activity;
import android.content.Context;
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
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arraybit.pos.GuestLoginDialogFragment;
import com.arraybit.pos.GuestRegistrationDialogFragment;
import com.arraybit.pos.R;

public class Globals {


    public static String activityName;
    public static String DateFormat = "d/M/yyyy";
    public static String TimeFormat = "HH:mm";
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
            GuestRegistrationDialogFragment guestRegistrationDialogFragment = new GuestRegistrationDialogFragment();
            guestRegistrationDialogFragment.show(fragmentManager, "");
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

    public static void initializeFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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





