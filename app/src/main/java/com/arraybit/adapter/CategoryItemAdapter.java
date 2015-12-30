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

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.pos.CategoryItemFragment;
import com.arraybit.pos.R;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ItemViewHolder> {

    int width, height;
    FragmentManager fragmentManager;
    boolean isViewChange;
    boolean isWaiterGrid = false;
    View view;
    Context context;
    ArrayList<ItemMaster> alItemMaster;
    ItemMaster objItemMaster;
    ItemClickListener objItemClickListener;
    SharePreferenceManage objSharePreferenceManage;

    public CategoryItemAdapter(Context context, ArrayList<ItemMaster> result, FragmentManager fragmentManager, boolean isViewChange, ItemClickListener objItemClickListener) {
        this.context = context;
        alItemMaster = result;
        this.fragmentManager = fragmentManager;
        this.isViewChange = isViewChange;
        this.objItemClickListener = objItemClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        objSharePreferenceManage = new SharePreferenceManage();
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
        holder.cvItem.setId(position);
        holder.btnAdd.setId(position);

        if (!isWaiterGrid) {

            if(width!=-1 && height!=-1) {
                if (objItemMaster.getImageName().equals("null")) {
                    Picasso.with(holder.ivItem.getContext()).load(R.drawable.vada_paav).priority(Picasso.Priority.NORMAL).resize(width, height).into(holder.ivItem);
                } else {
                    Picasso.with(holder.ivItem.getContext()).load(objItemMaster.getImageName()).priority(Picasso.Priority.NORMAL).resize(width, height).into(holder.ivItem);
                }
            }
        }

        holder.txtItemName.setText(objItemMaster.getItemName());
        if(objItemMaster.getShortDescription().equals("")){
            holder.txtItemDescription.setVisibility(View.GONE);
        }else{
            holder.txtItemDescription.setVisibility(View.VISIBLE);
            holder.txtItemDescription.setText(objItemMaster.getShortDescription());
        }
        holder.txtItemPrice.setText("Rs. " + Globals.dfWithPrecision.format(objItemMaster.getSellPrice()));
    }

    @Override
    public int getItemCount() {
        return alItemMaster.size();
    }

    public void ItemListDataChanged(ArrayList<ItemMaster> result) {
        alItemMaster.addAll(result);
        notifyDataSetChanged();
    }

    public void SetSearchFilter(ArrayList<ItemMaster> result) {
        alItemMaster = new ArrayList<>();
        alItemMaster.addAll(result);
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        public void ButtonOnClick(ItemMaster objItemMaster);

        public void CardViewOnClick(ItemMaster objItemMaster);
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

            if(!isWaiterGrid)
            {
                width = ivItem.getLayoutParams().width;
                if(width==-1)
                {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    width = displayMetrics.widthPixels/2;
                }
                height = ivItem.getLayoutParams().height;
            }

//            if(!isWaiterGrid) {
//                if(isViewChange) {
//                    itemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//                        @Override
//                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//
//                            final ImageView imageView = (ImageView) v.findViewById(R.id.ivItem);
//                            imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                                @Override
//                                public void onGlobalLayout() {
//
//                                    objSharePreferenceManage = new SharePreferenceManage();
//                                    objSharePreferenceManage.CreatePreference("ImagePreference", "Width", String.valueOf(imageView.getWidth()), context);
//                                    objSharePreferenceManage.CreatePreference("ImagePreference", "Height", String.valueOf(imageView.getHeight()), context);
//                                    notifyDataSetChanged();
//                                }
//                            });
//                        }
//                    });
//                }
//            }
            cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //orderItemDetail=(OrderItemDetail)context;
                    //orderItemDetail.ItemDetail();
                    //DetailFragment detailFragment = new DetailFragment(v.getId());
                    Globals.HideKeyBoard(context,v);
                    if (isWaiterGrid) {
                        objItemClickListener.ButtonOnClick(alItemMaster.get(v.getId()));
                    } else {
                        objItemClickListener.CardViewOnClick(alItemMaster.get(v.getId()));
                    }

                }

            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //objItemClickListener = (ItemClickListener)context;
                    Globals.HideKeyBoard(context,v);
                    objItemClickListener.ButtonOnClick(alItemMaster.get(v.getId()));
                    //AddItemQtyDialogFragment addItemQtyDialogFragment = new AddItemQtyDialogFragment();
                    //addItemQtyDialogFragment.show(fragmentManager,"");
                }
            });
        }
    }
}
