package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.arraybit.modal.OrderItemTran;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>  {

    Context context;
    ArrayList<OrderItemTran> alOrderItemTran;
    LayoutInflater layoutInflater;
    View view;

    public OrderDetailAdapter(Context context, ArrayList<OrderItemTran> result) {
        this.context = context;
        alOrderItemTran = result;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public OrderDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderDetailViewHolder holder, int position) {
        OrderItemTran objOrderItemTran = alOrderItemTran.get(position);

        holder.txtIndex.setText(String.valueOf(position + 1));
        holder.txtName.setText(objOrderItemTran.getItem());
        holder.txtPrice.setText(Globals.dfWithPrecision.format(objOrderItemTran.getRate()));
        holder.txtQty.setText(String.valueOf(objOrderItemTran.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return alOrderItemTran.size();
    }



    class OrderDetailViewHolder extends RecyclerView.ViewHolder {

        TextView txtIndex, txtName, txtPrice, txtQty;

        public OrderDetailViewHolder(View itemView) {
            super(itemView);

            txtIndex = (TextView) itemView.findViewById(R.id.txtIndex);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtQty = (TextView) itemView.findViewById(R.id.txtQty);

        }
    }
}
