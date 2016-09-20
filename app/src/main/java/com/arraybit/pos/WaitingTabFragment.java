package com.arraybit.pos;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
    Context context;
    FragmentManager fragmentManager;

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

    public void LoadWaitingListData(Context context,FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        Bundle bundle = getArguments();
        objWaitingStatusMaster = bundle.getParcelable(ITEMS_COUNT_KEY);
        alWaitingMaster = new ArrayList<>();

        if (Service.CheckNet(context)) {
            new WaitingMasterLoadingTask().execute();
        } else {
            Globals.SetErrorLayout(errorLayout,true,getResources().getString(R.string.MsgCheckConnection),rvWaiting,R.drawable.wifi_off);
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
        waitingListAdapter = new WaitingListAdapter(context, alWaitingMaster, this, false);
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

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(fragmentManager, "");
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            WaitingJSONParser objWaitingJSONParser = new WaitingJSONParser();
            String OrderBy;
            if(objWaitingStatusMaster.getWaitingStatusMasterId()==0)
            {
                OrderBy="WaitingMasterId";
            }
            else
            {
                OrderBy="UpdateDateTime";
            }
            return objWaitingJSONParser.SelectAllWaitingMasterByWaitingStatusMasterId(objWaitingStatusMaster.getWaitingStatusMasterId(),Globals.businessMasterId,OrderBy);
        }

        @Override
        protected void onPostExecute(Object result) {
            progressDialog.dismiss();
            ArrayList<WaitingMaster> lstWaitingMaster = (ArrayList<WaitingMaster>) result;
            if (lstWaitingMaster == null) {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvWaiting,0);
            } else if (lstWaitingMaster.size() == 0) {
                Globals.SetErrorLayout(errorLayout, true, String.format(getResources().getString(R.string.MsgNoRecordFound), getResources().getString(R.string.MsgWaitingPerson)), rvWaiting, 0);
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvWaiting,0);
                alWaitingMaster = lstWaitingMaster;
                SetupRecyclerView(rvWaiting);
            }
        }
    }
    //endregion
}


