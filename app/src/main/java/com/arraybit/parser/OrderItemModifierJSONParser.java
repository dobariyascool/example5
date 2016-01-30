package com.arraybit.parser;

import android.net.ParseException;

import com.arraybit.global.Service;
import com.arraybit.modal.OrderItemModifierTran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

public class OrderItemModifierJSONParser {
    public String InsertOrderItemModifierTran = "InsertOrderItemModifierTran";
    public String UpdateOrderItemModifierTran = "UpdateOrderItemModifierTran";
    public String SelectOrderItemModifierTran = "SelectOrderItemModifierTran";

    private OrderItemModifierTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        OrderItemModifierTran objOrderItemModifierTran = null;
        try {
            if (jsonObject != null) {
                objOrderItemModifierTran = new OrderItemModifierTran();
                objOrderItemModifierTran.setOrderItemModifierTranId(jsonObject.getLong("OrderItemModifierTranId"));
                objOrderItemModifierTran.setlinktoOrderItemTranId(jsonObject.getLong("linktoOrderItemTranId"));
                objOrderItemModifierTran.setlinktoModifierMasterId(jsonObject.getInt("linktoModifierMasterId"));
                objOrderItemModifierTran.setRate(jsonObject.getDouble("Rate"));
            }
            return objOrderItemModifierTran;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<OrderItemModifierTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<OrderItemModifierTran> lstOrderItemModifierTran = new ArrayList<>();
        OrderItemModifierTran objOrderItemModifierTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objOrderItemModifierTran = new OrderItemModifierTran();
                objOrderItemModifierTran.setOrderItemModifierTranId(jsonArray.getJSONObject(i).getLong("OrderItemModifierTranId"));
                objOrderItemModifierTran.setlinktoOrderItemTranId(jsonArray.getJSONObject(i).getLong("linktoOrderItemTranId"));
                objOrderItemModifierTran.setlinktoModifierMasterId(jsonArray.getJSONObject(i).getInt("linktoModifierMasterId"));
                objOrderItemModifierTran.setRate(jsonArray.getJSONObject(i).getDouble("Rate"));
                lstOrderItemModifierTran.add(objOrderItemModifierTran);
            }
            return lstOrderItemModifierTran;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public String InsertOrderItemModifierTran(OrderItemModifierTran objOrderItemModifierTran) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("orderItemModifierTran");
            stringer.object();

            stringer.key("linktoOrderItemTranId").value(objOrderItemModifierTran.getlinktoOrderItemTranId());
            stringer.key("linktoModifierMasterId").value(objOrderItemModifierTran.getlinktoModifierMasterId());
            stringer.key("Rate").value(objOrderItemModifierTran.getRate());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertOrderItemModifierTran, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertOrderItemModifierTran + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        } catch (Exception ex) {
            return "-1";
        }
    }

    public String UpdateOrderItemModifierTran(OrderItemModifierTran objOrderItemModifierTran) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("orderItemModifierTran");
            stringer.object();

            stringer.key("OrderItemModifierTranId").value(objOrderItemModifierTran.getOrderItemModifierTranId());
            stringer.key("linktoOrderItemTranId").value(objOrderItemModifierTran.getlinktoOrderItemTranId());
            stringer.key("linktoModifierMasterId").value(objOrderItemModifierTran.getlinktoModifierMasterId());
            stringer.key("Rate").value(objOrderItemModifierTran.getRate());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateOrderItemModifierTran, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateOrderItemModifierTran + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        } catch (Exception ex) {
            return "-1";
        }
    }

    public OrderItemModifierTran SelectOrderItemModifierTran(long orderItemModifierTranId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectOrderItemModifierTran + "/" + orderItemModifierTranId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectOrderItemModifierTran + "Result");
                if (jsonObject != null) {
                    return SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }
}
