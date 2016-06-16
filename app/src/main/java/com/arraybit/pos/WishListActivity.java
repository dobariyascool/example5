package com.arraybit.pos;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.arraybit.adapter.CategoryItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class WishListActivity extends AppCompatActivity{

    LinearLayout errorLayout;
    ArrayList<String> alString;
    ProgressDialog progressDialog = new ProgressDialog();
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        FrameLayout wishListLayout = (FrameLayout)findViewById(R.id.wishListLayout);
        Globals.SetScaleImageBackground(this, null, null, wishListLayout);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.wishListLayout, new WishListFragment(), getResources().getString(R.string.title_activity_wish_list));
        fragmentTransaction.addToBackStack(getResources().getString(R.string.title_activity_wish_list));
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_detail))) {
            DetailFragment detailFragment = (DetailFragment)getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_detail));
            detailFragment.SaveWishListData();
            DetailFragment.isItemSuggestedClick = false;
            DetailFragment.alOptionValue = new ArrayList<>();
            DetailFragment.alSubItemOptionValue = new ArrayList<>();
            getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else if(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName() != null
                &&  getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName().equals(getResources().getString(R.string.title_fragment_sub_detail))){
            getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_sub_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            DetailFragment detailFragment = (DetailFragment)getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_sub_detail));
            detailFragment.SaveWishListData();
            DetailFragment.isItemSuggestedClick = false;
            DetailFragment.alOptionValue = new ArrayList<>();
            DetailFragment.alSubItemOptionValue = new ArrayList<>();
        }else{
            super.onBackPressed();
            SaveWishListInSharePreference(true);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.cart_layout).setVisible(true);
        menu.findItem(R.id.login).setVisible(false);
        menu.findItem(R.id.registration).setVisible(false);
        menu.findItem(R.id.shortList).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    private void SaveWishListInSharePreference(boolean isBackPressed) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (isBackPressed) {
            if (objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", WishListActivity.this) != null) {
                alString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", WishListActivity.this);
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
                        objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, WishListActivity.this);
                    }
                } else {
                    if (CategoryItemAdapter.alWishItemMaster.size() > 0) {
                        alString = new ArrayList<>();
                        for (ItemMaster objWishItemMaster : CategoryItemAdapter.alWishItemMaster) {
                            if (objWishItemMaster.getIsChecked() != -1) {
                                alString.add(String.valueOf(objWishItemMaster.getItemMasterId()));
                            }
                        }
                        objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, WishListActivity.this);
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
                    objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, WishListActivity.this);
                }
            }
        } else {
            if (objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", WishListActivity.this) != null) {
                CategoryItemAdapter.alWishItemMaster = new ArrayList<>();
                alString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", WishListActivity.this);
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
        return isDuplicate;
    }

}
