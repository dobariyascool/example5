package com.arraybit.pos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.CategoryItemAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.modal.CounterMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class ItemTabFragment extends Fragment implements SearchView.OnQueryTextListener {

    public final static String ITEMS_COUNT_KEY = "ItemTabFragment$ItemsCount";
    TextView txtMsg;
    RecyclerView rvItem;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    ArrayList<ItemMaster> alItemMaster;
    String itemTypeMasterId = null;
    SharePreferenceManage objSharePreferenceManage;

    CategoryItemAdapter categoryItemAdapter;
    CategoryMaster objCategoryMaster;
    CounterMaster objCounterMaster;
    int counterMasterId;
    int currentPage = 1;
    ArrayList<ItemMaster> filteredList=new ArrayList<>();

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

        setHasOptionsMenu(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

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

        new GuestHomeItemLoadingTask().execute();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void SetupRecyclerView() {

        categoryItemAdapter = new CategoryItemAdapter(getActivity(), alItemMaster, getFragmentManager(),CategoryItemFragment.isViewChange);
        rvItem.setVisibility(View.VISIBLE);
        rvItem.setAdapter(categoryItemAdapter);
        if (CategoryItemFragment.isViewChange) {
            rvItem.setLayoutManager(gridLayoutManager);
        } else {
            rvItem.setLayoutManager(linearLayoutManager);
        }

        if(CategoryItemFragment.isViewChange) {
            rvItem.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (current_page > currentPage) {
                        currentPage = current_page;
                        if (Service.CheckNet(getActivity())) {
                            new GuestHomeItemLoadingTask().execute();
                        }
                    }
                }
            });
        }
        else
        {
            rvItem.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (current_page > currentPage) {
                        currentPage = current_page;
                        if (Service.CheckNet(getActivity())) {
                            new GuestHomeItemLoadingTask().execute();
                        }
                    }
                }
            });
        }
    }

    public void ItemDataFilter(String itemTypeMasterId) {
        this.itemTypeMasterId = itemTypeMasterId;
        alItemMaster = new ArrayList<>();
        new GuestHomeItemLoadingTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);
       // mSearchView.setQueryHint("Item Name");
        //mSearchView.setMaxWidth(500);
        mSearchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        categoryItemAdapter.SetSearchFilter(alItemMaster);
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
        final ArrayList<ItemMaster> filteredList = Filter(alItemMaster, newText);
        categoryItemAdapter.SetSearchFilter(filteredList);
        return false;
    }

    private ArrayList<ItemMaster> Filter(ArrayList<ItemMaster> lstItemMaster, String filterName) {
        filterName = filterName.toLowerCase();
        final ArrayList<ItemMaster> filteredList = new ArrayList<>();
        for (ItemMaster objItemMaster : lstItemMaster) {
            final String strItem = objItemMaster.getItemName().toLowerCase();
            if (strItem.contains(filterName)) {
                filteredList.add(objItemMaster);
            }
        }
        return filteredList;
    }

    public class GuestHomeItemLoadingTask extends AsyncTask {

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
    }
}
