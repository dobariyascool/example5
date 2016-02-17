package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.modal.WaitingMaster;
import com.arraybit.pos.R;
import com.arraybit.pos.WaitingActivity;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class WaitingListAdapter extends RecyclerView.Adapter<WaitingListAdapter.WaitingListViewHolder> {

    Context context;
    ArrayList<WaitingMaster> alWaitingMaster;
    LayoutInflater layoutInflater;
    View view;
    childLayoutClickListener objChildLayoutClickListener;

    // Constructor
    public WaitingListAdapter(Context context, ArrayList<WaitingMaster> result, childLayoutClickListener objChildLayoutClickListener) {
        this.context = context;
        this.alWaitingMaster = result;
        this.layoutInflater = LayoutInflater.from(context);
        this.objChildLayoutClickListener = objChildLayoutClickListener;
    }

    @Override
    public WaitingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_waiting_list, parent, false);
        return new WaitingListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(WaitingListViewHolder holder, int position) {

        WaitingMaster objWaitingMaster = alWaitingMaster.get(position);

        holder.childLayout.setId(position);
        holder.txtIndex.setText(String.valueOf(position + 1));
        holder.txtName.setText(String.valueOf(objWaitingMaster.getPersonName()));
        holder.txtMobileNo.setText(String.valueOf(objWaitingMaster.getPersonMobile()));
        holder.txtPersons.setText(String.valueOf(objWaitingMaster.getNoOfPersons()));
    }

    @Override
    public int getItemCount() {
        return alWaitingMaster.size();
    }


    public void WaitingListDataRemove(int position,ScaleInAnimationAdapter scaleInAnimationAdapter) {
        alWaitingMaster.remove(position);
        notifyItemRemoved(position);
        //change the index
        scaleInAnimationAdapter.notifyDataSetChanged();
    }

    //region interface
    public interface childLayoutClickListener {
        void ChangeStatusClick(WaitingMaster objWaitingMaster, int position);
    }
    //endregion

    //region ViewHolder
    class WaitingListViewHolder extends RecyclerView.ViewHolder {

        TextView txtIndex, txtName, txtMobileNo, txtPersons;
        LinearLayout childLayout;

        public WaitingListViewHolder(View itemView) {
            super(itemView);

            childLayout = (LinearLayout) itemView.findViewById(R.id.childLayout);

            txtIndex = (TextView) itemView.findViewById(R.id.txtIndex);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtMobileNo = (TextView) itemView.findViewById(R.id.txtMobileNo);
            txtPersons = (TextView) itemView.findViewById(R.id.txtPersons);

            childLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    WaitingActivity activity = (WaitingActivity) context;

                    //fragment outside click not show the dialog
                    if(activity.getSupportFragmentManager().getBackStackEntryCount() == 0) {

                        WaitingMaster objWaitingMaster = new WaitingMaster();

                        objWaitingMaster.setWaitingMasterId(alWaitingMaster.get(v.getId()).getWaitingMasterId());
                        objWaitingMaster.setWaitingStatus(alWaitingMaster.get(v.getId()).getWaitingStatus());
                        objWaitingMaster.setPersonMobile(alWaitingMaster.get(v.getId()).getPersonMobile());

                        objChildLayoutClickListener.ChangeStatusClick(objWaitingMaster, v.getId());
                    }
                }
            });

        }
    }
    //endregion
}
