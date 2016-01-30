package com.arraybit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class ModifierAdapter extends ArrayAdapter<String> {

    Context context;
    LayoutInflater L_Inflater;

    ArrayList<String> alString;
    ArrayList<String> alRate;
    Boolean isSmallFont;

    public ModifierAdapter(Context context, ArrayList<String> result,ArrayList<String> rate,Boolean isSmallFont) {
        super(context,0,result);
        this.context = context;
        this.alString = result;
        this.alRate = rate;
        this.L_Inflater = LayoutInflater.from(context);
        this.isSmallFont = isSmallFont;
    }

    @Override
    public int getCount() {
        return alString.size();
    }

    @Override
    public String getItem(int position) {
        return alString.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (convertView == null) {
            view = L_Inflater.inflate(R.layout.row_modifier, parent, false);
        }

        view.setTag(alString.get(position));
        TextView txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        TextView txtRate = (TextView) view.findViewById(R.id.txtRate);

        if (isSmallFont) {
            txtItemName.setTextSize(12f);
            txtRate.setTextSize(12f);
        }

        txtItemName.setText(alString.get(position));
        txtRate.setText(String.valueOf(alRate.get(position)));
        return view;
    }
}
