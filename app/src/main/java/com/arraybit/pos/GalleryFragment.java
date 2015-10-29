package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arraybit.adapter.GalleryAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessGalleryTran;
import com.arraybit.parser.BusinessGalleryJSONParser;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class GalleryFragment extends Fragment {

    static ArrayList<BusinessGalleryTran> alBusinessGalleryTran;
    RecyclerView rvGallery;
    TextView txtMsg;
    LinearLayoutManager linearLayoutManager;
    int currentPage = 1, BusinessMasterId;
    GalleryAdapter adapter;

    @SuppressLint("ValidFragment")
    public GalleryFragment(int BusinessMasterId) {
        this.BusinessMasterId = BusinessMasterId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvGallery = (RecyclerView) view.findViewById(R.id.rvGallery);
        rvGallery.setVisibility(View.GONE);

        rvGallery.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
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
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        rvGallery.setItemAnimator(animator);
        rvGallery.setAdapter(adapter);
        rvGallery.setLayoutManager(linearLayoutManager);
    }

    public class GalleryLoadingTask extends AsyncTask {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);

            progressDialog.show();
            SignInActivity.ServerName="10.0.3.2:6122";
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

            progressDialog.dismiss();
            ArrayList<BusinessGalleryTran> lstBusinessGalleryTran = (ArrayList<BusinessGalleryTran>) result;
            if (currentPage == 1) {
                Toast.makeText(getActivity(), getResources().getString(R.string.MsgSelectFail), Toast.LENGTH_LONG).show();
            } else if (lstBusinessGalleryTran.size() == 0) {
                if (currentPage == 1) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.MsgNoRecord), Toast.LENGTH_LONG).show();
                    alBusinessGalleryTran = lstBusinessGalleryTran;
                    return;
                }
            } else {
                SetGalleryRecyclerView(lstBusinessGalleryTran);
            }
        }
    }
}
