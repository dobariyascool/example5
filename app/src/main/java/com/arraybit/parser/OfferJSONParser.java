package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.OfferMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OfferJSONParser {

    public String SelectOfferMaster = "SelectOfferMaster";
    public String SelectAllOfferMaster = "SelectAllOfferMaster";

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
                if(!jsonObject.getString("FromDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonObject.getString("FromDate"));
                    objOfferMaster.setFromDate(sdfControlDateFormat.format(dt));
                }
                if(!jsonObject.getString("ToDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonObject.getString("ToDate"));
                    objOfferMaster.setToDate(sdfControlDateFormat.format(dt));
                }
                if(!jsonObject.getString("FromTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonObject.getString("FromTime"));
                    objOfferMaster.setFromTime(sdfControlTimeFormat.format(dt));
                }
                if(!jsonObject.getString("ToTime").equals("null")) {
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
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objOfferMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objOfferMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objOfferMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objOfferMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
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
                if(!jsonArray.getJSONObject(i).getString("FromDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("FromDate"));
                    objOfferMaster.setFromDate(sdfControlDateFormat.format(dt));
                }
                if(!jsonArray.getJSONObject(i).getString("ToDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("ToDate"));
                    objOfferMaster.setToDate(sdfControlDateFormat.format(dt));
                }
                if(!jsonArray.getJSONObject(i).getString("FromTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("FromTime"));
                    objOfferMaster.setFromTime(sdfControlTimeFormat.format(dt));
                }
                if(!jsonArray.getJSONObject(i).getString("ToTime").equals("null")) {
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
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objOfferMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objOfferMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objOfferMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objOfferMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
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

    public OfferMaster SelectOfferMaster(int offerMasterId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectOfferMaster + "/" + offerMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectOfferMaster + "Result");
                if (jsonObject != null) {
                    return SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<OfferMaster> SelectAllOfferMaster() {
        ArrayList<OfferMaster> lstOfferMaster = null;
        dt = new Date();
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllOfferMaster + "/" + sdfControlDateFormat.format(dt) + "/" + Globals.GetCurrentTime());
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
}
