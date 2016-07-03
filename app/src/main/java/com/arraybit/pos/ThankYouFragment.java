package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        LinearLayout thankYouFragment = (LinearLayout) view.findViewById(R.id.thankYouFragment);
        LinearLayout buttonLayout = (LinearLayout) view.findViewById(R.id.buttonLayout);
        Globals.SetScaleImageBackground(getActivity(), thankYouFragment, null, null);

        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);

        Button btnFeedback = (Button) view.findViewById(R.id.btnFeedback);
        Button btnSkip = (Button) view.findViewById(R.id.btnSkip);

        if (isShowButton) {
            buttonLayout.setVisibility(View.VISIBLE);
        } else {
            buttonLayout.setVisibility(View.GONE);
        }

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
                        if (isShowButton) {

                        } else {
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
