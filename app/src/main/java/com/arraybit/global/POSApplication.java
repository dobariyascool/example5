package com.arraybit.global;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

public class POSApplication extends Application {
    private static boolean activityVisible;
    private static AppCompatActivity appCompatActivity;

    public static AppCompatActivity getAppCompatActivity() {
        return POSApplication.appCompatActivity;
    }

    public static void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        POSApplication.appCompatActivity = appCompatActivity;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }
}
