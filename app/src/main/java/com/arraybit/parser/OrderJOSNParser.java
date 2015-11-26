package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.OrderMaster;

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
    public String UpdateOrderMaster = "UpdateOrderMaster";
    public String SelectOrderMaster = "SelectOrderMaster";
    public String SelectAllOrderMaster = "SelectAllOrderMasterByOrderStatusMasterId";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
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
                objOrderMaster.setOrderDateTime(sdfControlDateFormat.format(dt));
                objOrderMaster.setlinktoCounterMasterId((short)jsonObject.getInt("linktoCounterMasterId"));
                objOrderMaster.setlinktoTableMasterIds(jsonObject.getString("linktoTableMasterIds"));
                if (!jsonObject.getString("linktoWaiterMasterId").equals("null")) {
                    objOrderMaster.setlinktoWaiterMasterId(jsonObject.getInt("linktoWaiterMasterId"));
                }
                if (!jsonObject.getString("linktoCustomerMasterId").equals("null")) {
                    objOrderMaster.setlinktoCustomerMasterId((short)jsonObject.getInt("linktoCustomerMasterId"));
                }
                objOrderMaster.setlinktoOrderTypeMasterId((short) jsonObject.getInt("linktoOrderTypeMasterId"));
                objOrderMaster.setlinktoOrderStatusMasterId((short) jsonObject.getInt("linktoOrderStatusMasterId"));
                objOrderMaster.setTotalAmount(jsonObject.getDouble("TotalAmount"));
                objOrderMaster.setTotalTax(jsonObject.getDouble("TotalTax"));
                objOrderMaster.setDiscount(jsonObject.getDouble("Discount"));
                objOrderMaster.setExtraAmount(jsonObject.getDouble("ExtraAmount"));
                objOrderMaster.setTotalItemPoint((short) jsonObject.getInt("TotalItemPoint"));
                objOrderMaster.setTotalDeductedPoint((short) jsonObject.getInt("TotalDeductedPoint"));
                objOrderMaster.setRemark(jsonObject.getString("Remark"));
                if (!jsonObject.getString("linktoSalesMasterId").equals("null")) {
                    objOrderMaster.setlinktoSalesMasterId(jsonObject.getLong("linktoSalesMasterId"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objOrderMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objOrderMaster.setlinktoUserMasterIdCreatedBy((short)jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if(!jsonObject.getString("UpdateDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                    objOrderMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                }
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objOrderMaster.setlinktoUserMasterIdUpdatedBy((short)jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objOrderMaster.setCounter(jsonObject.getString("Counter"));
                objOrderMaster.setCustomer(jsonObject.getString("Customer"));
                objOrderMaster.setOrderType(jsonObject.getString("OrderType"));
                objOrderMaster.setTableName(jsonObject.getString("TableName"));
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
                objOrderMaster.setOrderDateTime(sdfControlDateFormat.format(dt));
                objOrderMaster.setlinktoCounterMasterId((short)jsonArray.getJSONObject(i).getInt("linktoCounterMasterId"));
                objOrderMaster.setlinktoTableMasterIds(jsonArray.getJSONObject(i).getString("linktoTableMasterIds"));
                if (!jsonArray.getJSONObject(i).getString("linktoWaiterMasterId").equals("null")) {
                    objOrderMaster.setlinktoWaiterMasterId(jsonArray.getJSONObject(i).getInt("linktoWaiterMasterId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoCustomerMasterId").equals("null")) {
                    objOrderMaster.setlinktoCustomerMasterId((short)jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
                }
                objOrderMaster.setlinktoOrderTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoOrderTypeMasterId"));
                objOrderMaster.setlinktoOrderStatusMasterId((short) jsonArray.getJSONObject(i).getInt("linktoOrderStatusMasterId"));
                objOrderMaster.setTotalAmount(jsonArray.getJSONObject(i).getDouble("TotalAmount"));
                objOrderMaster.setTotalTax(jsonArray.getJSONObject(i).getDouble("TotalTax"));
                objOrderMaster.setDiscount(jsonArray.getJSONObject(i).getDouble("Discount"));
                objOrderMaster.setExtraAmount(jsonArray.getJSONObject(i).getDouble("ExtraAmount"));
                objOrderMaster.setTotalItemPoint((short) jsonArray.getJSONObject(i).getInt("TotalItemPoint"));
                objOrderMaster.setTotalDeductedPoint((short) jsonArray.getJSONObject(i).getInt("TotalDeductedPoint"));
                objOrderMaster.setRemark(jsonArray.getJSONObject(i).getString("Remark"));
                if (!jsonArray.getJSONObject(i).getString("linktoSalesMasterId").equals("null")) {
                    objOrderMaster.setlinktoSalesMasterId(jsonArray.getJSONObject(i).getLong("linktoSalesMasterId"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objOrderMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objOrderMaster.setlinktoUserMasterIdCreatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if(!jsonArray.getJSONObject(i).getString("UpdateDateTime").equals("null")){
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                objOrderMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));}
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objOrderMaster.setlinktoUserMasterIdUpdatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objOrderMaster.setCounter(jsonArray.getJSONObject(i).getString("Counter"));
                objOrderMaster.setCustomer(jsonArray.getJSONObject(i).getString("Customer"));
                objOrderMaster.setOrderType(jsonArray.getJSONObject(i).getString("OrderType"));
                objOrderMaster.setTableName(jsonArray.getJSONObject(i).getString("TableName"));

                lstOrderMaster.add(objOrderMaster);
            }
            return lstOrderMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public String InsertOrderMaster(OrderMaster objOrderMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("orderMaster");
            stringer.object();

            stringer.key("OrderNumber").value(objOrderMaster.getOrderNumber());
            dt = sdfControlDateFormat.parse(objOrderMaster.getOrderDateTime());
            stringer.key("OrderDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoCounterMasterId").value(objOrderMaster.getlinktoCounterMasterId());
            stringer.key("linktoTableMasterIds").value(objOrderMaster.getlinktoTableMasterIds());
            stringer.key("linktoWaiterMasterId").value(objOrderMaster.getlinktoWaiterMasterId());
            stringer.key("linktoCustomerMasterId").value(objOrderMaster.getlinktoCustomerMasterId());
            stringer.key("linktoOrderTypeMasterId").value(objOrderMaster.getlinktoOrderTypeMasterId());
            stringer.key("linktoOrderStatusMasterId").value(objOrderMaster.getlinktoOrderStatusMasterId());
            stringer.key("TotalAmount").value(objOrderMaster.getTotalAmount());
            stringer.key("TotalTax").value(objOrderMaster.getTotalTax());
            stringer.key("Discount").value(objOrderMaster.getDiscount());
            stringer.key("ExtraAmount").value(objOrderMaster.getExtraAmount());
            stringer.key("TotalItemPoint").value(objOrderMaster.getTotalItemPoint());
            stringer.key("TotalDeductedPoint").value(objOrderMaster.getTotalDeductedPoint());
            stringer.key("Remark").value(objOrderMaster.getRemark());
            stringer.key("linktoSalesMasterId").value(objOrderMaster.getlinktoSalesMasterId());
            dt = sdfControlDateFormat.parse(objOrderMaster.getCreateDateTime());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdCreatedBy").value(objOrderMaster.getlinktoUserMasterIdCreatedBy());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertOrderMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertOrderMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        }
        catch (Exception ex) {
            return "-1";
        }
    }

    public String UpdateOrderMaster(OrderMaster objOrderMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("orderMaster");
            stringer.object();

            stringer.key("OrderMasterId").value(objOrderMaster.getOrderMasterId());
            stringer.key("OrderNumber").value(objOrderMaster.getOrderNumber());
            dt = sdfControlDateFormat.parse(objOrderMaster.getOrderDateTime());
            stringer.key("OrderDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoCounterMasterId").value(objOrderMaster.getlinktoCounterMasterId());
            stringer.key("linktoTableMasterIds").value(objOrderMaster.getlinktoTableMasterIds());
            stringer.key("linktoWaiterMasterId").value(objOrderMaster.getlinktoWaiterMasterId());
            stringer.key("linktoCustomerMasterId").value(objOrderMaster.getlinktoCustomerMasterId());
            stringer.key("linktoOrderTypeMasterId").value(objOrderMaster.getlinktoOrderTypeMasterId());
            stringer.key("linktoOrderStatusMasterId").value(objOrderMaster.getlinktoOrderStatusMasterId());
            stringer.key("TotalAmount").value(objOrderMaster.getTotalAmount());
            stringer.key("TotalTax").value(objOrderMaster.getTotalTax());
            stringer.key("Discount").value(objOrderMaster.getDiscount());
            stringer.key("ExtraAmount").value(objOrderMaster.getExtraAmount());
            stringer.key("TotalItemPoint").value(objOrderMaster.getTotalItemPoint());
            stringer.key("TotalDeductedPoint").value(objOrderMaster.getTotalDeductedPoint());
            stringer.key("Remark").value(objOrderMaster.getRemark());
            stringer.key("linktoSalesMasterId").value(objOrderMaster.getlinktoSalesMasterId());
            dt = sdfControlDateFormat.parse(objOrderMaster.getUpdateDateTime());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdUpdatedBy").value(objOrderMaster.getlinktoUserMasterIdUpdatedBy());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateOrderMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateOrderMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        }
        catch (Exception ex) {
            return "-1";
        }
    }

    public OrderMaster SelectOrderMaster(long orderMasterId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectOrderMaster + "/" + orderMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectOrderMaster + "Result");
                if (jsonObject != null) {
                    return SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return null;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<OrderMaster> SelectAllOrderMaster(int currentPage,int linktoCounterMasterId,int linktoOrderStatusMasterId) {
        ArrayList<OrderMaster> lstOrderMaster = null;
        try {
            Date date = new Date();
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllOrderMaster +"/"+currentPage+"/"+linktoCounterMasterId+"/"+linktoOrderStatusMasterId+"/"+sdfControlDateFormat.format(date));
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllOrderMaster + "Result");
                if (jsonArray != null) {
                    lstOrderMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstOrderMaster;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
