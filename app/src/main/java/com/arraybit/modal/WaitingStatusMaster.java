package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class WaitingStatusMaster implements Parcelable {

    //region Properties
    short WaitingStatusMasterId;
    String WaitingStatus;
    String StatusColor;
    public static final Creator<WaitingStatusMaster> CREATOR = new Creator<WaitingStatusMaster>() {
        @Override
        public WaitingStatusMaster createFromParcel(Parcel source) {
            WaitingStatusMaster objWaitingStatusMaster = new WaitingStatusMaster();
            objWaitingStatusMaster.WaitingStatusMasterId = (short) source.readInt();
            objWaitingStatusMaster.WaitingStatus = source.readString();
            objWaitingStatusMaster.StatusColor = source.readString();
            return objWaitingStatusMaster;

        }

        @Override
        public WaitingStatusMaster[] newArray(int size) {
            return new WaitingStatusMaster[size];

        }
    };

    public short getWaitingStatusMasterId() {
        return this.WaitingStatusMasterId;
    }

    public void setWaitingStatusMasterId(short waitingStatusMasterId) {
        this.WaitingStatusMasterId = waitingStatusMasterId;
    }

    public String getWaitingStatus() {
        return this.WaitingStatus;
    }

    public void setWaitingStatus(String waitingStatus) {
        this.WaitingStatus = waitingStatus;
    }

    public String getStatusColor() {
        return this.StatusColor;
    }

    //endregion

    public void setStatusColor(String statusColor) {
        this.StatusColor = statusColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeInt(WaitingStatusMasterId);
        parcel.writeString(WaitingStatus);
        parcel.writeString(StatusColor);
    }
}
