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

package com.liferay.alerts.database;

import android.content.Context;

import android.database.Cursor;

import com.liferay.alerts.model.User;

/**
 * @author Bruno Farache
 */
public class UserDAO extends BaseDAO<User> {

	public static final String TABLE_NAME = "User";

	public synchronized static void destroy() {
		_instance = null;
	}

	public synchronized static UserDAO getInstance(Context context) {
		if (_instance == null) {
			_instance = new UserDAO();
			_instance.helper = DatabaseHelper.getInstance(context);
		}

		return _instance;
	}

	@Override
	protected User fromCursor(Cursor cursor) {
		return new User(cursor);
	}

	@Override
	protected TableColumn[] getTableColumns() {
		return _TABLE_COLUMNS;
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	private static final TableColumn[] _TABLE_COLUMNS = {
		new TableColumn(User.ID, TableColumn.INTEGER, true),
		new TableColumn(User.UUID, TableColumn.TEXT),
		new TableColumn(User.FULL_NAME, TableColumn.TEXT),
		new TableColumn(User.PORTRAIT_ID, TableColumn.INTEGER)
	};

	private static UserDAO _instance;

}