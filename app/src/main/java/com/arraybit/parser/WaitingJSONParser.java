package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.WaitingMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WaitingJSONParser
{
    public String InsertWaitingMaster = "InsertWaitingMaster";
    public String UpdateWaitingMaster = "UpdateWaitingMaster";
    public String SelectWaitingMaster = "SelectWaitingMaster";
    public String SelectAllWaitingMaster = "SelectAllWaitingMaster";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    //region Class Methods

    private WaitingMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        WaitingMaster objWaitingMaster = null;
        try {
            if (jsonObject != null) {
                objWaitingMaster = new WaitingMaster();
                objWaitingMaster.setWaitingMasterId(jsonObject.getLong("WaitingMasterId"));
                objWaitingMaster.setPersonName(jsonObject.getString("PersonName"));
                objWaitingMaster.setPersonMobile(jsonObject.getString("PersonMobile"));
                objWaitingMaster.setNoOfPersons((short)jsonObject.getInt("NoOfPersons"));
                objWaitingMaster.setlinktoWaitingStatusMasterId((short)jsonObject.getInt("linktoWaitingStatusMasterId"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objWaitingMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objWaitingMaster.setlinktoUserMasterIdCreatedBy((short)jsonObject.getInt("linktoUserMasterIdCreatedBy"));

                /// Extra
                objWaitingMaster.setWaitingStatus(jsonObject.getString("WaitingStatus"));
            }
            return objWaitingMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<WaitingMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<WaitingMaster> lstWaitingMaster = new ArrayList<>();
        WaitingMaster objWaitingMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objWaitingMaster = new WaitingMaster();
                objWaitingMaster.setWaitingMasterId(jsonArray.getJSONObject(i).getLong("WaitingMasterId"));
                objWaitingMaster.setPersonName(jsonArray.getJSONObject(i).getString("PersonName"));
                objWaitingMaster.setPersonMobile(jsonArray.getJSONObject(i).getString("PersonMobile"));
                objWaitingMaster.setNoOfPersons((short)jsonArray.getJSONObject(i).getInt("NoOfPersons"));
                objWaitingMaster.setlinktoWaitingStatusMasterId((short)jsonArray.getJSONObject(i).getInt("linktoWaitingStatusMasterId"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objWaitingMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objWaitingMaster.setlinktoUserMasterIdCreatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));

                /// Extra
                objWaitingMaster.setWaitingStatus(jsonArray.getJSONObject(i).getString("WaitingStatus"));
                lstWaitingMaster.add(objWaitingMaster);
            }
            return lstWaitingMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    //endregion

    //region Insert

    public String InsertWaitingMaster(WaitingMaster objWaitingMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("waitingMaster");
            stringer.object();

            stringer.key("PersonName").value(objWaitingMaster.getPersonName());
            stringer.key("PersonMobile").value(objWaitingMaster.getPersonMobile());
            stringer.key("NoOfPersons").value(objWaitingMaster.getNoOfPersons());
            stringer.key("linktoWaitingStatusMasterId").value(objWaitingMaster.getlinktoWaitingStatusMasterId());
            dt = sdfControlDateFormat.parse(objWaitingMaster.getCreateDateTime());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdCreatedBy").value(objWaitingMaster.getlinktoUserMasterIdCreatedBy());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertWaitingMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertWaitingMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        }
        catch (Exception ex) {
            return "-1";
        }
    }

    //endregion

    //region Update

    public String UpdateWaitingMaster(WaitingMaster objWaitingMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("waitingMaster");
            stringer.object();

            stringer.key("WaitingMasterId").value(objWaitingMaster.getWaitingMasterId());
            stringer.key("PersonName").value(objWaitingMaster.getPersonName());
            stringer.key("PersonMobile").value(objWaitingMaster.getPersonMobile());
            stringer.key("NoOfPersons").value(objWaitingMaster.getNoOfPersons());
            stringer.key("linktoWaitingStatusMasterId").value(objWaitingMaster.getlinktoWaitingStatusMasterId());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateWaitingMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateWaitingMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        }
        catch (Exception ex) {
            return "-1";
        }
    }

    //endregion

    //region Select
    public WaitingMaster SelectWaitingMaster(long waitingMasterId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectWaitingMaster + "/" + waitingMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectWaitingMaster + "Result");
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
    //endregion

    //region SelectAll

    public ArrayList<WaitingMaster> SelectAllWaitingMasterPageWise(int currentPage) {
        ArrayList<WaitingMaster> lstWaitingMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllWaitingMaster);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllWaitingMaster + "PageWiseResult");
                if (jsonArray != null) {
                    lstWaitingMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstWaitingMaster;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<WaitingMaster> SelectAllWaitingMasterByWaitingStatusMasterId(int currentPage) {
        ArrayList<WaitingMaster> lstWaitingMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllWaitingMaster +"/"+currentPage);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllWaitingMaster + "Result");
                if (jsonArray != null) {
                    lstWaitingMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstWaitingMaster;
        }
        catch (Exception ex) {
            return null;
        }
    }

    //endregion

}
