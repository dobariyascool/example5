package com.arraybit.parser;

import android.net.ParseException;

import com.arraybit.global.Service;
import com.arraybit.modal.SectionMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SectionJSONParser {

    public String SelectAllSectionMaster = "SelectAllSectionMaster";

    //region Class Methods

    private SectionMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        SectionMaster objSectionMaster = null;
        try {
            if (jsonObject != null) {
                objSectionMaster = new SectionMaster();
                objSectionMaster.setSectionMasterId((short) jsonObject.getInt("SectionMasterId"));
                objSectionMaster.setSectionName(jsonObject.getString("SectionName"));
                objSectionMaster.setDescription(jsonObject.getString("Description"));
                objSectionMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objSectionMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));

                /// Extra
                objSectionMaster.setBusiness(jsonObject.getString("Business"));
            }
            return objSectionMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<SectionMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<SectionMaster> lstSectionMaster = new ArrayList<>();
        SectionMaster objSectionMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objSectionMaster = new SectionMaster();
                objSectionMaster.setSectionMasterId((short) jsonArray.getJSONObject(i).getInt("SectionMasterId"));
                objSectionMaster.setSectionName(jsonArray.getJSONObject(i).getString("SectionName"));
                objSectionMaster.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                objSectionMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objSectionMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));

                /// Extra
                objSectionMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstSectionMaster.add(objSectionMaster);
            }
            return lstSectionMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    //endregion

    //region SelectAll

    public ArrayList<SectionMaster> SelectAllSectionMaster(int linktoBusinessMasterId) {
        ArrayList<SectionMaster> lstSectionMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllSectionMaster + "/" + linktoBusinessMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllSectionMaster + "Result");
                if (jsonArray != null) {
                    lstSectionMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstSectionMaster;
        } catch (Exception ex) {
            return null;
        }
    }

    //endregion
}
