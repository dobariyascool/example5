package com.arraybit.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arraybit.global.Globals;
import com.arraybit.pos.DetailFragment;
import com.arraybit.pos.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    OrderItemDetail orderItemDetail;
    FragmentManager fragmentManager;
    boolean isViewChange;
    View view;
    private List<String> mItemList;

    public RecyclerAdapter(List<String> itemList,FragmentManager fragmentManager,boolean isViewChange) {
        mItemList = itemList;
        this.fragmentManager=fragmentManager;
        this.isViewChange = isViewChange;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final Context context = parent.getContext();
        if(isViewChange)
        {
            view = LayoutInflater.from(context).inflate(R.layout.row_recylerview_grid, parent, false);
        }
        else
        {
            view = LayoutInflater.from(context).inflate(R.layout.row_recyclerview, parent, false);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //orderItemDetail=(OrderItemDetail)context;
                //orderItemDetail.ItemDetail();
                DetailFragment detailFragment=new DetailFragment();
                Globals.InitializeFragment(detailFragment, fragmentManager);

            }
        });
        return RecyclerItemViewHolder.newInstance(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerItemViewHolder holder1 = (RecyclerItemViewHolder) holder;
        String itemText = mItemList.get(position);
        holder1.setItemText(itemText);

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public interface OrderItemDetail
    {
        public void ItemDetail();
    }

    public static class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView mItemTextView;

        public RecyclerItemViewHolder(final View parent, TextView itemTextView) {
            super(parent);
            mItemTextView = itemTextView;
        }

        public static RecyclerItemViewHolder newInstance(View parent) {
            TextView itemTextView = (TextView) parent.findViewById(R.id.itemTextView);
            return new RecyclerItemViewHolder(parent, itemTextView);
        }

        public void setItemText(CharSequence text) {
            mItemTextView.setText(text);
        }

    }

}
