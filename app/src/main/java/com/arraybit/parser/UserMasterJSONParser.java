package com.arraybit.parser;

import android.content.Context;

import com.arraybit.global.Service;
import com.arraybit.modal.UserMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserMasterJSONParser {

    public String SelectRegisteredUserMasterUserName = "SelectUserName";
    Context context;

    //region Class Methods
    private UserMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        UserMaster objUserMaster = null;
        try {
            if (jsonObject != null) {
                objUserMaster = new UserMaster();
                objUserMaster.setUserMasterId((short) jsonObject.getInt("UserMasterId"));
                objUserMaster.setUsername(jsonObject.getString("Username"));
                objUserMaster.setPassword(jsonObject.getString("Password"));
                objUserMaster.setLinktoRoleMasterId((short) jsonObject.getInt("linktoRoleMasterId"));
                objUserMaster.setLinktoUserTypeMasterId((short) jsonObject.getInt("linktoUserTypeMasterId"));
                objUserMaster.setRole(jsonObject.getString("Role"));
                objUserMaster.setWaiterMasterId((short) jsonObject.getInt("WaiterMasterId"));
                objUserMaster.setLinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
            }
            return objUserMaster;
        } catch (JSONException e) {
            return null;
        }
    }


    private ArrayList<UserMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<UserMaster> lstUserMaster = new ArrayList<>();
        UserMaster objUserMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objUserMaster = new UserMaster();
                objUserMaster.setUserMasterId((short) jsonArray.getJSONObject(i).getInt("UserMasterId"));
                objUserMaster.setUsername(jsonArray.getJSONObject(i).getString("Username"));
                objUserMaster.setPassword(jsonArray.getJSONObject(i).getString("Password"));
                objUserMaster.setLinktoRoleMasterId((short) jsonArray.getJSONObject(i).getInt("linktoRoleMasterId"));
                objUserMaster.setLinktoUserTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoUserTypeMasterId"));
                objUserMaster.setRole(jsonArray.getJSONObject(i).getString("Role"));
                objUserMaster.setWaiterMasterId((short) jsonArray.getJSONObject(i).getInt("WaiterMasterId"));
                objUserMaster.setLinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                lstUserMaster.add(objUserMaster);
            }
            return lstUserMaster;
        } catch (JSONException e) {
            return null;
        }
    }
    //endregion

    //region Select
    public UserMaster SelectRegisteredUserName(String username,String password)
    {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectRegisteredUserMasterUserName + "/" + username + "/" + password + "/" + null);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectRegisteredUserMasterUserName + "Result");
                if (jsonObject != null) {
                    return SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }
    //endregion
}
