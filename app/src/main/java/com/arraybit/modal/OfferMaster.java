package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class OfferMaster implements Parcelable {
    //region Properties

    int OfferMasterId;
    short linktoOfferTypeMasterId;
    String OfferTitle;
    String OfferContent;
    String FromDate;
    String ToDate;
    String FromTime;
    String ToTime;
    double MinimumBillAmount;
    double Discount;
    boolean IsDiscountPercentage;
    short OfferLimit;
    int RedeemCount;
    String OfferCode;
    String ImagePhysicalNameBytes;
    String ImagePhysicalName;
    String XS_ImagePhysicalName;
    String SM_ImagePhysicalName;
    String MD_ImagePhysicalName;
    String LG_ImagePhysicalName;
    String XL_ImagePhysicalName;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    short linktoBusinessMasterId;
    int linktoCustomerMasterId;
    String TermsAndConditions;
    boolean IsEnabled;
    boolean IsDeleted;
    boolean IsForCustomers;
    boolean IsUnconditional;
    int BuyItemCount;
    int GetItemCount;
    boolean IsForApp;
    boolean IsOnline;
    String linktoOrderTypeMasterIds;
    short linktoCounterMasterId;
    short linktoOrderTypeMasterId;
    /// Extra
    String OfferType;
    String Business;
    public static final Parcelable.Creator<OfferMaster> CREATOR = new Creator<OfferMaster>() {
        public OfferMaster createFromParcel(Parcel source) {
            OfferMaster objOfferMaster = new OfferMaster();
            objOfferMaster.OfferMasterId = source.readInt();
            objOfferMaster.linktoOfferTypeMasterId = (short)source.readInt();
            objOfferMaster.OfferTitle = source.readString();
            objOfferMaster.OfferContent = source.readString();
            objOfferMaster.FromDate = source.readString();
            objOfferMaster.ToDate = source.readString();
            objOfferMaster.FromTime = source.readString();
            objOfferMaster.ToTime = source.readString();
            objOfferMaster.MinimumBillAmount = source.readDouble();
            objOfferMaster.Discount = source.readDouble();
            objOfferMaster.IsDiscountPercentage = source.readByte() != 0;
            objOfferMaster.OfferLimit = (short)source.readInt();
            objOfferMaster.RedeemCount = source.readInt();
            objOfferMaster.OfferCode = source.readString();
            objOfferMaster.ImagePhysicalNameBytes = source.readString();
            objOfferMaster.ImagePhysicalName = source.readString();
            objOfferMaster.XS_ImagePhysicalName = source.readString();
            objOfferMaster.SM_ImagePhysicalName = source.readString();
            objOfferMaster.MD_ImagePhysicalName = source.readString();
            objOfferMaster.LG_ImagePhysicalName = source.readString();
            objOfferMaster.XL_ImagePhysicalName = source.readString();
            objOfferMaster.CreateDateTime = source.readString();
            objOfferMaster.linktoUserMasterIdCreatedBy = (short)source.readInt();
            objOfferMaster.UpdateDateTime = source.readString();
            objOfferMaster.linktoUserMasterIdUpdatedBy = (short)source.readInt();
            objOfferMaster.linktoBusinessMasterId = (short)source.readInt();
            objOfferMaster.linktoCustomerMasterId = source.readInt();
            objOfferMaster.TermsAndConditions = source.readString();
            objOfferMaster.IsEnabled = source.readByte() != 0;
            objOfferMaster.IsDeleted = source.readByte() != 0;
            objOfferMaster.IsForCustomers = source.readByte() != 0;
            objOfferMaster.IsUnconditional = source.readByte() != 0;
            objOfferMaster.BuyItemCount = source.readInt();
            objOfferMaster.GetItemCount = source.readInt();
            objOfferMaster.linktoCounterMasterId = (short)source.readInt();
            objOfferMaster.linktoOrderTypeMasterId = (short)source.readInt();

            /// Extra
            objOfferMaster.OfferType = source.readString();
            objOfferMaster.Business = source.readString();
            return objOfferMaster;
        }

        public OfferMaster[] newArray(int size) {
            return new OfferMaster[size];
        }
    };
    String ValidBuyItems;
    String ValidDays;
    String ValidGetItems;
    String ValidItems;
    short Counter;
    short OrderType;

    public boolean getIsForApp() {
        return IsForApp;
    }

    public void setIsForApp(boolean isForApp) {
        IsForApp = isForApp;
    }

    public boolean getIsOnline() {
        return IsOnline;
    }

    public void setIsOnline(boolean isOnline) {
        IsOnline = isOnline;
    }

    public String getLinktoOrderTypeMasterIds() {
        return linktoOrderTypeMasterIds;
    }

    public void setLinktoOrderTypeMasterIds(String linktoOrderTypeMasterIds) {
        this.linktoOrderTypeMasterIds = linktoOrderTypeMasterIds;
    }

    public String getValidBuyItems() {
        return ValidBuyItems;
    }

    public void setValidBuyItems(String validBuyItems) {
        ValidBuyItems = validBuyItems;
    }

    public String getValidDays() {
        return ValidDays;
    }

    public void setValidDays(String validDays) {
        ValidDays = validDays;
    }

    public String getValidGetItems() {
        return ValidGetItems;
    }

    public void setValidGetItems(String validGetItems) {
        ValidGetItems = validGetItems;
    }

    public String getValidItems() {
        return ValidItems;
    }

    public void setValidItems(String validItems) {
        ValidItems = validItems;
    }

    public int getOfferMasterId() { return this.OfferMasterId; }

    public void setOfferMasterId(int offerMasterId) { this.OfferMasterId = offerMasterId; }

    public short getlinktoOfferTypeMasterId() { return this.linktoOfferTypeMasterId; }

    public void setlinktoOfferTypeMasterId(short linktoOfferTypeMasterId) { this.linktoOfferTypeMasterId = linktoOfferTypeMasterId; }

    public String getOfferTitle() { return this.OfferTitle; }

    public void setOfferTitle(String offerTitle) { this.OfferTitle = offerTitle; }

    public String getOfferContent() { return this.OfferContent; }

    public void setOfferContent(String offerContent) { this.OfferContent = offerContent; }

    public String getFromDate() { return this.FromDate; }

    public void setFromDate(String fromDate) { this.FromDate = fromDate; }

    public String getToDate() { return this.ToDate; }

    public void setToDate(String toDate) { this.ToDate = toDate; }

    public String getFromTime() { return this.FromTime; }

    public void setFromTime(String fromTime) { this.FromTime = fromTime; }

    public String getToTime() { return this.ToTime; }

    public void setToTime(String toTime) { this.ToTime = toTime; }

    public double getMinimumBillAmount() { return this.MinimumBillAmount; }

    public void setMinimumBillAmount(double minimumBillAmount) { this.MinimumBillAmount = minimumBillAmount; }

    public double getDiscount() { return this.Discount; }

    public void setDiscount(double discount) { this.Discount = discount; }

    public boolean getIsDiscountPercentage() { return this.IsDiscountPercentage; }

    public void setIsDiscountPercentage(boolean isDiscountPercentage) { this.IsDiscountPercentage = isDiscountPercentage; }

    public short getOfferLimit() { return this.OfferLimit; }

    public void setOfferLimit(short offerLimit) { this.OfferLimit = offerLimit; }

    public int getRedeemCount() { return this.RedeemCount; }

    public void setRedeemCount(int redeemCount) { this.RedeemCount = redeemCount; }

    public String getOfferCode() { return this.OfferCode; }

    public void setOfferCode(String offerCode) { this.OfferCode = offerCode; }

    public String getImagePhysicalNameBytes() { return this.ImagePhysicalNameBytes; }

    public void setImagePhysicalNameBytes(String imagePhysicalNameBytes) { this.ImagePhysicalNameBytes = imagePhysicalNameBytes; }

    public String getImagePhysicalName() { return this.ImagePhysicalName; }

    public void setImagePhysicalName(String imagePhysicalName) { this.ImagePhysicalName = imagePhysicalName; }

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

    public String getTermsAndConditions() { return this.TermsAndConditions; }

    public void setTermsAndConditions(String termsAndConditions) { this.TermsAndConditions = termsAndConditions; }

    public boolean getIsEnabled() { return this.IsEnabled; }

    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }

    public boolean getIsDeleted() { return this.IsDeleted; }

    public void setIsDeleted(boolean isDeleted) { this.IsDeleted = isDeleted; }

    public boolean getIsForCustomers() { return this.IsForCustomers; }

    public void setIsForCustomers(boolean isForCustomers) { this.IsForCustomers = isForCustomers; }

    public boolean getIsUnconditional() { return this.IsUnconditional; }

    public void setIsUnconditional(boolean isUnconditional) { this.IsUnconditional = isUnconditional; }

    public int getBuyItemCount() { return this.BuyItemCount; }

    public void setBuyItemCount(int buyItemCount) { this.BuyItemCount = buyItemCount; }

    public int getGetItemCount() { return this.GetItemCount; }

    public void setGetItemCount(int getItemCount) { this.GetItemCount = getItemCount; }

    public short getlinktoCounterMasterId() { return this.linktoCounterMasterId; }

    public void setlinktoCounterMasterId(short linktoCounterMasterId) { this.linktoCounterMasterId = linktoCounterMasterId; }

    public short getlinktoOrderTypeMasterId() { return this.linktoOrderTypeMasterId; }

    public void setlinktoOrderTypeMasterId(short linktoOrderTypeMasterId) { this.linktoOrderTypeMasterId = linktoOrderTypeMasterId; }

    public String getOfferType() { return this.OfferType; }

    public void setOfferType(String offerType) { this.OfferType = offerType; }

    public String getBusiness() { return this.Business; }

    public void setBusiness(String business) { this.Business = business; }

    public short getCounter() { return this.Counter; }

    public void setCounter(short counter) { this.Counter = counter; }

    public short getOrderType() { return this.OrderType; }

    public void setOrderType(short orderType) { this.OrderType = orderType;
    }

    public String getXS_ImagePhysicalName() {
        return XS_ImagePhysicalName;
    }

    public void setXS_ImagePhysicalName(String XS_ImagePhysicalName) {
        this.XS_ImagePhysicalName = XS_ImagePhysicalName;
    }

    public String getSM_ImagePhysicalName() {
        return SM_ImagePhysicalName;
    }

    public void setSM_ImagePhysicalName(String SM_ImagePhysicalName) {
        this.SM_ImagePhysicalName = SM_ImagePhysicalName;
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

    public int getLinktoCustomerMasterId() {
        return linktoCustomerMasterId;
    }

    public void setLinktoCustomerMasterId(int linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    //endregion

    public void setXL_ImagePhysicalName(String XL_ImagePhysicalName) {
        this.XL_ImagePhysicalName = XL_ImagePhysicalName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(OfferMasterId);
        parcel.writeInt(linktoOfferTypeMasterId);
        parcel.writeString(OfferTitle);
        parcel.writeString(OfferContent);
        parcel.writeString(FromDate);
        parcel.writeString(ToDate);
        parcel.writeString(FromTime);
        parcel.writeString(ToTime);
        parcel.writeDouble(MinimumBillAmount);
        parcel.writeDouble(Discount);
        parcel.writeByte((byte) (IsDiscountPercentage ? 1 : 0));
        parcel.writeInt(OfferLimit);
        parcel.writeInt(RedeemCount);
        parcel.writeString(OfferCode);
        parcel.writeString(ImagePhysicalNameBytes);
        parcel.writeString(ImagePhysicalName);
        parcel.writeString(XS_ImagePhysicalName);
        parcel.writeString(SM_ImagePhysicalName);
        parcel.writeString(MD_ImagePhysicalName);
        parcel.writeString(LG_ImagePhysicalName);
        parcel.writeString(XL_ImagePhysicalName);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(linktoCustomerMasterId);
        parcel.writeString(TermsAndConditions);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));
        parcel.writeByte((byte)(IsDeleted ? 1 : 0));
        parcel.writeByte((byte) (IsForCustomers ? 1 : 0));
        parcel.writeByte((byte) (IsUnconditional ? 1 : 0));
        parcel.writeInt(BuyItemCount);
        parcel.writeInt(GetItemCount);
        parcel.writeInt(linktoCounterMasterId);
        parcel.writeInt(linktoOrderTypeMasterId);

        /// Extra
        parcel.writeString(OfferType);
        parcel.writeString(Business);
    }
}
