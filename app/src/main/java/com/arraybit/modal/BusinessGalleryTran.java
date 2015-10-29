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
    String ImagePhysicalName;
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
            objBusinessGalleryTran.ImagePhysicalName = source.readString();
            objBusinessGalleryTran.linktoBusinessMasterId = source.readInt();
            objBusinessGalleryTran.SortOrder = (short)source.readInt();

            /// Extra
            objBusinessGalleryTran.Business = source.readString();
            return objBusinessGalleryTran;
        }

        public BusinessGalleryTran[] newArray(int size) {
            return new BusinessGalleryTran[size];
        }
    };

    public int getBusinessGalleryTranId() { return this.BusinessGalleryTranId; }

    public void setBusinessGalleryTranId(int businessGalleryTranId) { this.BusinessGalleryTranId = businessGalleryTranId; }

    public String getImageTitle() { return this.ImageTitle; }

    public void setImageTitle(String imageTitle) { this.ImageTitle = imageTitle; }

    public String getImagePhysicalNameBytes() { return this.ImagePhysicalNameBytes; }

    public void setImagePhysicalNameBytes(String imagePhysicalNameBytes) { this.ImagePhysicalNameBytes = imagePhysicalNameBytes; }

    public String getImagePhysicalName() { return this.ImagePhysicalName; }

    public void setImagePhysicalName(String imagePhysicalName) { this.ImagePhysicalName = imagePhysicalName; }

    public int getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

    public void setlinktoBusinessMasterId(int linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public short getSortOrder() { return this.SortOrder; }

    public void setSortOrder(short sortOrder) { this.SortOrder = sortOrder; }

    public String getBusiness() { return this.Business; }

    //endregion

    public void setBusiness(String business) { this.Business = business; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(BusinessGalleryTranId);
        parcel.writeString(ImageTitle);
        parcel.writeString(ImagePhysicalNameBytes);
        parcel.writeString(ImagePhysicalName);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(SortOrder);

        /// Extra
        parcel.writeString(Business);
    }
}
