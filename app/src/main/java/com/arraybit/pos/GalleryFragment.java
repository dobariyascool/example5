package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arraybit.adapter.GalleryAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
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

        rvGallery.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        new GalleryLoadingTask().execute();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (alBusinessGalleryTran == null) {
            new GalleryLoadingTask().execute();

        } else if (alBusinessGalleryTran.size() == 0) {
            Toast.makeText(getActivity(), getResources().getString(R.string.MsgNoRecord), Toast.LENGTH_LONG).show();
        } else {
            SetGalleryRecyclerView(alBusinessGalleryTran);
        }
    }

    public void SetGalleryRecyclerView(ArrayList<BusinessGalleryTran> lstBusinessGalleryTran) {
        rvGallery.setVisibility(View.VISIBLE);
        txtMsg.setVisibility(View.GONE);
        adapter = new GalleryAdapter(getActivity(), lstBusinessGalleryTran);
        rvGallery.setAdapter(adapter);
        rvGallery.setLayoutManager(gridLayoutManager);
    }

    @SuppressWarnings("unchecked")
    public class GalleryLoadingTask extends AsyncTask {
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
            if (lstBusinessGalleryTran == null) {
                if (currentPage == 1) {
                    txtMsg.setText(getResources().getString(R.string.MsgSelectFail));
                }
            } else {
                if (lstBusinessGalleryTran.size() == 0) {
                    if (currentPage == 1) {
                        txtMsg.setText(getResources().getString(R.string.MsgSelectFail));
                    }
                } else {
                    SetGalleryRecyclerView(lstBusinessGalleryTran);
                }
            }
        }
    }
}
