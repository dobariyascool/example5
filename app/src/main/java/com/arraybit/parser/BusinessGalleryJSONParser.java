package com.arraybit.parser;


import com.arraybit.global.Service;
import com.arraybit.modal.BusinessGalleryTran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusinessGalleryJSONParser
{
    public String SelectAllBusinessGalleryTran = "SelectAllBusinessGalleryTran";

    private BusinessGalleryTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessGalleryTran objBusinessGalleryTran = null;
        try {
            if (jsonObject != null) {
                objBusinessGalleryTran = new BusinessGalleryTran();
                objBusinessGalleryTran.setBusinessGalleryTranId(jsonObject.getInt("BusinessGalleryTranId"));
                objBusinessGalleryTran.setImageTitle(jsonObject.getString("ImageTitle"));
                objBusinessGalleryTran.setImagePhysicalName(jsonObject.getString("ImagePhysicalName"));
                objBusinessGalleryTran.setlinktoBusinessMasterId(jsonObject.getInt("linktoBusinessMasterId"));
                if (!jsonObject.getString("SortOrder").equals("null")) {
                    objBusinessGalleryTran.setSortOrder((short)jsonObject.getInt("SortOrder"));
                }

                /// Extra
                objBusinessGalleryTran.setBusiness(jsonObject.getString("Business"));
            }
            return objBusinessGalleryTran;
        } catch (JSONException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<BusinessGalleryTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessGalleryTran> lstBusinessGalleryTran = new ArrayList<>();
        BusinessGalleryTran objBusinessGalleryTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessGalleryTran = new BusinessGalleryTran();
                objBusinessGalleryTran.setBusinessGalleryTranId(jsonArray.getJSONObject(i).getInt("BusinessGalleryTranId"));
                objBusinessGalleryTran.setImageTitle(jsonArray.getJSONObject(i).getString("ImageTitle"));
                objBusinessGalleryTran.setImagePhysicalName(jsonArray.getJSONObject(i).getString("ImagePhysicalName"));
                objBusinessGalleryTran.setlinktoBusinessMasterId(jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                if (!jsonArray.getJSONObject(i).getString("SortOrder").equals("null")) {
                    objBusinessGalleryTran.setSortOrder((short)jsonArray.getJSONObject(i).getInt("SortOrder"));
                }

                /// Extra
                objBusinessGalleryTran.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstBusinessGalleryTran.add(objBusinessGalleryTran);
            }
            return lstBusinessGalleryTran;
        } catch (JSONException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<BusinessGalleryTran> SelectAllBusinessGalleryTranPageWise(int BusinessMasterId) {
        ArrayList<BusinessGalleryTran> lstBusinessGalleryTran = null;
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllBusinessGalleryTran+"/"+BusinessMasterId);
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllBusinessGalleryTran + "Result");
                if (jsonArray != null) {
                    lstBusinessGalleryTran = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstBusinessGalleryTran;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
