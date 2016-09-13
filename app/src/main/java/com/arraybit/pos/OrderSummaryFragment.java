package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.arraybit.adapter.OrderSummaryAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.DiscountMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OfferMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.SalesMaster;
import com.arraybit.modal.TableMaster;
import com.arraybit.modal.TaxMaster;
import com.arraybit.parser.DiscountJSONParser;
import com.arraybit.parser.ItemJSONParser;
import com.arraybit.parser.OfferJSONParser;
import com.arraybit.parser.OrderJOSNParser;
import com.arraybit.parser.SalesJSONParser;
import com.arraybit.parser.TableJSONParser;
import com.arraybit.parser.TaxJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings({"unchecked", "ConstantConditions"})
@SuppressLint("ValidFragment")
public class OrderSummaryFragment extends Fragment implements View.OnClickListener, AddDiscountDialogFragment.DiscountSelectionListener, GuestLoginDialogFragment.LoginResponseListener, ConfirmDialog.ConfirmationResponseListener {


    public DiscountMaster discountMaster;
    RecyclerView rvOrderItemSummery;
    LinearLayout headerLayout, amountLayout, taxLayout, errorLayout;
    FrameLayout orderSummeryLayout;
    TableMaster objTableMaster;
    SharePreferenceManage objSharePreferenceManage;
    ArrayList<OrderMaster> lstOrderMaster;
    ArrayList<ItemMaster> alOrderItemTran;
    short counterMasterId, userMasterId, waiterMasterId;
    double totalAmount, totalTax, totalDiscount, tax1, tax2, tax3, tax4, tax5;
    String billNumber = null;
    ArrayList<TaxMaster> alTaxMaster;
    TextView txtTotalAmount, txtTotalDiscount, txtNetAmount, txtHeaderDiscount, txtRoundingOff;
    TextView txtHeaderTotalAmount, txtHeaderNetAmount, txtHeaderRounding, txtMsg;
    ImageView ivErrorIcon;
    String strNetAmount;
    OrderSummaryAdapter orderSummeryAdapter;
    boolean isHomeShow = false;
    com.arraybit.pos.ProgressDialog progressDialog;
    View focusView, snackFocus;
    CompoundButton cbOrderPlace, cbGetPromoCode;
    EditText etOfferCode;
    Button btnApply;
    RelativeLayout rlPromoCode;
    OfferMaster objOfferMaster;

    public OrderSummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_summary, container, false);

        setHasOptionsMenu(true);

        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
            objTableMaster = MenuActivity.objTableMaster;
        } else if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            Bundle bundle = getArguments();
            if (bundle != null && bundle.getParcelable("TableMaster") != null) {
                objTableMaster = bundle.getParcelable("TableMaster");
                isHomeShow = bundle.getBoolean("isHomeShow");
            }
        }

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName()
                    .equals(getActivity().getResources().getString(R.string.title_fragment_guest_options))) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                if (isHomeShow) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                } else {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }
            if (objTableMaster != null && objTableMaster.getTableName() != null) {
                app_bar.setTitle(objTableMaster.getTableName() + "  Summary");
            } else {
                app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_order_detail));
            }
        }
        //end

        orderSummeryLayout = (FrameLayout) view.findViewById(R.id.orderSummeryLayout);

        headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);
        amountLayout = (LinearLayout) view.findViewById(R.id.amountLayout);
        taxLayout = (LinearLayout) view.findViewById(R.id.taxLayout);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);
        ivErrorIcon = (ImageView) errorLayout.findViewById(R.id.ivErrorIcon);
        cbOrderPlace = (CompoundButton) errorLayout.findViewById(R.id.cbOrderPlace);

        rlPromoCode = (RelativeLayout) view.findViewById(R.id.rlPromoCode);
        cbGetPromoCode = (CompoundButton) view.findViewById(R.id.cbGetPromoCode);
        etOfferCode = (EditText) view.findViewById(R.id.etOfferCode);
        btnApply = (Button) view.findViewById(R.id.btnApply);

        txtTotalAmount = (TextView) view.findViewById(R.id.txtTotalAmount);
        txtTotalDiscount = (TextView) view.findViewById(R.id.txtTotalDiscount);
        txtNetAmount = (TextView) view.findViewById(R.id.txtNetAmount);
        txtHeaderDiscount = (TextView) view.findViewById(R.id.txtHeaderDiscount);
        txtHeaderNetAmount = (TextView) view.findViewById(R.id.txtHeaderNetAmount);
        txtHeaderRounding = (TextView) view.findViewById(R.id.txtHeaderRounding);
        txtHeaderTotalAmount = (TextView) view.findViewById(R.id.txtHeaderTotalAmount);
        txtRoundingOff = (TextView) view.findViewById(R.id.txtRoundingOff);

        rvOrderItemSummery = (RecyclerView) view.findViewById(R.id.rvOrderItemSummery);
        rvOrderItemSummery.setVisibility(View.GONE);

        Button btnAddMore = (Button) view.findViewById(R.id.btnAddMore);
        Button btnCheckOut = (Button) view.findViewById(R.id.btnCheckOut);

        TextView txtHeaderItem = (TextView) view.findViewById(R.id.txtHeaderItem);
        TextView txtHeaderNo = (TextView) view.findViewById(R.id.txtHeaderNo);
        TextView txtHeaderRate = (TextView) view.findViewById(R.id.txtHeaderRate);
        TextView txtHeaderAmount = (TextView) view.findViewById(R.id.txtHeaderAmount);

        if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {
            if (Globals.objAppThemeMaster != null) {
                Globals.SetToolBarBackground(getActivity(), app_bar, Globals.objAppThemeMaster.getColorPrimary(), Globals.objAppThemeMaster.getColorCardText());

                objSharePreferenceManage = new SharePreferenceManage();
                String encodedImage = objSharePreferenceManage.GetPreference("GuestAppTheme", "EncodedBackImage1", getActivity());
                if (encodedImage != null && !encodedImage.equals("")) {
                    Globals.SetPageBackground(getActivity(), encodedImage, null, null, orderSummeryLayout, null);
//                Globals.SetPageBackground(getActivity(), Globals.objAppThemeMaster.getBackImageName1(), null, null, orderSummeryLayout, null);
                } else {
                    Globals.SetScaleImageBackground(getActivity(), null, null, orderSummeryLayout);
                }
//                if (Globals.objAppThemeMaster.getBackImageName1() != null && !Globals.objAppThemeMaster.getBackImageName1().equals("")) {
////                    Log.e("image", " " + Globals.objAppThemeMaster.getBackImageName1());
//                    Glide.with(this).load(Globals.objAppThemeMaster.getBackImageName1()).asBitmap().into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            Drawable drawable = new BitmapDrawable(resource);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                orderSummeryLayout.setBackground(drawable);
//                            }
//                        }
//                    });
//                } else {
//                    Globals.SetScaleImageBackground(getActivity(), null, null, orderSummeryLayout);
//                }

                headerLayout.setBackgroundColor(Globals.objAppThemeMaster.getColorAccentDark());
                txtHeaderItem.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorPrimary()));
                txtHeaderNo.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorPrimary()));
                txtHeaderRate.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorPrimary()));
                txtHeaderAmount.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorPrimary()));

                txtHeaderNetAmount.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_blur));
                txtHeaderTotalAmount.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_blur));
                txtHeaderRounding.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                txtTotalAmount.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                txtNetAmount.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                txtRoundingOff.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                txtTotalDiscount.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));

                ivErrorIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.errorIconColor), PorterDuff.Mode.SRC_IN);
                txtMsg.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                cbOrderPlace.setTextColor(Globals.objAppThemeMaster.getColorAccent());

                cbGetPromoCode.setTextColor(Globals.objAppThemeMaster.getColorAccentDark());
