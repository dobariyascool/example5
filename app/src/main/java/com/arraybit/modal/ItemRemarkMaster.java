package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemRemarkMaster implements Parcelable {

    //region Properties

    short ItemRemarkMasterId;
    String ItemRemark;
    short linktoBusinessMasterId;
    /// Extra
    String Business;
    public static final Creator<ItemRemarkMaster> CREATOR = new Creator<ItemRemarkMaster>() {

        @Override
        public ItemRemarkMaster createFromParcel(Parcel source) {
            ItemRemarkMaster objItemRemarkMaster = new ItemRemarkMaster();
            objItemRemarkMaster.ItemRemarkMasterId = (short)source.readInt();
            objItemRemarkMaster.ItemRemark = source.readString();
            objItemRemarkMaster.linktoBusinessMasterId = (short)source.readInt();

            /// Extra
            objItemRemarkMaster.Business = source.readString();
            return objItemRemarkMaster;

        }

        @Override
        public ItemRemarkMaster[] newArray(int size) {
            return new ItemRemarkMaster[size];
        }
    };

    public short getItemRemarkMasterId() { return this.ItemRemarkMasterId; }

    public void setItemRemarkMasterId(short itemRemarkMasterId) { this.ItemRemarkMasterId = itemRemarkMasterId; }

    public String getItemRemark() { return this.ItemRemark; }

    public void setItemRemark(String itemRemark) { this.ItemRemark = itemRemark; }

    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public String getBusiness() { return this.Business; }

    //endregion

    public void setBusiness(String business) { this.Business = business; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeInt(ItemRemarkMasterId);
        parcel.writeString(ItemRemark);
        parcel.writeInt(linktoBusinessMasterId);

        /// Extra
        parcel.writeString(Business);


    }

}
