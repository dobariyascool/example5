package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class TableMaster implements Parcelable {

    //region Properties

    short TableMasterId;
    String TableName;
    String ShortName;
    String Description;
    short MinPerson;
    short MaxPerson;
    short linktoTableStatusMasterId;
    short linktoOrderTypeMasterId;
    int OriginX;
    int OriginY;
    double Height;
    double Width;
    String TableColor;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    short linktoBusinessMasterId;
    boolean IsEnabled;
    /// Extra
    String TableStatus;
    String StatusColor;
    String Section;
    String Business;
    public static final Parcelable.Creator<TableMaster> CREATOR = new Creator<TableMaster>() {
        public TableMaster createFromParcel(Parcel source) {
            TableMaster objTableMaster = new TableMaster();
            objTableMaster.TableMasterId = (short)source.readInt();
            objTableMaster.TableName = source.readString();
            objTableMaster.ShortName = source.readString();
            objTableMaster.Description = source.readString();
            objTableMaster.MinPerson = (short)source.readInt();
            objTableMaster.MaxPerson = (short)source.readInt();
            objTableMaster.linktoTableStatusMasterId = (short)source.readInt();
            objTableMaster.linktoOrderTypeMasterId = (short)source.readInt();
            objTableMaster.OriginX = source.readInt();
            objTableMaster.OriginY = source.readInt();
            objTableMaster.Height = source.readDouble();
            objTableMaster.Width = source.readDouble();
            objTableMaster.TableColor = source.readString();
            objTableMaster.CreateDateTime = source.readString();
            objTableMaster.linktoUserMasterIdCreatedBy = (short)source.readInt();
            objTableMaster.UpdateDateTime = source.readString();
            objTableMaster.linktoUserMasterIdUpdatedBy = (short)source.readInt();
            objTableMaster.linktoBusinessMasterId = (short)source.readInt();
            objTableMaster.IsEnabled = source.readByte() != 0;

            /// Extra
            objTableMaster.TableStatus = source.readString();
            objTableMaster.StatusColor = source.readString();
            objTableMaster.Business = source.readString();
            return objTableMaster;
        }

        public TableMaster[] newArray(int size) {
            return new TableMaster[size];
        }
    };

    public short getTableMasterId() { return this.TableMasterId; }

    public void setTableMasterId(short tableMasterId) { this.TableMasterId = tableMasterId; }

    public String getTableName() { return this.TableName; }

    public void setTableName(String tableName) { this.TableName = tableName; }

    public String getShortName() { return this.ShortName; }

    public void setShortName(String shortName) { this.ShortName = shortName; }

    public String getDescription() { return this.Description; }

    public void setDescription(String description) { this.Description = description; }

    public short getMinPerson() { return this.MinPerson; }

    public void setMinPerson(short minPerson) { this.MinPerson = minPerson; }

    public short getMaxPerson() { return this.MaxPerson; }

    public void setMaxPerson(short maxPerson) { this.MaxPerson = maxPerson; }

    public short getlinktoTableStatusMasterId() { return this.linktoTableStatusMasterId; }

    public void setlinktoTableStatusMasterId(short linktoTableStatusMasterId) { this.linktoTableStatusMasterId = linktoTableStatusMasterId; }

    public short getlinktoOrderTypeMasterId() { return this.linktoOrderTypeMasterId; }

    public void setlinktoOrderTypeMasterId(short linktoTableTypeMasterId) { this.linktoOrderTypeMasterId = linktoTableTypeMasterId; }

    public int getOriginX() { return this.OriginX; }

    public void setOriginX(int originX) { this.OriginX = originX; }

    public int getOriginY() { return this.OriginY; }

    public void setOriginY(int originY) { this.OriginY = originY; }

    public double getHeight() { return this.Height; }

    public void setHeight(double height) { this.Height = height; }

    public double getWidth() { return this.Width; }

    public void setWidth(double width) { this.Width = width; }

    public String getTableColor() { return this.TableColor; }

    public void setTableColor(String tableColor) { this.TableColor = tableColor; }

    public String getCreateDateTime() { return this.CreateDateTime; }

    public void setCreateDateTime(String createDateTime) { this.CreateDateTime = createDateTime; }

    public short getlinktoUserMasterIdCreatedBy() { return this.linktoUserMasterIdCreatedBy; }

    public void setlinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) { this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy; }

    public String getUpdateDateTime() { return this.UpdateDateTime; }

    public void setUpdateDateTime(String updateDateTime) { this.UpdateDateTime = updateDateTime; }

    public short getlinktoUserMasterIdUpdatedBy() { return this.linktoUserMasterIdUpdatedBy; }

    public void setlinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) { this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy; }

    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public boolean getIsEnabled() { return this.IsEnabled; }

    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }

    public String getTableStatus() { return this.TableStatus; }

    public void setTableStatus(String tableStatus) { this.TableStatus = tableStatus; }

    public String getSection() { return this.Section; }

    public void setSection(String section) { this.Section = section; }

    public String getBusiness() { return this.Business; }

    public void setBusiness(String business) { this.Business = business; }

    public String getStatusColor() { return StatusColor; }

    //endregion

    public void setStatusColor(String statusColor) {
        StatusColor = statusColor;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeInt(TableMasterId);
        parcel.writeString(TableName);
        parcel.writeString(ShortName);
        parcel.writeString(Description);
        parcel.writeInt(MinPerson);
        parcel.writeInt(MaxPerson);
        parcel.writeInt(linktoTableStatusMasterId);
        parcel.writeInt(linktoOrderTypeMasterId);
        parcel.writeInt(OriginX);
        parcel.writeInt(OriginY);
        parcel.writeDouble(Height);
        parcel.writeDouble(Width);
        parcel.writeString(TableColor);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeByte((byte) (IsEnabled ? 1 : 0));

        /// Extra
        parcel.writeString(TableStatus);
        parcel.writeString(StatusColor);
        parcel.writeString(Business);
    }

}
