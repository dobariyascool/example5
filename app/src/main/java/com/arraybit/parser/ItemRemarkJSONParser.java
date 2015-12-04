package com.arraybit.parser;

import android.net.ParseException;

import com.arraybit.global.Service;
import com.arraybit.modal.ItemRemarkMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemRemarkJSONParser {

    public String SelectAllItemRemarkMaster = "SelectAllItemRemarkMaster";

    private ItemRemarkMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        ItemRemarkMaster objItemRemarkMaster = null;
        try {
            if (jsonObject != null) {
                objItemRemarkMaster = new ItemRemarkMaster();
                objItemRemarkMaster.setItemRemarkMasterId((short)jsonObject.getInt("ItemRemarkMasterId"));
                objItemRemarkMaster.setItemRemark(jsonObject.getString("ItemRemark"));
                objItemRemarkMaster.setlinktoBusinessMasterId((short)jsonObject.getInt("linktoBusinessMasterId"));

                /// Extra
                objItemRemarkMaster.setBusiness(jsonObject.getString("Business"));
            }
            return objItemRemarkMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<ItemRemarkMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<ItemRemarkMaster> lstItemRemarkMaster = new ArrayList<>();
        ItemRemarkMaster objItemRemarkMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objItemRemarkMaster = new ItemRemarkMaster();
                objItemRemarkMaster.setItemRemarkMasterId((short)jsonArray.getJSONObject(i).getInt("ItemRemarkMasterId"));
                objItemRemarkMaster.setItemRemark(jsonArray.getJSONObject(i).getString("ItemRemark"));
                objItemRemarkMaster.setlinktoBusinessMasterId((short)jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));

                /// Extra
                objItemRemarkMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstItemRemarkMaster.add(objItemRemarkMaster);
            }
            return lstItemRemarkMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public ArrayList<ItemRemarkMaster> SelectAllItemRemarkMasterPageWise(int currentPage) {
        ArrayList<ItemRemarkMaster> lstItemRemarkMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllItemRemarkMaster);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllItemRemarkMaster + "Result");
                if (jsonArray != null) {
                    lstItemRemarkMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstItemRemarkMaster;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<String> SelectAllItemRemarkMaster(int linktoBusinessMasterId) {
        ArrayList<String> alString = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllItemRemarkMaster+"/"+linktoBusinessMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllItemRemarkMaster + "Result");
                if (jsonArray != null) {
                    alString = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        alString.add(jsonArray.getJSONObject(i).getString("ItemRemark"));
                    }
                }
            }
            return alString;
        }
        catch (Exception ex) {
            return null;
        }
    }

}
