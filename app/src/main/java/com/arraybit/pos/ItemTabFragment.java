package com.arraybit.pos;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.arraybit.adapter.CategoryItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings({"unchecked", "ObjectEqualsNull", "ConstantConditions"})
public class ItemTabFragment extends Fragment implements SearchView.OnQueryTextListener, CategoryItemAdapter.ItemClickListener, AddItemQtyDialogFragment.AddToCartListener {

    public final static String ITEMS_COUNT_KEY = "ItemTabFragment";
    public static short cnt = 0;
    public static Activity activity;
    TextView txtMsg;
    RecyclerView rvItem;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    ArrayList<ItemMaster> alItemMaster;
    String itemTypeMasterId = null;
    SharePreferenceManage objSharePreferenceManage;
    CategoryItemAdapter categoryItemAdapter;
    CategoryMaster objCategoryMaster;
    int counterMasterId;
    DisplayMetrics displayMetrics;
    boolean isFilter = false,isFavorite;
    String searchText;
    TextView txtCartNumber;
    RelativeLayout relativeLayout;
    CartIconListener objCartIconListener;
    MenuItem searchItem;
    ArrayList<ItemMaster> alItemMasterFilter;
    View v;
    LinearLayout errorLayout;

    public ItemTabFragment() {
    }

