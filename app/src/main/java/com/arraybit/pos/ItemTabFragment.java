package com.arraybit.pos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.CategoryItemAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class ItemTabFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "ItemTabFragment$ItemsCount";
    TextView txtMsg;
    RecyclerView rvItem;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    ArrayList<ItemMaster> alItemMaster;
    String itemTypeMasterId = null;
    CategoryMaster objCategoryMaster;
    CategoryItemAdapter categoryItemAdapter;

    int currentPage = 1;

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

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);

        Bundle bundle = getArguments();
        objCategoryMaster = bundle.getParcelable(ITEMS_COUNT_KEY);
        alItemMaster = new ArrayList<>();

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
            return objItemJSONParser.SelectAllItemMaster(currentPage, objCategoryMaster.getCategoryMasterId(), itemTypeMasterId);
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
