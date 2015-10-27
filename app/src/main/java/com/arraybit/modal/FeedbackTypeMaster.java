package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for FeedbackTypeMaster
/// </summary>
public class FeedbackTypeMaster implements Parcelable {
    //region Properties

    short FeedbackTypeMasterId;
    String FeedbackType;
    public static final Parcelable.Creator<FeedbackTypeMaster> CREATOR = new Creator<FeedbackTypeMaster>() {
        public FeedbackTypeMaster createFromParcel(Parcel source) {
            FeedbackTypeMaster objFeedbackTypeMaster = new FeedbackTypeMaster();
            objFeedbackTypeMaster.FeedbackTypeMasterId = (short)source.readInt();
            objFeedbackTypeMaster.FeedbackType = source.readString();
            return objFeedbackTypeMaster;
        }

        public FeedbackTypeMaster[] newArray(int size) {
            return new FeedbackTypeMaster[size];
        }
    };

    public short getFeedbackTypeMasterId() { return this.FeedbackTypeMasterId; }

    public void setFeedbackTypeMasterId(short feedbackTypeMasterId) { this.FeedbackTypeMasterId = feedbackTypeMasterId; }

    public String getFeedbackType() { return this.FeedbackType; }

    //endregion

    public void setFeedbackType(String feedbackType) { this.FeedbackType = feedbackType; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(FeedbackTypeMasterId);
        parcel.writeString(FeedbackType);
    }
}
