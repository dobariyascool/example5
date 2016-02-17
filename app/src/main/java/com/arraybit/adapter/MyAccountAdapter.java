package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.MyAccountViewHolder> {

    Context context;
    ArrayList<String> alString;
    LayoutInflater layoutInflater;
    View view;
    OptionClickListener objOptionClickListener;

    // Constructor
    public MyAccountAdapter(Context context, ArrayList<String> result, OptionClickListener objOptionClickListener) {
        this.context = context;
        this.alString = result;
        this.layoutInflater = LayoutInflater.from(context);
        this.objOptionClickListener = objOptionClickListener;
    }

    @Override
    public MyAccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_myaccount, parent, false);
        return new MyAccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAccountViewHolder holder, int position) {
        holder.txtTitle.setText(alString.get(position));
        holder.cvOptions.setId(position);
    }

    @Override
    public int getItemCount() {
        return alString.size();
    }

    public interface OptionClickListener {
        void OptionClick(int id);
    }

    //region ViewHolder
    class MyAccountViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        CardView cvOptions;

        public MyAccountViewHolder(View itemView) {
            super(itemView);

            cvOptions = (CardView) itemView.findViewById(R.id.cvOptions);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            cvOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objOptionClickListener.OptionClick(v.getId());

                }
            });
        }
    }
    //endregion
}
