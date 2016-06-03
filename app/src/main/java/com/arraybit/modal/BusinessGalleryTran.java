package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for BusinessGalleryTran
/// </summary>
public class BusinessGalleryTran implements Parcelable {
    //region Properties

    int BusinessGalleryTranId;
    String ImageTitle;
    String ImagePhysicalNameBytes;
    String ImageName;
    String XS_ImagePhysicalName;
    String SM_ImagePhysicalName;
    String MD_ImagePhysicalName;
    String LG_ImagePhysicalName;
    String XL_ImagePhysicalName;
    int linktoBusinessMasterId;
    short SortOrder;
    /// Extra
    String Business;
    public static final Parcelable.Creator<BusinessGalleryTran> CREATOR = new Creator<BusinessGalleryTran>() {
        public BusinessGalleryTran createFromParcel(Parcel source) {
            BusinessGalleryTran objBusinessGalleryTran = new BusinessGalleryTran();
            objBusinessGalleryTran.BusinessGalleryTranId = source.readInt();
            objBusinessGalleryTran.ImageTitle = source.readString();
            objBusinessGalleryTran.ImagePhysicalNameBytes = source.readString();
            objBusinessGalleryTran.ImageName = source.readString();
            objBusinessGalleryTran.XS_ImagePhysicalName = source.readString();
            objBusinessGalleryTran.SM_ImagePhysicalName = source.readString();
            objBusinessGalleryTran.MD_ImagePhysicalName = source.readString();
            objBusinessGalleryTran.LG_ImagePhysicalName = source.readString();
            objBusinessGalleryTran.XL_ImagePhysicalName = source.readString();
            objBusinessGalleryTran.linktoBusinessMasterId = source.readInt();
            objBusinessGalleryTran.SortOrder = (short) source.readInt();

            /// Extra
            objBusinessGalleryTran.Business = source.readString();
            return objBusinessGalleryTran;
        }

        public BusinessGalleryTran[] newArray(int size) {
            return new BusinessGalleryTran[size];
        }
    };

    public int getBusinessGalleryTranId() {
        return this.BusinessGalleryTranId;
    }

    public void setBusinessGalleryTranId(int businessGalleryTranId) {
        this.BusinessGalleryTranId = businessGalleryTranId;
    }

    public String getImageTitle() {
        return this.ImageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.ImageTitle = imageTitle;
    }

    public String getImagePhysicalNameBytes() {
        return this.ImagePhysicalNameBytes;
    }

    public void setImagePhysicalNameBytes(String imagePhysicalNameBytes) {
        this.ImagePhysicalNameBytes = imagePhysicalNameBytes;
    }

    public String getImagePhysicalName() {
        return this.ImageName;
    }

    public void setImagePhysicalName(String imagePhysicalName) {
        this.ImageName = imagePhysicalName;
    }

    public int getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(int linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public short getSortOrder() {
        return this.SortOrder;
    }

    public void setSortOrder(short sortOrder) {
        this.SortOrder = sortOrder;
    }

    public String getBusiness() {
        return this.Business;
    }

    public void setBusiness(String business) {
        this.Business = business;
    }

    public String getSM_ImagePhysicalName() {
        return SM_ImagePhysicalName;
    }

    public void setSM_ImagePhysicalName(String SM_ImagePhysicalName) {
        this.SM_ImagePhysicalName = SM_ImagePhysicalName;
    }

    public String getXS_ImagePhysicalName() {
        return XS_ImagePhysicalName;
    }

    public void setXS_ImagePhysicalName(String XS_ImagePhysicalName) {
        this.XS_ImagePhysicalName = XS_ImagePhysicalName;
    }

    public String getMD_ImagePhysicalName() {
        return MD_ImagePhysicalName;
    }

    public void setMD_ImagePhysicalName(String MD_ImagePhysicalName) {
        this.MD_ImagePhysicalName = MD_ImagePhysicalName;
    }

    public String getLG_ImagePhysicalName() {
        return LG_ImagePhysicalName;
    }

    public void setLG_ImagePhysicalName(String LG_ImagePhysicalName) {
        this.LG_ImagePhysicalName = LG_ImagePhysicalName;
    }

    public String getXL_ImagePhysicalName() {
        return XL_ImagePhysicalName;
    }


    //endregion

    public void setXL_ImagePhysicalName(String XL_ImagePhysicalName) {
        this.XL_ImagePhysicalName = XL_ImagePhysicalName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(BusinessGalleryTranId);
        parcel.writeString(ImageTitle);
        parcel.writeString(ImagePhysicalNameBytes);
        parcel.writeString(ImageName);
        parcel.writeString(XS_ImagePhysicalName);
        parcel.writeString(SM_ImagePhysicalName);
        parcel.writeString(MD_ImagePhysicalName);
        parcel.writeString(LG_ImagePhysicalName);
        parcel.writeString(XL_ImagePhysicalName);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(SortOrder);

        /// Extra
        parcel.writeString(Business);
    }
}
