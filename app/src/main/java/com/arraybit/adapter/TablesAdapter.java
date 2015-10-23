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

        holder.ItemTextView.setText(objTableMaster.getTableName());
    }

    @Override
    public int getItemCount() {
        return alTableMaster.size();
    }

    class TableViewHolder extends RecyclerView.ViewHolder {

        TextView ItemTextView;

        public TableViewHolder(View itemView) {
            super(itemView);
            ItemTextView = (TextView)itemView.findViewById(R.id.textView);
        }
    }
}
