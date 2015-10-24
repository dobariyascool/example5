package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
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

public class RegisteredUserJSONParser {

    public String InsertRegisteredUserMaster = "InsertRegisteredUserMaster";
    public String UpdateRegisteredUserMaster = "UpdateRegisteredUserMaster";
    public String SelectRegisteredUserMaster = "SelectRegisteredUserMaster";
    public String SelectAllRegisteredUserMaster = "SelectAllRegisteredUserMasterPageWise";
    public String SelectRegisteredUserMasterUserName = "GetRegisteredUserMaster";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    //region Class Methods

    private RegisteredUserMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        RegisteredUserMaster objRegisteredUserMaster = null;
        try {
            if (jsonObject != null) {
                objRegisteredUserMaster = new RegisteredUserMaster();
                objRegisteredUserMaster.setRegisteredUserMasterId(jsonObject.getInt("RegisteredUserMasterId"));
                objRegisteredUserMaster.setEmail(jsonObject.getString("Email"));
                objRegisteredUserMaster.setPhone(jsonObject.getString("Phone"));
                objRegisteredUserMaster.setPassword(jsonObject.getString("Password"));
                objRegisteredUserMaster.setFirstName(jsonObject.getString("FirstName"));
                objRegisteredUserMaster.setLastName(jsonObject.getString("LastName"));
                objRegisteredUserMaster.setGender(jsonObject.getString("Gender"));
                //dt = sdfDateFormat.parse(jsonObject.getString("BirthDate"));
                //objRegisteredUserMaster.setBirthDate(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("linktoAreaMasterId").equals("null")) {
                    objRegisteredUserMaster.setlinktoAreaMasterId((short) jsonObject.getInt("linktoAreaMasterId"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objRegisteredUserMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("linktoUserMasterIdCreatedBy").equals("null")) {
                    objRegisteredUserMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                }
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objRegisteredUserMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objRegisteredUserMaster.setlinktoSourceMasterId((short) jsonObject.getInt("linktoSourceMasterId"));
                objRegisteredUserMaster.setComment(jsonObject.getString("Comment"));
                objRegisteredUserMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));

                /// Extra
                objRegisteredUserMaster.setArea(jsonObject.getString("Area"));
            }
            return objRegisteredUserMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<RegisteredUserMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<RegisteredUserMaster> lstRegisteredUserMaster = new ArrayList<>();
        RegisteredUserMaster objRegisteredUserMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objRegisteredUserMaster = new RegisteredUserMaster();
                objRegisteredUserMaster.setRegisteredUserMasterId(jsonArray.getJSONObject(i).getInt("RegisteredUserMasterId"));
                objRegisteredUserMaster.setEmail(jsonArray.getJSONObject(i).getString("Email"));
                objRegisteredUserMaster.setPhone(jsonArray.getJSONObject(i).getString("Phone"));
                objRegisteredUserMaster.setPassword(jsonArray.getJSONObject(i).getString("Password"));
                objRegisteredUserMaster.setFirstName(jsonArray.getJSONObject(i).getString("FirstName"));
                objRegisteredUserMaster.setLastName(jsonArray.getJSONObject(i).getString("LastName"));
                objRegisteredUserMaster.setGender(jsonArray.getJSONObject(i).getString("Gender"));
                //dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("BirthDate"));
                //objRegisteredUserMaster.setBirthDate(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("linktoAreaMasterId").equals("null")) {
                    objRegisteredUserMaster.setlinktoAreaMasterId((short) jsonArray.getJSONObject(i).getInt("linktoAreaMasterId"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objRegisteredUserMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdCreatedBy").equals("null")) {
                    objRegisteredUserMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objRegisteredUserMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objRegisteredUserMaster.setlinktoSourceMasterId((short) jsonArray.getJSONObject(i).getInt("linktoSourceMasterId"));
                objRegisteredUserMaster.setComment(jsonArray.getJSONObject(i).getString("Comment"));
                objRegisteredUserMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));

