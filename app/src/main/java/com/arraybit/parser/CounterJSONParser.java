package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.modal.CounterMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CounterJSONParser {

    public String SelectAllCounterMaster = "SelectAllCounterMaster";

    private CounterMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        CounterMaster objCounterMaster = null;
        try {
            if (jsonObject != null) {
                objCounterMaster = new CounterMaster();
                objCounterMaster.setCounterMasterId((short) jsonObject.getInt("CounterMasterId"));
                objCounterMaster.setCounterName(jsonObject.getString("CounterName"));
                objCounterMaster.setShortName(jsonObject.getString("ShortName"));
                objCounterMaster.setDescription(jsonObject.getString("Description"));
                objCounterMaster.setImageName(jsonObject.getString("ImageName"));
                objCounterMaster.setLinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objCounterMaster.setLinktoDepartmentMasterId((short) jsonObject.getInt("linktoDepartmentMasterId"));
            }
            return objCounterMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<CounterMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<CounterMaster> lstCounterMaster = new ArrayList<>();
        CounterMaster objCounterMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objCounterMaster = new CounterMaster();
                objCounterMaster.setCounterMasterId((short) jsonArray.getJSONObject(i).getInt("CounterMasterId"));
                objCounterMaster.setCounterName(jsonArray.getJSONObject(i).getString("CounterName"));
                objCounterMaster.setShortName(jsonArray.getJSONObject(i).getString("ShortName"));
                objCounterMaster.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                objCounterMaster.setImageName(jsonArray.getJSONObject(i).getString("ImageName"));
                objCounterMaster.setLinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objCounterMaster.setLinktoDepartmentMasterId((short) jsonArray.getJSONObject(i).getInt("linktoDepartmentMasterId"));
                lstCounterMaster.add(objCounterMaster);
            }
            return lstCounterMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<CounterMaster> SelectAllCounterMaster(int linktoBusinessMasterId,int linktoUserMasterId) {
        ArrayList<CounterMaster> lstCounterMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllCounterMaster + "/" + linktoBusinessMasterId + "/" +linktoUserMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllCounterMaster + "Result");
                if (jsonArray != null) {
                    lstCounterMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstCounterMaster;
        } catch (Exception ex) {
            return null;
        }
    }
}

