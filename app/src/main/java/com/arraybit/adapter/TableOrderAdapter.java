package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TableMaster;
import com.arraybit.modal.TaxMaster;
import com.arraybit.pos.OrderSummaryFragment;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("ConstantConditions")
public class TableOrderAdapter extends RecyclerView.Adapter<TableOrderAdapter.TableOrderViewHolder> {

    public boolean isItemAnimate;
    ArrayList<OrderMaster> alOrderMaster;
    LayoutInflater layoutInflater;
    Context context;
    View view;
    Date currentDate, orderDate = null;
    ArrayList<TaxMaster> alTaxMaster;
    FragmentManager fragmentManager;
    double totalTaxPercentage, totalTaxAmount, totalNetAmount;
    int previousPosition;

    public TableOrderAdapter(Context context, ArrayList<OrderMaster> result, ArrayList<TaxMaster> alTaxMaster, FragmentManager fragmentManager,boolean isItemAnimate) {
        this.context = context;
        alOrderMaster = result;
        this.layoutInflater = LayoutInflater.from(context);
        this.alTaxMaster = alTaxMaster;
        this.fragmentManager = fragmentManager;
        this.isItemAnimate = isItemAnimate;
    }

    @Override
    public TableOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_table_order, parent, false);
        return new TableOrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(TableOrderViewHolder holder, int position) {
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
        holder.txtTableName.setText(objOrderMaster.getTableName());
        holder.txtTotalOrder.setText(objOrderMaster.getTotalKOT() + " Orders ");
        holder.txtTotalItem.setText(objOrderMaster.getTotalItem() + " Items");
        if (alTaxMaster == null) {
            holder.txtTotalAmount.setText("Rs. " + Globals.dfWithPrecision.format(objOrderMaster.getTotalAmount()));
        } else {
            CalculateTax(objOrderMaster, holder);
        }

        //holder animation
        if(isItemAnimate) {
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

    private void CalculateTax(OrderMaster objOrderMaster, TableOrderViewHolder holder) {
        try {
            for (int i = 0; i < alTaxMaster.size(); i++) {
                if (alTaxMaster.get(i).getIsPercentage()) {
                    totalTaxPercentage = totalTaxPercentage + alTaxMaster.get(i).getTaxRate();
                } else {
                    totalTaxAmount = totalTaxAmount + alTaxMaster.get(i).getTaxRate();
                }
            }
            totalNetAmount = Math.round(objOrderMaster.getTotalAmount() + (objOrderMaster.getTotalAmount() * totalTaxPercentage / 100) + totalTaxAmount);
            holder.txtTotalAmount.setText(Globals.dfWithPrecision.format(totalNetAmount));
        } catch (Exception ex) {
            holder.txtTotalAmount.setText(Globals.dfWithPrecision.format(objOrderMaster.getTotalAmount()));
        }
    }

    @SuppressLint("RtlHardcoded")
    private void ReplaceFragment(Fragment fragment,String fragmentName){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(350);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(R.id.tableOrderFragment, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    class TableOrderViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderTimeDifference, txtOrderTime, txtTableName, txtTotalOrder, txtTotalItem, txtTotalAmount;
        CardView cvOrder;

        public TableOrderViewHolder(View itemView) {
            super(itemView);

            txtOrderTimeDifference = (TextView) itemView.findViewById(R.id.txtOrderTimeDifference);
            txtOrderTime = (TextView) itemView.findViewById(R.id.txtOrderTime);
            txtTableName = (TextView) itemView.findViewById(R.id.txtTableName);
            txtTotalOrder = (TextView) itemView.findViewById(R.id.txtTotalOrder);
            txtTotalItem = (TextView) itemView.findViewById(R.id.txtTotalItem);
            txtTotalAmount = (TextView) itemView.findViewById(R.id.txtTotalAmount);

            cvOrder = (CardView) itemView.findViewById(R.id.cvOrder);

            cvOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Globals.HideKeyBoard(context, v);

                    TableMaster objTableMaster = new TableMaster();
                    if (alOrderMaster.get(getAdapterPosition()).getlinktoTableMasterIds().length() > 0) {
                        String[] strTableMasterId = alOrderMaster.get(getAdapterPosition()).getlinktoTableMasterIds().split(",");
                        objTableMaster.setTableMasterId(Short.valueOf(strTableMasterId[0]));
                    }
                    objTableMaster.setTableName(alOrderMaster.get(getAdapterPosition()).getTableName());
                    objTableMaster.setlinktoOrderTypeMasterId(alOrderMaster.get(getAdapterPosition()).getlinktoOrderTypeMasterId());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("TableMaster", objTableMaster);
                    OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment();
                    orderSummaryFragment.setArguments(bundle);

                    ReplaceFragment(orderSummaryFragment,context.getResources().getString(R.string.title_fragment_order_summary));
                    //Globals.ReplaceFragment(orderSummaryFragment, fragmentManager, context.getResources().getString(R.string.title_fragment_order_summary));
                }
            });
        }
    }
}
