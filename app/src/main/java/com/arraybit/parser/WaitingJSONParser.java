package com.arraybit.parser;

import android.util.Log;

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

public class WaitingJSONParser {
    public String InsertWaitingMaster = "InsertWaitingMaster";
    public String UpdateWaitingStatus = "UpdateWaitingStatus";
    public String SelectOrderMasterByTableMasterId = "SelectOrderMasterByTableMasterId";
    public String SelectAllWaitingMasterByWaitingStatusId = "SelectAllWaitingMasterByWaitingStatusMasterId";

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
                objWaitingMaster.setNoOfPersons((short) jsonObject.getInt("NoOfPersons"));
                objWaitingMaster.setlinktoWaitingStatusMasterId((short) jsonObject.getInt("linktoWaitingStatusMasterId"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objWaitingMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objWaitingMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                objWaitingMaster.setLinktoTableMasterId((short) jsonObject.getInt("linktoTableMasterId"));

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
                objWaitingMaster.setNoOfPersons((short) jsonArray.getJSONObject(i).getInt("NoOfPersons"));
                objWaitingMaster.setlinktoWaitingStatusMasterId((short) jsonArray.getJSONObject(i).getInt("linktoWaitingStatusMasterId"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objWaitingMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objWaitingMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                objWaitingMaster.setLinktoTableMasterId((short) jsonArray.getJSONObject(i).getInt("linktoTableMasterId"));

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
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("waitingMaster");
            stringer.object();

            stringer.key("PersonName").value(objWaitingMaster.getPersonName());
            stringer.key("PersonMobile").value(objWaitingMaster.getPersonMobile());
            stringer.key("NoOfPersons").value(objWaitingMaster.getNoOfPersons());
            stringer.key("linktoWaitingStatusMasterId").value(objWaitingMaster.getlinktoWaitingStatusMasterId());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdCreatedBy").value(objWaitingMaster.getlinktoUserMasterIdCreatedBy());
            stringer.key("linktoBusinessMasterId").value(objWaitingMaster.getlinktoBusinessMasterId());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertWaitingMaster, stringer);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertWaitingMaster + "Result");
                if (jsonObject != null) {
                    return String.valueOf(jsonObject.getInt("ErrorCode"));
                }
            }
            return "-1";
        } catch (Exception ex) {
            return "-1";
        }
    }

    //endregion

    //region Update

    public String UpdateWaitingStatus(WaitingMaster objWaitingMaster) {
        dt= new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("waitingMaster");
            stringer.object();

            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("WaitingMasterId").value(objWaitingMaster.getWaitingMasterId());
            stringer.key("linktoWaitingStatusMasterId").value(objWaitingMaster.getlinktoWaitingStatusMasterId());

//            if(objWaitingMaster.getLinktoUserMasterIdUpdatedBy()!=0) {
                stringer.key("linktoUserMasterIdUpdatedBy").value(objWaitingMaster.getLinktoUserMasterIdUpdatedBy());
//            }
            if(objWaitingMaster.getLinktoTableMasterId()!=0){
                stringer.key("linktoTableMasterId").value(objWaitingMaster.getLinktoTableMasterId());
            }

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateWaitingStatus, stringer);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateWaitingStatus + "Result");
                return String.valueOf(jsonObject.getInt("ErrorCode"));
            }
            return "-1";
        } catch (Exception ex) {
            return "-1";
        }
    }

    //endregion

    public boolean CheckOrderPlaceForTableMasterId(int businessMasterId,String linktoTableMasterId){
        dt = new Date();
        boolean isOrderPlace = false;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectOrderMasterByTableMasterId + "/" + linktoTableMasterId + "/" + businessMasterId + "/" + sdfControlDateFormat.format(dt));
            if (jsonResponse != null) {
                isOrderPlace = jsonResponse.optBoolean(this.SelectOrderMasterByTableMasterId + "Result");
            }
            return isOrderPlace;
        } catch (Exception ex) {
            return isOrderPlace;
        }
    }

    //region SelectAll

    public ArrayList<WaitingMaster> SelectAllWaitingMasterByWaitingStatusMasterId(int linktoWaitingStatusMasterId, int linktoBusinessMasterId, String OrderBy) {
        ArrayList<WaitingMaster> lstWaitingMaster = null;
        Date date;
        try {
            date = new Date();
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllWaitingMasterByWaitingStatusId + "/" + linktoWaitingStatusMasterId + "/" + sdfControlDateFormat.format(date) + "/" + linktoBusinessMasterId +"/"+OrderBy);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllWaitingMasterByWaitingStatusId + "Result");
                if (jsonArray != null) {
                    lstWaitingMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstWaitingMaster;
        } catch (Exception ex) {
            return null;
        }
    }

    //endregion
}
