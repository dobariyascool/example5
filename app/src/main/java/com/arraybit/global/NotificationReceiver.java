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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.modal.WaiterNotificationMaster;
import com.arraybit.parser.WaiterNotificationJSONParser;
import com.arraybit.pos.NotificationDetailActivity;
import com.arraybit.pos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("unchecked")
public class NotificationReceiver extends BroadcastReceiver{

    public static int notificationCount=0;
    public final String SelectAllWaiterNotificationMaster = "SelectAllWaiterNotificationMaster";
    int linktoWaiterMasterId;
    Context context;
    ArrayList<WaiterNotificationMaster> alWaiterNotification;
    ArrayList<String> alString = new ArrayList<>();
    ArrayList<Integer> alNotificationId = new ArrayList<>();
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat(Globals.DateTimeFormat, Locale.US);
    int cnt = 0;

    public static void GenerateNotification(Context context, int notificationID, String notificationText, String notificationTitle, boolean isSoundPlay) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;

        Intent notificationIntent = new Intent(context, NotificationDetailActivity.class);
        if (!POSApplication.isActivityVisible()) {
            notificationIntent.putExtra("isBackHome", true);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (notificationText == null || notificationText.equals("")) {
            notificationText = context.getResources().getString(R.string.notificationText);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        if (isSoundPlay) {
            notification = new Notification.Builder(context)
                    .setContentText(notificationText)
                    .setContentTitle(notificationTitle)
                    .setSmallIcon(R.mipmap.call_waiter)
                    .setLargeIcon(bitmap)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setWhen(System.currentTimeMillis()).build();
        } else {
            notification = new Notification.Builder(context)
                    .setContentText(notificationText)
                    .setContentTitle(notificationTitle)
                    .setSmallIcon(R.mipmap.call_waiter)
                    .setLargeIcon(bitmap)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis()).build();
        }
        notificationManager.notify(notificationID, notification);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (Service.CheckNet(context)) {
            if(objSharePreferenceManage.GetPreference("ServerPreference", "ServerName",context)!=null);
            {
                Globals.serverName = objSharePreferenceManage.GetPreference("ServerPreference", "ServerName",context);
                Globals.ChangeUrl();
                RequestNotification();
            }
        }
    }

