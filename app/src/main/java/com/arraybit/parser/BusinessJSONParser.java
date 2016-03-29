package com.arraybit.parser;


import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BusinessJSONParser {
    public String SelectBusinessMaster = "SelectBusinessMasterByBusinessMasterId";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;

    private BusinessMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessMaster objBusinessMaster = null;
        try {
            if (jsonObject != null) {
                objBusinessMaster = new BusinessMaster();
                objBusinessMaster.setBusinessMasterId((short) jsonObject.getInt("BusinessMasterId"));
                objBusinessMaster.setBusinessName(jsonObject.getString("BusinessName"));
                objBusinessMaster.setBusinessShortName(jsonObject.getString("BusinessShortName"));
                objBusinessMaster.setAddress(jsonObject.getString("Address"));
                objBusinessMaster.setPhone1(jsonObject.getString("Phone1"));
                objBusinessMaster.setPhone2(jsonObject.getString("Phone2"));
                objBusinessMaster.setEmail(jsonObject.getString("Email"));
                objBusinessMaster.setFax(jsonObject.getString("Fax"));
                objBusinessMaster.setWebsite(jsonObject.getString("Website"));
                objBusinessMaster.setlinktoCountryMasterId((short) jsonObject.getInt("linktoCountryMasterId"));
                objBusinessMaster.setlinktoStateMasterId((short) jsonObject.getInt("linktoStateMasterId"));
                objBusinessMaster.setCity(jsonObject.getString("City"));
                objBusinessMaster.setZipCode(jsonObject.getString("ZipCode"));
                objBusinessMaster.setImageName(jsonObject.getString("ImageName"));
                objBusinessMaster.setExtraText(jsonObject.getString("ExtraText"));
                objBusinessMaster.setTIN(jsonObject.getString("TIN"));
                objBusinessMaster.setCST(jsonObject.getString("CST"));
                objBusinessMaster.setlinktoBusinessTypeMasterId((short) jsonObject.getInt("linktoBusinessTypeMasterId"));
                objBusinessMaster.setUniqueId(jsonObject.getString("UniqueId"));
                objBusinessMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));

                /// Extra
                objBusinessMaster.setCountry(jsonObject.getString("Country"));
                objBusinessMaster.setState(jsonObject.getString("State"));
                objBusinessMaster.setBusinessType(jsonObject.getString("BusinessType"));
            }
            return objBusinessMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<BusinessMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessMaster> lstBusinessMaster = new ArrayList<>();
        BusinessMaster objBusinessMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessMaster = new BusinessMaster();
                objBusinessMaster.setBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("BusinessMasterId"));
                objBusinessMaster.setBusinessName(jsonArray.getJSONObject(i).getString("BusinessName"));
                objBusinessMaster.setBusinessShortName(jsonArray.getJSONObject(i).getString("BusinessShortName"));
                objBusinessMaster.setAddress(jsonArray.getJSONObject(i).getString("Address"));
                objBusinessMaster.setPhone1(jsonArray.getJSONObject(i).getString("Phone1"));
                objBusinessMaster.setPhone2(jsonArray.getJSONObject(i).getString("Phone2"));
                objBusinessMaster.setEmail(jsonArray.getJSONObject(i).getString("Email"));
                objBusinessMaster.setFax(jsonArray.getJSONObject(i).getString("Fax"));
                objBusinessMaster.setWebsite(jsonArray.getJSONObject(i).getString("Website"));
                objBusinessMaster.setlinktoCountryMasterId((short) jsonArray.getJSONObject(i).getInt("linktoCountryMasterId"));
                objBusinessMaster.setlinktoStateMasterId((short) jsonArray.getJSONObject(i).getInt("linktoStateMasterId"));
                objBusinessMaster.setCity(jsonArray.getJSONObject(i).getString("City"));
                objBusinessMaster.setZipCode(jsonArray.getJSONObject(i).getString("ZipCode"));
                objBusinessMaster.setImageName(jsonArray.getJSONObject(i).getString("ImageName"));
                objBusinessMaster.setExtraText(jsonArray.getJSONObject(i).getString("ExtraText"));
                objBusinessMaster.setTIN(jsonArray.getJSONObject(i).getString("TIN"));
                objBusinessMaster.setCST(jsonArray.getJSONObject(i).getString("CST"));
                objBusinessMaster.setlinktoBusinessTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessTypeMasterId"));
                objBusinessMaster.setUniqueId(jsonArray.getJSONObject(i).getString("UniqueId"));
                objBusinessMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));

                /// Extra
                objBusinessMaster.setCountry(jsonArray.getJSONObject(i).getString("Country"));
                objBusinessMaster.setState(jsonArray.getJSONObject(i).getString("State"));
                objBusinessMaster.setBusinessType(jsonArray.getJSONObject(i).getString("BusinessType"));
                lstBusinessMaster.add(objBusinessMaster);
            }
            return lstBusinessMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    public BusinessMaster SelectBusinessMasterByUniqueId(int businessMasterId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectBusinessMaster + "/" + businessMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectBusinessMaster + "Result");
                if (jsonObject != null) {
                    return SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }
}
