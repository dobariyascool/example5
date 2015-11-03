package com.arraybit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.pos.GuestOrderActivity;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<String> mItemList;

    public OrdersAdapter(List<String> itemList) {
        mItemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_orders, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, GuestOrderActivity.class);
                context.startActivity(intent);
            }
        });

        return RecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerItemViewHolder holder1 = (RecyclerItemViewHolder) holder;
        String itemText = mItemList.get(position);
        holder1.setItemText(itemText);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public static class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView mItemTextView;

        public RecyclerItemViewHolder(final View parent, TextView itemTextView) {
            super(parent);
            mItemTextView = itemTextView;
        }

        public static RecyclerItemViewHolder newInstance(View parent) {
            TextView itemTextView = (TextView) parent.findViewById(R.id.textView);
            return new RecyclerItemViewHolder(parent, itemTextView);
        }

        public void setItemText(CharSequence text) {
            mItemTextView.setText(text);
        }

    }
}