    public static ItemTabFragment createInstance(CategoryMaster objCategoryMaster) {
        ItemTabFragment itemTabFragment = new ItemTabFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEMS_COUNT_KEY, objCategoryMaster);
        itemTabFragment.setArguments(bundle);
        return itemTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_tab, container, false);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);

        rvItem = (RecyclerView) view.findViewById(R.id.rvItem);
        rvItem.setVisibility(View.GONE);

        displayMetrics = getActivity().getResources().getDisplayMetrics();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        Bundle bundle = getArguments();
        objCategoryMaster = bundle.getParcelable(ITEMS_COUNT_KEY);
        alItemMaster = new ArrayList<>();

        //get counterMasterId
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
            counterMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
        }
        //end

        setHasOptionsMenu(true);
        if (Service.CheckNet(getActivity())) {
            new GuestHomeItemLoadingTask().execute();
        } else {
            Globals.SetErrorLayout(errorLayout,true,getResources().getString(R.string.MsgCheckConnection),rvItem,R.drawable.wifi_drawable);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void ItemDataFilter(String itemTypeMasterId) {
        this.itemTypeMasterId = itemTypeMasterId;
        alItemMasterFilter = new ArrayList<>();
        boolean isDuplicate;
        if (itemTypeMasterId != null) {
            for (int i = 0; i < alItemMaster.size(); i++) {
                if (!alItemMaster.get(i).getOptionValueTranIds().equals("")) {
                    isDuplicate = false;
                    String[] optionType = alItemMaster.get(i).getOptionValueTranIds().split(",");
                    String[] itemType = itemTypeMasterId.split(",");
                    for (String strItemType : itemType) {
                        for (String strOptionType : optionType) {
                            if (strItemType.equals(strOptionType)) {
                                if (alItemMasterFilter.size() == 0) {
                                    alItemMasterFilter.add(alItemMaster.get(i));
                                } else {
                                    for (int l = 0; l < alItemMasterFilter.size(); l++) {
                                        if (alItemMasterFilter.get(l).getItemMasterId() == alItemMaster.get(i).getItemMasterId()) {
                                            isDuplicate = true;
                                            break;
                                        }
                                    }
                                    if (!isDuplicate) {
                                        alItemMasterFilter.add(alItemMaster.get(i));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (alItemMasterFilter.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgItem), rvItem,0);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvItem,0);
                SetupRecyclerView(false, alItemMasterFilter);
            }

        } else {
            if (alItemMaster.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgItem), rvItem,0);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvItem,0);
                SetupRecyclerView(false, alItemMaster);
            }

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
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
                objCartIconListener = (CartIconListener) Globals.targetFragment;
                objCartIconListener.CartIconOnClick();
            }
        });

        searchItem = menu.findItem(R.id.action_search);
        final SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setInputType(InputType.TYPE_CLASS_TEXT);
        mSearchView.setMaxWidth(displayMetrics.widthPixels);
        mSearchView.setOnQueryTextListener(this);
        searchText = mSearchView.getQuery().toString();

        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        if (CategoryItemFragment.sbItemTypeMasterId.toString().equals("")) {
                            if (alItemMaster.size() != 0 && alItemMaster != null) {
                                categoryItemAdapter.SetSearchFilter(alItemMaster);
                                Globals.HideKeyBoard(getActivity(), MenuItemCompat.getActionView(searchItem));
                            }
                        } else {
                            if (alItemMasterFilter.size() != 0 && alItemMasterFilter != null) {
                                categoryItemAdapter.SetSearchFilter(alItemMasterFilter);
                                Globals.HideKeyBoard(getActivity(), MenuItemCompat.getActionView(searchItem));
                            }
                        }
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (CategoryItemFragment.sbItemTypeMasterId.toString().equals("")) {
            if (alItemMaster.size() != 0 && alItemMaster != null) {
                searchText = newText;
                final ArrayList<ItemMaster> filteredList = Filter(alItemMaster, newText);
                categoryItemAdapter.SetSearchFilter(filteredList);
            }
        } else {
            if (alItemMasterFilter.size() != 0 && alItemMasterFilter != null) {
                searchText = newText;
                final ArrayList<ItemMaster> filteredList = Filter(alItemMasterFilter, newText);
                categoryItemAdapter.SetSearchFilter(filteredList);
            }
        }

        return false;
    }


    @Override
    public void ButtonOnClick(ItemMaster objItemMaster) {
        MenuItemCompat.collapseActionView(searchItem);
        if (objItemMaster.getItemModifierIds().equals("") && objItemMaster.getOptionValueTranIds().equals("")) {
            AddItemQtyDialogFragment addItemQtyDialogFragment = new AddItemQtyDialogFragment(objItemMaster);
            addItemQtyDialogFragment.setTargetFragment(this, 0);
            addItemQtyDialogFragment.show(getFragmentManager(), "");
        } else {
            objCartIconListener = (CartIconListener) Globals.targetFragment;
            objCartIconListener.CardViewOnClick(objItemMaster);
        }
//        if (objItemMaster.getItemModifierIds().equals("")) {
//            AddItemQtyDialogFragment addItemQtyDialogFragment = new AddItemQtyDialogFragment(objItemMaster);
//            addItemQtyDialogFragment.setTargetFragment(this, 0);
//            addItemQtyDialogFragment.show(getFragmentManager(), "");
//        } else {
//            objCartIconListener = (CartIconListener) Globals.targetFragment;
//            objCartIconListener.CardViewOnClick(objItemMaster);
//        }
    }

    @Override
    public void CardViewOnClick(ItemMaster objItemMaster) {

        objCartIconListener = (CartIconListener) Globals.targetFragment;
        objCartIconListener.CardViewOnClick(objItemMaster);

    }

    @Override
    public void AddToCart(boolean isAddToCart, ItemMaster objOrderItemTran) {
        if (isAddToCart) {
            if (objOrderItemTran.getItemName() != null) {
                Globals.counter = Globals.counter + 1;
                SetCartNumber(txtCartNumber);
                Globals.alOrderItemTran.add(objOrderItemTran);
                Globals.ShowSnackBar(rvItem,String.format(getActivity().getResources().getString(R.string.MsgCartItem),objOrderItemTran.getItemName()), getActivity(), 3000);
            }
        }
    }

    public void SetupRecyclerView(final boolean isFilter, ArrayList<ItemMaster> alItemMaster) {
        if (alItemMaster == null && !isFilter) {
            categoryItemAdapter = new CategoryItemAdapter(getActivity(), this.alItemMaster, getFragmentManager(), CategoryItemFragment.isViewChange, this, false);
            rvItem.setVisibility(View.VISIBLE);
            rvItem.setAdapter(categoryItemAdapter);
            if (CategoryItemFragment.isViewChange && (searchText == null || searchText.isEmpty())) {
                rvItem.setLayoutManager(gridLayoutManager);
            } else if (!CategoryItemFragment.isViewChange && (searchText == null || searchText.isEmpty())) {
                rvItem.setLayoutManager(linearLayoutManager);
            } else if (CategoryItemFragment.isViewChange && !searchText.isEmpty()) {
                final ArrayList<ItemMaster> filteredList = Filter(this.alItemMaster, searchText);
                categoryItemAdapter.SetSearchFilter(filteredList);
                rvItem.setLayoutManager(gridLayoutManager);
            } else if (!CategoryItemFragment.isViewChange && !searchText.isEmpty()) {
                final ArrayList<ItemMaster> filteredList = Filter(this.alItemMaster, searchText);
                categoryItemAdapter.SetSearchFilter(filteredList);
                rvItem.setLayoutManager(linearLayoutManager);
            }
        } else if (alItemMaster == null && isFilter) {
            categoryItemAdapter = new CategoryItemAdapter(getActivity(), alItemMasterFilter, getFragmentManager(), CategoryItemFragment.isViewChange, this, false);
            rvItem.setVisibility(View.VISIBLE);
            rvItem.setAdapter(categoryItemAdapter);
            if (CategoryItemFragment.isViewChange && (searchText == null || searchText.isEmpty())) {
                rvItem.setLayoutManager(gridLayoutManager);
            } else if (!CategoryItemFragment.isViewChange && (searchText == null || searchText.isEmpty())) {
                rvItem.setLayoutManager(linearLayoutManager);
            } else if (CategoryItemFragment.isViewChange && !searchText.isEmpty()) {
                final ArrayList<ItemMaster> filteredList = Filter(alItemMasterFilter, searchText);
                categoryItemAdapter.SetSearchFilter(filteredList);
                rvItem.setLayoutManager(gridLayoutManager);
            } else if (!CategoryItemFragment.isViewChange && !searchText.isEmpty()) {
                final ArrayList<ItemMaster> filteredList = Filter(alItemMasterFilter, searchText);
                categoryItemAdapter.SetSearchFilter(filteredList);
                rvItem.setLayoutManager(linearLayoutManager);
            }

        } else {
            categoryItemAdapter = new CategoryItemAdapter(getActivity(), alItemMaster, getFragmentManager(), CategoryItemFragment.isViewChange, this, false);
            rvItem.setVisibility(View.VISIBLE);
            rvItem.setAdapter(categoryItemAdapter);
            if (CategoryItemFragment.isViewChange && (searchText == null || searchText.isEmpty())) {
                rvItem.setLayoutManager(gridLayoutManager);
            } else if (!CategoryItemFragment.isViewChange && (searchText == null || searchText.isEmpty())) {
                rvItem.setLayoutManager(linearLayoutManager);
            } else if (CategoryItemFragment.isViewChange && !searchText.isEmpty()) {
                final ArrayList<ItemMaster> filteredList = Filter(alItemMaster, searchText);
                categoryItemAdapter.SetSearchFilter(filteredList);
                rvItem.setLayoutManager(gridLayoutManager);
            } else if (!CategoryItemFragment.isViewChange && !searchText.isEmpty()) {
                final ArrayList<ItemMaster> filteredList = Filter(alItemMaster, searchText);
                categoryItemAdapter.SetSearchFilter(filteredList);
                rvItem.setLayoutManager(linearLayoutManager);
            }

        }

        if (CategoryItemFragment.objCategoryMaster != null && CategoryItemFragment.objCategoryMaster.getCategoryName().equals(objCategoryMaster.getCategoryName())) {
            if(objCategoryMaster.getDescription()!=null && !objCategoryMaster.getDescription().equals("")){
                Globals.ShowSnackBar(rvItem, String.format(getActivity().getResources().getString(R.string.MsgCartItem),objCategoryMaster.getDescription()), getActivity(), 3000);
            }else{
                Globals.ShowSnackBar(rvItem, getActivity().getResources().getString(R.string.MsgCartWithNoName), getActivity(), 3000);
            }
            CategoryItemFragment.objCategoryMaster = null;
        }
        rvItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Globals.HideKeyBoard(getActivity(), recyclerView);
                if (!categoryItemAdapter.isItemAnimate) {
                    categoryItemAdapter.isItemAnimate = true;
                }
            }
        });
    }

    //region Private Methods and Interface
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

    private ArrayList<ItemMaster> Filter(ArrayList<ItemMaster> lstItemMaster, String filterName) {
        filterName = filterName.toLowerCase();
        final ArrayList<ItemMaster> filteredList = new ArrayList<>();
        for (ItemMaster objItemMaster : lstItemMaster) {
            isFilter = false;
            ArrayList<String> alString = new ArrayList<>(Arrays.asList(objItemMaster.getItemName().toLowerCase().split(" ")));
            alString.add(0, objItemMaster.getItemName().toLowerCase());
            for (String aStrArray : alString) {
                if (aStrArray.length() >= filterName.length()) {
                    final String strItem = aStrArray.substring(0, filterName.length()).toLowerCase();
                    if (!isFilter) {
                        if (strItem.contains(filterName)) {
                            filteredList.add(objItemMaster);
                            isFilter = true;
                        }
                    }
                }
            }
        }
        return filteredList;
    }

    interface CartIconListener {
        void CartIconOnClick();
        void CardViewOnClick(ItemMaster objItemMaster);
    }

    //endregion

    //region LoadingTask
    class GuestHomeItemLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (objCategoryMaster.getCategoryName().equals("All") && cnt == 0) {
                progressDialog = new com.arraybit.pos.ProgressDialog();
                progressDialog.show(getActivity().getSupportFragmentManager(), "");
            }
            isFavorite = objCategoryMaster.getCategoryName().equals(getActivity().getResources().getString(R.string.strFavorite));
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ItemJSONParser objItemJSONParser = new ItemJSONParser();
            if(isFavorite){
                return objItemJSONParser.SelectAllItemMaster(counterMasterId, MenuActivity.objTableMaster.getlinktoOrderTypeMasterId(), objCategoryMaster.getCategoryMasterId(), itemTypeMasterId,Globals.businessMasterId,1);
            }else{
                return objItemJSONParser.SelectAllItemMaster(counterMasterId, MenuActivity.objTableMaster.getlinktoOrderTypeMasterId(), objCategoryMaster.getCategoryMasterId(), itemTypeMasterId,Globals.businessMasterId,0);
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (cnt == 0 && (progressDialog != null && progressDialog.isVisible())) {
                progressDialog.dismiss();
                cnt = 1;
            }

            ArrayList<ItemMaster> lstItemMaster = (ArrayList<ItemMaster>) result;
            if (lstItemMaster == null) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgSelectFail), rvItem,0);
            } else if (lstItemMaster.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgItem), rvItem,0);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvItem,0);
                alItemMaster = lstItemMaster;
                if (CategoryItemFragment.sbItemTypeMasterId.toString().equals("")) {
                    SetupRecyclerView(false, alItemMaster);
                    // cnt = 1;
                } else {
                    ItemDataFilter(CategoryItemFragment.sbItemTypeMasterId.toString());
                    //cnt = 1;
                }
            }
        }
    }
    //endregion

    //region CommentCode
    /*public class GuestHomeItemLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());

            if (currentPage > 2 && alItemMaster.size() != 0) {
                progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ItemJSONParser objItemJSONParser = new ItemJSONParser();
            if ((linearLayoutManager.canScrollVertically() || gridLayoutManager.canScrollVertically()) && alItemMaster.size() == 0) {
                currentPage = 1;
            }

            return objItemJSONParser.SelectAllItemMaster(currentPage,counterMasterId, objCategoryMaster.getCategoryMasterId(), itemTypeMasterId);

        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (currentPage > 2) {
                progressDialog.dismiss();
            }
            ArrayList<ItemMaster> lstItemMaster = (ArrayList<ItemMaster>) result;
            if (lstItemMaster == null) {
                if (currentPage == 1) {
                    Globals.SetError(txtMsg, rvItem, getResources().getString(R.string.MsgSelectFail), true);
                }
            } else if (lstItemMaster.size() == 0) {
                if (currentPage == 1) {
                    Globals.SetError(txtMsg, rvItem, getResources().getString(R.string.MsgNoRecord), true);
                }
            } else {
                if (currentPage > 1) {
                    categoryItemAdapter.ItemListDataChanged(lstItemMaster);
                    return;
                } else if (lstItemMaster.size() < 10) {
                    currentPage += 1;
                }

                Globals.SetError(txtMsg, rvItem, null, false);
                alItemMaster = lstItemMaster;
                SetupRecyclerView();
            }
        }
    }*/
    //endregion
}