//                btnApply.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rightside_rounded_button));
                LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(getActivity(),R.drawable.rightside_rounded_button);
                GradientDrawable gradientDrawable1 = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.gradientDrawable1);
                gradientDrawable1.setColor(Globals.objAppThemeMaster.getColorAccent());
                GradientDrawable gradientDrawable2 = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.gradientDrawable2);
                gradientDrawable2.setColor(Globals.objAppThemeMaster.getColorAccent());
                GradientDrawable gradientDrawable3 = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.gradientDrawable3);
                gradientDrawable3.setColor(Globals.objAppThemeMaster.getColorAccent());
                btnApply.setBackground(layerDrawable);
                btnApply.setTextColor(Globals.objAppThemeMaster.getColorPrimary());
                etOfferCode.setTextColor(Globals.objAppThemeMaster.getColorCardText());

                GradientDrawable gd = new GradientDrawable();
                gd.setShape(GradientDrawable.RECTANGLE);
                gd.setCornerRadius(8);
                gd.setStroke(4, Globals.objAppThemeMaster.getColorAccent());
                etOfferCode.setBackground(gd);

                Globals.CustomView(btnAddMore, ContextCompat.getColor(getActivity(), android.R.color.transparent), Globals.objAppThemeMaster.getColorAccent());
                Globals.CustomView(btnCheckOut, Globals.objAppThemeMaster.getColorAccent(), ContextCompat.getColor(getActivity(), android.R.color.transparent));
                btnAddMore.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorAccent()));
                btnCheckOut.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorPrimary()));

            } else {
                Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary), ContextCompat.getColor(getActivity(), android.R.color.white));

                Globals.SetScaleImageBackground(getActivity(), null, null, orderSummeryLayout);

                headerLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.accent_dark));

                txtHeaderItem.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));
                txtHeaderNo.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));
                txtHeaderRate.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));
                txtHeaderAmount.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));

                txtHeaderNetAmount.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_blur));
                txtHeaderTotalAmount.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_blur));
                txtHeaderRounding.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                txtTotalAmount.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                txtNetAmount.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                txtRoundingOff.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                txtTotalDiscount.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));

                ivErrorIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.errorIconColor), PorterDuff.Mode.SRC_IN);
                txtMsg.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
                cbOrderPlace.setTextColor(ContextCompat.getColor(getActivity(), R.color.accent));

                cbGetPromoCode.setTextColor(ContextCompat.getColor(getActivity(), R.color.accent_dark));
                LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(getActivity(),R.drawable.rightside_rounded_button);
                GradientDrawable gradientDrawable1 = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.gradientDrawable1);
                gradientDrawable1.setColor(ContextCompat.getColor(getActivity(), R.color.accent));
                GradientDrawable gradientDrawable2 = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.gradientDrawable2);
                gradientDrawable2.setColor(ContextCompat.getColor(getActivity(), R.color.accent));
                GradientDrawable gradientDrawable3 = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.gradientDrawable3);
                gradientDrawable3.setColor(ContextCompat.getColor(getActivity(), R.color.accent));
                btnApply.setBackground(layerDrawable);

                btnApply.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rightside_rounded_button));
                btnApply.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
                etOfferCode.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                GradientDrawable gd = new GradientDrawable();
                gd.setShape(GradientDrawable.RECTANGLE);
                gd.setCornerRadius(8);
                gd.setStroke(4, ContextCompat.getColor(getActivity(), R.color.accent));
                etOfferCode.setBackground(gd);

                Globals.CustomView(btnAddMore, ContextCompat.getColor(getActivity(), android.R.color.transparent), ContextCompat.getColor(getActivity(), R.color.accent_secondary));
                Globals.CustomView(btnCheckOut, ContextCompat.getColor(getActivity(), R.color.accent_secondary), ContextCompat.getColor(getActivity(), android.R.color.transparent));
                btnAddMore.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.accent_secondary)));
                btnCheckOut.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));
            }
        } else {
            Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary_black), ContextCompat.getColor(getActivity(), android.R.color.white));
            orderSummeryLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_img));
            int[][] states = new int[][] { new int[] { android.R.attr.state_pressed }, new int[] {  } };
            int[] colors = new int[] { ContextCompat.getColor(getActivity(),android.R.color.white) ,ContextCompat.getColor(getActivity(),R.color.accent_red)  };

            btnCheckOut.setTextColor(new ColorStateList(states, colors));
