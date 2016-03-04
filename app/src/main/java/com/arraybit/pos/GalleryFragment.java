package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.GalleryAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessGalleryTran;
import com.arraybit.parser.BusinessGalleryJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
@SuppressLint("ValidFragment")
public class GalleryFragment extends Fragment {

    static ArrayList<BusinessGalleryTran> alBusinessGalleryTran;
    RecyclerView rvGallery;
    TextView txtMsg;
    int BusinessMasterId;
    GalleryAdapter adapter;
    GridLayoutManager gridLayoutManager;

    @SuppressLint("ValidFragment")
    public GalleryFragment(int BusinessMasterId) {
        this.BusinessMasterId = BusinessMasterId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);

        rvGallery = (RecyclerView) view.findViewById(R.id.rvGallery);
        rvGallery.setVisibility(View.GONE);

        if (alBusinessGalleryTran == null) {
            if (Service.CheckNet(getActivity())) {
                new GalleryLoadingTask().execute();
            } else {
                Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
        } else if (alBusinessGalleryTran.size() == 0) {
            txtMsg.setText(getResources().getString(R.string.MsgGallery));
        } else {
            SetGalleryRecyclerView(alBusinessGalleryTran);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void SetGalleryRecyclerView(ArrayList<BusinessGalleryTran> lstBusinessGalleryTran) {
        rvGallery.setVisibility(View.VISIBLE);
        txtMsg.setVisibility(View.GONE);
        adapter = new GalleryAdapter(getActivity(), lstBusinessGalleryTran, getActivity().getSupportFragmentManager(), false);
        rvGallery.setAdapter(adapter);
        rvGallery.setLayoutManager(gridLayoutManager);
        rvGallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!adapter.isItemAnimate) {
                    adapter.isItemAnimate = true;
                }
            }
        });
    }


    //region LoadingTask
    class GalleryLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

        }

        @Override
        protected Object doInBackground(Object[] params) {
            BusinessGalleryJSONParser objBusinessGalleryJSONParser = new BusinessGalleryJSONParser();
            if (BusinessMasterId == 0) {
                return null;
            } else {
                return objBusinessGalleryJSONParser.SelectAllBusinessGalleryTranPageWise(BusinessMasterId);
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            ArrayList<BusinessGalleryTran> lstBusinessGalleryTran = (ArrayList<BusinessGalleryTran>) result;
            alBusinessGalleryTran = lstBusinessGalleryTran;
            if (lstBusinessGalleryTran == null) {
                txtMsg.setText(getResources().getString(R.string.MsgSelectFail));
            } else if (lstBusinessGalleryTran.size() == 0) {
                txtMsg.setText(getResources().getString(R.string.MsgGallery));
            } else {
                SetGalleryRecyclerView(alBusinessGalleryTran);
            }

        }
    }
    //endregion
}
