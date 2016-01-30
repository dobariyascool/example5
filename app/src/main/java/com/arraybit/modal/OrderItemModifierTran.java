package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItemModifierTran implements Parcelable {
    //region Properties

    long OrderItemModifierTranId;
    long linktoOrderItemTranId;
    int linktoModifierMasterId;
    double Rate;
    //endregion
    public static final Creator<OrderItemModifierTran> CREATOR = new Creator<OrderItemModifierTran>() {
        @Override
        public OrderItemModifierTran createFromParcel(Parcel source) {
            OrderItemModifierTran objOrderItemModifierTran = new OrderItemModifierTran();
            objOrderItemModifierTran.OrderItemModifierTranId = source.readLong();
            objOrderItemModifierTran.linktoOrderItemTranId = source.readLong();
            objOrderItemModifierTran.linktoModifierMasterId = source.readInt();
            objOrderItemModifierTran.Rate = source.readDouble();
            return objOrderItemModifierTran;

        }

        @Override
        public OrderItemModifierTran[] newArray(int size) {
            return new OrderItemModifierTran[size];
        }
    };

    public long getOrderItemModifierTranId() { return this.OrderItemModifierTranId; }

    public void setOrderItemModifierTranId(long orderItemModifierTranId) { this.OrderItemModifierTranId = orderItemModifierTranId; }

    public long getlinktoOrderItemTranId() { return this.linktoOrderItemTranId; }

    public void setlinktoOrderItemTranId(long linktoOrderItemTranId) { this.linktoOrderItemTranId = linktoOrderItemTranId; }

    public int getlinktoModifierMasterId() { return this.linktoModifierMasterId; }

    public void setlinktoModifierMasterId(int linktoModifierMasterId) { this.linktoModifierMasterId = linktoModifierMasterId; }

    public double getRate() { return this.Rate; }

    public void setRate(double rate) { this.Rate = rate; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(OrderItemModifierTranId);
        parcel.writeLong(linktoOrderItemTranId);
        parcel.writeInt(linktoModifierMasterId);
        parcel.writeDouble(Rate);

    }
}
