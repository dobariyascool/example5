package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.modal.BusinessGalleryTran;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//Test
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    Context context;
    ArrayList<BusinessGalleryTran> alBusinessGalleryTran;
    View view;
    private LayoutInflater inflater;

    public GalleryAdapter(Context context, ArrayList<BusinessGalleryTran> alBusinessGalleryTran) {
        this.context = context;
        this.alBusinessGalleryTran = alBusinessGalleryTran;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        BusinessGalleryTran current = alBusinessGalleryTran.get(position);
        holder.txtGalleryTitle.setText(current.getImageTitle());
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

        public GalleryViewHolder(View itemView) {
            super(itemView);

            txtGalleryTitle = (TextView) itemView.findViewById(R.id.txtGalleryTitle);
            ivGalleryImage = (ImageView) itemView.findViewById(R.id.ivGalleryImage);
        }
    }
}
