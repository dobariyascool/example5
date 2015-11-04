package com.arraybit.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.arraybit.pos.DetailFragment;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ItemViewHolder> {

    FragmentManager fragmentManager;
    boolean isViewChange;
    View view;
    Context context;
    ArrayList<ItemMaster> alItemMaster;

    public CategoryItemAdapter(Context context, ArrayList<ItemMaster> result, FragmentManager fragmentManager, boolean isViewChange) {
        this.context = context;
        alItemMaster = result;
        this.fragmentManager = fragmentManager;
        this.isViewChange = isViewChange;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (isViewChange) {
            view = LayoutInflater.from(context).inflate(R.layout.row_recylerview_grid, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.row_recyclerview, parent, false);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //orderItemDetail=(OrderItemDetail)context;
                //orderItemDetail.ItemDetail();
                DetailFragment detailFragment = new DetailFragment();
                Globals.InitializeFragment(detailFragment, fragmentManager);

            }
        });
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ItemMaster objItemMaster = alItemMaster.get(position);

        holder.mItemTextView.setText(objItemMaster.getItemName());
        holder.mItemTextView1.setText(objItemMaster.getShortDescription());
        holder.mItemTextView2.setText("Rs. " + Globals.dfWithPrecision.format(objItemMaster.getSellPrice()));
    }


    @Override
    public int getItemCount() {
        return alItemMaster.size();
    }

    public void ItemListDataChanged(ArrayList<ItemMaster> result) {
        alItemMaster.addAll(result);
        notifyDataSetChanged();
    }

    public interface OrderItemDetail {
        public void ItemDetail();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mItemTextView, mItemTextView1, mItemTextView2;
        CardView mItemCardView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mItemCardView = (CardView) itemView.findViewById(R.id.itemCardView);

            mItemTextView = (TextView) itemView.findViewById(R.id.itemTextView);
            mItemTextView1 = (TextView) itemView.findViewById(R.id.itemTextView1);
            mItemTextView2 = (TextView) itemView.findViewById(R.id.itemTextView2);
        }
    }

}
