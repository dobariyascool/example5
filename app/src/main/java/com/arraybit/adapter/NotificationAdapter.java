package com.arraybit.adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.arraybit.modal.WaiterNotificationMaster;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    public boolean isItemAnimate;
    Context context;
    ArrayList<WaiterNotificationMaster> alWaiterNotificationMaster;
    LayoutInflater layoutInflater;
    View view;
    int previousPosition;

    // Constructor
    public NotificationAdapter(Context context, ArrayList<WaiterNotificationMaster> result) {
        this.context = context;
        this.alWaiterNotificationMaster = result;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        WaiterNotificationMaster objWaiterNotificationMaster = alWaiterNotificationMaster.get(position);

        holder.txtTableName.setText(objWaiterNotificationMaster.getTable());
        holder.txtNotificationTime.setText(objWaiterNotificationMaster.getNotificationTime());
        if (objWaiterNotificationMaster.getMessage() == null || objWaiterNotificationMaster.getMessage().equals("")) {
            holder.txtNotificationText.setText(context.getResources().getString(R.string.notificationText));
        } else {
            holder.txtNotificationText.setText(objWaiterNotificationMaster.getMessage());
        }
        //holder animation
        if (isItemAnimate) {
            if (position > previousPosition) {
                Globals.SetItemAnimator(holder);
            }
            previousPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return alWaiterNotificationMaster.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView txtTableName, txtNotificationTime, txtNotificationText;
        CardView cvTable;
        ImageView ivClear;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            txtTableName = (TextView) itemView.findViewById(R.id.txtTableName);
            txtNotificationTime = (TextView) itemView.findViewById(R.id.txtNotificationTime);
            txtNotificationText = (TextView) itemView.findViewById(R.id.txtNotificationText);
            ivClear = (ImageView) itemView.findViewById(R.id.ivClear);
            cvTable = (CardView) itemView.findViewById(R.id.cvTable);
        }
    }
}
