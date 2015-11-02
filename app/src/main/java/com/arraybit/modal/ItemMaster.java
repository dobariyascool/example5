package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for ItemMaster
/// </summary>
public class ItemMaster implements Parcelable {
    //region Properties

    int ItemMasterId;
    String ItemName;
    String ShortName;
    String ItemCode;
    String ShortDescription;
    double MRP;
    double SellPrice;
    short ItemPoint;
    short PriceByPoint;
    short linktoItemTypeMasterId;
    short linktoUnitMasterId;
    String SearchWords;
    String ImageNameBytes;
    String ImageName;
    short linktoItemStatusMasterId;
    int SortOrder;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    short linktoBusinessMasterId;
    boolean IsEnabled;
    boolean IsDeleted;
    /// Extra
    String ItemType;
    String Unit;
    String ItemStatus;
    public static final Parcelable.Creator<ItemMaster> CREATOR = new Creator<ItemMaster>() {
        public ItemMaster createFromParcel(Parcel source) {
            ItemMaster objItemMaster = new ItemMaster();
            objItemMaster.ItemMasterId = source.readInt();
            objItemMaster.ItemName = source.readString();
            objItemMaster.ShortName = source.readString();
            objItemMaster.ItemCode = source.readString();
            objItemMaster.ShortDescription = source.readString();
            objItemMaster.MRP = source.readDouble();
            objItemMaster.SellPrice = source.readDouble();
            objItemMaster.ItemPoint = (short)source.readInt();
            objItemMaster.PriceByPoint = (short)source.readInt();
            objItemMaster.linktoItemTypeMasterId = (short)source.readInt();
            objItemMaster.linktoUnitMasterId = (short)source.readInt();
            objItemMaster.SearchWords = source.readString();
            objItemMaster.ImageNameBytes = source.readString();
            objItemMaster.ImageName = source.readString();
            objItemMaster.linktoItemStatusMasterId = (short)source.readInt();
            objItemMaster.SortOrder = source.readInt();
            objItemMaster.CreateDateTime = source.readString();
            objItemMaster.linktoUserMasterIdCreatedBy = (short)source.readInt();
            objItemMaster.UpdateDateTime = source.readString();
            objItemMaster.linktoUserMasterIdUpdatedBy = (short)source.readInt();
            objItemMaster.linktoBusinessMasterId = (short)source.readInt();
            objItemMaster.IsEnabled = source.readByte() != 0;
            objItemMaster.IsDeleted = source.readByte() != 0;

            /// Extra
            objItemMaster.ItemType = source.readString();
            objItemMaster.Unit = source.readString();
            objItemMaster.ItemStatus = source.readString();
            return objItemMaster;
        }

        public ItemMaster[] newArray(int size) {
            return new ItemMaster[size];
        }
    };

    public int getItemMasterId() { return this.ItemMasterId; }

    public void setItemMasterId(int itemMasterId) { this.ItemMasterId = itemMasterId; }

    public String getItemName() { return this.ItemName; }

    public void setItemName(String itemName) { this.ItemName = itemName; }

    public String getShortName() { return this.ShortName; }

    public void setShortName(String shortName) { this.ShortName = shortName; }

    public String getItemCode() { return this.ItemCode; }

    public void setItemCode(String itemCode) { this.ItemCode = itemCode; }

    public String getShortDescription() { return this.ShortDescription; }

    public void setShortDescription(String shortDescription) { this.ShortDescription = shortDescription; }

    public double getMRP() { return this.MRP; }

    public void setMRP(double mRP) { this.MRP = mRP; }

    public double getSellPrice() { return this.SellPrice; }

    public void setSellPrice(double sellPrice) { this.SellPrice = sellPrice; }

    public short getItemPoint() { return this.ItemPoint; }

    public void setItemPoint(short itemPoint) { this.ItemPoint = itemPoint; }

    public short getPriceByPoint() { return this.PriceByPoint; }

    public void setPriceByPoint(short priceByPoint) { this.PriceByPoint = priceByPoint; }

    public short getlinktoItemTypeMasterId() { return this.linktoItemTypeMasterId; }

    public void setlinktoItemTypeMasterId(short linktoItemTypeMasterId) { this.linktoItemTypeMasterId = linktoItemTypeMasterId; }

    public short getlinktoUnitMasterId() { return this.linktoUnitMasterId; }

    public void setlinktoUnitMasterId(short linktoUnitMasterId) { this.linktoUnitMasterId = linktoUnitMasterId; }

    public String getSearchWords() { return this.SearchWords; }

    public void setSearchWords(String searchWords) { this.SearchWords = searchWords; }

    public String getImageNameBytes() { return this.ImageNameBytes; }

    public void setImageNameBytes(String imageNameBytes) { this.ImageNameBytes = imageNameBytes; }

    public String getImageName() { return this.ImageName; }

    public void setImageName(String imageName) { this.ImageName = imageName; }

    public short getlinktoItemStatusMasterId() { return this.linktoItemStatusMasterId; }

    public void setlinktoItemStatusMasterId(short linktoItemStatusMasterId) { this.linktoItemStatusMasterId = linktoItemStatusMasterId; }

    public int getSortOrder() { return this.SortOrder; }

    public void setSortOrder(int sortOrder) { this.SortOrder = sortOrder; }

    public String getCreateDateTime() { return this.CreateDateTime; }

    public void setCreateDateTime(String createDateTime) { this.CreateDateTime = createDateTime; }

    public short getlinktoUserMasterIdCreatedBy() { return this.linktoUserMasterIdCreatedBy; }

    public void setlinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) { this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy; }

    public String getUpdateDateTime() { return this.UpdateDateTime; }

    public void setUpdateDateTime(String updateDateTime) { this.UpdateDateTime = updateDateTime; }

    public short getlinktoUserMasterIdUpdatedBy() { return this.linktoUserMasterIdUpdatedBy; }

    public void setlinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) { this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy; }

    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public boolean getIsEnabled() { return this.IsEnabled; }

    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }

    public boolean getIsDeleted() { return this.IsDeleted; }

    public void setIsDeleted(boolean isDeleted) { this.IsDeleted = isDeleted; }

    public String getItemType() { return this.ItemType; }

    public void setItemType(String itemType) { this.ItemType = itemType; }

    public String getUnit() { return this.Unit; }

    public void setUnit(String unit) { this.Unit = unit; }

    public String getItemStatus() { return this.ItemStatus; }

    //endregion

    public void setItemStatus(String itemStatus) { this.ItemStatus = itemStatus; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(ItemMasterId);
        parcel.writeString(ItemName);
        parcel.writeString(ShortName);
        parcel.writeString(ItemCode);
        parcel.writeString(ShortDescription);
        parcel.writeDouble(MRP);
        parcel.writeDouble(SellPrice);
        parcel.writeInt(ItemPoint);
        parcel.writeInt(PriceByPoint);
        parcel.writeInt(linktoItemTypeMasterId);
        parcel.writeInt(linktoUnitMasterId);
        parcel.writeString(SearchWords);
        parcel.writeString(ImageNameBytes);
        parcel.writeString(ImageName);
        parcel.writeInt(linktoItemStatusMasterId);
        parcel.writeInt(SortOrder);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));
        parcel.writeByte((byte)(IsDeleted ? 1 : 0));

        /// Extra
        parcel.writeString(ItemType);
        parcel.writeString(Unit);
        parcel.writeString(ItemStatus);
    }
}

