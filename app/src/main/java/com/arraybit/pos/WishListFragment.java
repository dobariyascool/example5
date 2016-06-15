package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.arraybit.adapter.CategoryItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class WishListFragment extends Fragment implements CategoryItemAdapter.ItemClickListener,DetailFragment.ResponseListener,AddItemQtyDialogFragment.AddToCartListener{

    LinearLayout errorLayout;
    RecyclerView rvWishItemMaster;
    ArrayList<String> alString;
    ArrayList<ItemMaster> alItemMaster;
    boolean isShowMsg = false, isRemoveFromList = false;
    CategoryItemAdapter itemAdapter;
    RelativeLayout relativeLayout;
    com.rey.material.widget.TextView txtCartNumber;
    ProgressDialog progressDialog = new ProgressDialog();
    String itemName;
    int counterMasterId;
    MenuItem searchItem;
    String searchText;
    DisplayMetrics displayMetrics;
    boolean isFilter = false;
    FrameLayout wishListFragment;
    StringBuilder sbItemMasterIds;
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();

    public WishListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_activity_wish_list));

            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        //end

        setHasOptionsMenu(true);

        displayMetrics = getResources().getDisplayMetrics();

        wishListFragment = (FrameLayout) view.findViewById(R.id.wishListFragment);
        Globals.SetScaleImageBackground(getActivity(), null, null, wishListFragment);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);


        rvWishItemMaster = (RecyclerView) view.findViewById(R.id.rvWishItemMaster);
        rvWishItemMaster.setVisibility(View.GONE);


        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
            counterMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
        }

        if (Service.CheckNet(getActivity())) {
            SaveWishListInSharePreference(false);
            sbItemMasterIds = new StringBuilder();
            if (alString != null && alString.size() != 0) {
                for (String str : alString) {
                    sbItemMasterIds.append(str);
                    sbItemMasterIds.append(",");
                }
            } else {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvWishItemMaster, 0);
            }
            if (!sbItemMasterIds.toString().equals("")) {
                new ItemLoadingTask().execute();
            }else{
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvWishItemMaster, 0);
            }

        } else {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), rvWishItemMaster, R.drawable.wifi_drawable);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem cartItem = menu.findItem(R.id.cart_layout);

        relativeLayout = (RelativeLayout) MenuItemCompat.getActionView(cartItem);
        final ImageView ivCart = (ImageView) relativeLayout.findViewById(R.id.ivCart);
        txtCartNumber = (TextView) relativeLayout.findViewById(R.id.txtCartNumber);

        SetCartNumber(txtCartNumber);

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.HideKeyBoard(getActivity(), v);
                if (!errorLayout.isShown()) {
                    ReplaceFragment(new CartItemFragment(), getResources().getString(R.string.title_fragment_cart_item));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_activity_wish_list))) {
                SaveWishListInSharePreference(true);
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.right_exit);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void SetCartNumber(TextView txtCartNumber) {
        if (Globals.counter > 0) {
            txtCartNumber.setText(String.valueOf(Globals.counter));
            txtCartNumber.setSoundEffectsEnabled(true);
            txtCartNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.cart_number));
            txtCartNumber.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fab_scale_up));
        } else {
            txtCartNumber.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isShowMsg){
            if(itemName!=null && !itemName.equals("")){
                Globals.ShowSnackBar(rvWishItemMaster, String.format(getActivity().getResources().getString(R.string.MsgCartItem), itemName),getActivity(), 3000);
            }else{
                Globals.ShowSnackBar(rvWishItemMaster, getActivity().getResources().getString(R.string.MsgCartWithNoName), getActivity(), 3000);
            }
            isShowMsg = false;
        }
    }

    private void SaveWishListInSharePreference(boolean isBackPressed) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
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
                CategoryItemAdapter.alWishItemMaster = new ArrayList<>();
                alString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", getActivity());
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

    private void SetRecyclerView() {
        if (alItemMaster == null) {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvWishItemMaster, 0);
        } else if (alItemMaster.size() == 0) {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvWishItemMaster, 0);
        } else {
            Globals.SetErrorLayout(errorLayout, false, null, rvWishItemMaster, 0);
            itemAdapter = new CategoryItemAdapter(getActivity(), alItemMaster, getActivity().getSupportFragmentManager(), false, this, false, true);
            rvWishItemMaster.setAdapter(itemAdapter);
            rvWishItemMaster.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @Override
    public void ButtonOnClick(ItemMaster objItemMaster) {
        if (objItemMaster.getItemModifierIds().equals("") && objItemMaster.getOptionValueTranIds().equals("")) {
            AddItemQtyDialogFragment addItemQtyDialogFragment = new AddItemQtyDialogFragment(objItemMaster);
            addItemQtyDialogFragment.setTargetFragment(this,0);
            addItemQtyDialogFragment.show(getActivity().getSupportFragmentManager(), "");
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("IsWishList",true);
            DetailFragment detailFragment = new DetailFragment(objItemMaster);
            detailFragment.setArguments(bundle);
            detailFragment.setTargetFragment(this,0);
            ReplaceFragment(detailFragment, getResources().getString(R.string.title_fragment_detail));
        }
    }

    @Override
    public void CardViewOnClick(ItemMaster objItemMaster) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("IsWishList", true);
        DetailFragment detailFragment = new DetailFragment(objItemMaster);
        detailFragment.setArguments(bundle);
        detailFragment.setTargetFragment(this, 0);
        ReplaceFragment(detailFragment, getResources().getString(R.string.title_fragment_detail));
    }

    @Override
    public void LikeOnClick(int position) {
        itemAdapter.RemoveData(position, isRemoveFromList);
        isRemoveFromList = false;
        if (alItemMaster.size() == 0) {
            SetRecyclerView();
        }
    }

    @Override
    public void ShowMessage(String itemName,boolean isWishListUpdate,ItemMaster objItemMaster) {
        if(isWishListUpdate){
            if(objItemMaster!=null) {
                itemAdapter.CheckIdInCurrentList(objItemMaster.getIsChecked(),objItemMaster.getItemMasterId(),objItemMaster.getIsChecked()==1?(short)0:(short)1,objItemMaster);
            }
        }else{
            isShowMsg = true;
            this.itemName = itemName;
            SetCartNumber(txtCartNumber);
        }

    }
    @SuppressLint("CommitTransaction")
    public void ReplaceFragment(Fragment fragment, String fragmentName) {
        if (Build.VERSION.SDK_INT >= 21) {
            Fade fade = new Fade();
            fade.setDuration(500);
            fragment.setEnterTransition(fade);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.wishListLayout, fragment, fragmentName);
            fragmentTransaction.addToBackStack(fragmentName);
            fragmentTransaction.commit();
        } else {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.wishListLayout, fragment, fragmentName);
            fragmentTransaction.addToBackStack(fragmentName);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void AddToCart(boolean isAddToCart, ItemMaster objOrderItemTran) {
        if (isAddToCart) {
            if (objOrderItemTran.getItemName() != null) {
                Globals.counter = Globals.counter + 1;
                SetCartNumber(txtCartNumber);
                Globals.alOrderItemTran.add(objOrderItemTran);
                Globals.ShowSnackBar(rvWishItemMaster,String.format(getActivity().getResources().getString(R.string.MsgCartItem),objOrderItemTran.getItemName()), getActivity(), 3000);
            }
        }
    }


    class ItemLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ItemJSONParser objItemJSONParser = new ItemJSONParser();
            return objItemJSONParser.SelectAllItemMaster(counterMasterId, GuestHomeActivity.objTableMaster.getlinktoOrderTypeMasterId(), 0, null, Globals.businessMasterId, 0, sbItemMasterIds.toString());
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            alItemMaster = (ArrayList<ItemMaster>) result;
            SetRecyclerView();
        }
    }
}
