package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.adapter.CartItemAdapter;
import com.arraybit.adapter.CategoryItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.CategoryJSONParser;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemCartListFragment extends Fragment implements View.OnClickListener, ItemTabFragment.CartIconListener, DetailFragment.ResponseListener, GuestLoginDialogFragment.LoginResponseListener, CartItemAdapter.CartItemOnClickListener, ItemTabFragment.AddItemToCart {

    public static boolean isViewChange = true;
    public static short i = 2;
    public static CategoryMaster objCategoryMaster = null;
    public static String itemName;
    public static StringBuilder sbItemTypeMasterId = new StringBuilder();
    ViewPager itemViewPager;
    TabLayout itemTabLayout;
    ItemPagerAdapter itemPagerAdapter;
    FloatingActionMenu famRoot;
    FloatingActionButton fabVeg, fabNonVeg, fabJain;
    boolean isForceToChange = false, isUpdate = false, isBackPressed = false;
    short isVegCheck = 0, isNonVegCheck = 0, isJainCheck = 0;
    CoordinatorLayout categoryItemCartFragment;
    LinearLayout errorLayout, headerLayout;
    boolean isFavoriteShow;
    ItemMaster objUpdateItemMaster;
    ItemTabFragment itemTabFragment;
    TextView txtMsg;
    ImageView ivErrorIcon;
    RecyclerView rvCartItem;
    CartItemAdapter adapter;
    int position;
    SharePreferenceManage sharePreferenceManage;

    public ItemCartListFragment(boolean isFavoriteShow) {
        if (!GuestHomeActivity.isMenuMode) {
            this.isFavoriteShow = isFavoriteShow;
            Globals.isWishListShow = (short) (isFavoriteShow ? 0 : 1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_cart_list, container, false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_category_item));
            if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {
                if (Globals.objAppThemeMaster != null) {
                    Globals.SetToolBarBackground(getActivity(), app_bar, Globals.objAppThemeMaster.getColorPrimary(), ContextCompat.getColor(getActivity(), android.R.color.white));
                } else {
                    Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary), ContextCompat.getColor(getActivity(), android.R.color.white));
                }
            } else {
                Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary_black), ContextCompat.getColor(getActivity(), android.R.color.white));

            }
        }
        //end

        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
            categoryItemCartFragment = (CoordinatorLayout) view.findViewById(R.id.categoryItemCartFragment);
            if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {
                if (Globals.objAppThemeMaster != null) {
                    sharePreferenceManage = new SharePreferenceManage();
                    String encodedImage = sharePreferenceManage.GetPreference("GuestAppTheme", getActivity().getString(R.string.guestEncodedImage1), getActivity());
                    if (encodedImage != null && !encodedImage.equals("")) {
                        Globals.SetPageBackground(getActivity(), encodedImage, null, null, null, categoryItemCartFragment);
//                    Globals.SetScaleImageBackground(getActivity(), categoryItemFragment);
                    } else {
                        Globals.SetScaleImageBackground(getActivity(), categoryItemCartFragment);
                    }
                } else {
//                    Bitmap originalBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.splash_screen_background);
//                    categoryItemFragment.setBackground(new BitmapDrawable(getActivity().getResources(), originalBitmap));
                    Globals.SetScaleImageBackground(getActivity(), categoryItemCartFragment);
                }
            } else {
                categoryItemCartFragment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.background_img));
            }
        }
//      cart view
        headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);

        rvCartItem = (RecyclerView) view.findViewById(R.id.rvCartItem);

        TextView txtHeaderItem = (TextView) view.findViewById(R.id.txtHeaderItem);
        TextView txtHeaderNo = (TextView) view.findViewById(R.id.txtHeaderNo);
        TextView txtHeaderRate = (TextView) view.findViewById(R.id.txtHeaderRate);
        TextView txtHeaderAmount = (TextView) view.findViewById(R.id.txtHeaderAmount);
//      cart view end

//      menu view
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);
        ivErrorIcon = (ImageView) errorLayout.findViewById(R.id.ivErrorIcon);

        famRoot = (FloatingActionMenu) view.findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);


        fabVeg = (FloatingActionButton) view.findViewById(R.id.fabVeg);
        fabNonVeg = (FloatingActionButton) view.findViewById(R.id.fabNonVeg);
        fabJain = (FloatingActionButton) view.findViewById(R.id.fabJain);

        itemTabLayout = (TabLayout) view.findViewById(R.id.itemTabLayout);

