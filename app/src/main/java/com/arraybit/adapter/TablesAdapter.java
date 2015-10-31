package com.arraybit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arraybit.modal.TableMaster;
import com.arraybit.pos.R;

import java.util.ArrayList;
import java.util.Objects;

public class TablesAdapter extends RecyclerView.Adapter<TablesAdapter.TableViewHolder> {

    Context context;
    ArrayList<TableMaster> alTableMaster;
    LayoutInflater layoutInflater;
    View view;

    // Constructor
    public TablesAdapter(Context context, ArrayList<TableMaster> result) {
        this.context = context;
        alTableMaster = result;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_tables, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        TableMaster objTableMaster = alTableMaster.get(position);

        if (Objects.equals(objTableMaster.getTableColor(), null)) {
            holder.cvTable.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_orange));
        } else {
            holder.cvTable.setCardBackgroundColor(Color.parseColor("#" + objTableMaster.getTableColor()));
        }
        holder.txtTableName.setText(objTableMaster.getTableName());
        holder.txtPersons.setText(String.valueOf(objTableMaster.getMaxPerson()));
        holder.txtTableStatus.setText(objTableMaster.getTableStatus());
        holder.txtTableStatus.setTextColor(Color.parseColor("#" + objTableMaster.getStatusColor()));
    }

    @Override
    public int getItemCount() {
        return alTableMaster.size();
    }

    public void TableDataChanged(ArrayList<TableMaster> result) {
        alTableMaster.addAll(result);
        notifyDataSetChanged();
    }

    class TableViewHolder extends RecyclerView.ViewHolder {

        TextView txtTableName, txtPersons, txtTableStatus;
        CardView cvTable;

        public TableViewHolder(View itemView) {
            super(itemView);

            txtTableName = (TextView) itemView.findViewById(R.id.txtTableName);
            txtPersons = (TextView) itemView.findViewById(R.id.txtPersons);
            txtTableStatus = (TextView) itemView.findViewById(R.id.txtTableStatus);
            cvTable = (CardView) itemView.findViewById(R.id.cvTable);
        }
    }
}
