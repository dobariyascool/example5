package com.arraybit.global;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.arraybit.modal.WaiterNotificationMaster;
import com.arraybit.parser.WaiterNotificationJSONParser;
import com.arraybit.pos.NotificationDetailActivity;
import com.arraybit.pos.R;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class NotificationReceiver extends BroadcastReceiver {

    int linktoWaiterMasterId;
    Context context;
    ArrayList<WaiterNotificationMaster> alWaiterNotification;
    ArrayList<String> alString = new ArrayList<>();
    SharePreferenceManage objSharePreferenceManage=new SharePreferenceManage();

    public static void GenerateNotification(Context context, int notificationID, String notificationText, String notificationTitle) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Intent notificationIntent = new Intent(context, NotificationDetailActivity.class);
        if(!POSApplication.isActivityVisible()){
            notificationIntent.putExtra("isBackHome",true);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        if(notificationText==null || notificationText.equals("")){
            notificationText = context.getResources().getString(R.string.notificationText);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        Notification notification = new Notification.Builder(context)
                .setContentText(notificationText)
                .setContentTitle(notificationTitle)
                .setSmallIcon(R.drawable.bell_drawable)
                .setLargeIcon(bitmap)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis()).build();

        notificationManager.notify(notificationID, notification);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        System.out.println("onReceive Called");
        if (Service.CheckNet(context)) {
            new NotificationLodingTask().execute();
        }
    }

    public void CheckDuplicateList(ArrayList<WaiterNotificationMaster> alWaiterList){
        alWaiterNotification = new ArrayList<>();
        boolean isDuplicate = false;
        if (objSharePreferenceManage.GetStringListPreference("NotificationPreference", "NotificationList", context) != null) {
            alString = objSharePreferenceManage.GetStringListPreference("NotificationPreference", "NotificationList", context);
            for(WaiterNotificationMaster objWaiterNotificationMaster : alWaiterList){
                for(String strWaiterMasterId : alString){
                    if(strWaiterMasterId.equals(String.valueOf(objWaiterNotificationMaster.getWaiterNotificationMasterId()))){
                        isDuplicate = true;
                    }
                }
                if(!isDuplicate){
                    GenerateNotification(context, (int) objWaiterNotificationMaster.getWaiterNotificationMasterId(),objWaiterNotificationMaster.getMessage(),objWaiterNotificationMaster.getTable());
                    alString.add(String.valueOf(objWaiterNotificationMaster.getWaiterNotificationMasterId()));
                    objSharePreferenceManage.CreateStringListPreference("NotificationPreference", "NotificationList", alString, context);
                }else{
                    isDuplicate = false;
                }
            }
        }else{
            for(WaiterNotificationMaster objWaiterNotificationMaster : alWaiterList){
                alString.add(String.valueOf(objWaiterNotificationMaster.getWaiterNotificationMasterId()));
                GenerateNotification(context, (int) objWaiterNotificationMaster.getWaiterNotificationMasterId(),objWaiterNotificationMaster.getTable(),objWaiterNotificationMaster.getMessage());
            }
            objSharePreferenceManage.CreateStringListPreference("NotificationPreference", "NotificationList", alString, context);
        }
    }

    @SuppressWarnings("unchecked")
    public class NotificationLodingTask extends AsyncTask {
        ArrayList<WaiterNotificationMaster> alWaiterNotificationMaster;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId",context)!=null){
                linktoWaiterMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId",context));
            }

        }

        @Override
        protected Object doInBackground(Object[] params) {
            WaiterNotificationJSONParser objWaiterNotificationJSONParser = new WaiterNotificationJSONParser();
            return objWaiterNotificationJSONParser.SelectAllWaiterNotificationMaster(linktoWaiterMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
             alWaiterNotificationMaster = (ArrayList<WaiterNotificationMaster>) result;
            if (alWaiterNotificationMaster != null && alWaiterNotificationMaster.size()!=0) {
                CheckDuplicateList(alWaiterNotificationMaster);
            }
        }
    }
}
