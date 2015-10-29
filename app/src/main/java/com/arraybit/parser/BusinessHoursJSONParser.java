package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessHoursTran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/// <summary>
/// JSONParser for BusinessHoursTran
/// </summary>
public class BusinessHoursJSONParser {
    public String SelectAllBusinessHoursTran = "SelectAllBusinessHoursTranPageWise";
    public String SelectAllBusinessHoursTranById = "SelectAllBusinessHoursTranByBusinessMasterId";

    // SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    SimpleDateFormat sdfControlTimeFormat = new SimpleDateFormat(Globals.TimeFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    private BusinessHoursTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessHoursTran objBusinessHoursTran = null;
        try {
            if (jsonObject != null) {
                objBusinessHoursTran = new BusinessHoursTran();
                objBusinessHoursTran.setBusinessHoursTranId((short) jsonObject.getInt("BusinessHoursTranId"));
                objBusinessHoursTran.setDayOfWeek((short) jsonObject.getInt("DayOfWeek"));
                dt = sdfTimeFormat.parse(jsonObject.getString("OpeningTime"));
                objBusinessHoursTran.setOpeningTime(sdfControlTimeFormat.format(dt));
                dt = sdfTimeFormat.parse(jsonObject.getString("ClosingTime"));
                objBusinessHoursTran.setClosingTime(sdfControlTimeFormat.format(dt));
                objBusinessHoursTran.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
            }
            return objBusinessHoursTran;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<BusinessHoursTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessHoursTran> lstBusinessHoursTran = new ArrayList<>();
        BusinessHoursTran objBusinessHoursTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessHoursTran = new BusinessHoursTran();
                objBusinessHoursTran.setBusinessHoursTranId((short) jsonArray.getJSONObject(i).getInt("BusinessHoursTranId"));
                objBusinessHoursTran.setDayOfWeek((short) jsonArray.getJSONObject(i).getInt("DayOfWeek"));
                dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("OpeningTime"));
                objBusinessHoursTran.setOpeningTime(sdfControlTimeFormat.format(dt));
                dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("ClosingTime"));
                objBusinessHoursTran.setClosingTime(sdfControlTimeFormat.format(dt));
                objBusinessHoursTran.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                lstBusinessHoursTran.add(objBusinessHoursTran);
            }
            return lstBusinessHoursTran;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public ArrayList<BusinessHoursTran> SelectAllBusinessHoursTranPageWise(int currentPage) {
        ArrayList<BusinessHoursTran> lstBusinessHoursTran = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllBusinessHoursTran);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllBusinessHoursTran + "PageWiseResult");
                if (jsonArray != null) {
                    lstBusinessHoursTran = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstBusinessHoursTran;
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<BusinessHoursTran> SelectAllBusinessHoursTranById(int BusinessMasterId) {
        ArrayList<BusinessHoursTran> lstBusinessHoursTran = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllBusinessHoursTranById + "/" + BusinessMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllBusinessHoursTranById + "Result");
                if (jsonArray != null) {
                    lstBusinessHoursTran = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstBusinessHoursTran;
        } catch (JSONException e) {
            return null;
        }
    }

}
