package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class UserMaster implements Parcelable {

    //region Properties
    short UserMasterId;
    String Username;
    String Password;
    short linktoRoleMasterId;
    short linktoUserTypeMasterId;
    String Role;
    public static final Creator<UserMaster> CREATOR = new Creator<UserMaster>() {
        @Override
        public UserMaster createFromParcel(Parcel source) {
            UserMaster objUserMaster = new UserMaster();
            objUserMaster.UserMasterId = (short) source.readInt();
            objUserMaster.Username = source.readString();
            objUserMaster.Password = source.readString();
            objUserMaster.linktoUserTypeMasterId = (short) source.readInt();
            objUserMaster.linktoRoleMasterId = (short) source.readInt();
            objUserMaster.Role = source.readString();
            return objUserMaster;
        }

        @Override
        public UserMaster[] newArray(int size) {
            return new UserMaster[size];
        }
    };

    public short getLinktoRoleMasterId() {
        return linktoRoleMasterId;
    }

    public void setLinktoRoleMasterId(short linktoRoleMasterId) {
        this.linktoRoleMasterId = linktoRoleMasterId;
    }

    public short getLinktoUserTypeMasterId() {
        return linktoUserTypeMasterId;
    }

    public void setLinktoUserTypeMasterId(short linktoUserTypeMasterId) {
        this.linktoUserTypeMasterId = linktoUserTypeMasterId;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public short getUserMasterId() {
        return UserMasterId;
    }

    public void setUserMasterId(short userMasterId) {
        UserMasterId = userMasterId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }
    //endregion

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(UserMasterId);
        parcel.writeString(Username);
        parcel.writeString(Password);
        parcel.writeInt(linktoUserTypeMasterId);
        parcel.writeInt(linktoRoleMasterId);
        parcel.writeString(Role);
    }
}
