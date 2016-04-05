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

public class ItemJSONParser {
    public String SelectAllItemMasterByCategoryMasterId = "SelectAllItemMasterByCategoryMasterId";
    public String SelectAllItemMasterModifier = "SelectAllItemMasterModifier";
    public String SelectAllOrderItemByTableMasterIds = "SelectAllOrderItemByTableMasterIds";
    public String SelectItemMaster = "SelectItemMaster";

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
                //objItemMaster.setlinktoItemTypeMasterId((short) jsonObject.getInt("linktoItemTypeMasterId"));
                objItemMaster.setlinktoUnitMasterId((short) jsonObject.getInt("linktoUnitMasterId"));
                objItemMaster.setSearchWords(jsonObject.getString("SearchWords"));
                objItemMaster.setImageName(jsonObject.getString("ImageName"));
                if (!jsonObject.getString("SortOrder").equals("null")) {
                    objItemMaster.setSortOrder(jsonObject.getInt("SortOrder"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objItemMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objItemMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objItemMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objItemMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objItemMaster.setIsFavourite(jsonObject.getBoolean("IsFavourite"));
                objItemMaster.setBarCode(jsonObject.getString("BarCode"));

                /// Extra
                //objItemMaster.setItemType(jsonObject.getString("ItemType"));
                objItemMaster.setUnit(jsonObject.getString("Unit"));
                objItemMaster.setActualSellPrice(jsonObject.getDouble("ActualSellPrice"));
                objItemMaster.setLinktoOrderMasterId(jsonObject.getLong("linktoOrderMasterId"));
                objItemMaster.setItemModifierIds(jsonObject.getString("ItemModifierMasterIds"));
                objItemMaster.setQuantity(jsonObject.getInt("Quantity"));
                objItemMaster.setRemark(jsonObject.getString("Remark"));
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
                //objItemMaster.setlinktoItemTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoItemTypeMasterId"));
                objItemMaster.setlinktoUnitMasterId((short) jsonArray.getJSONObject(i).getInt("linktoUnitMasterId"));
                objItemMaster.setSearchWords(jsonArray.getJSONObject(i).getString("SearchWords"));
                objItemMaster.setImageName(jsonArray.getJSONObject(i).getString("ImageName"));
                if (!jsonArray.getJSONObject(i).getString("SortOrder").equals("null")) {
                    objItemMaster.setSortOrder(jsonArray.getJSONObject(i).getInt("SortOrder"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objItemMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objItemMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objItemMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objItemMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objItemMaster.setIsFavourite(jsonArray.getJSONObject(i).getBoolean("IsFavourite"));
                objItemMaster.setBarCode(jsonArray.getJSONObject(i).getString("BarCode"));

                /// Extra
                //objItemMaster.setItemType(jsonArray.getJSONObject(i).getString("ItemType"));
                objItemMaster.setUnit(jsonArray.getJSONObject(i).getString("Unit"));
                objItemMaster.setActualSellPrice(jsonArray.getJSONObject(i).getDouble("ActualSellPrice"));
                objItemMaster.setLinktoOrderMasterId(jsonArray.getJSONObject(i).getLong("linktoOrderMasterId"));
                objItemMaster.setItemModifierIds(jsonArray.getJSONObject(i).getString("ItemModifierMasterIds"));
                objItemMaster.setQuantity(jsonArray.getJSONObject(i).getInt("Quantity"));
                objItemMaster.setRemark(jsonArray.getJSONObject(i).getString("Remark"));
                objItemMaster.setOptionValueTranIds(jsonArray.getJSONObject(i).getString("OptionValueTranIds"));
                lstItemMaster.add(objItemMaster);
            }
            return lstItemMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public ArrayList<ItemMaster> SelectAllItemMaster(int linktoCounterMasterId, int linktoOrderTypeMasterId, int linktoCategoryMasterId, String linktoItemTypeMasterId, int linktoBusinessMasterId) {
        ArrayList<ItemMaster> lstItemMaster = null;
        JSONObject jsonResponse;
        try {
            if (linktoCategoryMasterId == 0) {
                jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllItemMasterByCategoryMasterId + "/" + linktoCounterMasterId + "/" + linktoOrderTypeMasterId + "/" + null + "/" + linktoItemTypeMasterId + "/" + linktoBusinessMasterId);
            } else {
                jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllItemMasterByCategoryMasterId + "/" + linktoCounterMasterId + "/" + linktoOrderTypeMasterId + "/" + linktoCategoryMasterId + "/" + linktoItemTypeMasterId + "/" + linktoBusinessMasterId);
            }
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllItemMasterByCategoryMasterId + "Result");
                if (jsonArray != null) {
                    lstItemMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstItemMaster;
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<ItemMaster> SelectAllItemMasterModifier(int linktoBusinessMasterId) {
        ArrayList<ItemMaster> lstItemMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllItemMasterModifier + "/" + Globals.itemType + "/" + linktoBusinessMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllItemMasterModifier + "Result");
                if (jsonArray != null) {
                    lstItemMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstItemMaster;
        } catch (Exception ex) {
            return null;
        }
    }


    public ItemMaster SelectItemMaster(int counterMasterId, int linktoOrderTypeMasterId, int itemMasterId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectItemMaster + "/" + counterMasterId + "/" + linktoOrderTypeMasterId + "/" + itemMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectItemMaster + "Result");
                if (jsonObject != null) {
                    return SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }

    }

    public ArrayList<ItemMaster> SelectAllOrderItemByTableMasterIds(String tableMasterId) {
        ArrayList<ItemMaster> lstItemMaster = null;
        Date date;
        try {
            date = new Date();
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllOrderItemByTableMasterIds + "/" + sdfControlDateFormat.format(date) + "/" + tableMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllOrderItemByTableMasterIds + "Result");
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

