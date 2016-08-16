package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class SalesMaster implements Parcelable {
    //region Properties

    long SalesMasterId;
    String BillNumber;
    String BillDateTime;
    short linktoCounterMasterId;
    String linktoTableMasterIds;
    int linktoWaiterMasterId;
    int linktoWaiterMasterIdCaptain;
    int linktoCustomerMasterId;
    short linktoOrderTypeMasterId;
    short linktoOrderStatusMasterId;
    double TotalAmount;
    double TotalTax;
    double DiscountPercentage;
    double DiscountAmount;
    double ExtraAmount;
    double TotalItemDiscount;
    double TotalItemTax;
    double NetAmount;
    double PaidAmount;
    double BalanceAmount;
    String Remark;
    boolean Iscomplimentary;
    short TotalItemPoint;
    short TotalDeductedPoint;
    short linktoBusinessMasterId;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    double Rounding;
    short RateIndex;
    short linktoOfferMasterId;
    String OfferCode;


    /// Extra
    String Counter;
    String Waiter;
    String WaiterCaptain;
    String Customer;
    String OrderType;
    String OrderStatus;
    String Business;
    short linktoSourceMasterId;

    public static final Parcelable.Creator<SalesMaster> CREATOR = new Creator<SalesMaster>() {
        public SalesMaster createFromParcel(Parcel source) {
            SalesMaster objSalesMaster = new SalesMaster();
            objSalesMaster.SalesMasterId = source.readLong();
            objSalesMaster.BillNumber = source.readString();
            objSalesMaster.BillDateTime = source.readString();
            objSalesMaster.linktoCounterMasterId = (short) source.readInt();
            objSalesMaster.linktoTableMasterIds = source.readString();
            objSalesMaster.linktoWaiterMasterId = source.readInt();
            objSalesMaster.linktoWaiterMasterIdCaptain = source.readInt();
            objSalesMaster.linktoCustomerMasterId = source.readInt();
            objSalesMaster.linktoOrderTypeMasterId = (short) source.readInt();
            objSalesMaster.linktoOrderStatusMasterId = (short) source.readInt();
            objSalesMaster.TotalAmount = source.readDouble();
            objSalesMaster.TotalTax = source.readDouble();
            objSalesMaster.DiscountPercentage = source.readDouble();
            objSalesMaster.DiscountAmount = source.readDouble();
            objSalesMaster.ExtraAmount = source.readDouble();
            objSalesMaster.TotalItemDiscount = source.readDouble();
            objSalesMaster.TotalItemTax = source.readDouble();
            objSalesMaster.NetAmount = source.readDouble();
            objSalesMaster.PaidAmount = source.readDouble();
            objSalesMaster.BalanceAmount = source.readDouble();
            objSalesMaster.Remark = source.readString();
            objSalesMaster.Iscomplimentary = source.readByte() != 0;
            objSalesMaster.TotalItemPoint = (short) source.readInt();
            objSalesMaster.TotalDeductedPoint = (short) source.readInt();
            objSalesMaster.linktoBusinessMasterId = (short) source.readInt();
            objSalesMaster.CreateDateTime = source.readString();
            objSalesMaster.linktoUserMasterIdCreatedBy = (short) source.readInt();
            objSalesMaster.UpdateDateTime = source.readString();
            objSalesMaster.linktoUserMasterIdUpdatedBy = (short) source.readInt();
            objSalesMaster.RateIndex = (short) source.readInt();
            objSalesMaster.linktoOfferMasterId = (short) source.readInt();
            objSalesMaster.OfferCode = source.readString();

            /// Extra
            objSalesMaster.Counter = source.readString();
            objSalesMaster.Waiter = source.readString();
            objSalesMaster.WaiterCaptain = source.readString();
            objSalesMaster.Customer = source.readString();
            objSalesMaster.OrderType = source.readString();
            objSalesMaster.OrderStatus = source.readString();
            objSalesMaster.Business = source.readString();
            objSalesMaster.linktoSourceMasterId = (short) source.readInt();
            return objSalesMaster;
        }

        public SalesMaster[] newArray(int size) {
            return new SalesMaster[size];
        }
    };

    public long getSalesMasterId() {
        return this.SalesMasterId;
    }

    public void setSalesMasterId(long salesMasterId) {
        this.SalesMasterId = salesMasterId;
    }

    public String getBillNumber() {
        return this.BillNumber;
    }

    public void setBillNumber(String billNumber) {
        this.BillNumber = billNumber;
    }

    public String getBillDateTime() {
        return this.BillDateTime;
    }

    public void setBillDateTime(String billDateTime) {
        this.BillDateTime = billDateTime;
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

    public int getlinktoWaiterMasterIdCaptain() {
        return this.linktoWaiterMasterIdCaptain;
    }

    public void setlinktoWaiterMasterIdCaptain(int linktoWaiterMasterIdCaptain) {
        this.linktoWaiterMasterIdCaptain = linktoWaiterMasterIdCaptain;
    }

    public int getlinktoCustomerMasterId() {
        return this.linktoCustomerMasterId;
    }

    public void setlinktoCustomerMasterId(int linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public short getlinktoOrderTypeMasterId() {
        return this.linktoOrderTypeMasterId;
    }

    public void setlinktoOrderTypeMasterId(short linktoOrderTypeMasterId) {
        this.linktoOrderTypeMasterId = linktoOrderTypeMasterId;
    }

    public short getlinktoOrderStatusMasterId() {
        return this.linktoOrderStatusMasterId;
    }

    public void setlinktoOrderStatusMasterId(short linktoOrderStatusMasterId) {
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

    public double getDiscountPercentage() {
        return this.DiscountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.DiscountPercentage = discountPercentage;
    }

    public double getDiscountAmount() {
        return this.DiscountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.DiscountAmount = discountAmount;
    }

    public double getExtraAmount() {
        return this.ExtraAmount;
    }

    public void setExtraAmount(double extraAmount) {
        this.ExtraAmount = extraAmount;
    }

    public double getTotalItemDiscount() {
        return this.TotalItemDiscount;
    }

    public void setTotalItemDiscount(double totalItemDiscount) {
        this.TotalItemDiscount = totalItemDiscount;
    }

    public double getTotalItemTax() {
        return this.TotalItemTax;
    }

    public void setTotalItemTax(double totalItemTax) {
        this.TotalItemTax = totalItemTax;
    }

    public double getNetAmount() {
        return this.NetAmount;
    }

    public void setNetAmount(double netAmount) {
        this.NetAmount = netAmount;
    }

    public double getPaidAmount() {
        return this.PaidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.PaidAmount = paidAmount;
    }

    public double getBalanceAmount() {
        return this.BalanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.BalanceAmount = balanceAmount;
    }

    public String getRemark() {
        return this.Remark;
    }

    public void setRemark(String remark) {
        this.Remark = remark;
    }

    public boolean getIscomplimentary() {
        return this.Iscomplimentary;
    }

    public void setIscomplimentary(boolean iscomplimentary) {
        this.Iscomplimentary = iscomplimentary;
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

    public short getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
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

    public String getWaiterCaptain() {
        return this.WaiterCaptain;
    }

    public void setWaiterCaptain(String waiterCaptain) {
        this.WaiterCaptain = waiterCaptain;
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

    public String getOrderStatus() {
        return this.OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.OrderStatus = orderStatus;
    }

    public String getBusiness() {
        return this.Business;
    }

    public void setBusiness(String business) {
        this.Business = business;
    }

    public double getRounding() {
        return Rounding;
    }

    public short getRateIndex() {
        return RateIndex;
    }

    public void setRateIndex(short rateIndex) {
        RateIndex = rateIndex;
    }

    //endregion

    public void setRounding(double rounding) {
        Rounding = rounding;
    }

    public short getLinktoOfferMasterId() {
        return linktoOfferMasterId;
    }

    public void setLinktoOfferMasterId(short linktoOfferMasterId) {
        this.linktoOfferMasterId = linktoOfferMasterId;
    }

    public String getOfferCode() {
        return OfferCode;
    }

    public void setOfferCode(String offerCode) {
        OfferCode = offerCode;
    }

    public short getLinktoSourceMasterId() {
        return linktoSourceMasterId;
    }

    public void setLinktoSourceMasterId(short linktoSourceMasterId) {
        this.linktoSourceMasterId = linktoSourceMasterId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(SalesMasterId);
        parcel.writeString(BillNumber);
        parcel.writeString(BillDateTime);
        parcel.writeInt(linktoCounterMasterId);
        parcel.writeString(linktoTableMasterIds);
        parcel.writeInt(linktoWaiterMasterId);
        parcel.writeInt(linktoWaiterMasterIdCaptain);
        parcel.writeInt(linktoCustomerMasterId);
        parcel.writeInt(linktoOrderTypeMasterId);
        parcel.writeInt(linktoOrderStatusMasterId);
        parcel.writeDouble(TotalAmount);
        parcel.writeDouble(TotalTax);
        parcel.writeDouble(DiscountPercentage);
        parcel.writeDouble(DiscountAmount);
        parcel.writeDouble(ExtraAmount);
        parcel.writeDouble(TotalItemDiscount);
        parcel.writeDouble(TotalItemTax);
        parcel.writeDouble(NetAmount);
        parcel.writeDouble(PaidAmount);
        parcel.writeDouble(BalanceAmount);
        parcel.writeString(Remark);
        parcel.writeByte((byte) (Iscomplimentary ? 1 : 0));
        parcel.writeInt(TotalItemPoint);
        parcel.writeInt(TotalDeductedPoint);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeInt(RateIndex);
        parcel.writeInt(linktoOfferMasterId);
        parcel.writeString(OfferCode);

        /// Extra
        parcel.writeString(Counter);
        parcel.writeString(Waiter);
        parcel.writeString(WaiterCaptain);
        parcel.writeString(Customer);
        parcel.writeString(OrderType);
        parcel.writeString(OrderStatus);
        parcel.writeString(Business);
        parcel.writeInt(linktoSourceMasterId);
    }
}
