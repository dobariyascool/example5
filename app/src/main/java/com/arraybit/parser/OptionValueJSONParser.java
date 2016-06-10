package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.modal.OptionValueTran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OptionValueJSONParser {
    public String SelectAllOptionValueTran = "SelectAllItemOption";

    private OptionValueTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        OptionValueTran objOptionValueTran = null;
        try {
            if (jsonObject != null) {
                objOptionValueTran = new OptionValueTran();
                objOptionValueTran.setOptionValueTranId(jsonObject.getInt("OptionValueTranId"));
                objOptionValueTran.setlinktoOptionMasterId((short) jsonObject.getInt("linktoOptionMasterId"));
                objOptionValueTran.setOptionValue(jsonObject.getString("OptionValue"));
                objOptionValueTran.setIsDeleted(jsonObject.getBoolean("IsDeleted"));

                /// Extra
                objOptionValueTran.setOptionName(jsonObject.getString("OptionName"));
            }
            return objOptionValueTran;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<OptionValueTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<OptionValueTran> lstOptionValueTran = new ArrayList<>();
        OptionValueTran objOptionValueTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objOptionValueTran = new OptionValueTran();
                objOptionValueTran.setOptionValueTranId(jsonArray.getJSONObject(i).getInt("OptionValueTranId"));
                objOptionValueTran.setlinktoOptionMasterId((short) jsonArray.getJSONObject(i).getInt("linktoOptionMasterId"));
                objOptionValueTran.setOptionValue(jsonArray.getJSONObject(i).getString("OptionValue"));
                objOptionValueTran.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));

                /// Extra
                objOptionValueTran.setOptionName(jsonArray.getJSONObject(i).getString("OptionName"));
                lstOptionValueTran.add(objOptionValueTran);
            }
            return lstOptionValueTran;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<OptionValueTran> SelectAllItemOptionValue(String linktoItemMasterId) {
        ArrayList<OptionValueTran> lstOptionValueTran = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllOptionValueTran + "/" + linktoItemMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllOptionValueTran + "Result");
                if (jsonArray != null) {
                    lstOptionValueTran = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstOptionValueTran;
        } catch (Exception ex) {
            return null;
        }
    }
}
