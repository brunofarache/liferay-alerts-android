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

import android.content.ContentValues;

import android.database.Cursor;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Bruno Farache
 */
public class Alert extends BaseModel implements Parcelable {

	public static final Parcelable.Creator<Alert> CREATOR =
		new Parcelable.Creator<Alert>() {
			public Alert createFromParcel(Parcel parcel) {
				return new Alert(parcel);
			}

			public Alert[] newArray(int size) {
				return new Alert[size];
			}
		};

	public static final String MESSAGE = "message";

	public Alert(Cursor cursor) {
		_id = cursor.getLong(cursor.getColumnIndex(ID));
		_message = cursor.getString(cursor.getColumnIndex(MESSAGE));
	}

	public Alert(String message) {
		_message = message;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public long getId() {
		return _id;
	}

	public String getMessage() {
		return _message;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(MESSAGE, getMessage());

		return values;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(getId());
		parcel.writeString(_message);
	}

	private Alert(Parcel parcel) {
		_id = parcel.readInt();
		_message = parcel.readString();
	}

	private long _id = -1;
	private String _message;

}