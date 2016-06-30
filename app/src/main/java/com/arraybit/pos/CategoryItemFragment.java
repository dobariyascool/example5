package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arraybit.adapter.CategoryItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.CategoryJSONParser;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"unchecked", "ConstantConditions"})
@SuppressLint("ValidFragment")
public class CategoryItemFragment extends Fragment implements View.OnClickListener, ItemTabFragment.CartIconListener, DetailFragment.ResponseListener, GuestLoginDialogFragment.LoginResponseListener {

    public static boolean isViewChange = false;
    public static short i = 0;
    public static CategoryMaster objCategoryMaster = null;
    public static String itemName;
    public static StringBuilder sbItemTypeMasterId = new StringBuilder();
    ViewPager itemViewPager;
    TabLayout itemTabLayout;
    ItemPagerAdapter itemPagerAdapter;
    FloatingActionMenu famRoot;
    FloatingActionButton fabVeg, fabNonVeg, fabJain;
    boolean isForceToChange = false,isUpdate = false;
    short isVegCheck = 0, isNonVegCheck = 0, isJainCheck = 0;
    CoordinatorLayout categoryItemFragment;
    LinearLayout errorLayout;
    boolean isFavoriteShow;
    ItemMaster objUpdateItemMaster;
    ItemTabFragment itemTabFragment;


    public CategoryItemFragment(boolean isFavoriteShow) {
        if(!GuestHomeActivity.isMenuMode) {
            this.isFavoriteShow = isFavoriteShow;
            Globals.isWishListShow = (short) (isFavoriteShow ? 0 : 1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_item, container, false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_category_item));
        }
        //end

        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
            categoryItemFragment = (CoordinatorLayout) view.findViewById(R.id.categoryItemFragment);
            Globals.SetScaleImageBackground(getActivity(), categoryItemFragment);
        }

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);


        famRoot = (FloatingActionMenu) view.findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);


        fabVeg = (FloatingActionButton) view.findViewById(R.id.fabVeg);
        fabNonVeg = (FloatingActionButton) view.findViewById(R.id.fabNonVeg);
        fabJain = (FloatingActionButton) view.findViewById(R.id.fabJain);

        itemTabLayout = (TabLayout) view.findViewById(R.id.itemTabLayout);
        itemTabLayout.setClickable(true);

        itemViewPager = (ViewPager) view.findViewById(R.id.itemViewPager);

        fabVeg.setOnClickListener(this);
        fabNonVeg.setOnClickListener(this);
        fabJain.setOnClickListener(this);


        if (Service.CheckNet(getActivity())) {
            new GuestHomeCategoryLodingTask().execute();
        } else {
            SetErrorLayout(true, getResources().getString(R.string.MsgCheckConnection), itemTabLayout, itemViewPager, R.drawable.wifi_drawable);
        }

        setHasOptionsMenu(true);

        Globals.targetFragment = CategoryItemFragment.this;

        CheckSelectedFloatingButton();

        return view;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.viewChange).setVisible(true);
        if(GuestHomeActivity.isMenuMode){
            menu.findItem(R.id.cart_layout).setVisible(false);
        }else{
            if(MenuActivity.parentActivity) {
                menu.findItem(R.id.cart_layout).setVisible(true);
            }else {
                menu.findItem(R.id.cart_layout).setVisible(true);
                menu.findItem(R.id.notification_layout).setVisible(false);
            }
        }

        if (i == 1) {
            menu.findItem(R.id.viewChange).setIcon(R.drawable.view_grid);
        } else if (i == 2) {
            menu.findItem(R.id.viewChange).setIcon(R.drawable.view_grid_two);
        } else {
            menu.findItem(R.id.viewChange).setIcon(R.drawable.view_list);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_fragment_category_item))) {
                if(Globals.isWishListShow==1){
                    SaveWishListInSharePreference(true);
                }
               if (MenuActivity.parentActivity || GuestHomeActivity.isMenuMode) {
                    //Globals.CategoryItemFragmentResetStaticVariable();
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
                getActivity().getSupportFragmentManager().popBackStack(getActivity().getResources().getString(R.string.title_fragment_category_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else if (item.getItemId() == R.id.viewChange) {
            if (!errorLayout.isShown()) {
                ItemTabFragment itemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
                i = (short) (i + 1);
                if (i == 1) {
                    item.setIcon(R.drawable.view_grid);
                    isViewChange = true;
                    isForceToChange = true;
                    if (fabJain.isSelected() || fabNonVeg.isSelected() || fabVeg.isSelected()) {
                        itemTabFragment.SetupRecyclerView(true, null);
                    } else {
                        itemTabFragment.SetupRecyclerView(false, null);
                    }
                } else if (i == 2) {
                    item.setIcon(R.drawable.view_grid_two);
                    isViewChange = true;
                    isForceToChange = true;
                    if (fabJain.isSelected() || fabNonVeg.isSelected() || fabVeg.isSelected()) {
                        itemTabFragment.SetupRecyclerView(true, null);
                    } else {
                        itemTabFragment.SetupRecyclerView(false, null);
                    }
                } else {
                    i = 0;
                    item.setIcon(R.drawable.view_list);
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
    public void ShowMessage(String itemName,boolean isWishListUpdate,ItemMaster objItemMaster) {
        if(isWishListUpdate) {
            if(objItemMaster!=null){
                isUpdate = true;
                objUpdateItemMaster = objItemMaster;
                itemTabFragment = (ItemTabFragment) itemPagerAdapter.GetCurrentFragment(itemTabLayout.getSelectedTabPosition());
                //itemTabFragment.UpdateWishList(objItemMaster);
            }
        }else{
            objCategoryMaster = itemPagerAdapter.GetCategoryMaster(itemTabLayout.getSelectedTabPosition());
            objCategoryMaster.setDescription(itemName);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isUpdate){
            itemTabFragment.UpdateWishList(objUpdateItemMaster);
            isUpdate = false;
        }
    }

    //region Private Methods and Interface
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
        if (Build.VERSION.SDK_INT >= 21) {
            Fade fade = new Fade();
            fade.setDuration(500);
            fragment.setEnterTransition(fade);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            RemoveFragment(fragmentTransaction, itemTabLayout.getSelectedTabPosition());
            fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
            fragmentTransaction.addToBackStack(fragmentName);
            fragmentTransaction.commit();
        } else {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            RemoveFragment(fragmentTransaction, itemTabLayout.getSelectedTabPosition());
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(android.R.id.content, fragment, fragmentName);
            fragmentTransaction.addToBackStack(fragmentName);
            fragmentTransaction.commit();
        }
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
        if (isDuplicate) {
            return isDuplicate;
        }
        return isDuplicate;
    }


    //compound button signup click event form guest login
    @Override
    public void LoginResponse() {
        ReplaceFragment(new SignUpFragment(), getActivity().getResources().getString(R.string.title_fragment_signup));
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
