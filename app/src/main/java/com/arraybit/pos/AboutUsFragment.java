package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.rey.material.widget.TextView;

@SuppressWarnings("ConstantConditions")
public class AboutUsFragment extends Fragment {

    CardView cardPolicy, cardTerms;

    public AboutUsFragment() {
    }


    @SuppressLint({"SetJavaScriptEnabled", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_about_us));

        setHasOptionsMenu(true);

        cardTerms = (CardView) view.findViewById(R.id.cardTerms);
        cardPolicy = (CardView) view.findViewById(R.id.cardPolicy);

        TextView txtCardPolicy = (TextView) view.findViewById(R.id.txtCardPolicy);
        TextView txtCardTerms = (TextView) view.findViewById(R.id.txtCardTerms);
        TextView txtVersionCode = (TextView) view.findViewById(R.id.txtVersionCode);

        txtVersionCode.setText(getResources().getString(R.string.abVersionCode) + "  " + BuildConfig.VERSION_CODE + "\n" +
                getResources().getString(R.string.abVersionName) + " " + BuildConfig.VERSION_NAME);

        txtCardPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.ReplaceFragment(new PolicyFragment((short) 1), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_policy));
            }
        });

        txtCardTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.ReplaceFragment(new PolicyFragment((short) 1), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_policy));
            }
        });

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
