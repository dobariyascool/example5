package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/// <summary>
/// Model for ItemMaster
/// </summary>
public class ItemMaster implements Parcelable {
    //region Properties

    int ItemMasterId;
    String ItemName;
    String ShortName;
    String ItemCode;
    String ShortDescription;
    double MRP;
    double SellPrice;
    short ItemPoint;
    short PriceByPoint;
    //short linktoItemTypeMasterId;
    short linktoUnitMasterId;
    String SearchWords;
    String ImageNameBytes;
    String ImageName;
    String XS_ImagePhysicalName;
    String SM_ImagePhysicalName;
    String MD_ImagePhysicalName;
    String LG_ImagePhysicalName;
    String XL_ImagePhysicalName;
    int SortOrder;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    short linktoBusinessMasterId;
    boolean IsEnabled;
    boolean IsDeleted;
    boolean IsFavourite;
    boolean IsRowMaterial;
    boolean IsDineInOnly;
    String BarCode;
    /// Extra
    //String ItemType;
    String Unit;
    String ItemModifierIds;
    String OptionValueTranIds;
    double ActualSellPrice;
    String Category;
    int Quantity;
    String Remark;
    long linktoOrderMasterId;
    String Tax;
    double TotalTax;
    double Tax1;
    double Tax2;
    double Tax3;
    double Tax4;
    double Tax5;
    double TaxRate;
    short RateIndex;
    public static final Parcelable.Creator<ItemMaster> CREATOR = new Creator<ItemMaster>() {
        public ItemMaster createFromParcel(Parcel source) {
            ItemMaster objItemMaster = new ItemMaster();
            objItemMaster.ItemMasterId = source.readInt();
            objItemMaster.ItemName = source.readString();
            objItemMaster.ShortName = source.readString();
            objItemMaster.ItemCode = source.readString();
            objItemMaster.ShortDescription = source.readString();
            objItemMaster.MRP = source.readDouble();
            objItemMaster.SellPrice = source.readDouble();
            objItemMaster.ItemPoint = (short) source.readInt();
            objItemMaster.PriceByPoint = (short) source.readInt();
            //objItemMaster.linktoItemTypeMasterId = (short)source.readInt();
            objItemMaster.linktoUnitMasterId = (short) source.readInt();
            objItemMaster.SearchWords = source.readString();
            objItemMaster.ImageNameBytes = source.readString();
            objItemMaster.ImageName = source.readString();
            objItemMaster.XS_ImagePhysicalName = source.readString();
            objItemMaster.SM_ImagePhysicalName = source.readString();
            objItemMaster.MD_ImagePhysicalName = source.readString();
            objItemMaster.LG_ImagePhysicalName = source.readString();
            objItemMaster.XL_ImagePhysicalName = source.readString();
            objItemMaster.SortOrder = source.readInt();
            objItemMaster.CreateDateTime = source.readString();
            objItemMaster.linktoUserMasterIdCreatedBy = (short) source.readInt();
            objItemMaster.UpdateDateTime = source.readString();
            objItemMaster.linktoUserMasterIdUpdatedBy = (short) source.readInt();
            objItemMaster.linktoBusinessMasterId = (short) source.readInt();
            objItemMaster.IsEnabled = source.readByte() != 0;
            objItemMaster.IsDeleted = source.readByte() != 0;
            objItemMaster.IsRowMaterial = source.readByte() != 0;
            objItemMaster.IsDineInOnly = source.readByte() != 0;
            objItemMaster.IsFavourite = source.readByte() != 0;
            objItemMaster.BarCode = source.readString();

            /// Extra
            //objItemMaster.ItemType = source.readString();
            objItemMaster.Unit = source.readString();
            objItemMaster.ActualSellPrice = source.readDouble();
            objItemMaster.linktoOrderMasterId = source.readLong();
            objItemMaster.ItemModifierIds= source.readString();
            objItemMaster.Quantity = source.readInt();
            objItemMaster.OptionValueTranIds = source.readString();
            objItemMaster.Tax = source.readString();
            objItemMaster.RateIndex = (short) source.readInt();
            objItemMaster.TaxRate = source.readDouble();
            objItemMaster.Tax1 = source.readDouble();
            objItemMaster.Tax2 = source.readDouble();
            objItemMaster.Tax3 = source.readDouble();
            objItemMaster.Tax4 = source.readDouble();
            objItemMaster.Tax5 = source.readDouble();
            objItemMaster.TotalTax = source.readDouble();
            objItemMaster.Category = source.readString();


            return objItemMaster;
        }

        public ItemMaster[] newArray(int size) {
            return new ItemMaster[size];
        }
    };
    String ItemRemark;
    String OptionValue;
    boolean IsRateTaxInclusive;
    double TotalAmount;
    double ExtraAmount;
    ArrayList<ItemMaster> alOrderItemModifierTran;

