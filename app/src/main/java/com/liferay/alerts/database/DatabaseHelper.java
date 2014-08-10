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

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Silvio Santos
 * @author Bruno Farache
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	public static DatabaseHelper getInstance(Context context) {
		if (_instance == null) {
			_instance = new DatabaseHelper(context.getApplicationContext());
		}

		return _instance;
	}

	public void deleteDatabase(Context context) {
		AlertDAO.destroy();

		try {
			_instance.close();
		}
		catch (RuntimeException re) {
		}

		context.deleteDatabase(_DATABASE_NAME);
	}

	public void onCreate(SQLiteDatabase database) {
		AlertDAO alertDAO = new AlertDAO();

		database.execSQL(alertDAO.getCreateTableSQL());
	}

	public void onOpen(SQLiteDatabase database) {
		super.onOpen(database);

		database.execSQL(_FOREIGN_KEYS_ON);
	}

	@Override
	public void onUpgrade(
		SQLiteDatabase database, int oldVersion, int newVersion) {
	}

	private DatabaseHelper(Context context) {
		super(context, _DATABASE_NAME, null, _DATABASE_VERSION);
	}

	private static final String _DATABASE_NAME = "liferay-alerts.db";

	private static final int _DATABASE_VERSION = 1;

	private static final String _FOREIGN_KEYS_ON = "PRAGMA foreign_keys = ON;";

	private static DatabaseHelper _instance;

}