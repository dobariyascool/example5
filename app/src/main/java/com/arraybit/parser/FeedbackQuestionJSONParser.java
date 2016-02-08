package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.modal.FeedbackQuestionMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedbackQuestionJSONParser {

    public String SelectAllFeedbackQuestionMaster = "SelectAllFeedbackQuestionMaster";


    private FeedbackQuestionMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        FeedbackQuestionMaster objFeedbackQuestionMaster = null;
        try {
            if (jsonObject != null) {
                objFeedbackQuestionMaster = new FeedbackQuestionMaster();
                objFeedbackQuestionMaster.setFeedbackQuestionMasterId(jsonObject.getInt("FeedbackQuestionMasterId"));
                objFeedbackQuestionMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objFeedbackQuestionMaster.setFeedbackQuestion(jsonObject.getString("FeedbackQuestion"));
                objFeedbackQuestionMaster.setQuestionType((short)jsonObject.getInt("QuestionType"));
                if (!jsonObject.getString("SortOrder").equals("null")) {
                    objFeedbackQuestionMaster.setSortOrder(jsonObject.getInt("SortOrder"));
                }
                objFeedbackQuestionMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objFeedbackQuestionMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));

            }
            return objFeedbackQuestionMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<FeedbackQuestionMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<FeedbackQuestionMaster> lstFeedbackQuestionMaster = new ArrayList<>();
        FeedbackQuestionMaster objFeedbackQuestionMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objFeedbackQuestionMaster = new FeedbackQuestionMaster();
                objFeedbackQuestionMaster.setFeedbackQuestionMasterId(jsonArray.getJSONObject(i).getInt("FeedbackQuestionMasterId"));
                objFeedbackQuestionMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objFeedbackQuestionMaster.setFeedbackQuestion(jsonArray.getJSONObject(i).getString("FeedbackQuestion"));
                objFeedbackQuestionMaster.setQuestionType((short) jsonArray.getJSONObject(i).getInt("QuestionType"));
                if (!jsonArray.getJSONObject(i).getString("SortOrder").equals("null")) {
                    objFeedbackQuestionMaster.setSortOrder(jsonArray.getJSONObject(i).getInt("SortOrder"));
                }
                objFeedbackQuestionMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objFeedbackQuestionMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                objFeedbackQuestionMaster.setLinktoFeedbackQuestionGroupMasterId((short) jsonArray.getJSONObject(i).getInt("linktoFeedbackQuestionGroupMasterId"));
                objFeedbackQuestionMaster.setGroupName(jsonArray.getJSONObject(i).getString("QuestionGroupName"));
                lstFeedbackQuestionMaster.add(objFeedbackQuestionMaster);
            }
            return lstFeedbackQuestionMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<FeedbackQuestionMaster> SelectAllFeedbackQuestionMaster() {
        ArrayList<FeedbackQuestionMaster> lstFeedbackQuestionMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllFeedbackQuestionMaster);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllFeedbackQuestionMaster + "Result");
                if (jsonArray != null) {
                    lstFeedbackQuestionMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstFeedbackQuestionMaster;
        }
        catch (Exception ex) {
            return null;
        }
    }
}

