package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.FeedbackAnswerMaster;
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
public class FeedbackJSONParser {
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
                objFeedbackMaster.setlinktoFeedbackTypeMasterId((short) jsonObject.getInt("linktoFeedbackTypeMasterId"));
                if (!jsonObject.getString("linktoCustomerMasterId").equals("null")) {
                    objFeedbackMaster.setlinktoCustomerMasterId(jsonObject.getInt("linktoCustomerMasterId"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("ReplyDateTime"));
                objFeedbackMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));

                /// Extra
                objFeedbackMaster.setFeedbackType(jsonObject.getString("FeedbackType"));
                objFeedbackMaster.setBusiness(jsonObject.getString("Business"));
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
                objFeedbackMaster.setlinktoFeedbackTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoFeedbackTypeMasterId"));
                if (!jsonArray.getJSONObject(i).getString("linktoCustomerMasterId").equals("null")) {
                    objFeedbackMaster.setlinktoCustomerMasterId(jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("ReplyDateTime"));
                objFeedbackMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));

                /// Extra
                objFeedbackMaster.setFeedbackType(jsonArray.getJSONObject(i).getString("FeedbackType"));
                objFeedbackMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstFeedbackMaster.add(objFeedbackMaster);
            }
            return lstFeedbackMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public String InsertFeedbackMaster(FeedbackMaster objFeedbackMaster, ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("feedbackMaster");
            stringer.object();

            stringer.key("Name").value(objFeedbackMaster.getName());
            stringer.key("Email").value(objFeedbackMaster.getEmail());
            stringer.key("Phone").value(objFeedbackMaster.getPhone());
            stringer.key("Feedback").value(objFeedbackMaster.getFeedback());
            stringer.key("FeedbackDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoFeedbackTypeMasterId").value(objFeedbackMaster.getlinktoFeedbackTypeMasterId());
            stringer.key("linktoCustomerMasterId").value(objFeedbackMaster.getlinktoCustomerMasterId());
            stringer.key("ReplyDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoBusinessMasterId").value(objFeedbackMaster.getlinktoBusinesseMasterId());

            stringer.endObject();
            stringer.key("lstFeedbackTran");
            stringer.array();

            for (int i = 0; i < alFeedbackAnswerMaster.size(); i++) {
                stringer.object();
                stringer.key("linktoFeedbackQuestionMasterId").value(alFeedbackAnswerMaster.get(i).getlinktoFeedbackQuestionMasterId());
                if (alFeedbackAnswerMaster.get(i).getFeedbackAnswerMasterId() != 0) {
                    stringer.key("linktoFeedbackAnswerMasterId").value(alFeedbackAnswerMaster.get(i).getFeedbackAnswerMasterId());
                } else {
                    stringer.key("linktoFeedbackAnswerMasterId").value(null);
                }
                stringer.key("Answer").value(alFeedbackAnswerMaster.get(i).getAnswer());
                stringer.endObject();
            }

            stringer.endArray();
            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertFeedbackMaster, stringer);
            if(jsonResponse!=null){
                JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertFeedbackMaster + "Result");
                return String.valueOf(jsonObject.getInt("ErrorCode"));
            }
            return "-1";
        } catch (Exception ex) {
            return "-1";
        }
    }
}
