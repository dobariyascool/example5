package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.arraybit.pos.CategoryItemFragment;
import com.arraybit.pos.R;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    boolean isVeg, isNonVeg, isJain;

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

        if (Globals.orderTypeMasterId == Globals.OrderType.TakeAway.getValue() && objItemMaster.getIsDineInOnly()) {
            holder.txtItemDineOnly.setVisibility(View.VISIBLE);
            if (isWaiterGrid) {
                holder.btnAddDisable.setVisibility(View.GONE);
                holder.btnAdd.setVisibility(View.GONE);
                holder.ibLike.setVisibility(View.INVISIBLE);

            }else {
                holder.btnAddDisable.setVisibility(View.VISIBLE);
                holder.btnAdd.setVisibility(View.GONE);
                if(Globals.isWishListShow==1) {
                    holder.ibLike.setVisibility(View.VISIBLE);
                }else{
                    holder.ibLike.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            holder.txtItemDineOnly.setVisibility(View.INVISIBLE);
            if (isWaiterGrid) {
                holder.btnAdd.setVisibility(View.GONE);
                holder.btnAddDisable.setVisibility(View.GONE);
            } else {
                holder.btnAdd.setVisibility(View.VISIBLE);
                holder.btnAddDisable.setVisibility(View.GONE);
            }
            if(Globals.isWishListShow==1) {
                holder.ibLike.setVisibility(View.VISIBLE);
            }else{
                holder.ibLike.setVisibility(View.INVISIBLE);
            }
        }



        if (!objItemMaster.getOptionValueTranIds().equals("")) {
            if (CheckOptionValue(objItemMaster.getOptionValueTranIds(), String.valueOf(Globals.OptionValue.DoubleSpicy.getValue()))) {
                holder.ivDoubleSpicy.setVisibility(View.VISIBLE);
                holder.ivSpicy.setVisibility(View.GONE);
                holder.ivSweet.setVisibility(View.GONE);
            } else if (CheckOptionValue(objItemMaster.getOptionValueTranIds(), String.valueOf(Globals.OptionValue.Spicy.getValue()))) {
                holder.ivSpicy.setVisibility(View.VISIBLE);
                holder.ivDoubleSpicy.setVisibility(View.GONE);
                holder.ivSweet.setVisibility(View.GONE);
            } else if (CheckOptionValue(objItemMaster.getOptionValueTranIds(), String.valueOf(Globals.OptionValue.Sweet.getValue()))) {
                holder.ivSweet.setVisibility(View.VISIBLE);
                holder.ivDoubleSpicy.setVisibility(View.GONE);
                holder.ivSpicy.setVisibility(View.GONE);
            } else {
                holder.ivSweet.setVisibility(View.GONE);
                holder.ivDoubleSpicy.setVisibility(View.GONE);
                holder.ivSpicy.setVisibility(View.GONE);
            }

            isVeg = CheckOptionValue(objItemMaster.getOptionValueTranIds(), String.valueOf(Globals.OptionValue.Veg.getValue()));
            isNonVeg = CheckOptionValue(objItemMaster.getOptionValueTranIds(), String.valueOf(Globals.OptionValue.NonVeg.getValue()));
            isJain = CheckOptionValue(objItemMaster.getOptionValueTranIds(), String.valueOf(Globals.OptionValue.Jain.getValue()));
            if (isNonVeg && !isVeg) {
                holder.ivNonVeg.setVisibility(View.VISIBLE);
                holder.ivJain.setVisibility(View.GONE);
            } else if (isJain && !isNonVeg) {
                holder.ivJain.setVisibility(View.VISIBLE);
                holder.ivNonVeg.setVisibility(View.GONE);
            } else {
                holder.ivJain.setVisibility(View.GONE);
                holder.ivNonVeg.setVisibility(View.GONE);
            }
        } else {
            holder.ivJain.setVisibility(View.GONE);
            holder.ivNonVeg.setVisibility(View.GONE);
            holder.ivSweet.setVisibility(View.GONE);
            holder.ivDoubleSpicy.setVisibility(View.GONE);
            holder.ivSpicy.setVisibility(View.GONE);
        }

        if (!isWaiterGrid && !isViewChange) {
            if ((holder.ivJain.getVisibility() == View.VISIBLE) || (holder.ivSpicy.getVisibility() == View.VISIBLE) || (holder.ivSweet.getVisibility() == View.VISIBLE) || (holder.ivDoubleSpicy.getVisibility() == View.VISIBLE)) {
                holder.txtItemName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(17)});
                if (objItemMaster.getItemName().length() > 15) {
                    holder.txtItemName.setText(objItemMaster.getItemName().substring(0, 15) + "...");
                } else {
                    holder.txtItemName.setText(objItemMaster.getItemName());
                }

            } else {
                holder.txtItemName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(objItemMaster.getItemName().length())});
                holder.txtItemName.setText(objItemMaster.getItemName());
            }
        } else if (isViewChange) {
            if (!isWaiterGrid) {
                if ((holder.ivJain.getVisibility() == View.VISIBLE) || (holder.ivSpicy.getVisibility() == View.VISIBLE) || (holder.ivSweet.getVisibility() == View.VISIBLE) || (holder.ivDoubleSpicy.getVisibility() == View.VISIBLE)) {
                    holder.txtItemName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                    if (objItemMaster.getItemName().length() > 9) {
                        holder.txtItemName.setText(objItemMaster.getItemName().substring(0, 9) + "...");
                    } else {
                        holder.txtItemName.setText(objItemMaster.getItemName());
                    }
                } else {
                    holder.txtItemName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(objItemMaster.getItemName().length())});
                    holder.txtItemName.setText(objItemMaster.getItemName());
                }
            } else {
                holder.txtItemName.setText(objItemMaster.getItemName());
            }
        }
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

    private boolean CheckOptionValue(String optionValueIds, String optionValue) {
        List<String> items = Arrays.asList(optionValueIds.split(","));
        boolean isMatch = false;
        for (String str : items) {
            if (str.equals(optionValue)) {
                isMatch = true;
                break;
            }
        }
        return isMatch;
    }

    public interface ItemClickListener {
        void ButtonOnClick(ItemMaster objItemMaster);

        void CardViewOnClick(ItemMaster objItemMaster);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtItemDescription, txtItemPrice, txtItemDineOnly;
        CardView cvItem;
        Button btnAdd, btnAddDisable;
        ImageView ivItem, ivJain, ivSpicy, ivDoubleSpicy, ivSweet, ivNonVeg;
        ToggleButton ibLike;

        public ItemViewHolder(View itemView) {
            super(itemView);

            cvItem = (CardView) itemView.findViewById(R.id.cvItem);

            ibLike = (ToggleButton) itemView.findViewById(R.id.ibLike);

            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            ivJain = (ImageView) itemView.findViewById(R.id.ivJain);
            ivSpicy = (ImageView) itemView.findViewById(R.id.ivSpicy);
            ivDoubleSpicy = (ImageView) itemView.findViewById(R.id.ivDoubleSpicy);
            ivSweet = (ImageView) itemView.findViewById(R.id.ivSweet);
            ivNonVeg = (ImageView) itemView.findViewById(R.id.ivNonVeg);

            txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
            txtItemDescription = (TextView) itemView.findViewById(R.id.txtItemDescription);
            txtItemPrice = (TextView) itemView.findViewById(R.id.txtItemPrice);
            txtItemDineOnly = (TextView) itemView.findViewById(R.id.txtItemDineOnly);

            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
            btnAddDisable = (Button) itemView.findViewById(R.id.btnAddDisable);

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

            ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ibLike.isChecked()) {
                        ibLike.setChecked(true);
                    }
                }
            });
        }
    }
}
