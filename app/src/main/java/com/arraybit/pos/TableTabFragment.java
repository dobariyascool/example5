package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.TablesAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.SectionMaster;
import com.arraybit.modal.TableMaster;
import com.arraybit.parser.TableJSONParser;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


@SuppressWarnings({"unchecked", "ConstantConditions"})
public class TableTabFragment extends Fragment implements SearchView.OnQueryTextListener, TablesAdapter.LayoutClickListener, TableStatusFragment.UpdateTableStatusListener {

    public final static String ITEMS_COUNT_KEY = "TableTabFragment$ItemsCount";
    RecyclerView rvTables;
    TablesAdapter tablesAdapter;
    ArrayList<TableMaster> alTableMaster;
    SectionMaster objSectionMaster;
    GridLayoutManager gridLayoutManager;
    int sectionMasterId;
    String tableStatusMasterId = null;
    DisplayMetrics displayMetrics;
    TextView txtMsg;
    SharePreferenceManage objSharePreferenceManage;
    int counterMasterId, position;
    boolean isFilter;
    Bundle bundle;
    String linktoOrderTypeMasterId;
    LinearLayout errorLayout;
    ProgressView progressView;

    public TableTabFragment() {
        // Required empty public constructor
    }

    public static TableTabFragment createInstance(SectionMaster objSectionMaster, boolean isChangeMode, String linktoOrderTypeMasterId) {

        TableTabFragment tableTabFragment = new TableTabFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEMS_COUNT_KEY, objSectionMaster);
        bundle.putBoolean("ChangeMode", isChangeMode);
        bundle.putString("OrderTypeMasterId", linktoOrderTypeMasterId);
        tableTabFragment.setArguments(bundle);
        return tableTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_tab, container, false);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);

        progressView = (ProgressView) view.findViewById(R.id.progressView);

        rvTables = (RecyclerView) view.findViewById(R.id.rvTables);
        rvTables.setHasFixedSize(true);
        rvTables.setVisibility(View.GONE);

        displayMetrics = getActivity().getResources().getDisplayMetrics();

        if (!getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            setHasOptionsMenu(true);
        }

        bundle = getArguments();
        objSectionMaster = bundle.getParcelable(ITEMS_COUNT_KEY);
        this.sectionMasterId = objSectionMaster.getSectionMasterId();
        this.linktoOrderTypeMasterId = bundle.getString("OrderTypeMasterId");

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        //get counterMasterId
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()) != null) {
            counterMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("CounterPreference", "CounterMasterId", getActivity()));
        }
        //end


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            final MenuItem searchItem = menu.findItem(R.id.action_search);
            final SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            mSearchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
            mSearchView.setMaxWidth(displayMetrics.widthPixels);
            mSearchView.setOnQueryTextListener(this);

            MenuItemCompat.setOnActionExpandListener(searchItem,
                    new MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            // Do something when collapsed
                            if (alTableMaster.size() != 0 && alTableMaster != null) {
                                tablesAdapter.SetSearchFilter(alTableMaster);
                                Globals.HideKeyBoard(getActivity(), MenuItemCompat.getActionView(searchItem));
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

    }

    public void LoadTableData(String tableStatusMasterId) {
        alTableMaster = new ArrayList<>();
        this.tableStatusMasterId = tableStatusMasterId;
        if (Service.CheckNet(getActivity())) {
            new TableMasterLoadingTask().execute();
        } else {
            Globals.SetErrorLayout(errorLayout,true,getResources().getString(R.string.MsgCheckConnection),rvTables);
        }

    }

    public void TableDataFilter(String tableStatusMasterId) {
        ArrayList<TableMaster> alTableMasterFilter = new ArrayList<>();
        if (tableStatusMasterId != null) {
            for (int i = 0; i < alTableMaster.size(); i++) {
                if (alTableMaster.get(i).getlinktoTableStatusMasterId() == Short.valueOf(tableStatusMasterId)) {
                    alTableMasterFilter.add(alTableMaster.get(i));
                }
            }
            if (alTableMasterFilter.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvTables);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvTables);
                SetupRecyclerView(rvTables, alTableMasterFilter);
            }
        } else {
            if (alTableMaster.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvTables);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvTables);
                SetupRecyclerView(rvTables, alTableMaster);
            }

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (alTableMaster.size() != 0 && alTableMaster != null) {
            final ArrayList<TableMaster> filteredList = Filter(alTableMaster, newText);
            tablesAdapter.SetSearchFilter(filteredList);
        }
        return false;
    }


    @SuppressLint("RtlHardcoded")
    @Override
    public void ChangeTableStatusClick(TableMaster objTableMaster, int position) {
        this.position = position;
        if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Vacant.toString())) {
            if (bundle.getBoolean("ChangeMode")) {
                Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
                intent.putExtra("TableMaster", objTableMaster);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                if (Globals.selectTableMasterId == 0) {
                    Globals.selectTableMasterId = objTableMaster.getTableMasterId();
                }
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("TableMaster", objTableMaster);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        } else if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Occupied.toString())) {

            AllTablesFragment.isRefresh = true;
            Bundle bundle = new Bundle();
            bundle.putParcelable("TableMaster", objTableMaster);
            bundle.putBoolean("isHomeShow", true);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment();
            orderSummaryFragment.setArguments(bundle);
            if (Build.VERSION.SDK_INT >= 21) {
                Slide slideTransition = new Slide();
                slideTransition.setSlideEdge(Gravity.RIGHT);
                slideTransition.setDuration(500);

                orderSummaryFragment.setEnterTransition(slideTransition);
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
            }

            fragmentTransaction.replace(R.id.allTablesFragment, orderSummaryFragment, getActivity().getResources().getString(R.string.title_fragment_order_summary));
            fragmentTransaction.addToBackStack(getActivity().getResources().getString(R.string.title_fragment_order_summary));
            fragmentTransaction.commit();

        } else if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Block.toString())) {
            TableStatusFragment tableStatusFragment = new TableStatusFragment(objTableMaster);
            tableStatusFragment.setTargetFragment(this, 0);
            tableStatusFragment.show(getFragmentManager(), "");
        } else if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Dirty.toString())) {
            TableStatusFragment tableStatusFragment = new TableStatusFragment(objTableMaster);
            tableStatusFragment.setTargetFragment(this, 0);
            tableStatusFragment.show(getFragmentManager(), "");
        }
    }

    @Override
    public void UpdateTableStatus(boolean flag, TableMaster objTableMaster) {
        if (flag) {
            tablesAdapter.UpdateData(position, objTableMaster);
        }
    }

    //region Private Methods and Interface
    private ArrayList<TableMaster> Filter(ArrayList<TableMaster> lstTableMaster, String filterName) {
        filterName = filterName.toLowerCase();
        final ArrayList<TableMaster> filteredList = new ArrayList<>();
        for (TableMaster objTableMaster : lstTableMaster) {
            isFilter = false;
            ArrayList<String> alString = new ArrayList<>(Arrays.asList(objTableMaster.getShortName().toLowerCase().split(" ")));
            alString.add(0, objTableMaster.getShortName().toLowerCase());
            for (String str : alString) {
                if (str.length() >= filterName.length()) {
                    final String strItem = str.substring(0, filterName.length()).toLowerCase();
                    if (!isFilter) {
                        if (strItem.contains(filterName)) {
                            filteredList.add(objTableMaster);
                            isFilter = true;
                        }
                    }
                }
            }
        }
        return filteredList;
    }

    private void SetupRecyclerView(RecyclerView rvTables, ArrayList<TableMaster> alTableMaster) {
        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            if (objSectionMaster.getSectionMasterId() == 0) {
                tablesAdapter = new TablesAdapter(getActivity(), alTableMaster, false, null, getActivity().getSupportFragmentManager(), true, false);
            } else {
                tablesAdapter = new TablesAdapter(getActivity(), alTableMaster, false, null, getActivity().getSupportFragmentManager(), false, false);
            }

        } else {
            if (objSectionMaster.getSectionMasterId() == 0) {
                tablesAdapter = new TablesAdapter(getActivity(), alTableMaster, true, this, getActivity().getSupportFragmentManager(), true, false);
            } else {
                tablesAdapter = new TablesAdapter(getActivity(), alTableMaster, true, this, getActivity().getSupportFragmentManager(), false, false);
            }

        }

        rvTables.setAdapter(tablesAdapter);
        rvTables.setLayoutManager(gridLayoutManager);
        rvTables.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Globals.HideKeyBoard(getActivity(), recyclerView);
                if (!tablesAdapter.isItemAnimate) {
                    tablesAdapter.isItemAnimate = true;
                }
            }
        });
    }
    //endregion

    //region LoadingTask
    class TableMasterLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TableJSONParser objTableJSONParser = new TableJSONParser();
            return objTableJSONParser.SelectAllTableMasterBySectionMasterId(counterMasterId, tableStatusMasterId, linktoOrderTypeMasterId,Globals.businessMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            ArrayList<TableMaster> lstTableMaster = (ArrayList<TableMaster>) result;
            if (lstTableMaster == null) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgSelectFail), rvTables);
            } else if (lstTableMaster.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvTables);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvTables);
                alTableMaster = lstTableMaster;
                SetupRecyclerView(rvTables, alTableMaster);
            }
        }
    }
    //endregion
}

