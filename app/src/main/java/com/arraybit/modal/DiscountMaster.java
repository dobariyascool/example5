package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class DiscountMaster implements Parcelable {
    //region Properties

    short DiscountMasterId;
    double Discount;
    boolean IsPercentage;
    String DiscountTitle;
    short linktoBusinessMasterId;
    /// Extra
    String Business;
    public static final Parcelable.Creator<DiscountMaster> CREATOR = new Creator<DiscountMaster>() {
        public DiscountMaster createFromParcel(Parcel source) {
            DiscountMaster objDiscountMaster = new DiscountMaster();
            objDiscountMaster.DiscountMasterId = (short)source.readInt();
            objDiscountMaster.Discount = source.readDouble();
            objDiscountMaster.IsPercentage = source.readByte() != 0;
            objDiscountMaster.DiscountTitle = source.readString();
            objDiscountMaster.linktoBusinessMasterId = (short)source.readInt();

            /// Extra
            objDiscountMaster.Business = source.readString();
            return objDiscountMaster;
        }

        public DiscountMaster[] newArray(int size) {
            return new DiscountMaster[size];
        }
    };

    public short getDiscountMasterId() { return this.DiscountMasterId; }

    public void setDiscountMasterId(short discountMasterId) { this.DiscountMasterId = discountMasterId; }

    public double getDiscount() { return this.Discount; }

    public void setDiscount(double discount) { this.Discount = discount; }

    public boolean getIsPercentage() { return this.IsPercentage; }

    public void setIsPercentage(boolean isPercentage) { this.IsPercentage = isPercentage; }

    public String getDiscountTitle() { return this.DiscountTitle; }

    public void setDiscountTitle(String discountTitle) { this.DiscountTitle = discountTitle; }

    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public String getBusiness() { return this.Business; }

    //endregion

    public void setBusiness(String business) { this.Business = business; }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(DiscountMasterId);
        parcel.writeDouble(Discount);
        parcel.writeByte((byte)(IsPercentage ? 1 : 0));
        parcel.writeString(DiscountTitle);
        parcel.writeInt(linktoBusinessMasterId);

        /// Extra
        parcel.writeString(Business);
    }

}
