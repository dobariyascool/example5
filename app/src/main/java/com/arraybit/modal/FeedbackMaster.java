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
    int linktoRegisteredUserMasterId;
    String ReplyDateTime;
    String Reply;
    short linktoUserMasterIdRepliedBy;
    short linktoBusinessTypeMasterId;
    boolean IsDeleted;
    /// Extra
    String FeedbackType;
    String BusinessType;
    public static final Parcelable.Creator<FeedbackMaster> CREATOR = new Creator<FeedbackMaster>() {
        public FeedbackMaster createFromParcel(Parcel source) {
            FeedbackMaster objFeedbackMaster = new FeedbackMaster();
            objFeedbackMaster.FeedbackMasterId = source.readInt();
            objFeedbackMaster.Name = source.readString();
            objFeedbackMaster.Email = source.readString();
            objFeedbackMaster.Phone = source.readString();
            objFeedbackMaster.Feedback = source.readString();
            objFeedbackMaster.FeedbackDateTime = source.readString();
            objFeedbackMaster.linktoFeedbackTypeMasterId = (short)source.readInt();
            objFeedbackMaster.linktoRegisteredUserMasterId = source.readInt();
            objFeedbackMaster.ReplyDateTime = source.readString();
            objFeedbackMaster.Reply = source.readString();
            objFeedbackMaster.linktoUserMasterIdRepliedBy = (short)source.readInt();
            objFeedbackMaster.linktoBusinessTypeMasterId = (short)source.readInt();
            objFeedbackMaster.IsDeleted = source.readByte() != 0;

            /// Extra
            objFeedbackMaster.FeedbackType = source.readString();
            objFeedbackMaster.BusinessType = source.readString();
            return objFeedbackMaster;
        }

        public FeedbackMaster[] newArray(int size) {
            return new FeedbackMaster[size];
        }
    };

    public int getFeedbackMasterId() { return this.FeedbackMasterId; }

    public void setFeedbackMasterId(int feedbackMasterId) { this.FeedbackMasterId = feedbackMasterId; }

    public String getName() { return this.Name; }

    public void setName(String name) { this.Name = name; }

    public String getEmail() { return this.Email; }

    public void setEmail(String email) { this.Email = email; }

    public String getPhone() { return this.Phone; }

    public void setPhone(String phone) { this.Phone = phone; }

    public String getFeedback() { return this.Feedback; }

    public void setFeedback(String feedback) { this.Feedback = feedback; }

    public String getFeedbackDateTime() { return this.FeedbackDateTime; }

    public void setFeedbackDateTime(String feedbackDateTime) { this.FeedbackDateTime = feedbackDateTime; }

    public short getlinktoFeedbackTypeMasterId() { return this.linktoFeedbackTypeMasterId; }

    public void setlinktoFeedbackTypeMasterId(short linktoFeedbackTypeMasterId) { this.linktoFeedbackTypeMasterId = linktoFeedbackTypeMasterId; }

    public int getlinktoRegisteredUserMasterId() { return this.linktoRegisteredUserMasterId; }

    public void setlinktoRegisteredUserMasterId(int linktoRegisteredUserMasterId) { this.linktoRegisteredUserMasterId = linktoRegisteredUserMasterId; }

    public String getReplyDateTime() { return this.ReplyDateTime; }

    public void setReplyDateTime(String replyDateTime) { this.ReplyDateTime = replyDateTime; }

    public String getReply() { return this.Reply; }

    public void setReply(String reply) { this.Reply = reply; }

    public short getlinktoUserMasterIdRepliedBy() { return this.linktoUserMasterIdRepliedBy; }

    public void setlinktoUserMasterIdRepliedBy(short linktoUserMasterIdRepliedBy) { this.linktoUserMasterIdRepliedBy = linktoUserMasterIdRepliedBy; }

    public short getlinktoBusinessTypeMasterId() { return this.linktoBusinessTypeMasterId; }

    public void setlinktoBusinessTypeMasterId(short linktoBusinessTypeMasterId) { this.linktoBusinessTypeMasterId = linktoBusinessTypeMasterId; }

    public boolean getIsDeleted() { return this.IsDeleted; }

    public void setIsDeleted(boolean isDeleted) { this.IsDeleted = isDeleted; }

    public String getFeedbackType() { return this.FeedbackType; }

    public void setFeedbackType(String feedbackType) { this.FeedbackType = feedbackType; }

    public String getBusinessType() { return this.BusinessType; }

    //endregion

    public void setBusinessType(String businessType) { this.BusinessType = businessType; }

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
        parcel.writeInt(linktoRegisteredUserMasterId);
        parcel.writeString(ReplyDateTime);
        parcel.writeString(Reply);
        parcel.writeInt(linktoUserMasterIdRepliedBy);
        parcel.writeInt(linktoBusinessTypeMasterId);
        parcel.writeByte((byte)(IsDeleted ? 1 : 0));

        /// Extra
        parcel.writeString(FeedbackType);
        parcel.writeString(BusinessType);
    }
}
