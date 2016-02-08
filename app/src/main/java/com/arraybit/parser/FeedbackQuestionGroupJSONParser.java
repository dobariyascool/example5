package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.modal.FeedbackQuestionGroupMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedbackQuestionGroupJSONParser {

    public String SelectAllFeedbackQuestionGroupMaster = "SelectAllFeedbackQuestionGroupMaster";

    private FeedbackQuestionGroupMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        FeedbackQuestionGroupMaster objFeedbackQuestionGroupMaster = null;
        try {
            if (jsonObject != null) {
                objFeedbackQuestionGroupMaster = new FeedbackQuestionGroupMaster();
                objFeedbackQuestionGroupMaster.setFeedbackQuestionGroupMasterId((short) jsonObject.getInt("FeedbackQuestionGroupMasterId"));
                objFeedbackQuestionGroupMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objFeedbackQuestionGroupMaster.setGroupName(jsonObject.getString("GroupName"));
                objFeedbackQuestionGroupMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));

                /// Extra
                objFeedbackQuestionGroupMaster.setBusiness(jsonObject.getString("Business"));
                objFeedbackQuestionGroupMaster.setTotalNullGroupFeedbackQuestion((short) jsonObject.getInt("TotalNullGroupFeedbackQuestion"));
            }
            return objFeedbackQuestionGroupMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<FeedbackQuestionGroupMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<FeedbackQuestionGroupMaster> lstFeedbackQuestionGroupMaster = new ArrayList<>();
        FeedbackQuestionGroupMaster objFeedbackQuestionGroupMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objFeedbackQuestionGroupMaster = new FeedbackQuestionGroupMaster();
                objFeedbackQuestionGroupMaster.setFeedbackQuestionGroupMasterId((short) jsonArray.getJSONObject(i).getInt("FeedbackQuestionGroupMasterId"));
                objFeedbackQuestionGroupMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objFeedbackQuestionGroupMaster.setGroupName(jsonArray.getJSONObject(i).getString("GroupName"));
                objFeedbackQuestionGroupMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));

                /// Extra
                objFeedbackQuestionGroupMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                objFeedbackQuestionGroupMaster.setTotalNullGroupFeedbackQuestion((short) jsonArray.getJSONObject(i).getInt("TotalNullGroupFeedbackQuestion"));
                lstFeedbackQuestionGroupMaster.add(objFeedbackQuestionGroupMaster);
            }
            return lstFeedbackQuestionGroupMaster;
        } catch (JSONException e) {
            return null;
        }
    }


    public ArrayList<FeedbackQuestionGroupMaster> SelectAllFeedbackQuestionGroupMaster(short linktoBusinessMasterId) {
        ArrayList<FeedbackQuestionGroupMaster> lstFeedbackQuestionGroupMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllFeedbackQuestionGroupMaster + "/" + linktoBusinessMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllFeedbackQuestionGroupMaster + "Result");
                if (jsonArray != null) {
                    lstFeedbackQuestionGroupMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstFeedbackQuestionGroupMaster;
        } catch (Exception ex) {
            return null;
        }
    }
}
