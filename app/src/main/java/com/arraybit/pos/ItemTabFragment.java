package com.arraybit.pos;

import android.app.ProgressDialog;
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
import android.widget.RelativeLayout;

import com.arraybit.adapter.CategoryItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings({"unchecked", "ObjectEqualsNull"})
public class ItemTabFragment extends Fragment implements SearchView.OnQueryTextListener, CategoryItemAdapter.ItemClickListener, AddItemQtyDialogFragment.AddToCartListener {

    //public final static String ITEMS_COUNT_KEY = "ItemTabFragment$ItemsCount";
    public final static String ITEMS_COUNT_KEY = "ItemTabFragment";
    public static short cnt = 0;
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
    int currentPage = 1;
    DisplayMetrics displayMetrics;
    boolean isFilter = false;
    String searchText;
    TextView txtCartNumber;
    RelativeLayout relativeLayout;
    CartIconListener objCartIconListener;

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

        rvItem = (RecyclerView) view.findViewById(R.id.rvItem);
        rvItem.setVisibility(View.GONE);

        displayMetrics = getActivity().getResources().getDisplayMetrics();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        //gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);

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

        new GuestHomeItemLoadingTask().execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void SetupRecyclerView() {

        categoryItemAdapter = new CategoryItemAdapter(getActivity(), alItemMaster, getFragmentManager(), CategoryItemFragment.isViewChange, this);
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

    public void ItemDataFilter(String itemTypeMasterId) {
        this.itemTypeMasterId = itemTypeMasterId;
        alItemMaster = new ArrayList<>();
        new GuestHomeItemLoadingTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem cartItem = menu.findItem(R.id.cart_layout);
        //MenuItemCompat.setActionView(cartItem, R.layout.cart_layout);

        relativeLayout = (RelativeLayout) MenuItemCompat.getActionView(cartItem);
        final ImageView ivCart = (ImageView) relativeLayout.findViewById(R.id.ivCart);
        txtCartNumber = (TextView) relativeLayout.findViewById(R.id.txtCartNumber);

        SetCartNumber(txtCartNumber);

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objCartIconListener = (CartIconListener) CategoryItemFragment.targetFragment;
                objCartIconListener.CartIconOnClick();
            }
        });

        MenuItem searchItem = menu.findItem(R.id.action_search);
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
                        if (alItemMaster.size() != 0 && alItemMaster != null) {
                            categoryItemAdapter.SetSearchFilter(alItemMaster);
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
        if (alItemMaster.size() != 0 && alItemMaster != null) {
            searchText = newText;
            final ArrayList<ItemMaster> filteredList = Filter(alItemMaster, newText);
            categoryItemAdapter.SetSearchFilter(filteredList);
        }
        return false;
    }

    private ArrayList<ItemMaster> Filter(ArrayList<ItemMaster> lstItemMaster, String filterName) {
        filterName = filterName.toLowerCase();
        final ArrayList<ItemMaster> filteredList = new ArrayList<>();
        for (ItemMaster objItemMaster : lstItemMaster) {
            isFilter = false;
            String[] strArray = objItemMaster.getItemName().split(" ");
            for (String aStrArray : strArray) {
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

    @Override
    public void ButtonOnClick(ItemMaster objItemMaster) {
        //counter = counter + 1;
        //txtCartNumber.setText(String.valueOf(counter));
        AddItemQtyDialogFragment addItemQtyDialogFragment = new AddItemQtyDialogFragment(objItemMaster);
        addItemQtyDialogFragment.setTargetFragment(this, 0);
        addItemQtyDialogFragment.show(getFragmentManager(), "");
    }

    @Override
    public void CardViewOnClick(ItemMaster objItemMaster) {

        objCartIconListener = (CartIconListener) CategoryItemFragment.targetFragment;
        objCartIconListener.CardViewOnClick(objItemMaster);

    }

    @Override
    public void AddToCart(boolean isAddToCart, ItemMaster objOrderItemTran, ArrayList<ItemMaster> alOrderItemModifierTran) {
        if (isAddToCart) {
            if (objOrderItemTran.getItemName() != null) {
                Globals.counter = Globals.counter + 1;
                SetCartNumber(txtCartNumber);
                objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);
                Globals.alOrderItemTran.add(objOrderItemTran);
            }
        }
    }

    private void SetCartNumber(TextView txtCartNumber){
        if (Globals.counter > 0) {
            txtCartNumber.setText(String.valueOf(Globals.counter));
            txtCartNumber.setSoundEffectsEnabled(true);
            txtCartNumber.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.cart_number));
            txtCartNumber.setAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
        } else {
            txtCartNumber.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        }
    }

    interface CartIconListener {
        void CartIconOnClick();

        void CardViewOnClick(ItemMaster objItemMaster);
    }

    public class GuestHomeItemLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());

            if (objCategoryMaster.getCategoryName().equals("All") && cnt == 0) {
                progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ItemJSONParser objItemJSONParser = new ItemJSONParser();
            return objItemJSONParser.SelectAllItemMaster(currentPage, counterMasterId, objCategoryMaster.getCategoryMasterId(), itemTypeMasterId);

        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (objCategoryMaster.getCategoryName().equals("All") && cnt == 0) {
                progressDialog.dismiss();
            }

            ArrayList<ItemMaster> lstItemMaster = (ArrayList<ItemMaster>) result;
            if (lstItemMaster == null) {
                Globals.SetError(txtMsg, rvItem, getResources().getString(R.string.MsgSelectFail), true);
            } else if (lstItemMaster.size() == 0) {
                Globals.SetError(txtMsg, rvItem, getResources().getString(R.string.MsgNoRecord), true);
            } else {
                Globals.SetError(txtMsg, rvItem, null, false);
                alItemMaster = lstItemMaster;
                SetupRecyclerView();
                cnt = 1;
            }
        }
    }

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
