package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class SectionMaster implements Parcelable {

    //region Properties

    short SectionMasterId;
    String SectionName;
    String Description;
    short linktoBusinessMasterId;
    boolean IsEnabled;
    /// Extra
    String Business;
    public static final Parcelable.Creator<SectionMaster> CREATOR = new Creator<SectionMaster>() {
        public SectionMaster createFromParcel(Parcel source) {
            SectionMaster objSectionMaster = new SectionMaster();
            objSectionMaster.SectionMasterId = (short)source.readInt();
            objSectionMaster.SectionName = source.readString();
            objSectionMaster.Description = source.readString();
            objSectionMaster.linktoBusinessMasterId = (short)source.readInt();
            objSectionMaster.IsEnabled = source.readByte() != 0;

            /// Extra
            objSectionMaster.Business = source.readString();
            return objSectionMaster;
        }

        public SectionMaster[] newArray(int size) {
            return new SectionMaster[size];
        }
    };

    public short getSectionMasterId() { return this.SectionMasterId; }

    public void setSectionMasterId(short sectionMasterId) { this.SectionMasterId = sectionMasterId; }

    public String getSectionName() { return this.SectionName; }

    public void setSectionName(String sectionName) { this.SectionName = sectionName; }

    public String getDescription() { return this.Description; }

    public void setDescription(String description) { this.Description = description; }

    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public boolean getIsEnabled() { return this.IsEnabled; }

    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }

    public String getBusiness() { return this.Business; }

    //endregion

    public void setBusiness(String business) { this.Business = business; }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeInt(SectionMasterId);
        parcel.writeString(SectionName);
        parcel.writeString(Description);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));

        /// Extra
        parcel.writeString(Business);
    }

}

