package com.arraybit.global;

import android.app.Application;

public class POSApplication extends Application {
    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }
}
