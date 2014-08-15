/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.alerts.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Bruno Farache
 */
public class User implements Parcelable {

	public static final Creator<User> CREATOR =
		new Creator<User>() {
			public User createFromParcel(Parcel parcel) {
				return new User(parcel);
			}

			public User[] newArray(int size) {
				return new User[size];
			}
		};

	public User(String uuid, long userId, String fullName, long portraitId) {
		_uuid = uuid;
		_userId = userId;
		_fullName = fullName;
		_portraitId = portraitId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getFullName() {
		return _fullName;
	}

	public long getPortraitId() {
		return _portraitId;
	}

	public long getUserId() {
		return _userId;
	}

	public String getUuid() {
		return _uuid;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(_uuid);
		parcel.writeLong(_userId);
		parcel.writeString(_fullName);
		parcel.writeLong(_portraitId);
	}

	private User(Parcel parcel) {
		_uuid = parcel.readString();
		_userId = parcel.readInt();
		_fullName = parcel.readString();
		_portraitId = parcel.readLong();
	}

	private String _fullName;
	private long _portraitId;
	private long _userId;
	private String _uuid;

}