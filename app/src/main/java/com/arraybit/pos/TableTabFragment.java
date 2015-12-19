package com.arraybit.pos;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.TextView;

import com.arraybit.adapter.TablesAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.SectionMaster;
import com.arraybit.modal.TableMaster;
import com.arraybit.parser.TableJSONParser;

import java.util.ArrayList;


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
    Context context;

    public TableTabFragment() {
        // Required empty public constructor
    }

    public static TableTabFragment createInstance(SectionMaster objSectionMaster, boolean isChangeMode) {

        TableTabFragment tableTabFragment = new TableTabFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEMS_COUNT_KEY, objSectionMaster);
        bundle.putBoolean("ChangeMode", isChangeMode);
        tableTabFragment.setArguments(bundle);
        return tableTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_tab, container, false);

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);

        rvTables = (RecyclerView) view.findViewById(R.id.rvTables);
        rvTables.setVisibility(View.GONE);

        displayMetrics = getActivity().getResources().getDisplayMetrics();

        if (!getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            setHasOptionsMenu(true);
        }

        bundle = getArguments();
        objSectionMaster = bundle.getParcelable(ITEMS_COUNT_KEY);
        this.sectionMasterId = objSectionMaster.getSectionMasterId();

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiter_home))) {
            MenuItem searchItem = menu.findItem(R.id.action_search);
            final SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            mSearchView.setInputType(InputType.TYPE_CLASS_TEXT);
            mSearchView.setMaxWidth(displayMetrics.widthPixels);
            mSearchView.setOnQueryTextListener(this);

            MenuItemCompat.setOnActionExpandListener(searchItem,
                    new MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            // Do something when collapsed
                            if(alTableMaster.size()!=0 && alTableMaster!=null) {
                                tablesAdapter.SetSearchFilter(alTableMaster);
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

    private ArrayList<TableMaster> Filter(ArrayList<TableMaster> lstTableMaster, String filterName) {
        filterName = filterName.toLowerCase();
        final ArrayList<TableMaster> filteredList = new ArrayList<>();
        for (TableMaster objTableMaster : lstTableMaster) {
            isFilter = false;
            String[] strArray = objTableMaster.getTableName().split(" ");
            for (String str : strArray) {
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

    private void SetupRecyclerView(RecyclerView rvTables) {
        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            tablesAdapter = new TablesAdapter(getActivity(), alTableMaster, false, null);
        } else {
            tablesAdapter = new TablesAdapter(getActivity(), alTableMaster, true, this);
        }
        rvTables.setAdapter(tablesAdapter);
        rvTables.setLayoutManager(gridLayoutManager);
    }

    public void LoadTableData(String tableStatusMasterId) {
        alTableMaster = new ArrayList<>();
        this.tableStatusMasterId = tableStatusMasterId;
        new TableMasterLoadingTask().execute();
    }

    public void TableDataFilter(int sectionMasterId, String tableStatusMasterId) {
        this.tableStatusMasterId = tableStatusMasterId;
        this.sectionMasterId = sectionMasterId;
        alTableMaster = new ArrayList<>();

        new TableMasterLoadingTask().execute();
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

    @Override
    public void ChangeTableStatusClick(TableMaster objTableMaster, int position) {
        this.position = position;
        if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Vacant.toString())) {
            if (bundle.getBoolean("ChangeMode")) {
                Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
                intent.putExtra("TableMasterId",objTableMaster.getTableMasterId());
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(),MenuActivity.class);
                intent.putExtra("TableMasterId",objTableMaster.getTableMasterId());
                startActivity(intent);
            }
        } else if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Occupied.toString())) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.allTablesFragment, new AllOrdersFragment(String.valueOf(objTableMaster.getTableMasterId())), getActivity().getResources().getString(R.string.title_fragment_all_orders));
            fragmentTransaction.addToBackStack(getActivity().getResources().getString(R.string.title_fragment_all_orders));
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

    class TableMasterLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TableJSONParser objTableJSONParser = new TableJSONParser();
            return objTableJSONParser.SelectAllTableMasterBySectionMasterId(counterMasterId, sectionMasterId, tableStatusMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            ArrayList<TableMaster> lstTableMaster = (ArrayList<TableMaster>) result;
            if (lstTableMaster == null) {
                Globals.SetError(txtMsg, rvTables, getActivity().getResources().getString(R.string.MsgSelectFail), true);
            } else if (lstTableMaster.size() == 0) {
                Globals.SetError(txtMsg, rvTables, getActivity().getResources().getString(R.string.MsgNoRecord), true);
            } else {
                Globals.SetError(txtMsg, rvTables, null, false);
                alTableMaster = lstTableMaster;
                SetupRecyclerView(rvTables);
            }
        }
    }
}

