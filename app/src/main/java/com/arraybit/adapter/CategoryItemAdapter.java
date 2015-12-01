package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.modal.ItemMaster;
import com.arraybit.pos.CategoryItemFragment;
import com.arraybit.pos.DetailFragment;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ItemViewHolder> {

    FragmentManager fragmentManager;
    boolean isViewChange;
    boolean isWaiterGrid = false;
    View view;
    Context context;
    ArrayList<ItemMaster> alItemMaster;
    ItemMaster objItemMaster;

    public CategoryItemAdapter(Context context, ArrayList<ItemMaster> result, FragmentManager fragmentManager, boolean isViewChange) {
        this.context = context;
        alItemMaster = result;
        this.fragmentManager = fragmentManager;
        this.isViewChange = isViewChange;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (isViewChange) {
            if (CategoryItemFragment.i == 1) {
                view = LayoutInflater.from(context).inflate(R.layout.row_category_item_grid, parent, false);
            } else {
                isWaiterGrid = true;
                view = LayoutInflater.from(context).inflate(R.layout.row_waiter_category_item_grid, parent, false);
            }
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.row_category_item, parent, false);
        }


        return new ItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        objItemMaster = alItemMaster.get(position);

        holder.cvItem.setId(objItemMaster.getItemMasterId());

        if (!isWaiterGrid) {
            if(objItemMaster.getImageName().equals("null")){
                Picasso.with(holder.ivItem.getContext()).load(R.drawable.vada_paav).into(holder.ivItem);
            }
            else {
                Picasso.with(holder.ivItem.getContext()).load(objItemMaster.getImageName()).into(holder.ivItem);
            }
        }

        holder.txtItemName.setText(objItemMaster.getItemName());
        holder.txtItemDescription.setText(objItemMaster.getShortDescription());
        holder.txtItemPrice.setVisibility(View.GONE);
        //holder.txtItemPrice.setText("Rs. " + Globals.dfWithPrecision.format(objItemMaster.getSellPrice()));
    }

    @Override
    public int getItemCount() {
        return alItemMaster.size();
    }

    public void ItemListDataChanged(ArrayList<ItemMaster> result) {
        alItemMaster.addAll(result);
        notifyDataSetChanged();
    }

    public void SetSearchFilter(ArrayList<ItemMaster> result){
        alItemMaster = new ArrayList<>();
        alItemMaster.addAll(result);
        notifyDataSetChanged();
    }

    public interface OrderItemDetail {
        public void ItemDetail();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtItemDescription, txtItemPrice;
        ImageView ivItem;
        CardView cvItem;

        public ItemViewHolder(View itemView) {
            super(itemView);

            cvItem = (CardView) itemView.findViewById(R.id.cvItem);

            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);

            txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
            txtItemDescription = (TextView) itemView.findViewById(R.id.txtItemDescription);
            txtItemPrice = (TextView) itemView.findViewById(R.id.txtItemPrice);

            cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //orderItemDetail=(OrderItemDetail)context;
                    //orderItemDetail.ItemDetail();
                    //DetailFragment detailFragment = new DetailFragment(v.getId());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_category_item,new DetailFragment(v.getId()),context.getResources().getString(R.string.title_fragment_detail));
                    fragmentTransaction.addToBackStack(context.getResources().getString(R.string.title_fragment_detail));
                    fragmentTransaction.commit();
                }

            });

        }
    }

}
