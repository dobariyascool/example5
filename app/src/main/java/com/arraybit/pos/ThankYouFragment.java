package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


@SuppressLint("ValidFragment")
public class ThankYouFragment extends Fragment {

    String message;
    boolean isShowButton;

    public ThankYouFragment(String message, boolean isShowButton) {
        // Required empty public constructor
        this.message = message;
        this.isShowButton = isShowButton;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thank_you, container, false);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Monotype Corsiva.ttf");
        LinearLayout thankYouFragment = (LinearLayout) view.findViewById(R.id.thankYouFragment);
        LinearLayout buttonLayout = (LinearLayout) view.findViewById(R.id.buttonLayout);
        Globals.SetScaleImageBackground(getActivity(), thankYouFragment, null, null);

        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView txtTitle2 = (TextView) view.findViewById(R.id.txtTitle2);
        txtTitle.setTypeface(custom_font);
        txtTitle2.setTypeface(custom_font);
        Button btnFeedback = (Button) view.findViewById(R.id.btnFeedback);
        Button btnSkip = (Button) view.findViewById(R.id.btnSkip);

        if (isShowButton) {
            buttonLayout.setVisibility(View.VISIBLE);
            txtTitle2.setVisibility(View.VISIBLE);
        } else {
            buttonLayout.setVisibility(View.GONE);
            txtTitle2.setVisibility(View.GONE);
        }

        Globals.CustomView(btnSkip, ContextCompat.getColor(getActivity(),android.R.color.transparent), ContextCompat.getColor(getActivity(),R.color.accent_dark));
        btnSkip.setTextColor(ContextCompat.getColor(getActivity(),R.color.accent_dark));

        Globals.CustomView(btnFeedback, ContextCompat.getColor(getActivity(),R.color.accent_dark), ContextCompat.getColor(getActivity(),android.R.color.transparent));
        btnFeedback.setTextColor(ContextCompat.getColor(getActivity(),R.color.primary));

        txtTitle.setText(message);

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                Globals.ReplaceFragment(new FeedbackFragment(getActivity()), getActivity().getSupportFragmentManager(), null);
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.userName = null;
                getActivity().getSupportFragmentManager().popBackStack();
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                intent.putExtra("TableMaster", GuestHomeActivity.objTableMaster);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("GuestScreen", true);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getActivity().finish();
            }
        });

        thankYouFragment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!isShowButton) {
                            Intent intent = new Intent(getActivity(), WaiterHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        }
                    }
                }, 1000);
                return false;
            }
        });

        return view;
    }



}
