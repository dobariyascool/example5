package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for BusinessMaster
/// </summary>
public class BusinessMaster implements Parcelable {
    //region Properties

    short BusinessMasterId;
    String BusinessName;
    String BusinessShortName;
    String Address;
    String Phone1;
    String Phone2;
    String Email;
    String Fax;
    String Website;
    short linktoCountryMasterId;
    short linktoStateMasterId;
    String City;
    String ZipCode;
    String ImageNameBytes;
    String ImageName;
    String ExtraText;
    String TIN;
    String TINRegistrationDate;
    String CST;
    String CSTRegistrationDate;
    short linktoBusinessTypeMasterId;
    String UniqueId;
    boolean IsEnabled;
    /// Extra
    String Country;
    String State;
    String BusinessType;
    public static final Parcelable.Creator<BusinessMaster> CREATOR = new Creator<BusinessMaster>() {
        public BusinessMaster createFromParcel(Parcel source) {
            BusinessMaster objBusinessMaster = new BusinessMaster();
            objBusinessMaster.BusinessMasterId = (short)source.readInt();
            objBusinessMaster.BusinessName = source.readString();
            objBusinessMaster.BusinessShortName = source.readString();
            objBusinessMaster.Address = source.readString();
            objBusinessMaster.Phone1 = source.readString();
            objBusinessMaster.Phone2 = source.readString();
            objBusinessMaster.Email = source.readString();
            objBusinessMaster.Fax = source.readString();
            objBusinessMaster.Website = source.readString();
            objBusinessMaster.linktoCountryMasterId = (short)source.readInt();
            objBusinessMaster.linktoStateMasterId = (short)source.readInt();
            objBusinessMaster.City = source.readString();
            objBusinessMaster.ZipCode = source.readString();
            objBusinessMaster.ImageNameBytes = source.readString();
            objBusinessMaster.ImageName = source.readString();
            objBusinessMaster.ExtraText = source.readString();
            objBusinessMaster.TIN = source.readString();
            objBusinessMaster.TINRegistrationDate = source.readString();
            objBusinessMaster.CST = source.readString();
            objBusinessMaster.CSTRegistrationDate = source.readString();
            objBusinessMaster.linktoBusinessTypeMasterId = (short)source.readInt();
            objBusinessMaster.UniqueId = source.readString();
            objBusinessMaster.IsEnabled = source.readByte() != 0;

            /// Extra
            objBusinessMaster.Country = source.readString();
            objBusinessMaster.State = source.readString();
            objBusinessMaster.BusinessType = source.readString();
            return objBusinessMaster;
        }

        public BusinessMaster[] newArray(int size) {
            return new BusinessMaster[size];
        }
    };

    public short getBusinessMasterId() { return this.BusinessMasterId; }

    public void setBusinessMasterId(short businessMasterId) { this.BusinessMasterId = businessMasterId; }

    public String getBusinessName() { return this.BusinessName; }

    public void setBusinessName(String businessName) { this.BusinessName = businessName; }

    public String getBusinessShortName() { return this.BusinessShortName; }

    public void setBusinessShortName(String businessShortName) { this.BusinessShortName = businessShortName; }

    public String getAddress() { return this.Address; }

    public void setAddress(String address) { this.Address = address; }

    public String getPhone1() { return this.Phone1; }

    public void setPhone1(String phone1) { this.Phone1 = phone1; }

    public String getPhone2() { return this.Phone2; }

    public void setPhone2(String phone2) { this.Phone2 = phone2; }

    public String getEmail() { return this.Email; }

    public void setEmail(String email) { this.Email = email; }

    public String getFax() { return this.Fax; }

    public void setFax(String fax) { this.Fax = fax; }

    public String getWebsite() { return this.Website; }

    public void setWebsite(String website) { this.Website = website; }

    public short getlinktoCountryMasterId() { return this.linktoCountryMasterId; }

    public void setlinktoCountryMasterId(short linktoCountryMasterId) { this.linktoCountryMasterId = linktoCountryMasterId; }

    public short getlinktoStateMasterId() { return this.linktoStateMasterId; }

    public void setlinktoStateMasterId(short linktoStateMasterId) { this.linktoStateMasterId = linktoStateMasterId; }

    public String getCity() { return this.City; }

    public void setCity(String city) { this.City = city; }

    public String getZipCode() { return this.ZipCode; }

    public void setZipCode(String zipCode) { this.ZipCode = zipCode; }

    public String getImageNameBytes() { return this.ImageNameBytes; }

    public void setImageNameBytes(String imageNameBytes) { this.ImageNameBytes = imageNameBytes; }

    public String getImageName() { return this.ImageName; }

    public void setImageName(String imageName) { this.ImageName = imageName; }

    public String getExtraText() { return this.ExtraText; }

    public void setExtraText(String extraText) { this.ExtraText = extraText; }

    public String getTIN() { return this.TIN; }

    public void setTIN(String tIN) { this.TIN = tIN; }

    public String getTINRegistrationDate() { return this.TINRegistrationDate; }

    public void setTINRegistrationDate(String tINRegistrationDate) { this.TINRegistrationDate = tINRegistrationDate; }

    public String getCST() { return this.CST; }

    public void setCST(String cST) { this.CST = cST; }

    public String getCSTRegistrationDate() { return this.CSTRegistrationDate; }

    public void setCSTRegistrationDate(String cSTRegistrationDate) { this.CSTRegistrationDate = cSTRegistrationDate; }

    public short getlinktoBusinessTypeMasterId() { return this.linktoBusinessTypeMasterId; }

    public void setlinktoBusinessTypeMasterId(short linktoBusinessTypeMasterId) { this.linktoBusinessTypeMasterId = linktoBusinessTypeMasterId; }

    public String getUniqueId() { return this.UniqueId; }

    public void setUniqueId(String uniqueId) { this.UniqueId = uniqueId; }

    public boolean getIsEnabled() { return this.IsEnabled; }

    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }

    public String getCountry() { return this.Country; }

    public void setCountry(String country) { this.Country = country; }

    public String getState() { return this.State; }

    public void setState(String state) { this.State = state; }

    public String getBusinessType() { return this.BusinessType; }

    //endregion

    public void setBusinessType(String businessType) { this.BusinessType = businessType; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(BusinessMasterId);
        parcel.writeString(BusinessName);
        parcel.writeString(BusinessShortName);
        parcel.writeString(Address);
        parcel.writeString(Phone1);
        parcel.writeString(Phone2);
        parcel.writeString(Email);
        parcel.writeString(Fax);
        parcel.writeString(Website);
        parcel.writeInt(linktoCountryMasterId);
        parcel.writeInt(linktoStateMasterId);
        parcel.writeString(City);
        parcel.writeString(ZipCode);
        parcel.writeString(ImageNameBytes);
        parcel.writeString(ImageName);
        parcel.writeString(ExtraText);
        parcel.writeString(TIN);
        parcel.writeString(TINRegistrationDate);
        parcel.writeString(CST);
        parcel.writeString(CSTRegistrationDate);
        parcel.writeInt(linktoBusinessTypeMasterId);
        parcel.writeString(UniqueId);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));

        /// Extra
        parcel.writeString(Country);
        parcel.writeString(State);
        parcel.writeString(BusinessType);
    }
}
