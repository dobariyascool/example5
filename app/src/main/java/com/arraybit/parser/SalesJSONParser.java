package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.SalesMaster;
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

public class SalesJSONParser
{
    public String InsertSalesMaster = "InsertSalesMaster";
    public String UpdateSalesMaster = "UpdateSalesMaster";
    public String SelectSalesMaster = "SelectSalesMaster";
    public String SelectBillNumber = "SelectBillNumber";
    public String SelectAllSalesMaster = "SelectAllSalesMaster";
    public String SelectAllSalesMasterBillNumber = "SelectAllSalesMasterBillNumber";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    private SalesMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        SalesMaster objSalesMaster = null;
        try {
            if (jsonObject != null) {
                objSalesMaster = new SalesMaster();
                objSalesMaster.setSalesMasterId(jsonObject.getLong("SalesMasterId"));
                objSalesMaster.setBillNumber(jsonObject.getString("BillNumber"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("BillDateTime"));
                objSalesMaster.setBillDateTime(sdfControlDateFormat.format(dt));
                objSalesMaster.setlinktoCounterMasterId((short)jsonObject.getInt("linktoCounterMasterId"));
                objSalesMaster.setlinktoTableMasterIds(jsonObject.getString("linktoTableMasterIds"));
                if (!jsonObject.getString("linktoWaiterMasterId").equals("null")) {
                    objSalesMaster.setlinktoWaiterMasterId(jsonObject.getInt("linktoWaiterMasterId"));
                }
                if (!jsonObject.getString("linktoWaiterMasterIdCaptain").equals("null")) {
                    objSalesMaster.setlinktoWaiterMasterIdCaptain(jsonObject.getInt("linktoWaiterMasterIdCaptain"));
                }
                if (!jsonObject.getString("linktoCustomerMasterId").equals("null")) {
                    objSalesMaster.setlinktoCustomerMasterId(jsonObject.getInt("linktoCustomerMasterId"));
                }
                objSalesMaster.setlinktoOrderTypeMasterId((short)jsonObject.getInt("linktoOrderTypeMasterId"));
                objSalesMaster.setlinktoOrderStatusMasterId((short)jsonObject.getInt("linktoOrderStatusMasterId"));
                objSalesMaster.setTotalAmount(jsonObject.getDouble("TotalAmount"));
                objSalesMaster.setTotalTax(jsonObject.getDouble("TotalTax"));
                objSalesMaster.setDiscountPercentage(jsonObject.getDouble("DiscountPercentage"));
                objSalesMaster.setDiscountAmount(jsonObject.getDouble("DiscountAmount"));
                objSalesMaster.setExtraAmount(jsonObject.getDouble("ExtraAmount"));
                objSalesMaster.setTotalItemDiscount(jsonObject.getDouble("TotalItemDiscount"));
                objSalesMaster.setTotalItemTax(jsonObject.getDouble("TotalItemTax"));
                objSalesMaster.setNetAmount(jsonObject.getDouble("NetAmount"));
                objSalesMaster.setPaidAmount(jsonObject.getDouble("PaidAmount"));
                objSalesMaster.setBalanceAmount(jsonObject.getDouble("BalanceAmount"));
                objSalesMaster.setRemark(jsonObject.getString("Remark"));
                objSalesMaster.setIscomplimentary(jsonObject.getBoolean("Iscomplimentary"));
                objSalesMaster.setTotalItemPoint((short)jsonObject.getInt("TotalItemPoint"));
                objSalesMaster.setTotalDeductedPoint((short)jsonObject.getInt("TotalDeductedPoint"));
                objSalesMaster.setlinktoBusinessMasterId((short)jsonObject.getInt("linktoBusinessMasterId"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objSalesMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objSalesMaster.setlinktoUserMasterIdCreatedBy((short)jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                objSalesMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objSalesMaster.setlinktoUserMasterIdUpdatedBy((short)jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objSalesMaster.setCounter(jsonObject.getString("Counter"));
                objSalesMaster.setWaiter(jsonObject.getString("Waiter"));
                objSalesMaster.setWaiterCaptain(jsonObject.getString("WaiterCaptain"));
                objSalesMaster.setCustomer(jsonObject.getString("Customer"));
                objSalesMaster.setOrderType(jsonObject.getString("OrderType"));
                objSalesMaster.setOrderStatus(jsonObject.getString("OrderStatus"));
                objSalesMaster.setBusiness(jsonObject.getString("Business"));
            }
            return objSalesMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<SalesMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<SalesMaster> lstSalesMaster = new ArrayList<>();
        SalesMaster objSalesMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objSalesMaster = new SalesMaster();
                objSalesMaster.setSalesMasterId(jsonArray.getJSONObject(i).getLong("SalesMasterId"));
                objSalesMaster.setBillNumber(jsonArray.getJSONObject(i).getString("BillNumber"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("BillDateTime"));
                objSalesMaster.setBillDateTime(sdfControlDateFormat.format(dt));
                objSalesMaster.setlinktoCounterMasterId((short)jsonArray.getJSONObject(i).getInt("linktoCounterMasterId"));
                objSalesMaster.setlinktoTableMasterIds(jsonArray.getJSONObject(i).getString("linktoTableMasterIds"));
                if (!jsonArray.getJSONObject(i).getString("linktoWaiterMasterId").equals("null")) {
                    objSalesMaster.setlinktoWaiterMasterId(jsonArray.getJSONObject(i).getInt("linktoWaiterMasterId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoWaiterMasterIdCaptain").equals("null")) {
                    objSalesMaster.setlinktoWaiterMasterIdCaptain(jsonArray.getJSONObject(i).getInt("linktoWaiterMasterIdCaptain"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoCustomerMasterId").equals("null")) {
                    objSalesMaster.setlinktoCustomerMasterId(jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
                }
                objSalesMaster.setlinktoOrderTypeMasterId((short)jsonArray.getJSONObject(i).getInt("linktoOrderTypeMasterId"));
                objSalesMaster.setlinktoOrderStatusMasterId((short)jsonArray.getJSONObject(i).getInt("linktoOrderStatusMasterId"));
                objSalesMaster.setTotalAmount(jsonArray.getJSONObject(i).getDouble("TotalAmount"));
                objSalesMaster.setTotalTax(jsonArray.getJSONObject(i).getDouble("TotalTax"));
                objSalesMaster.setDiscountPercentage(jsonArray.getJSONObject(i).getDouble("DiscountPercentage"));
                objSalesMaster.setDiscountAmount(jsonArray.getJSONObject(i).getDouble("DiscountAmount"));
                objSalesMaster.setExtraAmount(jsonArray.getJSONObject(i).getDouble("ExtraAmount"));
                objSalesMaster.setTotalItemDiscount(jsonArray.getJSONObject(i).getDouble("TotalItemDiscount"));
                objSalesMaster.setTotalItemTax(jsonArray.getJSONObject(i).getDouble("TotalItemTax"));
                objSalesMaster.setNetAmount(jsonArray.getJSONObject(i).getDouble("NetAmount"));
                objSalesMaster.setPaidAmount(jsonArray.getJSONObject(i).getDouble("PaidAmount"));
                objSalesMaster.setBalanceAmount(jsonArray.getJSONObject(i).getDouble("BalanceAmount"));
                objSalesMaster.setRemark(jsonArray.getJSONObject(i).getString("Remark"));
                objSalesMaster.setIscomplimentary(jsonArray.getJSONObject(i).getBoolean("Iscomplimentary"));
                objSalesMaster.setTotalItemPoint((short)jsonArray.getJSONObject(i).getInt("TotalItemPoint"));
                objSalesMaster.setTotalDeductedPoint((short)jsonArray.getJSONObject(i).getInt("TotalDeductedPoint"));
                objSalesMaster.setlinktoBusinessMasterId((short)jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objSalesMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objSalesMaster.setlinktoUserMasterIdCreatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                objSalesMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objSalesMaster.setlinktoUserMasterIdUpdatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objSalesMaster.setCounter(jsonArray.getJSONObject(i).getString("Counter"));
                objSalesMaster.setWaiter(jsonArray.getJSONObject(i).getString("Waiter"));
                objSalesMaster.setWaiterCaptain(jsonArray.getJSONObject(i).getString("WaiterCaptain"));
                objSalesMaster.setCustomer(jsonArray.getJSONObject(i).getString("Customer"));
                objSalesMaster.setOrderType(jsonArray.getJSONObject(i).getString("OrderType"));
                objSalesMaster.setOrderStatus(jsonArray.getJSONObject(i).getString("OrderStatus"));
                objSalesMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstSalesMaster.add(objSalesMaster);
            }
            return lstSalesMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public String InsertSalesMaster(SalesMaster objSalesMaster,ArrayList<ItemMaster> alSalesItemTran,ArrayList<TaxMaster> alTaxMaster,ArrayList<OrderMaster> alOrderMaster) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("salesMaster");
            stringer.object();

            stringer.key("BillNumber").value(objSalesMaster.getBillNumber());
            stringer.key("BillDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoCounterMasterId").value(objSalesMaster.getlinktoCounterMasterId());
            stringer.key("linktoTableMasterIds").value(objSalesMaster.getlinktoTableMasterIds());
            stringer.key("linktoWaiterMasterId").value(objSalesMaster.getlinktoWaiterMasterId());
            stringer.key("linktoWaiterMasterIdCaptain").value(objSalesMaster.getlinktoWaiterMasterIdCaptain());
            if(objSalesMaster.getlinktoCustomerMasterId()>0) {
                stringer.key("linktoCustomerMasterId").value(objSalesMaster.getlinktoCustomerMasterId());
            }
            stringer.key("linktoOrderTypeMasterId").value(objSalesMaster.getlinktoOrderTypeMasterId());
            stringer.key("linktoOrderStatusMasterId").value(null);
            stringer.key("TotalAmount").value(objSalesMaster.getTotalAmount());
            stringer.key("TotalTax").value(objSalesMaster.getTotalTax());
            stringer.key("DiscountPercentage").value(objSalesMaster.getDiscountPercentage());
            stringer.key("DiscountAmount").value(objSalesMaster.getDiscountAmount());
            stringer.key("ExtraAmount").value(objSalesMaster.getExtraAmount());
            stringer.key("TotalItemDiscount").value(objSalesMaster.getTotalItemDiscount());
            stringer.key("TotalItemTax").value(objSalesMaster.getTotalItemTax());
            stringer.key("NetAmount").value(objSalesMaster.getNetAmount());
            stringer.key("PaidAmount").value(objSalesMaster.getPaidAmount());
            stringer.key("BalanceAmount").value(objSalesMaster.getBalanceAmount());
            stringer.key("Rounding").value(objSalesMaster.getRounding());
            stringer.key("Remark").value(objSalesMaster.getRemark());
            stringer.key("Iscomplimentary").value(objSalesMaster.getIscomplimentary());
            stringer.key("TotalItemPoint").value(objSalesMaster.getTotalItemPoint());
            stringer.key("TotalDeductedPoint").value(objSalesMaster.getTotalDeductedPoint());
            stringer.key("linktoBusinessMasterId").value(objSalesMaster.getlinktoBusinessMasterId());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdCreatedBy").value(objSalesMaster.getlinktoUserMasterIdCreatedBy());
            stringer.key("RateIndex").value(objSalesMaster.getRateIndex());

            stringer.endObject();

            stringer.key("lstSalesItemTran");
            stringer.array();

            for(int i=0;i<alSalesItemTran.size();i++) {
                stringer.object();
                stringer.key("ItemMasterId").value(alSalesItemTran.get(i).getItemMasterId());
                stringer.key("linktoUnitMasterId").value(alSalesItemTran.get(i).getlinktoUnitMasterId());
                stringer.key("ItemCode").value(alSalesItemTran.get(i).getItemCode());
                stringer.key("ItemName").value(alSalesItemTran.get(i).getItemName());
                stringer.key("Quantity").value(alSalesItemTran.get(i).getQuantity());
                stringer.key("ActualSellPrice").value(alSalesItemTran.get(i).getActualSellPrice());
                stringer.key("Remark").value(alSalesItemTran.get(i).getRemark());
                stringer.key("lstOrderItemModifierTran");
                stringer.array();
                for(int j=0;j<alSalesItemTran.get(i).getAlOrderItemModifierTran().size();j++){
                    stringer.object();
                    stringer.key("ItemModifierMasterIds").value(alSalesItemTran.get(i).getAlOrderItemModifierTran().get(j).getItemModifierIds());
                    stringer.key("ActualSellPrice").value(alSalesItemTran.get(i).getAlOrderItemModifierTran().get(j).getActualSellPrice());
                    stringer.endObject();
                }
                stringer.endArray();
                stringer.endObject();
            }
            stringer.endArray();

            stringer.key("lstTaxMaster");
            stringer.array();
            for(int i=0;i<alTaxMaster.size();i++) {
                stringer.object();
                stringer.key("TaxMasterId").value(alTaxMaster.get(i).getTaxMasterId());
                stringer.key("TaxName").value(alTaxMaster.get(i).getTaxName());
                stringer.key("TaxRate").value(alTaxMaster.get(i).getTaxRate());
                stringer.key("IsPercentage").value(alTaxMaster.get(i).getIsPercentage());
                stringer.endObject();
            }
            stringer.endArray();

            stringer.key("lstOrderMaster");
            stringer.array();
            for(int i=0;i<alOrderMaster.size();i++){
                stringer.object();
                stringer.key("OrderMasterId").value(alOrderMaster.get(i).getOrderMasterId());
                stringer.endObject();
            }
            stringer.endArray();
            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertSalesMaster, stringer);
            if(jsonResponse!=null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertSalesMaster + "Result");
                return String.valueOf(jsonObject.getInt("ErrorCode"));
            }
            else
            {
                return "-1";
            }
        }
        catch (Exception ex) {
            return "-1";
        }
    }

    public String UpdateSalesMaster(SalesMaster objSalesMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("salesMaster");
            stringer.object();

            stringer.key("SalesMasterId").value(objSalesMaster.getSalesMasterId());
            stringer.key("BillNumber").value(objSalesMaster.getBillNumber());
            dt = sdfControlDateFormat.parse(objSalesMaster.getBillDateTime());
            stringer.key("BillDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoCounterMasterId").value(objSalesMaster.getlinktoCounterMasterId());
            stringer.key("linktoTableMasterIds").value(objSalesMaster.getlinktoTableMasterIds());
            stringer.key("linktoWaiterMasterId").value(objSalesMaster.getlinktoWaiterMasterId());
            stringer.key("linktoWaiterMasterIdCaptain").value(objSalesMaster.getlinktoWaiterMasterIdCaptain());
            stringer.key("linktoCustomerMasterId").value(objSalesMaster.getlinktoCustomerMasterId());
            stringer.key("linktoOrderTypeMasterId").value(objSalesMaster.getlinktoOrderTypeMasterId());
            stringer.key("linktoOrderStatusMasterId").value(objSalesMaster.getlinktoOrderStatusMasterId());
            stringer.key("TotalAmount").value(objSalesMaster.getTotalAmount());
            stringer.key("TotalTax").value(objSalesMaster.getTotalTax());
            stringer.key("DiscountPercentage").value(objSalesMaster.getDiscountPercentage());
            stringer.key("DiscountAmount").value(objSalesMaster.getDiscountAmount());
            stringer.key("ExtraAmount").value(objSalesMaster.getExtraAmount());
            stringer.key("TotalItemDiscount").value(objSalesMaster.getTotalItemDiscount());
            stringer.key("TotalItemTax").value(objSalesMaster.getTotalItemTax());
            stringer.key("NetAmount").value(objSalesMaster.getNetAmount());
            stringer.key("PaidAmount").value(objSalesMaster.getPaidAmount());
            stringer.key("BalanceAmount").value(objSalesMaster.getBalanceAmount());
            stringer.key("Remark").value(objSalesMaster.getRemark());
            stringer.key("Iscomplimentary").value(objSalesMaster.getIscomplimentary());
            stringer.key("TotalItemPoint").value(objSalesMaster.getTotalItemPoint());
            stringer.key("TotalDeductedPoint").value(objSalesMaster.getTotalDeductedPoint());
            stringer.key("linktoBusinessMasterId").value(objSalesMaster.getlinktoBusinessMasterId());
            dt = sdfControlDateFormat.parse(objSalesMaster.getUpdateDateTime());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdUpdatedBy").value(objSalesMaster.getlinktoUserMasterIdUpdatedBy());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateSalesMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateSalesMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        }
        catch (Exception ex) {
            return "-1";
        }
    }



    public SalesMaster SelectSalesMaster(long salesMasterId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectSalesMaster + "/" + salesMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectSalesMaster + "Result");
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

    public ArrayList<SalesMaster> SelectAllSalesMaster() {
        ArrayList<SalesMaster> lstSalesMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllSalesMaster);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllSalesMaster + "Result");
                if (jsonArray != null) {
                    lstSalesMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstSalesMaster;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<SpinnerItem> SelectAllSalesMasterBillNumber() {
        ArrayList<SpinnerItem> lstSpinnerItem = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllSalesMasterBillNumber);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllSalesMasterBillNumber + "Result");
                if (jsonArray != null) {
                    lstSpinnerItem = new ArrayList<>();
                    SpinnerItem objSpinnerItem;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objSpinnerItem = new SpinnerItem();
                        objSpinnerItem.setText(jsonArray.getJSONObject(i).getString("BillNumber"));
                        objSpinnerItem.setValue(jsonArray.getJSONObject(i).getInt("SalesMasterId"));
                        lstSpinnerItem.add(objSpinnerItem);
                    }
                }
            }
            return lstSpinnerItem;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public String SelectBillNumber() {
        String number = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectBillNumber);
            if (jsonResponse != null) {
                number = jsonResponse.getString(this.SelectBillNumber + "Result");
            }
            return number;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