    public int getItemMasterId() {
        return this.ItemMasterId;
    }

    public void setItemMasterId(int itemMasterId) {
        this.ItemMasterId = itemMasterId;
    }

    public String getItemName() {
        return this.ItemName;
    }

    public void setItemName(String itemName) {
        this.ItemName = itemName;
    }

    public String getShortName() {
        return this.ShortName;
    }

    public void setShortName(String shortName) {
        this.ShortName = shortName;
    }

    public String getItemCode() {
        return this.ItemCode;
    }

    public void setItemCode(String itemCode) {
        this.ItemCode = itemCode;
    }

    public String getShortDescription() {
        return this.ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.ShortDescription = shortDescription;
    }

    public double getMRP() { return this.MRP; }

    public void setMRP(double mRP) { this.MRP = mRP; }
//
    public double getSellPrice() { return this.SellPrice; }

    public void setSellPrice(double sellPrice) { this.SellPrice = sellPrice; }

    public short getItemPoint() {
        return this.ItemPoint;
    }

    public void setItemPoint(short itemPoint) {
        this.ItemPoint = itemPoint;
    }

    public short getPriceByPoint() {
        return this.PriceByPoint;
    }

    public void setPriceByPoint(short priceByPoint) {
        this.PriceByPoint = priceByPoint;
    }

//    public short getlinktoItemTypeMasterId() { return this.linktoItemTypeMasterId; }
//
//    public void setlinktoItemTypeMasterId(short linktoItemTypeMasterId) { this.linktoItemTypeMasterId = linktoItemTypeMasterId; }

    public short getlinktoUnitMasterId() {
        return this.linktoUnitMasterId;
    }

    public void setlinktoUnitMasterId(short linktoUnitMasterId) {
        this.linktoUnitMasterId = linktoUnitMasterId;
    }

    public String getSearchWords() {
        return this.SearchWords;
    }

    public void setSearchWords(String searchWords) {
        this.SearchWords = searchWords;
    }

    public String getImageNameBytes() {
        return this.ImageNameBytes;
    }

    public void setImageNameBytes(String imageNameBytes) {
        this.ImageNameBytes = imageNameBytes;
    }

    public String getImageName() {
        return this.ImageName;
    }

    public void setImageName(String imageName) {
        this.ImageName = imageName;
    }

