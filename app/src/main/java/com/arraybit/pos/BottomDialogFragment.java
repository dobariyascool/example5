package com.arraybit.pos;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;


public class BottomDialogFragment extends DialogFragment {


    public BottomDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_bottom_dialog, null, false);
        final Dialog mBottomSheetDialog = new Dialog(getActivity(),
                R.style.MaterialDialogSheet);

        CardView cvDineIn = (CardView) view.findViewById(R.id.cvDineIn);
        CardView cvTakeAway = (CardView) view.findViewById(R.id.cvTakeAway);

        cvDineIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments() != null && getArguments().getBoolean("IsMenuMode")) {
                    Globals.isWishListShow = 1;
                    Globals.DisableBroadCastReceiver(getActivity());
                    Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
                    intent.putExtra("linktoOrderTypeMasterId", Globals.OrderType.DineIn.getValue());
                    intent.putExtra("IsMenuMode", true);
                    startActivity(intent);
                } else {
                    AllTablesFragment allTablesFragment = new AllTablesFragment(getActivity(), true, null);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("IsVacant", true);
                    bundle.putInt("linktoOrderTypeMasterId", Globals.OrderType.DineIn.getValue());
                    allTablesFragment.setArguments(bundle);
                    Globals.ReplaceFragment(allTablesFragment, getActivity().getSupportFragmentManager(), getResources().getString(R.string.title_fragment_all_tables));
                }

                mBottomSheetDialog.dismiss();
            }
        });

        cvTakeAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getArguments()!=null && getArguments().getBoolean("IsMenuMode")){
                    Globals.isWishListShow = 1;
                    Globals.DisableBroadCastReceiver(getActivity());
                    Intent intent = new Intent(getActivity(), GuestHomeActivity.class);
                    intent.putExtra("linktoOrderTypeMasterId", Globals.OrderType.TakeAway.getValue());
                    intent.putExtra("IsMenuMode", true);
                    startActivity(intent);
                }else{
                    AllTablesFragment allTablesFragment = new AllTablesFragment(getActivity(), true, null);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("IsVacant", true);
                    bundle.putInt("linktoOrderTypeMasterId", Globals.OrderType.TakeAway.getValue());
                    allTablesFragment.setArguments(bundle);
                    Globals.ReplaceFragment(allTablesFragment, getActivity().getSupportFragmentManager(), getResources().getString(R.string.title_fragment_all_tables));
                }
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        //to prevent the dialog on back press
        mBottomSheetDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    // To dismiss the fragment when the back-button is pressed.
                    dismiss();
                    return true;
                } else {
                    // Otherwise, do nothing else
                    return false;
                }
            }
        });
        mBottomSheetDialog.show();
        return mBottomSheetDialog;
    }

}
