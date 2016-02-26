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
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.WaitingMaster;
import com.arraybit.modal.WaitingStatusMaster;
import com.arraybit.parser.WaitingJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;


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
    ScaleInAnimationAdapter scaleInAnimationAdapter;

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
        Globals.TextViewFontTypeFace(txtMsg,getActivity());

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
            waitingListAdapter.WaitingListDataRemove(this.position, scaleInAnimationAdapter);
        }
    }

    //region Private Methods
    private void SetupRecyclerView(RecyclerView rvWaiting) {
        waitingListAdapter = new WaitingListAdapter(getActivity(), alWaitingMaster, this);
        scaleInAnimationAdapter = new ScaleInAnimationAdapter(waitingListAdapter);
        rvWaiting.setAdapter(scaleInAnimationAdapter);
        rvWaiting.setLayoutManager(linearLayoutManager);
    }
    //endregion

    //region LoadingTask
    @SuppressWarnings("ResourceType")
    class WaitingMasterLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
//            progressDialog.setIndeterminate(true);
//            progressDialog.setCancelable(false);
//            progressDialog.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            WaitingJSONParser objWaitingJSONParser = new WaitingJSONParser();
            return objWaitingJSONParser.SelectAllWaitingMasterByWaitingStatusMasterId(objWaitingStatusMaster.getWaitingStatusMasterId());
        }

        @Override
        protected void onPostExecute(Object result) {
            //progressDialog.dismiss();
            ArrayList<WaitingMaster> lstWaitingMaster = (ArrayList<WaitingMaster>) result;
            if (lstWaitingMaster == null) {
                Globals.SetError(txtMsg, rvWaiting, getResources().getString(R.string.MsgSelectFail), true);
            } else if (lstWaitingMaster.size() == 0) {
                Globals.SetError(txtMsg, rvWaiting, getResources().getString(R.string.MsgNoRecord), true);
            } else {
                Globals.SetError(txtMsg, rvWaiting, null, false);
                alWaitingMaster = lstWaitingMaster;
                SetupRecyclerView(rvWaiting);
            }
        }
    }
    //endregion
}


