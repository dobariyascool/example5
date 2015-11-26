package com.arraybit.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.arraybit.modal.OrderMaster;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    public static int orderMasterId;
    ArrayList<OrderMaster> alOrderMaster;
    LayoutInflater layoutInflater;
    Context context;
    View view;
    OrderLayoutClickListener objOrderLayoutClickListener;
    FragmentManager fragmentManager;


    public OrdersAdapter(Context context, ArrayList<OrderMaster> result ,FragmentManager fragmentManager) {
        this.context = context;
        alOrderMaster = result;
        this.layoutInflater = LayoutInflater.from(context);
        this.objOrderLayoutClickListener = objOrderLayoutClickListener;
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
        holder.txtTableName.setText(objOrderMaster.getTableName());
        holder.txtOrderNumber.setText(objOrderMaster.getOrderNumber());
        holder.txtOrderType.setText(objOrderMaster.getOrderType());

        holder.txtTotalAmount.setText("Rs. "+Globals.dfWithPrecision.format(objOrderMaster.getTotalAmount()));
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

        TextView txtTableName, txtOrderNumber, txtOrderType, txtTotalAmount;
        CardView cvOrder;

        public OrderViewHolder(View itemView) {
            super(itemView);

            txtTableName = (TextView) itemView.findViewById(R.id.txtTableName);
            txtOrderNumber = (TextView) itemView.findViewById(R.id.txtOrderNumber);
            txtOrderType = (TextView) itemView.findViewById(R.id.txtOrderType);
            txtTotalAmount = (TextView) itemView.findViewById(R.id.txtTotalAmount);

            cvOrder = (CardView) itemView.findViewById(R.id.cvOrder);

            cvOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    orderMasterId = v.getId();

                    objOrderLayoutClickListener = (OrderLayoutClickListener)context;
                    objOrderLayoutClickListener.OrderLayoutClick(orderMasterId);
                    //OrderDetailFragment detailFragment = new  OrderDetailFragment();
                    //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    //fragmentTransaction.replace(R.id.allOrderFragment,detailFragment,"detailFragment");
                    //fragmentTransaction.addToBackStack("detailFragment");
                    //fragmentTransaction.commit();
                    //Intent intent = new Intent(context,WaiterOrderDetailActivity.class);
                   // context.startActivity(intent);
                    //objOrderLayoutClickListener = (OrderLayoutClickListener) context;
                    //objOrderLayoutClickListener.OrderLayoutClick(v.getId());
                    //Globals.InitializeFragment(new OrderDetailFragment(v.getId()),fragmentManager);
                    //objOrderLayoutClickListener = (OrderLayoutClickListener) context;
                    //objOrderLayoutClickListener.OrderLayoutClick(v.getId());

//                    OrderDetailFragment detailFragment = new  OrderDetailFragment(v.getId());
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.allOrderFragment, detailFragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();

                }
            });
        }
    }
}
