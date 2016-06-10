package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.adapter.OfferItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.OfferMaster;
import com.arraybit.parser.OfferJSONParser;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

@SuppressLint("ValidFragment")
@SuppressWarnings({"ConstantConditions", "unchecked"})
public class OfferDetailFragment extends Fragment {


    ProgressDialog progressDialog = new ProgressDialog();
    OfferMaster objOfferMaster;
    NestedScrollView scrollView;
    LinearLayout errorLayout, termsConditionLayout, discountLayout;
    ImageView ivOfferImage, ivTimings;
    TextView txtOfferTitle, txtOfferContent, txtFromDate, txtValidDays, txtMinBillAmt, txtOfferCustomer, txtOfferCode, txtOfferCondition, txtOfferDiscount, txtBuyGetItem, txtValidFor;
    WebView wvCondition;
    ImageButton ibVisible;
    int isShow = 0;
    RecyclerView rvSelectedItem, rvBuyItem, rvGetItem;
    StringBuilder sbDays, sbOrderType;
    CardView cvCondition;


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
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_offer_detail));
        }
        //end

        setHasOptionsMenu(true);

        LinearLayout errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

        scrollView = (NestedScrollView) view.findViewById(R.id.scrollView);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        termsConditionLayout = (LinearLayout) view.findViewById(R.id.termsConditionLayout);
        discountLayout = (LinearLayout) view.findViewById(R.id.discountLayout);

        ivOfferImage = (ImageView) view.findViewById(R.id.ivOfferImage);
        ivTimings = (ImageView) view.findViewById(R.id.ivTimings);
        ibVisible = (ImageButton) view.findViewById(R.id.ibVisible);

        cvCondition = (CardView) view.findViewById(R.id.cvCondition);

        rvSelectedItem = (RecyclerView) view.findViewById(R.id.rvSelectedItem);
        rvBuyItem = (RecyclerView) view.findViewById(R.id.rvBuyItem);
        rvGetItem = (RecyclerView) view.findViewById(R.id.rvGetItem);

        txtOfferTitle = (TextView) view.findViewById(R.id.txtOfferTitle);
        txtOfferContent = (TextView) view.findViewById(R.id.txtOfferContent);
        txtFromDate = (TextView) view.findViewById(R.id.txtFromDate);
        txtValidDays = (TextView) view.findViewById(R.id.txtValidDays);
        txtMinBillAmt = (TextView) view.findViewById(R.id.txtMinBillAmt);
        txtOfferCustomer = (TextView) view.findViewById(R.id.txtOfferCustomer);
        txtOfferCode = (TextView) view.findViewById(R.id.txtOfferCode);
        txtOfferCondition = (TextView) view.findViewById(R.id.txtOfferCondition);
        txtOfferDiscount = (TextView) view.findViewById(R.id.txtOfferDiscount);
        txtBuyGetItem = (TextView) view.findViewById(R.id.txtBuyGetItem);
        txtValidFor = (TextView) view.findViewById(R.id.txtValidFor);

        wvCondition = (WebView) view.findViewById(R.id.wvCondition);
        wvCondition.getSettings().setDatabaseEnabled(true);
        wvCondition.getSettings().setDomStorageEnabled(true);
        wvCondition.getSettings().setAppCacheEnabled(true);
        wvCondition.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvCondition.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        ibVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow == 0) {
                    ibVisible.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.collapse_drawable));
                    cvCondition.setVisibility(View.VISIBLE);
                    wvCondition.loadData(objOfferMaster.getTermsAndConditions(), "text/html", "UTF-8");
                    isShow = 1;
                } else {
                    ibVisible.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.expand_drawable));
                    cvCondition.setVisibility(View.GONE);
                    isShow = 0;
                }
            }
        });

        termsConditionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow == 0) {
                    ibVisible.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.collapse_drawable));
                    cvCondition.setVisibility(View.VISIBLE);
                    wvCondition.loadData(objOfferMaster.getTermsAndConditions(), "text/html", "UTF-8");
                    isShow = 1;
                } else {
                    ibVisible.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.expand_drawable));
                    cvCondition.setVisibility(View.GONE);
                    isShow = 0;
                }
            }
        });


        if (Service.CheckNet(getActivity())) {
            scrollView.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            new OfferLoadingTask().execute();
        } else {
            scrollView.setVisibility(View.GONE);
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null,R.drawable.wifi_drawable);
        }


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
            Fade fade = new Fade();
            fade.setDuration(500);
            fragment.setEnterTransition(fade);
            fragment.setEnterTransition(fade);
        } else {
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        fragmentTransaction.replace(R.id.offerDetailFragment, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    @SuppressLint("SetTextI18n")
    private void SetOfferDetail() {
        if (objOfferMaster != null) {
            if (objOfferMaster.getMD_ImagePhysicalName() != null && !objOfferMaster.getMD_ImagePhysicalName().equals("")) {
                Picasso.with(getActivity()).load(objOfferMaster.getMD_ImagePhysicalName()).into(ivOfferImage);
            }
            txtOfferTitle.setText(objOfferMaster.getOfferTitle());
            if (objOfferMaster.getOfferContent() != null && !objOfferMaster.getOfferContent().equals("")) {
                txtOfferContent.setVisibility(View.VISIBLE);
                txtOfferContent.setText(objOfferMaster.getOfferContent());
            } else {
                txtOfferContent.setVisibility(View.GONE);
            }

            if (objOfferMaster.getFromDate() != null && !objOfferMaster.getFromDate().equals("") && objOfferMaster.getToDate() != null && !objOfferMaster.getToDate().equals("")) {
                txtFromDate.setVisibility(View.VISIBLE);
                if (objOfferMaster.getFromTime() != null && !objOfferMaster.getFromTime().equals("") && objOfferMaster.getToTime() != null && !objOfferMaster.getToTime().equals("")) {
                    try {
                        Date fromTime = new SimpleDateFormat(Globals.TimeFormat, Locale.US).parse(objOfferMaster.getFromTime());
                        Date toTime = new SimpleDateFormat(Globals.TimeFormat, Locale.US).parse(objOfferMaster.getToTime());
                        txtFromDate.setText(objOfferMaster.getFromDate() + " To " + objOfferMaster.getToDate() + "  " + new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).format(fromTime) + " To " + new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).format(toTime));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    txtFromDate.setText(objOfferMaster.getFromDate() + " To " + objOfferMaster.getToDate());
                }
            } else {
                ivTimings.setVisibility(View.GONE);
                txtFromDate.setVisibility(View.GONE);
            }

            if (objOfferMaster.getValidDays() == null || objOfferMaster.getValidDays().equals("")) {
                txtValidDays.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaValidFor), "All Days"));
            } else {
                String[] strArray = objOfferMaster.getValidDays().split(",");
                sbDays = new StringBuilder();
                if (strArray.length < 7) {
                    for (String strDay : strArray) {
                        if (strDay.equals(strArray[strArray.length - 1])) {
                            sbDays.append(strDay);
                        } else {
                            sbDays.append(strDay).append(", ");
                        }
                    }
                    txtValidDays.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaValidOn), sbDays.toString()));
                } else {
                    txtValidDays.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaValidFor), "All Days"));
                }

            }
            if (objOfferMaster.getMinimumBillAmount() != 0) {
                txtMinBillAmt.setVisibility(View.VISIBLE);
                txtMinBillAmt.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaMinAmt), Globals.dfWithPrecision.format(objOfferMaster.getMinimumBillAmount())));
            } else {
                txtMinBillAmt.setVisibility(View.GONE);
            }
            if (objOfferMaster.getIsForCustomers()) {
                txtOfferCustomer.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaOfferFor), "Customer"));
            } else {
                txtOfferCustomer.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaOfferFor), "Register User"));
            }
            if (objOfferMaster.getOfferCode() != null && !objOfferMaster.getOfferCode().equals("")) {
                txtOfferCode.setVisibility(View.VISIBLE);
                txtOfferCode.setText(objOfferMaster.getOfferCode());
            } else {
                txtOfferCode.setVisibility(View.GONE);
            }
            if (objOfferMaster.getTermsAndConditions() != null && !objOfferMaster.getTermsAndConditions().equals("")) {
                cvCondition.setVisibility(View.GONE);
                termsConditionLayout.setVisibility(View.VISIBLE);
            } else {
                cvCondition.setVisibility(View.GONE);
                termsConditionLayout.setVisibility(View.GONE);
            }
            if (objOfferMaster.getDiscount() != 0) {
                discountLayout.setVisibility(View.VISIBLE);
                if (objOfferMaster.getIsDiscountPercentage()) {
                    String strOfferDiscount = String.valueOf(objOfferMaster.getDiscount()).substring(0, String.valueOf(objOfferMaster.getDiscount()).lastIndexOf("."));
                    txtOfferDiscount.setText(String.format(getResources().getString(R.string.odaOfferDiscount), strOfferDiscount));
                } else {
                    txtOfferDiscount.setText(String.format(getResources().getString(R.string.odaOfferDiscountWithRupee), Globals.dfWithPrecision.format(objOfferMaster.getDiscount())));
                }

            } else {
                discountLayout.setVisibility(View.GONE);
            }
            if (objOfferMaster.getValidItems() != null && !objOfferMaster.getValidItems().equals("")) {
                txtBuyGetItem.setVisibility(View.VISIBLE);
                txtBuyGetItem.setText("Selected Items");
                ArrayList<String> alString = new ArrayList<>(Arrays.asList(objOfferMaster.getValidItems().split(",")));
                OfferItemAdapter offerItemAdapter = new OfferItemAdapter(getActivity(), alString, "");
                rvSelectedItem.setVisibility(View.VISIBLE);
                rvSelectedItem.setAdapter(offerItemAdapter);
                rvSelectedItem.setLayoutManager(new LinearLayoutManager(getActivity()));
            } else {
                rvSelectedItem.setVisibility(View.GONE);
                if (objOfferMaster.getBuyItemCount() == 0 && objOfferMaster.getGetItemCount() == 0) {
                    txtBuyGetItem.setVisibility(View.GONE);
                } else {
                    txtBuyGetItem.setVisibility(View.VISIBLE);
                    if (objOfferMaster.getBuyItemCount() > 1 && objOfferMaster.getGetItemCount() > 1) {
                        txtBuyGetItem.setText("Buy " + objOfferMaster.getBuyItemCount() + " items Get " + objOfferMaster.getGetItemCount() + " items");
                    } else {
                        txtBuyGetItem.setText("Buy " + objOfferMaster.getBuyItemCount() + " item Get " + objOfferMaster.getGetItemCount() + " item");
                    }

                    if (objOfferMaster.getValidBuyItems() != null && !objOfferMaster.getValidBuyItems().equals("")) {
                        ArrayList<String> alBuyItem = new ArrayList<>(Arrays.asList(objOfferMaster.getValidBuyItems().split(",")));
                        OfferItemAdapter offerBuyItemAdapter = new OfferItemAdapter(getActivity(), alBuyItem, "Buy Items");
                        rvBuyItem.setVisibility(View.VISIBLE);
                        rvBuyItem.setAdapter(offerBuyItemAdapter);
                        rvBuyItem.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                    if (objOfferMaster.getValidGetItems() != null && !objOfferMaster.getValidGetItems().equals("")) {
                        ArrayList<String> alGetItem = new ArrayList<>(Arrays.asList(objOfferMaster.getValidGetItems().split(",")));
                        OfferItemAdapter offerGetItemAdapter = new OfferItemAdapter(getActivity(), alGetItem, "Get Items");
                        rvGetItem.setVisibility(View.VISIBLE);
                        rvGetItem.setAdapter(offerGetItemAdapter);
                        rvGetItem.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
            }

            if (objOfferMaster.getLinktoOrderTypeMasterIds() == null || objOfferMaster.getLinktoOrderTypeMasterIds().equals("")) {
                txtValidFor.setVisibility(View.GONE);
            } else {
                txtValidFor.setVisibility(View.VISIBLE);
                String[] strOrderType = objOfferMaster.getLinktoOrderTypeMasterIds().split(",");
                sbOrderType = new StringBuilder();
                for (String orderType : strOrderType) {
                    if (orderType.equals(strOrderType[strOrderType.length - 1])) {
                        if (orderType.equals(String.valueOf(Globals.OrderType.DineIn.getValue()))) {
                            sbOrderType.append("Dine In");
                        } else if (orderType.equals(String.valueOf(Globals.OrderType.TakeAway.getValue()))) {
                            sbOrderType.append("Take Away");
                        } else if (orderType.equals(String.valueOf(Globals.OrderType.HomeDelivery.getValue()))) {
                            sbOrderType.append("Home Delivery");
                        }
                    } else {
                        if (orderType.equals(String.valueOf(Globals.OrderType.DineIn.getValue()))) {
                            sbOrderType.append("Dine In").append(", ");
                        } else if (orderType.equals(String.valueOf(Globals.OrderType.TakeAway.getValue()))) {
                            sbOrderType.append("Take Away").append(", ");
                        } else if (orderType.equals(String.valueOf(Globals.OrderType.HomeDelivery.getValue()))) {
                            sbOrderType.append("Home Delivery").append(", ");
                        }
                    }
                }
                if ((objOfferMaster.getIsForApp()) && (objOfferMaster.getIsOnline())) {
                    txtValidFor.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaOfferOnlyFor), sbOrderType.toString()) + " (" + getResources().getString(R.string.odaOfferValid) + ") ");
                } else if (objOfferMaster.getIsOnline()) {
                    txtValidFor.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaOfferOnlyFor), sbOrderType.toString()) + " (" + getResources().getString(R.string.odaOfferValidOnline) + ") ");
                } else if (objOfferMaster.getIsForApp()) {
                    txtValidFor.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaOfferOnlyFor), sbOrderType.toString()) + " (" + getResources().getString(R.string.odaOfferValidApp) + ") ");
                } else {
                    txtValidFor.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaOfferOnlyFor), sbOrderType.toString()));
                }
            }
        }
    }
    //endregion

    //region LoadingTask
    class OfferLoadingTask extends AsyncTask {

        com.arraybit.pos.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new com.arraybit.pos.ProgressDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OfferJSONParser objOfferJSONParser = new OfferJSONParser();
            return objOfferJSONParser.SelectOfferMaster(objOfferMaster.getOfferMasterId());
        }

        @Override
        protected void onPostExecute(Object result) {
            progressDialog.dismiss();
            if (result != null) {
                objOfferMaster = (OfferMaster) result;
                SetOfferDetail();
            }
        }
    }
    //endregion
}
