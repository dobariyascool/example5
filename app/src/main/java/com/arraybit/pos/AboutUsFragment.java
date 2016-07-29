package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_about_us));

        setHasOptionsMenu(true);

        LinearLayout versionLayout = (LinearLayout) view.findViewById(R.id.versionLayout);

        cardTerms = (CardView) view.findViewById(R.id.cardTerms);
        cardPolicy = (CardView) view.findViewById(R.id.cardPolicy);

        TextView txtCardPolicy = (TextView) view.findViewById(R.id.txtCardPolicy);
        TextView txtCardTerms = (TextView) view.findViewById(R.id.txtCardTerms);
        TextView txtVersionCode = (TextView) view.findViewById(R.id.txtVersionCode);

        if (Build.VERSION.SDK_INT >= 17 && Build.VERSION.SDK_INT < 19) {
            versionLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.card_view_with_border));
            txtCardPolicy.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.card_view_with_border));
            txtCardTerms.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.card_view_with_border));
        }

        if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {//                if(Globals.objAppThemeMaster!=null) {
//                    Globals.SetToolBarBackground(getActivity(), app_bar, Globals.objAppThemeMaster.getColorPrimary(), ContextCompat.getColor(getActivity(), android.R.color.white));
//                }
//                else
//                {
            Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary), ContextCompat.getColor(getActivity(), android.R.color.white));
//                }
            txtCardPolicy.setTextColor(ContextCompat.getColor(getActivity(), R.color.accent_dark));
            txtCardTerms.setTextColor(ContextCompat.getColor(getActivity(), R.color.accent_dark));
        } else {
            Globals.SetToolBarBackground(getActivity(), app_bar, ContextCompat.getColor(getActivity(), R.color.primary_black), ContextCompat.getColor(getActivity(), android.R.color.white));
        }

        txtVersionCode.setText(getResources().getString(R.string.abVersionCode) + "  " + BuildConfig.VERSION_CODE + "\n" +
                getResources().getString(R.string.abVersionName) + " " + BuildConfig.VERSION_NAME);

        txtCardPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplaceFragment(new PolicyFragment((short) 1), getActivity().getResources().getString(R.string.title_fragment_policy));
            }
        });

        txtCardTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplaceFragment(new PolicyFragment((short) 1), getActivity().getResources().getString(R.string.title_fragment_policy));
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getActivity().getResources().getString(R.string.title_fragment_about_us))) {
                getActivity().getSupportFragmentManager().popBackStack();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RtlHardcoded")
    private void ReplaceFragment(Fragment fragment, String fragmentName) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(350);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(R.id.aboutFragment, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_waiting))) {
            menu.findItem(R.id.mWaiting).setVisible(false);
        }
    }

}
