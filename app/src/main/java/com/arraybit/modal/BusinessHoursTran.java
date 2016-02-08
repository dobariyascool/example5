package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for BusinessHoursTran
/// </summary>
public class BusinessHoursTran implements Parcelable {
    //region Properties

    short BusinessHoursTranId;
    short DayOfWeek;
    String OpeningTime;
    String ClosingTime;
    String BreakStartTime;
    String BreakEndTime;
    short linktoBusinessMasterId;

    public static final Parcelable.Creator<BusinessHoursTran> CREATOR = new Creator<BusinessHoursTran>() {
        public BusinessHoursTran createFromParcel(Parcel source) {
            BusinessHoursTran objBusinessHoursTran = new BusinessHoursTran();
            objBusinessHoursTran.BusinessHoursTranId = (short) source.readInt();
            objBusinessHoursTran.DayOfWeek = (short) source.readInt();
            objBusinessHoursTran.OpeningTime = source.readString();
            objBusinessHoursTran.ClosingTime = source.readString();
            objBusinessHoursTran.BreakStartTime = source.readString();
            objBusinessHoursTran.BreakEndTime = source.readString();
            objBusinessHoursTran.linktoBusinessMasterId = (short) source.readInt();
            return objBusinessHoursTran;
        }

        public BusinessHoursTran[] newArray(int size) {
            return new BusinessHoursTran[size];
        }
    };

    public short getBusinessHoursTranId() {
        return this.BusinessHoursTranId;
    }

    public void setBusinessHoursTranId(short businessHoursTranId) {
        this.BusinessHoursTranId = businessHoursTranId;
    }

    public short getDayOfWeek() {
        return this.DayOfWeek;
    }

    public void setDayOfWeek(short dayOfWeek) {
        this.DayOfWeek = dayOfWeek;
    }

    public String getOpeningTime() {
        return this.OpeningTime;
    }

    public void setOpeningTime(String openingTime) {
        this.OpeningTime = openingTime;
    }

    public String getClosingTime() {
        return this.ClosingTime;
    }

    public void setClosingTime(String closingTime) {
        this.ClosingTime = closingTime;
    }

    public short getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public String getBreakStartTime() {
        return BreakStartTime;
    }

    public void setBreakStartTime(String breakStartTime) {
        BreakStartTime = breakStartTime;
    }

    public String getBreakEndTime() {
        return BreakEndTime;
    }

    public void setBreakEndTime(String breakEndTime) {
        BreakEndTime = breakEndTime;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }


    //endregion


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(BusinessHoursTranId);
        parcel.writeInt(DayOfWeek);
        parcel.writeString(OpeningTime);
        parcel.writeString(ClosingTime);
        parcel.writeString(BreakStartTime);
        parcel.writeString(BreakEndTime);
        parcel.writeInt(linktoBusinessMasterId);
    }
}
