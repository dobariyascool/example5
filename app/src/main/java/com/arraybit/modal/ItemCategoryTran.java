package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemCategoryTran implements Parcelable {
    int ItemCategoryTranId;
    int linktoItemMasterId;
    short linktoCategoryMasterId;

    public static final Parcelable.Creator<ItemCategoryTran> CREATOR = new Creator<ItemCategoryTran>() {
        @Override
        public ItemCategoryTran createFromParcel(Parcel source) {
            ItemCategoryTran objItemCategoryTran = new ItemCategoryTran();
            objItemCategoryTran.ItemCategoryTranId = source.readInt();
            objItemCategoryTran.linktoItemMasterId = source.readInt();
            objItemCategoryTran.linktoCategoryMasterId = (short) source.readInt();
            return objItemCategoryTran;
        }

        @Override
        public ItemCategoryTran[] newArray(int size) {
            return new ItemCategoryTran[size];
        }
    };

    public int getItemCategoryTranId() {
        return ItemCategoryTranId;
    }

    public void setItemCategoryTranId(int itemCategoryTranId) {
        ItemCategoryTranId = itemCategoryTranId;
    }

    public int getLinktoItemMasterId() {
        return linktoItemMasterId;
    }

    public void setLinktoItemMasterId(int linktoItemMasterId) {
        this.linktoItemMasterId = linktoItemMasterId;
    }

    public short getLinktoCategoryMasterId() {
        return linktoCategoryMasterId;
    }

    public void setLinktoCategoryMasterId(short linktoCategoryMasterId) {
        this.linktoCategoryMasterId = linktoCategoryMasterId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(ItemCategoryTranId);
        parcel.writeInt(linktoItemMasterId);
        parcel.writeInt(ItemCategoryTranId);
    }
}
