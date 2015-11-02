package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.CategoryMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/// <summary>
/// JSONParser for CategoryMaster
/// </summary>

public class CategoryJSONParser {
    public String SelectAllCategoryMaster = "SelectAllCategoryMaster";
    public String SelectAllCategoryMasterCategoryName = "SelectAllCategoryMasterCategoryName";


    private CategoryMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        CategoryMaster objCategoryMaster = null;
        try {
            if (jsonObject != null) {
                objCategoryMaster = new CategoryMaster();
                objCategoryMaster.setCategoryMasterId((short) jsonObject.getInt("CategoryMasterId"));
                objCategoryMaster.setCategoryName(jsonObject.getString("CategoryName"));
                if (!jsonObject.getString("linktoCategoryMasterId").equals("null")) {
                    objCategoryMaster.setlinktoCategoryMasterId((short) jsonObject.getInt("linktoCategoryMasterId"));
                }
                objCategoryMaster.setImagelName(jsonObject.getString("ImagelName"));
                objCategoryMaster.setCategoryColor(jsonObject.getString("CategoryColor"));
                objCategoryMaster.setDescription(jsonObject.getString("Description"));
                objCategoryMaster.setIsNew(jsonObject.getBoolean("IsNew"));
                objCategoryMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                if (!jsonObject.getString("SortOrder").equals("null")) {
                    objCategoryMaster.setSortOrder((short) jsonObject.getInt("SortOrder"));
                }
                objCategoryMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));

                /// Extra
                objCategoryMaster.setCategory(jsonObject.getString("Category"));
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
                if (!jsonArray.getJSONObject(i).getString("linktoCategoryMasterId").equals("null")) {
                    objCategoryMaster.setlinktoCategoryMasterId((short) jsonArray.getJSONObject(i).getInt("linktoCategoryMasterId"));
                }
                objCategoryMaster.setImagelName(jsonArray.getJSONObject(i).getString("ImagelName"));
                objCategoryMaster.setCategoryColor(jsonArray.getJSONObject(i).getString("CategoryColor"));
                objCategoryMaster.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                objCategoryMaster.setIsNew(jsonArray.getJSONObject(i).getBoolean("IsNew"));
                objCategoryMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                if (!jsonArray.getJSONObject(i).getString("SortOrder").equals("null")) {
                    objCategoryMaster.setSortOrder((short) jsonArray.getJSONObject(i).getInt("SortOrder"));
                }
                objCategoryMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));

                /// Extra
                objCategoryMaster.setCategory(jsonArray.getJSONObject(i).getString("Category"));
                lstCategoryMaster.add(objCategoryMaster);
            }
            return lstCategoryMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<CategoryMaster> SelectAllCategoryMaster() {
        ArrayList<CategoryMaster> lstCategoryMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllCategoryMaster);
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

    public ArrayList<SpinnerItem> SelectAllCategoryMasterCategoryName() {
        ArrayList<SpinnerItem> lstSpinnerItem = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllCategoryMasterCategoryName);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllCategoryMasterCategoryName + "Result");
                if (jsonArray != null) {
                    lstSpinnerItem = new ArrayList<>();
                    SpinnerItem objSpinnerItem;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objSpinnerItem = new SpinnerItem();
                        objSpinnerItem.setText(jsonArray.getJSONObject(i).getString("CategoryName"));
                        objSpinnerItem.setValue(jsonArray.getJSONObject(i).getInt("CategoryMasterId"));
                        lstSpinnerItem.add(objSpinnerItem);
                    }
                }
            }
            return lstSpinnerItem;
        } catch (Exception ex) {
            return null;
        }
    }

}