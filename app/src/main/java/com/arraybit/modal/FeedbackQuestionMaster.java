package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class FeedbackQuestionMaster implements Parcelable {
    //region Properties

    int FeedbackQuestionMasterId;
    short linktoBusinessMasterId;
    String FeedbackQuestion;
    short QuestionType;
    int SortOrder;
    boolean IsEnabled;
    boolean IsDeleted;
    short linktoFeedbackQuestionGroupMasterId;
    /// Extra
    String Business;
    String GroupName;
    public static final Parcelable.Creator<FeedbackQuestionMaster> CREATOR = new Creator<FeedbackQuestionMaster>() {
        public FeedbackQuestionMaster createFromParcel(Parcel source) {
            FeedbackQuestionMaster objFeedbackQuestionMaster = new FeedbackQuestionMaster();
            objFeedbackQuestionMaster.FeedbackQuestionMasterId = source.readInt();
            objFeedbackQuestionMaster.linktoBusinessMasterId = (short) source.readInt();
            objFeedbackQuestionMaster.FeedbackQuestion = source.readString();
            objFeedbackQuestionMaster.QuestionType = (short) source.readInt();
            objFeedbackQuestionMaster.SortOrder = source.readInt();
            objFeedbackQuestionMaster.IsEnabled = source.readByte() != 0;
            objFeedbackQuestionMaster.IsDeleted = source.readByte() != 0;
            objFeedbackQuestionMaster.linktoFeedbackQuestionGroupMasterId = (short) source.readInt();

            /// Extra
            objFeedbackQuestionMaster.Business = source.readString();
            objFeedbackQuestionMaster.GroupName = source.readString();
            return objFeedbackQuestionMaster;
        }

        public FeedbackQuestionMaster[] newArray(int size) {
            return new FeedbackQuestionMaster[size];
        }
    };

    public short getLinktoFeedbackQuestionGroupMasterId() {
        return linktoFeedbackQuestionGroupMasterId;
    }

    public void setLinktoFeedbackQuestionGroupMasterId(short linktoFeedbackQuestionGroupMasterId) {
        this.linktoFeedbackQuestionGroupMasterId = linktoFeedbackQuestionGroupMasterId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public int getFeedbackQuestionMasterId() {
        return this.FeedbackQuestionMasterId;
    }

    public void setFeedbackQuestionMasterId(int feedbackQuestionMasterId) {
        this.FeedbackQuestionMasterId = feedbackQuestionMasterId;
    }

    public short getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public String getFeedbackQuestion() {
        return this.FeedbackQuestion;
    }

    public void setFeedbackQuestion(String feedbackQuestion) {
        this.FeedbackQuestion = feedbackQuestion;
    }

    public short getQuestionType() {
        return this.QuestionType;
    }

    public void setQuestionType(short questionType) {
        this.QuestionType = questionType;
    }

    public int getSortOrder() {
        return this.SortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.SortOrder = sortOrder;
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

    public String getBusiness() {
        return this.Business;
    }

    //endregion

    public void setBusiness(String business) {
        this.Business = business;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(FeedbackQuestionMasterId);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeString(FeedbackQuestion);
        parcel.writeInt(QuestionType);
        parcel.writeInt(SortOrder);
        parcel.writeByte((byte) (IsEnabled ? 1 : 0));
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));
        parcel.writeInt(linktoFeedbackQuestionGroupMasterId);

        /// Extra
        parcel.writeString(Business);
        parcel.writeString(GroupName);
    }
}
