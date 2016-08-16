package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.OfferMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OfferJSONParser {

    public String SelectAllOfferMaster = "SelectAllOfferMaster";
    public String SelectOfferMaster = "SelectOfferMaster";
    public String SelectOfferMasterOfferCodeVerification = "SelectOfferMasterOfferCodeVerification";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    SimpleDateFormat sdfControlTimeFormat = new SimpleDateFormat(Globals.TimeFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SimpleDateFormat sdfTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);

    private OfferMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        OfferMaster objOfferMaster = null;
        try {
            if (jsonObject != null) {
                objOfferMaster = new OfferMaster();
                objOfferMaster.setOfferMasterId(jsonObject.getInt("OfferMasterId"));
                objOfferMaster.setlinktoOfferTypeMasterId((short) jsonObject.getInt("linktoOfferTypeMasterId"));
                objOfferMaster.setOfferTitle(jsonObject.getString("OfferTitle"));
                objOfferMaster.setOfferContent(jsonObject.getString("OfferContent"));
                if (!jsonObject.getString("FromDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonObject.getString("FromDate"));
                    objOfferMaster.setFromDate(sdfControlDateFormat.format(dt));
                }
                if (!jsonObject.getString("ToDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonObject.getString("ToDate"));
                    objOfferMaster.setToDate(sdfControlDateFormat.format(dt));
                }
                if (!jsonObject.getString("FromTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonObject.getString("FromTime"));
                    objOfferMaster.setFromTime(sdfControlTimeFormat.format(dt));
                }
                if (!jsonObject.getString("ToTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonObject.getString("ToTime"));
                    objOfferMaster.setToTime(sdfControlTimeFormat.format(dt));
                }
                if (!jsonObject.getString("MinimumBillAmount").equals("null")) {
                    objOfferMaster.setMinimumBillAmount(jsonObject.getDouble("MinimumBillAmount"));
                }
                objOfferMaster.setDiscount(jsonObject.getDouble("Discount"));
                objOfferMaster.setIsDiscountPercentage(jsonObject.getBoolean("IsDiscountPercentage"));
                if (!jsonObject.getString("RedeemCount").equals("null")) {
                    objOfferMaster.setRedeemCount(jsonObject.getInt("RedeemCount"));
                }
                objOfferMaster.setOfferCode(jsonObject.getString("OfferCode"));
                objOfferMaster.setImagePhysicalName(jsonObject.getString("ImagePhysicalName"));
                objOfferMaster.setXS_ImagePhysicalName(jsonObject.getString("xs_ImagePhysicalName"));
                objOfferMaster.setSM_ImagePhysicalName(jsonObject.getString("sm_ImagePhysicalName"));
                objOfferMaster.setMD_ImagePhysicalName(jsonObject.getString("md_ImagePhysicalName"));
                objOfferMaster.setLG_ImagePhysicalName(jsonObject.getString("lg_ImagePhysicalName"));
                objOfferMaster.setXL_ImagePhysicalName(jsonObject.getString("xl_ImagePhysicalName"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objOfferMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objOfferMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objOfferMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objOfferMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objOfferMaster.setLinktoCustomerMasterId(jsonObject.getInt("linktoCustomerMasterId"));
                objOfferMaster.setTermsAndConditions(jsonObject.getString("TermsAndConditions"));
                objOfferMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objOfferMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                objOfferMaster.setIsForCustomers(jsonObject.getBoolean("IsForCustomers"));
                if (!jsonObject.getString("BuyItemCount").equals("null")) {
                    objOfferMaster.setBuyItemCount(jsonObject.getInt("BuyItemCount"));
                }
                if (!jsonObject.getString("GetItemCount").equals("null")) {
                    objOfferMaster.setGetItemCount(jsonObject.getInt("GetItemCount"));
                }
                /// Extra
                objOfferMaster.setOfferType(jsonObject.getString("OfferType"));
                objOfferMaster.setBusiness(jsonObject.getString("Business"));
                if (!jsonObject.getString("ValidBuyItems").equals("null")) {
                    objOfferMaster.setValidBuyItems(jsonObject.getString("ValidBuyItems"));
                }
                if (!jsonObject.getString("ValidDays").equals("null")) {
                    objOfferMaster.setValidDays(jsonObject.getString("ValidDays"));
                }
                if (!jsonObject.getString("ValidItems").equals("null")) {
                    objOfferMaster.setValidItems(jsonObject.getString("ValidItems"));
                }
                if (!jsonObject.getString("ValidGetItems").equals("null")) {
                    objOfferMaster.setValidGetItems(jsonObject.getString("ValidGetItems"));
                }
                if (!jsonObject.getString("linktoOrderTypeMasterIds").equals("null")) {
                    objOfferMaster.setLinktoOrderTypeMasterIds(jsonObject.getString("linktoOrderTypeMasterIds"));
                }
                objOfferMaster.setIsForApp(jsonObject.getBoolean("IsForApp"));
                objOfferMaster.setIsOnline(jsonObject.getBoolean("IsOnline"));
            }
            return objOfferMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<OfferMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<OfferMaster> lstOfferMaster = new ArrayList<>();
        OfferMaster objOfferMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objOfferMaster = new OfferMaster();
                objOfferMaster.setOfferMasterId(jsonArray.getJSONObject(i).getInt("OfferMasterId"));
                objOfferMaster.setlinktoOfferTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoOfferTypeMasterId"));
                objOfferMaster.setOfferTitle(jsonArray.getJSONObject(i).getString("OfferTitle"));
                objOfferMaster.setOfferContent(jsonArray.getJSONObject(i).getString("OfferContent"));
                if (!jsonArray.getJSONObject(i).getString("FromDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("FromDate"));
                    objOfferMaster.setFromDate(sdfControlDateFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("ToDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("ToDate"));
                    objOfferMaster.setToDate(sdfControlDateFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("FromTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("FromTime"));
                    objOfferMaster.setFromTime(sdfControlTimeFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("ToTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("ToTime"));
                    objOfferMaster.setToTime(sdfControlTimeFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("MinimumBillAmount").equals("null")) {
                    objOfferMaster.setMinimumBillAmount(jsonArray.getJSONObject(i).getDouble("MinimumBillAmount"));
                }
                objOfferMaster.setDiscount(jsonArray.getJSONObject(i).getDouble("Discount"));
                objOfferMaster.setIsDiscountPercentage(jsonArray.getJSONObject(i).getBoolean("IsDiscountPercentage"));
                if (!jsonArray.getJSONObject(i).getString("RedeemCount").equals("null")) {
                    objOfferMaster.setRedeemCount(jsonArray.getJSONObject(i).getInt("RedeemCount"));
                }
                objOfferMaster.setOfferCode(jsonArray.getJSONObject(i).getString("OfferCode"));
                objOfferMaster.setImagePhysicalName(jsonArray.getJSONObject(i).getString("ImagePhysicalName"));
                objOfferMaster.setXS_ImagePhysicalName(jsonArray.getJSONObject(i).getString("xs_ImagePhysicalName"));
                objOfferMaster.setSM_ImagePhysicalName(jsonArray.getJSONObject(i).getString("sm_ImagePhysicalName"));
                objOfferMaster.setMD_ImagePhysicalName(jsonArray.getJSONObject(i).getString("md_ImagePhysicalName"));
                objOfferMaster.setLG_ImagePhysicalName(jsonArray.getJSONObject(i).getString("lg_ImagePhysicalName"));
                objOfferMaster.setXL_ImagePhysicalName(jsonArray.getJSONObject(i).getString("xl_ImagePhysicalName"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objOfferMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objOfferMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objOfferMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objOfferMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objOfferMaster.setLinktoCustomerMasterId(jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
                objOfferMaster.setTermsAndConditions(jsonArray.getJSONObject(i).getString("TermsAndConditions"));
                objOfferMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objOfferMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                objOfferMaster.setIsForCustomers(jsonArray.getJSONObject(i).getBoolean("IsForCustomers"));
                if (!jsonArray.getJSONObject(i).getString("BuyItemCount").equals("null")) {
                    objOfferMaster.setBuyItemCount(jsonArray.getJSONObject(i).getInt("BuyItemCount"));
                }
                if (!jsonArray.getJSONObject(i).getString("GetItemCount").equals("null")) {
                    objOfferMaster.setGetItemCount(jsonArray.getJSONObject(i).getInt("GetItemCount"));
                }

                /// Extra
                objOfferMaster.setOfferType(jsonArray.getJSONObject(i).getString("OfferType"));
                objOfferMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstOfferMaster.add(objOfferMaster);
            }
            return lstOfferMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public ArrayList<OfferMaster> SelectAllOfferMaster(int linktoBusinessMasterId) {
        ArrayList<OfferMaster> lstOfferMaster = null;
        dt = new Date();
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllOfferMaster + "/" + sdfControlDateFormat.format(dt) + "/" + Globals.GetCurrentTime() + "/" + linktoBusinessMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllOfferMaster + "Result");
                if (jsonArray != null) {
                    lstOfferMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstOfferMaster;
        } catch (Exception ex) {
            return null;
        }
    }

    public OfferMaster SelectOfferMaster(int offerMasterId) {
        OfferMaster objOfferMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectOfferMaster + "/" + offerMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectOfferMaster + "Result");
                if (jsonObject != null) {
                    objOfferMaster = SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return objOfferMaster;
        } catch (Exception ex) {
            return null;
        }
    }

    public OfferMaster SelectOfferCodeVerification(OfferMaster offerMaster) {

        OfferMaster objOfferMaster = null;
        try {

            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("objOfferMaster");
            stringer.object();

            stringer.key("OfferCode").value(offerMaster.getOfferCode());
            stringer.key("MinimumBillAmount").value(offerMaster.getMinimumBillAmount());
            stringer.key("linktoBusinessMasterId").value(offerMaster.getlinktoBusinessMasterId());
            stringer.key("linktoCustomerMasterId").value(offerMaster.getLinktoCustomerMasterId());
            stringer.key("linktoOrderTypeMasterIds").value(offerMaster.getlinktoOrderTypeMasterId());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.SelectOfferMasterOfferCodeVerification ,stringer);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectOfferMasterOfferCodeVerification + "Result");
                if (jsonObject != null) {
                    objOfferMaster = SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return objOfferMaster;
        } catch (Exception ex) {
            return null;
        }
    }
}

