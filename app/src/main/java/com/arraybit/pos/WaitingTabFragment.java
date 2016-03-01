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
import android.widget.LinearLayout;

import com.arraybit.adapter.WaitingListAdapter;
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
    int position;
    WaitingListAdapter waitingListAdapter;
    WaitingStatusMaster objWaitingStatusMaster;
    LinearLayout errorLayout;

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

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);

        rvWaiting = (RecyclerView) view.findViewById(R.id.rvWaiting);
        rvWaiting.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        return view;
    }

    public void LoadWaitingListData() {

        Bundle bundle = getArguments();
        objWaitingStatusMaster = bundle.getParcelable(ITEMS_COUNT_KEY);
        alWaitingMaster = new ArrayList<>();

        if (Service.CheckNet(getActivity())) {
            new WaitingMasterLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(rvWaiting, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

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

    //region Private Methods
    private void SetupRecyclerView(RecyclerView rvWaiting) {
        waitingListAdapter = new WaitingListAdapter(getActivity(), alWaitingMaster, this, false);
        rvWaiting.setAdapter(waitingListAdapter);
        rvWaiting.setLayoutManager(linearLayoutManager);
        rvWaiting.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!waitingListAdapter.isItemAnimate) {
                    waitingListAdapter.isItemAnimate = true;
                }
            }
        });
    }
    //endregion

    //region LoadingTask
    @SuppressWarnings("ResourceType")
    class WaitingMasterLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            WaitingJSONParser objWaitingJSONParser = new WaitingJSONParser();
            return objWaitingJSONParser.SelectAllWaitingMasterByWaitingStatusMasterId(objWaitingStatusMaster.getWaitingStatusMasterId());
        }

        @Override
        protected void onPostExecute(Object result) {
            ArrayList<WaitingMaster> lstWaitingMaster = (ArrayList<WaitingMaster>) result;
            if (lstWaitingMaster == null) {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvWaiting);
            } else if (lstWaitingMaster.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvWaiting);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvWaiting);
                alWaitingMaster = lstWaitingMaster;
                SetupRecyclerView(rvWaiting);
            }
        }
    }
    //endregion
}


