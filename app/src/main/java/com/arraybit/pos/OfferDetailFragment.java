package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.modal.OfferMaster;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

@SuppressLint("ValidFragment")
@SuppressWarnings("ConstantConditions")
public class OfferDetailFragment extends Fragment {


    ImageView ivOffer;
    OfferMaster objOfferMaster;
    TextView txtOfferTitle, txtOfferContent, txtAmount, txtHeader, txtTermsCondition, txtOfferDiscount, txtFromDate, txtToDate;
    LinearLayout billAmountLayout, offerDateLayout;


    public OfferDetailFragment(OfferMaster objOfferMaster) {
        this.objOfferMaster = objOfferMaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer_detail_frgament, container, false);


        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_offer_detail));
        }
        //end

        setHasOptionsMenu(true);

        //ImageView
        ivOffer = (ImageView) view.findViewById(R.id.ivOffer);
        //end

        txtOfferTitle = (TextView) view.findViewById(R.id.txtOfferTitle);
        txtOfferContent = (TextView) view.findViewById(R.id.txtOfferContent);
        txtAmount = (TextView) view.findViewById(R.id.txtAmount);
        txtHeader = (TextView) view.findViewById(R.id.txtHeader);
        txtTermsCondition = (TextView) view.findViewById(R.id.txtTermsCondition);
        txtOfferDiscount = (TextView) view.findViewById(R.id.txtOfferDiscount);
        txtFromDate = (TextView) view.findViewById(R.id.txtFromDate);
        txtToDate = (TextView) view.findViewById(R.id.txtToDate);


        billAmountLayout = (LinearLayout) view.findViewById(R.id.billAmountLayout);
        offerDateLayout = (LinearLayout) view.findViewById(R.id.offerDateLayout);

        SetDetailLayout();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
        } else if (item.getTitle() == getActivity().getResources().getString(R.string.navRegistration)) {
            ReplaceFragment(new SignUpFragment(), getActivity().getResources().getString(R.string.title_fragment_signup));
        } else if (item.getTitle() == getActivity().getResources().getString(R.string.navLogin)) {
            GuestLoginDialogFragment guestLoginDialogFragment = new GuestLoginDialogFragment();
            guestLoginDialogFragment.setTargetFragment(this, 0);
            guestLoginDialogFragment.show(getActivity().getSupportFragmentManager(), "");
        } else if (item.getTitle() == getActivity().getResources().getString(R.string.wmMyAccount)) {
            MyAccountFragment myAccountFragment = new MyAccountFragment();
            myAccountFragment.setTargetFragment(this, 0);
            ReplaceFragment(myAccountFragment, getActivity().getResources().getString(R.string.title_fragment_myaccount));
        } else if (item.getTitle() == getActivity().getResources().getString(R.string.wmLogout)) {
            Globals.ClearPreference(getActivity().getApplication());
            Globals.userName = null;
        }
        return super.onOptionsItemSelected(item);
    }

    //region Private Methods
    @SuppressLint("RtlHardcoded")
    private void ReplaceFragment(Fragment fragment, String fragmentName) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(500);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(R.id.offerDetailFragment, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    @SuppressLint("SetTextI18n")
    private void SetDetailLayout() {
        if (objOfferMaster != null) {
            txtOfferTitle.setText(objOfferMaster.getOfferTitle());
            if (objOfferMaster.getOfferContent().equals("") || objOfferMaster.getOfferContent().isEmpty() || objOfferMaster.getOfferContent() == null) {
                txtOfferContent.setVisibility(View.GONE);
            } else {
                txtOfferContent.setVisibility(View.VISIBLE);
                txtOfferContent.setText(objOfferMaster.getOfferContent());
            }
            if (objOfferMaster.getMinimumBillAmount() == 0.0) {
                billAmountLayout.setVisibility(View.GONE);
            } else {
                billAmountLayout.setVisibility(View.VISIBLE);
                txtAmount.setText(Globals.dfWithPrecision.format(objOfferMaster.getMinimumBillAmount()));
            }
            if (objOfferMaster.getTermsAndConditions().equals("") || objOfferMaster.getTermsAndConditions().isEmpty() || objOfferMaster.getTermsAndConditions() == null) {
                txtHeader.setVisibility(View.GONE);
                txtTermsCondition.setVisibility(View.GONE);
            } else {
                txtHeader.setVisibility(View.VISIBLE);
                txtTermsCondition.setVisibility(View.VISIBLE);
                txtHeader.setText(getActivity().getResources().getString(R.string.odfTerm));
                txtTermsCondition.setText(objOfferMaster.getTermsAndConditions());
            }
            if (!objOfferMaster.getImagePhysicalName().equals("null")) {
                Picasso.with(getActivity()).load(objOfferMaster.getImagePhysicalName()).into(ivOffer);
            } else {
                ivOffer.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.image_border));
            }
            if (objOfferMaster.getIsDiscountPercentage()) {
                String str = (Globals.dfWithPrecision.format(objOfferMaster.getDiscount()))
                        .substring(Globals.dfWithPrecision.format(objOfferMaster.getDiscount()).lastIndexOf(".") + 1, Globals.dfWithPrecision.format(objOfferMaster.getDiscount()).length());
                if (str.equals("0") || str.equals("00")) {
                    txtOfferDiscount.setText(Globals.dfWithPrecision.format(objOfferMaster.getDiscount()).substring(0, Globals.dfWithPrecision.format(objOfferMaster.getDiscount()).lastIndexOf(".")) + " % OFF");
                } else {
                    txtOfferDiscount.setText(Globals.dfWithPrecision.format(objOfferMaster.getDiscount()) + " % OFF");
                }
            } else {
                txtOfferDiscount.setText(Globals.dfWithPrecision.format(objOfferMaster.getDiscount()) + " OFF");
            }
            if (objOfferMaster.getFromDate() == null && objOfferMaster.getToDate() == null) {
                offerDateLayout.setVisibility(View.GONE);
            } else {
                offerDateLayout.setVisibility(View.VISIBLE);
                if (objOfferMaster.getFromTime() == null && objOfferMaster.getToTime() == null) {
                    txtFromDate.setText(String.valueOf(objOfferMaster.getFromDate()));
                    txtToDate.setText(String.valueOf(objOfferMaster.getToDate()));
                } else {
                    txtFromDate.setText(String.valueOf(objOfferMaster.getFromDate()) + " " + objOfferMaster.getFromTime());
                    txtToDate.setText(String.valueOf(objOfferMaster.getToDate()) + " " + objOfferMaster.getToTime());
                }
            }
        }
    }
    //endregion
}
