package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.arraybit.modal.BusinessHoursTran;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class WorkingHoursAdapter extends RecyclerView.Adapter<WorkingHoursAdapter.WorkingHoursViewHolder> {

    ArrayList<BusinessHoursTran> alBusinessHoursTran;
    Context context;
    View view;
    Date dt = null;
    SimpleDateFormat sdfTimeFormat = new SimpleDateFormat("HH", Locale.US);
    private LayoutInflater inflater;

    public WorkingHoursAdapter(Context context, ArrayList<BusinessHoursTran> result) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.alBusinessHoursTran = result;
    }

    @Override
    public WorkingHoursViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_workinghours, parent, false);
        return new WorkingHoursViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(WorkingHoursViewHolder holder, int position) {

        BusinessHoursTran current = alBusinessHoursTran.get(position);

        try {
            dt = sdfTimeFormat.parse(current.getOpeningTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (position == 0) {
            holder.txtHeader.setVisibility(View.VISIBLE);
        } else {
            holder.txtHeader.setVisibility(View.GONE);
        }
        holder.txtDayOfWeek.setText(String.valueOf(Globals.Days.valueOf("Day" + current.getDayOfWeek()).getValue()));

        if (current.getOpeningTime().equals("12:00 AM")) {
            holder.txtStartTime.setText("Close");
            holder.txtEndTime.setVisibility(View.GONE);
        } else {
            if (current.getBreakStartTime() == null && current.getBreakEndTime() == null) {
                holder.txtStartTime.setText(current.getOpeningTime() + " " + context.getString(R.string.rTo) + " " + current.getClosingTime());
                holder.txtEndTime.setVisibility(View.GONE);
            } else {
                holder.txtStartTime.setText(current.getOpeningTime() + " " + context.getString(R.string.rTo) + " " + current.getBreakStartTime());
                holder.txtEndTime.setText(current.getBreakEndTime() + " " + context.getString(R.string.rTo) + " " + current.getClosingTime());
            }
        }

    }

    @Override
    public int getItemCount() {
        return alBusinessHoursTran.size();
    }

    class WorkingHoursViewHolder extends RecyclerView.ViewHolder {
        TextView txtDayOfWeek, txtStartTime, txtEndTime, txtHeader;

        public WorkingHoursViewHolder(View itemView) {
            super(itemView);

            txtDayOfWeek = (TextView) itemView.findViewById(R.id.txtDayOfWeek);
            txtStartTime = (TextView) itemView.findViewById(R.id.txtStartTime);
            txtEndTime = (TextView) itemView.findViewById(R.id.txtEndTime);
            txtHeader = (TextView) itemView.findViewById(R.id.txtHeader);
        }
    }

}
