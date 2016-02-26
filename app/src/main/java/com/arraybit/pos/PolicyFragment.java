package com.arraybit.pos;


import android.annotation.SuppressLint;
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

@SuppressWarnings("ConstantConditions")
public class PolicyFragment extends Fragment {

    short value;

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
            app_bar.setTitle(getResources().getString(R.string.title_fragment_policy));
        }
        setHasOptionsMenu(true);

        WebView wvPolicy = (WebView) view.findViewById(R.id.wvPolicy);
        wvPolicy.getSettings().setJavaScriptEnabled(true);
        wvPolicy.getSettings().setDatabaseEnabled(true);
        wvPolicy.getSettings().setDomStorageEnabled(true);
        wvPolicy.getSettings().setAppCacheEnabled(true);
        wvPolicy.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvPolicy.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        wvPolicy.loadUrl("file:///android_asset/privacy_policy.html");

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
    }
}
