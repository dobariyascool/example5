package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.modal.ItemCategoryTran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ItemCategoryJSONParser {
    public String SelectAllItemCategoryTran = "";

    private ItemCategoryTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        ItemCategoryTran objItemCategoryTran = null;
        try {
            if (jsonObject != null) {
                objItemCategoryTran = new ItemCategoryTran();
                objItemCategoryTran.setItemCategoryTranId(jsonObject.getInt("ItemCategoryTranId"));
                objItemCategoryTran.setLinktoItemMasterId(jsonObject.getInt("LinktoItemMasterId"));
                objItemCategoryTran.setLinktoCategoryMasterId((short) jsonObject.getInt("LinktoCategoryMasterId"));
            }
            return objItemCategoryTran;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<ItemCategoryTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<ItemCategoryTran> lstItemCategoryTran = new ArrayList<>();
        ItemCategoryTran objItemCategoryTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objItemCategoryTran = new ItemCategoryTran();
                objItemCategoryTran.setItemCategoryTranId(jsonArray.getJSONObject(i).getInt("ItemCategoryTranId"));
                objItemCategoryTran.setLinktoItemMasterId(jsonArray.getJSONObject(i).getInt("LinktoItemMasterId"));
                objItemCategoryTran.setLinktoCategoryMasterId((short) jsonArray.getJSONObject(i).getInt("LinktoCategoryMasterId"));
            }
            return lstItemCategoryTran;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<ItemCategoryTran> SelectAllItemCategoryTran() {
        ArrayList<ItemCategoryTran> lstItemCategoryTran = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllItemCategoryTran);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllItemCategoryTran + "result");
                if (jsonArray != null) {
                    lstItemCategoryTran = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstItemCategoryTran;
        } catch (Exception ex) {
            return null;
        }
    }

}
