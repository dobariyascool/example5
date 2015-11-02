package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for CategoryMaster
/// </summary>
public class CategoryMaster implements Parcelable {
    //region Properties

    short CategoryMasterId;
    String CategoryName;
    short linktoCategoryMasterId;
    String ImagelNameBytes;
    String ImagelName;
    String CategoryColor;
    String Description;
    boolean IsNew;
    short linktoBusinessMasterId;
    short SortOrder;
    boolean IsEnabled;
    /// Extra
    String Category;
    public static final Parcelable.Creator<CategoryMaster> CREATOR = new Creator<CategoryMaster>() {
        public CategoryMaster createFromParcel(Parcel source) {
            CategoryMaster objCategoryMaster = new CategoryMaster();
            objCategoryMaster.CategoryMasterId = (short)source.readInt();
            objCategoryMaster.CategoryName = source.readString();
            objCategoryMaster.linktoCategoryMasterId = (short)source.readInt();
            objCategoryMaster.ImagelNameBytes = source.readString();
            objCategoryMaster.ImagelName = source.readString();
            objCategoryMaster.CategoryColor = source.readString();
            objCategoryMaster.Description = source.readString();
            objCategoryMaster.IsNew = source.readByte() != 0;
            objCategoryMaster.linktoBusinessMasterId = (short)source.readInt();
            objCategoryMaster.SortOrder = (short)source.readInt();
            objCategoryMaster.IsEnabled = source.readByte() != 0;

            /// Extra
            objCategoryMaster.Category = source.readString();
            return objCategoryMaster;
        }

        public CategoryMaster[] newArray(int size) {
            return new CategoryMaster[size];
        }
    };

    public short getCategoryMasterId() { return this.CategoryMasterId; }

    public void setCategoryMasterId(short categoryMasterId) { this.CategoryMasterId = categoryMasterId; }

    public String getCategoryName() { return this.CategoryName; }

    public void setCategoryName(String categoryName) { this.CategoryName = categoryName; }

    public short getlinktoCategoryMasterId() { return this.linktoCategoryMasterId; }

    public void setlinktoCategoryMasterId(short linktoCategoryMasterId) { this.linktoCategoryMasterId = linktoCategoryMasterId; }

    public String getImagelNameBytes() { return this.ImagelNameBytes; }

    public void setImagelNameBytes(String imagelNameBytes) { this.ImagelNameBytes = imagelNameBytes; }

    public String getImagelName() { return this.ImagelName; }

    public void setImagelName(String imagelName) { this.ImagelName = imagelName; }

    public String getCategoryColor() { return this.CategoryColor; }

    public void setCategoryColor(String categoryColor) { this.CategoryColor = categoryColor; }

    public String getDescription() { return this.Description; }

    public void setDescription(String description) { this.Description = description; }

    public boolean getIsNew() { return this.IsNew; }

    public void setIsNew(boolean isNew) { this.IsNew = isNew; }

    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public short getSortOrder() { return this.SortOrder; }

    public void setSortOrder(short sortOrder) { this.SortOrder = sortOrder; }

    public boolean getIsEnabled() { return this.IsEnabled; }

    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }

    public String getCategory() { return this.Category; }

    //endregion

    public void setCategory(String category) { this.Category = category; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(CategoryMasterId);
        parcel.writeString(CategoryName);
        parcel.writeInt(linktoCategoryMasterId);
        parcel.writeString(ImagelNameBytes);
        parcel.writeString(ImagelName);
        parcel.writeString(CategoryColor);
        parcel.writeString(Description);
        parcel.writeByte((byte)(IsNew ? 1 : 0));
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(SortOrder);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));

        /// Extra
        parcel.writeString(Category);
    }
}

