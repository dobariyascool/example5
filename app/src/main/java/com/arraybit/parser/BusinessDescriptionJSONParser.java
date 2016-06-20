package com.arraybit.parser;


import android.content.Context;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessDescription;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BusinessDescriptionJSONParser {

    public String SelectBusinessDescription = "SelectBusinessDescription";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    private BusinessDescription SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessDescription objBusinessDescription = null;
        try {
            if (jsonObject != null) {
                objBusinessDescription = new BusinessDescription();
                objBusinessDescription.setBusinessDescriptionId((short) jsonObject.getInt("BusinessDescriptionId"));
                objBusinessDescription.setKeyword(jsonObject.getString("Title"));
                if(!jsonObject.getString("Description").equals("null")){
                    objBusinessDescription.setDescription(jsonObject.getString("Description"));
                }
                objBusinessDescription.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                /// Extra
                //objBusinessDescription.setBusiness(jsonObject.getString("Business"));
            }
            return objBusinessDescription;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<BusinessDescription> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessDescription> lstBusinessDescription = new ArrayList<>();
        BusinessDescription objBusinessDescription;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessDescription = new BusinessDescription();
                objBusinessDescription.setBusinessDescriptionId((short) jsonArray.getJSONObject(i).getInt("BusinessDescriptionId"));
                objBusinessDescription.setKeyword(jsonArray.getJSONObject(i).getString("Title"));
                if(!jsonArray.getJSONObject(i).getString("Description").equals("null")) {
                    objBusinessDescription.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                }
                objBusinessDescription.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objBusinessDescription.setIsDefault(jsonArray.getJSONObject(i).getBoolean("IsDefault"));
                /// Extra
                //objBusinessDescription.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstBusinessDescription.add(objBusinessDescription);
            }
            return lstBusinessDescription;
        } catch (JSONException e) {
            return null;
        }
    }

    public BusinessDescription SelectBusinessDescription(final Context context, String businessMasterId, String keyword) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectBusinessDescription + "/" + businessMasterId + "/" + URLEncoder.encode(keyword, "UTF-8").replace("+","%20"));
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectBusinessDescription + "Result");
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