//      menu view end

        itemTabLayout.setClickable(true);
        if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {
            getActivity().setTheme(R.style.AppThemeGuest);
            if (Globals.objAppThemeMaster != null) {
                headerLayout.setBackground(new ColorDrawable(Globals.objAppThemeMaster.getColorAccentDark()));
                txtHeaderItem.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorPrimary()));
                txtHeaderNo.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorPrimary()));
                txtHeaderRate.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorPrimary()));
                txtHeaderAmount.setTextColor(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorPrimary()));

                itemTabLayout.setSelectedTabIndicatorColor(Globals.objAppThemeMaster.getColorAccentDark());
                itemTabLayout.setTabTextColors(ColorStateList.valueOf(Globals.objAppThemeMaster.getColorCardText()));
                itemTabLayout.setBackgroundColor(Globals.objAppThemeMaster.getColorPrimary());
            } else {
                headerLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.accent_secondary));
                txtHeaderItem.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));
                txtHeaderNo.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));
                txtHeaderRate.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));
                txtHeaderAmount.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.primary)));

                itemTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.accent));
                itemTabLayout.setTabTextColors(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), android.R.color.white)));
                itemTabLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.primary));
            }
            ivErrorIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.errorIconColor), PorterDuff.Mode.SRC_IN);
            txtMsg.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
        }
        SetRecyclerView();
        itemViewPager = (ViewPager) view.findViewById(R.id.itemViewPager);

        fabVeg.setOnClickListener(this);
        fabNonVeg.setOnClickListener(this);
        fabJain.setOnClickListener(this);


        if (Service.CheckNet(getActivity())) {
            new GuestHomeCategoryLodingTask().execute();
        } else {
            SetErrorLayout(true, getResources().getString(R.string.MsgCheckConnection), itemTabLayout, itemViewPager, R.drawable.wifi_off);
        }

        setHasOptionsMenu(true);

        Globals.targetFragment = ItemCartListFragment.this;

        CheckSelectedFloatingButton();

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.viewChange).setVisible(true);
        if (GuestHomeActivity.isMenuMode) {
            menu.findItem(R.id.cart_layout).setVisible(false);
        } else {
            if (MenuActivity.parentActivity) {
                menu.findItem(R.id.cart_layout).setVisible(true);
            } else {
                menu.findItem(R.id.cart_layout).setVisible(true);
                menu.findItem(R.id.notification_layout).setVisible(false);
            }
        }

        if (i == 1) {
            menu.findItem(R.id.viewChange).setIcon(R.drawable.view_grid_two);
        } else if (i == 2) {
            menu.findItem(R.id.viewChange).setIcon(R.drawable.view_list);
        } else {
            menu.findItem(R.id.viewChange).setIcon(R.drawable.view_grid);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
                if (Globals.isWishListShow == 1) {
                    SaveWishListInSharePreference(true);
                }
                if (MenuActivity.parentActivity || GuestHomeActivity.isMenuMode) {
                    Globals.targetFragment = null;
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("IsMenu", true);
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, R.anim.right_exit);
                } else {
                    Globals.CategoryItemFragmentResetStaticVariable();
                    Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            } else if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
                Globals.targetFragment = null;
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_category_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else if (item.getItemId() == R.id.viewChange) {
            if (!errorLayout.isShown()) {
                ItemTabFragment itemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
                i = (short) (i + 1);
                if (i == 1) {
                    item.setIcon(R.drawable.view_grid_two);
                    isViewChange = true;
                    isForceToChange = true;
                    if (fabJain.isSelected() || fabNonVeg.isSelected() || fabVeg.isSelected()) {
                        itemTabFragment.SetupRecyclerView(true, null);
                    } else {
                        itemTabFragment.SetupRecyclerView(false, null);
                    }
                } else if (i == 2) {
                    item.setIcon(R.drawable.view_list);
                    isViewChange = true;
                    isForceToChange = true;
                    if (fabJain.isSelected() || fabNonVeg.isSelected() || fabVeg.isSelected()) {
                        itemTabFragment.SetupRecyclerView(true, null);
                    } else {
                        itemTabFragment.SetupRecyclerView(false, null);
                    }
                } else {
                    i = 0;
                    item.setIcon(R.drawable.view_grid);
                    isViewChange = false;
                    isForceToChange = true;
                    if (fabJain.isSelected() || fabNonVeg.isSelected() || fabVeg.isSelected()) {
                        itemTabFragment.SetupRecyclerView(true, null);
                    } else {
                        itemTabFragment.SetupRecyclerView(false, null);
                    }
                }
            }
        }
        if (MenuActivity.parentActivity || getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
            OptionMenuClick(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        ItemTabFragment itemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
        if (v.getId() == R.id.fabVeg) {
            famRoot.close(true);
            if (isVegCheck == 0) {
                fabVeg.setSelected(true);
                fabVeg.setColorNormal(ContextCompat.getColor(getActivity(), R.color.accent_secondary));
                isVegCheck += 1;
            } else {
                fabVeg.setSelected(false);
                fabVeg.setColorNormal(ContextCompat.getColor(getActivity(), android.R.color.white));
                isVegCheck = 0;
            }
            CheckSelected();
            if (sbItemTypeMasterId.toString().equals("")) {
                itemTabFragment.ItemDataFilter(null);
            } else {
                itemTabFragment.ItemDataFilter(sbItemTypeMasterId.toString());
            }
        } else if (v.getId() == R.id.fabNonVeg) {
            famRoot.close(true);
            if (isNonVegCheck == 0) {
                fabNonVeg.setSelected(true);
                fabNonVeg.setColorNormal(ContextCompat.getColor(getActivity(), R.color.accent_secondary));
                isNonVegCheck += 1;
            } else {
                fabNonVeg.setSelected(false);
                fabNonVeg.setColorNormal(ContextCompat.getColor(getActivity(), android.R.color.white));
                isNonVegCheck = 0;
            }
            CheckSelected();
            if (sbItemTypeMasterId.toString().equals("")) {
                itemTabFragment.ItemDataFilter(null);
            } else {
                itemTabFragment.ItemDataFilter(sbItemTypeMasterId.toString());
            }

        } else if (v.getId() == R.id.fabJain) {
            famRoot.close(true);
            if (isJainCheck == 0) {
                fabJain.setSelected(true);
                fabJain.setColorNormal(ContextCompat.getColor(getActivity(), R.color.accent_secondary));
                isJainCheck += 1;
            } else {
                fabJain.setSelected(false);
                fabJain.setColorNormal(ContextCompat.getColor(getActivity(), android.R.color.white));
                isJainCheck = 0;
            }
            CheckSelected();
            if (sbItemTypeMasterId.toString().equals("")) {
                itemTabFragment.ItemDataFilter(null);
            } else {
                itemTabFragment.ItemDataFilter(sbItemTypeMasterId.toString());
            }
        }
    }

    @Override
    public void CartIconOnClick() {
        if (!errorLayout.isShown()) {
            ReplaceFragment(new CartItemFragment(), getActivity().getResources().getString(R.string.title_fragment_cart_item));
        }
    }

    @Override
    public void CardViewOnClick(ItemMaster objItemMaster) {
        DetailFragment detailFragment = new DetailFragment(objItemMaster);
        detailFragment.setTargetFragment(this, 0);
        ReplaceFragment(detailFragment, getActivity().getResources().getString(R.string.title_fragment_detail));
    }

    @Override
    public void ShowMessage(String itemName, boolean isWishListUpdate, ItemMaster objItemMaster) {
        if (isWishListUpdate) {
            if (objItemMaster != null) {
                isUpdate = true;
                objUpdateItemMaster = objItemMaster;
                itemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
                //itemTabFragment.UpdateWishList(objItemMaster);
            }
        } else {
            objCategoryMaster = itemPagerAdapter.GetCategoryMaster(itemTabLayout.getSelectedTabPosition());
            objCategoryMaster.setDescription(itemName);
        }
    }

    @Override
    public void ImageViewOnClick(int position, boolean isBackPressed) {
        adapter.RemoveData(position);
        if (Globals.alOrderItemTran.size() == 0) {
            SetRecyclerView();
        }
        ItemTabFragment itemTabFragment = (ItemTabFragment) itemPagerAdapter.getItem(itemTabLayout.getSelectedTabPosition());
        itemTabFragment.SetCartNumber();
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

    //compound button signup click event form guest login
    @Override
    public void LoginResponse() {
        ReplaceFragment(new SignUpFragment(), getActivity().getResources().getString(R.string.title_fragment_signup));
    }

    @Override
    public void AddToCart() {
        SetRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isUpdate) {
            itemTabFragment.UpdateWishList(objUpdateItemMaster);
            isUpdate = false;
        }
    }

    //region Private Methods and Interface
    private void SetRecyclerView() {
        if (Globals.alOrderItemTran.size() == 0) {
            rvCartItem.setVisibility(View.GONE);
            txtMsg.setText(getActivity().getResources().getString(R.string.MsgCart));
            isBackPressed = true;
            SaveCartDataInSharePreference(isBackPressed);
        } else {
            rvCartItem.setVisibility(View.VISIBLE);
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

    private void SetErrorLayout(boolean isShow, String errorMsg, TabLayout tabLayout, ViewPager viewPager, int errorIcon) {
        TextView txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);
        ImageView ivErrorIcon = (ImageView) errorLayout.findViewById(R.id.ivErrorIcon);
        if (errorIcon != 0) {
            ivErrorIcon.setImageResource(errorIcon);
        } else {
            ivErrorIcon.setImageResource(R.drawable.alert_drawable);
        }
        if (isShow) {
            errorLayout.setVisibility(View.VISIBLE);
            txtMsg.setText(errorMsg);
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            famRoot.setVisibility(View.GONE);

        } else {
            errorLayout.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            famRoot.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("CommitTransaction")
    public void ReplaceFragment(Fragment fragment, String fragmentName) {
//        if (Build.VERSION.SDK_INT >= 21) {
//            Fade fade = new Fade();
//            fade.setDuration(500);
//            fragment.setEnterTransition(fade);
//            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//            RemoveFragment(fragmentTransaction, itemTabLayout.getSelectedTabPosition());
//            fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
//            fragmentTransaction.addToBackStack(fragmentName);
//            fragmentTransaction.commit();
//        } else {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        RemoveFragment(fragmentTransaction, itemTabLayout.getSelectedTabPosition());
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
//        }
    }

    private void RemoveFragment(FragmentTransaction fragmentTransaction, int selectedPosition) {
        if (selectedPosition == 0) {
            ItemTabFragment CurrentItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
            ItemTabFragment NextItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition() + 1);
            fragmentTransaction.remove(CurrentItemTabFragment);
            fragmentTransaction.remove(NextItemTabFragment);

        } else if (selectedPosition == itemPagerAdapter.getCount() - 1) {
            ItemTabFragment CurrentItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
            ItemTabFragment PreviousItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition() - 1);
            fragmentTransaction.remove(CurrentItemTabFragment);
            fragmentTransaction.remove(PreviousItemTabFragment);
        } else {
            ItemTabFragment CurrentItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
            ItemTabFragment NextItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition() + 1);
            ItemTabFragment PreviousItemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition() - 1);
            fragmentTransaction.remove(CurrentItemTabFragment);
            fragmentTransaction.remove(NextItemTabFragment);
            fragmentTransaction.remove(PreviousItemTabFragment);
        }

    }

    private void OptionMenuClick(MenuItem menuItem) {
        if (menuItem.getTitle() == getActivity().getResources().getString(R.string.navLogin)) {
            GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
            guestLoginDialogFragment.setTargetFragment(this, 0);
            guestLoginDialogFragment.show(getActivity().getSupportFragmentManager(), "");
        } else if (menuItem.getTitle() == getActivity().getResources().getString(R.string.navRegistration)) {
            ReplaceFragment(new SignUpFragment(), getActivity().getResources().getString(R.string.title_fragment_signup));
        } else if (menuItem.getTitle() == getActivity().getResources().getString(R.string.wmLogout)) {
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            objSharePreferenceManage.RemovePreference("RegistrationPreference", "UserName", getActivity());
            objSharePreferenceManage.RemovePreference("RegistrationPreference", "CustomerMasterId", getActivity());
            objSharePreferenceManage.RemovePreference("RegistrationPreference", "FullName", getActivity());
            objSharePreferenceManage.ClearPreference("RegistrationPreference", getActivity());
            Globals.userName = null;
        } else if (menuItem.getTitle() == getActivity().getResources().getString(R.string.wmMyAccount)) {
            ReplaceFragment(new MyAccountFragment(), getActivity().getResources().getString(R.string.title_fragment_myaccount));
        }
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    private void CheckSelected() {
        sbItemTypeMasterId = new StringBuilder();
        if (fabVeg.isSelected()) {
            sbItemTypeMasterId.append(Globals.OptionValue.Veg.getValue() + ",");
        }
        if (fabNonVeg.isSelected()) {
            sbItemTypeMasterId.append(Globals.OptionValue.NonVeg.getValue() + ",");
        }
        if (fabJain.isSelected()) {
            sbItemTypeMasterId.append(Globals.OptionValue.Jain.getValue() + ",");
        }
    }

    private void CheckSelectedFloatingButton() {
        if (!sbItemTypeMasterId.toString().equals("")) {
            String[] strOptionValue = sbItemTypeMasterId.toString().split(",");
            for (String optionValue : strOptionValue) {
                if (optionValue.equals(String.valueOf(Globals.OptionValue.Veg.getValue()))) {
                    isVegCheck += 0;
                    fabVeg.setSelected(true);
                    fabVeg.setColorNormal(ContextCompat.getColor(getActivity(), R.color.accent_secondary));
                } else if (optionValue.equals(String.valueOf(Globals.OptionValue.NonVeg.getValue()))) {
                    isNonVegCheck += 0;
                    fabNonVeg.setSelected(true);
                    fabNonVeg.setColorNormal(ContextCompat.getColor(getActivity(), R.color.accent_secondary));
                } else if (optionValue.equals(String.valueOf(Globals.OptionValue.Jain.getValue()))) {
                    isJainCheck += 0;
                    fabJain.setSelected(true);
                    fabJain.setColorNormal(ContextCompat.getColor(getActivity(), R.color.accent_secondary));
                }
            }
        }
    }

    private void SaveWishListInSharePreference(boolean isBackPressed) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        ArrayList<String> alString;
        if (isBackPressed) {
            if (objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", getActivity()) != null) {
                alString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", getActivity());
                if (alString.size() > 0) {
                    if (CategoryItemAdapter.alWishItemMaster.size() > 0) {
                        for (ItemMaster objWishItemMaster : CategoryItemAdapter.alWishItemMaster) {
                            if (objWishItemMaster.getIsChecked() != -1) {
                                if (!CheckDuplicateId(alString, String.valueOf(objWishItemMaster.getItemMasterId()), (short) 1)) {
                                    alString.add(String.valueOf(objWishItemMaster.getItemMasterId()));
                                }
                            } else {
                                CheckDuplicateId(alString, String.valueOf(objWishItemMaster.getItemMasterId()), (short) -1);
                            }
                        }
                        objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, getActivity());
                    }
                } else {
                    if (CategoryItemAdapter.alWishItemMaster.size() > 0) {
                        alString = new ArrayList<>();
                        for (ItemMaster objWishItemMaster : CategoryItemAdapter.alWishItemMaster) {
                            if (objWishItemMaster.getIsChecked() != -1) {
                                alString.add(String.valueOf(objWishItemMaster.getItemMasterId()));
                            }
                        }
                        objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, getActivity());
                    }
                }
            } else {
                if (CategoryItemAdapter.alWishItemMaster.size() > 0) {
                    alString = new ArrayList<>();
                    for (ItemMaster objWishItemMaster : CategoryItemAdapter.alWishItemMaster) {
                        if (objWishItemMaster.getIsChecked() != -1) {
                            alString.add(String.valueOf(objWishItemMaster.getItemMasterId()));
                        }
                    }
                    objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, getActivity());
                }
            }
        } else {
            if (objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", getActivity()) != null) {
                alString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", getActivity());
                CategoryItemAdapter.alWishItemMaster = new ArrayList<>();
                if (alString.size() > 0) {
                    for (String itemMasterId : alString) {
                        ItemMaster objItemMaster = new ItemMaster();
                        objItemMaster.setItemMasterId(Integer.parseInt(itemMasterId));
                        objItemMaster.setIsChecked((short) 1);
                        CategoryItemAdapter.alWishItemMaster.add(objItemMaster);
                    }
                }
            } else {
                CategoryItemAdapter.alWishItemMaster = new ArrayList<>();
            }
        }
    }

    private boolean CheckDuplicateId(ArrayList<String> arrayList, String id, short isCheck) {
        boolean isDuplicate = false;
        int cnt = 0;
        for (String strId : arrayList) {
            if (strId.equals(id)) {
                isDuplicate = true;
                if (isCheck == -1) {
                    arrayList.remove(cnt);
                    break;
                }
            }
            cnt++;
        }
//        if (isDuplicate) {
//            return isDuplicate;
//        }
        return isDuplicate;
    }

    //endregion

    //region PagerAdapter
    static class ItemPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> itemFragmentList = new ArrayList<>();
        private final List<CategoryMaster> itemFragmentTitleList = new ArrayList<>();

        public ItemPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void AddFragment(Fragment fragment, CategoryMaster title) {
            fragment.setTargetFragment(Globals.targetFragment, 0);
            itemFragmentList.add(fragment);
            itemFragmentTitleList.add(title);
        }

        public Fragment GetCurrentFragment(int position) {
            return itemFragmentList.get(position);
        }

        public CategoryMaster GetCategoryMaster(int position) {
            return itemFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return itemFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return itemFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return itemFragmentTitleList.get(position).getCategoryName();
        }
    }
    //endregion


    //region Loading Task
    public class GuestHomeCategoryLodingTask extends AsyncTask {
        ArrayList<CategoryMaster> alCategoryMaster;
        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            CategoryJSONParser objCategoryJSONParser = new CategoryJSONParser();
            alCategoryMaster = objCategoryJSONParser.SelectAllCategoryMaster(Globals.businessMasterId);
            return alCategoryMaster;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            if (alCategoryMaster == null) {
                SetErrorLayout(true, getResources().getString(R.string.MsgSelectFail), itemTabLayout, itemViewPager, 0);
            } else if (alCategoryMaster.size() == 0) {
                SetErrorLayout(true, getResources().getString(R.string.MsgNoRecord), itemTabLayout, itemViewPager, 0);
            } else {

                SetErrorLayout(false, null, itemTabLayout, itemViewPager, 0);

                itemPagerAdapter = new ItemPagerAdapter(getFragmentManager());

                CategoryMaster objCategoryMaster = new CategoryMaster();
                objCategoryMaster.setCategoryMasterId((short) 0);
                objCategoryMaster.setCategoryName("All");
                ArrayList<CategoryMaster> alCategory = new ArrayList<>();
                alCategory.add(objCategoryMaster);
                if (isFavoriteShow) {
                    objCategoryMaster = new CategoryMaster();
                    objCategoryMaster.setCategoryMasterId((short) 0);
                    objCategoryMaster.setCategoryName(getActivity().getResources().getString(R.string.strFavorite));
                    alCategory.add(objCategoryMaster);
                }
                alCategoryMaster.addAll(0, alCategory);

                for (int i = 0; i < alCategoryMaster.size(); i++) {
                    itemPagerAdapter.AddFragment(ItemTabFragment.createInstance(alCategoryMaster.get(i)), alCategoryMaster.get(i));
                }

                itemViewPager.setAdapter(itemPagerAdapter);
                itemTabLayout.setupWithViewPager(itemViewPager);

                itemTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        //set the current view on tab click
                        itemViewPager.setCurrentItem(tab.getPosition());
                        if (famRoot.isMenuButtonHidden()) {
                            famRoot.showMenuButton(true);
                        }

                        ItemTabFragment itemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
                        if (fabVeg.isSelected() || fabNonVeg.isSelected() || fabJain.isSelected()) {
                            itemTabFragment.ItemDataFilter(sbItemTypeMasterId.toString());
                        } else {
                            itemTabFragment.ItemDataFilter(null);
                        }
                        //change view
                        if (isForceToChange) {
                            if (fabVeg.isSelected() || fabNonVeg.isSelected() || fabJain.isSelected()) {
                                itemTabFragment.SetupRecyclerView(true, null);
                            } else {
                                itemTabFragment.SetupRecyclerView(false, null);
                            }
                            isForceToChange = false;
                        } else {
                            isForceToChange = false;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        }
    }
    //endregion

}
