package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for FeedbackMaster
/// </summary>
public class FeedbackMaster implements Parcelable {
    //region Properties

    int FeedbackMasterId;
    String Name;
    String Email;
    String Phone;
    String Feedback;
    String FeedbackDateTime;
    short linktoFeedbackTypeMasterId;
    int linktoCustomerMasterId;
    short linktoBusinessMasterId;
    /// Extra
    String FeedbackType;
    String Business;
    public static final Parcelable.Creator<FeedbackMaster> CREATOR = new Creator<FeedbackMaster>() {
        public FeedbackMaster createFromParcel(Parcel source) {
            FeedbackMaster objFeedbackMaster = new FeedbackMaster();
            objFeedbackMaster.FeedbackMasterId = source.readInt();
            objFeedbackMaster.Name = source.readString();
            objFeedbackMaster.Email = source.readString();
            objFeedbackMaster.Phone = source.readString();
            objFeedbackMaster.Feedback = source.readString();
            objFeedbackMaster.FeedbackDateTime = source.readString();
            objFeedbackMaster.linktoFeedbackTypeMasterId = (short) source.readInt();
            objFeedbackMaster.linktoCustomerMasterId = source.readInt();
            objFeedbackMaster.linktoBusinessMasterId = (short) source.readInt();

            /// Extra
            objFeedbackMaster.FeedbackType = source.readString();
            objFeedbackMaster.Business = source.readString();
            return objFeedbackMaster;
        }

        public FeedbackMaster[] newArray(int size) {
            return new FeedbackMaster[size];
        }
    };
    int FeedbackRowPosition;

    public int getFeedbackMasterId() {
        return this.FeedbackMasterId;
    }

    public void setFeedbackMasterId(int feedbackMasterId) {
        this.FeedbackMasterId = feedbackMasterId;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPhone() {
        return this.Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getFeedback() {
        return this.Feedback;
    }

    public void setFeedback(String feedback) {
        this.Feedback = feedback;
    }

    public String getFeedbackDateTime() {
        return this.FeedbackDateTime;
    }

    public void setFeedbackDateTime(String feedbackDateTime) {
        this.FeedbackDateTime = feedbackDateTime;
    }

    public short getlinktoFeedbackTypeMasterId() {
        return this.linktoFeedbackTypeMasterId;
    }

    public void setlinktoFeedbackTypeMasterId(short linktoFeedbackTypeMasterId) {
        this.linktoFeedbackTypeMasterId = linktoFeedbackTypeMasterId;
    }

    public int getlinktoCustomerMasterId() {
        return this.linktoCustomerMasterId;
    }

    public void setlinktoCustomerMasterId(int linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public short getlinktoBusinesseMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public String getFeedbackType() {
        return this.FeedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.FeedbackType = feedbackType;
    }

    public String getBusiness() {
        return this.Business;
    }

    public void setBusiness(String business) {
        this.Business = business;
    }

    public int getFeedbackRowPosition() {
        return FeedbackRowPosition;
    }

    //endregion

    public void setFeedbackRowPosition(int feedbackRowPosition) {
        FeedbackRowPosition = feedbackRowPosition;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(FeedbackMasterId);
        parcel.writeString(Name);
        parcel.writeString(Email);
        parcel.writeString(Phone);
        parcel.writeString(Feedback);
        parcel.writeString(FeedbackDateTime);
        parcel.writeInt(linktoFeedbackTypeMasterId);
        parcel.writeInt(linktoCustomerMasterId);
        parcel.writeInt(linktoBusinessMasterId);

        /// Extra
        parcel.writeString(FeedbackType);
        parcel.writeString(Business);
    }
}
