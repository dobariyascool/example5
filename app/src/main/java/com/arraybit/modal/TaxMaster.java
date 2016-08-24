package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class TaxMaster implements Parcelable {
    //region Properties

    short TaxMasterId;
    String TaxName;
    double TaxRate;
    short linktoBusinessMasterId;
    boolean IsEnabled;
    boolean IsDeleted;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    public static final Parcelable.Creator<TaxMaster> CREATOR = new Creator<TaxMaster>() {
        public TaxMaster createFromParcel(Parcel source) {
            TaxMaster objTaxMaster = new TaxMaster();
            objTaxMaster.TaxMasterId = (short) source.readInt();
            objTaxMaster.TaxName = source.readString();
            objTaxMaster.TaxRate = source.readDouble();
            objTaxMaster.linktoBusinessMasterId = (short) source.readInt();
            objTaxMaster.IsEnabled = source.readByte() != 0;
            objTaxMaster.IsDeleted = source.readByte() != 0;
            objTaxMaster.CreateDateTime = source.readString();
            objTaxMaster.linktoUserMasterIdCreatedBy = (short) source.readInt();
            objTaxMaster.UpdateDateTime = source.readString();
            objTaxMaster.linktoUserMasterIdUpdatedBy = (short) source.readInt();
            return objTaxMaster;
        }

        public TaxMaster[] newArray(int size) {
            return new TaxMaster[size];
        }
    };

    public short getTaxMasterId() {
        return this.TaxMasterId;
    }

    public void setTaxMasterId(short taxMasterId) {
        this.TaxMasterId = taxMasterId;
    }

    public String getTaxName() {
        return this.TaxName;
    }

    public void setTaxName(String taxName) {
        this.TaxName = taxName;
    }

    public double getTaxRate() {
        return this.TaxRate;
    }

    public void setTaxRate(double taxRate) {
        this.TaxRate = taxRate;
    }

    public short getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public boolean getIsEnabled() {
        return this.IsEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.IsEnabled = isEnabled;
    }

    public boolean getIsDeleted() {
        return this.IsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.IsDeleted = isDeleted;
    }

    public String getCreateDateTime() {
        return this.CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.CreateDateTime = createDateTime;
    }

    public short getlinktoUserMasterIdCreatedBy() {
        return this.linktoUserMasterIdCreatedBy;
    }

    public void setlinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) {
        this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy;
    }

    public String getUpdateDateTime() {
        return this.UpdateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.UpdateDateTime = updateDateTime;
    }

    public short getlinktoUserMasterIdUpdatedBy() {
        return this.linktoUserMasterIdUpdatedBy;
    }

    //endregion

    public void setlinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) {
        this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(TaxMasterId);
        parcel.writeString(TaxName);
        parcel.writeDouble(TaxRate);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeByte((byte) (IsEnabled ? 1 : 0));
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
    }

}
