package com.arraybit.pos;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.arraybit.adapter.NotificationAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.WaiterNotificationMaster;
import com.arraybit.parser.WaiterNotificationJSONParser;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class NotificationDetailActivity extends AppCompatActivity {

    boolean isBackHome;
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
    RecyclerView rvNotification;
    LinearLayout errorLayout;
    int linktoWaiterMasterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        //app_bar
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        //end

        FrameLayout notificationLayout = (FrameLayout)findViewById(R.id.notificationLayout);
        Globals.SetScaleImageBackground(this, null, null, notificationLayout);

        errorLayout = (LinearLayout) findViewById(R.id.errorLayout);
        rvNotification = (RecyclerView)findViewById(R.id.rvNotification);

        isBackHome = getIntent().getBooleanExtra("isBackHome", false);

        if (Service.CheckNet(this)) {
            new NotificationLodingTask().execute();
        }else{
            Globals.SetErrorLayout(errorLayout,true,getResources().getString(R.string.MsgCheckConnection),rvNotification,R.drawable.wifi_drawable);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.logout).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waiter_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            if (isBackHome) {
                Globals.serverName = objSharePreferenceManage.GetPreference("ServerPreference", "ServerName", NotificationDetailActivity.this);
                Globals.SetBusinessMasterId(NotificationDetailActivity.this);
                Globals.isWishListShow = 0;
                Intent i = new Intent(NotificationDetailActivity.this, WaiterHomeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            } else {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isBackHome) {
            Globals.serverName = objSharePreferenceManage.GetPreference("ServerPreference", "ServerName", NotificationDetailActivity.this);
            Globals.SetBusinessMasterId(NotificationDetailActivity.this);
            Globals.isWishListShow = 0;
            Intent i = new Intent(NotificationDetailActivity.this, WaiterHomeActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("unchecked")
    public class NotificationLodingTask extends AsyncTask {
        ArrayList<WaiterNotificationMaster> alWaiterNotificationMaster;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId",NotificationDetailActivity.this)!=null){
                linktoWaiterMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId",NotificationDetailActivity.this));
            }

        }

        @Override
        protected Object doInBackground(Object[] params) {
            WaiterNotificationJSONParser objWaiterNotificationJSONParser = new WaiterNotificationJSONParser();
            return objWaiterNotificationJSONParser.SelectAllWaiterNotificationMaster(linktoWaiterMasterId,60);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            alWaiterNotificationMaster = (ArrayList<WaiterNotificationMaster>) result;
            if(alWaiterNotificationMaster==null){
                Globals.SetErrorLayout(errorLayout,true,getResources().getString(R.string.MsgServerNotResponding),rvNotification,0);
            }else if(alWaiterNotificationMaster.size()==0){
                Globals.SetErrorLayout(errorLayout,true,String.format(getResources().getString(R.string.MsgNoRecordFound),getResources().getString(R.string.notification)),rvNotification,0);
            }else{
                NotificationAdapter notificationAdapter = new NotificationAdapter(NotificationDetailActivity.this,alWaiterNotificationMaster);
                rvNotification.setAdapter(notificationAdapter);
                rvNotification.setVisibility(View.VISIBLE);
                rvNotification.setLayoutManager(new LinearLayoutManager(NotificationDetailActivity.this));
            }
        }
    }
}
