package com.arraybit.pos;

import android.annotation.SuppressLint;
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

import com.arraybit.adapter.GalleryAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessGalleryTran;
import com.arraybit.parser.BusinessGalleryJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

@SuppressWarnings("unchecked")
@SuppressLint("ValidFragment")
public class GalleryFragment extends Fragment {

    static ArrayList<BusinessGalleryTran> alBusinessGalleryTran;
    RecyclerView rvGallery;
    TextView txtMsg;
    int currentPage = 1, BusinessMasterId;
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

        gridLayoutManager = new GridLayoutManager(getActivity(),2);

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

        rvGallery.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        new GalleryLoadingTask().execute();
                    } else {
                        Globals.ShowSnackBar(rvGallery, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void SetGalleryRecyclerView(ArrayList<BusinessGalleryTran> lstBusinessGalleryTran) {
        rvGallery.setVisibility(View.VISIBLE);
        txtMsg.setVisibility(View.GONE);
        adapter = new GalleryAdapter(getActivity(), lstBusinessGalleryTran,getActivity().getSupportFragmentManager());
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        rvGallery.setAdapter(scaleInAnimationAdapter);
        rvGallery.setLayoutManager(gridLayoutManager);
    }


    //region LoadingTask
    class GalleryLoadingTask extends AsyncTask {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (currentPage > 1) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);

                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            BusinessGalleryJSONParser objBusinessGalleryJSONParser = new BusinessGalleryJSONParser();
            if (BusinessMasterId == 0) {
                return null;
            } else {
                return objBusinessGalleryJSONParser.SelectAllBusinessGalleryTranPageWise(currentPage, BusinessMasterId);
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (currentPage > 1) {
                progressDialog.dismiss();
            }
            ArrayList<BusinessGalleryTran> lstBusinessGalleryTran = (ArrayList<BusinessGalleryTran>) result;
            alBusinessGalleryTran = lstBusinessGalleryTran;
            if (lstBusinessGalleryTran == null) {
                if (currentPage == 1) {
                    txtMsg.setText(getResources().getString(R.string.MsgSelectFail));
                }
            } else {
                if (lstBusinessGalleryTran.size() == 0) {
                    if (currentPage == 1) {
                        txtMsg.setText(getResources().getString(R.string.MsgGallery));
                    }
                } else {
                    SetGalleryRecyclerView(lstBusinessGalleryTran);
                }
            }
        }
    }
    //endregion
}
