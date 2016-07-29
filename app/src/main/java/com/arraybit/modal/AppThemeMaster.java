package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for AppThemeMaster
/// </summary>
public class AppThemeMaster implements Parcelable {
    //region Properties

    int AppThemeMasterId;
    String LogoImageNameBytes;
    String LogoImageName;
    String ProfileImageNameBytes;
    String ProfileImageName;
    String BackImageName1Bytes;
    String BackImageName1;
    String BackImageName2Bytes;
    String BackImageName2;
    String ContactMap;
    int ColorPrimary;
    int ColorPrimaryDark;
    int ColorPrimaryLight;
    int ColorAccent;
    int ColorAccentDark;
    int ColorAccentLight;
    int ColorTextPrimary;
    int ColorTextSecondary;
    int ColorButtonRipple;
    int ColorCardView;
    int ColorCardViewRipple;
    int ColorCardText;
    int ColorHeaderText;

    public static final Creator<AppThemeMaster> CREATOR = new Creator<AppThemeMaster>() {
        @Override
        public AppThemeMaster createFromParcel(Parcel in) {
            AppThemeMaster appThemeMaster=new AppThemeMaster();
            appThemeMaster.AppThemeMasterId = in.readInt();
            appThemeMaster.LogoImageNameBytes = in.readString();
            appThemeMaster.LogoImageName = in.readString();
            appThemeMaster.ProfileImageNameBytes = in.readString();
            appThemeMaster.ProfileImageName = in.readString();
            appThemeMaster.BackImageName1Bytes = in.readString();
            appThemeMaster.BackImageName1 = in.readString();
            appThemeMaster.BackImageName2Bytes = in.readString();
            appThemeMaster.BackImageName2 = in.readString();
            appThemeMaster.ContactMap = in.readString();
            appThemeMaster.ColorPrimary = in.readInt();
            appThemeMaster.ColorPrimaryDark = in.readInt();
            appThemeMaster.ColorPrimaryLight = in.readInt();
            appThemeMaster.ColorAccent = in.readInt();
            appThemeMaster.ColorAccentDark = in.readInt();
            appThemeMaster.ColorAccentLight = in.readInt();
            appThemeMaster.ColorTextPrimary = in.readInt();
            appThemeMaster.ColorTextSecondary = in.readInt();
            appThemeMaster.ColorButtonRipple = in.readInt();
            appThemeMaster.ColorCardView = in.readInt();
            appThemeMaster.ColorCardViewRipple = in.readInt();
            appThemeMaster.ColorCardText = in.readInt();
            appThemeMaster.ColorHeaderText = in.readInt();
            return appThemeMaster;
        }

        @Override
        public AppThemeMaster[] newArray(int size) {
            return new AppThemeMaster[size];
        }
    };

    public int getAppThemeMasterId() {
        return this.AppThemeMasterId;
    }

    public void setAppThemeMasterId(int appThemeMasterId) {
        this.AppThemeMasterId = appThemeMasterId;
    }

    public String getLogoImageNameBytes() {
        return this.LogoImageNameBytes;
    }

    public void setLogoImageNameBytes(String logoImageNameBytes) {
        this.LogoImageNameBytes = logoImageNameBytes;
    }

    public String getLogoImageName() {
        return this.LogoImageName;
    }

    public void setLogoImageName(String logoImageName) {
        this.LogoImageName = logoImageName;
    }

    public String getProfileImageNameBytes() {
        return this.ProfileImageNameBytes;
    }

    public void setProfileImageNameBytes(String profileImageNameBytes) {
        this.ProfileImageNameBytes = profileImageNameBytes;
    }

    public String getProfileImageName() {
        return this.ProfileImageName;
    }

    public void setProfileImageName(String profileImageName) {
        this.ProfileImageName = profileImageName;
    }

    public String getBackImageName1Bytes() {
        return this.BackImageName1Bytes;
    }

    public void setBackImageName1Bytes(String backImageName1Bytes) {
        this.BackImageName1Bytes = backImageName1Bytes;
    }

    public String getBackImageName1() {
        return this.BackImageName1;
    }

    public void setBackImageName1(String backImageName1) {
        this.BackImageName1 = backImageName1;
    }

