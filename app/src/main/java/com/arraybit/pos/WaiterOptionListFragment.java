package com.arraybit.pos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;

public class WaiterOptionListFragment extends Fragment implements View.OnClickListener {

    LinearLayout waiterOptionLayout;
    ImageView ivHomeActivityImage;

//    NotificationListener notificationListener;

    public WaiterOptionListFragment() {
        // Required empty public constructor
    }

//    interface NotificationListener{
//        void ShowNotificationCount();
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiter_option_list, container, false);

        GuestHomeActivity.isGuestMode = false;

        waiterOptionLayout = (LinearLayout) view.findViewById(R.id.waiterOptionLayout);

        Bitmap originalBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.waiter_home);
        waiterOptionLayout.setBackground(new BitmapDrawable(getActivity().getResources(), originalBitmap));

        ivHomeActivityImage= (ImageView) view.findViewById(R.id.ivHomeActivityImage);
        if (Globals.objAppThemeMaster != null) {
            SharePreferenceManage sharePreferenceManage = new SharePreferenceManage();
            String encodedLogoImage = sharePreferenceManage.GetPreference("GuestAppTheme", getResources().getString(R.string.encodedLogoImage), getActivity());
            if (encodedLogoImage != null && !encodedLogoImage.equals("")) {
                byte[] decodedString = Base64.decode(encodedLogoImage.getBytes(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivHomeActivityImage.setImageDrawable(new BitmapDrawable(getActivity().getResources(), decodedByte));
            }
//            if (Globals.objAppThemeMaster.getLogoImageName() != null && !Globals.objAppThemeMaster.getLogoImageName().equals("")) {
//                Log.e("image", " " + Globals.objAppThemeMaster.getLogoImageName());
//                Glide.with(this).load(Globals.objAppThemeMaster.getLogoImageName()).asBitmap().into(ivHomeActivityImage);
//            }
        }

        CardView cvOrders = (CardView) view.findViewById(R.id.cvOrders);
        CardView cvDineIn = (CardView) view.findViewById(R.id.cvDineIn);
        CardView cvTakeAway = (CardView) view.findViewById(R.id.cvTakeAway);
        CardView cvBill = (CardView) view.findViewById(R.id.cvBill);

        cvOrders.setOnClickListener(this);
        cvDineIn.setOnClickListener(this);
        cvTakeAway.setOnClickListener(this);
        cvBill.setOnClickListener(this);

//        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName().equals(getActivity().getResources().getString(R.string.title_fragment_waiter_options))) {
            if (v.getId() == R.id.cvOrders) {
                Globals.ReplaceFragment(new AllOrdersFragment(null), getActivity().getSupportFragmentManager(), null);
            } else if (v.getId() == R.id.cvDineIn) {
                Globals.orderTypeMasterId = (short) Globals.OrderType.DineIn.getValue();
                Globals.ReplaceFragment(new AllTablesFragment(getActivity(), false, String.valueOf(Globals.OrderType.DineIn.getValue())), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_all_tables));
            } else if (v.getId() == R.id.cvTakeAway) {
                Globals.orderTypeMasterId = (short) Globals.OrderType.TakeAway.getValue();
                Globals.ReplaceFragment(new AllTablesFragment(getActivity(), false, String.valueOf(Globals.OrderType.TakeAway.getValue())), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_fragment_all_tables));
            } else if (v.getId() == R.id.cvBill) {
                Globals.ReplaceFragment(new TableOrderFragment(), getActivity().getSupportFragmentManager(), null);
            }
        }
    }

//    public void ShowNotification(){
//        notificationListener = (NotificationListener)getActivity();
//        notificationListener.ShowNotificationCount();
//    }
}
