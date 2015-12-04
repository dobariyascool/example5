package com.arraybit.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.arraybit.modal.OrderMaster;
import com.arraybit.pos.OrderDetailFragment;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    ArrayList<OrderMaster> alOrderMaster;
    LayoutInflater layoutInflater;
    Context context;
    View view;
    FragmentManager fragmentManager;
    Date dt;


    public OrdersAdapter(Context context, ArrayList<OrderMaster> result, FragmentManager fragmentManager) {
        this.context = context;
        alOrderMaster = result;
        this.layoutInflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
    }


    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_orders, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        OrderMaster objOrderMaster = alOrderMaster.get(position);
        holder.cvOrder.setId((int) objOrderMaster.getOrderMasterId());

        holder.txtOrderTime.setText(objOrderMaster.getOrderTime());
        holder.txtTableName.setText(objOrderMaster.getTableName());
        holder.txtOrderNumber.setText(objOrderMaster.getOrderNumber());
        holder.txtOrderType.setText(objOrderMaster.getOrderType());

        holder.txtTotalAmount.setText("Rs. " + Globals.dfWithPrecision.format(objOrderMaster.getTotalAmount()));
    }

    @Override
    public int getItemCount() {
        return alOrderMaster.size();
    }

    public void OrderDataChanged(ArrayList<OrderMaster> result) {
        alOrderMaster.addAll(result);
        notifyDataSetChanged();
    }

    //region interface
    public interface OrderLayoutClickListener {
        void OrderLayoutClick(int orderMasterId);
    }
    //endregion

    class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderTime, txtTableName, txtOrderNumber, txtOrderType, txtTotalAmount;
        CardView cvOrder;

        public OrderViewHolder(View itemView) {
            super(itemView);

            txtOrderTime = (TextView) itemView.findViewById(R.id.txtOrderTime);
            txtTableName = (TextView) itemView.findViewById(R.id.txtTableName);
            txtOrderNumber = (TextView) itemView.findViewById(R.id.txtOrderNumber);
            txtOrderType = (TextView) itemView.findViewById(R.id.txtOrderType);
            txtTotalAmount = (TextView) itemView.findViewById(R.id.txtTotalAmount);

            cvOrder = (CardView) itemView.findViewById(R.id.cvOrder);

            cvOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.allOrdersFragment, new OrderDetailFragment(v.getId()), context.getResources().getString(R.string.title_fragment_order_detail));
                    fragmentTransaction.addToBackStack(context.getResources().getString(R.string.title_fragment_order_detail));
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