                /// Extra
                objRegisteredUserMaster.setArea(jsonArray.getJSONObject(i).getString("Area"));
                lstRegisteredUserMaster.add(objRegisteredUserMaster);
            }
            return lstRegisteredUserMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    //endregion

    //region Insert

    public String InsertRegisteredUserMaster(RegisteredUserMaster objRegisteredUserMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("registeredUserMaster");
            stringer.object();

            stringer.key("Email").value(objRegisteredUserMaster.getEmail());
            stringer.key("Phone").value(objRegisteredUserMaster.getPhone());
            stringer.key("Password").value(objRegisteredUserMaster.getPassword());
            stringer.key("FirstName").value(objRegisteredUserMaster.getFirstName());
            stringer.key("LastName").value(objRegisteredUserMaster.getLastName());
            stringer.key("Gender").value(objRegisteredUserMaster.getGender());
            dt = sdfControlDateFormat.parse(objRegisteredUserMaster.getBirthDate());
            stringer.key("BirthDate").value(sdfDateFormat.format(dt));
            stringer.key("linktoAreaMasterId").value(objRegisteredUserMaster.getlinktoAreaMasterId());
            dt = sdfControlDateFormat.parse(objRegisteredUserMaster.getCreateDateTime());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdCreatedBy").value(objRegisteredUserMaster.getlinktoUserMasterIdCreatedBy());
            dt = sdfControlDateFormat.parse(objRegisteredUserMaster.getLastLoginDateTime());
            stringer.key("LastLoginDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoSourceMasterId").value(objRegisteredUserMaster.getlinktoSourceMasterId());
            stringer.key("Comment").value(objRegisteredUserMaster.getComment());
            stringer.key("IsEnabled").value(objRegisteredUserMaster.getIsEnabled());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertRegisteredUserMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertRegisteredUserMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        } catch (Exception ex) {
            return "-1";
        }
    }

    //endregion

    //region Update

    public String UpdateRegisteredUserMaster(RegisteredUserMaster objRegisteredUserMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("registeredUserMaster");
            stringer.object();

            stringer.key("RegisteredUserMasterId").value(objRegisteredUserMaster.getRegisteredUserMasterId());
            stringer.key("Email").value(objRegisteredUserMaster.getEmail());
            stringer.key("Phone").value(objRegisteredUserMaster.getPhone());
            stringer.key("Password").value(objRegisteredUserMaster.getPassword());
            stringer.key("FirstName").value(objRegisteredUserMaster.getFirstName());
            stringer.key("LastName").value(objRegisteredUserMaster.getLastName());
            stringer.key("Gender").value(objRegisteredUserMaster.getGender());
            dt = sdfControlDateFormat.parse(objRegisteredUserMaster.getBirthDate());
            stringer.key("BirthDate").value(sdfDateFormat.format(dt));
            stringer.key("linktoAreaMasterId").value(objRegisteredUserMaster.getlinktoAreaMasterId());
            dt = sdfControlDateFormat.parse(objRegisteredUserMaster.getUpdateDateTime());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdUpdatedBy").value(objRegisteredUserMaster.getlinktoUserMasterIdUpdatedBy());
            dt = sdfControlDateFormat.parse(objRegisteredUserMaster.getLastLoginDateTime());
            stringer.key("LastLoginDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoSourceMasterId").value(objRegisteredUserMaster.getlinktoSourceMasterId());
            stringer.key("Comment").value(objRegisteredUserMaster.getComment());
            stringer.key("IsEnabled").value(objRegisteredUserMaster.getIsEnabled());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateRegisteredUserMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateRegisteredUserMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        } catch (Exception ex) {
            return "-1";
        }
    }

    //endregion

    //region Select

    public RegisteredUserMaster SelectRegisteredUserMaster(int registeredUserMasterId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectRegisteredUserMaster + "/" + registeredUserMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectRegisteredUserMaster + "Result");
                if (jsonObject != null) {
                    return SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    public RegisteredUserMaster SelectRegisteredUserMasterUserName(String name, String password) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectRegisteredUserMasterUserName + "/" + URLEncoder.encode(name, "utf-8").replace(".", "2E") + "/" + URLEncoder.encode(password, "utf-8").replace(".", "2E"));
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectRegisteredUserMasterUserName + "Result");
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

    public ArrayList<RegisteredUserMaster> SelectAllRegisteredUserMasterPageWise(int currentPage) {
        ArrayList<RegisteredUserMaster> lstRegisteredUserMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllRegisteredUserMaster);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllRegisteredUserMaster + "PageWiseResult");
                if (jsonArray != null) {
                    lstRegisteredUserMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstRegisteredUserMaster;
        } catch (Exception ex) {
            return null;
        }
    }

    //endregion
}

