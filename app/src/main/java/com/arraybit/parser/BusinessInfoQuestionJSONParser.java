package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.modal.BusinessInfoQuestionMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusinessInfoQuestionJSONParser {
    public String SelectBusinessInfoQuestionMaster = "SelectAllBusinessInfoAnswerMaster";

    private BusinessInfoQuestionMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessInfoQuestionMaster objBusinessInfoQuestionMaster = null;
        try {
            if (jsonObject != null) {
                objBusinessInfoQuestionMaster = new BusinessInfoQuestionMaster();
                objBusinessInfoQuestionMaster.setBusinessInfoQuestionMasterId(jsonObject.getInt("BusinessInfoQuestionMasterId"));
                objBusinessInfoQuestionMaster.setlinktoBusinessTypeMasterId((short) jsonObject.getInt("linktoBusinessTypeMasterId"));
                objBusinessInfoQuestionMaster.setQuestion(jsonObject.getString("Question"));
                objBusinessInfoQuestionMaster.setQuestionType((short) jsonObject.getInt("QuestionType"));

                /// Extra
            }
            return objBusinessInfoQuestionMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<BusinessInfoQuestionMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessInfoQuestionMaster> lstBusinessInfoQuestionMaster = new ArrayList<>();
        BusinessInfoQuestionMaster objBusinessInfoQuestionMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessInfoQuestionMaster = new BusinessInfoQuestionMaster();
                objBusinessInfoQuestionMaster.setBusinessInfoQuestionMasterId(jsonArray.getJSONObject(i).getInt("linktoBusinessInfoQuestionMasterId"));
                objBusinessInfoQuestionMaster.setQuestion(jsonArray.getJSONObject(i).getString("BusinessInfoQuestion"));
                objBusinessInfoQuestionMaster.setQuestionType((short) jsonArray.getJSONObject(i).getInt("QuestionType"));
                objBusinessInfoQuestionMaster.setIsAnswer(jsonArray.getJSONObject(i).getBoolean("IsAnswer"));
                if (!jsonArray.getJSONObject(i).getString("Answer").equals("")) {
                    objBusinessInfoQuestionMaster.setAnswer(jsonArray.getJSONObject(i).getString("Answer"));
                    lstBusinessInfoQuestionMaster.add(objBusinessInfoQuestionMaster);
                }
            }
            return lstBusinessInfoQuestionMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<BusinessInfoQuestionMaster> SelectAllBusinessInfoQuestionMaster(short linktoBusinessTypeMasterId, short linktoBusinessMasterId) {
        ArrayList<BusinessInfoQuestionMaster> lstBusinessInfoQuestionMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectBusinessInfoQuestionMaster + "/" + linktoBusinessTypeMasterId + "/" + linktoBusinessMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectBusinessInfoQuestionMaster + "Result");
                if (jsonArray != null) {
                    lstBusinessInfoQuestionMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstBusinessInfoQuestionMaster;
        } catch (Exception ex) {
            return null;
        }
    }

}
