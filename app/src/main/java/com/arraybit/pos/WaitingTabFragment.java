package com.arraybit.pos;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.WaitingListAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.WaitingMaster;
import com.arraybit.modal.WaitingStatusMaster;
import com.arraybit.parser.WaitingJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


@SuppressWarnings("unchecked")
public class WaitingTabFragment extends Fragment implements WaitingListAdapter.childLayoutClickListener, WaitingStatusFragment.UpdateStatusListener {

    public final static String ITEMS_COUNT_KEY = "WaitingTabFragment$ItemsCount";
    RecyclerView rvWaiting;
    TextView txtMsg;
    ArrayList<WaitingMaster> alWaitingMaster;
    LinearLayoutManager linearLayoutManager;
    int currentPage = 1, position;
    WaitingListAdapter waitingListAdapter;
    WaitingStatusMaster objWaitingStatusMaster;

    public WaitingTabFragment() {
    }

    public static WaitingTabFragment createInstance(WaitingStatusMaster objStatusMaster) {
        WaitingTabFragment waitingTabFragment = new WaitingTabFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEMS_COUNT_KEY, objStatusMaster);
        waitingTabFragment.setArguments(bundle);
        return waitingTabFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_tab, container, false);

        rvWaiting = (RecyclerView) view.findViewById(R.id.rvWaiting);
        rvWaiting.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);

        return view;
    }

    private void setupRecyclerView(RecyclerView rvWaiting) {
        waitingListAdapter = new WaitingListAdapter(getActivity(), alWaitingMaster, this);
        rvWaiting.setAdapter(waitingListAdapter);
        rvWaiting.setLayoutManager(linearLayoutManager);

    }

    public void LoadWaitingListData() {

        Bundle bundle = getArguments();
        objWaitingStatusMaster = bundle.getParcelable(ITEMS_COUNT_KEY);
        alWaitingMaster = new ArrayList<>();

        new WaitingMasterLoadingTask().execute();
        rvWaiting.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        new WaitingMasterLoadingTask().execute();
                    }
                }
            }
        });
    }

    @Override
    public void ChangeStatusClick(WaitingMaster objWaitingMaster, int position) {
        this.position = position;
        WaitingStatusFragment waitingStatusFragment = new WaitingStatusFragment(objWaitingMaster);
        waitingStatusFragment.setTargetFragment(this, 0);
        waitingStatusFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void UpdateStatus(boolean flag) {
        if (flag) {
            waitingListAdapter.WaitingListDataRemove(this.position);
        }
    }

    //region LoadingTask
    @SuppressWarnings("ResourceType")
    class WaitingMasterLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (currentPage > 2 && alWaitingMaster.size() != 0) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            WaitingJSONParser objWaitingJSONParser = new WaitingJSONParser();
            if (linearLayoutManager.canScrollVertically() && alWaitingMaster.size() == 0) {
                currentPage = 1;
            }
            return objWaitingJSONParser.SelectAllWaitingMasterByWaitingStatusMasterId(currentPage, objWaitingStatusMaster.getWaitingStatusMasterId());
        }

        @Override
        protected void onPostExecute(Object result) {
            if (currentPage > 2) {
                progressDialog.dismiss();
            }
            ArrayList<WaitingMaster> lstWaitingMaster = (ArrayList<WaitingMaster>) result;
            if (lstWaitingMaster == null) {
                if (currentPage == 1) {
                    Globals.SetError(txtMsg, rvWaiting, getResources().getString(R.string.MsgSelectFail), true);
                }
            } else if (lstWaitingMaster.size() == 0) {
                if (currentPage == 1) {
                    Globals.SetError(txtMsg, rvWaiting, getResources().getString(R.string.MsgNoRecord), true);
                }
            } else {
                if (currentPage > 1) {
                    waitingListAdapter.WaitingListDataChanged(lstWaitingMaster);
                    return;
                } else if (lstWaitingMaster.size() < 10) {
                    currentPage += 1;
                }

                Globals.SetError(txtMsg, rvWaiting, null, false);
                alWaitingMaster = lstWaitingMaster;
                setupRecyclerView(rvWaiting);
            }
        }
    }
    //endregion
}


