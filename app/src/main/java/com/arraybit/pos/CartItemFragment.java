package com.arraybit.pos;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.CartItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TableMaster;
import com.arraybit.modal.TaxMaster;
import com.arraybit.parser.OrderJOSNParser;
import com.arraybit.parser.TableJSONParser;
import com.arraybit.parser.TaxJSONParser;
import com.google.gson.Gson;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SuppressWarnings({"ConstantConditions", "unchecked"})
public class CartItemFragment extends Fragment implements CartItemAdapter.CartItemOnClickListener, View.OnClickListener, RemarkDialogFragment.RemarkResponseListener, AddItemQtyDialogFragment.QtyRemarkDialogResponseListener, ConfirmDialog.ConfirmationResponseListener {

    TextView txtMsg, txtEditMessage;
    CompoundButton cbMenu;
    LinearLayout headerLayout, cartItemFragment;
    RecyclerView rvCartItem;
    CardView cvRemark;
    Button btnAddMore, btnConfirmOrder;
    CartItemAdapter adapter;
    OrderMaster objOrderMaster;
    String orderNumber, orderMasterId;
    SharePreferenceManage objSharePreferenceManage;
    short counterMasterId, userMasterId, waiterMasterId;
    double totalAmount, totalTax, netAmount;
    View view;
    TextView txtRemark;
    ArrayList<TaxMaster> alTaxMaster;
    int position;
    Context context;
    boolean isBackPressed = false, isHome;

    public CartItemFragment() {
        // Required empty public constructor
    }

    public CartItemFragment(Context context) {
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_item, container, false);

//        if (getArguments() != null) {
//            isHome = getArguments().getBoolean("isHome", false);
//        } else {
//            isHome = false;
//        }

       isHome = getArguments() != null && getArguments().getBoolean("isHome", false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            if (MenuActivity.objTableMaster != null && MenuActivity.objTableMaster.getTableName() != null) {
//                app_bar.setTitle(MenuActivity.objTableMaster.getTableName() + "  Orders");
//            } else {
//                app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_cart_item));
//            }
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        if (MenuActivity.objTableMaster != null && MenuActivity.objTableMaster.getTableName() != null) {
            app_bar.setTitle(MenuActivity.objTableMaster.getTableName() + "  Orders");
        } else {
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_cart_item));
        }
        //end

        txtRemark = (TextView) view.findViewById(R.id.txtRemark);

        cvRemark = (CardView) view.findViewById(R.id.cvRemark);

        cartItemFragment = (LinearLayout) view.findViewById(R.id.cartItemFragment);

        setHasOptionsMenu(true);

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        txtEditMessage = (TextView) view.findViewById(R.id.txtEditMessage);

        cbMenu = (CompoundButton) view.findViewById(R.id.cbMenu);

        headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);

        rvCartItem = (RecyclerView) view.findViewById(R.id.rvCartItem);

        btnAddMore = (Button) view.findViewById(R.id.btnAddMore);
        btnConfirmOrder = (Button) view.findViewById(R.id.btnConfirmOrder);
        Button btnRemark = (Button) view.findViewById(R.id.btnRemark);
        TextView txtHeaderItem = (TextView) view.findViewById(R.id.txtHeaderItem);
        TextView txtHeaderNo = (TextView) view.findViewById(R.id.txtHeaderNo);
        TextView txtHeaderRate = (TextView) view.findViewById(R.id.txtHeaderRate);
        TextView txtHeaderAmount = (TextView) view.findViewById(R.id.txtHeaderAmount);

        if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {
            getActivity().setTheme(R.style.AppThemeGuest);

            Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary), ContextCompat.getColor(getActivity(), android.R.color.white));
            Globals.SetScaleImageBackground(getActivity(), cartItemFragment, null, null);
