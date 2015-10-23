package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arraybit.modal.TableMaster;
import com.arraybit.pos.R;

import java.util.ArrayList;

public class TablesAdapter extends RecyclerView.Adapter<TablesAdapter.TableViewHolder>{

    Context context;
    ArrayList<TableMaster> alTableMaster;
    LayoutInflater layoutInflater;
    View view;

    // Constructor
    public TablesAdapter(Context context, ArrayList<TableMaster> result) {
        this.context = context;
        this.alTableMaster = result;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_tables, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        TableMaster objTableMaster = alTableMaster.get(position);

        holder.txtTableName.setText(objTableMaster.getTableName());
        holder.txtMinPerson.setText("Min Person "+objTableMaster.getMinPerson());
        holder.txtMaxPerson.setText("Max Person "+objTableMaster.getMaxPerson());
        holder.txtTableStatus.setText(objTableMaster.getTableStatus());
        holder.txtDescription.setText(objTableMaster.getDescription());
    }

    @Override
    public int getItemCount() {
        return alTableMaster.size();
    }

    public void TableDataChanged(ArrayList<TableMaster> result){
        alTableMaster.addAll(result);
        notifyDataSetChanged();
    }

    class TableViewHolder extends RecyclerView.ViewHolder {

        TextView txtTableName,txtMinPerson,txtMaxPerson,txtTableStatus,txtDescription;

        public TableViewHolder(View itemView) {
            super(itemView);

            txtTableName = (TextView)itemView.findViewById(R.id.txtTableName);
            txtMinPerson = (TextView)itemView.findViewById(R.id.txtMinPerson);
            txtMaxPerson = (TextView)itemView.findViewById(R.id.txtMaxPerson);
            txtTableStatus = (TextView)itemView.findViewById(R.id.txtTableStatus);
            txtDescription = (TextView)itemView.findViewById(R.id.txtDescription);
        }
    }
}
