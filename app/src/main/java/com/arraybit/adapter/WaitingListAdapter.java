package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.modal.Person;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class WaitingListAdapter extends RecyclerView.Adapter<WaitingListAdapter.WaitingListViewHolder> {

    Context context;
    ArrayList<Person> alPerson;
    LayoutInflater layoutInflater;
    View view;
    WaitingList waitingList;

    // Constructor
    public WaitingListAdapter(Context context, ArrayList<Person> result) {
        this.context = context;
        this.alPerson = result;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public WaitingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_waiting_list, parent, false);
        return new WaitingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WaitingListViewHolder holder, int position) {

        Person objPerson = alPerson.get(position);

        if (position == 0) {
            holder.headerLayout.setVisibility(View.VISIBLE);
        } else {
            holder.headerLayout.setVisibility(View.GONE);
        }
        holder.txtIndex.setText(String.valueOf(objPerson.getIndex()));
        holder.txtName.setText(String.valueOf(objPerson.getName()));
        holder.txtMobileNo.setText(String.valueOf(objPerson.getMobile()));
        holder.txtPersons.setText(String.valueOf(objPerson.getPerson()));
    }

    @Override
    public int getItemCount() {
        return alPerson.size();
    }

    public interface WaitingList {
        void ChangeStatus();
    }

    class WaitingListViewHolder extends RecyclerView.ViewHolder {

        TextView txtIndex, txtName, txtMobileNo, txtPersons;
        LinearLayout headerLayout, childLayout;

        public WaitingListViewHolder(View itemView) {
            super(itemView);

            headerLayout = (LinearLayout) itemView.findViewById(R.id.headerLayout);
            childLayout = (LinearLayout) itemView.findViewById(R.id.childLayout);

            txtIndex = (TextView) itemView.findViewById(R.id.txtIndex);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtMobileNo = (TextView) itemView.findViewById(R.id.txtMobileNo);
            txtPersons = (TextView) itemView.findViewById(R.id.txtPersons);

            childLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    waitingList = (WaitingList) context;
                    waitingList.ChangeStatus();
                }
            });
        }
    }
}
