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

    //region Class Method
    private BusinessGalleryTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessGalleryTran objBusinessGalleryTran = null;
        try {
            if (jsonObject != null) {
                objBusinessGalleryTran = new BusinessGalleryTran();
                objBusinessGalleryTran.setBusinessGalleryTranId(jsonObject.getInt("BusinessGalleryTranId"));
                objBusinessGalleryTran.setImageTitle(jsonObject.getString("ImageTitle"));
                objBusinessGalleryTran.setImagePhysicalName(jsonObject.getString("ImageName"));
                objBusinessGalleryTran.setXS_ImagePhysicalName(jsonObject.getString("xs_ImagePhysicalName"));
                objBusinessGalleryTran.setSM_ImagePhysicalName(jsonObject.getString("sm_ImagePhysicalName"));
                objBusinessGalleryTran.setMD_ImagePhysicalName(jsonObject.getString("md_ImagePhysicalName"));
                objBusinessGalleryTran.setLG_ImagePhysicalName(jsonObject.getString("lg_ImagePhysicalName"));
                objBusinessGalleryTran.setXL_ImagePhysicalName(jsonObject.getString("xl_ImagePhysicalName"));
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
                objBusinessGalleryTran.setImagePhysicalName(jsonArray.getJSONObject(i).getString("ImageName"));
                objBusinessGalleryTran.setXS_ImagePhysicalName(jsonArray.getJSONObject(i).getString("xs_ImagePhysicalName"));
                objBusinessGalleryTran.setSM_ImagePhysicalName(jsonArray.getJSONObject(i).getString("sm_ImagePhysicalName"));
                objBusinessGalleryTran.setMD_ImagePhysicalName(jsonArray.getJSONObject(i).getString("md_ImagePhysicalName"));
                objBusinessGalleryTran.setLG_ImagePhysicalName(jsonArray.getJSONObject(i).getString("lg_ImagePhysicalName"));
                objBusinessGalleryTran.setXL_ImagePhysicalName(jsonArray.getJSONObject(i).getString("xl_ImagePhysicalName"));
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
    //endregion

    //region SelectAll
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
    //endregion
}
