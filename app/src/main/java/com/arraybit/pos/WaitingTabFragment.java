package com.arraybit.pos;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.WaitingListAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.modal.WaitingMaster;
import com.arraybit.parser.WaitingJSONParser;

import java.util.ArrayList;


public class WaitingTabFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "WaitingTabFragment$ItemsCount";
    RecyclerView rvWaiting;
    ArrayList<WaitingMaster> alWaitingMaster;
    LinearLayoutManager linearLayoutManager;
    int currentPage = 1;
    WaitingListAdapter waitingListAdapter;


    public WaitingTabFragment() {
    }

    public static WaitingTabFragment createInstance(ArrayList<WaitingMaster> alWaitingMaster) {

        WaitingTabFragment waitingTabFragment = new WaitingTabFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ITEMS_COUNT_KEY, alWaitingMaster);
        waitingTabFragment.setArguments(bundle);
        return waitingTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_tab, container, false);

        rvWaiting = (RecyclerView) view.findViewById(R.id.rvWaiting);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        Bundle bundle = getArguments();
        alWaitingMaster = bundle.getParcelableArrayList(ITEMS_COUNT_KEY);

        setupRecyclerView(rvWaiting);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rvWaiting.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page > currentPage) {
                    currentPage = current_page;
                    new WaitingMasterLoadingTask().execute();
                }
            }
        });
    }

    private void setupRecyclerView(RecyclerView rvWaiting) {
        waitingListAdapter = new WaitingListAdapter(getActivity(),alWaitingMaster);
        rvWaiting.setAdapter(waitingListAdapter);
        rvWaiting.setLayoutManager(linearLayoutManager);
        if(rvWaiting.getAdapter().getItemCount()>0) {
            rvWaiting.setTag(alWaitingMaster.get(0).getlinktoWaitingStatusMasterId());
        }
    }

    class WaitingMasterLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            alWaitingMaster = new ArrayList<>();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            WaitingJSONParser objWaitingJSONParser = new WaitingJSONParser();
            alWaitingMaster = objWaitingJSONParser.SelectAllWaitingMasterByWaitingStatusMasterId(currentPage,1);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            progressDialog.dismiss();
            if(alWaitingMaster!=null)
            {
//                if(alWaitingMaster.size() > 10){
//
//                    currentPage +=1;
//                }
                waitingListAdapter.WaitingListDataChanged(alWaitingMaster);
            }
        }

    }

}
