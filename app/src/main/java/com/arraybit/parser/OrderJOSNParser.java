package com.arraybit.parser;

import android.util.Log;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TaxMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderJOSNParser {

    public String InsertOrderMaster = "InsertOrderMaster";
    public String UpdateOrderMasterStatus = "UpdateOrderMasterStatus";
    public String SelectOrderNumber = "SelectOrderNumber";
    public String SelectAllOrderMaster = "SelectAllOrderMasterByOrderStatusMasterId";
    public String SelectAllOrderMasterByFromDate = "SelectAllOrderMasterByFromDate";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    SimpleDateFormat sdfControlDateTimeFormat = new SimpleDateFormat("d/M/yyyy HH:mm", Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    private OrderMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        OrderMaster objOrderMaster = null;
        try {
            if (jsonObject != null) {
                objOrderMaster = new OrderMaster();
                objOrderMaster.setOrderMasterId(jsonObject.getLong("OrderMasterId"));
                objOrderMaster.setOrderNumber(jsonObject.getString("OrderNumber"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("OrderDateTime"));
                objOrderMaster.setOrderDateTime(sdfControlDateTimeFormat.format(dt));
                objOrderMaster.setOrderTime(new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).format(dt.getTime()));
                objOrderMaster.setlinktoCounterMasterId((short) jsonObject.getInt("linktoCounterMasterId"));
                objOrderMaster.setlinktoTableMasterIds(jsonObject.getString("linktoTableMasterIds"));
                if (!jsonObject.getString("linktoWaiterMasterId").equals("null")) {
                    objOrderMaster.setlinktoWaiterMasterId(jsonObject.getInt("linktoWaiterMasterId"));
                }
                if (!jsonObject.getString("linktoCustomerMasterId").equals("null")) {
                    objOrderMaster.setlinktoCustomerMasterId((short) jsonObject.getInt("linktoCustomerMasterId"));
                }
                if (!jsonObject.getString("linktoOrderStatusMasterId").equals("null")) {
                    objOrderMaster.setlinktoOrderStatusMasterId(Short.valueOf(jsonObject.getString("linktoOrderStatusMasterId")));
                }
                objOrderMaster.setlinktoOrderStatusMasterId((short) jsonObject.getInt("linktoOrderStatusMasterId"));
                objOrderMaster.setTotalAmount(jsonObject.getDouble("TotalAmount"));
                objOrderMaster.setTotalTax(jsonObject.getDouble("TotalTax"));
                objOrderMaster.setDiscount(jsonObject.getDouble("Discount"));
                objOrderMaster.setDiscountPercentage(jsonObject.getDouble("DiscountPercentage"));
                objOrderMaster.setExtraAmount(jsonObject.getDouble("ExtraAmount"));
                objOrderMaster.setTotalItemPoint((short) jsonObject.getInt("TotalItemPoint"));
                objOrderMaster.setTotalDeductedPoint((short) jsonObject.getInt("TotalDeductedPoint"));
                objOrderMaster.setRemark(jsonObject.getString("Remark"));
                if (!jsonObject.getString("linktoSalesMasterId").equals("null")) {
                    objOrderMaster.setlinktoSalesMasterId(jsonObject.getLong("linktoSalesMasterId"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objOrderMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objOrderMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("UpdateDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                    objOrderMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                }
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objOrderMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objOrderMaster.setlinktoOrderTypeMasterId((short) jsonObject.getInt("linktoOrderTypeMasterId"));

                /// Extra
                objOrderMaster.setCounter(jsonObject.getString("Counter"));
                objOrderMaster.setCustomer(jsonObject.getString("Customer"));
                objOrderMaster.setOrderType(jsonObject.getString("OrderType"));
                objOrderMaster.setTableName(jsonObject.getString("TableName"));
                objOrderMaster.setTotalKOT(jsonObject.getInt("TotalKOT"));
            }
            return objOrderMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<OrderMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<OrderMaster> lstOrderMaster = new ArrayList<>();
        OrderMaster objOrderMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objOrderMaster = new OrderMaster();
                objOrderMaster.setOrderMasterId(jsonArray.getJSONObject(i).getLong("OrderMasterId"));
                objOrderMaster.setOrderNumber(jsonArray.getJSONObject(i).getString("OrderNumber"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("OrderDateTime"));
                objOrderMaster.setOrderDateTime(sdfControlDateTimeFormat.format(dt));
                objOrderMaster.setOrderTime(new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).format(dt.getTime()));
                objOrderMaster.setlinktoCounterMasterId((short) jsonArray.getJSONObject(i).getInt("linktoCounterMasterId"));
                objOrderMaster.setlinktoTableMasterIds(jsonArray.getJSONObject(i).getString("linktoTableMasterIds"));
                if (!jsonArray.getJSONObject(i).getString("linktoWaiterMasterId").equals("null")) {
                    objOrderMaster.setlinktoWaiterMasterId(jsonArray.getJSONObject(i).getInt("linktoWaiterMasterId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoCustomerMasterId").equals("null")) {
                    objOrderMaster.setlinktoCustomerMasterId((short) jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoOrderStatusMasterId").equals("null")) {
                    objOrderMaster.setlinktoOrderStatusMasterId(Short.valueOf(jsonArray.getJSONObject(i).getString("linktoOrderStatusMasterId")));
                }
                objOrderMaster.setTotalAmount(jsonArray.getJSONObject(i).getDouble("TotalAmount"));
                objOrderMaster.setTotalTax(jsonArray.getJSONObject(i).getDouble("TotalTax"));
                objOrderMaster.setDiscount(jsonArray.getJSONObject(i).getDouble("Discount"));
                objOrderMaster.setDiscountPercentage(jsonArray.getJSONObject(i).getDouble("DiscountPercentage"));
                objOrderMaster.setExtraAmount(jsonArray.getJSONObject(i).getDouble("ExtraAmount"));
                objOrderMaster.setTotalItemPoint((short) jsonArray.getJSONObject(i).getInt("TotalItemPoint"));
                objOrderMaster.setTotalDeductedPoint((short) jsonArray.getJSONObject(i).getInt("TotalDeductedPoint"));
                objOrderMaster.setRemark(jsonArray.getJSONObject(i).getString("Remark"));
                if (!jsonArray.getJSONObject(i).getString("linktoSalesMasterId").equals("null")) {
                    objOrderMaster.setlinktoSalesMasterId(jsonArray.getJSONObject(i).getLong("linktoSalesMasterId"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objOrderMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objOrderMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("UpdateDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                    objOrderMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objOrderMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objOrderMaster.setlinktoOrderTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoOrderTypeMasterId"));

                /// Extra
                objOrderMaster.setCounter(jsonArray.getJSONObject(i).getString("Counter"));
                objOrderMaster.setCustomer(jsonArray.getJSONObject(i).getString("Customer"));
                objOrderMaster.setOrderType(jsonArray.getJSONObject(i).getString("OrderType"));
                objOrderMaster.setTableName(jsonArray.getJSONObject(i).getString("TableName"));
                objOrderMaster.setTotalItem(jsonArray.getJSONObject(i).getInt("TotalItem"));
                objOrderMaster.setTotalKOT(jsonArray.getJSONObject(i).getInt("TotalKOT"));

                lstOrderMaster.add(objOrderMaster);
            }
            return lstOrderMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public String InsertOrderMaster(OrderMaster objOrderMaster, ArrayList<ItemMaster> alOrderItemTran ,ArrayList<TaxMaster> alTaxMaster) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("orderMaster");
            stringer.object();

            stringer.key("OrderNumber").value(objOrderMaster.getOrderNumber());
            stringer.key("OrderDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoCounterMasterId").value(objOrderMaster.getlinktoCounterMasterId());
            stringer.key("linktoTableMasterIds").value(objOrderMaster.getlinktoTableMasterIds());
            stringer.key("linktoWaiterMasterId").value(objOrderMaster.getlinktoWaiterMasterId());
            stringer.key("linktoBusinessMasterId").value(objOrderMaster.getLinktoBusinessMasterId());
            if(objOrderMaster.getlinktoCustomerMasterId() > 0) {
                stringer.key("linktoCustomerMasterId").value(objOrderMaster.getlinktoCustomerMasterId());
            }
            stringer.key("linktoOrderTypeMasterId").value(objOrderMaster.getlinktoOrderTypeMasterId());
            stringer.key("linktoOrderStatusMasterId").value(null);
            stringer.key("TotalAmount").value(objOrderMaster.getTotalAmount());
            stringer.key("TotalTax").value(objOrderMaster.getTotalTax());
            stringer.key("Discount").value(objOrderMaster.getDiscount());
            stringer.key("ExtraAmount").value(objOrderMaster.getExtraAmount());
            stringer.key("TotalItemPoint").value(objOrderMaster.getTotalItemPoint());
            stringer.key("TotalDeductedPoint").value(objOrderMaster.getTotalDeductedPoint());
            stringer.key("Remark").value(objOrderMaster.getRemark());
            stringer.key("NetAmount").value(objOrderMaster.getNetAmount());
            stringer.key("RateIndex").value(objOrderMaster.getRateIndex());
            //stringer.key("linktoSalesMasterId").value(null);
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdCreatedBy").value(objOrderMaster.getlinktoUserMasterIdCreatedBy());

            stringer.endObject();

            stringer.key("lstOrderItemTran");
            stringer.array();

            for (int i = 0; i < alOrderItemTran.size(); i++) {
                stringer.object();
                stringer.key("ItemMasterId").value(alOrderItemTran.get(i).getItemMasterId());
                stringer.key("Quantity").value(alOrderItemTran.get(i).getQuantity());
                stringer.key("ActualSellPrice").value(alOrderItemTran.get(i).getActualSellPrice());
                stringer.key("Remark").value(alOrderItemTran.get(i).getRemark());
                stringer.key("Tax1").value(alOrderItemTran.get(i).getTax1());
                stringer.key("Tax2").value(alOrderItemTran.get(i).getTax2());
                stringer.key("Tax3").value(alOrderItemTran.get(i).getTax3());
                stringer.key("Tax4").value(alOrderItemTran.get(i).getTax4());
                stringer.key("Tax5").value(alOrderItemTran.get(i).getTax5());
                stringer.key("IsRateTaxInclusive").value(alOrderItemTran.get(i).getIsRateTaxInclusive());
                stringer.key("lstOrderItemModifierTran");
                stringer.array();
                for (int j = 0; j < alOrderItemTran.get(i).getAlOrderItemModifierTran().size(); j++) {
                    stringer.object();
                    stringer.key("ItemModifierMasterIds").value(alOrderItemTran.get(i).getAlOrderItemModifierTran().get(j).getItemModifierIds());
                    stringer.key("ActualSellPrice").value(alOrderItemTran.get(i).getAlOrderItemModifierTran().get(j).getActualSellPrice());
                    stringer.endObject();
                }
                stringer.endArray();
                stringer.endObject();
            }
            stringer.endArray();

            stringer.key("lstTaxMaster");
            stringer.array();

            if(alTaxMaster!=null && alTaxMaster.size()!=0) {
                for (int i = 0; i < alTaxMaster.size(); i++) {
                    stringer.object();
                    stringer.key("TaxMasterId").value(alTaxMaster.get(i).getTaxMasterId());
                    stringer.key("TaxName").value(alTaxMaster.get(i).getTaxName());
                    stringer.key("TaxRate").value(alTaxMaster.get(i).getTaxRate());
                    stringer.key("IsPercentage").value(alTaxMaster.get(i).getIsPercentage());
                    stringer.endObject();
                }
            }
            stringer.endArray();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertOrderMaster, stringer);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertOrderMaster + "Result");
                if (jsonObject.getInt("ErrorCode") == 0) {
                    return String.valueOf(jsonObject.getLong("ErrorNumber"));
                } else {
                    return String.valueOf(jsonObject.getInt("ErrorCode"));
                }
            }
            return "-1";
        } catch (Exception ex) {
            return "-1";
        }
    }

    public String UpdateOrderMasterStatus(OrderMaster objOrderMaster, boolean isUpdateTotal) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("orderMaster");
            stringer.object();

            stringer.key("OrderMasterId").value(objOrderMaster.getOrderMasterId());
            stringer.key("linktoOrderStatusMasterId").value(objOrderMaster.getlinktoOrderStatusMasterId());
            stringer.key("linktoWaiterMasterId").value(objOrderMaster.getlinktoWaiterMasterId());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("TotalAmount").value(objOrderMaster.getTotalAmount());
            stringer.key("linktoUserMasterIdUpdatedBy").value(objOrderMaster.getlinktoUserMasterIdUpdatedBy());

            stringer.endObject();
            if (isUpdateTotal) {
                stringer.key("isUpdateTotal").value("1");
            } else {
                stringer.key("isUpdateTotal").value("0");
            }

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateOrderMasterStatus, stringer);
            if(jsonResponse!=null){
                JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateOrderMasterStatus + "Result");
                return String.valueOf(jsonObject.getInt("ErrorCode"));
            }else{
                return "-1";
            }
        } catch (Exception ex) {
            return "-1";
        }
    }

    public String SelectOrderNumber(int linktoBusinessMasterId) {
        String number = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectOrderNumber + "/" + linktoBusinessMasterId);
            if (jsonResponse != null) {
                number = jsonResponse.getString(this.SelectOrderNumber + "Result");
            }
            return number;
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<OrderMaster> SelectAllOrderMaster(int linktoCounterMasterId, int linktoOrderStatusMasterId, String linktoTableMasterIds, String linktoOrderTypeMasterId, int linktoBusinessMasterId) {
        ArrayList<OrderMaster> lstOrderMaster = null;
        JSONObject jsonResponse;
        Date date;
        try {
            date = new Date();
            if (linktoOrderStatusMasterId == 0) {
                jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllOrderMaster + "/" + linktoCounterMasterId + "/" + null + "/" + linktoTableMasterIds + "/" + linktoOrderTypeMasterId + "/" + sdfControlDateFormat.format(date) + "/" + linktoBusinessMasterId);
            } else {
                jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllOrderMaster + "/" + linktoCounterMasterId + "/" + linktoOrderStatusMasterId + "/" + linktoTableMasterIds + "/" + linktoOrderTypeMasterId + "/" + sdfControlDateFormat.format(date) + "/" + linktoBusinessMasterId);
            }
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllOrderMaster + "Result");
                if (jsonArray != null) {
                    lstOrderMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstOrderMaster;
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<OrderMaster> SelectAllOrderMasterByFromDate(int linktoCounterMasterId, int linktoBusinessMasterId) {
        ArrayList<OrderMaster> lstOrderMaster = null;
        Date date;
        try {
            date = new Date();
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllOrderMasterByFromDate + "/" + linktoCounterMasterId + "/" + sdfControlDateFormat.format(date) + "/" + linktoBusinessMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllOrderMasterByFromDate + "Result");
                if (jsonArray != null) {
                    lstOrderMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstOrderMaster;
        } catch (Exception ex) {
            return null;
        }
    }

}
