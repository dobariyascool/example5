package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.arraybit.pos.CategoryItemFragment;
import com.arraybit.pos.R;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ItemViewHolder> {

    public boolean isItemAnimate;
    int width, height;
    FragmentManager fragmentManager;
    boolean isViewChange;
    boolean isWaiterGrid = false;
    View view;
    Context context;
    ArrayList<ItemMaster> alItemMaster;
    ItemMaster objItemMaster;
    ItemClickListener objItemClickListener;
    int previousPosition;

    public CategoryItemAdapter(Context context, ArrayList<ItemMaster> result, FragmentManager fragmentManager, boolean isViewChange, ItemClickListener objItemClickListener, Boolean isItemAnimate) {
        this.context = context;
        alItemMaster = result;
        this.fragmentManager = fragmentManager;
        this.isViewChange = isViewChange;
        this.objItemClickListener = objItemClickListener;
        this.isItemAnimate = isItemAnimate;
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
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        objItemMaster = alItemMaster.get(position);
        if (!isWaiterGrid) {
            if (isViewChange) {
                if (objItemMaster.getMD_ImagePhysicalName() == null || objItemMaster.getMD_ImagePhysicalName().equals("")) {
                    Picasso.with(holder.ivItem.getContext()).load(R.drawable.default_image).into(holder.ivItem);
                } else {
                    Picasso.with(holder.ivItem.getContext()).load(objItemMaster.getMD_ImagePhysicalName()).into(holder.ivItem);
                }
            } else {
                if (objItemMaster.getSM_ImagePhysicalName() == null || objItemMaster.getSM_ImagePhysicalName().equals("")) {
                    Picasso.with(holder.ivItem.getContext()).load(R.drawable.default_image).into(holder.ivItem);
                } else {
                    Picasso.with(holder.ivItem.getContext()).load(objItemMaster.getSM_ImagePhysicalName()).into(holder.ivItem);
                }
            }
            if (objItemMaster.getShortDescription().equals("")) {
                holder.txtItemDescription.setVisibility(View.INVISIBLE);
            } else {
                holder.txtItemDescription.setVisibility(View.VISIBLE);
                holder.txtItemDescription.setText(objItemMaster.getShortDescription());
            }

        }

        holder.txtItemName.setText(objItemMaster.getItemName());
        holder.txtItemPrice.setText(context.getResources().getString(R.string.dfRupee) + " " + Globals.dfWithPrecision.format(objItemMaster.getSellPrice()));

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
        return alItemMaster.size();
    }

    public void SetSearchFilter(ArrayList<ItemMaster> result) {
        isItemAnimate = false;
        alItemMaster = new ArrayList<>();
        alItemMaster.addAll(result);
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void ButtonOnClick(ItemMaster objItemMaster);

        void CardViewOnClick(ItemMaster objItemMaster);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtItemDescription, txtItemPrice;
        ImageView ivItem;
        CardView cvItem;
        Button btnAdd;

        public ItemViewHolder(View itemView) {
            super(itemView);

            cvItem = (CardView) itemView.findViewById(R.id.cvItem);

            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);

            txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
            txtItemDescription = (TextView) itemView.findViewById(R.id.txtItemDescription);
            txtItemPrice = (TextView) itemView.findViewById(R.id.txtItemPrice);

            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);

            if (!isWaiterGrid && isViewChange) {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                width = displayMetrics.widthPixels / 2 - 24;
                height = displayMetrics.widthPixels / 2 - 24;

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
                ivItem.setLayoutParams(layoutParams);
            }

            cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Globals.HideKeyBoard(context, v);
                    if (isWaiterGrid) {
                        objItemClickListener.ButtonOnClick(alItemMaster.get(getAdapterPosition()));
                    } else {
                        objItemClickListener.CardViewOnClick(alItemMaster.get(getAdapterPosition()));
                    }

                }

            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Globals.HideKeyBoard(context, v);
                    objItemClickListener.ButtonOnClick(alItemMaster.get(getAdapterPosition()));
                }
            });
        }
    }
}
