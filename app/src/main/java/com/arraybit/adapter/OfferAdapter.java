package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.modal.OfferMaster;
import com.arraybit.pos.GuestHomeActivity;
import com.arraybit.pos.OfferDetailFragment;
import com.arraybit.pos.R;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {


    public boolean isItemAnimate;
    View view;
    Context context;
    ArrayList<OfferMaster> alOfferMaster;
    FragmentManager fragmentManager;
    int previousPosition;
    SimpleDateFormat sdfDate = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("d MMM", Locale.US);


    public OfferAdapter(Context context, ArrayList<OfferMaster> result, FragmentManager fragmentManager, Boolean isItemAnimate) {
        this.context = context;
        alOfferMaster = result;
        this.fragmentManager = fragmentManager;
        this.isItemAnimate = isItemAnimate;
    }


    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OfferViewHolder holder, int position) {
        OfferMaster objOfferMaster = alOfferMaster.get(position);
        holder.txtOfferTitle.setText(objOfferMaster.getOfferTitle());
        if (objOfferMaster.getOfferContent().equals("null") || objOfferMaster.getOfferContent().equals("")) {
            holder.txtOfferContent.setVisibility(View.GONE);
        } else {
            holder.txtOfferContent.setVisibility(View.VISIBLE);
            holder.txtOfferContent.setText(objOfferMaster.getOfferContent());
        }
        if (objOfferMaster.getToDate() == null || objOfferMaster.getToDate().equals("")) {
            holder.dateLayout.setVisibility(View.GONE);
        } else {
            holder.dateLayout.setVisibility(View.VISIBLE);
            try {
                Date date = sdfDate.parse(objOfferMaster.getToDate());
                String str = sdfDateFormat.format(date);
                holder.txtOfferExpiredDate.setText(str);
            } catch (ParseException e) {
                holder.dateLayout.setVisibility(View.GONE);
            }
        }
        if (!objOfferMaster.getMD_ImagePhysicalName().equals("")) {
            Picasso.with(holder.ivOffer.getContext()).load(objOfferMaster.getMD_ImagePhysicalName()).into(holder.ivOffer);
        }


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
        return alOfferMaster.size();
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {

        TextView txtOfferTitle, txtOfferContent, txtOfferExpiredDate, txtOfferExpired;
        ImageView ivOffer;
        CardView cvOffer;
        LinearLayout titleLayout, dateLayout;


        public OfferViewHolder(View itemView) {
            super(itemView);

            titleLayout = (LinearLayout) itemView.findViewById(R.id.titleLayout);
            dateLayout = (LinearLayout) itemView.findViewById(R.id.dateLayout);

            cvOffer = (CardView) itemView.findViewById(R.id.cvOffer);

            ivOffer = (ImageView) itemView.findViewById(R.id.ivOffer);

            txtOfferTitle = (TextView) itemView.findViewById(R.id.txtOfferTitle);
            txtOfferContent = (TextView) itemView.findViewById(R.id.txtOfferContent);
            txtOfferExpiredDate = (TextView) itemView.findViewById(R.id.txtOfferExpiredDate);
            txtOfferExpired = (TextView) itemView.findViewById(R.id.txtOfferExpired);

            if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {
                if (Globals.objAppThemeMaster != null) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        cvOffer.setCardBackgroundColor(Globals.objAppThemeMaster.getColorCardView());
                        cvOffer.setElevation(4f);
                    } else {
                        cvOffer.setCardBackgroundColor(Globals.objAppThemeMaster.getColorCardView());
                    }

                    titleLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparentBrown));
                    dateLayout.setBackgroundColor(Globals.objAppThemeMaster.getColorAccent());
                    txtOfferTitle.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorCardText()));
                    txtOfferContent.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dimWhite)));
                    txtOfferExpired.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorPrimary()));
                    txtOfferExpiredDate.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorPrimary()));
                } else {
                    if (Build.VERSION.SDK_INT >= 21) {
                        cvOffer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_orange));
                        cvOffer.setElevation(4f);
                    } else {
                        cvOffer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_orange));
                    }

                    titleLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparentBrown));
                    dateLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.accent));
                    txtOfferTitle.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.white)));
                    txtOfferContent.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dimWhite)));
                    txtOfferExpired.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple)));
                    txtOfferExpiredDate.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple)));
                }
            }

            cvOffer.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RtlHardcoded")
                @Override
                public void onClick(View v) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    OfferDetailFragment fragment = new OfferDetailFragment(alOfferMaster.get(getAdapterPosition()));
                    if (Build.VERSION.SDK_INT >= 21) {
                        Slide slideTransition = new Slide();
                        slideTransition.setSlideEdge(Gravity.RIGHT);
                        slideTransition.setDuration(500);
                        fragment.setEnterTransition(slideTransition);
                    } else {
                        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
                    }
                    fragmentTransaction.replace(R.id.offerFragment, fragment, context.getResources().getString(R.string.title_fragment_offer_detail));
                    fragmentTransaction.addToBackStack(context.getResources().getString(R.string.title_fragment_offer_detail));
                    fragmentTransaction.commit();
                }
            });

        }
    }
}