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

import com.arraybit.adapter.TablesAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Service;
import com.arraybit.modal.TableMaster;
import com.arraybit.parser.TableJSONParser;

import java.util.ArrayList;


public class TableTabFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "TableTabFragment$ItemsCount";
    RecyclerView rvTables;
    TablesAdapter tablesAdapter;
    ArrayList<TableMaster> alTableMaster;
    GridLayoutManager gridLayoutManager;
    int currentPage;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_tab, container, false);
        rvTables = (RecyclerView) view.findViewById(R.id.rvTables);

        Bundle bundle = getArguments();
        alTableMaster = bundle.getParcelableArrayList(ITEMS_COUNT_KEY);

        gridLayoutManager=new GridLayoutManager(getActivity(),2);

        setupRecyclerView(rvTables);
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

    private void setupRecyclerView(RecyclerView rvTables) {

        tablesAdapter = new TablesAdapter(getActivity(),alTableMaster);
        rvTables.setAdapter(tablesAdapter);
        rvTables.setLayoutManager(gridLayoutManager);
        if(rvTables.getAdapter().getItemCount()>0)
        {
            rvTables.setId((int) alTableMaster.get(0).getlinktoSectionMasterId());
        }
    }

    @SuppressWarnings("ResourceType")
    class TableMasterLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(currentPage > 2) {
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
            alTableMaster = objTableJSONParser.SelectAllTableMasterBySectionMasterId(currentPage,rvTables.getId());
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if(currentPage > 2) {
                progressDialog.dismiss();
            }
            if(alTableMaster!=null){
                tablesAdapter.TableDataChanged(alTableMaster);
            }
        }

    }
}
