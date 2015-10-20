package com.arraybit.parser;

import com.arraybit.modal.AreaMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AreaJSONParser {

    //region Class Methods
    private AreaMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        AreaMaster objAreaMaster = null;
        try {
            if (jsonObject != null) {
                objAreaMaster = new AreaMaster();
                objAreaMaster.setAreaMasterId((short) jsonObject.getInt("AreaMasterId"));
                objAreaMaster.setAreaName(jsonObject.getString("AreaName"));
                objAreaMaster.setZipCode(jsonObject.getString("ZipCode"));
                objAreaMaster.setLinktoCityMasterId((short) jsonObject.getInt("linktoCityMasterId"));
                objAreaMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                /// Extra
                objAreaMaster.setCity(jsonObject.getString("City"));
            }
            return objAreaMaster;
        } catch (JSONException e) {
            return null;
        }
    }


    private ArrayList<AreaMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<AreaMaster> lstAreaMaster = new ArrayList<>();
        AreaMaster objAreaMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objAreaMaster = new AreaMaster();
                objAreaMaster.setAreaMasterId((short) jsonArray.getJSONObject(i).getInt("AreaMasterId"));
                objAreaMaster.setAreaName(jsonArray.getJSONObject(i).getString("AreaName"));
                objAreaMaster.setZipCode(jsonArray.getJSONObject(i).getString("ZipCode"));
                objAreaMaster.setLinktoCityMasterId((short) jsonArray.getJSONObject(i).getInt("linktoCityMasterId"));
                objAreaMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                /// Extra
                objAreaMaster.setCity(jsonArray.getJSONObject(i).getString("City"));
                lstAreaMaster.add(objAreaMaster);
            }
            return lstAreaMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    //endregion

}
