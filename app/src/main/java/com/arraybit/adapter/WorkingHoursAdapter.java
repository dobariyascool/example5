package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arraybit.global.Globals;
import com.arraybit.modal.BusinessHoursTran;
import com.arraybit.pos.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Richa on 29-10-2015.
 */
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

    @Override
    public void onBindViewHolder(WorkingHoursViewHolder holder, int position) {

        BusinessHoursTran current = alBusinessHoursTran.get(position);

        try {
            dt = sdfTimeFormat.parse(current.getOpeningTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (position == 0) {
            holder.layoutHeader.setVisibility(View.VISIBLE);
            holder.txtDayOfWeek.setText(String.valueOf(Globals.Days.valueOf("Day" + current.getDayOfWeek()).getValue()));

            if (sdfTimeFormat.format(dt).equals("00")) {
                holder.txtOpeningTime.setText("--Close--");
                holder.txtClosingTime.setText("");
            } else {
                holder.txtOpeningTime.setText(String.valueOf(current.getOpeningTime()));
                holder.txtClosingTime.setText(String.valueOf(current.getClosingTime()));
            }
        } else {
            holder.layoutHeader.setVisibility(View.GONE);
            holder.txtDayOfWeek.setText(String.valueOf(Globals.Days.valueOf("Day" + current.getDayOfWeek()).getValue()));

            if (sdfTimeFormat.format(dt).equals("00")) {
                holder.txtOpeningTime.setText("--Close--");
                holder.txtClosingTime.setText("");
            } else {
                holder.txtOpeningTime.setText(String.valueOf(current.getOpeningTime()));
                holder.txtClosingTime.setText(String.valueOf(current.getClosingTime()));
            }

        }
    }

    @Override
    public int getItemCount() {
        return alBusinessHoursTran.size();
    }

    class WorkingHoursViewHolder extends RecyclerView.ViewHolder {
        TextView txtDayOfWeek, txtOpeningTime, txtClosingTime;
        LinearLayout layoutHeader;

        public WorkingHoursViewHolder(View itemView) {
            super(itemView);

            txtDayOfWeek = (TextView) itemView.findViewById(R.id.txtDayOfWeek);
            txtOpeningTime = (TextView) itemView.findViewById(R.id.txtOpeningTime);
            txtClosingTime = (TextView) itemView.findViewById(R.id.txtClosingTime);
            layoutHeader = (LinearLayout) itemView.findViewById(R.id.layoutHeader);
        }
    }

}
