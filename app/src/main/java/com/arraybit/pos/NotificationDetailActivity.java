package com.arraybit.pos;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.adapter.NotificationAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.WaiterNotificationMaster;
import com.arraybit.parser.WaiterNotificationJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class NotificationDetailActivity extends AppCompatActivity implements NotificationAdapter.OnClickListener {

    boolean isBackHome;
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
    RecyclerView rvNotification;
    LinearLayout errorLayout;
    int linktoWaiterMasterId, linktoUserMasterId, position;
    NotificationAdapter notificationAdapter;
    WaiterNotificationMaster objWaiterNotificationTran;
    FrameLayout notificationLayout;
    ItemTouchHelper.SimpleCallback simpleItemTouchHelper;
    ArrayList<WaiterNotificationMaster> alWaiterNotificationMaster;
    TextView txtMsg;
    ImageView ivErrorIcon;

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

        notificationLayout = (FrameLayout) findViewById(R.id.notificationLayout);


        errorLayout = (LinearLayout) findViewById(R.id.errorLayout);
        txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);
        ivErrorIcon = (ImageView) errorLayout.findViewById(R.id.ivErrorIcon);
        rvNotification = (RecyclerView) findViewById(R.id.rvNotification);

        isBackHome = getIntent().getBooleanExtra("isBackHome", false);

        Globals.SetToolBarBackground(this, app_bar, ContextCompat.getColor(this, R.color.primary_black), ContextCompat.getColor(this, android.R.color.white));
        notificationLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.background_img));

        if (Service.CheckNet(this)) {
            new NotificationLodingTask().execute();
        } else {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), rvNotification, R.drawable.wifi_drawable);
        }


        simpleItemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                position = viewHolder.getAdapterPosition();
                objWaiterNotificationTran = alWaiterNotificationMaster.get(viewHolder.getAdapterPosition());
                if (Service.CheckNet(NotificationDetailActivity.this)) {
                    new InsertLodingTask().execute();
                } else {
                    Globals.ShowSnackBar(errorLayout, getResources().getString(R.string.MsgCheckConnection), NotificationDetailActivity.this, 1000);
                }

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rvNotification);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.notification_layout).setVisible(false);
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
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(0, R.anim.right_exit);
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
            setResult(RESULT_OK);
            super.onBackPressed();
        }
    }

    @Override
    public void OnRemoveClick(WaiterNotificationMaster objWaiterNotificationMaster, int position) {
        objWaiterNotificationTran = objWaiterNotificationMaster;
        this.position = position;
        if (Service.CheckNet(this)) {
            new InsertLodingTask().execute();
        } else {
            Globals.ShowSnackBar(errorLayout, getResources().getString(R.string.MsgCheckConnection), NotificationDetailActivity.this, 1000);
        }
    }

    private void SetRecyclerView(ArrayList<WaiterNotificationMaster> alWaiterNotificationMaster) {
        if (alWaiterNotificationMaster == null) {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvNotification, 0);
        } else if (alWaiterNotificationMaster.size() == 0) {
            Globals.SetErrorLayout(errorLayout, true, String.format(getResources().getString(R.string.MsgNoRecordFound), getResources().getString(R.string.notification)), rvNotification, 0);
        } else {
            notificationAdapter = new NotificationAdapter(NotificationDetailActivity.this, alWaiterNotificationMaster, this);
            rvNotification.setAdapter(notificationAdapter);
            rvNotification.setVisibility(View.VISIBLE);
            rvNotification.setLayoutManager(new LinearLayoutManager(NotificationDetailActivity.this));
        }
    }

    @SuppressWarnings("unchecked")
    public class NotificationLodingTask extends AsyncTask {
        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getSupportFragmentManager(), "");
            if (objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", NotificationDetailActivity.this) != null) {
                linktoWaiterMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", NotificationDetailActivity.this));
            }

        }

        @Override
        protected Object doInBackground(Object[] params) {
            WaiterNotificationJSONParser objWaiterNotificationJSONParser = new WaiterNotificationJSONParser();
            return objWaiterNotificationJSONParser.SelectAllWaiterNotificationMaster(linktoWaiterMasterId, 60);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            alWaiterNotificationMaster = (ArrayList<WaiterNotificationMaster>) result;
            SetRecyclerView(alWaiterNotificationMaster);
        }
    }

    public class InsertLodingTask extends AsyncTask {
        com.arraybit.pos.ProgressDialog progressDialog;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getSupportFragmentManager(), "");
            if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", NotificationDetailActivity.this) != null) {
                linktoUserMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", NotificationDetailActivity.this));
            }
            objWaiterNotificationTran.setLinktoUserMasterId((short) linktoUserMasterId);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            WaiterNotificationJSONParser objWaiterNotificationJSONParser = new WaiterNotificationJSONParser();
            status = objWaiterNotificationJSONParser.InsertWaiterNotificationTran(objWaiterNotificationTran);
            return status;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (status.equals("-1")) {
                Globals.ShowSnackBar(notificationLayout, getResources().getString(R.string.MsgServerNotResponding), NotificationDetailActivity.this, 2000);
            } else if (status.equals("0")) {
                notificationAdapter.NotificationDataRemove(position);
                if (alWaiterNotificationMaster.size() == 0) {
                    Globals.SetErrorLayout(errorLayout, true, String.format(getResources().getString(R.string.MsgNoRecordFound), getResources().getString(R.string.notification)), rvNotification, 0);
                }
            }
        }
    }
}
