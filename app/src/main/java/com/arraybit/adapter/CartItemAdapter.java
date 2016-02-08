package com.arraybit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    Context context;
    ArrayList<ItemMaster> alItemMaster;
    LayoutInflater layoutInflater;
    View view;
    CartItemOnClickListener objCartItemOnClickListener;
    boolean isModifierChanged = true;

    // Constructor
    public CartItemAdapter(Context context, ArrayList<ItemMaster> result, CartItemOnClickListener objCartItemOnClickListener) {
        this.context = context;
        this.alItemMaster = result;
        this.objCartItemOnClickListener = objCartItemOnClickListener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_cart_item, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartItemViewHolder holder, int position) {
        ItemMaster objItemMaster = alItemMaster.get(position);

        holder.ivClose.setId(position);
        holder.txtItem.setText(objItemMaster.getItemName());
        holder.txtQty.setText(String.valueOf(objItemMaster.getQuantity()));
        holder.txtAmount.setText(String.valueOf(Globals.dfWithPrecision.format(objItemMaster.getSellPrice())));
        holder.txtRate.setText(String.valueOf(Globals.dfWithPrecision.format(objItemMaster.getSellPrice() / objItemMaster.getQuantity())));
        if (!isModifierChanged) {
            RemoveModifierView(holder);
        }
        if (objItemMaster.getAlOrderItemModifierTran().size() != 0) {
            LinearLayout.LayoutParams txtQtyLayoutParams= (LinearLayout.LayoutParams) holder.txtQty.getLayoutParams();
            txtQtyLayoutParams.gravity = Gravity.TOP;
            holder.txtQty.setLayoutParams(txtQtyLayoutParams);
            SetModifierLayout(objItemMaster.getAlOrderItemModifierTran(), holder);
        }
        if (objItemMaster.getRemark().equals("")) {
            holder.remarkLayout.setVisibility(View.GONE);
        } else {
            holder.remarkLayout.setVisibility(View.VISIBLE);
            if (objItemMaster.getRemark().substring(objItemMaster.getRemark().length() - 1, objItemMaster.getRemark().length()).equals(",")) {
                holder.txtRemark.setText(objItemMaster.getRemark().substring(0, objItemMaster.getRemark().length() - 1));
            } else if (objItemMaster.getRemark().substring(objItemMaster.getRemark().length() - 1, objItemMaster.getRemark().length()).equals(" ")) {
                holder.txtRemark.setText(objItemMaster.getRemark().substring(0, objItemMaster.getRemark().length() - 2));
            } else {
                holder.txtRemark.setText(objItemMaster.getRemark());
            }
        }
    }

    @Override
    public int getItemCount() {
        return alItemMaster.size();
    }

    public void RemoveData(int position) {
        if (alItemMaster.size() != 0 && position >= 0) {
            isModifierChanged = false;
            alItemMaster.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
            Globals.counter = Globals.counter - 1;
        }

    }

    private void SetModifierLayout(ArrayList<ItemMaster> alOrderItemModifierTran, CartItemViewHolder holder) {
        TextView[] txtModifierName = new TextView[alOrderItemModifierTran.size()];
        TextView[] txtModifierRate = new TextView[alOrderItemModifierTran.size()];
        TextView[] txtModifierAmount = new TextView[alOrderItemModifierTran.size()];

        for (int i = 0; i < alOrderItemModifierTran.size(); i++) {

            txtModifierName[i] = new TextView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtModifierName[i].setLayoutParams(layoutParams);
            txtModifierName[i].setTextSize(12f);
            txtModifierName[i].setTextColor(ContextCompat.getColor(context, R.color.grey));
            txtModifierName[i].setMaxLines(1);
            txtModifierName[i].setText(alOrderItemModifierTran.get(i).getItemName());

            txtModifierRate[i] = new TextView(context);
            LinearLayout.LayoutParams txtModifierRateParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtModifierRate[i].setLayoutParams(txtModifierRateParams);
            txtModifierRate[i].setTextSize(12f);
            txtModifierRate[i].setGravity(Gravity.END);
            txtModifierRate[i].setMaxLines(1);
            txtModifierRate[i].setTextColor(ContextCompat.getColor(context, R.color.grey));
            txtModifierRate[i].setText(Globals.dfWithPrecision.format(alOrderItemModifierTran.get(i).getActualSellPrice()));

            txtModifierAmount[i] = new TextView(context);
            LinearLayout.LayoutParams txtModifierAmountParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtModifierAmount[i].setLayoutParams(txtModifierAmountParams);
            txtModifierAmount[i].setTextSize(12f);
            txtModifierAmount[i].setGravity(Gravity.END);
            txtModifierAmount[i].setMaxLines(1);
            txtModifierAmount[i].setTextColor(ContextCompat.getColor(context, R.color.grey));
            txtModifierAmount[i].setText(Globals.dfWithPrecision.format(alOrderItemModifierTran.get(i).getMRP()));

            holder.modifierLayout.addView(txtModifierName[i]);
            holder.modifierRateLayout.addView(txtModifierRate[i]);
            holder.modifierAmountLayout.addView(txtModifierAmount[i]);

        }

    }

    private void RemoveModifierView(CartItemViewHolder holder){
        holder.modifierLayout.removeAllViewsInLayout();
        holder.modifierRateLayout.removeAllViewsInLayout();
        holder.modifierAmountLayout.removeAllViewsInLayout();
        holder.modifierLayout.removeAllViews();
        holder.modifierRateLayout.removeAllViews();
        holder.modifierAmountLayout.removeAllViews();
    }

    public interface CartItemOnClickListener {
        void ImageViewOnClick(int position);
    }

    //region ViewHolder
    class CartItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtIndex, txtItem, txtQty, txtRate, txtAmount, txtRemark;
        LinearLayout childLayout, modifierLayout, modifierRateLayout, modifierAmountLayout, remarkLayout;
        ImageView ivClose;

        public CartItemViewHolder(View itemView) {
            super(itemView);

            childLayout = (LinearLayout) itemView.findViewById(R.id.childLayout);
            modifierLayout = (LinearLayout) itemView.findViewById(R.id.modifierLayout);
            modifierRateLayout = (LinearLayout) itemView.findViewById(R.id.modifierRateLayout);
            modifierAmountLayout = (LinearLayout) itemView.findViewById(R.id.modifierAmountLayout);
            remarkLayout = (LinearLayout) itemView.findViewById(R.id.remarkLayout);

            txtItem = (TextView) itemView.findViewById(R.id.txtItem);
            txtAmount = (TextView) itemView.findViewById(R.id.txtAmount);
            txtRate = (TextView) itemView.findViewById(R.id.txtRate);
            txtQty = (TextView) itemView.findViewById(R.id.txtQty);
            txtRemark = (TextView) itemView.findViewById(R.id.txtRemark);
            ivClose = (ImageView) itemView.findViewById(R.id.ivClose);

            childLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objCartItemOnClickListener.ImageViewOnClick(v.getId());
                }
            });
        }
    }
    //endregion
}



