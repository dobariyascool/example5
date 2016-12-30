package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.pos.GuestHomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryJSONParser {
    public String SelectAllCategoryMaster = "SelectAllCategoryMaster";

    private CategoryMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        CategoryMaster objCategoryMaster = null;
        try {
            if (jsonObject != null) {
                objCategoryMaster = new CategoryMaster();
                objCategoryMaster.setCategoryMasterId((short) jsonObject.getInt("CategoryMasterId"));
                objCategoryMaster.setCategoryName(jsonObject.getString("CategoryName"));
                if (!jsonObject.getString("linktoCategoryMasterIdParent").equals("null")) {
                    objCategoryMaster.setlinktoCategoryMasterIdParent((short) jsonObject.getInt("linktoCategoryMasterIdParent"));
                }
                if (!jsonObject.getString("ImageName").equals("")) {
                    objCategoryMaster.setImagelName(jsonObject.getString("ImageName"));
                }
                objCategoryMaster.setCategoryColor(jsonObject.getString("CategoryColor"));
                objCategoryMaster.setDescription(jsonObject.getString("Description"));
                //objCategoryMaster.setIsNew(jsonObject.getBoolean("IsNew"));
                objCategoryMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                if (!jsonObject.getString("SortOrder").equals("null")) {
                    objCategoryMaster.setSortOrder((short) jsonObject.getInt("SortOrder"));
                }
                objCategoryMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));

                /// Extra
                //objCategoryMaster.setCategory(jsonObject.getString("Category"));
            }
            return objCategoryMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<CategoryMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<CategoryMaster> lstCategoryMaster = new ArrayList<>();
        CategoryMaster objCategoryMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objCategoryMaster = new CategoryMaster();
                objCategoryMaster.setCategoryMasterId((short) jsonArray.getJSONObject(i).getInt("CategoryMasterId"));
                objCategoryMaster.setCategoryName(jsonArray.getJSONObject(i).getString("CategoryName"));
                if (!jsonArray.getJSONObject(i).getString("linktoCategoryMasterIdParent").equals("null")) {
                    objCategoryMaster.setlinktoCategoryMasterIdParent((short) jsonArray.getJSONObject(i).getInt("linktoCategoryMasterIdParent"));
                }
                if (!jsonArray.getJSONObject(i).getString("ImageName").equals("")) {
                    objCategoryMaster.setImagelName(jsonArray.getJSONObject(i).getString("ImageName"));
                }
                objCategoryMaster.setCategoryColor(jsonArray.getJSONObject(i).getString("CategoryColor"));
                objCategoryMaster.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                //objCategoryMaster.setIsNew(jsonArray.getJSONObject(i).getBoolean("IsNew"));
                objCategoryMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                if (!jsonArray.getJSONObject(i).getString("SortOrder").equals("null")) {
                    objCategoryMaster.setSortOrder((short) jsonArray.getJSONObject(i).getInt("SortOrder"));
                }
                objCategoryMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));

                /// Extra
                //objCategoryMaster.setCategory(jsonArray.getJSONObject(i).getString("Category"));
                lstCategoryMaster.add(objCategoryMaster);
            }
            return lstCategoryMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<CategoryMaster> SelectAllCategoryMaster(int linktoBusinessMasterId) {
        ArrayList<CategoryMaster> lstCategoryMaster = null;
        try {
            String url = null;
            if (GuestHomeActivity.isGuestMode || GuestHomeActivity.isMenuMode) {
                url = Service.Url + this.SelectAllCategoryMaster + "/" + linktoBusinessMasterId+ "/true";
            }else {
                url = Service.Url + this.SelectAllCategoryMaster + "/" + linktoBusinessMasterId+ "/false";
            }
            JSONObject jsonResponse = Service.HttpGetService(url);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllCategoryMaster + "Result");
                if (jsonArray != null) {
                    lstCategoryMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstCategoryMaster;
        } catch (Exception ex) {
            return null;
        }
    }
}