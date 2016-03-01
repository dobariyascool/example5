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

public class OptionListAdapter extends RecyclerView.Adapter<OptionListAdapter.OptionViewHolder> {

    Context context;
    ArrayList<String> alString;
    LayoutInflater layoutInflater;
    View view;

    OptionListClickListener objOptionListClickListener;

    public OptionListAdapter(Context context, ArrayList<String> results) {
        this.context = context;
        alString = results;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_options_list, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OptionViewHolder holder, int position) {
        holder.txtTitle.setText(alString.get(position));
        //holder.cvOptions.setId(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return alString.size();
    }

    public interface OptionListClickListener {
        void onClick(int position);
    }

    class OptionViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        CardView cvOptions;

        public OptionViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            cvOptions = (CardView) itemView.findViewById(R.id.cvOptions);

            cvOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objOptionListClickListener = (OptionListClickListener) context;
                    objOptionListClickListener.onClick(getAdapterPosition());
                }
            });

        }
    }
}