//            Globals.CustomView(btnAddMore, ContextCompat.getColor(getActivity(), android.R.color.transparent), ContextCompat.getColor(getActivity(), R.color.red_tab_indicator));
            Globals.CustomView(btnCheckOut, ContextCompat.getColor(getActivity(), R.color.accent_red), ContextCompat.getColor(getActivity(), android.R.color.transparent));
//            btnAddMore.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.red_tab_indicator)));
            btnCheckOut.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), android.R.color.white)));
        }

        etOfferCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etOfferCode.getText().toString().equals("")) {
                    btnApply.setText(getResources().getString(R.string.coaCancel));
//                    OfferMaster objOfferMaster = new OfferMaster();
//                    objOfferMaster.setOfferCode("Remove");
//                    if (tbHomeDelivery.isChecked()) {
//                        SaveCheckOutData(null, objOfferMaster, Globals.OrderType.HomeDelivery.getValue());
//                    } else if (tbTakeAway.isChecked()) {
//                        SaveCheckOutData(null, objOfferMaster, Globals.OrderType.TakeAway.getValue());
//                    }

                } else {
                    if (!etOfferCode.getText().toString().equals(etOfferCode.getText().toString().toUpperCase())) {
                        etOfferCode.setText(etOfferCode.getText().toString().toUpperCase());
                    }
                    if (etOfferCode.getText().toString().contains(" ")) {
                        etOfferCode.setText(etOfferCode.getText().toString().trim());
                    }
                    etOfferCode.setSelection(etOfferCode.getText().length());
                    btnApply.setText(getResources().getString(R.string.coaApply));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnAddMore.setOnClickListener(this);
        btnCheckOut.setOnClickListener(this);
        cbOrderPlace.setOnClickListener(this);
        cbGetPromoCode.setOnClickListener(this);
        btnApply.setOnClickListener(this);

        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getString(R.string.title_fragment_guest_options))) {
            txtHeaderDiscount.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
        } else {
            txtHeaderDiscount.setTextColor(ContextCompat.getColor(getActivity(), R.color.accent_red));
            txtHeaderDiscount.setOnClickListener(this);
        }

        GetValueFromSharePreference();

        if (Service.CheckNet(getActivity())) {
            new AllOrdersLoadingTask().execute();
        } else {
            SetErrorLayout(true, getResources().getString(R.string.MsgCheckConnection));
        }


        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(0).getName()
                .equals(getActivity().getResources().getString(R.string.title_fragment_guest_options))) {
            menu.findItem(R.id.home).setVisible(false);
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.shortList).setVisible(false);
            menu.findItem(R.id.callWaiter).setVisible(false);
            menu.findItem(R.id.cart_layout).setVisible(false);
            //Globals.SetOptionMenu(Globals.userName, getActivity(), menu);
        } else if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            if (isHomeShow) {
                menu.findItem(R.id.home).setVisible(true);
            } else {
                menu.findItem(R.id.home).setVisible(false);
            }
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.notification_layout).setVisible(false);
        } else if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
            menu.findItem(R.id.home).setVisible(true);
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            Globals.objDiscountMaster = null;
        }
        if (item.getItemId() == R.id.home) {
            if (MenuActivity.parentActivity) {
                Globals.isWishListShow = 1;
                Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
                intent.putExtra("ParentActivity", true);
                intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                Globals.isWishListShow = 0;
                Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
            Globals.objDiscountMaster = null;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddMore) {
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
                if (MenuActivity.parentActivity || GuestHomeActivity.isGuestMode) {
                    if (GuestHomeActivity.isGuestMode) {
                        getActivity().getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_order_summary), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    Globals.isWishListShow = 1;
                    Globals.orderTypeMasterId = objTableMaster.getlinktoOrderTypeMasterId();
                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    intent.putExtra("ParentActivity", true);
                    intent.putExtra("TableMaster", objTableMaster);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    Globals.isWishListShow = 0;
                    Globals.orderTypeMasterId = objTableMaster.getlinktoOrderTypeMasterId();
                    if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
                        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_order_summary))) {
                            getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    } else {
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("TableMaster", objTableMaster);
                        intent.putExtra("IsFavoriteShow", true);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                }

            } else {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_order_summary))) {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        } else if (v.getId() == R.id.btnCheckOut) {
            focusView = v;
            ConfirmDialog confirmDialog = new ConfirmDialog(getActivity().getResources().getString(R.string.cdfCheckOutMsg), false);
            confirmDialog.setTargetFragment(this, 0);
            confirmDialog.show(getActivity().getSupportFragmentManager(), "");
        } else if (v.getId() == R.id.txtHeaderDiscount) {
            Bundle bundle = new Bundle();
            bundle.putDouble("TotalAmount", totalAmount);
            if (lstOrderMaster.get(0).getDiscountPercentage() != 0) {
                bundle.putDouble("Discount", lstOrderMaster.get(0).getDiscountPercentage());
                bundle.putBoolean("isPercentage", true);
            } else {
                double totaldiscount = 0;
                for (int i = 0; i < lstOrderMaster.size(); i++) {
                    totaldiscount = totaldiscount + lstOrderMaster.get(i).getDiscount();
                }
                Log.e("dis", " " + totaldiscount);
                bundle.putDouble("Discount", totaldiscount);
                bundle.putBoolean("isPercentage", false);
            }
            AddDiscountDialogFragment addDiscountDialogFragment = new AddDiscountDialogFragment();
            addDiscountDialogFragment.setTargetFragment(this, 0);
            addDiscountDialogFragment.setArguments(bundle);
            addDiscountDialogFragment.show(getFragmentManager(), "");
        } else if (v.getId() == R.id.cbOrderPlace) {
            if (MenuActivity.parentActivity) {
                Globals.isWishListShow = 1;
                Globals.orderTypeMasterId = objTableMaster.getlinktoOrderTypeMasterId();
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ParentActivity", true);
                intent.putExtra("TableMaster", objTableMaster);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                Globals.isWishListShow = 0;
                Globals.orderTypeMasterId = objTableMaster.getlinktoOrderTypeMasterId();
                if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                            && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_order_summary))) {
                        getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                } else {
                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("TableMaster", objTableMaster);
                    intent.putExtra("IsFavoriteShow", true);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        } else if (v.getId() == R.id.cbGetPromoCode) {
            rlPromoCode.setVisibility(View.VISIBLE);
            cbGetPromoCode.setVisibility(View.GONE);

            btnApply.setSelected(true);
        } else if (v.getId() == R.id.btnApply) {
            Globals.HideKeyBoard(getActivity(), v);
            snackFocus = v;
            if (btnApply.getText().equals(getResources().getString(R.string.coaCancel))) {
                cbGetPromoCode.setVisibility(View.VISIBLE);
                rlPromoCode.setVisibility(View.GONE);

                if (discountMaster != null) {
                    Globals.objDiscountMaster = discountMaster;
                    discountMaster = null;
                    objOfferMaster = null;
                    CalculateDiscount(totalAmount, totalTax);
                    etOfferCode.setText("");

                }
            } else {
                if (Service.CheckNet(getActivity())) {
                    new RequestVerifyOfferCode().execute();
                } else {
                    Globals.ShowSnackBar(snackFocus, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        }
    }

    @Override
    public void DiscountCount(DiscountMaster objDiscountMaster) {
        Globals.objDiscountMaster = objDiscountMaster;
//        if(discountMaster!=null)
//        {
        rlPromoCode.setVisibility(View.GONE);
        discountMaster = null;
        objOfferMaster = null;
        etOfferCode.setText("");
        btnApply.setText(getResources().getString(R.string.coaApply));
//        }
        CalculateDiscount(totalAmount, totalTax);
    }

    @Override
    public void LoginResponse() {
        if (Service.CheckNet(getActivity())) {
//            new BillNumberLoadingTask().execute();
            new InsertSalesMasterLodingTask().execute();
        } else {
            Globals.ShowSnackBar(orderSummeryLayout, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }
    }

    @Override
    public void ConfirmResponse() {
        if (MenuActivity.parentActivity && Globals.userName == null) {
            GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
            guestLoginDialogFragment.setTargetFragment(this, 0);
            guestLoginDialogFragment.show(getActivity().getSupportFragmentManager(), "");
        } else {
            if (Service.CheckNet(getActivity())) {
//                new BillNumberLoadingTask().execute();
                new InsertSalesMasterLodingTask().execute();
            } else {
                Globals.ShowSnackBar(focusView, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
        }
    }

    //region Private Methods and Interface
    private void SetErrorLayout(boolean isShow, String errorMsg) {
        android.widget.TextView txtMsg = (android.widget.TextView) errorLayout.findViewById(R.id.txtMsg);
        if (isShow) {
            errorLayout.setVisibility(View.VISIBLE);
            txtMsg.setText(errorMsg);
            rvOrderItemSummery.setVisibility(View.GONE);
            headerLayout.setVisibility(View.GONE);
            amountLayout.setVisibility(View.GONE);

        } else {
            errorLayout.setVisibility(View.GONE);
            rvOrderItemSummery.setVisibility(View.VISIBLE);
            headerLayout.setVisibility(View.VISIBLE);
            amountLayout.setVisibility(View.VISIBLE);
        }
    }

    private void SetSalesList(ArrayList<OrderMaster> lstOrderMaster, ArrayList<ItemMaster> lstOrderItemTran) {
        alOrderItemTran = new ArrayList<>();
        ArrayList<ItemMaster> alOrderItemModifierTran;
        ArrayList<ItemMaster> alOrderItem;
        ItemMaster objItemMaster;
        for (int i = 0; i < lstOrderMaster.size(); i++) {
            alOrderItemModifierTran = new ArrayList<>();
            objItemMaster = new ItemMaster();
            alOrderItem = new ArrayList<>();
            for (int j = 0; j < lstOrderItemTran.size(); j++) {
                if (lstOrderMaster.get(i).getOrderMasterId() == lstOrderItemTran.get(j).getLinktoOrderMasterId()) {
                    alOrderItem.add(lstOrderItemTran.get(j));
                }
            }
            for (int k = 0; k < alOrderItem.size(); k++) {
                if (alOrderItem.get(k).getItemModifierIds().equals("0")) {
                    alOrderItemModifierTran = new ArrayList<>();
                    objItemMaster = alOrderItem.get(k);
                    if (k == alOrderItem.size() - 1) {
                        objItemMaster.setAlOrderItemModifierTran(alOrderItemModifierTran);
                        alOrderItemTran.add(objItemMaster);
                    } else if (k != alOrderItem.size() - 1) {
                        if (alOrderItem.get(k + 1).getItemModifierIds().equals("0")) {
                            objItemMaster.setAlOrderItemModifierTran(alOrderItemModifierTran);
                            alOrderItemTran.add(objItemMaster);
                        }
                    }
                } else {
                    alOrderItemModifierTran.add(alOrderItem.get(k));
                    if (k == alOrderItem.size() - 1) {
                        objItemMaster.setAlOrderItemModifierTran(alOrderItemModifierTran);
                        alOrderItemTran.add(objItemMaster);
                    } else if (k != alOrderItem.size() - 1) {
                        if (alOrderItem.get(k + 1).getItemModifierIds().equals("0")) {
                            objItemMaster.setAlOrderItemModifierTran(alOrderItemModifierTran);
                            alOrderItemTran.add(objItemMaster);
                        }
                    }
                }
            }
        }
    }

    private void GetValueFromSharePreference() {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
            counterMasterId = Short.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
        }
        if (objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()) != null) {
            userMasterId = Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "UserMasterId", getActivity()));
        }
        if (objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", getActivity()) != null) {
            waiterMasterId = Short.valueOf(objSharePreferenceManage.GetPreference("WaiterPreference", "WaiterMasterId", getActivity()));
        }
    }

    private void CalculateAmount() {
        if (lstOrderMaster.size() != 0) {
            for (int i = 0; i < lstOrderMaster.size(); i++) {
                totalAmount = totalAmount + lstOrderMaster.get(i).getTotalAmount();
//                totalTax = totalTax + lstOrderMaster.get(i).getTotalTax();
                totalDiscount = totalDiscount + lstOrderMaster.get(i).getDiscount();
            }
            totalTax = tax1 + tax2 + tax3 + tax4 + tax5;

        }
        txtTotalAmount.setText(Globals.dfWithPrecision.format(totalAmount));
        SetTextLayout();
        CalculateDiscount(totalAmount, totalTax);
    }

    @SuppressLint("SetTextI18n")
    private void CalculateDiscount(double totalAmount, double totalTax) {
        if (Globals.objDiscountMaster != null) {
            if (Globals.objDiscountMaster.getIsPercentage()) {
                totalDiscount = (totalAmount * Globals.objDiscountMaster.getDiscount()) / 100;
            } else {
                totalDiscount = Globals.objDiscountMaster.getDiscount();
            }

            if (totalDiscount <= totalAmount) {
                if (discountMaster == null) {
                    String orderMasterIds = null;
                    DiscountJSONParser discountJSONParser = new DiscountJSONParser();
                    for (int i = 0; i < lstOrderMaster.size(); i++) {
                        if (i == 0) {
                            orderMasterIds = String.valueOf(lstOrderMaster.get(i).getOrderMasterId());
                        } else {
                            orderMasterIds = orderMasterIds + "," + lstOrderMaster.get(i).getOrderMasterId();
                        }
                    }
                    Log.e("ids", " " + orderMasterIds);
                    discountJSONParser.UpdateOrderMasterDiscount(orderMasterIds, Globals.objDiscountMaster.getDiscount(), (Globals.objDiscountMaster.getIsPercentage()) ? 1 : 0);
                }
                txtTotalDiscount.setText(Globals.dfWithPrecision.format(totalDiscount));
                strNetAmount = Globals.dfWithPrecision.format((totalAmount + totalTax) - totalDiscount);
                txtNetAmount.setText(Globals.dfWithPrecision.format(Math.round((totalAmount + totalTax) - totalDiscount)));
                if (!strNetAmount.isEmpty()) {
                    txtRoundingOff.setText("- 0." + strNetAmount.substring(strNetAmount.lastIndexOf(".") + 1, strNetAmount.length()));
                }
            } else {
                txtTotalDiscount.setText(Globals.dfWithPrecision.format(0.00));
                strNetAmount = Globals.dfWithPrecision.format(totalAmount + totalTax);
                txtRoundingOff.setText("- 0." + strNetAmount.substring(strNetAmount.lastIndexOf(".") + 1, strNetAmount.length()));
                txtNetAmount.setText(Globals.dfWithPrecision.format(Math.round(totalAmount + totalTax)));
            }
            SetDiscountListOrderMaster();
        } else {
            if (totalDiscount == 0) {

                strNetAmount = Globals.dfWithPrecision.format(totalAmount + totalTax);
                txtNetAmount.setText(Globals.dfWithPrecision.format(Math.round(totalAmount + totalTax)));
                if (!strNetAmount.isEmpty()) {
                    txtRoundingOff.setText("- 0." + strNetAmount.substring(strNetAmount.lastIndexOf(".") + 1, strNetAmount.length()));
                }
                Globals.objDiscountMaster = new DiscountMaster();
                if (lstOrderMaster.get(0).getDiscountPercentage() != 0) {
                    Globals.objDiscountMaster.setDiscount(lstOrderMaster.get(0).getDiscountPercentage());
                    Globals.objDiscountMaster.setIsPercentage(true);
                } else {
                    Globals.objDiscountMaster.setDiscount(totalDiscount);
                    Globals.objDiscountMaster.setIsPercentage(false);
                }
            } else {
                if (totalDiscount <= totalAmount) {
                    txtTotalDiscount.setText(Globals.dfWithPrecision.format(totalDiscount));
                    strNetAmount = Globals.dfWithPrecision.format((totalAmount + totalTax) - totalDiscount);
                    txtNetAmount.setText(Globals.dfWithPrecision.format(Math.round((totalAmount + totalTax) - totalDiscount)));
                    if (!strNetAmount.isEmpty()) {
                        txtRoundingOff.setText("- 0." + strNetAmount.substring(strNetAmount.lastIndexOf(".") + 1, strNetAmount.length()));
                    }
                    Globals.objDiscountMaster = new DiscountMaster();
                    if (lstOrderMaster.get(0).getDiscountPercentage() != 0) {
                        Globals.objDiscountMaster.setDiscount(lstOrderMaster.get(0).getDiscountPercentage());
                        Globals.objDiscountMaster.setIsPercentage(true);
                    } else {
                        Globals.objDiscountMaster.setDiscount(totalDiscount);
                        Globals.objDiscountMaster.setIsPercentage(false);
                    }
                } else {
                    txtTotalDiscount.setText(Globals.dfWithPrecision.format(0.00));
                    strNetAmount = Globals.dfWithPrecision.format(totalAmount + totalTax);
                    txtRoundingOff.setText("- 0." + strNetAmount.substring(strNetAmount.lastIndexOf(".") + 1, strNetAmount.length()));
                    txtNetAmount.setText(Globals.dfWithPrecision.format(Math.round(totalAmount + totalTax)));
                }
            }
        }
    }

    private void SetDiscountListOrderMaster() {
        if (Globals.objDiscountMaster.getIsPercentage()) {
            for (int i = 0; i < lstOrderMaster.size(); i++) {
                lstOrderMaster.get(i).setDiscountPercentage(Globals.objDiscountMaster.getDiscount());
                lstOrderMaster.get(i).setDiscount((lstOrderMaster.get(i).getTotalAmount() * Globals.objDiscountMaster.getDiscount()) / 100);
            }
        } else {
            long maxIndex = lstOrderMaster.get(0).getOrderMasterId();
            double maxAmount = lstOrderMaster.get(0).getTotalAmount();
            for (int i = 1; i < lstOrderMaster.size(); i++) {
                if (maxAmount < lstOrderMaster.get(i).getTotalAmount()) {
                    maxAmount = lstOrderMaster.get(i).getTotalAmount();
                    maxIndex = lstOrderMaster.get(i).getOrderMasterId();
                }
            }

            for (int i = 0; i < lstOrderMaster.size(); i++) {
                if (lstOrderMaster.get(i).getOrderMasterId() == maxIndex) {
                    lstOrderMaster.get(i).setDiscountPercentage(0.00);
                    lstOrderMaster.get(i).setDiscount(Globals.objDiscountMaster.getDiscount());
                } else {
                    lstOrderMaster.get(i).setDiscountPercentage(0.00);
                    lstOrderMaster.get(i).setDiscount(0.00);
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void SetTextLayout() {
        LinearLayout[] linearLayout = new LinearLayout[alTaxMaster.size()];
        TextView[] txtTaxName = new TextView[alTaxMaster.size()];
        TextView[] txtTaxRate = new TextView[alTaxMaster.size()];

        for (int i = 0; i < alTaxMaster.size(); i++) {

            linearLayout[i] = new LinearLayout(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout[i].setOrientation(LinearLayout.HORIZONTAL);
            linearLayout[i].setLayoutParams(layoutParams);
            linearLayout[i].setPadding(8, 0, 8, 0);

            txtTaxName[i] = new TextView(getActivity());
            LinearLayout.LayoutParams txtTaxNameParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtTaxNameParams.weight = 0.5f;
            txtTaxName[i].setLayoutParams(txtTaxNameParams);
            txtTaxName[i].setGravity(Gravity.START);
            txtTaxName[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
            txtTaxName[i].setTextSize(10f);

            txtTaxRate[i] = new TextView(getActivity());
            LinearLayout.LayoutParams txtTaxRateParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtTaxRateParams.weight = 0.5f;
            txtTaxName[i].setLayoutParams(txtTaxRateParams);
            txtTaxRate[i].setGravity(Gravity.END);
            txtTaxRate[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
            txtTaxRate[i].setTextSize(10f);


            String str = String.valueOf(alTaxMaster.get(i).getTaxRate());
            String precision = str.substring(str.lastIndexOf(".") + 1);

            if (Integer.valueOf(precision) > 0) {
                txtTaxName[i].setText(alTaxMaster.get(i).getTaxName() + "  [" + " " + str + " % ]");
            } else {
                txtTaxName[i].setText(alTaxMaster.get(i).getTaxName() + "  [" + " " + str.substring(0, str.lastIndexOf(".")) + " % ]");
            }
            if (i == 0) {
                txtTaxRate[i].setText(Globals.dfWithPrecision.format(tax1));
            } else if (i == 1) {
                txtTaxRate[i].setText(Globals.dfWithPrecision.format(tax2));
            } else if (i == 2) {
                txtTaxRate[i].setText(Globals.dfWithPrecision.format(tax3));
            } else if (i == 3) {
                txtTaxRate[i].setText(Globals.dfWithPrecision.format(tax4));
            } else if (i == 4) {
                txtTaxRate[i].setText(Globals.dfWithPrecision.format(tax5));
            }

            if (WaiterHomeActivity.isWaiterMode) {
                txtTaxName[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.waitingTitleIconColor));
                txtTaxRate[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.waitingTitleIconColor));
            }

            linearLayout[i].addView(txtTaxName[i]);
            linearLayout[i].addView(txtTaxRate[i]);
            taxLayout.addView(linearLayout[i]);
        }

    }

    private void SetOffer(OfferMaster objOfferMaster) {

        if (objOfferMaster == null) {
//            etOfferCode.setText("");
            Globals.ShowSnackBar(snackFocus, getResources().getString(R.string.MsgOfferCodeFailed), getActivity(), 2000);
        } else {
            if (objOfferMaster.getOfferCode() == null) {
//                etOfferCode.setText("");
                Globals.ShowSnackBar(snackFocus, getResources().getString(R.string.MsgOfferCodeFailed), getActivity(), 2000);
            } else {
                Globals.ShowSnackBar(snackFocus, getResources().getString(R.string.MsgOfferCodeSuccess), getActivity(), 2000);
//                discountMaster= Globals.objDiscountMaster;
                discountMaster = new DiscountMaster();
                discountMaster.setIsPercentage(Globals.objDiscountMaster.getIsPercentage());
                discountMaster.setDiscount(Globals.objDiscountMaster.getDiscount());
                Globals.objDiscountMaster.setDiscount(objOfferMaster.getDiscount());
                Globals.objDiscountMaster.setIsPercentage(objOfferMaster.getIsDiscountPercentage());
//                btnApply.setText(getResources().getString(R.string.coaCancel));

                CalculateDiscount(totalAmount, totalTax);
            }
        }
    }

    //endregion

    //region LoadingTask
    class AllOrdersLoadingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

        }

        @Override
        protected Object doInBackground(Object[] params) {
            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            return objOrderJOSNParser.SelectAllOrderMaster(counterMasterId, 0, String.valueOf(objTableMaster.getTableMasterId()), null, Globals.businessMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            lstOrderMaster = (ArrayList<OrderMaster>) result;

            if (lstOrderMaster == null) {
                cbOrderPlace.setVisibility(View.GONE);
                SetErrorLayout(true, getActivity().getResources().getString(R.string.MsgSelectFail));
            } else if (lstOrderMaster.size() == 0) {
                if (Globals.isWishListShow == 0) {
                    cbOrderPlace.setVisibility(View.VISIBLE);
                }
                SetErrorLayout(true, String.format(getActivity().getResources().getString(R.string.MsgNoRecordFound), getActivity().getResources().getString(R.string.MsgOrderSummary)));
            } else {
                cbOrderPlace.setVisibility(View.GONE);
                new TaxLoadingTask().execute();
                new OrderSummeryLoadingTask().execute();
            }
        }
    }

    class OrderSummeryLoadingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            ItemJSONParser objItemJSONParser = new ItemJSONParser();
            return objItemJSONParser.SelectAllOrderItemByTableMasterIds(String.valueOf(objTableMaster.getTableMasterId()));
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            ArrayList<ItemMaster> lstOrderItemTran = (ArrayList<ItemMaster>) result;

            if (lstOrderItemTran == null) {
                cbOrderPlace.setVisibility(View.GONE);
                SetErrorLayout(true, getActivity().getResources().getString(R.string.MsgSelectFail));
            } else if (lstOrderItemTran.size() == 0) {
                if (Globals.isWishListShow == 0) {
                    cbOrderPlace.setVisibility(View.VISIBLE);
                }
                SetErrorLayout(true, String.format(getActivity().getResources().getString(R.string.MsgNoRecordFound), getActivity().getResources().getString(R.string.MsgOrderSummary)));
            } else {
                cbOrderPlace.setVisibility(View.GONE);
                SetErrorLayout(false, null);
                orderSummeryAdapter = new OrderSummaryAdapter(getActivity(), lstOrderItemTran, lstOrderMaster, false);
                rvOrderItemSummery.setAdapter(orderSummeryAdapter);
                rvOrderItemSummery.setLayoutManager(new LinearLayoutManager(getActivity()));
                SetSalesList(lstOrderMaster, lstOrderItemTran);
                for (int i = 0; i < lstOrderItemTran.size(); i++) {
                    if (lstOrderItemTran.get(i).getItemModifierIds().equals("0")) {
                        tax1 = tax1 + lstOrderItemTran.get(i).getTax1();
                        tax2 = tax2 + lstOrderItemTran.get(i).getTax2();
                        tax3 = tax3 + lstOrderItemTran.get(i).getTax3();
                        tax4 = tax4 + lstOrderItemTran.get(i).getTax4();
                        tax5 = tax5 + lstOrderItemTran.get(i).getTax5();
                    }
                }
                CalculateAmount();
                rvOrderItemSummery.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (!orderSummeryAdapter.isItemAnimate) {
                            orderSummeryAdapter.isItemAnimate = true;
                        }
                    }
                });

            }

        }
    }

    class BillNumberLoadingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            SalesJSONParser objSalesJSONParser = new SalesJSONParser();
            billNumber = objSalesJSONParser.SelectBillNumber();
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            new InsertSalesMasterLodingTask().execute();
        }
    }

    class InsertSalesMasterLodingTask extends AsyncTask {

        SalesMaster objSalesMaster;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objSalesMaster = new SalesMaster();
            objSalesMaster.setBillNumber(billNumber);
            objSalesMaster.setlinktoCounterMasterId(counterMasterId);
            objSalesMaster.setlinktoTableMasterIds(String.valueOf(objTableMaster.getTableMasterId()));
            objSalesMaster.setlinktoWaiterMasterId(waiterMasterId);
            if (Globals.userName != null) {
                objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("RegistrationPreference", "CustomerMasterId", getActivity()) != null) {
                    int customerMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("RegistrationPreference", "CustomerMasterId", getActivity()));
                    objSalesMaster.setlinktoCustomerMasterId((short) customerMasterId);
                }
            }
            objSalesMaster.setlinktoOrderTypeMasterId(objTableMaster.getlinktoOrderTypeMasterId());
            objSalesMaster.setTotalAmount(totalAmount);
            objSalesMaster.setTotalTax(totalTax);
            objSalesMaster.setDiscountAmount(totalDiscount);
            objSalesMaster.setDiscountPercentage(0.00);
            objSalesMaster.setExtraAmount(0.00);
            objSalesMaster.setTotalItemDiscount(0.00);
            objSalesMaster.setTotalItemTax(0.00);
            objSalesMaster.setNetAmount(Double.valueOf(txtNetAmount.getText().toString()));
            objSalesMaster.setPaidAmount(0.00);
            objSalesMaster.setBalanceAmount(Double.valueOf(txtNetAmount.getText().toString()));
            objSalesMaster.setRemark("");
            objSalesMaster.setRounding(Double.valueOf(txtRoundingOff.getText().toString().substring(1, txtRoundingOff.getText().length())));
            objSalesMaster.setIscomplimentary(false);
            objSalesMaster.setTotalItemPoint((short) 0);
            objSalesMaster.setTotalDeductedPoint((short) 0);
            objSalesMaster.setlinktoBusinessMasterId(Globals.businessMasterId);
            objSalesMaster.setlinktoUserMasterIdCreatedBy(userMasterId);
            objSalesMaster.setRateIndex(lstOrderMaster.get(0).getRateIndex());
            if (objOfferMaster != null) {
                objSalesMaster.setLinktoOfferMasterId((short) objOfferMaster.getOfferMasterId());
                objSalesMaster.setOfferCode(objOfferMaster.getOfferCode());
                objSalesMaster.setLinktoSourceMasterId((short) Globals.sourceMasterId);
            }
            Log.e("rate", " " + lstOrderMaster.get(0).getRateIndex());
        }

        @Override
        protected Object doInBackground(Object[] params) {
            SalesJSONParser objSalesJSONParser = new SalesJSONParser();
            status = objSalesJSONParser.InsertSalesMaster(objSalesMaster, alOrderItemTran, alTaxMaster, lstOrderMaster);
            return status;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (status.equals("-1")) {
                progressDialog.dismiss();
                Globals.ShowSnackBar(orderSummeryLayout, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
            } else if (status.equals("0")) {
                new TableStatusLoadingTask().execute();
            }
        }
    }

    class TableStatusLoadingTask extends AsyncTask {

        TableMaster objTable;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objTable = new TableMaster();
            objTable.setTableMasterId(objTableMaster.getTableMasterId());
            if (Globals.orderTypeMasterId == Globals.OrderType.DineIn.getValue()) {
                objTable.setlinktoTableStatusMasterId((short) Globals.TableStatus.Dirty.getValue());
            } else {
                objTable.setlinktoTableStatusMasterId((short) Globals.TableStatus.Vacant.getValue());
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TableJSONParser objTableJSONParser = new TableJSONParser();
            status = objTableJSONParser.UpdateTableStatus(objTable);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            if (status.equals("0")) {
                if (MenuActivity.parentActivity || GuestHomeActivity.isGuestMode) {
                    Globals.counter = 0;
                    Globals.alOrderItemTran.clear();
                    Globals.alOrderItemSummery = new ArrayList<>();
                    Globals.alOrderMasterId = new ArrayList<>();
                    objSharePreferenceManage.RemovePreference("WishListPreference", "WishList", getActivity());
                    objSharePreferenceManage.ClearPreference("WishListPreference", getActivity());
                    objSharePreferenceManage.RemovePreference("CartItemListPreference", "CartItemList", getActivity());
                    objSharePreferenceManage.ClearPreference("CartItemListPreference", getActivity());
                    getActivity().getSupportFragmentManager().popBackStack();
                    Globals.ReplaceFragment(new ThankYouFragment(getActivity().getResources().getString(R.string.thankYouMsg), true), getActivity().getSupportFragmentManager(), null);
                } else {
                    Globals.counter = 0;
                    Globals.alOrderItemTran.clear();
                    Globals.selectTableMasterId = 0;
                    Globals.alOrderItemSummery = new ArrayList<>();
                    Globals.alOrderMasterId = new ArrayList<>();
                    objSharePreferenceManage.RemovePreference("CartItemListPreference", "CartItemList", getActivity());
                    objSharePreferenceManage.ClearPreference("CartItemListPreference", getActivity());
                    Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("IsCheckOutMessage", true);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        }
    }

    class TaxLoadingTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TaxJSONParser objTaxJSONParser = new TaxJSONParser();
            return objTaxJSONParser.SelectAllTaxMaster(Globals.businessMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            alTaxMaster = (ArrayList<TaxMaster>) result;
        }
    }

    class RequestVerifyOfferCode extends AsyncTask {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
            objOfferMaster = new OfferMaster();
            objOfferMaster.setlinktoBusinessMasterId(Globals.businessMasterId);
            if (objSharePreferenceManage.GetPreference("RegistrationPreference", "UserName", getActivity()) != null) {
                objOfferMaster.setLinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("RegistrationPreference", "CustomerMasterId", getActivity())));
            }
            objOfferMaster.setOfferCode(etOfferCode.getText().toString().trim());
            objOfferMaster.setlinktoOrderTypeMasterId(Globals.orderTypeMasterId);
            objOfferMaster.setMinimumBillAmount(totalAmount);

        }

        @Override
        protected Object doInBackground(Object[] params) {
            OfferJSONParser offerJSONParser = new OfferJSONParser();
            objOfferMaster = offerJSONParser.SelectOfferCodeVerification(objOfferMaster);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            SetOffer(objOfferMaster);
//            if (tbTakeAway.isChecked()) {
//                SaveCheckOutData(null, objOfferMaster, Globals.OrderType.TakeAway.getValue());
//            } else if (tbHomeDelivery.isChecked()) {
//                SaveCheckOutData(null, objOfferMaster, Globals.OrderType.HomeDelivery.getValue());
//            }
        }
    }
    //endregion
}
