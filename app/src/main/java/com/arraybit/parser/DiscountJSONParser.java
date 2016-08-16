package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.modal.DiscountMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiscountJSONParser {

    public String SelectAllDiscountMaster = "SelectAllDiscountMaster";
    public String UpdateOrderMasterDiscount = "UpdateOrderMasterDiscount";

    private DiscountMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        DiscountMaster objDiscountMaster = null;
        try {
            if (jsonObject != null) {
                objDiscountMaster = new DiscountMaster();
                objDiscountMaster.setDiscountMasterId((short) jsonObject.getInt("DiscountMasterId"));
                objDiscountMaster.setDiscount(jsonObject.getDouble("Discount"));
                objDiscountMaster.setIsPercentage(jsonObject.getBoolean("IsPercentage"));
                objDiscountMaster.setDiscountTitle(jsonObject.getString("DiscountTitle"));
                objDiscountMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));

                /// Extra
                objDiscountMaster.setBusiness(jsonObject.getString("Business"));
            }
            return objDiscountMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<DiscountMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<DiscountMaster> lstDiscountMaster = new ArrayList<>();
        DiscountMaster objDiscountMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objDiscountMaster = new DiscountMaster();
                objDiscountMaster.setDiscountMasterId((short) jsonArray.getJSONObject(i).getInt("DiscountMasterId"));
                objDiscountMaster.setDiscount(jsonArray.getJSONObject(i).getDouble("Discount"));
                objDiscountMaster.setIsPercentage(jsonArray.getJSONObject(i).getBoolean("IsPercentage"));
                objDiscountMaster.setDiscountTitle(jsonArray.getJSONObject(i).getString("DiscountTitle"));
                objDiscountMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));

                /// Extra
                objDiscountMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstDiscountMaster.add(objDiscountMaster);
            }
            return lstDiscountMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<DiscountMaster> SelectAllDiscountMaster(int linktoBusinessMasterId) {
        ArrayList<DiscountMaster> lstDiscountMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllDiscountMaster + "/" + linktoBusinessMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllDiscountMaster + "Result");
                if (jsonArray != null) {
                    lstDiscountMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstDiscountMaster;
        } catch (Exception ex) {
            return null;
        }
    }


    public String UpdateOrderMasterDiscount(String orderMasterIds,double discount,int isPercentage) {
        try {
            String Discount = String.valueOf(discount).replace(".","2E2");
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.UpdateOrderMasterDiscount + "/" + orderMasterIds+"/"+Discount+"/"+String.valueOf(isPercentage));
            if(jsonResponse!=null){
                JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateOrderMasterDiscount + "Result");
                return String.valueOf(jsonObject.getInt("ErrorCode"));
            }else{
                return "-1";
            }
        } catch (Exception ex) {
            return "-1";
        }
    }
}