    public String getBackImageName2Bytes() {
        return this.BackImageName2Bytes;
    }

    public void setBackImageName2Bytes(String backImageName2Bytes) {
        this.BackImageName2Bytes = backImageName2Bytes;
    }

    public String getBackImageName2() {
        return this.BackImageName2;
    }

    public void setBackImageName2(String backImageName2) {
        this.BackImageName2 = backImageName2;
    }

    public String getContactMap() {
        return this.ContactMap;
    }

    public void setContactMap(String contactMap) {
        this.ContactMap = contactMap;
    }

    public int getColorPrimary() {
        return ColorPrimary;
    }

    public void setColorPrimary(int colorPrimary) {
        ColorPrimary = colorPrimary;
    }

    public int getColorPrimaryDark() {
        return ColorPrimaryDark;
    }

    public void setColorPrimaryDark(int colorPrimaryDark) {
        ColorPrimaryDark = colorPrimaryDark;
    }

    public int getColorPrimaryLight() {
        return ColorPrimaryLight;
    }

    public void setColorPrimaryLight(int colorPrimaryLight) {
        ColorPrimaryLight = colorPrimaryLight;
    }

    public int getColorAccent() {
        return ColorAccent;
    }

    public void setColorAccent(int colorAccent) {
        ColorAccent = colorAccent;
    }

    public int getColorAccentDark() {
        return ColorAccentDark;
    }

    public void setColorAccentDark(int colorAccentDark) {
        ColorAccentDark = colorAccentDark;
    }

    public int getColorAccentLight() {
        return ColorAccentLight;
    }

    public void setColorAccentLight(int colorAccentLight) {
        ColorAccentLight = colorAccentLight;
    }

    public int getColorTextPrimary() {
        return ColorTextPrimary;
    }

    public void setColorTextPrimary(int colorTextPrimary) {
        ColorTextPrimary = colorTextPrimary;
    }

    public int getColorTextSecondary() {
        return ColorTextSecondary;
    }

    public void setColorTextSecondary(int colorTextSecondary) {
        ColorTextSecondary = colorTextSecondary;
    }

    public int getColorButtonRipple() {
        return ColorButtonRipple;
    }

    public void setColorButtonRipple(int colorButtonRipple) {
        ColorButtonRipple = colorButtonRipple;
    }

    public int getColorCardView() {
        return ColorCardView;
    }

    public void setColorCardView(int colorCardView) {
        ColorCardView = colorCardView;
    }

    public int getColorCardViewRipple() {
        return ColorCardViewRipple;
    }

    public void setColorCardViewRipple(int colorCardViewRipple) {
        ColorCardViewRipple = colorCardViewRipple;
    }

    public int getColorCardText() {
        return ColorCardText;
    }

    public void setColorCardText(int colorCardText) {
        ColorCardText = colorCardText;
    }

    public int getColorHeaderText() {
        return ColorHeaderText;
    }

    public void setColorHeaderText(int colorHeaderText) {
        ColorHeaderText = colorHeaderText;
    }

    public static Creator<AppThemeMaster> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(AppThemeMasterId);
        dest.writeString(LogoImageNameBytes);
        dest.writeString(LogoImageName);
        dest.writeString(ProfileImageNameBytes);
        dest.writeString(ProfileImageName);
        dest.writeString(BackImageName1Bytes);
        dest.writeString(BackImageName1);
        dest.writeString(BackImageName2Bytes);
        dest.writeString(BackImageName2);
        dest.writeString(ContactMap);
        dest.writeInt(ColorPrimary);
        dest.writeInt(ColorPrimaryDark);
        dest.writeInt(ColorPrimaryLight);
        dest.writeInt(ColorAccent);
        dest.writeInt(ColorAccentDark);
        dest.writeInt(ColorAccentLight);
        dest.writeInt(ColorTextPrimary);
        dest.writeInt(ColorTextSecondary);
        dest.writeInt(ColorButtonRipple);
        dest.writeInt(ColorCardView);
        dest.writeInt(ColorCardViewRipple);
        dest.writeInt(ColorCardText);
        dest.writeInt(ColorHeaderText);
    }
}
