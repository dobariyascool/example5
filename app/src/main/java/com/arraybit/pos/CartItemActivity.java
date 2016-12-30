package com.arraybit.pos;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartItemActivity extends AppCompatActivity {

    String activityName;
    FrameLayout fragmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item);

        fragmentLayout = (FrameLayout) findViewById(R.id.fragmentLayout);

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isHome",false)) {
            MenuActivity.objTableMaster = GuestHomeActivity.objTableMaster;
            Bundle bundle = new Bundle();
            bundle.putBoolean("isHome", true);
            CartItemFragment cartItemFragment = new CartItemFragment(CartItemActivity.this);
            cartItemFragment.setArguments(bundle);
            AddFragmentInLayout(cartItemFragment);
        }
    }

    private void AddFragmentInLayout(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.cartFragmentLayout, fragment, getResources().getString(R.string.title_fragment_cart_item));
        fragmentTransaction.addToBackStack( getResources().getString(R.string.title_fragment_cart_item));
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Globals.targetFragment=null;
            if (requestCode == 0) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else if (requestCode == 100) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.title_fragment_cart_item))) {

            SaveCartDataInSharePreference(true);

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            overridePendingTransition(0, R.anim.right_exit);
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
                    objSharePreferenceManage.CreatePreference("CartItemListPreference", "CartItemList", null, CartItemActivity.this);
                    Globals.counter = 0;
                } else {
                    objSharePreferenceManage = new SharePreferenceManage();
                    String string = objSharePreferenceManage.GetPreference("CartItemListPreference", "CartItemList", CartItemActivity.this);
                    if (string != null) {
                        ItemMaster[] objItemMaster = gson.fromJson(string,
                                ItemMaster[].class);

                        lstItemMaster = Arrays.asList(objItemMaster);
                        Globals.alOrderItemTran.addAll(new ArrayList<>(lstItemMaster));
                        Globals.counter = Globals.alOrderItemTran.size();
                        if (objSharePreferenceManage.GetPreference("CartItemListPreference", "OrderRemark", CartItemActivity.this) != null) {
                            RemarkDialogFragment.strRemark = objSharePreferenceManage.GetPreference("CartItemListPreference", "OrderRemark", CartItemActivity.this);
                        }
                    } else {
                        objSharePreferenceManage.RemovePreference("CheckOutDataPreference", "CheckOutData", CartItemActivity.this);
                        objSharePreferenceManage.ClearPreference("CheckOutDataPreference", CartItemActivity.this);
                    }
                }
            } else {
                objSharePreferenceManage = new SharePreferenceManage();
                String string = gson.toJson(Globals.alOrderItemTran);
                objSharePreferenceManage.CreatePreference("CartItemListPreference", "CartItemList", string, CartItemActivity.this);
                objSharePreferenceManage.CreatePreference("CartItemListPreference", "OrderRemark", RemarkDialogFragment.strRemark, CartItemActivity.this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
