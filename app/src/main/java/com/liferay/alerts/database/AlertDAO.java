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

import com.liferay.alerts.model.Alert;

/**
 * @author Bruno Farache
 */
public class AlertDAO extends BaseDAO<Alert> {

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

	@Override
	protected Alert fromCursor(Cursor cursor) {
		return new Alert(cursor);
	}

	@Override
	protected TableColumn[] getTableColumns() {
		return _TABLE_COLUMNS;
	}

	@Override
	protected String getTableName() {
		return _TABLE_NAME;
	}

	private static final TableColumn[] _TABLE_COLUMNS = {
		new TableColumn(Alert.ID, TableColumn.INTEGER, true),
		new TableColumn(Alert.MESSAGE, TableColumn.TEXT)
	};

	private static final String _TABLE_NAME = "Alert";

	private static AlertDAO _instance;

}