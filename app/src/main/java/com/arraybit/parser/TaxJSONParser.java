package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.TaxMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TaxJSONParser {

    public String SelectAllTaxMaster = "SelectAllTaxMaster";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    private TaxMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        TaxMaster objTaxMaster = null;
        try {
            if (jsonObject != null) {
                objTaxMaster = new TaxMaster();
                objTaxMaster.setTaxMasterId((short) jsonObject.getInt("TaxMasterId"));
                objTaxMaster.setTaxName(jsonObject.getString("TaxName"));
                objTaxMaster.setTaxRate(jsonObject.getDouble("TaxRate"));
                objTaxMaster.setIsPercentage(jsonObject.getBoolean("IsPercentage"));
                objTaxMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objTaxMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objTaxMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objTaxMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objTaxMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                objTaxMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objTaxMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
            }
            return objTaxMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<TaxMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<TaxMaster> lstTaxMaster = new ArrayList<>();
        TaxMaster objTaxMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objTaxMaster = new TaxMaster();
                objTaxMaster.setTaxMasterId((short) jsonArray.getJSONObject(i).getInt("TaxMasterId"));
                objTaxMaster.setTaxName(jsonArray.getJSONObject(i).getString("TaxName"));
                objTaxMaster.setTaxRate(jsonArray.getJSONObject(i).getDouble("TaxRate"));
                objTaxMaster.setIsPercentage(jsonArray.getJSONObject(i).getBoolean("IsPercentage"));
                objTaxMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objTaxMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objTaxMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objTaxMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objTaxMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objTaxMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                lstTaxMaster.add(objTaxMaster);
            }
            return lstTaxMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public ArrayList<TaxMaster> SelectAllTaxMaster() {
        ArrayList<TaxMaster> lstTaxMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllTaxMaster);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllTaxMaster + "Result");
                if (jsonArray != null) {
                    lstTaxMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstTaxMaster;
        } catch (Exception ex) {
            return null;
        }
    }

}
