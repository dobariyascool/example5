package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class CounterMaster implements Parcelable {

    public short CounterMasterId;
    public String ShortName;
    public String CounterName;
    public String Description;
    public String ImageName;
    public String CounterColor;
    public short linktoDepartmentMasterId;
    public short linktoBusinessMasterId;
    public static final Creator<CounterMaster> CREATOR = new Creator<CounterMaster>() {
        @Override
        public CounterMaster createFromParcel(Parcel source) {
            CounterMaster objCounterMaster = new CounterMaster();
            objCounterMaster.CounterMasterId = (short) source.readInt();
            objCounterMaster.ShortName = source.readString();
            objCounterMaster.CounterName = source.readString();
            objCounterMaster.Description = source.readString();
            objCounterMaster.CounterColor = source.readString();
            objCounterMaster.ImageName = source.readString();
            objCounterMaster.linktoBusinessMasterId = (short) source.readInt();
            objCounterMaster.linktoDepartmentMasterId = (short) source.readInt();

            /// Extra
            return objCounterMaster;
        }

        @Override
        public CounterMaster[] newArray(int size) {
            return new CounterMaster[size];
        }
    };

    public short getCounterMasterId() {
        return CounterMasterId;
    }

    public void setCounterMasterId(short counterMasterId) {
        CounterMasterId = counterMasterId;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getCounterName() {
        return CounterName;
    }

    public void setCounterName(String counterName) {
        CounterName = counterName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public short getLinktoDepartmentMasterId() {
        return linktoDepartmentMasterId;
    }

    public void setLinktoDepartmentMasterId(short linktoDepartmentMasterId) {
        this.linktoDepartmentMasterId = linktoDepartmentMasterId;
    }

    public String getCounterColor() {
        return CounterColor;
    }

    public void setCounterColor(String counterColor) {
        CounterColor = counterColor;
    }

    public short getLinktoBusinessMasterId() {
        return linktoBusinessMasterId;
    }

    public void setLinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(CounterMasterId);
        parcel.writeString(ShortName);
        parcel.writeString(CounterName);
        parcel.writeString(Description);
        parcel.writeString(CounterColor);
        parcel.writeString(ImageName);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(linktoDepartmentMasterId);
    }
}
