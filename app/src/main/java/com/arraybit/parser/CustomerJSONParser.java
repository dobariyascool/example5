package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.modal.RegisteredUserMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CustomerJSONParser {

    public String InsertCustomerMaster = "InsertCustomerMaster";
    public String UpdateCustomerMaster = "UpdateCustomerMaster";
    public String UpdateRegisteredUserMasterPassword = "UpdateRegisteredUserMasterPassword";
    public String SelectCustomerMaster = "SelectCustomerMaster";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    //region Class Methods
    private CustomerMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        CustomerMaster objCustomerMaster = null;
        try {
            if (jsonObject != null) {
                objCustomerMaster = new CustomerMaster();
                objCustomerMaster.setCustomerMasterId(jsonObject.getInt("CustomerMasterId"));
                objCustomerMaster.setShortName(jsonObject.getString("ShortName"));
                objCustomerMaster.setCustomerName(jsonObject.getString("CustomerName"));
                objCustomerMaster.setDescription(jsonObject.getString("Description"));
                objCustomerMaster.setContactPersonName(jsonObject.getString("ContactPersonName"));
                objCustomerMaster.setDesignation(jsonObject.getString("Designation"));
                objCustomerMaster.setPhone1(jsonObject.getString("Phone1"));
                objCustomerMaster.setIsPhone1DND(jsonObject.getBoolean("IsPhone1DND"));
                objCustomerMaster.setPhone2(jsonObject.getString("Phone2"));
                if (!jsonObject.getString("IsPhone2DND").equals("null")) {
                    objCustomerMaster.setIsPhone2DND(jsonObject.getBoolean("IsPhone2DND"));
                }
                objCustomerMaster.setEmail1(jsonObject.getString("Email1"));
                objCustomerMaster.setEmail2(jsonObject.getString("Email2"));
                objCustomerMaster.setFax(jsonObject.getString("Fax"));
                objCustomerMaster.setImageName(jsonObject.getString("ImageName"));
                if (!jsonObject.getString("BirthDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonObject.getString("BirthDate"));
                    objCustomerMaster.setBirthDate(sdfControlDateFormat.format(dt));
                }
                if (!jsonObject.getString("AnniversaryDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonObject.getString("AnniversaryDate"));
                    objCustomerMaster.setAnniversaryDate(sdfControlDateFormat.format(dt));
                }
                objCustomerMaster.setCustomerType((short)jsonObject.getInt("CustomerType"));
                if (!jsonObject.getString("IsFavourite").equals("null")) {
                    objCustomerMaster.setIsFavourite(jsonObject.getBoolean("IsFavourite"));
                }
                if (!jsonObject.getString("IsCredit").equals("null")) {
                    objCustomerMaster.setIsCredit(jsonObject.getBoolean("IsCredit"));
                }
                objCustomerMaster.setOpeningBalance(jsonObject.getDouble("OpeningBalance"));
                objCustomerMaster.setCreditDays((short) jsonObject.getInt("CreditDays"));
                objCustomerMaster.setCreditBalance(jsonObject.getDouble("CreditBalance"));
                objCustomerMaster.setCreditLimit(jsonObject.getDouble("CreditLimit"));
                objCustomerMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objCustomerMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objCustomerMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objCustomerMaster.setlinktoUserMasterIdUpdatedBy((short)jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objCustomerMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objCustomerMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                objCustomerMaster.setGender(jsonObject.getString("Gender"));
                objCustomerMaster.setPassword(jsonObject.getString("Password"));
                objCustomerMaster.setlinktoSourceMasterId((short)jsonObject.getInt("linktoSourceMasterId"));
            }
            return objCustomerMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<CustomerMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<CustomerMaster> lstCustomerMaster = new ArrayList<>();
        CustomerMaster objCustomerMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objCustomerMaster = new CustomerMaster();
                objCustomerMaster.setCustomerMasterId(jsonArray.getJSONObject(i).getInt("CustomerMasterId"));
                objCustomerMaster.setShortName(jsonArray.getJSONObject(i).getString("ShortName"));
                objCustomerMaster.setCustomerName(jsonArray.getJSONObject(i).getString("CustomerName"));
                objCustomerMaster.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                objCustomerMaster.setContactPersonName(jsonArray.getJSONObject(i).getString("ContactPersonName"));
                objCustomerMaster.setDesignation(jsonArray.getJSONObject(i).getString("Designation"));
                objCustomerMaster.setPhone1(jsonArray.getJSONObject(i).getString("Phone1"));
                objCustomerMaster.setIsPhone1DND(jsonArray.getJSONObject(i).getBoolean("IsPhone1DND"));
                objCustomerMaster.setPhone2(jsonArray.getJSONObject(i).getString("Phone2"));
                if (!jsonArray.getJSONObject(i).getString("IsPhone2DND").equals("null")) {
                    objCustomerMaster.setIsPhone2DND(jsonArray.getJSONObject(i).getBoolean("IsPhone2DND"));
                }
                objCustomerMaster.setEmail1(jsonArray.getJSONObject(i).getString("Email1"));
                objCustomerMaster.setEmail2(jsonArray.getJSONObject(i).getString("Email2"));
                objCustomerMaster.setFax(jsonArray.getJSONObject(i).getString("Fax"));
                objCustomerMaster.setImageName(jsonArray.getJSONObject(i).getString("ImageName"));
                if (!jsonArray.getJSONObject(i).getString("BirthDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("BirthDate"));
                    objCustomerMaster.setBirthDate(sdfControlDateFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("AnniversaryDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("AnniversaryDate"));
                    objCustomerMaster.setAnniversaryDate(sdfControlDateFormat.format(dt));
                }
                objCustomerMaster.setCustomerType((short)jsonArray.getJSONObject(i).getInt("CustomerType"));
                if (!jsonArray.getJSONObject(i).getString("IsFavourite").equals("null")) {
                    objCustomerMaster.setIsFavourite(jsonArray.getJSONObject(i).getBoolean("IsFavourite"));
                }
                if (!jsonArray.getJSONObject(i).getString("IsCredit").equals("null")) {
                    objCustomerMaster.setIsCredit(jsonArray.getJSONObject(i).getBoolean("IsCredit"));
                }
                objCustomerMaster.setOpeningBalance(jsonArray.getJSONObject(i).getDouble("OpeningBalance"));
                objCustomerMaster.setCreditDays((short) jsonArray.getJSONObject(i).getInt("CreditDays"));
                objCustomerMaster.setCreditBalance(jsonArray.getJSONObject(i).getDouble("CreditBalance"));
                objCustomerMaster.setCreditLimit(jsonArray.getJSONObject(i).getDouble("CreditLimit"));
                objCustomerMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objCustomerMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objCustomerMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objCustomerMaster.setlinktoUserMasterIdUpdatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objCustomerMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objCustomerMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                objCustomerMaster.setGender(jsonArray.getJSONObject(i).getString("Gender"));
                objCustomerMaster.setPassword(jsonArray.getJSONObject(i).getString("Password"));
                objCustomerMaster.setlinktoSourceMasterId((short) jsonArray.getJSONObject(i).getInt("linktoSourceMasterId"));

                /// Extra
                lstCustomerMaster.add(objCustomerMaster);
            }
            return lstCustomerMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    //endregion

    //region Insert

    public String InsertCustomerMaster(CustomerMaster objCustomerMaster) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("customerMaster");

            stringer.object();

            stringer.key("Email1").value(objCustomerMaster.getEmail1());
            stringer.key("Phone1").value(objCustomerMaster.getPhone1());
            stringer.key("Password").value(objCustomerMaster.getPassword());
            stringer.key("ShortName").value(objCustomerMaster.getCustomerName());
            stringer.key("CustomerName").value(objCustomerMaster.getCustomerName());
            stringer.key("Gender").value(objCustomerMaster.getGender());
            if(objCustomerMaster.getBirthDate()!=null) {
                stringer.key("BirthDate").value(objCustomerMaster.getBirthDate());
            }
            stringer.key("linktoCityMasterId").value(objCustomerMaster.getLinktoCityMasterId());
            stringer.key("linktoAreaMasterId").value(objCustomerMaster.getLinktoAreaMasterId());
            stringer.key("linktoBusinessMasterId").value(objCustomerMaster.getlinktoBusinessMasterId());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoSourceMasterId").value(objCustomerMaster.getlinktoSourceMasterId());
            stringer.key("CustomerType").value(objCustomerMaster.getCustomerType());
            stringer.key("LastLoginDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("IsEnabled").value(objCustomerMaster.getIsEnabled());

            stringer.endObject();
            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertCustomerMaster, stringer);
            if(jsonResponse!=null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertCustomerMaster + "Result");
                return String.valueOf(jsonObject.getInt("ErrorCode"));
            }else{
                return "-1";
            }
        } catch (Exception ex) {
            return "-1";
        }
    }

    //endregion

    //region Update
    public String UpdateCustomerMaster(CustomerMaster objCustomerMaster) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("customerMaster");
            stringer.object();

            stringer.key("CustomerMasterId").value(objCustomerMaster.getCustomerMasterId());
            stringer.key("Phone1").value(objCustomerMaster.getPhone1());
            stringer.key("CustomerName").value(objCustomerMaster.getCustomerName());
            stringer.key("Gender").value(objCustomerMaster.getGender());
            if(objCustomerMaster.getBirthDate()!=null) {
                stringer.key("BirthDate").value(objCustomerMaster.getBirthDate());
            }
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdUpdatedBy").value(objCustomerMaster.getlinktoUserMasterIdUpdatedBy());
            stringer.key("ShortName").value(objCustomerMaster.getCustomerName());
            stringer.key("CustomerType").value(objCustomerMaster.getCustomerType());
            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateCustomerMaster, stringer);
            if(jsonResponse!=null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateCustomerMaster + "Result");
                return String.valueOf(jsonObject.getInt("ErrorCode"));
            }else{
                return "-1";
            }

        } catch (Exception ex) {
            return "-1";
        }
    }

    public String UpdateRegisteredUserMasterPassword(RegisteredUserMaster objRegisteredUserMaster) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("registeredUserMaster");
            stringer.object();

            stringer.key("RegisteredUserMasterId").value(objRegisteredUserMaster.getRegisteredUserMasterId());
            stringer.key("Password").value(objRegisteredUserMaster.getPassword());
            stringer.key("OldPassword").value(objRegisteredUserMaster.getOldPassword());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdUpdatedBy").value(objRegisteredUserMaster.getlinktoUserMasterIdUpdatedBy());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateRegisteredUserMasterPassword, stringer);
            if(jsonResponse!=null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateRegisteredUserMasterPassword + "Result");
                return String.valueOf(jsonObject.getInt("ErrorCode"));
            }else{
                return "-1";
            }
        } catch (Exception ex) {
            return "-1";
        }
    }
    //endregion

    //region Select

