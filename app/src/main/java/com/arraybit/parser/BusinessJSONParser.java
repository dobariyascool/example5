package com.arraybit.parser;


import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessMaster;

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
/// JSONParser for BusinessMaster
/// </summary>
public class BusinessJSONParser
{
    public String InsertBusinessMaster = "InsertBusinessMaster";
    public String UpdateBusinessMaster = "UpdateBusinessMaster";
    public String SelectBusinessMaster = "SelectBusinessMaster";
    public String SelectAllBusinessMaster = "SelectAllBusinessMasterPageWise";
    public String SelectBusinessMasterByUniqueId="SelectBusinessMasterByUniqueId";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private BusinessMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessMaster objBusinessMaster = null;
        try {
            if (jsonObject != null) {
                objBusinessMaster = new BusinessMaster();
                objBusinessMaster.setBusinessMasterId((short)jsonObject.getInt("BusinessMasterId"));
                objBusinessMaster.setBusinessName(jsonObject.getString("BusinessName"));
                objBusinessMaster.setBusinessShortName(jsonObject.getString("BusinessShortName"));
                objBusinessMaster.setAddress(jsonObject.getString("Address"));
                objBusinessMaster.setPhone1(jsonObject.getString("Phone1"));
                objBusinessMaster.setPhone2(jsonObject.getString("Phone2"));
                objBusinessMaster.setEmail(jsonObject.getString("Email"));
                objBusinessMaster.setFax(jsonObject.getString("Fax"));
                objBusinessMaster.setWebsite(jsonObject.getString("Website"));
                objBusinessMaster.setlinktoCountryMasterId((short)jsonObject.getInt("linktoCountryMasterId"));
                objBusinessMaster.setlinktoStateMasterId((short)jsonObject.getInt("linktoStateMasterId"));
                objBusinessMaster.setCity(jsonObject.getString("City"));
                objBusinessMaster.setZipCode(jsonObject.getString("ZipCode"));
                objBusinessMaster.setImageName(jsonObject.getString("ImageName"));
                objBusinessMaster.setExtraText(jsonObject.getString("ExtraText"));
                objBusinessMaster.setTIN(jsonObject.getString("TIN"));
                dt = sdfDateFormat.parse(jsonObject.getString("TINRegistrationDate"));
                objBusinessMaster.setTINRegistrationDate(sdfControlDateFormat.format(dt));
                objBusinessMaster.setCST(jsonObject.getString("CST"));
                dt = sdfDateFormat.parse(jsonObject.getString("CSTRegistrationDate"));
                objBusinessMaster.setCSTRegistrationDate(sdfControlDateFormat.format(dt));
                objBusinessMaster.setlinktoBusinessTypeMasterId((short)jsonObject.getInt("linktoBusinessTypeMasterId"));
                objBusinessMaster.setUniqueId(jsonObject.getString("UniqueId"));
                objBusinessMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));