//            if (Globals.objAppThemeMaster != null) {
//                Globals.SetToolBarBackground(getActivity(), app_bar, Globals.objAppThemeMaster.getColorPrimary(), ContextCompat.getColor(getActivity(), android.R.color.white));
//                objSharePreferenceManage = new SharePreferenceManage();
//                if (objSharePreferenceManage.GetPreference("GuestAppTheme", getActivity().getString(R.string.guestEncodedImage1), getActivity()) != null &&
//                        !objSharePreferenceManage.GetPreference("GuestAppTheme", getActivity().getString(R.string.guestEncodedImage1), getActivity()).equals("")) {
//                    String encodedImage = objSharePreferenceManage.GetPreference("GuestAppTheme", getActivity().getString(R.string.guestEncodedImage1), getActivity());
//                    Globals.SetPageBackground(getActivity(), encodedImage, cartItemFragment, null, null, null);
//                } else {
//                    Bitmap originalBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.splash_screen_background);
//                    cartItemFragment.setBackground(new BitmapDrawable(getActivity().getResources(), originalBitmap));
//                }
//                headerLayout.setBackground(new ColorDrawable(Globals.objAppThemeMaster.getColorPrimaryLight()));
//                txtHeaderItem.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorTextPrimary()));
//                txtHeaderNo.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorTextPrimary()));
//                txtHeaderRate.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorTextPrimary()));
//                txtHeaderAmount.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorTextPrimary()));
//
//                ColorStateList colorButtonStateList = new ColorStateList(
//                        new int[][]
//                                {
//                                        new int[]{android.R.attr.state_pressed},
//                                        new int[]{}
//                                },
//                        new int[]
//                                {
//                                        Globals.objAppThemeMaster.getColorButtonRipple(),
//                                        Globals.objAppThemeMaster.getColorPrimary()
//                                }
//                );
////                Globals.CustomView(btnAddMore, android.R.color.transparent, Globals.objAppThemeMaster.getColorPrimaryLight());
//                Globals.CustomView(btnAddMore, android.R.color.transparent, ContextCompat.getColor(getActivity(), android.R.color.black));
//                btnAddMore.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), android.R.color.white)));
////                btnAddMore.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorAccent()));
//                Globals.CustomView(btnConfirmOrder, Globals.objAppThemeMaster.getColorPrimaryLight(), android.R.color.transparent);
//                btnConfirmOrder.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorTextPrimary()));
////                Globals.ButtonTint(btnAddMore, Globals.objAppThemeMaster.getColorButtonRipple(), android.R.color.transparent);
////                Globals.ButtonTint(btnConfirmOrder, Globals.objAppThemeMaster.getColorButtonRipple(), Globals.objAppThemeMaster.getColorPrimary());
//              }else{

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                RippleDrawable rippleDrawable = (RippleDrawable) btnAddMore.getBackground(); // assumes bg is a RippleDrawable
//
//                int[][] states = new int[][]{new int[]{android.R.attr.state_enabled}};
//                int[] colors = new int[]{R.color.holo_blue_dark}; // sets the ripple color to blue
//
//                ColorStateList colorStateList = new ColorStateList(states, colors);
//                rippleDrawable.setTintList(colorStateList);
//            }
//
//            } else {
//                Bitmap originalBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.splash_screen_background);
//                cartItemFragment.setBackground(new BitmapDrawable(getActivity().getResources(), originalBitmap));

            txtMsg.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), android.R.color.white)));
            cbMenu.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.accent_secondary)));

            headerLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.accent_secondary));

            txtHeaderItem.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));
            txtHeaderNo.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));
            txtHeaderRate.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));
            txtHeaderAmount.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));

            Globals.CustomView(btnAddMore, ContextCompat.getColor(getActivity(), android.R.color.transparent), ContextCompat.getColor(getActivity(), R.color.accent_secondary));
            btnAddMore.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.accent_secondary)));
            Globals.CustomView(btnConfirmOrder, ContextCompat.getColor(getActivity(), R.color.accent_secondary), ContextCompat.getColor(getActivity(), android.R.color.transparent));
            btnConfirmOrder.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));

            btnRemark.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), android.R.color.white)));
            cvRemark.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent_orange));
            Drawable drawable[] = btnRemark.getCompoundDrawablesRelative();
            drawable[2].mutate().setColorFilter(ContextCompat.getColor(getActivity(), R.color.accent), PorterDuff.Mode.SRC_IN);
