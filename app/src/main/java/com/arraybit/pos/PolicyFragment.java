package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessDescription;
import com.arraybit.parser.BusinessDescriptionJSONParser;

@SuppressLint("ValidFragment")
@SuppressWarnings({"ConstantConditions", "unchecked"})
public class PolicyFragment extends Fragment {

    short value;
    WebView wvPolicy;

    public PolicyFragment(short value) {
        this.value = value;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_policy, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(Build.VERSION.SDK_INT >=21){
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        if (value == 1) {
            app_bar.setTitle(getResources().getString(R.string.title_fragment_policy));
        } else {
            app_bar.setTitle(getResources().getString(R.string.title_fragment_terms_service));
        }
        setHasOptionsMenu(true);

        wvPolicy = (WebView) view.findViewById(R.id.wvPolicy);
        wvPolicy.getSettings().setJavaScriptEnabled(true);
        wvPolicy.getSettings().setDatabaseEnabled(true);
        wvPolicy.getSettings().setDomStorageEnabled(true);
        wvPolicy.getSettings().setAppCacheEnabled(true);
        wvPolicy.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvPolicy.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //wvPolicy.loadUrl("file:///android_asset/privacy_policy.html");

        if (Service.CheckNet(getActivity())) {
            new DescriptionLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            menu.findItem(R.id.mWaiting).setVisible(false);
        }
        if(Globals.isWishListShow==0 && !GuestHomeActivity.isMenuMode) {
            menu.findItem(R.id.logout).setVisible(false);
        }else if(Globals.isWishListShow==1){
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.registration).setVisible(false);
            menu.findItem(R.id.shortList).setVisible(false);
        }
    }

    //region Loading Task
    class DescriptionLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            BusinessDescriptionJSONParser objBusinessDescriptionJSONParser = new BusinessDescriptionJSONParser();
            if(value==1){
                return objBusinessDescriptionJSONParser.SelectBusinessDescription(getActivity(), String.valueOf(Globals.businessMasterId),Globals.PrivacyPolicy);
            }else{
                return objBusinessDescriptionJSONParser.SelectBusinessDescription(getActivity(), String.valueOf(Globals.businessMasterId),Globals.TermsOfService);
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            BusinessDescription objBusinessDescription = (BusinessDescription) result;
            if(objBusinessDescription!=null && objBusinessDescription.getDescription()!=null && !objBusinessDescription.getDescription().equals("")){
                wvPolicy.setVisibility(View.VISIBLE);
                wvPolicy.loadData(objBusinessDescription.getDescription(), "text/html; charset=UTF-8", null);
            }
        }
    }
    //endregion
}
