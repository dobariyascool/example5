package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.modal.FeedbackAnswerMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedbackAnswerJSONParser {

    public String SelectAllFeedbackAnswerMaster = "SelectAllFeedbackAnswerMaster";

    private FeedbackAnswerMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        FeedbackAnswerMaster objFeedbackAnswerMaster = null;
        try {
            if (jsonObject != null) {
                objFeedbackAnswerMaster = new FeedbackAnswerMaster();
                objFeedbackAnswerMaster.setFeedbackAnswerMasterId(jsonObject.getInt("FeedbackAnswerMasterId"));
                objFeedbackAnswerMaster.setlinktoFeedbackQuestionMasterId(jsonObject.getInt("linktoFeedbackQuestionMasterId"));
                objFeedbackAnswerMaster.setAnswer(jsonObject.getString("Answer"));
                objFeedbackAnswerMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));

                /// Extra
                objFeedbackAnswerMaster.setFeedbackQuestion(jsonObject.getString("FeedbackQuestion"));
            }
            return objFeedbackAnswerMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<FeedbackAnswerMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<FeedbackAnswerMaster> lstFeedbackAnswerMaster = new ArrayList<>();
        FeedbackAnswerMaster objFeedbackAnswerMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objFeedbackAnswerMaster = new FeedbackAnswerMaster();
                objFeedbackAnswerMaster.setFeedbackAnswerMasterId(jsonArray.getJSONObject(i).getInt("FeedbackAnswerMasterId"));
                objFeedbackAnswerMaster.setlinktoFeedbackQuestionMasterId(jsonArray.getJSONObject(i).getInt("linktoFeedbackQuestionMasterId"));
                objFeedbackAnswerMaster.setAnswer(jsonArray.getJSONObject(i).getString("Answer"));
                objFeedbackAnswerMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));

                /// Extra
                objFeedbackAnswerMaster.setFeedbackQuestion(jsonArray.getJSONObject(i).getString("FeedbackQuestion"));
                lstFeedbackAnswerMaster.add(objFeedbackAnswerMaster);
            }
            return lstFeedbackAnswerMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<FeedbackAnswerMaster> SelectAllFeedbackAnswerMaster() {
        ArrayList<FeedbackAnswerMaster> lstFeedbackAnswerMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllFeedbackAnswerMaster);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllFeedbackAnswerMaster + "Result");
                if (jsonArray != null) {
                    lstFeedbackAnswerMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstFeedbackAnswerMaster;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
