package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        }
        return super.onOptionsItemSelected(item);
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
                if(objOfferMaster.getFromTime()==null && objOfferMaster.getToTime()==null){
                    txtFromDate.setText(String.valueOf(objOfferMaster.getFromDate()));
                    txtToDate.setText(String.valueOf(objOfferMaster.getToDate()));
                }
                else{
                    txtFromDate.setText(String.valueOf(objOfferMaster.getFromDate())+ " "+objOfferMaster.getFromTime());
                    txtToDate.setText(String.valueOf(objOfferMaster.getToDate())+ " "+objOfferMaster.getToTime());
                }
            }
        }
    }
}
