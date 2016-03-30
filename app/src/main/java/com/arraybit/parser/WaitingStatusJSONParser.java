package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.modal.WaitingStatusMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WaitingStatusJSONParser {

    public String SelectAllWaitingStatusMaster = "SelectAllWaitingStatus";

    //region Class Methods

    private WaitingStatusMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        WaitingStatusMaster objWaitingStatusMaster = null;
        try {
            if (jsonObject != null) {
                objWaitingStatusMaster = new WaitingStatusMaster();
                objWaitingStatusMaster.setWaitingStatusMasterId((short)jsonObject.getInt("WaitingStatusMasterId"));
                objWaitingStatusMaster.setWaitingStatus(jsonObject.getString("WaitingStatus"));
                objWaitingStatusMaster.setStatusColor(jsonObject.getString("StatusColor"));
            }
            return objWaitingStatusMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<WaitingStatusMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<WaitingStatusMaster> lstWaitingStatusMaster = new ArrayList<>();
        WaitingStatusMaster objWaitingStatusMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objWaitingStatusMaster = new WaitingStatusMaster();
                objWaitingStatusMaster.setWaitingStatusMasterId((short)jsonArray.getJSONObject(i).getInt("WaitingStatusMasterId"));
                objWaitingStatusMaster.setWaitingStatus(jsonArray.getJSONObject(i).getString("WaitingStatus"));
                objWaitingStatusMaster.setStatusColor(jsonArray.getJSONObject(i).getString("StatusColor"));
                lstWaitingStatusMaster.add(objWaitingStatusMaster);
            }
            return lstWaitingStatusMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    //endregion

    //region SelectAll

    public ArrayList<WaitingStatusMaster> SelectAllWaitingStatusMaster() {
        ArrayList<WaitingStatusMaster> lstWaitingStatusMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllWaitingStatusMaster);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllWaitingStatusMaster + "Result");
                if (jsonArray != null) {
                    lstWaitingStatusMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstWaitingStatusMaster;
        }
        catch (Exception ex) {
            return null;
        }
    }

    //endregion
}