                /// Extra
                objBusinessMaster.setCountry(jsonObject.getString("Country"));
                objBusinessMaster.setState(jsonObject.getString("State"));
                objBusinessMaster.setBusinessType(jsonObject.getString("BusinessType"));
            }
            return objBusinessMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<BusinessMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessMaster> lstBusinessMaster = new ArrayList<>();
        BusinessMaster objBusinessMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessMaster = new BusinessMaster();
                objBusinessMaster.setBusinessMasterId((short)jsonArray.getJSONObject(i).getInt("BusinessMasterId"));
                objBusinessMaster.setBusinessName(jsonArray.getJSONObject(i).getString("BusinessName"));
                objBusinessMaster.setBusinessShortName(jsonArray.getJSONObject(i).getString("BusinessShortName"));
                objBusinessMaster.setAddress(jsonArray.getJSONObject(i).getString("Address"));
                objBusinessMaster.setPhone1(jsonArray.getJSONObject(i).getString("Phone1"));
                objBusinessMaster.setPhone2(jsonArray.getJSONObject(i).getString("Phone2"));
                objBusinessMaster.setEmail(jsonArray.getJSONObject(i).getString("Email"));
                objBusinessMaster.setFax(jsonArray.getJSONObject(i).getString("Fax"));
                objBusinessMaster.setWebsite(jsonArray.getJSONObject(i).getString("Website"));
                objBusinessMaster.setlinktoCountryMasterId((short)jsonArray.getJSONObject(i).getInt("linktoCountryMasterId"));
                objBusinessMaster.setlinktoStateMasterId((short)jsonArray.getJSONObject(i).getInt("linktoStateMasterId"));
                objBusinessMaster.setCity(jsonArray.getJSONObject(i).getString("City"));
                objBusinessMaster.setZipCode(jsonArray.getJSONObject(i).getString("ZipCode"));
                objBusinessMaster.setImageName(jsonArray.getJSONObject(i).getString("ImageName"));
                objBusinessMaster.setExtraText(jsonArray.getJSONObject(i).getString("ExtraText"));
                objBusinessMaster.setTIN(jsonArray.getJSONObject(i).getString("TIN"));
                dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("TINRegistrationDate"));
                objBusinessMaster.setTINRegistrationDate(sdfControlDateFormat.format(dt));
                objBusinessMaster.setCST(jsonArray.getJSONObject(i).getString("CST"));
                dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("CSTRegistrationDate"));
                objBusinessMaster.setCSTRegistrationDate(sdfControlDateFormat.format(dt));
                objBusinessMaster.setlinktoBusinessTypeMasterId((short)jsonArray.getJSONObject(i).getInt("linktoBusinessTypeMasterId"));
                objBusinessMaster.setUniqueId(jsonArray.getJSONObject(i).getString("UniqueId"));
                objBusinessMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));

                /// Extra
                objBusinessMaster.setCountry(jsonArray.getJSONObject(i).getString("Country"));
                objBusinessMaster.setState(jsonArray.getJSONObject(i).getString("State"));
                objBusinessMaster.setBusinessType(jsonArray.getJSONObject(i).getString("BusinessType"));
                lstBusinessMaster.add(objBusinessMaster);
            }
            return lstBusinessMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public String InsertBusinessMaster(BusinessMaster objBusinessMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("businessMaster");
            stringer.object();

            stringer.key("BusinessName").value(objBusinessMaster.getBusinessName());
            stringer.key("BusinessShortName").value(objBusinessMaster.getBusinessShortName());
            stringer.key("Address").value(objBusinessMaster.getAddress());
            stringer.key("Phone1").value(objBusinessMaster.getPhone1());
            stringer.key("Phone2").value(objBusinessMaster.getPhone2());
            stringer.key("Email").value(objBusinessMaster.getEmail());
            stringer.key("Fax").value(objBusinessMaster.getFax());
            stringer.key("Website").value(objBusinessMaster.getWebsite());
            stringer.key("linktoCountryMasterId").value(objBusinessMaster.getlinktoCountryMasterId());
            stringer.key("linktoStateMasterId").value(objBusinessMaster.getlinktoStateMasterId());
            stringer.key("City").value(objBusinessMaster.getCity());
            stringer.key("ZipCode").value(objBusinessMaster.getZipCode());
            stringer.key("ImageNameBytes").value(objBusinessMaster.getImageNameBytes());
            stringer.key("ExtraText").value(objBusinessMaster.getExtraText());
            stringer.key("TIN").value(objBusinessMaster.getTIN());
            dt = sdfControlDateFormat.parse(objBusinessMaster.getTINRegistrationDate());
            stringer.key("TINRegistrationDate").value(sdfDateFormat.format(dt));
            stringer.key("CST").value(objBusinessMaster.getCST());
            dt = sdfControlDateFormat.parse(objBusinessMaster.getCSTRegistrationDate());
            stringer.key("CSTRegistrationDate").value(sdfDateFormat.format(dt));
            stringer.key("linktoBusinessTypeMasterId").value(objBusinessMaster.getlinktoBusinessTypeMasterId());
            stringer.key("UniqueId").value(objBusinessMaster.getUniqueId());
            stringer.key("IsEnabled").value(objBusinessMaster.getIsEnabled());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertBusinessMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertBusinessMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        }
        catch (Exception ex) {
            return "-1";
        }
    }

    public String UpdateBusinessMaster(BusinessMaster objBusinessMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("businessMaster");
            stringer.object();

            stringer.key("BusinessMasterId").value(objBusinessMaster.getBusinessMasterId());
            stringer.key("BusinessName").value(objBusinessMaster.getBusinessName());
            stringer.key("BusinessShortName").value(objBusinessMaster.getBusinessShortName());
            stringer.key("Address").value(objBusinessMaster.getAddress());
            stringer.key("Phone1").value(objBusinessMaster.getPhone1());
            stringer.key("Phone2").value(objBusinessMaster.getPhone2());
            stringer.key("Email").value(objBusinessMaster.getEmail());
            stringer.key("Fax").value(objBusinessMaster.getFax());
            stringer.key("Website").value(objBusinessMaster.getWebsite());
            stringer.key("linktoCountryMasterId").value(objBusinessMaster.getlinktoCountryMasterId());
            stringer.key("linktoStateMasterId").value(objBusinessMaster.getlinktoStateMasterId());
            stringer.key("City").value(objBusinessMaster.getCity());
            stringer.key("ZipCode").value(objBusinessMaster.getZipCode());
            stringer.key("ImageNameBytes").value(objBusinessMaster.getImageNameBytes());
            stringer.key("ExtraText").value(objBusinessMaster.getExtraText());
            stringer.key("TIN").value(objBusinessMaster.getTIN());
            dt = sdfControlDateFormat.parse(objBusinessMaster.getTINRegistrationDate());
            stringer.key("TINRegistrationDate").value(sdfDateFormat.format(dt));
            stringer.key("CST").value(objBusinessMaster.getCST());
            dt = sdfControlDateFormat.parse(objBusinessMaster.getCSTRegistrationDate());
            stringer.key("CSTRegistrationDate").value(sdfDateFormat.format(dt));
            stringer.key("linktoBusinessTypeMasterId").value(objBusinessMaster.getlinktoBusinessTypeMasterId());
            stringer.key("UniqueId").value(objBusinessMaster.getUniqueId());
            stringer.key("IsEnabled").value(objBusinessMaster.getIsEnabled());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateBusinessMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateBusinessMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        }
        catch (Exception ex) {
            return "-1";
        }
    }

    public BusinessMaster SelectBusinessMaster(short businessMasterId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectBusinessMaster + "/" + businessMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectBusinessMaster + "Result");
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

    public BusinessMaster SelectBusinessMasterByUniqueId(String uniqueId)
    {
        try {
            JSONObject jsonResponse=Service.HttpGetService(Service.Url+this.SelectBusinessMasterByUniqueId+"/"+uniqueId);
            if(jsonResponse!=null)
            {
                JSONObject jsonObject=jsonResponse.getJSONObject(this.SelectBusinessMasterByUniqueId+"Result");
                if(jsonObject!=null)
                {
                    return SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return null;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public ArrayList<BusinessMaster> SelectAllBusinessMasterPageWise(int currentPage) {
        ArrayList<BusinessMaster> lstBusinessMaster = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllBusinessMaster);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllBusinessMaster + "PageWiseResult");
                if (jsonArray != null) {
                    lstBusinessMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstBusinessMaster;
        }
        catch (Exception ex) {
            return null;
        }
    }

}
