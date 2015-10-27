package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.FeedbackMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/// <summary>
/// JSONParser for FeedbackMaster
/// </summary>
public class FeedbackJSONParser
{
    public String InsertFeedbackMaster = "InsertFeedbackMaster";
    public String UpdateFeedbackMaster = "UpdateFeedbackMaster";
    public String SelectFeedbackMaster = "SelectFeedbackMaster";
    public String SelectAllFeedbackMaster = "SelectAllFeedbackMasterPageWise";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    private FeedbackMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        FeedbackMaster objFeedbackMaster = null;
        try {
            if (jsonObject != null) {
                objFeedbackMaster = new FeedbackMaster();
                objFeedbackMaster.setFeedbackMasterId(jsonObject.getInt("FeedbackMasterId"));
                objFeedbackMaster.setName(jsonObject.getString("Name"));
                objFeedbackMaster.setEmail(jsonObject.getString("Email"));
                objFeedbackMaster.setPhone(jsonObject.getString("Phone"));
                objFeedbackMaster.setFeedback(jsonObject.getString("Feedback"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("FeedbackDateTime"));
                objFeedbackMaster.setFeedbackDateTime(sdfControlDateFormat.format(dt));
                objFeedbackMaster.setlinktoFeedbackTypeMasterId((short)jsonObject.getInt("linktoFeedbackTypeMasterId"));
                if (!jsonObject.getString("linktoRegisteredUserMasterId").equals("null")) {
                    objFeedbackMaster.setlinktoRegisteredUserMasterId(jsonObject.getInt("linktoRegisteredUserMasterId"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("ReplyDateTime"));
                objFeedbackMaster.setReplyDateTime(sdfControlDateFormat.format(dt));
                objFeedbackMaster.setReply(jsonObject.getString("Reply"));
                if (!jsonObject.getString("linktoUserMasterIdRepliedBy").equals("null")) {
                    objFeedbackMaster.setlinktoUserMasterIdRepliedBy((short)jsonObject.getInt("linktoUserMasterIdRepliedBy"));
                }
                objFeedbackMaster.setlinktoBusinessTypeMasterId((short)jsonObject.getInt("linktoBusinessTypeMasterId"));
                objFeedbackMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));

                /// Extra
                objFeedbackMaster.setFeedbackType(jsonObject.getString("FeedbackType"));
                objFeedbackMaster.setBusinessType(jsonObject.getString("BusinessType"));
            }
            return objFeedbackMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<FeedbackMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<FeedbackMaster> lstFeedbackMaster = new ArrayList<>();
        FeedbackMaster objFeedbackMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objFeedbackMaster = new FeedbackMaster();
                objFeedbackMaster.setFeedbackMasterId(jsonArray.getJSONObject(i).getInt("FeedbackMasterId"));
                objFeedbackMaster.setName(jsonArray.getJSONObject(i).getString("Name"));
                objFeedbackMaster.setEmail(jsonArray.getJSONObject(i).getString("Email"));
                objFeedbackMaster.setPhone(jsonArray.getJSONObject(i).getString("Phone"));
                objFeedbackMaster.setFeedback(jsonArray.getJSONObject(i).getString("Feedback"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("FeedbackDateTime"));
                objFeedbackMaster.setFeedbackDateTime(sdfControlDateFormat.format(dt));
                objFeedbackMaster.setlinktoFeedbackTypeMasterId((short)jsonArray.getJSONObject(i).getInt("linktoFeedbackTypeMasterId"));
                if (!jsonArray.getJSONObject(i).getString("linktoRegisteredUserMasterId").equals("null")) {
                    objFeedbackMaster.setlinktoRegisteredUserMasterId(jsonArray.getJSONObject(i).getInt("linktoRegisteredUserMasterId"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("ReplyDateTime"));
                objFeedbackMaster.setReplyDateTime(sdfControlDateFormat.format(dt));
                objFeedbackMaster.setReply(jsonArray.getJSONObject(i).getString("Reply"));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdRepliedBy").equals("null")) {
                    objFeedbackMaster.setlinktoUserMasterIdRepliedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdRepliedBy"));
                }
                objFeedbackMaster.setlinktoBusinessTypeMasterId((short)jsonArray.getJSONObject(i).getInt("linktoBusinessTypeMasterId"));
                objFeedbackMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));

                /// Extra
                objFeedbackMaster.setFeedbackType(jsonArray.getJSONObject(i).getString("FeedbackType"));
                objFeedbackMaster.setBusinessType(jsonArray.getJSONObject(i).getString("BusinessType"));
                lstFeedbackMaster.add(objFeedbackMaster);
            }
            return lstFeedbackMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public String InsertFeedbackMaster(FeedbackMaster objFeedbackMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("feedbackMaster");
            stringer.object();

            stringer.key("Name").value(objFeedbackMaster.getName());
            stringer.key("Email").value(objFeedbackMaster.getEmail());
            stringer.key("Phone").value(objFeedbackMaster.getPhone());
            stringer.key("Feedback").value(objFeedbackMaster.getFeedback());
            dt = sdfControlDateFormat.parse(objFeedbackMaster.getFeedbackDateTime());
            stringer.key("FeedbackDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoFeedbackTypeMasterId").value(objFeedbackMaster.getlinktoFeedbackTypeMasterId());
            stringer.key("linktoRegisteredUserMasterId").value(objFeedbackMaster.getlinktoRegisteredUserMasterId());
            dt = sdfControlDateFormat.parse(objFeedbackMaster.getReplyDateTime());
            stringer.key("ReplyDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("Reply").value(objFeedbackMaster.getReply());
            stringer.key("linktoUserMasterIdRepliedBy").value(objFeedbackMaster.getlinktoUserMasterIdRepliedBy());
            stringer.key("linktoBusinessTypeMasterId").value(objFeedbackMaster.getlinktoBusinessTypeMasterId());
            stringer.key("IsDeleted").value(objFeedbackMaster.getIsDeleted());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertFeedbackMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertFeedbackMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        }
        catch (Exception ex) {
            return "-1";
        }
    }

    public String UpdateFeedbackMaster(FeedbackMaster objFeedbackMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("feedbackMaster");
            stringer.object();

            stringer.key("FeedbackMasterId").value(objFeedbackMaster.getFeedbackMasterId());
            stringer.key("Name").value(objFeedbackMaster.getName());
            stringer.key("Email").value(objFeedbackMaster.getEmail());
            stringer.key("Phone").value(objFeedbackMaster.getPhone());
            stringer.key("Feedback").value(objFeedbackMaster.getFeedback());
            dt = sdfControlDateFormat.parse(objFeedbackMaster.getFeedbackDateTime());
            stringer.key("FeedbackDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoFeedbackTypeMasterId").value(objFeedbackMaster.getlinktoFeedbackTypeMasterId());
            stringer.key("linktoRegisteredUserMasterId").value(objFeedbackMaster.getlinktoRegisteredUserMasterId());
            dt = sdfControlDateFormat.parse(objFeedbackMaster.getReplyDateTime());
            stringer.key("ReplyDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("Reply").value(objFeedbackMaster.getReply());
            stringer.key("linktoUserMasterIdRepliedBy").value(objFeedbackMaster.getlinktoUserMasterIdRepliedBy());
            stringer.key("linktoBusinessTypeMasterId").value(objFeedbackMaster.getlinktoBusinessTypeMasterId());
            stringer.key("IsDeleted").value(objFeedbackMaster.getIsDeleted());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateFeedbackMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateFeedbackMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        }
        catch (Exception ex) {
            return "-1";
        }
    }

    public FeedbackMaster SelectFeedbackMaster(int feedbackMasterId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectFeedbackMaster + "/" + feedbackMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectFeedbackMaster + "Result");
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

    public ArrayList<FeedbackMaster> SelectAllFeedbackMasterPageWise(int currentPage) {
        ArrayList<FeedbackMaster> lstFeedbackMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllFeedbackMaster);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllFeedbackMaster + "PageWiseResult");
                if (jsonArray != null) {
                    lstFeedbackMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstFeedbackMaster;
        }
        catch (Exception ex) {
            return null;
        }
    }

}
