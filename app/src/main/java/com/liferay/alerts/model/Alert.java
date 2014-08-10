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

import java.util.Random;

/**
 * @author Bruno Farache
 */
public class Alert extends BaseModel {

	public static final String ID = "_id";

	public static final String MESSAGE = "message";

	public Alert(Cursor cursor) {
		_id = cursor.getLong(cursor.getColumnIndex(ID));
		_message = cursor.getString(cursor.getColumnIndex(MESSAGE));
	}

	public Alert(String message) {
		_message = message;

		Random random = new Random();
		_id = random.nextLong();
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

		values.put(ID, getId());
		values.put(MESSAGE, getMessage());

		return values;
	}

	private long _id = -1;
	private String _message;

}