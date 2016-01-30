package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class AboutUsFragment extends Fragment {

    short value;

    public AboutUsFragment(short value) {
        this.value = value;
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        WebView wvPolicy = (WebView) view.findViewById(R.id.wvPolicy);
        wvPolicy.getSettings().setJavaScriptEnabled(true);
        wvPolicy.getSettings().setDatabaseEnabled(true);
        wvPolicy.getSettings().setDomStorageEnabled(true);
        wvPolicy.getSettings().setAppCacheEnabled(true);
        wvPolicy.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvPolicy.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        if (value == 1) {
            wvPolicy.loadUrl("file:///android_asset/about_us.html");
        } else {
            wvPolicy.loadUrl("file:///android_asset/privacy_policy.html");
        }
        return view;
    }
}
