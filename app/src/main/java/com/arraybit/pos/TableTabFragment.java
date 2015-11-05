package com.arraybit.pos;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arraybit.adapter.TablesAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.SectionMaster;
import com.arraybit.modal.TableMaster;
import com.arraybit.parser.TableJSONParser;

import java.util.ArrayList;


@SuppressWarnings("unchecked")
public class TableTabFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "TableTabFragment$ItemsCount";
    RecyclerView rvTables;
    TablesAdapter tablesAdapter;
    ArrayList<TableMaster> alTableMaster;
    SectionMaster objSectionMaster;
    GridLayoutManager gridLayoutManager;
    int currentPage = 1, sectionMasterId;
    String tableStatusMasterId = null;
    TextView txtMsg;

    public TableTabFragment() {
        // Required empty public constructor
    }

    public static TableTabFragment createInstance(ArrayList<TableMaster> alTableMaster) {

        TableTabFragment tableTabFragment = new TableTabFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ITEMS_COUNT_KEY, alTableMaster);
        tableTabFragment.setArguments(bundle);
        return tableTabFragment;
    }

    public static TableTabFragment createInstance(SectionMaster objSectionMaster) {

        TableTabFragment tableTabFragment = new TableTabFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEMS_COUNT_KEY, objSectionMaster);
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

        Bundle bundle = getArguments();
        objSectionMaster = bundle.getParcelable(ITEMS_COUNT_KEY);
        this.sectionMasterId = objSectionMaster.getSectionMasterId();
        alTableMaster = new ArrayList<>();

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        new TableMasterLoadingTask().execute();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rvTables.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        new TableMasterLoadingTask().execute();
                    }
                }
            }
        });
    }

    private void SetupRecyclerView(RecyclerView rvTables) {

        tablesAdapter = new TablesAdapter(getActivity(), alTableMaster);
        rvTables.setAdapter(tablesAdapter);
        rvTables.setLayoutManager(gridLayoutManager);

    }

    public void TableDataFilter(int sectionMasterId, String tableStatusMasterId) {
        this.tableStatusMasterId = tableStatusMasterId;
        this.sectionMasterId = sectionMasterId;
        alTableMaster = new ArrayList<>();
        new TableMasterLoadingTask().execute();

        rvTables.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        new TableMasterLoadingTask().execute();
                    }
                }
            }
        });
    }

    @SuppressWarnings("ResourceType")
    class TableMasterLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;
        boolean isShow;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(currentPage > 2 && alTableMaster.size() != 0) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TableJSONParser objTableJSONParser = new TableJSONParser();
            if (gridLayoutManager.canScrollVertically() && alTableMaster.size() == 0) {
                currentPage = 1;
            }
            return objTableJSONParser.SelectAllTableMasterBySectionMasterId(currentPage, sectionMasterId, tableStatusMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {

            if(currentPage > 2) {
                progressDialog.dismiss();
            }

            ArrayList<TableMaster> lstTableMaster = (ArrayList<TableMaster>) result;
            if (lstTableMaster == null) {
                if (currentPage == 1) {
                    Globals.SetError(txtMsg, rvTables, getResources().getString(R.string.MsgSelectFail), true);
                }
            } else if (lstTableMaster.size() == 0) {
                if (currentPage == 1) {
                    Globals.SetError(txtMsg, rvTables, getResources().getString(R.string.MsgNoRecord), true);
                }
            } else {
                if (currentPage > 1) {
                    tablesAdapter.TableDataChanged(lstTableMaster);
                    return;
                } else if (lstTableMaster.size() < 10) {
                    currentPage += 1;
                }

                Globals.SetError(txtMsg, rvTables, null, false);
                alTableMaster = lstTableMaster;
                SetupRecyclerView(rvTables);
            }
        }
    }
}

