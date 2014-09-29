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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class User extends BaseModel implements Parcelable {

	public static final Creator<User> CREATOR =
		new Creator<User>() {
			public User createFromParcel(Parcel parcel) {
				return new User(parcel);
			}

			public User[] newArray(int size) {
				return new User[size];
			}
		};

	public static final String FULL_NAME = "fullName";

	public static final String PORTRAIT_ID = "portraitId";

	public static final String UUID = "uuid";

	public User(Cursor cursor) {
		_id = cursor.getLong(cursor.getColumnIndex(ID));
		_uuid = cursor.getString(cursor.getColumnIndex(UUID));
		_fullName = cursor.getString(cursor.getColumnIndex(FULL_NAME));
		_portraitId = cursor.getLong(cursor.getColumnIndex(PORTRAIT_ID));
	}

	public User(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);

		_id = jsonObject.getLong("userId");
		_uuid = jsonObject.getString(User.UUID);
		_fullName = jsonObject.getString(User.FULL_NAME);
		_portraitId = jsonObject.getLong(User.PORTRAIT_ID);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getFullName() {
		return _fullName;
	}

	@Override
	public long getId() {
		return _id;
	}

	public long getPortraitId() {
		return _portraitId;
	}

	public String getUuid() {
		return _uuid;
	}

	@Override
	public void setId(long id) {
		_id = id;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(ID, _id);
		values.put(UUID, _uuid);
		values.put(FULL_NAME, _fullName);
		values.put(PORTRAIT_ID, _portraitId);

		return values;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(_id);
		parcel.writeString(_uuid);
		parcel.writeString(_fullName);
		parcel.writeLong(_portraitId);
	}

	private User(Parcel parcel) {
		_id = parcel.readLong();
		_uuid = parcel.readString();
		_fullName = parcel.readString();
		_portraitId = parcel.readLong();
	}

	private String _fullName;
	private long _id;
	private long _portraitId;
	private String _uuid;

}