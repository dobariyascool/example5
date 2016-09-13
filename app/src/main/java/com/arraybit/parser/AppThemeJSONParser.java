package com.arraybit.parser;


import android.graphics.Color;
import android.util.Log;

import com.arraybit.global.Service;
import com.arraybit.modal.AppThemeMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/// <summary>
/// JSONParser for AppThemeMaster
/// </summary>
public class AppThemeJSONParser
{

	public String SelectAppThemeMaster = "SelectAppThemeMaster";
	public String SelectAllAppThemeMaster = "SelectAllAppThemeMaster";


	public AppThemeMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
		AppThemeMaster objAppThemeMaster = null;
		try {
			if (jsonObject != null) {
				objAppThemeMaster = new AppThemeMaster();
				objAppThemeMaster.setAppThemeMasterId(jsonObject.getInt("AppThemeMasterId"));
				objAppThemeMaster.setLogoImageName(jsonObject.getString("LogoImageName"));
				objAppThemeMaster.setProfileImageName(jsonObject.getString("ProfileImageName"));
				objAppThemeMaster.setBackImageName1(jsonObject.getString("BackImageName1"));
				objAppThemeMaster.setBackImageName2(jsonObject.getString("BackImageName2"));
				objAppThemeMaster.setWelcomeBackImage(jsonObject.getString("WelcomeBackImage"));
				objAppThemeMaster.setContactMap(jsonObject.getString("ContactMap"));
				objAppThemeMaster.setColorPrimary(Color.parseColor("#" + jsonObject.getString("ColorPrimary")));
				objAppThemeMaster.setColorPrimaryDark(Color.parseColor("#" + jsonObject.getString("ColorPrimaryDark")));
				objAppThemeMaster.setColorPrimaryLight(Color.parseColor("#"+jsonObject.getString("ColorPrimaryLight")));
				objAppThemeMaster.setColorAccent(Color.parseColor("#"+jsonObject.getString("ColorAccent")));
				objAppThemeMaster.setColorAccentDark(Color.parseColor("#"+jsonObject.getString("ColorAccentDark")));
				objAppThemeMaster.setColorAccentLight(Color.parseColor("#"+jsonObject.getString("ColorAccentLight")));
				objAppThemeMaster.setColorTextPrimary(Color.parseColor("#"+jsonObject.getString("ColorTextPrimary")));
				objAppThemeMaster.setColorFloatingButton(Color.parseColor("#"+jsonObject.getString("ColorFloatingButton")));
				objAppThemeMaster.setColorButtonRipple(Color.parseColor("#"+jsonObject.getString("ColorButtonRipple")));
				objAppThemeMaster.setColorCardView(Color.parseColor("#"+jsonObject.getString("ColorCardView")));
				objAppThemeMaster.setColorCardViewRipple(Color.parseColor("#"+jsonObject.getString("ColorCardViewRipple")));
				objAppThemeMaster.setColorCardText(Color.parseColor("#"+jsonObject.getString("ColorCardText")));
				objAppThemeMaster.setColorHeaderText(Color.parseColor("#"+jsonObject.getString("ColorHeaderText")));
			}
			return objAppThemeMaster;
		} catch (JSONException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private ArrayList<AppThemeMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
		ArrayList<AppThemeMaster> lstAppThemeMaster = new ArrayList<>();
		AppThemeMaster objAppThemeMaster;
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				objAppThemeMaster = new AppThemeMaster();
				objAppThemeMaster.setAppThemeMasterId(jsonArray.getJSONObject(i).getInt("AppThemeMasterId"));
				objAppThemeMaster.setLogoImageName(jsonArray.getJSONObject(i).getString("LogoImageName"));
				objAppThemeMaster.setProfileImageName(jsonArray.getJSONObject(i).getString("ProfileImageName"));
				objAppThemeMaster.setBackImageName1(jsonArray.getJSONObject(i).getString("BackImageName1"));
				objAppThemeMaster.setBackImageName2(jsonArray.getJSONObject(i).getString("BackImageName2"));
				objAppThemeMaster.setWelcomeBackImage(jsonArray.getJSONObject(i).getString("WelcomebackImage"));
				objAppThemeMaster.setContactMap(jsonArray.getJSONObject(i).getString("ContactMap"));
				objAppThemeMaster.setColorPrimary(Color.parseColor("#"+jsonArray.getJSONObject(i).getString("ColorPrimary")));
				objAppThemeMaster.setColorPrimaryDark(Color.parseColor("#"+jsonArray.getJSONObject(i).getString("ColorPrimaryDark")));
				objAppThemeMaster.setColorPrimaryLight(Color.parseColor("#"+jsonArray.getJSONObject(i).getString("ColorPrimaryLight")));
				objAppThemeMaster.setColorAccent(Color.parseColor("#"+jsonArray.getJSONObject(i).getString("ColorAccent")));
				objAppThemeMaster.setColorAccentDark(Color.parseColor("#"+jsonArray.getJSONObject(i).getString("ColorAccentDark")));
				objAppThemeMaster.setColorAccentLight(Color.parseColor("#"+jsonArray.getJSONObject(i).getString("ColorAccentLight")));
				objAppThemeMaster.setColorTextPrimary(Color.parseColor("#"+jsonArray.getJSONObject(i).getString("ColorTextPrimary")));
				objAppThemeMaster.setColorFloatingButton(Color.parseColor("#" + jsonArray.getJSONObject(i).getString("ColorFloatingButton")));
				objAppThemeMaster.setColorButtonRipple(Color.parseColor("#"+jsonArray.getJSONObject(i).getString("ColorButtonRipple")));
				objAppThemeMaster.setColorCardView(Color.parseColor("#"+jsonArray.getJSONObject(i).getString("ColorCardView")));
				objAppThemeMaster.setColorCardViewRipple(Color.parseColor("#"+jsonArray.getJSONObject(i).getString("ColorCardViewRipple")));
				objAppThemeMaster.setColorCardText(Color.parseColor("#"+jsonArray.getJSONObject(i).getString("ColorCardText")));
				objAppThemeMaster.setColorHeaderText(Color.parseColor("#"+jsonArray.getJSONObject(i).getString("ColorHeaderText")));
				lstAppThemeMaster.add(objAppThemeMaster);
			}
			return lstAppThemeMaster;
		} catch (JSONException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public JSONObject SelectAppThemeMaster(int linktoBusinessMasterId) {
		try {

			JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAppThemeMaster + "/" + linktoBusinessMasterId);
			Log.e("json"," "+jsonResponse);
			if (jsonResponse != null) {
				JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectAppThemeMaster + "Result");
				if (jsonObject != null) {
					return jsonObject;
				}
			}
			return null;
		}
		catch (Exception ex) {
			return null;
		}
	}

	public ArrayList<AppThemeMaster> SelectAllAppThemeMaster() {
		ArrayList<AppThemeMaster> lstAppThemeMaster = null;
		try {
			JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllAppThemeMaster);
			if (jsonResponse != null) {
				JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllAppThemeMaster + "Result");
				if (jsonArray != null) {
					lstAppThemeMaster = SetListPropertiesFromJSONArray(jsonArray);
				}
			}
			return lstAppThemeMaster;
		}
		catch (Exception ex) {
			return null;
		}
	}

}