//            DrawableCompat.setTint(drawable[2].mutate(), ContextCompat.getColor(getActivity(), R.color.accent));
            btnRemark.setCompoundDrawablesRelative(drawable[0], drawable[1], drawable[2], drawable[3]);
            txtRemark.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.grey)));

            txtEditMessage.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.text_info)));
            LayerDrawable shape = (LayerDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.dash_line_separator);
            GradientDrawable gradientDrawable = (GradientDrawable) shape.findDrawableByLayerId(R.id.dash_separator);
            gradientDrawable.setStroke(2, ContextCompat.getColor(getActivity(), R.color.text_info), 6, 6);
//            txtEditMessage.setBackground(shape);

            txtEditMessage.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.dash_line_separator));
            rvCartItem.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.dash_line_separator));

//            }
//            }
        } else {
            Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary_black), ContextCompat.getColor(getActivity(), android.R.color.white));
            cartItemFragment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_img));
            Globals.CustomView(btnConfirmOrder, ContextCompat.getColor(getActivity(), R.color.accent_red), ContextCompat.getColor(getActivity(), android.R.color.transparent));
            btnConfirmOrder.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), android.R.color.white)));
        }


        SetRecyclerView();
//        SaveCartDataInSharePreference(isBackPressed);

        cbMenu.setOnClickListener(this);
        btnAddMore.setOnClickListener(this);
        btnConfirmOrder.setOnClickListener(this);
        btnRemark.setOnClickListener(this);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            isBackPressed = true;
            SaveCartDataInSharePreference(isBackPressed);
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {

                if (isHome) {
                    Intent returnIntent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, R.anim.right_exit);
                } else {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                RemarkDialogFragment.strRemark = null;
            } else {
                RemarkDialogFragment.strRemark = null;
                getFragmentManager().popBackStack();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (!isHome) {
            menu.findItem(R.id.viewChange).setVisible(false);
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.cart_layout).setVisible(false);
//        SaveCartDataInSharePreference(isBackPressed);
            if (Globals.isWishListShow == 0) {
                menu.findItem(R.id.logout).setVisible(false);
//            menu.findItem(R.id.notification_layout).setVisible(false);
            } else if (Globals.isWishListShow == 1) {
                menu.findItem(R.id.login).setVisible(false);
                menu.findItem(R.id.logout).setVisible(false);
                menu.findItem(R.id.shortList).setVisible(false);
                menu.findItem(R.id.callWaiter).setVisible(false);
            }
        }
    }

    @Override
    public void ImageViewOnClick(int position, boolean isBackPressed) {
        adapter.RemoveData(position);
        if (Globals.alOrderItemTran.size() == 0) {
            SetRecyclerView();
        }
//        SaveCartDataInSharePreference(isBackPressed);
//        if (getActivity() instanceof GuestHomeActivity) {
//            AddMoreOnClickListener addMoreOnClickListener = (AddMoreOnClickListener) context;
//            addMoreOnClickListener.SetCartNumber();
//            if (Globals.alOrderItemTran.size() == 0) {
//                SetRecyclerView();
//            }
//        } else {
//            if (Globals.alOrderItemTran.size() == 0) {
//                SetRecyclerView();
//            }
//        }
    }

    @Override
    public void EditCartItem(ItemMaster objItemMaster, int position) {
        this.position = position;
        AddItemQtyDialogFragment addItemQtyDialogFragment = new AddItemQtyDialogFragment(objItemMaster);
        Bundle bundle = new Bundle();
        bundle.putBoolean("IsEdit", true);
        addItemQtyDialogFragment.setArguments(bundle);
        addItemQtyDialogFragment.setTargetFragment(this, 0);
        addItemQtyDialogFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddMore) {

            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {

                if (isHome) {
                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    intent.putExtra("ParentActivity", true);
                    intent.putExtra("IsFavoriteShow", false);
                    intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
                    getActivity().startActivityForResult(intent, 100);
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        } else if (v.getId() == R.id.btnConfirmOrder) {
            view = v;
            ConfirmDialog confirmDialog = new ConfirmDialog(getActivity().getResources().getString(R.string.cdfConfirmMsg), false);
            confirmDialog.setTargetFragment(this, 0);
            confirmDialog.show(getActivity().getSupportFragmentManager(), "");
        } else if (v.getId() == R.id.cbMenu) {
            isBackPressed = true;
            RemarkDialogFragment.strRemark = null;
            SaveCartDataInSharePreference(isBackPressed);
            if (MenuActivity.parentActivity) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {
                    if (isHome) {
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        intent.putExtra("ParentActivity", true);
                        intent.putExtra("IsFavoriteShow", false);
                        intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
                        getActivity().startActivityForResult(intent, 100);
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                        SaveCartDataInSharePreference(isBackPressed);
                    } else {
                        getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                        SaveCartDataInSharePreference(isBackPressed);
                    }
                }
            } else {
                if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_cart_item))) {
                    getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    SaveCartDataInSharePreference(isBackPressed);
                }
            }
        } else if (v.getId() == R.id.btnRemark) {
            RemarkDialogFragment remarkDialogFragment = new RemarkDialogFragment();
            remarkDialogFragment.setTargetFragment(this, 0);
            remarkDialogFragment.show(getActivity().getSupportFragmentManager(), "");
        }
    }

    @Override
    public void RemarkResponse() {
        if (RemarkDialogFragment.strRemark != null) {
            txtRemark.setVisibility(View.VISIBLE);
            txtRemark.setText(RemarkDialogFragment.strRemark);
        } else {
            txtRemark.setVisibility(View.GONE);
            txtRemark.setText("");
        }
    }

    @Override
    public void UpdateQtyRemarkResponse(ItemMaster objOrderItem) {
        adapter.UpdateData(position, objOrderItem);
    }

    @Override
    public void ConfirmResponse() {
//        if (MenuActivity.parentActivity) {
        GetValueFromSharePreference();
        if (Service.CheckNet(getActivity())) {
            new TaxLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

//        } else {
//            GetValueFromSharePreference();
//            if (Service.CheckNet(getActivity())) {
//                new TaxLoadingTask().execute();
//            } else {
//                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
//            }
//        }
    }

    //region Private Methods and Interface
    private void SetRecyclerView() {
        if (Globals.alOrderItemTran.size() == 0) {
            SetVisibility();
            txtMsg.setText(getActivity().getResources().getString(R.string.MsgCart));
            isBackPressed = true;
            SaveCartDataInSharePreference(isBackPressed);
        } else {
            SetVisibility();
            adapter = new CartItemAdapter(getActivity(), Globals.alOrderItemTran, this, false);
            rvCartItem.setAdapter(adapter);
            rvCartItem.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvCartItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Globals.HideKeyBoard(getActivity(), recyclerView);
                    if (!adapter.isItemAnimate) {
                        adapter.isItemAnimate = true;
                        adapter.isModifierChanged = false;
                    }
                }
            });
        }
    }

    private void SetVisibility() {
        if (Globals.alOrderItemTran.size() == 0) {
            txtMsg.setVisibility(View.VISIBLE);
            cbMenu.setVisibility(View.VISIBLE);
            MenuActivity.parentActivity = true;
            cvRemark.setVisibility(View.GONE);
            rvCartItem.setVisibility(View.GONE);
            headerLayout.setVisibility(View.GONE);
            btnAddMore.setVisibility(View.GONE);
            btnConfirmOrder.setVisibility(View.GONE);
            txtEditMessage.setVisibility(View.GONE);
        } else {
            txtMsg.setVisibility(View.GONE);
            cbMenu.setVisibility(View.GONE);
            cvRemark.setVisibility(View.VISIBLE);
            headerLayout.setVisibility(View.VISIBLE);
            rvCartItem.setVisibility(View.VISIBLE);
            btnAddMore.setVisibility(View.VISIBLE);
            btnConfirmOrder.setVisibility(View.VISIBLE);
            txtEditMessage.setVisibility(View.VISIBLE);
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

    private void SaveCartDataInSharePreference(boolean isBackPressed) {
        Gson gson = new Gson();
        SharePreferenceManage objSharePreferenceManage;
        List<ItemMaster> lstItemMaster;
        try {
            if (Globals.alOrderItemTran.size() == 0) {
                if (isBackPressed) {
                    objSharePreferenceManage = new SharePreferenceManage();
                    objSharePreferenceManage.CreatePreference("CartItemListPreference", "CartItemList", null, getActivity());
                    Globals.counter = 0;
                } else {
                    objSharePreferenceManage = new SharePreferenceManage();
                    String string = objSharePreferenceManage.GetPreference("CartItemListPreference", "CartItemList", getActivity());
                    if (string != null) {
                        ItemMaster[] objItemMaster = gson.fromJson(string,
                                ItemMaster[].class);

                        lstItemMaster = Arrays.asList(objItemMaster);
                        Globals.alOrderItemTran.addAll(new ArrayList<>(lstItemMaster));
                        Globals.counter = Globals.alOrderItemTran.size();
                        if (objSharePreferenceManage.GetPreference("CartItemListPreference", "OrderRemark", getActivity()) != null) {
                            RemarkDialogFragment.strRemark = objSharePreferenceManage.GetPreference("CartItemListPreference", "OrderRemark", getActivity());
                        }
                    } else {
                        objSharePreferenceManage.RemovePreference("CheckOutDataPreference", "CheckOutData", getActivity());
                        objSharePreferenceManage.ClearPreference("CheckOutDataPreference", getActivity());
                    }
                }
            } else {
                objSharePreferenceManage = new SharePreferenceManage();
                String string = gson.toJson(Globals.alOrderItemTran);
                objSharePreferenceManage.CreatePreference("CartItemListPreference", "CartItemList", string, getActivity());
                objSharePreferenceManage.CreatePreference("CartItemListPreference", "OrderRemark", RemarkDialogFragment.strRemark, getActivity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion

    //region LoadingTask
    class OrderLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

            if (Globals.alOrderItemTran.size() != 0) {
                for (int i = 0; i < Globals.alOrderItemTran.size(); i++) {
                    if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() != 0) {
                        totalAmount = totalAmount + Globals.alOrderItemTran.get(i).getTotalAmount();
                        totalTax = totalTax + Globals.alOrderItemTran.get(i).getTotalTax();
                        netAmount = totalAmount + totalTax;
                    } else {
                        totalAmount = totalAmount + Globals.alOrderItemTran.get(i).getSellPrice();
                        totalTax = totalTax + Globals.alOrderItemTran.get(i).getTotalTax();
                        netAmount = totalAmount + totalTax;
                    }
                }
            }

            objOrderMaster = new OrderMaster();
            objOrderMaster.setOrderNumber(orderNumber);
            objOrderMaster.setlinktoCounterMasterId(counterMasterId);
            objOrderMaster.setlinktoTableMasterIds(String.valueOf(MenuActivity.objTableMaster.getTableMasterId()));
            objOrderMaster.setlinktoOrderTypeMasterId(MenuActivity.objTableMaster.getlinktoOrderTypeMasterId());
            objOrderMaster.setTotalAmount(totalAmount);
            objOrderMaster.setTotalTax(totalTax);
            objOrderMaster.setDiscount(0.00);
            objOrderMaster.setExtraAmount(0.00);
            objOrderMaster.setNetAmount(netAmount);
            objOrderMaster.setTotalItemPoint((short) 0);
            objOrderMaster.setTotalDeductedPoint((short) 0);
            objOrderMaster.setlinktoWaiterMasterId(waiterMasterId);
            objOrderMaster.setlinktoUserMasterIdCreatedBy(userMasterId);
            objOrderMaster.setRateIndex(Globals.alOrderItemTran.get(0).getRateIndex());
            if (Globals.userName != null) {
                objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("RegistrationPreference", "CustomerMasterId", getActivity()) != null) {
                    int customerMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("RegistrationPreference", "CustomerMasterId", getActivity()));
                    objOrderMaster.setlinktoCustomerMasterId((short) customerMasterId);
                }
            }
            objOrderMaster.setLinktoBusinessMasterId(Globals.businessMasterId);
            if (RemarkDialogFragment.strRemark != null) {
                objOrderMaster.setRemark(RemarkDialogFragment.strRemark);
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            orderMasterId = objOrderJOSNParser.InsertOrderMaster(objOrderMaster, Globals.alOrderItemTran, alTaxMaster);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            progressDialog.dismiss();
            if (orderMasterId.equals("-1")) {
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
            } else if (!orderMasterId.equals("0")) {
                if (!AllTablesFragment.isRefresh) {
                    new TableStatusLoadingTask().execute();
                }
                RemarkDialogFragment.strRemark = null;
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgConfirmOrder), getActivity(), 1000);
                if (MenuActivity.parentActivity || GuestHomeActivity.isGuestMode) {
//                    Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
//                    intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
//                    intent.putExtra("ShowMessage",true);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    Globals.counter = 0;
                    Globals.alOrderItemTran.clear();
                    objSharePreferenceManage = new SharePreferenceManage();
                    objSharePreferenceManage.RemovePreference("CartItemListPreference", "CartItemList", getActivity());
                    objSharePreferenceManage.RemovePreference("CartItemListPreference", "OrderRemark", getActivity());
                    objSharePreferenceManage.ClearPreference("CartItemListPreference", getActivity());
                    if (getActivity() instanceof MenuActivity) {
                        getActivity().finish();
                    } else if (getActivity() instanceof GuestHomeActivity) {
                        getActivity().onBackPressed();
                    } else {
                        getActivity().finish();
                        Globals.counter = 0;
                        Globals.alOrderItemTran.clear();
                    }
                } else {
                    Globals.counter = 0;
                    Globals.alOrderItemTran.clear();
                    if (Globals.orderTypeMasterId == Globals.OrderType.DineIn.getValue()) {
                        Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("TableName", MenuActivity.objTableMaster.getTableName());
                        intent.putExtra("ShowMessage", true);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    } else {
                        Globals.ReplaceFragment(new OrderSummaryFragment(), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_order_summary));
                    }
                }
            }
        }
    }

    class OrderNumberLoadingTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OrderJOSNParser objOrderJOSNParser = new OrderJOSNParser();
            orderNumber = objOrderJOSNParser.SelectOrderNumber(Globals.businessMasterId);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (orderNumber != null) {
                new OrderLoadingTask().execute();
            }
        }
    }

    class TableStatusLoadingTask extends AsyncTask {

        TableMaster objTableMaster;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objTableMaster = new TableMaster();
            objTableMaster.setTableMasterId(MenuActivity.objTableMaster.getTableMasterId());
            objTableMaster.setlinktoTableStatusMasterId((short) Globals.TableStatus.Occupied.getValue());
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TableJSONParser objTableJSONParser = new TableJSONParser();
            status = objTableJSONParser.UpdateTableStatus(objTableMaster);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            AllTablesFragment.isRefresh = true;
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
            new OrderNumberLoadingTask().execute();
        }
    }

    //endregion
}