    public int getSortOrder() {
        return this.SortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.SortOrder = sortOrder;
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

    public short getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
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

//    public String getItemType() { return this.ItemType; }
//
//    public void setItemType(String itemType) { this.ItemType = itemType; }

    public String getUnit() {
        return this.Unit;
    }

    public void setUnit(String unit) {
        this.Unit = unit;
    }

    public boolean isFavourite() {
        return IsFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        IsFavourite = isFavourite;
    }

    public boolean isRowMaterial() {
        return IsRowMaterial;
    }

    public void setIsRowMaterial(boolean isRowMaterial) {
        IsRowMaterial = isRowMaterial;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }


    public double getExtraAmount() {
        return ExtraAmount;
    }

    public void setExtraAmount(double extraAmount) {
        ExtraAmount = extraAmount;
    }


    public ArrayList<ItemMaster> getAlOrderItemModifierTran() {
        return alOrderItemModifierTran;
    }

    public void setAlOrderItemModifierTran(ArrayList<ItemMaster> alOrderItemModifierTran) {
        this.alOrderItemModifierTran = alOrderItemModifierTran;
    }


    public String getItemModifierIds() {
        return ItemModifierIds;
    }

    public void setItemModifierIds(String itemModifierIds) {
        ItemModifierIds = itemModifierIds;
    }

    public double getActualSellPrice() {
        return ActualSellPrice;
    }

    public void setActualSellPrice(double actualSellPrice) {
        ActualSellPrice = actualSellPrice;
    }

    public long getLinktoOrderMasterId() {
        return linktoOrderMasterId;
    }

    public void setLinktoOrderMasterId(long linktoOrderMasterId) {
        this.linktoOrderMasterId = linktoOrderMasterId;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getOptionValueTranIds() {
        return OptionValueTranIds;
    }

    public void setOptionValueTranIds(String optionValueTranIds) {
        OptionValueTranIds = optionValueTranIds;
    }

    public String getXL_ImagePhysicalName() {
        return XL_ImagePhysicalName;
    }

    public void setXL_ImagePhysicalName(String XL_ImagePhysicalName) {
        this.XL_ImagePhysicalName = XL_ImagePhysicalName;
    }

    public String getLG_ImagePhysicalName() {
        return LG_ImagePhysicalName;
    }

    public void setLG_ImagePhysicalName(String LG_ImagePhysicalName) {
        this.LG_ImagePhysicalName = LG_ImagePhysicalName;
    }

    public String getMD_ImagePhysicalName() {
        return MD_ImagePhysicalName;
    }

    public void setMD_ImagePhysicalName(String MD_ImagePhysicalName) {
        this.MD_ImagePhysicalName = MD_ImagePhysicalName;
    }

    public String getSM_ImagePhysicalName() {
        return SM_ImagePhysicalName;
    }

    public void setSM_ImagePhysicalName(String SM_ImagePhysicalName) {
        this.SM_ImagePhysicalName = SM_ImagePhysicalName;
    }

    public String getXS_ImagePhysicalName() {
        return XS_ImagePhysicalName;
    }

    public void setXS_ImagePhysicalName(String XS_ImagePhysicalName) {
        this.XS_ImagePhysicalName = XS_ImagePhysicalName;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public double getTotalTax() {
        return TotalTax;
    }

    public void setTotalTax(double totalTax) {
        TotalTax = totalTax;
    }

    public double getTax1() {
        return Tax1;
    }

    public void setTax1(double tax1) {
        Tax1 = tax1;
    }

    public double getTax2() {
        return Tax2;
    }

    public void setTax2(double tax2) {
        Tax2 = tax2;
    }

    public double getTax3() {
        return Tax3;
    }

    public void setTax3(double tax3) {
        Tax3 = tax3;
    }

    public double getTax4() {
        return Tax4;
    }

    public void setTax4(double tax4) {
        Tax4 = tax4;
    }

    public double getTax5() {
        return Tax5;
    }

    public void setTax5(double tax5) {
        Tax5 = tax5;
    }

    public double getTaxRate() {
        return TaxRate;
    }

    public void setTaxRate(double taxRate) {
        TaxRate = taxRate;
    }

    public short getRateIndex() {
        return RateIndex;
    }

    public void setRateIndex(short rateIndex) {
        RateIndex = rateIndex;
    }

    public boolean getIsRateTaxInclusive() {
        return IsRateTaxInclusive;
    }

    public void setIsRateTaxInclusive(boolean isRateTaxInclusive) {
        IsRateTaxInclusive = isRateTaxInclusive;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getItemRemark() {
        return ItemRemark;
    }

    public void setItemRemark(String itemRemark) {
        ItemRemark = itemRemark;
    }

    public String getOptionValue() {
        return OptionValue;
    }

    public void setOptionValue(String optionValue) {
        OptionValue = optionValue;
    }

    public boolean getIsDineInOnly() {
        return IsDineInOnly;
    }

    public void setIsDineInOnly(boolean isDineInOnly) {
        IsDineInOnly = isDineInOnly;
    }

    //endregion
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(ItemMasterId);
        parcel.writeString(ItemName);
        parcel.writeString(ShortName);
        parcel.writeString(ItemCode);
        parcel.writeString(ShortDescription);
        parcel.writeDouble(MRP);
        parcel.writeDouble(SellPrice);
        parcel.writeInt(ItemPoint);
        parcel.writeInt(PriceByPoint);
        //parcel.writeInt(linktoItemTypeMasterId);
        parcel.writeInt(linktoUnitMasterId);
        parcel.writeString(SearchWords);
        parcel.writeString(ImageNameBytes);
        parcel.writeString(ImageName);
        parcel.writeString(XS_ImagePhysicalName);
        parcel.writeString(SM_ImagePhysicalName);
        parcel.writeString(MD_ImagePhysicalName);
        parcel.writeString(LG_ImagePhysicalName);
        parcel.writeString(XL_ImagePhysicalName);
        parcel.writeInt(SortOrder);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeByte((byte) (IsEnabled ? 1 : 0));
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));
        parcel.writeByte((byte) (IsFavourite ? 1 : 0));
        parcel.writeByte((byte) (IsRowMaterial ? 1 : 0));
        parcel.writeByte((byte) (IsDineInOnly ? 1 : 0));
        parcel.writeString(BarCode);

        /// Extra
        //parcel.writeString(ItemType);
        parcel.writeString(Unit);
        parcel.writeInt(Quantity);
        parcel.writeDouble(ActualSellPrice);
        parcel.writeLong(linktoOrderMasterId);
        parcel.writeString(ItemModifierIds);
        parcel.writeString(OptionValueTranIds);
        parcel.writeString(Tax);
        parcel.writeInt(RateIndex);
        parcel.writeDouble(TotalTax);
        parcel.writeDouble(Tax1);
        parcel.writeDouble(Tax2);
        parcel.writeDouble(Tax3);
        parcel.writeDouble(Tax4);
        parcel.writeDouble(Tax5);
        parcel.writeDouble(TaxRate);
        parcel.writeString(Category);
    }
}

