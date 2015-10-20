package com.arraybit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arraybit.pos.GuestOrderActivity;
import com.arraybit.pos.R;

import java.util.List;

public class TablesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<String> ItemList;

    public TablesAdapter(List<String> itemList) {
        ItemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_tables, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, GuestOrderActivity.class);
                context.startActivity(intent);

            }
        });

        return TableViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TableViewHolder holder1 = (TableViewHolder) holder;
        String itemText = ItemList.get(position);
        holder1.setItemText(itemText);
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder {

        private final TextView ItemTextView;

        public TableViewHolder(final View parent, TextView itemTextView) {
            super(parent);
            ItemTextView = itemTextView;
        }

        public static TableViewHolder newInstance(View parent) {
            TextView itemTextView = (TextView) parent.findViewById(R.id.textView);
            return new TableViewHolder(parent, itemTextView);
        }

        public void setItemText(CharSequence text) {
            ItemTextView.setText(text);
        }

    }
}
