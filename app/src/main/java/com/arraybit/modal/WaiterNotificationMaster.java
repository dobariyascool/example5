package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class WaiterNotificationMaster implements Parcelable {
	//region Properties

	long WaiterNotificationMasterId;
	String NotificationDateTime;
	short linktoTableMasterId;
	String Message;
	/// Extra
	String Table;
	public static final Creator<WaiterNotificationMaster> CREATOR = new Creator<WaiterNotificationMaster>() {
		public WaiterNotificationMaster createFromParcel(Parcel source) {
			WaiterNotificationMaster objWaiterNotificationMaster = new WaiterNotificationMaster();
			objWaiterNotificationMaster.WaiterNotificationMasterId = source.readLong();
			objWaiterNotificationMaster.NotificationDateTime = source.readString();
			objWaiterNotificationMaster.linktoTableMasterId = (short)source.readInt();
			objWaiterNotificationMaster.Message = source.readString();

			/// Extra
			objWaiterNotificationMaster.Table = source.readString();
			return objWaiterNotificationMaster;
		}

		public WaiterNotificationMaster[] newArray(int size) {
			return new WaiterNotificationMaster[size];
		}
	};

	public long getWaiterNotificationMasterId() { return this.WaiterNotificationMasterId; }

	public void setWaiterNotificationMasterId(long waiterNotificationMasterId) { this.WaiterNotificationMasterId = waiterNotificationMasterId; }

	public String getNotificationDateTime() { return this.NotificationDateTime; }

	public void setNotificationDateTime(String notificationDateTime) { this.NotificationDateTime = notificationDateTime; }

	public short getlinktoTableMasterId() { return this.linktoTableMasterId; }

	public void setlinktoTableMasterId(short linktoTableMasterId) { this.linktoTableMasterId = linktoTableMasterId; }

	public String getMessage() { return this.Message; }

	public void setMessage(String message) { this.Message = message; }

	public String getTable() { return this.Table; }

	//endregion

	public void setTable(String table) { this.Table = table; }

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(WaiterNotificationMasterId);
		parcel.writeString(NotificationDateTime);
		parcel.writeInt(linktoTableMasterId);
		parcel.writeString(Message);

		/// Extra
		parcel.writeString(Table);
	}
}