//    public RegisteredUserMaster SelectRegisteredUserMaster(int registeredUserMasterId) {
//        try {
//            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectRegisteredUserMaster + "/" + registeredUserMasterId);
//            if (jsonResponse != null) {
//                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectRegisteredUserMaster + "Result");
//                if (jsonObject != null) {
//                    return SetClassPropertiesFromJSONObject(jsonObject);
//                }
//            }
//            return null;
//        } catch (Exception ex) {
//            return null;
//        }
//    }

//    public RegisteredUserMaster SelectRegisteredUserMasterUserName(String name, String password, int registeredUserMasterId) {
//        try {
//
//            JSONObject jsonResponse;
//            if (registeredUserMasterId == 0) {
//                jsonResponse = Service.HttpGetService(Service.Url + this.SelectRegisteredUserMasterUserName + "/" + URLEncoder.encode(name, "utf-8").replace(".", "2E") + "/" + URLEncoder.encode(password, "utf-8").replace(".", "2E") + "/" + null);
//            } else {
//                jsonResponse = Service.HttpGetService(Service.Url + this.SelectRegisteredUserMasterUserName + "/" + null + "/" + null + "/" + registeredUserMasterId);
//            }
//            if (jsonResponse != null) {
//                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectRegisteredUserMasterUserName + "Result");
//                if (jsonObject != null) {
//                    return SetClassPropertiesFromJSONObject(jsonObject);
//                }
//            }
//            return null;
//        } catch (Exception ex) {
//            return null;
//        }
//    }

    public CustomerMaster SelectCustomerMaster(String name, String password, int customerMasterId) {
        try {

            JSONObject jsonResponse;
            if (customerMasterId == 0) {
                jsonResponse = Service.HttpGetService(Service.Url + this.SelectCustomerMaster + "/" + URLEncoder.encode(name, "utf-8").replace(".", "2E") + "/" + URLEncoder.encode(password, "utf-8").replace(".", "2E") + "/" + null);
            } else {
                jsonResponse = Service.HttpGetService(Service.Url + this.SelectCustomerMaster + "/" + null + "/" + null + "/" + customerMasterId);
            }
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectCustomerMaster + "Result");
                if (jsonObject != null) {
                    return SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    //endregion

    //region SelectAll

//    public ArrayList<RegisteredUserMaster> SelectAllRegisteredUserMasterPageWise(int currentPage) {
//        ArrayList<RegisteredUserMaster> lstRegisteredUserMaster = null;
//        try {
//            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllRegisteredUserMaster);
//            if (jsonResponse != null) {
//                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllRegisteredUserMaster + "PageWiseResult");
//                if (jsonArray != null) {
//                    lstRegisteredUserMaster = SetListPropertiesFromJSONArray(jsonArray);
//                }
//            }
//            return lstRegisteredUserMaster;
//        } catch (Exception ex) {
//            return null;
//        }
//    }

    //endregion
}

