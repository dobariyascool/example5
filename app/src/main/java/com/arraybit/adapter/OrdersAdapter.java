package com.arraybit.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.arraybit.modal.OrderMaster;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    ArrayList<OrderMaster> alOrderMaster;
    LayoutInflater layoutInflater;
    Context context;
    View view;
    FragmentManager fragmentManager;
    Date currentDate,orderDate=null;
    Calendar calendar;
    SearchView searchView;
    OrdersClickListener objOrdersClickListener;


    public OrdersAdapter(Context context, ArrayList<OrderMaster> result) {
        this.context = context;
        alOrderMaster = result;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_orders, parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        OrderMaster objOrderMaster = alOrderMaster.get(position);
        holder.cvOrder.setId(position);

        try {
            String strCurrentDate = new SimpleDateFormat("d/M/yyyy HH:mm",Locale.US).format(new Date());
            currentDate = new SimpleDateFormat("d/M/yyyy HH:mm",Locale.US).parse(strCurrentDate);
            orderDate = new SimpleDateFormat("d/M/yyyy HH:mm",Locale.US).parse(objOrderMaster.getOrderDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = currentDate.getTime() - orderDate.getTime();
        int hour = (int) difference / (60 * 60 * 1000) % 24;
        int min = (int)(difference / (60 * 1000)) % 60;

        if(hour!=0 && min!=0){
            holder.txtOrderTimeDifference.setText(String.valueOf(hour + " hours " + min +" minute ago"));
        }
        else if(hour==0 && min!=0){
            holder.txtOrderTimeDifference.setText(String.valueOf(min +" minute ago"));
        }
        else if(hour!=0 && min==0){
            holder.txtOrderTimeDifference.setText(String.valueOf(hour +" hours ago"));
        }
        else if(hour==0 && min==0){
            holder.txtOrderTimeDifference.setText(String.valueOf("Just Now"));
        }

        holder.txtOrderTime.setText(objOrderMaster.getOrderTime());
        holder.txtTableName.setText(objOrderMaster.getTableName());
        holder.txtOrderNumber.setText(objOrderMaster.getOrderNumber());
        holder.txtOrderType.setText(objOrderMaster.getOrderType());
        holder.txtTotalItem.setText(objOrderMaster.getTotalItem()+" Items");
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

    public void SetSearchFilter(ArrayList<OrderMaster> result) {
        alOrderMaster = new ArrayList<>();
        alOrderMaster.addAll(result);
        notifyDataSetChanged();
    }

    //region interface
    public interface OrdersClickListener {
        void OrderDetail(OrderMaster objOrderMaster);
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderTimeDifference,txtOrderTime, txtTableName, txtOrderNumber, txtOrderType,txtTotalItem, txtTotalAmount;
        CardView cvOrder;

        public OrderViewHolder(View itemView) {
            super(itemView);

            txtOrderTimeDifference = (TextView) itemView.findViewById(R.id.txtOrderTimeDifference);
            txtOrderTime = (TextView) itemView.findViewById(R.id.txtOrderTime);
            txtTableName = (TextView) itemView.findViewById(R.id.txtTableName);
            txtOrderNumber = (TextView) itemView.findViewById(R.id.txtOrderNumber);
            txtOrderType = (TextView) itemView.findViewById(R.id.txtOrderType);
            txtTotalItem = (TextView) itemView.findViewById(R.id.txtTotalItem);
            txtTotalAmount = (TextView) itemView.findViewById(R.id.txtTotalAmount);

            cvOrder = (CardView) itemView.findViewById(R.id.cvOrder);

            cvOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Globals.HideKeyBoard(context,v);
                    objOrdersClickListener = (OrdersClickListener)Globals.targetFragment;
                    objOrdersClickListener.OrderDetail(alOrderMaster.get(v.getId()));
                }
            });
        }
    }
}
