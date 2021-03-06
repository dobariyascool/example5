package com.arraybit.parser;

import android.util.Log;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.OrderItemTran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderItemJSONParser {

    public String SelectAllOrderItemTran = "SelectAllOrderItemTranByOrderMasterId";
    public String UpdateOrderItemTranStatus = "UpdateOrderItemTranStatus";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    private OrderItemTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        OrderItemTran objOrderItemTran = null;
        try {
            if (jsonObject != null) {
                objOrderItemTran = new OrderItemTran();
                objOrderItemTran.setOrderItemTranId(jsonObject.getLong("OrderItemTranId"));
                objOrderItemTran.setlinktoOrderMasterId(jsonObject.getLong("linktoOrderMasterId"));
                objOrderItemTran.setlinktoItemMasterId(jsonObject.getInt("linktoItemMasterId"));
                objOrderItemTran.setQuantity((short) jsonObject.getInt("Quantity"));
                objOrderItemTran.setRate(jsonObject.getDouble("Rate"));
                objOrderItemTran.setItemPoint((short) jsonObject.getInt("ItemPoint"));
                objOrderItemTran.setDeductedPoint((short) jsonObject.getInt("DeductedPoint"));
                objOrderItemTran.setItemRemark(jsonObject.getString("ItemRemark"));
                if(!jsonObject.getString("linktoOrderStatusMasterId").equals("null")){
                    objOrderItemTran.setlinktoOrderStatusMasterId(Short.valueOf(jsonObject.getString("linktoOrderStatusMasterId")));
                }
                if(!jsonObject.getString("UpdateDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                    objOrderItemTran.setUpdateDateTime(sdfControlDateFormat.format(dt));
                }
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objOrderItemTran.setlinktoUserMasterIdUpdatedBy((short)jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objOrderItemTran.setOrder(jsonObject.getString("Order"));
                objOrderItemTran.setItem(jsonObject.getString("Item"));
                objOrderItemTran.setOrderStatus(jsonObject.getString("OrderStatus"));
                objOrderItemTran.setOrderItemTranIds(jsonObject.getString("OrderItemTranIds"));
                objOrderItemTran.setModifierRates(jsonObject.getString("ModifierRates"));
            }
            return objOrderItemTran;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<OrderItemTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<OrderItemTran> lstOrderItemTran = new ArrayList<>();
        OrderItemTran objOrderItemTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objOrderItemTran = new OrderItemTran();
                objOrderItemTran.setOrderItemTranId(jsonArray.getJSONObject(i).getLong("OrderItemTranId"));
                objOrderItemTran.setlinktoOrderMasterId(jsonArray.getJSONObject(i).getLong("linktoOrderMasterId"));
                objOrderItemTran.setlinktoItemMasterId(jsonArray.getJSONObject(i).getInt("linktoItemMasterId"));
                objOrderItemTran.setQuantity((short) jsonArray.getJSONObject(i).getInt("Quantity"));
                objOrderItemTran.setRate(jsonArray.getJSONObject(i).getDouble("Rate"));
                objOrderItemTran.setItemPoint((short) jsonArray.getJSONObject(i).getInt("ItemPoint"));
                objOrderItemTran.setDeductedPoint((short) jsonArray.getJSONObject(i).getInt("DeductedPoint"));
                objOrderItemTran.setItemRemark(jsonArray.getJSONObject(i).getString("ItemRemark"));
                if(!jsonArray.getJSONObject(i).getString("linktoOrderStatusMasterId").equals("null")){
                    objOrderItemTran.setlinktoOrderStatusMasterId(Short.valueOf(jsonArray.getJSONObject(i).getString("linktoOrderStatusMasterId")));
                }
                if(!jsonArray.getJSONObject(i).getString("UpdateDateTime").equals("null")) {
                    dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                    objOrderItemTran.setUpdateDateTime(sdfControlDateFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objOrderItemTran.setlinktoUserMasterIdUpdatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objOrderItemTran.setOrder(jsonArray.getJSONObject(i).getString("Order"));
                objOrderItemTran.setItem(jsonArray.getJSONObject(i).getString("Item"));
                objOrderItemTran.setOrderStatus(jsonArray.getJSONObject(i).getString("OrderStatus"));
                objOrderItemTran.setOrderItemTranIds(jsonArray.getJSONObject(i).getString("OrderItemTranIds"));
                objOrderItemTran.setModifierRates(jsonArray.getJSONObject(i).getString("ModifierRates"));
                objOrderItemTran.setRateIndex((short)jsonArray.getJSONObject(i).getInt("RateIndex"));
                lstOrderItemTran.add(objOrderItemTran);
            }
            return lstOrderItemTran;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public String UpdateOrderItemTranStatus(OrderItemTran objOrderItemTran) {
        dt=new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("orderItemTran");
            stringer.object();

            stringer.key("OrderItemTranIds").value(objOrderItemTran.getOrderItemTranIds());
            stringer.key("linktoOrderStatusMasterId").value(objOrderItemTran.getlinktoOrderStatusMasterId());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdUpdatedBy").value(objOrderItemTran.getlinktoUserMasterIdUpdatedBy());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateOrderItemTranStatus, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateOrderItemTranStatus + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        }
        catch (Exception ex) {
            return "-1";
        }
    }

    public ArrayList<OrderItemTran> SelectAllOrderItemTran(String orderMasterId) {
        ArrayList<OrderItemTran> lstOrderItemTran = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllOrderItemTran +"/"+orderMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllOrderItemTran + "Result");
                if (jsonArray != null) {
                    lstOrderItemTran = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstOrderItemTran;
        }
        catch (Exception ex) {
            return null;
        }
    }

}
