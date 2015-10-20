package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class AreaMaster implements Parcelable {

    //region Properties
    short AreaMasterId;
    String AreaName;
    String ZipCode;
    short linktoCityMasterId;
    Boolean IsEnabled;

    /// Extra
    String City;
    public static final Creator<AreaMaster> CREATOR = new Creator<AreaMaster>() {
        @Override
        public AreaMaster createFromParcel(Parcel source) {
            AreaMaster objAreaMaster = new AreaMaster();
            objAreaMaster.AreaMasterId = (short) source.readInt();
            objAreaMaster.AreaName = source.readString();
            objAreaMaster.ZipCode = source.readString();
            objAreaMaster.linktoCityMasterId = (short) source.readInt();
            objAreaMaster.IsEnabled = source.readByte() != 0;
            /// Extra
            objAreaMaster.City = source.readString();
            return objAreaMaster;
        }

        @Override
        public AreaMaster[] newArray(int size) {
            return new AreaMaster[size];
        }
    };

    public short getAreaMasterId() {
        return AreaMasterId;
    }

    public void setAreaMasterId(short areaMasterId) {
        AreaMasterId = areaMasterId;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    public short getLinktoCityMasterId() {
        return linktoCityMasterId;
    }

    public void setLinktoCityMasterId(short linktoCityMasterId) {
        this.linktoCityMasterId = linktoCityMasterId;
    }

    public Boolean getIsEnabled() {
        return IsEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        IsEnabled = isEnabled;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeInt(AreaMasterId);
        parcel.writeString(AreaName);
        parcel.writeString(ZipCode);
        parcel.writeInt(linktoCityMasterId);
        parcel.writeByte((byte) (IsEnabled ? 1 : 0));

        /// Extra
        parcel.writeString(City);
    }
}
