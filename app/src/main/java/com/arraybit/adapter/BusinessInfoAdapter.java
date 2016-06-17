package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.modal.BusinessInfoQuestionMaster;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class BusinessInfoAdapter extends RecyclerView.Adapter<BusinessInfoAdapter.BusinessInfoViewHolder> {

    View view;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<BusinessInfoQuestionMaster> alBusinessInfoQuestionMaster;
    String str;
    //fghgfhgfh

    public BusinessInfoAdapter(Context context, ArrayList<BusinessInfoQuestionMaster> alBusinessInfoQuestionMaster) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.alBusinessInfoQuestionMaster = alBusinessInfoQuestionMaster;
    }

    @Override
    public BusinessInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_business_info, parent, false);
        return new BusinessInfoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(BusinessInfoViewHolder holder, int position) {
        BusinessInfoQuestionMaster objBusinessInfoQuestionMaster = alBusinessInfoQuestionMaster.get(position);

        holder.txtQuestion.setText(objBusinessInfoQuestionMaster.getQuestion());
        holder.txtAnswer.setText(objBusinessInfoQuestionMaster.getAnswer());
    }

    @Override
    public int getItemCount() {
        return alBusinessInfoQuestionMaster.size();
    }

    class BusinessInfoViewHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion, txtAnswer;
        public BusinessInfoViewHolder(View itemView) {
            super(itemView);

            txtAnswer = (TextView) itemView.findViewById(R.id.txtAnswer);
            txtQuestion = (TextView) itemView.findViewById(R.id.txtQuestion);
        }
    }
}

