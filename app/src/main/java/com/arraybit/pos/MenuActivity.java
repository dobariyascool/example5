package com.arraybit.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.arraybit.adapter.CategoryItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.TableMaster;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity{

    public static TableMaster objTableMaster = null;
    public static boolean parentActivity = false;
    FrameLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mainLayout = (FrameLayout) findViewById(R.id.mainLayout);
        Globals.SetScaleImageBackground(MenuActivity.this, null, null, mainLayout);

        Intent intent = getIntent();
        if (intent.getParcelableExtra("TableMaster") != null) {
            objTableMaster = intent.getParcelableExtra("TableMaster");
            Globals.orderTypeMasterId = objTableMaster.getlinktoOrderTypeMasterId();
            if (Globals.selectTableMasterId != objTableMaster.getTableMasterId()) {
                CategoryItemFragment.i = 0;
                CategoryItemFragment.isViewChange = false;
                Globals.counter = 0;
                Globals.selectTableMasterId = 0;
                Globals.alOrderItemTran = new ArrayList<>();
            }
            parentActivity = intent.getBooleanExtra("ParentActivity", false);
        }

        if(Globals.isWishListShow==1) {
            SaveWishListInSharePreference(false);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, new CategoryItemFragment(getIntent().getBooleanExtra("IsFavoriteShow",false)));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (parentActivity) {
            //Globals.SetOptionMenu(Globals.userName, MenuActivity.this, menu);
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.registration).setVisible(false);
            menu.findItem(R.id.shortList).setVisible(false);
        }else{
            menu.findItem(R.id.logout).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (MenuActivity.parentActivity) {
            getMenuInflater().inflate(R.menu.menu_home, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_waiter_home, menu);
        }
        return true;
    }

    public void EditTextOnClick(View view) {
        GuestProfileFragment guestProfileFragment = (GuestProfileFragment)getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_myprofile));
        if(guestProfileFragment!=null) {
            guestProfileFragment.EditTextOnClick();
        }else{
            DetailFragment detailFragment = (DetailFragment)getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_detail));
            if(detailFragment!=null){
                detailFragment.EditTextOnClick();
            }else{
                DetailFragment subDetailFragment = (DetailFragment)getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_sub_detail));
                subDetailFragment.EditTextOnClick();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (MenuActivity.parentActivity) {
            //Globals.OptionMenuItemClick(item, MenuActivity.this, getSupportFragmentManager());
        } else {
            if (id == R.id.logout) {
                Globals.ClearPreference(MenuActivity.this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_category_item))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_category_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_detail))) {
                    DetailFragment detailFragment = (DetailFragment)getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_detail));
                    detailFragment.SaveWishListData();
                    DetailFragment.isItemSuggestedClick = false;
                    DetailFragment.alOptionValue = new ArrayList<>();
                    DetailFragment.alSubItemOptionValue = new ArrayList<>();
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_cart_item))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_cart_item), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_all_orders))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_all_orders), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_signup))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_signup), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_policy))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_policy), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_myaccount))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_myaccount), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_notification_settings))) {
                    NotificationSettingsFragment notificationSettingsFragment = (NotificationSettingsFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_notification_settings));
                    notificationSettingsFragment.CreateNotificationPreference();
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_notification_settings), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                        && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_change_password))) {
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_change_password), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else if(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName() != null
                        &&  getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName().equals(getResources().getString(R.string.title_fragment_sub_detail))){
                   getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_sub_detail), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    DetailFragment detailFragment = (DetailFragment)getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_sub_detail));
                    detailFragment.SaveWishListData();
                    DetailFragment.isItemSuggestedClick = false;
                    DetailFragment.alOptionValue = new ArrayList<>();
                    DetailFragment.alSubItemOptionValue = new ArrayList<>();
                }
            } else {
                if(Globals.isWishListShow==1) {
                    SaveWishListInSharePreference(true);
                }
                if (MenuActivity.parentActivity) {
                    //Globals.CategoryItemFragmentResetStaticVariable();
                    finish();
                    overridePendingTransition(0, R.anim.right_exit);
                } else {
                    Globals.CategoryItemFragmentResetStaticVariable();
                    Intent intent = new Intent(MenuActivity.this, WaiterHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        }
    }

    private void SaveWishListInSharePreference(boolean isBackPressed) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        ArrayList<String> alString;
        if (isBackPressed) {
            if (objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", MenuActivity.this) != null) {
                alString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", MenuActivity.this);
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
                        objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, MenuActivity.this);
                    }
                } else {
                    if (CategoryItemAdapter.alWishItemMaster.size() > 0) {
                        alString = new ArrayList<>();
                        for (ItemMaster objWishItemMaster : CategoryItemAdapter.alWishItemMaster) {
                            if (objWishItemMaster.getIsChecked() != -1) {
                                alString.add(String.valueOf(objWishItemMaster.getItemMasterId()));
                            }
                        }
                        objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, MenuActivity.this);
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
                    objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, MenuActivity.this);
                }
            }
        } else {
            if (objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", MenuActivity.this) != null) {
                alString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", MenuActivity.this);
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

}
