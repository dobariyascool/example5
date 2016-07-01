package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class WaitingMaster implements Parcelable {

    //region Properties

    long WaitingMasterId;
    String PersonName;
    String PersonMobile;
    short NoOfPersons;
    short linktoWaitingStatusMasterId;
    short linktoBusinessMasterId;
    short linktoTableMasterId;

    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    /// Extra
    String WaitingStatus;
    public static final Parcelable.Creator<WaitingMaster> CREATOR = new Creator<WaitingMaster>() {
        public WaitingMaster createFromParcel(Parcel source) {
            WaitingMaster objWaitingMaster = new WaitingMaster();
            objWaitingMaster.WaitingMasterId = source.readLong();
            objWaitingMaster.PersonName = source.readString();
            objWaitingMaster.PersonMobile = source.readString();
            objWaitingMaster.NoOfPersons = (short)source.readInt();
            objWaitingMaster.linktoWaitingStatusMasterId = (short)source.readInt();
            objWaitingMaster.CreateDateTime = source.readString();
            objWaitingMaster.linktoUserMasterIdCreatedBy = (short)source.readInt();
            objWaitingMaster.linktoBusinessMasterId =(short) source.readInt();
            objWaitingMaster.linktoTableMasterId =(short) source.readInt();

            /// Extra
            objWaitingMaster.WaitingStatus = source.readString();
            return objWaitingMaster;
        }

        public WaitingMaster[] newArray(int size) {
            return new WaitingMaster[size];
        }
    };

    public long getWaitingMasterId() { return this.WaitingMasterId; }

    public void setWaitingMasterId(long waitingMasterId) { this.WaitingMasterId = waitingMasterId; }

    public String getPersonName() { return this.PersonName; }

    public void setPersonName(String personName) { this.PersonName = personName; }

    public String getPersonMobile() { return this.PersonMobile; }

    public void setPersonMobile(String personMobile) { this.PersonMobile = personMobile; }

    public short getNoOfPersons() { return this.NoOfPersons; }

    public void setNoOfPersons(short noOfPersons) { this.NoOfPersons = noOfPersons; }

    public short getlinktoWaitingStatusMasterId() { return this.linktoWaitingStatusMasterId; }

    public void setlinktoWaitingStatusMasterId(short linktoWaitingStatusMasterId) { this.linktoWaitingStatusMasterId = linktoWaitingStatusMasterId; }

    public String getCreateDateTime() { return this.CreateDateTime; }

    public void setCreateDateTime(String createDateTime) { this.CreateDateTime = createDateTime; }

    public short getlinktoUserMasterIdCreatedBy() { return this.linktoUserMasterIdCreatedBy; }

    public void setlinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) { this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy; }

    public String getWaitingStatus() { return this.WaitingStatus; }

    public void setWaitingStatus(String waitingStatus) { this.WaitingStatus = waitingStatus; }

    public short getlinktoBusinessMasterId() { return linktoBusinessMasterId; }


    public short getLinktoTableMasterId() {
        return linktoTableMasterId;
    }

    public void setLinktoTableMasterId(short linktoTableMasterId) {
        this.linktoTableMasterId = linktoTableMasterId;
    }


    //endregion

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeLong(WaitingMasterId);
        parcel.writeString(PersonName);
        parcel.writeString(PersonMobile);
        parcel.writeInt(NoOfPersons);
        parcel.writeInt(linktoWaitingStatusMasterId);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(linktoTableMasterId);

        /// Extra
        parcel.writeString(WaitingStatus);

    }

}
