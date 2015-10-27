package com.arraybit.parser;

import com.arraybit.global.Service;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.FeedbackTypeMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/// <summary>
/// JSONParser for FeedbackTypeMaster
/// </summary>
public class FeedbackTypeJSONParser
{
    public String SelectAllFeedbackTypeMaster = "SelectAllFeedbackTypeMaster";
    public String SelectAllFeedbackTypeMasterFeedbackType = "SelectAllFeedbackTypeMasterFeedbackType";


    private FeedbackTypeMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        FeedbackTypeMaster objFeedbackTypeMaster = null;
        try {
            if (jsonObject != null) {
                objFeedbackTypeMaster = new FeedbackTypeMaster();
                objFeedbackTypeMaster.setFeedbackTypeMasterId((short)jsonObject.getInt("FeedbackTypeMasterId"));
                objFeedbackTypeMaster.setFeedbackType(jsonObject.getString("FeedbackType"));
            }
            return objFeedbackTypeMaster;
        } catch (JSONException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<FeedbackTypeMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<FeedbackTypeMaster> lstFeedbackTypeMaster = new ArrayList<>();
        FeedbackTypeMaster objFeedbackTypeMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objFeedbackTypeMaster = new FeedbackTypeMaster();
                objFeedbackTypeMaster.setFeedbackTypeMasterId((short)jsonArray.getJSONObject(i).getInt("FeedbackTypeMasterId"));
                objFeedbackTypeMaster.setFeedbackType(jsonArray.getJSONObject(i).getString("FeedbackType"));
                lstFeedbackTypeMaster.add(objFeedbackTypeMaster);
            }
            return lstFeedbackTypeMaster;
        } catch (JSONException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<FeedbackTypeMaster> SelectAllFeedbackTypeMaster() {
        ArrayList<FeedbackTypeMaster> lstFeedbackTypeMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllFeedbackTypeMaster);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllFeedbackTypeMaster + "Result");
                if (jsonArray != null) {
                    lstFeedbackTypeMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstFeedbackTypeMaster;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<SpinnerItem> SelectAllFeedbackTypeMasterFeedbackType() {
        ArrayList<SpinnerItem> lstSpinnerItem = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllFeedbackTypeMasterFeedbackType);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllFeedbackTypeMasterFeedbackType + "Result");
                if (jsonArray != null) {
                    lstSpinnerItem = new ArrayList<>();
                    SpinnerItem objSpinnerItem;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objSpinnerItem = new SpinnerItem();
                        objSpinnerItem.setText(jsonArray.getJSONObject(i).getString("FeedbackType"));
                        objSpinnerItem.setValue(jsonArray.getJSONObject(i).getInt("FeedbackTypeMasterId"));
                        lstSpinnerItem.add(objSpinnerItem);
                    }
                }
            }
            return lstSpinnerItem;
        }
        catch (Exception ex) {
            return null;
        }
    }

}
