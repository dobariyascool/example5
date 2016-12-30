package com.arraybit.pos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rey.material.widget.TextView;

public class GuestOptionListFragment extends Fragment implements View.OnClickListener {


    LinearLayout guestOptionLayout, menuModeLayout, guestModeLayout;
    GuestLoginDialogFragment.LoginResponseListener objLoginResponseListener;
    com.rey.material.widget.TextView txtCartNumber;
    RelativeLayout relativeLayout;
    RelativeLayout footerLayout;
    Context context;
    ImageView ivHomeActivityImage;
    SharePreferenceManage sharePreferenceManage;
    TextView txtTableName;

    public GuestOptionListFragment (Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_option_list, container, false);
        boolean isStart = getArguments().getBoolean("isStart", false);
        guestOptionLayout = (LinearLayout) view.findViewById(R.id.guestOptionLayout);
//        if (!getActivity().getResources().getBoolean(R.bool.isTablet)) {
        menuModeLayout = (LinearLayout) view.findViewById(R.id.menuModeLayout);
        guestModeLayout = (LinearLayout) view.findViewById(R.id.guestModeLayout);

        ivHomeActivityImage = (ImageView) view.findViewById(R.id.ivHomeActivityImage);
        txtTableName = (TextView) view.findViewById(R.id.txtTableName);
//        }

        footerLayout = (RelativeLayout) view.findViewById(R.id.footerLayout);

        CardView cvMenu = (CardView) view.findViewById(R.id.cvMenu);
        CardView cvOrders = (CardView) view.findViewById(R.id.cvOrders);
        CardView cvOffers = (CardView) view.findViewById(R.id.cvOffers);
        CardView cvFeedback = (CardView) view.findViewById(R.id.cvFeedback);
        CardView cvMenuModeMenu = (CardView) view.findViewById(R.id.cvMenuModeMenu);
        CardView cvMenuModeOffers = (CardView) view.findViewById(R.id.cvMenuModeOffers);

        if (Globals.objAppThemeMaster != null) {
//            if (isStart) {
//                if (Globals.objAppThemeMaster.getBackImageName2() != null && !Globals.objAppThemeMaster.getBackImageName2().equals("")) {
////                Log.e("image", " " + Globals.objAppThemeMaster.getBackImageName2());
//                    Glide.with(this).load(Globals.objAppThemeMaster.getBackImageName2()).asBitmap().into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
//                            Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(resource, displayMetrics.widthPixels, displayMetrics.heightPixels);
//                            Drawable drawable = new BitmapDrawable(getResources(),resizeBitmap);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                guestOptionLayout.setBackground(drawable);
//                            }
//                        }
//                    });
//                } else {
//                    Globals.SetHomePageBackground(getActivity(), guestOptionLayout, null, null);
//                }
//
//                if (Globals.objAppThemeMaster.getLogoImageName() != null && !Globals.objAppThemeMaster.getLogoImageName().equals("")) {
////                Log.e("image", " " + Globals.objAppThemeMaster.getLogoImageName());
//                    Glide.with(this).load(Globals.objAppThemeMaster.getLogoImageName()).asBitmap().into(ivHomeActivityImage);
//                }
//            } else {
                sharePreferenceManage = new SharePreferenceManage();
                String encodedImage = sharePreferenceManage.GetPreference("GuestAppTheme", getActivity().getString(R.string.guestEncodedImage2), getActivity());
                String encodedLogoImage = sharePreferenceManage.GetPreference("GuestAppTheme", getResources().getString(R.string.encodedLogoImage), getActivity());
                if (encodedImage != null && !encodedImage.equals("")) {
                    Globals.SetPageBackground(getActivity(), encodedImage, guestOptionLayout, null, null, null);
                } else {
                    Globals.SetHomePageBackground(getActivity(), guestOptionLayout, null, null);
                }
                if (encodedLogoImage != null && !encodedLogoImage.equals("")) {
                    byte[] decodedString = Base64.decode(encodedLogoImage.getBytes(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ivHomeActivityImage.setImageDrawable(new BitmapDrawable(getActivity().getResources(), decodedByte));
                }
//            }
        } else {
            Globals.SetHomePageBackground(getActivity(), guestOptionLayout, null, null);
        }

        cvMenu.setOnClickListener(this);
        cvOrders.setOnClickListener(this);
        cvOffers.setOnClickListener(this);
        cvFeedback.setOnClickListener(this);
        cvMenuModeMenu.setOnClickListener(this);
        cvMenuModeOffers.setOnClickListener(this);

        if (GuestHomeActivity.isMenuMode) {
            txtTableName.setText(Globals.orderTypeMasterId == Globals.OrderType.DineIn.getValue() ? "Dine In" : "Take Away");
            menuModeLayout.setVisibility(View.VISIBLE);
            guestModeLayout.setVisibility(View.GONE);
        } else {
            txtTableName.setText(Globals.orderTypeMasterId == Globals.OrderType.DineIn.getValue() ? GuestHomeActivity.objTableMaster.getTableName() + " - Dine In" : GuestHomeActivity.objTableMaster.getTableName() + " - Take Away");
            menuModeLayout.setVisibility(View.GONE);
            guestModeLayout.setVisibility(View.VISIBLE);
        }
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (!GuestHomeActivity.isMenuMode) {
            Globals.SetOptionMenu(Globals.userName, getActivity(), menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!GuestHomeActivity.isMenuMode) {
            if (item.getItemId() == R.id.callWaiter) {
                CallWaiterDialog callWaiterDialog = new CallWaiterDialog();
                callWaiterDialog.show(getActivity().getSupportFragmentManager(), "");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!GuestHomeActivity.isMenuMode) {
            objLoginResponseListener = (GuestLoginDialogFragment.LoginResponseListener) getActivity();
            objLoginResponseListener.LoginResponse();
        }
    }

    @Override
    public void onClick(View v) {
        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_guest_options))) {
            if (v.getId() == R.id.cvOrders) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("TableMaster", GuestHomeActivity.objTableMaster);
                OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment();
                orderSummaryFragment.setArguments(bundle);
                Globals.ReplaceFragment(orderSummaryFragment, getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_order_summary));
            } else if (v.getId() == R.id.cvFeedback) {
                if (Globals.userName == null) {
                    GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
                    guestLoginDialogFragment.setTargetFragment(this, 0);
                    guestLoginDialogFragment.show(getActivity().getSupportFragmentManager(), "");
                } else {
                    Globals.ReplaceFragment(new FeedbackFragment(getActivity()), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_feedback));
                }
            } else if (v.getId() == R.id.cvMenu || v.getId() == R.id.cvMenuModeMenu) {
                if (v.getId() == R.id.cvMenu && !GuestHomeActivity.isMenuMode) {
                    Globals.isWishListShow = 1;
                    Globals.orderTypeMasterId = GuestHomeActivity.objTableMaster.getlinktoOrderTypeMasterId();
                } else {
                    Globals.isWishListShow = 0;
                }
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ParentActivity", true);
                intent.putExtra("IsFavoriteShow", false);
                intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
                getActivity().startActivityForResult(intent, 0);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);

            } else if (v.getId() == R.id.cvOffers || v.getId() == R.id.cvMenuModeOffers) {
                Globals.ReplaceFragment(new OfferFragment(getActivity()), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_offer));
            }
        }
    }
}
