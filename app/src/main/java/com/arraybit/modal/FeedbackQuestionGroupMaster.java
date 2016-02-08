package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class FeedbackQuestionGroupMaster implements Parcelable {

    //region Properties

    short FeedbackQuestionGroupMasterId;
    short linktoBusinessMasterId;
    String GroupName;
    boolean IsDeleted;
    /// Extra
    String Business;
    public static final Parcelable.Creator<FeedbackQuestionGroupMaster> CREATOR = new Creator<FeedbackQuestionGroupMaster>() {
        public FeedbackQuestionGroupMaster createFromParcel(Parcel source) {
            FeedbackQuestionGroupMaster objFeedbackQuestionGroupMaster = new FeedbackQuestionGroupMaster();
            objFeedbackQuestionGroupMaster.FeedbackQuestionGroupMasterId = (short) source.readInt();
            objFeedbackQuestionGroupMaster.linktoBusinessMasterId = (short) source.readInt();
            objFeedbackQuestionGroupMaster.GroupName = source.readString();
            objFeedbackQuestionGroupMaster.IsDeleted = source.readByte() != 0;

            /// Extra
            objFeedbackQuestionGroupMaster.Business = source.readString();
            return objFeedbackQuestionGroupMaster;
        }

        public FeedbackQuestionGroupMaster[] newArray(int size) {
            return new FeedbackQuestionGroupMaster[size];
        }
    };
    short TotalNullGroupFeedbackQuestion;

    protected FeedbackQuestionGroupMaster(Parcel in) {
        GroupName = in.readString();
        IsDeleted = in.readByte() != 0;
        Business = in.readString();
    }

    public FeedbackQuestionGroupMaster() {

    }

    public short getFeedbackQuestionGroupMasterId() {
        return this.FeedbackQuestionGroupMasterId;
    }

    public void setFeedbackQuestionGroupMasterId(short feedbackQuestionGroupMasterId) {
        this.FeedbackQuestionGroupMasterId = feedbackQuestionGroupMasterId;
    }

    public short getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public String getGroupName() {
        return this.GroupName;
    }

    public void setGroupName(String groupName) {
        this.GroupName = groupName;
    }

    public boolean getIsDeleted() {
        return this.IsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.IsDeleted = isDeleted;
    }

    public String getBusiness() {
        return this.Business;
    }

    public void setBusiness(String business) {
        this.Business = business;
    }

    public short getTotalNullGroupFeedbackQuestion() {
        return TotalNullGroupFeedbackQuestion;
    }
    //endregion

    public void setTotalNullGroupFeedbackQuestion(short totalNullGroupFeedbackQuestion) {
        TotalNullGroupFeedbackQuestion = totalNullGroupFeedbackQuestion;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(FeedbackQuestionGroupMasterId);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeString(GroupName);
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));

        /// Extra
        parcel.writeString(Business);
    }

}
