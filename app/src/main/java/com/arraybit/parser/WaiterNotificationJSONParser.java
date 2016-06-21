package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.WaiterNotificationMaster;

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
/// JSONParser for WaiterNotificationMaster
/// </summary>
public class WaiterNotificationJSONParser
{
	public String InsertWaiterNotification = "InsertWaiterNotification";
	public String InsertWaiterNotificationTran = "InsertWaiterNotificationTran";
	public String SelectAllWaiterNotificationMaster = "SelectAllWaiterNotificationMaster";

	SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
	SimpleDateFormat sdfTimeFormat = new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US);
	SimpleDateFormat sdfDateFormat = new SimpleDateFormat(Globals.DateTimeFormat, Locale.US);
	Date dt = null;
	Date time = null;
	SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

	private WaiterNotificationMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
		WaiterNotificationMaster objWaiterNotificationMaster = null;
		try {
			if (jsonObject != null) {
				objWaiterNotificationMaster = new WaiterNotificationMaster();
				objWaiterNotificationMaster.setWaiterNotificationMasterId((short) jsonObject.getInt("WaiterNotificationMasterId"));
				objWaiterNotificationMaster.setlinktoTableMasterId((short) jsonObject.getInt("linktoTableMasterId"));
				if(!jsonObject.getString("Message").equals("null")) {
					objWaiterNotificationMaster.setMessage(jsonObject.getString("Message"));
				}
				dt = sdfDateTimeFormat.parse(jsonObject.getString("NotificationDateTime"));
				objWaiterNotificationMaster.setNotificationDateTime(sdfControlDateFormat.format(dt));
				/// Extra
				objWaiterNotificationMaster.setTable(jsonObject.getString("TableName"));
			}
			return objWaiterNotificationMaster;
		} catch (JSONException e) {
			return null;
		} catch (ParseException e) {
			return null;
		}
	}

	private ArrayList<WaiterNotificationMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
		ArrayList<WaiterNotificationMaster> lstWaiterNotificationMaster = new ArrayList<>();
		WaiterNotificationMaster objWaiterNotificationMaster;
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				objWaiterNotificationMaster = new WaiterNotificationMaster();
				objWaiterNotificationMaster.setWaiterNotificationMasterId((short) jsonArray.getJSONObject(i).getInt("WaiterNotificationMasterId"));
				objWaiterNotificationMaster.setlinktoTableMasterId((short) jsonArray.getJSONObject(i).getInt("linktoTableMasterId"));
				if (!jsonArray.getJSONObject(i).getString("Message").equals("null")) {
					objWaiterNotificationMaster.setMessage(jsonArray.getJSONObject(i).getString("Message"));
				}
				dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("NotificationDateTime"));
				objWaiterNotificationMaster.setNotificationDateTime(sdfControlDateFormat.format(dt));
				objWaiterNotificationMaster.setNotificationTime(sdfTimeFormat.format(dt));
				/// Extra
				objWaiterNotificationMaster.setTable(jsonArray.getJSONObject(i).getString("TableName"));
				if(jsonArray.getJSONObject(i).getInt("WaiterNotificationTranId")==0) {
					lstWaiterNotificationMaster.add(objWaiterNotificationMaster);
				}
			}
			return lstWaiterNotificationMaster;
		} catch (JSONException e) {
			return null;
		} catch (ParseException e) {
			return null;
		}
	}

	//region Insert

	public String InsertWaiterNotification(WaiterNotificationMaster objWaiterNotificationMaster) {
		dt = new Date();
		try {
			JSONStringer stringer = new JSONStringer();
			stringer.object();

			stringer.key("waiterNotificationMaster");
			stringer.object();

			stringer.key("Message").value(objWaiterNotificationMaster.getMessage());
			stringer.key("linktoTableMasterId").value(objWaiterNotificationMaster.getlinktoTableMasterId());
			stringer.key("NotificationDateTime").value(sdfDateTimeFormat.format(dt));

			stringer.endObject();

			stringer.endObject();

			JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertWaiterNotification, stringer);
			if (jsonResponse != null) {
				JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertWaiterNotification + "Result");
				if (jsonObject != null) {
					return String.valueOf(jsonObject.getInt("ErrorCode"));
				}
			}
			return "-1";
		} catch (Exception ex) {
			return "-1";
		}
	}

	public String InsertWaiterNotificationTran(WaiterNotificationMaster objWaiterNotificationMaster) {
		dt = new Date();
		try {
			JSONStringer stringer = new JSONStringer();
			stringer.object();

			stringer.key("waiterNotificationTran");
			stringer.object();

			stringer.key("linktoWaiterNotificationMasterId").value(objWaiterNotificationMaster.getWaiterNotificationMasterId());
			stringer.key("linktoUserMasterId").value(objWaiterNotificationMaster.getLinktoUserMasterId());

			stringer.endObject();

			stringer.endObject();

			JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertWaiterNotificationTran, stringer);
			if (jsonResponse != null) {
				JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertWaiterNotificationTran + "Result");
				if (jsonObject != null) {
					return String.valueOf(jsonObject.getInt("ErrorCode"));
				}
			}
			return "-1";
		} catch (Exception ex) {
			return "-1";
		}
	}

	//endregion

	//region SelectAll
	public ArrayList<WaiterNotificationMaster> SelectAllWaiterNotificationMaster(int linktoWaiterMasterId,int duration) {
		ArrayList<WaiterNotificationMaster> lstWaiterNotificationMaster = null;
		Date date;
		try {
			date = new Date(System.currentTimeMillis() - duration * 60 * 1000);
			//date = new Date();
			JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllWaiterNotificationMaster + "/" + linktoWaiterMasterId + "/" +sdfDateFormat.format(date));
			if (jsonResponse != null) {
				JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllWaiterNotificationMaster + "Result");
				if (jsonArray != null) {
					lstWaiterNotificationMaster = SetListPropertiesFromJSONArray(jsonArray);
				}
			}
			return lstWaiterNotificationMaster;
		} catch (Exception ex) {
			return null;
		}
	}
	//endregion
}
