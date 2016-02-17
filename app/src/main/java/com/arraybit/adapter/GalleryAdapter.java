package com.arraybit.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.modal.BusinessGalleryTran;
import com.arraybit.pos.FullViewDialogFragment;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    Context context;
    ArrayList<BusinessGalleryTran> alBusinessGalleryTran;
    View view;
    FragmentManager fragmentManager;
    private LayoutInflater inflater;

    public GalleryAdapter(Context context, ArrayList<BusinessGalleryTran> alBusinessGalleryTran, FragmentManager fragmentManager) {
        this.context = context;
        this.alBusinessGalleryTran = alBusinessGalleryTran;
        inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, final int position) {
        BusinessGalleryTran current = alBusinessGalleryTran.get(position);
        holder.txtGalleryTitle.setText(current.getImageTitle());
        holder.txtGalleryTitle.setVisibility(View.GONE);
        holder.cvGallery.setId(position);
        if (current.getImagePhysicalName() != null) {
            Picasso.with(holder.ivGalleryImage.getContext()).load(current.getImagePhysicalName()).into(holder.ivGalleryImage);
        }
    }

    @Override
    public int getItemCount() {
        return alBusinessGalleryTran.size();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {

        TextView txtGalleryTitle;
        ImageView ivGalleryImage;
        CardView cvGallery;

        public GalleryViewHolder(View itemView) {
            super(itemView);

            txtGalleryTitle = (TextView) itemView.findViewById(R.id.txtGalleryTitle);
            ivGalleryImage = (ImageView) itemView.findViewById(R.id.ivGalleryImage);
            cvGallery = (CardView) itemView.findViewById(R.id.cvGallery);

            cvGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FullViewDialogFragment fullViewDialogFragment = new FullViewDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("BusinessGallery", alBusinessGalleryTran.get(v.getId()));
                    fullViewDialogFragment.setArguments(bundle);
                    fullViewDialogFragment.show(fragmentManager, "");
                }
            });
        }
    }
}
