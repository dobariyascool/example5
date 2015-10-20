package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.security.Policy;
import java.util.ArrayList;

public class OptionListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> alString;
    LayoutInflater l_Inflater;
    ViewHolder holder;
    OptionListClickListener objOptionListClickListener;

    public OptionListAdapter(Context context, ArrayList<String> results) {
        this.context = context;
        alString = results;
        l_Inflater = LayoutInflater.from(context);
    }

    public interface OptionListClickListener
    {
       void onClick(int position);
    }

    @Override
    public int getCount() {
        return alString.size();
    }

    @Override
    public Object getItem(int position) {
        return alString.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.row_options_list, null);
            holder=new ViewHolder();

            holder.txtView=(TextView)convertView.findViewById(R.id.textView);
            holder.listLayout=(LinearLayout)convertView.findViewById(R.id.listLayout);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    objOptionListClickListener=(OptionListClickListener)context;
                    int id = v.getId();
                    objOptionListClickListener.onClick(id);
                    System.out.println("id"+id);
                }
            });
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setTag(holder);
        convertView.setId(position);
//        if(position==0){
//            holder.listLayout.setBackgroundColor(context.getResources().getColor(R.color.primary));
//            holder.txtView.setTypeface(Typeface.DEFAULT_BOLD);
//            holder.txtView.setAllCaps(true);
//        }
        holder.txtView.setText(alString.get(position));

        return convertView;
    }

    public static class ViewHolder
    {
        TextView txtView;
        LinearLayout listLayout;

    }
}
