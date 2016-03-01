package com.arraybit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.arraybit.modal.TableMaster;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class TablesAdapter extends RecyclerView.Adapter<TablesAdapter.TableViewHolder> {

    public boolean isItemAnimate;
    Context context;
    ArrayList<TableMaster> alTableMaster;
    LayoutInflater layoutInflater;
    View view;
    boolean isClickEnable;
    LayoutClickListener objLayoutClickListener;
    FragmentManager fragmentManager;
    Boolean isAll;
    int previousPosition;

    // Constructor
    public TablesAdapter(Context context, ArrayList<TableMaster> result, boolean isClickEnable, LayoutClickListener objLayoutClickListener, FragmentManager fragmentManager, Boolean isAll, boolean isItemAnimate) {
        this.context = context;
        alTableMaster = result;
        this.isClickEnable = isClickEnable;
        this.layoutInflater = LayoutInflater.from(context);
        this.objLayoutClickListener = objLayoutClickListener;
        this.fragmentManager = fragmentManager;
        this.isAll = isAll;
        this.isItemAnimate = isItemAnimate;
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_tables, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        TableMaster objTableMaster = alTableMaster.get(position);

        holder.cvTable.setId(position);
        if (objTableMaster.getTableColor() == null) {
            holder.cvTable.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_orange));
        } else {
            holder.cvTable.setCardBackgroundColor(Color.parseColor("#" + objTableMaster.getTableColor()));
        }
        holder.txtTableName.setText(objTableMaster.getShortName());
        holder.txtPersons.setText(String.valueOf(objTableMaster.getMaxPerson()));
        holder.txtTableStatus.setText(objTableMaster.getTableStatus());
        holder.txtTableStatus.setTextColor(Color.parseColor("#" + objTableMaster.getStatusColor()));
        if (isAll) {
            holder.txtSection.setVisibility(View.VISIBLE);
            holder.txtSection.setText(objTableMaster.getSection());
        } else {
            holder.txtSection.setVisibility(View.GONE);
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
        return alTableMaster.size();
    }

    public void UpdateData(int position, TableMaster objTableMaster) {
        isItemAnimate = false;
        alTableMaster.get(position).setTableStatus(objTableMaster.getTableStatus());
        alTableMaster.get(position).setStatusColor(objTableMaster.getStatusColor());
        notifyDataSetChanged();
    }

    public void SetSearchFilter(ArrayList<TableMaster> result) {
        isItemAnimate = false;
        alTableMaster = new ArrayList<>();
        alTableMaster.addAll(result);
        notifyDataSetChanged();
    }

    //region interface
    public interface LayoutClickListener {
        void ChangeTableStatusClick(TableMaster objTableMaster, int position);
    }
    //endregion

    class TableViewHolder extends RecyclerView.ViewHolder {

        TextView txtTableName, txtPersons, txtTableStatus, txtSection;
        CardView cvTable;

        public TableViewHolder(View itemView) {
            super(itemView);

            txtTableName = (TextView) itemView.findViewById(R.id.txtTableName);
            txtSection = (TextView) itemView.findViewById(R.id.txtSection);
            txtPersons = (TextView) itemView.findViewById(R.id.txtPersons);
            txtTableStatus = (TextView) itemView.findViewById(R.id.txtTableStatus);
            cvTable = (CardView) itemView.findViewById(R.id.cvTable);

            if (isClickEnable) {

                cvTable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName() != null
                                && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(context.getResources().getString(R.string.title_fragment_all_tables))) {

                            Globals.HideKeyBoard(context, v);
                            objLayoutClickListener.ChangeTableStatusClick(alTableMaster.get(v.getId()), v.getId());
                        }
                    }
                });
            }
        }
    }
}
