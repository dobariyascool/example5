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
    public String SelectAllItemSuggested = "SelectAllItemSuggested";
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
                objItemMaster.setXS_ImagePhysicalName(jsonObject.getString("xs_ImagePhysicalName"));
                objItemMaster.setSM_ImagePhysicalName(jsonObject.getString("sm_ImagePhysicalName"));
                objItemMaster.setMD_ImagePhysicalName(jsonObject.getString("md_ImagePhysicalName"));
                objItemMaster.setLG_ImagePhysicalName(jsonObject.getString("lg_ImagePhysicalName"));
                objItemMaster.setXL_ImagePhysicalName(jsonObject.getString("xl_ImagePhysicalName"));
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
                objItemMaster.setOptionValueTranIds(jsonObject.getString("OptionValueTranIds"));

                /// Extra
                //objItemMaster.setItemType(jsonObject.getString("ItemType"));
                objItemMaster.setUnit(jsonObject.getString("Unit"));
                objItemMaster.setActualSellPrice(jsonObject.getDouble("ActualSellPrice"));
                objItemMaster.setLinktoOrderMasterId(jsonObject.getLong("linktoOrderMasterId"));
                objItemMaster.setItemModifierIds(jsonObject.getString("ItemModifierMasterIds"));
                objItemMaster.setQuantity(jsonObject.getInt("Quantity"));
                objItemMaster.setRemark(jsonObject.getString("Remark"));
                objItemMaster.setTax(jsonObject.getString("Tax"));
                objItemMaster.setRateIndex((short) jsonObject.getInt("RateIndex"));
                objItemMaster.setTaxRate(jsonObject.getDouble("TaxRate"));
                objItemMaster.setTax1(jsonObject.getDouble("Tax1"));
                objItemMaster.setTax2(jsonObject.getDouble("Tax2"));
                objItemMaster.setTax3(jsonObject.getDouble("Tax3"));
                objItemMaster.setTax4(jsonObject.getDouble("Tax4"));
                objItemMaster.setTax5(jsonObject.getDouble("Tax5"));
                objItemMaster.setCategory(jsonObject.getString("Category"));
                objItemMaster.setIsDineInOnly(jsonObject.getBoolean("IsDineInOnly"));
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
                objItemMaster.setXS_ImagePhysicalName(jsonArray.getJSONObject(i).getString("xs_ImagePhysicalName"));
                objItemMaster.setSM_ImagePhysicalName(jsonArray.getJSONObject(i).getString("sm_ImagePhysicalName"));
                objItemMaster.setMD_ImagePhysicalName(jsonArray.getJSONObject(i).getString("md_ImagePhysicalName"));
                objItemMaster.setLG_ImagePhysicalName(jsonArray.getJSONObject(i).getString("lg_ImagePhysicalName"));
                objItemMaster.setXL_ImagePhysicalName(jsonArray.getJSONObject(i).getString("xl_ImagePhysicalName"));
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
                objItemMaster.setTax(jsonArray.getJSONObject(i).getString("Tax"));
                objItemMaster.setRateIndex((short) jsonArray.getJSONObject(i).getInt("RateIndex"));
                objItemMaster.setTaxRate(jsonArray.getJSONObject(i).getDouble("TaxRate"));
                objItemMaster.setTax1(jsonArray.getJSONObject(i).getDouble("Tax1"));
                objItemMaster.setTax2(jsonArray.getJSONObject(i).getDouble("Tax2"));
                objItemMaster.setTax3(jsonArray.getJSONObject(i).getDouble("Tax3"));
                objItemMaster.setTax4(jsonArray.getJSONObject(i).getDouble("Tax4"));
                objItemMaster.setTax5(jsonArray.getJSONObject(i).getDouble("Tax5"));
                objItemMaster.setCategory(jsonArray.getJSONObject(i).getString("Category"));
                objItemMaster.setIsDineInOnly(jsonArray.getJSONObject(i).getBoolean("IsDineInOnly"));
                lstItemMaster.add(objItemMaster);
            }
            return lstItemMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public ArrayList<ItemMaster> SelectAllItemMaster(int linktoCounterMasterId, int linktoOrderTypeMasterId, int linktoCategoryMasterId, String linktoItemTypeMasterId, int linktoBusinessMasterId, int isFavorite) {
        ArrayList<ItemMaster> lstItemMaster = null;
        JSONObject jsonResponse;
        try {
            if (linktoCategoryMasterId == 0) {
                jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllItemMasterByCategoryMasterId + "/" + linktoCounterMasterId + "/" + linktoOrderTypeMasterId + "/" + null + "/" + linktoItemTypeMasterId + "/" + linktoBusinessMasterId + "/" + isFavorite);
            } else {
                jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllItemMasterByCategoryMasterId + "/" + linktoCounterMasterId + "/" + linktoOrderTypeMasterId + "/" + linktoCategoryMasterId + "/" + linktoItemTypeMasterId + "/" + linktoBusinessMasterId + "/" + isFavorite);
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

    public ArrayList<ItemMaster> SelectAllItemMasterModifier(int linktoBusinessMasterId, int linktoItemMasterId) {
        ArrayList<ItemMaster> lstItemMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllItemMasterModifier + "/" + Globals.itemType + "/" + linktoBusinessMasterId + "/" + linktoItemMasterId);
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

    public ArrayList<ItemMaster> SelectAllItemSuggested(int linktoBusinessMasterId, int linktoItemMasterId, int rateIndex, int isDineIn) {
        ArrayList<ItemMaster> lstItemMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllItemSuggested + "/" + linktoItemMasterId + "/" + linktoBusinessMasterId + "/" + rateIndex + "/" + isDineIn);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllItemSuggested + "Result");
                if (jsonArray != null) {
                    lstItemMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstItemMaster;
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

