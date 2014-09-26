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

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;

import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.User;
import com.liferay.alerts.util.CharPool;

import java.util.ArrayList;

/**
 * @author Bruno Farache
 */
public class AlertDAO extends BaseDAO<Alert> {

	public static final String TABLE_NAME = "Alert";

	public synchronized static void destroy() {
		_instance = null;
	}

	public synchronized static AlertDAO getInstance(Context context) {
		if (_instance == null) {
			_instance = new AlertDAO();
			_instance.helper = DatabaseHelper.getInstance(context);
		}

		return _instance;
	}

	public ArrayList<Alert> getUnread() {
		return get(getReadClause(), String.valueOf(0));
	}

	public void markAllAsRead() throws DatabaseException {
		ContentValues values = new ContentValues();
		values.put(Alert.READ, 1);

		update(values, true);
	}

	@Override
	protected Alert fromCursor(Cursor cursor) {
		return new Alert(cursor);
	}

	protected String getReadClause() {
		if (_readWhereClause == null) {
			StringBuilder sb = new StringBuilder();

			sb.append(Alert.READ);
			sb.append(CharPool.SPACE);
			sb.append("= ?");

			_readWhereClause = sb.toString();
		}

		return _readWhereClause;
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
		new TableColumn(Alert.ID, TableColumn.INTEGER, true, true),
		new TableColumn(Alert.PAYLOAD, TableColumn.TEXT),
		new TableColumn(Alert.READ, TableColumn.INTEGER),
		new TableColumn(Alert.TIMESTAMP, TableColumn.INTEGER),
		new TableColumn(
			Alert.USER_ID, TableColumn.INTEGER, UserDAO.TABLE_NAME, User.ID)
	};

	private static AlertDAO _instance;

	private String _readWhereClause;

}