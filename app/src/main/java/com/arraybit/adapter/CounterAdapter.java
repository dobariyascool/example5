package com.arraybit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CounterMaster;
import com.arraybit.pos.R;
import com.arraybit.pos.WelcomeActivity;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.CounterViewHolder> {
    Context context;
    ArrayList<CounterMaster> alCounterMaster;
    View view;
    SharePreferenceManage objSharePreferenceManage;
    short userType;
    private LayoutInflater inflater;

    public CounterAdapter(Context context, ArrayList<CounterMaster> alCounterMaster,short userType) {
        this.context = context;
        this.alCounterMaster = alCounterMaster;
        inflater = LayoutInflater.from(context);
        this.userType = userType;
    }

    @Override
    public CounterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_counter, parent, false);
        return new CounterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CounterViewHolder holder, int position) {
        CounterMaster current = alCounterMaster.get(position);
        holder.txtCounterTitle.setText(current.getCounterName());
    }

    @Override
    public int getItemCount() {
        return alCounterMaster.size();
    }

    class CounterViewHolder extends RecyclerView.ViewHolder {
        CardView cvCounter;
        TextView txtCounterTitle;

        public CounterViewHolder(View itemView) {
            super(itemView);
            txtCounterTitle = (TextView) itemView.findViewById(R.id.txtCounterTitle);

            cvCounter = (CardView) itemView.findViewById(R.id.cvCounter);

            cvCounter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    objSharePreferenceManage = new SharePreferenceManage();
                    objSharePreferenceManage.CreatePreference("CounterPreference", "CounterMasterId", String.valueOf(alCounterMaster.get(getAdapterPosition()).getCounterMasterId()), context);
                    objSharePreferenceManage.CreatePreference("CounterPreference", "CounterName", alCounterMaster.get(getAdapterPosition()).getCounterName(), context);

                    Intent intent = new Intent(context, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("UserType",userType);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });
        }
    }
}
