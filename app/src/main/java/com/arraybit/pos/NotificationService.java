package com.arraybit.pos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.arraybit.global.NotificationReceiver;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    Intent intent;
    final Calendar calendar = Calendar.getInstance();

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        return START_STICKY;
    }

    public void onCreate(){
        super.onCreate();
        Intent myIntent = new Intent(this, NotificationReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);

        //set the repeating alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        int delay = 1000; // delay for 1 sec.
        int period = 9000; // repeat every 9 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }, delay, period);
//        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
    }


    public void onDestroyed(){
//        super.onDestroy();
        stopService(intent);
        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        Intent i = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }
}
