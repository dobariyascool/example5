package com.arraybit.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.arraybit.modal.BusinessGalleryTran;
import com.arraybit.pos.FullViewDialogFragment;
import com.arraybit.pos.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    public boolean isItemAnimate;
    Context context;
    ArrayList<BusinessGalleryTran> alBusinessGalleryTran;
    View view;
    FragmentManager fragmentManager;
    int previousPosition;
    private LayoutInflater inflater;

    public GalleryAdapter(Context context, ArrayList<BusinessGalleryTran> alBusinessGalleryTran, FragmentManager fragmentManager, boolean isItemAnimate) {
        this.context = context;
        this.alBusinessGalleryTran = alBusinessGalleryTran;
        inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
        this.isItemAnimate = isItemAnimate;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, final int position) {
        BusinessGalleryTran current = alBusinessGalleryTran.get(position);
        if (current.getMD_ImagePhysicalName() != null && !current.getMD_ImagePhysicalName().equals("")) {
            Picasso.with(holder.ivGalleryImage.getContext()).load(current.getMD_ImagePhysicalName()).into(holder.ivGalleryImage);
        }

        //holder animation
        if (this.isItemAnimate) {
            if (position > previousPosition) {
                Globals.SetItemAnimator(holder);
            }
            previousPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return alBusinessGalleryTran.size();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGalleryImage;

        public GalleryViewHolder(View itemView) {
            super(itemView);

            ivGalleryImage = (ImageView) itemView.findViewById(R.id.ivGalleryImage);

            ivGalleryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FullViewDialogFragment fullViewDialogFragment = new FullViewDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("BusinessGallery", alBusinessGalleryTran.get(getAdapterPosition()));
                    fullViewDialogFragment.setArguments(bundle);
                    fullViewDialogFragment.show(fragmentManager, "");
                }
            });
        }
    }
}
