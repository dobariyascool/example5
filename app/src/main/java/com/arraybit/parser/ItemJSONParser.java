package com.arraybit.parser;


import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.ItemMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/// <summary>
/// JSONParser for ItemMaster
/// </summary>

public class ItemJSONParser {
    public String SelectAllItemMaster = "SelectAllItemMasterPageWise";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    private ItemMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        ItemMaster objItemMaster = null;
        try {
            if (jsonObject != null) {
                objItemMaster = new ItemMaster();
                objItemMaster.setItemMasterId(jsonObject.getInt("ItemMasterId"));
                objItemMaster.setItemName(jsonObject.getString("ItemName"));
                objItemMaster.setShortName(jsonObject.getString("ShortName"));
                objItemMaster.setItemCode(jsonObject.getString("ItemCode"));
                objItemMaster.setShortDescription(jsonObject.getString("ShortDescription"));
                objItemMaster.setMRP(jsonObject.getDouble("MRP"));
                objItemMaster.setSellPrice(jsonObject.getDouble("SellPrice"));
                objItemMaster.setItemPoint((short) jsonObject.getInt("ItemPoint"));
                objItemMaster.setPriceByPoint((short) jsonObject.getInt("PriceByPoint"));
                objItemMaster.setlinktoItemTypeMasterId((short) jsonObject.getInt("linktoItemTypeMasterId"));
                objItemMaster.setlinktoUnitMasterId((short) jsonObject.getInt("linktoUnitMasterId"));
                objItemMaster.setSearchWords(jsonObject.getString("SearchWords"));
                objItemMaster.setImageName(jsonObject.getString("ImageName"));
                objItemMaster.setlinktoItemStatusMasterId((short) jsonObject.getInt("linktoItemStatusMasterId"));
                if (!jsonObject.getString("SortOrder").equals("null")) {
                    objItemMaster.setSortOrder(jsonObject.getInt("SortOrder"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objItemMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objItemMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                //dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                //objItemMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objItemMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objItemMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objItemMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objItemMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));

                /// Extra
                objItemMaster.setItemType(jsonObject.getString("ItemType"));
                objItemMaster.setUnit(jsonObject.getString("Unit"));
                objItemMaster.setItemStatus(jsonObject.getString("ItemStatus"));
            }
            return objItemMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<ItemMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<ItemMaster> lstItemMaster = new ArrayList<>();
        ItemMaster objItemMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objItemMaster = new ItemMaster();
                objItemMaster.setItemMasterId(jsonArray.getJSONObject(i).getInt("ItemMasterId"));
                objItemMaster.setItemName(jsonArray.getJSONObject(i).getString("ItemName"));
                objItemMaster.setShortName(jsonArray.getJSONObject(i).getString("ShortName"));
                objItemMaster.setItemCode(jsonArray.getJSONObject(i).getString("ItemCode"));
                objItemMaster.setShortDescription(jsonArray.getJSONObject(i).getString("ShortDescription"));
                objItemMaster.setMRP(jsonArray.getJSONObject(i).getDouble("MRP"));
                objItemMaster.setSellPrice(jsonArray.getJSONObject(i).getDouble("SellPrice"));
                objItemMaster.setItemPoint((short) jsonArray.getJSONObject(i).getInt("ItemPoint"));
                objItemMaster.setPriceByPoint((short) jsonArray.getJSONObject(i).getInt("PriceByPoint"));
                objItemMaster.setlinktoItemTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoItemTypeMasterId"));
                objItemMaster.setlinktoUnitMasterId((short) jsonArray.getJSONObject(i).getInt("linktoUnitMasterId"));
                objItemMaster.setSearchWords(jsonArray.getJSONObject(i).getString("SearchWords"));
                objItemMaster.setImageName(jsonArray.getJSONObject(i).getString("ImageName"));
                objItemMaster.setlinktoItemStatusMasterId((short) jsonArray.getJSONObject(i).getInt("linktoItemStatusMasterId"));
                if (!jsonArray.getJSONObject(i).getString("SortOrder").equals("null")) {
                    objItemMaster.setSortOrder(jsonArray.getJSONObject(i).getInt("SortOrder"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objItemMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objItemMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
//                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
//                objItemMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objItemMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objItemMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objItemMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objItemMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));

                /// Extra
                objItemMaster.setItemType(jsonArray.getJSONObject(i).getString("ItemType"));
                objItemMaster.setUnit(jsonArray.getJSONObject(i).getString("Unit"));
                objItemMaster.setItemStatus(jsonArray.getJSONObject(i).getString("ItemStatus"));
                lstItemMaster.add(objItemMaster);
            }
            return lstItemMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public ArrayList<ItemMaster> SelectAllItemMasterPageWise(int currentPage,int linktoCategoryMasterId) {
        ArrayList<ItemMaster> lstItemMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllItemMaster +"/"+currentPage+"/"+linktoCategoryMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllItemMaster + "Result");
                if (jsonArray != null) {
                    lstItemMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstItemMaster;
        } catch (Exception ex) {
            return null;
        }
    }

}
