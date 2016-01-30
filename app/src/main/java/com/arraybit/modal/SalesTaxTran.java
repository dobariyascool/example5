package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class SalesTaxTran implements Parcelable {
    //region Properties

    long SalesTaxTranId;
    long linktoSalesMasterId;
    short linktoTaxMasterId;
    String TaxName;
    double TaxRate;
    boolean IsPercentage;
    /// Extra
    long Sales;
    String Tax;
    public static final Parcelable.Creator<SalesTaxTran> CREATOR = new Creator<SalesTaxTran>() {
        public SalesTaxTran createFromParcel(Parcel source) {
            SalesTaxTran objSalesTaxTran = new SalesTaxTran();
            objSalesTaxTran.SalesTaxTranId = source.readLong();
            objSalesTaxTran.linktoSalesMasterId = source.readLong();
            objSalesTaxTran.linktoTaxMasterId = (short)source.readInt();
            objSalesTaxTran.TaxName = source.readString();
            objSalesTaxTran.TaxRate = source.readDouble();
            objSalesTaxTran.IsPercentage = source.readByte() != 0;

            /// Extra
            objSalesTaxTran.Sales = source.readLong();
            objSalesTaxTran.Tax = source.readString();
            return objSalesTaxTran;
        }

        public SalesTaxTran[] newArray(int size) {
            return new SalesTaxTran[size];
        }
    };

    public long getSalesTaxTranId() { return this.SalesTaxTranId; }

    public void setSalesTaxTranId(long salesTaxTranId) { this.SalesTaxTranId = salesTaxTranId; }

    public long getlinktoSalesMasterId() { return this.linktoSalesMasterId; }

    public void setlinktoSalesMasterId(long linktoSalesMasterId) { this.linktoSalesMasterId = linktoSalesMasterId; }

    public short getlinktoTaxMasterId() { return this.linktoTaxMasterId; }

    public void setlinktoTaxMasterId(short linktoTaxMasterId) { this.linktoTaxMasterId = linktoTaxMasterId; }

    public String getTaxName() { return this.TaxName; }

    public void setTaxName(String taxName) { this.TaxName = taxName; }

    public double getTaxRate() { return this.TaxRate; }

    public void setTaxRate(double taxRate) { this.TaxRate = taxRate; }

    public boolean getIsPercentage() { return this.IsPercentage; }

    public void setIsPercentage(boolean isPercentage) { this.IsPercentage = isPercentage; }

    public long getSales() { return this.Sales; }

    public void setSales(long sales) { this.Sales = sales; }

    public String getTax() { return this.Tax; }

    //endregion

    public void setTax(String tax) { this.Tax = tax; }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(SalesTaxTranId);
        parcel.writeLong(linktoSalesMasterId);
        parcel.writeInt(linktoTaxMasterId);
        parcel.writeString(TaxName);
        parcel.writeDouble(TaxRate);
        parcel.writeByte((byte)(IsPercentage ? 1 : 0));

        /// Extra
        parcel.writeLong(Sales);
        parcel.writeString(Tax);
    }

}

