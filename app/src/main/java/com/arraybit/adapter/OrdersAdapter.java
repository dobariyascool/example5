package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.modal.OrderItemTran;
import com.arraybit.modal.OrderMaster;
import com.arraybit.pos.R;
import com.arraybit.pos.WaiterHomeActivity;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("ConstantConditions")
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    public boolean isViewFilter = false;
    public boolean isItemAnimate;
    ArrayList<OrderMaster> alOrderMaster;
    LayoutInflater layoutInflater;
    Context context;
    View view;
    Date currentDate, orderDate = null;
    ArrayList<OrderItemTran> alOrderItemTran;
    LayoutClickListener objLayoutClickListener;
    FragmentManager fragmentManager;
    double totalModifier;
    int previousPosition;

    public OrdersAdapter(Context context, ArrayList<OrderMaster> result, ArrayList<OrderItemTran> alOrderItemTran, FragmentManager fragmentManager, LayoutClickListener objLayoutClickListener, boolean isItemAnimate) {
        this.context = context;
        alOrderMaster = result;
        this.layoutInflater = LayoutInflater.from(context);
        this.alOrderItemTran = alOrderItemTran;
        this.fragmentManager = fragmentManager;
        this.objLayoutClickListener = objLayoutClickListener;
        this.isItemAnimate = isItemAnimate;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_orders, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        OrderMaster objOrderMaster = alOrderMaster.get(position);
        //holder.cvOrder.setId(position);

        try {
            String strCurrentDate = new SimpleDateFormat("d/M/yyyy HH:mm", Locale.US).format(new Date());
            currentDate = new SimpleDateFormat("d/M/yyyy HH:mm", Locale.US).parse(strCurrentDate);
            orderDate = new SimpleDateFormat("d/M/yyyy HH:mm", Locale.US).parse(objOrderMaster.getOrderDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = currentDate.getTime() - orderDate.getTime();
        int hour = (int) difference / (60 * 60 * 1000) % 24;
        int min = (int) (difference / (60 * 1000)) % 60;

        if (hour != 0 && min != 0) {
            holder.txtOrderTimeDifference.setText(String.valueOf(hour + " hours " + min + " minute ago"));
        } else if (hour == 0 && min != 0) {
            holder.txtOrderTimeDifference.setText(String.valueOf(min + " minute ago"));
        } else if (hour != 0 && min == 0) {
            holder.txtOrderTimeDifference.setText(String.valueOf(hour + " hours ago"));
        } else if (hour == 0 && min == 0) {
            holder.txtOrderTimeDifference.setText(String.valueOf("Just Now"));
        }

        holder.txtOrderTime.setText(objOrderMaster.getOrderTime());
        if (objOrderMaster.getTableName().length() > 8 && objOrderMaster.getOrderNumber().length() >= 2) {
            holder.txtTableName.setText(objOrderMaster.getTableName().substring(0, 8) + ".." + "[" + objOrderMaster.getOrderNumber() + "]");
        } else {
            holder.txtTableName.setText(objOrderMaster.getTableName() + " " + "[" + objOrderMaster.getOrderNumber() + "]");
        }


        if (objOrderMaster.getlinktoOrderStatusMasterId() != null) {
            holder.ivOrderIcon.setVisibility(View.VISIBLE);
            if (objOrderMaster.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Cooking.getValue()) {
                holder.ivOrderIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.cooking));
            } else if (objOrderMaster.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Ready.getValue()) {
                holder.ivOrderIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ready));
            } else if (objOrderMaster.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Served.getValue()) {
                holder.ivOrderIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.served));
            } else if (objOrderMaster.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Cancelled.getValue()) {
                holder.ivOrderIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.cancel));
            }

            if (WaiterHomeActivity.isWaiterMode) {
                Drawable drawable = holder.ivOrderIcon.getDrawable();
                DrawableCompat.setTint(drawable.mutate(), ContextCompat.getColor(context, R.color.red_tab_indicator));
            }

        } else {
            holder.ivOrderIcon.setVisibility(View.GONE);
        }

        holder.txtOrderType.setText(objOrderMaster.getOrderType());
        if (isViewFilter) {
            holder.itemLayout.removeAllViewsInLayout();
            holder.itemLayout.removeAllViews();
        }
        if (objOrderMaster.getTotalItem() != 0) {
            SetItemLayout(objOrderMaster, holder, position);
        }
        holder.txtTotalAmount.setText(context.getResources().getString(R.string.dfRupee) + " " + Globals.dfWithPrecision.format(objOrderMaster.getTotalAmount()));

        //holder animation
        if (isItemAnimate) {
            if (position > previousPosition) {
                Globals.SetItemAnimator(holder);
            }
            previousPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return alOrderMaster.size();
    }

    public void SetSearchFilter(ArrayList<OrderMaster> result) {
        alOrderMaster = new ArrayList<>();
        alOrderMaster.addAll(result);
        isViewFilter = true;
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    public void UpdateOrderItemTran(int position, int orderPosition, OrderItemTran objOrderItemTran, boolean isTotalUpdate) {
        alOrderItemTran.get(position).setlinktoOrderStatusMasterId(objOrderItemTran.getlinktoOrderStatusMasterId());
        if (isTotalUpdate) {
            if (objOrderItemTran != null && !objOrderItemTran.getModifierRates().equals("")) {
                String[] strModifier = objOrderItemTran.getModifierRates().split(",");
                for (String strModifierPrice : strModifier) {
                    totalModifier = totalModifier + (Double.valueOf(strModifierPrice) * objOrderItemTran.getQuantity());
                }
            }
            if (objOrderItemTran.getlinktoOrderStatusMasterId() == Globals.OrderStatus.Cancelled.getValue()) {
                double total = alOrderMaster.get(orderPosition).getTotalAmount();
                alOrderMaster.get(orderPosition).setTotalAmount(total - (alOrderItemTran.get(position).getRate() * alOrderItemTran.get(position).getQuantity()) - totalModifier);
            } else {
                double total = alOrderMaster.get(orderPosition).getTotalAmount();
                alOrderMaster.get(orderPosition).setTotalAmount(total + (alOrderItemTran.get(position).getRate() * alOrderItemTran.get(position).getQuantity()) + totalModifier);
            }
        }
        isViewFilter = true;
        isItemAnimate = false;
        notifyDataSetChanged();
        totalModifier = 0;
    }

    public void UpdateOrder(int position, short linktoOrderStatusMasterId) {
        alOrderMaster.get(position).setlinktoOrderStatusMasterId(linktoOrderStatusMasterId);
        notifyDataSetChanged();
        isItemAnimate = false;
        isViewFilter = true;
    }

    public void RemoveOrder(int position) {
        alOrderMaster.remove(position);
        notifyItemRemoved(position);
        isViewFilter = true;
        isItemAnimate = true;
    }

    private void SetItemLayout(final OrderMaster objOrderMaster, OrderViewHolder holder, final int position) {
        TextView[] txtItemQty = new TextView[alOrderItemTran.size()];
        TextView[] txtItemName = new TextView[alOrderItemTran.size()];
        ImageView[] ivStatus = new ImageView[alOrderItemTran.size()];
        LinearLayout[] mainLayout = new LinearLayout[alOrderItemTran.size()];

        for (int i = 0; i < alOrderItemTran.size(); i++) {
            if (alOrderItemTran.get(i).getlinktoOrderMasterId() == objOrderMaster.getOrderMasterId()) {

                mainLayout[i] = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 8, 0, 0);
                mainLayout[i].setOrientation(LinearLayout.HORIZONTAL);
                mainLayout[i].setGravity(Gravity.CENTER);
                mainLayout[i].setLayoutParams(layoutParams);

                txtItemQty[i] = new TextView(context);
                LinearLayout.LayoutParams txtItemQtyParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                txtItemQtyParams.weight = 0.1f;
                txtItemQty[i].setLayoutParams(txtItemQtyParams);
                if (context.getResources().getBoolean(R.bool.isTablet)) {
                    txtItemQty[i].setTextSize(16f);
                } else {
                    txtItemQty[i].setTextSize(12f);
                }
                txtItemQty[i].setTextColor(ContextCompat.getColor(context, R.color.grey));
                txtItemQty[i].setMaxLines(1);
                txtItemQty[i].setText(String.valueOf(alOrderItemTran.get(i).getQuantity()));

                txtItemName[i] = new TextView(context);
                LinearLayout.LayoutParams txtItemNameParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                txtItemNameParams.weight = 0.7f;
                txtItemName[i].setLayoutParams(txtItemNameParams);
                if (context.getResources().getBoolean(R.bool.isTablet)) {
                    txtItemName[i].setTextSize(12f);
                } else {
                    txtItemName[i].setTextSize(12f);
                }
                txtItemName[i].setGravity(Gravity.START);
                txtItemName[i].setMaxLines(1);
                txtItemName[i].setTextColor(ContextCompat.getColor(context, R.color.grey));
                txtItemName[i].setText(alOrderItemTran.get(i).getItem());

                ivStatus[i] = new ImageView(context);
                LinearLayout.LayoutParams ivStatusParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                ivStatusParams.weight = 0.2f;
                ivStatus[i].setLayoutParams(ivStatusParams);
                ivStatus[i].setId(i);
                if (alOrderItemTran.get(i).getlinktoOrderStatusMasterId() == null) {
                    ivStatus[i].setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.default_status));
                } else {
                    if (alOrderItemTran.get(i).getlinktoOrderStatusMasterId() == Globals.OrderStatus.Cooking.getValue()) {
                        ivStatus[i].setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.cooking));
                    } else if (alOrderItemTran.get(i).getlinktoOrderStatusMasterId() == Globals.OrderStatus.Ready.getValue()) {
                        ivStatus[i].setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ready));
                    } else if (alOrderItemTran.get(i).getlinktoOrderStatusMasterId() == Globals.OrderStatus.Served.getValue()) {
                        ivStatus[i].setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.served));
                    } else if (alOrderItemTran.get(i).getlinktoOrderStatusMasterId() == Globals.OrderStatus.Cancelled.getValue()) {
                        txtItemName[i].setPaintFlags(txtItemName[i].getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        ivStatus[i].setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.cancel));
                    }
                }
                ivStatus[i].setColorFilter(ContextCompat.getColor(context, R.color.accent_red), PorterDuff.Mode.SRC_IN);
                ivStatus[i].setBackground(ContextCompat.getDrawable(context, R.drawable.icon_hover_drawable));

                ivStatus[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objLayoutClickListener.ChangeOrderItemStatusClick(alOrderItemTran.get(v.getId()), alOrderMaster.get(position), v.getId(), position, false);
                    }
                });

                mainLayout[i].addView(txtItemQty[i]);
                mainLayout[i].addView(txtItemName[i]);
                mainLayout[i].addView(ivStatus[i]);

                holder.itemLayout.addView(mainLayout[i]);
            }
        }

    }

    //region interface
    public interface LayoutClickListener {
        void ChangeOrderItemStatusClick(OrderItemTran objOrderItemTran, OrderMaster objOrderMaster, int itemPosition, int orderPosition, boolean isOrder);
    }
    //endregion

    class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderTimeDifference, txtOrderTime, txtTableName, txtOrderNumber, txtOrderType, txtTotalAmount;
        LinearLayout itemLayout;
        CardView cvOrder;
        ImageView ivOrderIcon;

        public OrderViewHolder(View itemView) {
            super(itemView);

            txtOrderTimeDifference = (TextView) itemView.findViewById(R.id.txtOrderTimeDifference);
            txtOrderTime = (TextView) itemView.findViewById(R.id.txtOrderTime);
            txtTableName = (TextView) itemView.findViewById(R.id.txtTableName);
            txtOrderNumber = (TextView) itemView.findViewById(R.id.txtOrderNumber);
            txtOrderType = (TextView) itemView.findViewById(R.id.txtOrderType);
            txtTotalAmount = (TextView) itemView.findViewById(R.id.txtTotalAmount);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);

            cvOrder = (CardView) itemView.findViewById(R.id.cvOrder);

            ivOrderIcon = (ImageView) itemView.findViewById(R.id.ivOrderIcon);

            cvOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Globals.HideKeyBoard(context, v);
                    objLayoutClickListener.ChangeOrderItemStatusClick(null, alOrderMaster.get(getAdapterPosition()), 0, getAdapterPosition(), true);
                }
            });
        }
    }
}
