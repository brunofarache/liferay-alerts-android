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
import android.content.Context;

import android.database.Cursor;

import android.os.Parcel;
import android.os.Parcelable;

import android.text.format.DateUtils;

import com.liferay.alerts.database.UserDAO;

import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class Alert extends BaseModel implements Parcelable {

	public static final String ALERT = "alert";

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

	public static final String PAYLOAD = "payload";

	public static final String TIMESTAMP = "timestamp";

	public static final String TYPE = "type";

	public static final String URL = "url";

	public static final String USER_ID = "userId";

	public Alert(Cursor cursor) {
		_id = cursor.getLong(cursor.getColumnIndex(ID));
		_userId = cursor.getLong(cursor.getColumnIndex(USER_ID));

		try {
			_payload = new JSONObject(
				cursor.getString(cursor.getColumnIndex(PAYLOAD)));
		}
		catch (JSONException je) {
		}

		_timestamp = cursor.getLong(cursor.getColumnIndex(TIMESTAMP));
	}

	public Alert(User user, String payload) throws JSONException {
		_user = user;
		_userId = user.getId();
		_payload = new JSONObject(payload);
		_timestamp = System.currentTimeMillis();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getFormattedTimestamp() {
		String formattedTimestamp;
		long elapsedTime = System.currentTimeMillis() - _timestamp;

		if (elapsedTime < DateUtils.MINUTE_IN_MILLIS) {
			formattedTimestamp = String.format(
				"%ds", TimeUnit.MILLISECONDS.toSeconds(elapsedTime));
		}
		else if (elapsedTime < DateUtils.HOUR_IN_MILLIS) {
			formattedTimestamp = String.format(
				"%dm", TimeUnit.MILLISECONDS.toMinutes(elapsedTime));
		}
		else if (elapsedTime < DateUtils.DAY_IN_MILLIS) {
			formattedTimestamp = String.format(
				"%dh", TimeUnit.MILLISECONDS.toHours(elapsedTime));
		}
		else {
			formattedTimestamp = String.format(
				"%dd", TimeUnit.MILLISECONDS.toDays(elapsedTime));
		}

		return formattedTimestamp;
	}

	@Override
	public long getId() {
		return _id;
	}

	public String getMessage() {
		String message = null;

		try {
			message = _payload.getString(MESSAGE);
		}
		catch (JSONException je) {
		}

		return message;
	}

	public AlertType getType() {
		AlertType type = null;

		try {
			type = AlertType.getType(_payload.getString(TYPE));
		}
		catch (JSONException je) {
			type = AlertType.TEXT;
		}

		return type;
	}

	public String getUrl() {
		String url = null;

		try {
			url = _payload.getString(URL);
		}
		catch (JSONException je) {
		}

		return url;
	}

	public User getUser(Context context) {
		if (_user == null) {
			_user = UserDAO.getInstance(context).get(_userId);
		}

		return _user;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(USER_ID, _userId);
		values.put(PAYLOAD, _payload.toString());
		values.put(TIMESTAMP, _timestamp);

		return values;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(_id);
		parcel.writeParcelable(_user, 0);
		parcel.writeString(_payload.toString());
		parcel.writeLong(_timestamp);
	}

	private Alert(Parcel parcel) {
		_id = parcel.readLong();
		_user = parcel.readParcelable(User.class.getClassLoader());
		_userId = _user.getId();

		try {
			_payload = new JSONObject(parcel.readString());
		}
		catch (JSONException je) {
		}

		_timestamp = parcel.readLong();
	}

	private long _id = -1;
	private JSONObject _payload;
	private long _timestamp;
	private User _user;
	private long _userId;

}