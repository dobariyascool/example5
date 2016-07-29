package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItemTran implements Parcelable {

    //region Properties

    long OrderItemTranId;
    long linktoOrderMasterId;
    int linktoItemMasterId;
    short Quantity;
    double Rate;
    short ItemPoint;
    short DeductedPoint;
    String ItemRemark;
    Short linktoOrderStatusMasterId;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    /// Extra
    String Order;
    String Item;
    String OrderStatus;
    String OrderItemTranIds;
    String ModifierRates;
    short RateIndex;

    public static final Creator<OrderItemTran> CREATOR = new Creator<OrderItemTran>() {
        @Override
        public OrderItemTran createFromParcel(Parcel source) {
            OrderItemTran objOrderItemTran = new OrderItemTran();
            objOrderItemTran.OrderItemTranId = source.readLong();
            objOrderItemTran.linktoOrderMasterId = source.readLong();
            objOrderItemTran.linktoItemMasterId = source.readInt();
            objOrderItemTran.Quantity = (short) source.readInt();
            objOrderItemTran.Rate = source.readDouble();
            objOrderItemTran.ItemPoint = (short) source.readInt();
            objOrderItemTran.DeductedPoint = (short) source.readInt();
            objOrderItemTran.ItemRemark = source.readString();
            objOrderItemTran.linktoOrderStatusMasterId = (short) source.readInt();
            objOrderItemTran.UpdateDateTime = source.readString();
            objOrderItemTran.linktoUserMasterIdUpdatedBy = (short) source.readInt();

            /// Extra
            objOrderItemTran.Order = source.readString();
            objOrderItemTran.Item = source.readString();
            objOrderItemTran.OrderStatus = source.readString();
            objOrderItemTran.OrderItemTranIds = source.readString();
            objOrderItemTran.ModifierRates = source.readString();
            objOrderItemTran.RateIndex = (short) source.readInt();

            return objOrderItemTran;

        }

        @Override
        public OrderItemTran[] newArray(int size) {
            return new OrderItemTran[size];
        }
    };

    public long getOrderItemTranId() {
        return this.OrderItemTranId;
    }

    public void setOrderItemTranId(long orderItemTranId) {
        this.OrderItemTranId = orderItemTranId;
    }

    public long getlinktoOrderMasterId() {
        return this.linktoOrderMasterId;
    }

    public void setlinktoOrderMasterId(long linktoOrderMasterId) {
        this.linktoOrderMasterId = linktoOrderMasterId;
    }

    public int getlinktoItemMasterId() {
        return this.linktoItemMasterId;
    }

    public void setlinktoItemMasterId(int linktoItemMasterId) {
        this.linktoItemMasterId = linktoItemMasterId;
    }

    public short getQuantity() {
        return this.Quantity;
    }

    public void setQuantity(short quantity) {
        this.Quantity = quantity;
    }

    public double getRate() {
        return this.Rate;
    }

    public void setRate(double rate) {
        this.Rate = rate;
    }

    public short getItemPoint() {
        return this.ItemPoint;
    }

    public void setItemPoint(short itemPoint) {
        this.ItemPoint = itemPoint;
    }

    public short getDeductedPoint() {
        return this.DeductedPoint;
    }

    public void setDeductedPoint(short deductedPoint) {
        this.DeductedPoint = deductedPoint;
    }

    public String getItemRemark() {
        return this.ItemRemark;
    }

    public void setItemRemark(String itemRemark) {
        this.ItemRemark = itemRemark;
    }

    public Short getlinktoOrderStatusMasterId() {
        return this.linktoOrderStatusMasterId;
    }

    public void setlinktoOrderStatusMasterId(Short linktoOrderStatusMasterId) {
        this.linktoOrderStatusMasterId = linktoOrderStatusMasterId;
    }

    public String getUpdateDateTime() {
        return this.UpdateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.UpdateDateTime = updateDateTime;
    }

    public short getlinktoUserMasterIdUpdatedBy() {
        return this.linktoUserMasterIdUpdatedBy;
    }

    public void setlinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) {
        this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy;
    }

    public String getOrder() {
        return this.Order;
    }

    public void setOrder(String order) {
        this.Order = order;
    }

    public String getItem() {
        return this.Item;
    }

    public void setItem(String item) {
        this.Item = item;
    }

    public String getOrderStatus() {
        return this.OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.OrderStatus = orderStatus;
    }

    public String getOrderItemTranIds() {
        return OrderItemTranIds;
    }

    public void setOrderItemTranIds(String orderItemTranIds) {
        OrderItemTranIds = orderItemTranIds;
    }

    public String getModifierRates() {
        return ModifierRates;
    }

    public void setModifierRates(String modifierRates) {
        ModifierRates = modifierRates;
    }

    public short getRateIndex() {
        return RateIndex;
    }

    public void setRateIndex(short rateIndex) {
        RateIndex = rateIndex;
    }

    //endregion


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeLong(OrderItemTranId);
        parcel.writeLong(linktoOrderMasterId);
        parcel.writeInt(linktoItemMasterId);
        parcel.writeInt(Quantity);
        parcel.writeDouble(Rate);
        parcel.writeInt(ItemPoint);
        parcel.writeInt(DeductedPoint);
        parcel.writeString(ItemRemark);
        parcel.writeInt(linktoOrderStatusMasterId);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);

        /// Extra
        parcel.writeString(Order);
        parcel.writeString(Item);
        parcel.writeString(OrderStatus);
        parcel.writeString(OrderItemTranIds);
        parcel.writeString(ModifierRates);
        parcel.writeInt(RateIndex);

    }
}
