package com.arraybit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class DiscountAdapter extends ArrayAdapter<String> {


    Context context;
    LayoutInflater L_Inflater;

    ArrayList<String> alString;
    ArrayList<String> alDiscount;
    ArrayList<Boolean> alDiscountType;

    public DiscountAdapter(Context context, ArrayList<String> result, ArrayList<String> alDiscount, ArrayList<Boolean> alDiscountType) {
        super(context, 0, result);
        this.context = context;
        this.alString = result;
        this.alDiscount = alDiscount;
        this.alDiscountType = alDiscountType;
        this.L_Inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return alDiscount.size();
    }

    @Override
    public String getItem(int position) {
        return alDiscount.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (convertView == null) {
            view = L_Inflater.inflate(R.layout.row_discount, parent, false);
        }
        view.setTag(alString.get(position));
        TextView txtDiscountTitle = (TextView) view.findViewById(R.id.txtDiscountTitle);
        TextView txtDiscount = (TextView) view.findViewById(R.id.txtDiscount);
        TextView txtDiscountType = (TextView) view.findViewById(R.id.txtDiscountType);
        txtDiscountTitle.setText(alString.get(position));

        if (alDiscountType.get(position).equals(true)) {
            String str = alDiscount.get(position).substring(alDiscount.get(position).lastIndexOf(".")+1,alDiscount.get(position).length());
            if(str.equals("0")){
                txtDiscount.setText(alDiscount.get(position).substring(0,alDiscount.get(position).lastIndexOf(".")));
            }
            else{
                txtDiscount.setText(alDiscount.get(position));
            }
            txtDiscountType.setText(context.getResources().getString(R.string.ddfPercentage));
        } else {
            txtDiscount.setText(alDiscount.get(position));
            txtDiscountType.setText(context.getResources().getString(R.string.ddfRupee));
        }
        return view;
    }
}

