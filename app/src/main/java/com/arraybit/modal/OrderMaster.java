package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OrderMaster implements Parcelable {

    //region Properties

    long OrderMasterId;
    String OrderNumber;
    String OrderDateTime;
    short linktoCounterMasterId;
    String linktoTableMasterIds;
    int linktoWaiterMasterId;
    short linktoCustomerMasterId;
    short linktoOrderTypeMasterId;
    Short linktoOrderStatusMasterId;
    short linktoBusinessMasterId;
    double TotalAmount;
    double TotalTax;
    double Discount;
    double DiscountPercentage;
    double ExtraAmount;
    double NetAmount;
    short TotalItemPoint;
    short TotalDeductedPoint;
    String Remark;
    long linktoSalesMasterId;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    String OrderTime;
    short PrintCount;
    short linktoSourceMasterId;
    /// Extra
    String Counter;
    String Waiter;
    String Customer;
    String OrderType;
    String TableName;
    int TotalItem;
    int TotalKOT;
    public static final Parcelable.Creator<OrderMaster> CREATOR = new Creator<OrderMaster>() {
        public OrderMaster createFromParcel(Parcel source) {
            OrderMaster objOrderMaster = new OrderMaster();
            objOrderMaster.OrderMasterId = source.readLong();
            objOrderMaster.OrderNumber = source.readString();
            objOrderMaster.OrderDateTime = source.readString();
            objOrderMaster.linktoCounterMasterId = (short) source.readInt();
            objOrderMaster.linktoTableMasterIds = source.readString();
            objOrderMaster.linktoWaiterMasterId = source.readInt();
            objOrderMaster.linktoCustomerMasterId = (short) source.readInt();
            objOrderMaster.linktoOrderTypeMasterId = (short) source.readInt();
            objOrderMaster.linktoOrderStatusMasterId = (short) source.readInt();
            objOrderMaster.linktoBusinessMasterId = (short) source.readInt();
            objOrderMaster.TotalAmount = source.readDouble();
            objOrderMaster.TotalTax = source.readDouble();
            objOrderMaster.Discount = source.readDouble();
            objOrderMaster.DiscountPercentage = source.readDouble();
            objOrderMaster.ExtraAmount = source.readDouble();
            objOrderMaster.TotalItemPoint = (short) source.readInt();
            objOrderMaster.TotalDeductedPoint = (short) source.readInt();
            objOrderMaster.Remark = source.readString();
            objOrderMaster.linktoSalesMasterId = source.readLong();
            objOrderMaster.CreateDateTime = source.readString();
            objOrderMaster.linktoUserMasterIdCreatedBy = (short) source.readInt();
            objOrderMaster.UpdateDateTime = source.readString();
            objOrderMaster.linktoUserMasterIdUpdatedBy = (short) source.readInt();
            objOrderMaster.PrintCount = (short) source.readInt();
            objOrderMaster.linktoSourceMasterId= (short) source.readInt();

            /// Extra
            objOrderMaster.Counter = source.readString();
            objOrderMaster.Waiter = source.readString();
            objOrderMaster.Customer = source.readString();
            objOrderMaster.OrderType = source.readString();
            objOrderMaster.TableName = source.readString();
            objOrderMaster.OrderTime = source.readString();
            objOrderMaster.TotalItem = source.readInt();
            objOrderMaster.TotalKOT = source.readInt();

            return objOrderMaster;
        }

        @Override
        public OrderMaster[] newArray(int size) {
            return new OrderMaster[size];
        }
    };
    short RateIndex;
    ArrayList<ItemMaster> AlOrderItemTran;

    public long getOrderMasterId() {
        return this.OrderMasterId;
    }

    public void setOrderMasterId(long orderMasterId) {
        this.OrderMasterId = orderMasterId;
    }

    public String getOrderNumber() {
        return this.OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.OrderNumber = orderNumber;
    }

    public String getOrderDateTime() {
        return this.OrderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.OrderDateTime = orderDateTime;
    }

    public short getlinktoCounterMasterId() {
        return this.linktoCounterMasterId;
    }

    public void setlinktoCounterMasterId(short linktoCounterMasterId) {
        this.linktoCounterMasterId = linktoCounterMasterId;
    }

    public String getlinktoTableMasterIds() {
        return this.linktoTableMasterIds;
    }

    public void setlinktoTableMasterIds(String linktoTableMasterIds) {
        this.linktoTableMasterIds = linktoTableMasterIds;
    }

    public int getlinktoWaiterMasterId() {
        return this.linktoWaiterMasterId;
    }

    public void setlinktoWaiterMasterId(int linktoWaiterMasterId) {
        this.linktoWaiterMasterId = linktoWaiterMasterId;
    }

    public short getlinktoCustomerMasterId() {
        return this.linktoCustomerMasterId;
    }

    public void setlinktoCustomerMasterId(short linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public short getlinktoOrderTypeMasterId() {
        return this.linktoOrderTypeMasterId;
    }

    public void setlinktoOrderTypeMasterId(short linktoOrderTypeMasterId) {
        this.linktoOrderTypeMasterId = linktoOrderTypeMasterId;
    }

    public Short getlinktoOrderStatusMasterId() {
        return this.linktoOrderStatusMasterId;
    }

    public void setlinktoOrderStatusMasterId(Short linktoOrderStatusMasterId) {
        this.linktoOrderStatusMasterId = linktoOrderStatusMasterId;
    }

    public double getTotalAmount() {
        return this.TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.TotalAmount = totalAmount;
    }

    public double getTotalTax() {
        return this.TotalTax;
    }

    public void setTotalTax(double totalTax) {
        this.TotalTax = totalTax;
    }

    public double getDiscount() {
        return this.Discount;
    }

    public void setDiscount(double discount) {
        this.Discount = discount;
    }

    public double getExtraAmount() {
        return this.ExtraAmount;
    }

    public void setExtraAmount(double extraAmount) {
        this.ExtraAmount = extraAmount;
    }

    public short getTotalItemPoint() {
        return this.TotalItemPoint;
    }

    public void setTotalItemPoint(short totalItemPoint) {
        this.TotalItemPoint = totalItemPoint;
    }

    public short getTotalDeductedPoint() {
        return this.TotalDeductedPoint;
    }

    public void setTotalDeductedPoint(short totalDeductedPoint) {
        this.TotalDeductedPoint = totalDeductedPoint;
    }

    public String getRemark() {
        return this.Remark;
    }

    public void setRemark(String remark) {
        this.Remark = remark;
    }

    public long getlinktoSalesMasterId() {
        return this.linktoSalesMasterId;
    }

    public void setlinktoSalesMasterId(long linktoSalesMasterId) {
        this.linktoSalesMasterId = linktoSalesMasterId;
    }

    public String getCreateDateTime() {
        return this.CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.CreateDateTime = createDateTime;
    }

    public short getlinktoUserMasterIdCreatedBy() {
        return this.linktoUserMasterIdCreatedBy;
    }

    public void setlinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) {
        this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy;
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

    public String getCounter() {
        return this.Counter;
    }

    public void setCounter(String counter) {
        this.Counter = counter;
    }

    public String getWaiter() {
        return this.Waiter;
    }

    public void setWaiter(String waiter) {
        this.Waiter = waiter;
    }

    public String getCustomer() {
        return this.Customer;
    }

    public void setCustomer(String customer) {
        this.Customer = customer;
    }

    public String getOrderType() {
        return this.OrderType;
    }

    public void setOrderType(String orderType) {
        this.OrderType = orderType;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public int getTotalItem() {
        return TotalItem;
    }

    public void setTotalItem(int totalItem) {
        TotalItem = totalItem;
    }

    public ArrayList<ItemMaster> getAlOrderItemTran() {
        return AlOrderItemTran;
    }

    public void setAlOrderItemTran(ArrayList<ItemMaster> alOrderItemTran) {
        AlOrderItemTran = alOrderItemTran;
    }

    public int getTotalKOT() {
        return TotalKOT;
    }

    public void setTotalKOT(int totalKOT) {
        TotalKOT = totalKOT;
    }

    public short getLinktoBusinessMasterId() {
        return linktoBusinessMasterId;
    }

    public void setLinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public short getRateIndex() {
        return RateIndex;
    }

    public void setRateIndex(short rateIndex) {
        RateIndex = rateIndex;
    }


    public double getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(double netAmount) {
        NetAmount = netAmount;
    }

    public double getDiscountPercentage() {
        return DiscountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        DiscountPercentage = discountPercentage;
    }

    public short getLinktoCounterMasterId() {
        return linktoCounterMasterId;
    }

    public void setLinktoCounterMasterId(short linktoCounterMasterId) {
        this.linktoCounterMasterId = linktoCounterMasterId;
    }

    public String getLinktoTableMasterIds() {
        return linktoTableMasterIds;
    }

    public void setLinktoTableMasterIds(String linktoTableMasterIds) {
        this.linktoTableMasterIds = linktoTableMasterIds;
    }

    public int getLinktoWaiterMasterId() {
        return linktoWaiterMasterId;
    }

    public void setLinktoWaiterMasterId(int linktoWaiterMasterId) {
        this.linktoWaiterMasterId = linktoWaiterMasterId;
    }

    public short getLinktoCustomerMasterId() {
        return linktoCustomerMasterId;
    }

    public void setLinktoCustomerMasterId(short linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public short getLinktoOrderTypeMasterId() {
        return linktoOrderTypeMasterId;
    }

    public void setLinktoOrderTypeMasterId(short linktoOrderTypeMasterId) {
        this.linktoOrderTypeMasterId = linktoOrderTypeMasterId;
    }

    public Short getLinktoOrderStatusMasterId() {
        return linktoOrderStatusMasterId;
    }

    public void setLinktoOrderStatusMasterId(Short linktoOrderStatusMasterId) {
        this.linktoOrderStatusMasterId = linktoOrderStatusMasterId;
    }

    public long getLinktoSalesMasterId() {
        return linktoSalesMasterId;
    }

    public void setLinktoSalesMasterId(long linktoSalesMasterId) {
        this.linktoSalesMasterId = linktoSalesMasterId;
    }

    public short getLinktoUserMasterIdCreatedBy() {
        return linktoUserMasterIdCreatedBy;
    }

    public void setLinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) {
        this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy;
    }

    public short getLinktoUserMasterIdUpdatedBy() {
        return linktoUserMasterIdUpdatedBy;
    }

    public void setLinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) {
        this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy;
    }

    public short getPrintCount() {
        return PrintCount;
    }

    public void setPrintCount(short printCount) {
        PrintCount = printCount;
    }

    public short getLinktoSourceMasterId() {
        return linktoSourceMasterId;
    }

    public void setLinktoSourceMasterId(short linktoSourceMasterId) {
        this.linktoSourceMasterId = linktoSourceMasterId;
    }

    public static Creator<OrderMaster> getCREATOR() {
        return CREATOR;
    }

    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(OrderMasterId);
        parcel.writeString(OrderNumber);
        parcel.writeString(OrderDateTime);
        parcel.writeInt(linktoCounterMasterId);
        parcel.writeString(linktoTableMasterIds);
        parcel.writeInt(linktoWaiterMasterId);
        parcel.writeInt(linktoCustomerMasterId);
        parcel.writeInt(linktoOrderTypeMasterId);
        parcel.writeInt(linktoOrderStatusMasterId);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeDouble(TotalAmount);
        parcel.writeDouble(TotalTax);
        parcel.writeDouble(Discount);
        parcel.writeDouble(DiscountPercentage);
        parcel.writeDouble(ExtraAmount);
        parcel.writeInt(TotalItemPoint);
        parcel.writeInt(TotalDeductedPoint);
        parcel.writeString(Remark);
        parcel.writeLong(linktoSalesMasterId);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeInt(PrintCount);
        parcel.writeInt(linktoSourceMasterId);

        /// Extra
        parcel.writeString(Counter);
        parcel.writeString(Waiter);
        parcel.writeString(Customer);
        parcel.writeString(OrderType);
        parcel.writeString(TableName);
        parcel.writeString(OrderTime);
        parcel.writeInt(TotalItem);
        parcel.writeInt(TotalKOT);
    }
}

