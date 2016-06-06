package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.modal.BusinessServiceTran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusinessServiceJSONParser {

    public String SelectAllBusinessServiceTran = "SelectAllBusinessService";

    private BusinessServiceTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessServiceTran objBusinessServiceTran = null;
        try {
            if (jsonObject != null) {
                objBusinessServiceTran = new BusinessServiceTran();
                objBusinessServiceTran.setBusinessServiceTran((short) jsonObject.getInt("BusinessServiceTranId"));
                objBusinessServiceTran.setlinktoServiceMasterId((short) jsonObject.getInt("linktoServiceMasterId"));
                objBusinessServiceTran.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));

                /// Extra
                objBusinessServiceTran.setService(jsonObject.getString("ServiceName"));
                objBusinessServiceTran.setImageName(jsonObject.getString("ImageName"));
                objBusinessServiceTran.setXSImagePhysicalName(jsonObject.getString("xs_ImagePhysicalName"));
            }
            return objBusinessServiceTran;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<BusinessServiceTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessServiceTran> lstBusinessServiceTran = new ArrayList<>();
        BusinessServiceTran objBusinessServiceTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessServiceTran = new BusinessServiceTran();
                objBusinessServiceTran.setBusinessServiceTran((short) jsonArray.getJSONObject(i).getInt("BusinessServiceTranId"));
                objBusinessServiceTran.setlinktoServiceMasterId((short) jsonArray.getJSONObject(i).getInt("linktoServiceMasterId"));
                objBusinessServiceTran.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));

                /// Extra
                objBusinessServiceTran.setService(jsonArray.getJSONObject(i).getString("ServiceName"));
                objBusinessServiceTran.setImageName(jsonArray.getJSONObject(i).getString("ImageName"));
                objBusinessServiceTran.setXSImagePhysicalName(jsonArray.getJSONObject(i).getString("xs_ImagePhysicalName"));
                objBusinessServiceTran.setIsSelected(jsonArray.getJSONObject(i).getBoolean("IsSelected"));
                lstBusinessServiceTran.add(objBusinessServiceTran);
            }
            return lstBusinessServiceTran;
        } catch (JSONException e) {
            return null;
        }
    }


    public ArrayList<BusinessServiceTran> SelectAllBusinessService(int linktoBusinessTypeMasterId,int BusinessMasterId) {
        ArrayList<BusinessServiceTran> lstBusinessServiceTran = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllBusinessServiceTran+"/"+BusinessMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllBusinessServiceTran + "Result");
                if (jsonArray != null) {
                    lstBusinessServiceTran = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstBusinessServiceTran;
        }
        catch (Exception ex) {
            return null;
        }
    }
}

