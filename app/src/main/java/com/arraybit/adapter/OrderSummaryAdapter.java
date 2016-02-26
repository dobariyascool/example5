package com.arraybit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.OrderSummeryViewHolder> {

    Context context;
    ArrayList<ItemMaster> alOrderItemTran;
    LayoutInflater layoutInflater;
    View view;
    ArrayList<OrderMaster> alOrderMaster;

    public OrderSummaryAdapter(Context context, ArrayList<ItemMaster> result, ArrayList<OrderMaster> alOrderMasterId) {
        this.context = context;
        alOrderItemTran = result;
        this.layoutInflater = LayoutInflater.from(context);
        this.alOrderMaster = alOrderMasterId;
    }

    @Override
    public OrderSummeryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_order_summery, parent, false);
        return new OrderSummeryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderSummeryViewHolder holder, int position) {
        holder.txtOrderNumber.setText(String.valueOf(alOrderMaster.get(position).getOrderNumber()));
        SetOrderItem(alOrderMaster.get(position).getOrderMasterId(), holder);
    }

    @Override
    public int getItemCount() {
        return alOrderMaster.size();
    }

    private void SetOrderItem(Long orderMasterId, OrderSummeryViewHolder holder) {

        LinearLayout[] layout = new LinearLayout[alOrderItemTran.size()];
        TextView[] txtName = new TextView[alOrderItemTran.size()];
        TextView[] txtRate = new TextView[alOrderItemTran.size()];
        TextView[] txtAmount = new TextView[alOrderItemTran.size()];
        TextView[] txtQty = new TextView[alOrderItemTran.size()];



        for (int i = 0; i < alOrderItemTran.size(); i++) {

            if (orderMasterId == alOrderItemTran.get(i).getLinktoOrderMasterId()) {

                layout[i] = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout[i].setLayoutParams(layoutParams1);
                layout[i].setOrientation(LinearLayout.HORIZONTAL);

                txtName[i] = new TextView(context);
                LinearLayout.LayoutParams txtNameLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                txtNameLayoutParams.weight = 0.5f;
                txtName[i].setLayoutParams(txtNameLayoutParams);
                txtName[i].setGravity(Gravity.START);
                Globals.TextViewFontTypeFace(txtName[i], context);

                txtQty[i] = new TextView(context);
                LinearLayout.LayoutParams txtQtyLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                txtQtyLayoutParams.gravity = Gravity.TOP;
                txtQtyLayoutParams.weight = 0.15f;
                txtQty[i].setLayoutParams(txtQtyLayoutParams);
                txtQty[i].setGravity(Gravity.CENTER);
                Globals.TextViewFontTypeFace(txtQty[i], context);


                txtRate[i] = new TextView(context);
                LinearLayout.LayoutParams txtRateLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                txtRateLayoutParams.weight = 0.22f;
                txtRate[i].setLayoutParams(txtRateLayoutParams);
                txtRate[i].setGravity(Gravity.END);
                Globals.TextViewFontTypeFace(txtRate[i], context);

                txtAmount[i] = new TextView(context);
                LinearLayout.LayoutParams txtAmountLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                txtAmountLayoutParams.weight = 0.3f;
                txtAmount[i].setLayoutParams(txtAmountLayoutParams);
                txtAmount[i].setGravity(Gravity.END);
                Globals.TextViewFontTypeFace(txtAmount[i], context);

                if (alOrderItemTran.get(i).getItemModifierIds().equals("0")) {
                    txtQty[i].setTextSize(14f);
                    txtName[i].setTextSize(14f);
                    txtRate[i].setTextSize(14f);
                    txtAmount[i].setTextSize(14f);

                    txtQty[i].setVisibility(View.VISIBLE);
                    txtQty[i].setText(String.valueOf(alOrderItemTran.get(i).getQuantity()));
                    txtName[i].setText(alOrderItemTran.get(i).getItemName());
                    txtRate[i].setText(Globals.dfWithPrecision.format(alOrderItemTran.get(i).getActualSellPrice()));
                    txtAmount[i].setText(Globals.dfWithPrecision.format(alOrderItemTran.get(i).getSellPrice()));

                    txtQty[i].setTextColor(ContextCompat.getColor(context, R.color.white_blur));
                    txtName[i].setTextColor(ContextCompat.getColor(context, R.color.white_blur));
                    txtRate[i].setTextColor(ContextCompat.getColor(context, R.color.white_blur));
                    txtAmount[i].setTextColor(ContextCompat.getColor(context, R.color.white_blur));

                } else {

                    txtQty[i].setTextSize(12f);
                    txtName[i].setTextSize(12f);
                    txtRate[i].setTextSize(12f);
                    txtAmount[i].setTextSize(12f);

                    txtQty[i].setVisibility(View.INVISIBLE);
                    txtName[i].setText(alOrderItemTran.get(i).getItemName());
                    txtRate[i].setText(Globals.dfWithPrecision.format(alOrderItemTran.get(i).getActualSellPrice()));
                    txtAmount[i].setText(Globals.dfWithPrecision.format((alOrderItemTran.get(i).getSellPrice())));

                    txtQty[i].setTextColor(ContextCompat.getColor(context, R.color.grey));
                    txtName[i].setTextColor(ContextCompat.getColor(context, R.color.grey));
                    txtRate[i].setTextColor(ContextCompat.getColor(context, R.color.grey));
                    txtAmount[i].setTextColor(ContextCompat.getColor(context, R.color.grey));
                }

                layout[i].addView(txtName[i]);
                layout[i].addView(txtQty[i]);
                layout[i].addView(txtRate[i]);
                layout[i].addView(txtAmount[i]);

                holder.orderItemLayout.addView(layout[i]);

            }
        }
    }

    class OrderSummeryViewHolder extends RecyclerView.ViewHolder {

        CardView cvOrderItem;
        TextView txtOrderNumber;
        LinearLayout orderItemLayout;

        public OrderSummeryViewHolder(View itemView) {
            super(itemView);

            cvOrderItem = (CardView) itemView.findViewById(R.id.cvOrderItem);

            txtOrderNumber = (TextView) itemView.findViewById(R.id.txtOrderNumber);
            Globals.TextViewFontTypeFace(txtOrderNumber,context);

            orderItemLayout = (LinearLayout) itemView.findViewById(R.id.orderItemLayout);
        }
    }
}
