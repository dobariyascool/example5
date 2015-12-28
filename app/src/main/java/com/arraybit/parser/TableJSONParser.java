package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.TableMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TableJSONParser {
    public String InsertTableMaster = "InsertTableMaster";
    public String UpdateTableMaster = "UpdateTableStatus";
    public String SelectTableMaster = "SelectTableMaster";
    public String SelectAllTableMasterBySectionId = "SelectAllTableMasterBySectionMasterId";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    //region Class Methods

    private TableMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        TableMaster objTableMaster = null;
        try {
            if (jsonObject != null) {
                objTableMaster = new TableMaster();
                objTableMaster.setTableMasterId((short) jsonObject.getInt("TableMasterId"));
                objTableMaster.setTableName(jsonObject.getString("TableName"));
                objTableMaster.setShortName(jsonObject.getString("ShortName"));
                objTableMaster.setDescription(jsonObject.getString("Description"));
                objTableMaster.setMinPerson((short) jsonObject.getInt("MinPerson"));
                objTableMaster.setMaxPerson((short) jsonObject.getInt("MaxPerson"));
                objTableMaster.setlinktoTableStatusMasterId((short) jsonObject.getInt("linktoTableStatusMasterId"));
                objTableMaster.setlinktoOrderTypeMasterId((short) jsonObject.getInt("linktoOrderTypeMasterId"));
                objTableMaster.setlinktoSectionMasterId((short) jsonObject.getInt("linktoSectionMasterId"));
                objTableMaster.setOriginX(jsonObject.getInt("OriginX"));
                objTableMaster.setOriginY(jsonObject.getInt("OriginY"));
                objTableMaster.setHeight(jsonObject.getDouble("Height"));
                objTableMaster.setWidth(jsonObject.getDouble("Width"));
                if (jsonObject.getString("TableColor").equals("")) {
                    objTableMaster.setTableColor(null);
                } else {
                    objTableMaster.setTableColor(jsonObject.getString("TableColor"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objTableMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objTableMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
//                dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
//                objTableMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objTableMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objTableMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objTableMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));

                /// Extra
                objTableMaster.setTableStatus(jsonObject.getString("TableStatus"));
                objTableMaster.setStatusColor(jsonObject.getString("StatusColor"));
                objTableMaster.setSection(jsonObject.getString("Section"));
                objTableMaster.setBusiness(jsonObject.getString("Business"));
            }
            return objTableMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<TableMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<TableMaster> lstTableMaster = new ArrayList<>();
        TableMaster objTableMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objTableMaster = new TableMaster();
                objTableMaster.setTableMasterId((short) jsonArray.getJSONObject(i).getInt("TableMasterId"));
                objTableMaster.setTableName(jsonArray.getJSONObject(i).getString("TableName"));
                objTableMaster.setShortName(jsonArray.getJSONObject(i).getString("ShortName"));
                objTableMaster.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                objTableMaster.setMinPerson((short) jsonArray.getJSONObject(i).getInt("MinPerson"));
                objTableMaster.setMaxPerson((short) jsonArray.getJSONObject(i).getInt("MaxPerson"));
                objTableMaster.setlinktoTableStatusMasterId((short) jsonArray.getJSONObject(i).getInt("linktoTableStatusMasterId"));
                objTableMaster.setlinktoOrderTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoOrderTypeMasterId"));
                objTableMaster.setlinktoSectionMasterId((short) jsonArray.getJSONObject(i).getInt("linktoSectionMasterId"));
                objTableMaster.setOriginX(jsonArray.getJSONObject(i).getInt("OriginX"));
                objTableMaster.setOriginY(jsonArray.getJSONObject(i).getInt("OriginY"));
                objTableMaster.setHeight(jsonArray.getJSONObject(i).getDouble("Height"));
                objTableMaster.setWidth(jsonArray.getJSONObject(i).getDouble("Width"));
                if (jsonArray.getJSONObject(i).getString("TableColor").equals("")) {
                    objTableMaster.setTableColor(null);
                } else {
                    objTableMaster.setTableColor(jsonArray.getJSONObject(i).getString("TableColor"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objTableMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objTableMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
//                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
//                objTableMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objTableMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objTableMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objTableMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));

                /// Extra
                objTableMaster.setTableStatus(jsonArray.getJSONObject(i).getString("TableStatus"));
                objTableMaster.setStatusColor(jsonArray.getJSONObject(i).getString("StatusColor"));
                objTableMaster.setSection(jsonArray.getJSONObject(i).getString("Section"));
                objTableMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstTableMaster.add(objTableMaster);
            }
            return lstTableMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    //endregion

    //region Insert

    public String InsertTableMaster(TableMaster objTableMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("tableMaster");
            stringer.object();

            stringer.key("TableName").value(objTableMaster.getTableName());
            stringer.key("ShortName").value(objTableMaster.getShortName());
            stringer.key("Description").value(objTableMaster.getDescription());
            stringer.key("MinPerson").value(objTableMaster.getMinPerson());
            stringer.key("MaxPerson").value(objTableMaster.getMaxPerson());
            stringer.key("linktoTableStatusMasterId").value(objTableMaster.getlinktoTableStatusMasterId());
            stringer.key("linktoOrderTypeMasterId").value(objTableMaster.getlinktoOrderTypeMasterId());
            stringer.key("linktoSectionMasterId").value(objTableMaster.getlinktoSectionMasterId());
            stringer.key("OriginX").value(objTableMaster.getOriginX());
            stringer.key("OriginY").value(objTableMaster.getOriginY());
            stringer.key("Height").value(objTableMaster.getHeight());
            stringer.key("Width").value(objTableMaster.getWidth());
            stringer.key("TableColor").value(objTableMaster.getTableColor());
            dt = sdfControlDateFormat.parse(objTableMaster.getCreateDateTime());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdCreatedBy").value(objTableMaster.getlinktoUserMasterIdCreatedBy());
            stringer.key("linktoBusinessMasterId").value(objTableMaster.getlinktoBusinessMasterId());
            stringer.key("IsEnabled").value(objTableMaster.getIsEnabled());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertTableMaster, stringer);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertTableMaster + "Result");
                return String.valueOf(jsonObject.getInt("ErrorCode"));
            }
            return "-1";
        } catch (Exception ex) {
            return "-1";
        }
    }

    //endregion

    //region Update

    public String UpdateTableStatus(TableMaster objTableMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("tableMaster");
            stringer.object();

            stringer.key("TableMasterId").value(objTableMaster.getTableMasterId());
            stringer.key("linktoTableStatusMasterId").value(objTableMaster.getlinktoTableStatusMasterId());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateTableMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateTableMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));

        } catch (Exception ex) {
            return "-1";
        }
    }
    //endregion

    //region Select

    public TableMaster SelectTableMaster(short tableMasterId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectTableMaster + "/" + tableMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectTableMaster + "Result");
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

    //region  SelectAll

    public ArrayList<TableMaster> SelectAllTableMasterBySectionMasterId(int linktoCounterMasterId, int linktoSectionMasterId, String linktoTableStatusMasterId, String linktoOrderTypeMasterId) {
        ArrayList<TableMaster> lstTableMaster = null;
        JSONObject jsonResponse;
        try {
            if (linktoSectionMasterId == 0) {
                jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllTableMasterBySectionId + "/" + linktoCounterMasterId + "/" + null + "/" + linktoTableStatusMasterId+"/"+linktoOrderTypeMasterId);
            } else {
                jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllTableMasterBySectionId + "/" + linktoCounterMasterId + "/" + linktoSectionMasterId + "/" + linktoTableStatusMasterId+"/"+linktoOrderTypeMasterId);
            }
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllTableMasterBySectionId + "Result");
                if (jsonArray != null) {
                    lstTableMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstTableMaster;
        } catch (Exception ex) {
            return null;
        }
    }

    //endregion
}