    public void CheckDuplicateList(ArrayList<WaiterNotificationMaster> alWaiterList) {
        alWaiterNotification = new ArrayList<>();
        boolean isDuplicate = false, isFirst = false;
        String date = new SimpleDateFormat(Globals.DateFormat, Locale.US).format(new Date());
        if (objSharePreferenceManage.GetPreference("NotificationPreference", "TodaysDate", context) != null) {
            if (objSharePreferenceManage.GetPreference("NotificationPreference", "TodaysDate", context).equals(date)) {
                if (objSharePreferenceManage.GetStringListPreference("NotificationPreference", "NotificationList", context) != null) {
                    alString = objSharePreferenceManage.GetStringListPreference("NotificationPreference", "NotificationList", context);
                    for(String str : alString) alNotificationId.add(Integer.valueOf(str));
                    Collections.sort(alNotificationId,Collections.reverseOrder());
                    for (WaiterNotificationMaster objWaiterNotificationMaster : alWaiterList) {
                        if (alNotificationId.size() > 4) {
                            for (int i = 0; i < 5; i++) {
                                if (alNotificationId.get(i)==objWaiterNotificationMaster.getWaiterNotificationMasterId()) {
                                    isDuplicate = true;
                                }
                            }
                        } else {
                            for (Integer waiterMasterId : alNotificationId) {
                                if (waiterMasterId==objWaiterNotificationMaster.getWaiterNotificationMasterId()) {
                                    isDuplicate = true;
                                }
                            }
                        }
                        if (!isDuplicate) {
                            if (!isFirst) {
                                isFirst = true;
                                GenerateNotification(context, (int) objWaiterNotificationMaster.getWaiterNotificationMasterId(), objWaiterNotificationMaster.getMessage(), objWaiterNotificationMaster.getTable(), true);
                            } else {
                                GenerateNotification(context, (int) objWaiterNotificationMaster.getWaiterNotificationMasterId(), objWaiterNotificationMaster.getMessage(), objWaiterNotificationMaster.getTable(), false);
                            }
                            if(POSApplication.isActivityVisible()){
                                notificationCount++;
                            }
                            alString.add(String.valueOf(objWaiterNotificationMaster.getWaiterNotificationMasterId()));
                            objSharePreferenceManage.CreateStringListPreference("NotificationPreference", "NotificationList", alString, context);
                        } else {
                            isDuplicate = false;
                        }
                        if (cnt == 4) {
                            cnt = 0;
                            isFirst = false;
                            break;
                        } else {
                            cnt++;
                        }
                    }
                } else {
                    for (WaiterNotificationMaster objWaiterNotificationMaster : alWaiterList) {
                        alString.add(String.valueOf(objWaiterNotificationMaster.getWaiterNotificationMasterId()));
                        if (cnt == 0) {
                            GenerateNotification(context, (int) objWaiterNotificationMaster.getWaiterNotificationMasterId(), objWaiterNotificationMaster.getTable(), objWaiterNotificationMaster.getMessage(), true);
                        } else {
                            GenerateNotification(context, (int) objWaiterNotificationMaster.getWaiterNotificationMasterId(), objWaiterNotificationMaster.getTable(), objWaiterNotificationMaster.getMessage(), false);
                        }
                        if (cnt == 4) {
                            cnt = 0;
                            break;
                        } else {
                            cnt++;
                        }
                    }
                    objSharePreferenceManage.CreateStringListPreference("NotificationPreference", "NotificationList", alString, context);
                }
            } else {
                objSharePreferenceManage.RemovePreference("NotificationPreference", "TodaysDate", context);
                objSharePreferenceManage.RemovePreference("NotificationPreference", "NotificationList", context);
                objSharePreferenceManage.ClearPreference("NotificationPreference", context);
                for (WaiterNotificationMaster objWaiterNotificationMaster : alWaiterList) {
                    alString.add(String.valueOf(objWaiterNotificationMaster.getWaiterNotificationMasterId()));
                    if (cnt == 0) {
                        GenerateNotification(context, (int) objWaiterNotificationMaster.getWaiterNotificationMasterId(), objWaiterNotificationMaster.getTable(), objWaiterNotificationMaster.getMessage(), true);
                    } else {
                        GenerateNotification(context, (int) objWaiterNotificationMaster.getWaiterNotificationMasterId(), objWaiterNotificationMaster.getTable(), objWaiterNotificationMaster.getMessage(), false);
                    }
                    if (cnt == 4) {
                        cnt = 0;
                        break;
                    } else {
                        cnt++;
                    }
                }
                objSharePreferenceManage.CreateStringListPreference("NotificationPreference", "NotificationList", alString, context);
                objSharePreferenceManage.CreatePreference("NotificationPreference", "TodaysDate", date, context);
            }
        } else {
            for (WaiterNotificationMaster objWaiterNotificationMaster : alWaiterList) {
                alString.add(String.valueOf(objWaiterNotificationMaster.getWaiterNotificationMasterId()));
                if (cnt == 0) {
                    GenerateNotification(context, (int) objWaiterNotificationMaster.getWaiterNotificationMasterId(), objWaiterNotificationMaster.getTable(), objWaiterNotificationMaster.getMessage(), true);
                } else {
                    GenerateNotification(context, (int) objWaiterNotificationMaster.getWaiterNotificationMasterId(), objWaiterNotificationMaster.getTable(), objWaiterNotificationMaster.getMessage(), false);
                }
                if (cnt == 4) {
                    cnt = 0;
                    break;
                } else {
                    cnt++;
                }
            }
            objSharePreferenceManage.CreateStringListPreference("NotificationPreference", "NotificationList", alString, context);
            objSharePreferenceManage.CreatePreference("NotificationPreference", "TodaysDate", date, context);
        }
    }


    private void RequestNotification() {
        if (objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", context) != null) {
            linktoWaiterMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", context));
        }
        final WaiterNotificationJSONParser objWaiterNotificationJSONParser = new WaiterNotificationJSONParser();
        Date date = new Date(System.currentTimeMillis() - 30 * 60 * 1000);
        String url = Service.Url + this.SelectAllWaiterNotificationMaster + "/" + linktoWaiterMasterId + "/" + sdfDateFormat.format(date);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                JSONArray jsonArray = null;
                try {

                    jsonArray = jsonObject.getJSONArray(SelectAllWaiterNotificationMaster + "Result");

                    if (jsonArray != null) {
                        ArrayList<WaiterNotificationMaster> alWaiterNotificationMaster = objWaiterNotificationJSONParser.SetListPropertiesFromJSONArray(jsonArray);
                        if (alWaiterNotificationMaster != null && alWaiterNotificationMaster.size() != 0) {
                            CheckDuplicateList(alWaiterNotificationMaster);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        queue.add(jsonObjectRequest);
    }


    @SuppressWarnings("unchecked")
    public class NotificationLodingTask extends AsyncTask {
        ArrayList<WaiterNotificationMaster> alWaiterNotificationMaster;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", context) != null) {
                linktoWaiterMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", context));
            }

        }

        @Override
        protected Object doInBackground(Object[] params) {
            WaiterNotificationJSONParser objWaiterNotificationJSONParser = new WaiterNotificationJSONParser();
            return objWaiterNotificationJSONParser.SelectAllWaiterNotificationMaster(linktoWaiterMasterId, 30);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            alWaiterNotificationMaster = (ArrayList<WaiterNotificationMaster>) result;
            if (alWaiterNotificationMaster != null && alWaiterNotificationMaster.size() != 0) {
                CheckDuplicateList(alWaiterNotificationMaster);
            }
        }
    }
}


