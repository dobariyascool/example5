package com.arraybit.global;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.arraybit.pos.SplashScreenActivity;
import com.arraybit.pos.WaitingTabFragment;

import java.util.Calendar;

public class TimeScheduler extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
//        if(SplashScreenActivity.isTime > 0){
//            WaitingListFragment.RefreshCurrentTab();
//        }
//        SplashScreenActivity.isTime=1;
    }

    //region Refersh data code
    //add in splash screen
    /*public static int isTime=0;

    //region Set Timer to refresh data
    Calendar calendar = Calendar.getInstance();

    Intent intent = new Intent(SplashScreenActivity.this, TimeScheduler.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(SplashScreenActivity.this, 0,intent, 0);

    //set the repeating alarm
    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 1, pendingIntent);
    //endregion

    //add waiting list fragment
    static int currentTabPosition;
    static WaitingListPagerAdapter obj;

    //refresh current tab after some time
    public static void RefreshCurrentTab(){
        WaitingTabFragment waitingTabFragment = (WaitingTabFragment) obj.GetCurrentFragment(currentTabPosition);
        waitingTabFragment.LoadWaitingListData();
    }*/
    //end
    //end
    //endregion
}
