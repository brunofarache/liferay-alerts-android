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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import com.liferay.alerts.model.Alert;
import com.liferay.alerts.util.CharPool;

import org.json.JSONException;
import org.json.JSONObject;

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
		UserDAO.destroy();

		try {
			_instance.close();
		}
		catch (RuntimeException re) {
		}

		context.deleteDatabase(_DATABASE_NAME);
	}

	public void onCreate(SQLiteDatabase database) {
		UserDAO userDAO = new UserDAO();
		AlertDAO alertDAO = new AlertDAO();

		database.execSQL(userDAO.getCreateTableSQL());
		database.execSQL(alertDAO.getCreateTableSQL());
	}

	public void onOpen(SQLiteDatabase database) {
		super.onOpen(database);

		database.execSQL(_FOREIGN_KEYS_ON);
	}

	@Override
	public void onUpgrade(
		SQLiteDatabase database, int oldVersion, int newVersion) {

		if (oldVersion < 2) {
			_upgradeToVersion2(database);
		}

		if (oldVersion < 3) {
			_upgradeToVersion3(database);
		}
	}

	private DatabaseHelper(Context context) {
		super(context, _DATABASE_NAME, null, _DATABASE_VERSION);
	}

	private void _convertMessageToJSONObject(SQLiteDatabase database) {
		StringBuilder select = new StringBuilder();

		select.append("SELECT ");
		select.append(Alert.ID);
		select.append(", ");
		select.append(Alert.PAYLOAD);
		select.append(" FROM ");
		select.append(AlertDAO.TABLE_NAME);

		Cursor cursor = database.rawQuery(select.toString(), null);

		while (cursor.moveToNext()) {
			try {
				long id = cursor.getLong(cursor.getColumnIndex(Alert.ID));
				String payload = cursor.getString(
					cursor.getColumnIndex(Alert.PAYLOAD));

				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Alert.MESSAGE, payload);

				ContentValues values = new ContentValues();
				values.put(Alert.PAYLOAD, jsonObject.toString());

				StringBuilder sb = new StringBuilder();

				sb.append(Alert.ID);
				sb.append(CharPool.SPACE);
				sb.append("= ?");

				String whereClause = sb.toString();

				String[] whereArgs = {
					String.valueOf(id)
				};

				database.update(
					AlertDAO.TABLE_NAME, values, whereClause, whereArgs);
			}
			catch (JSONException je) {
				Log.e(_TAG, "Couldn't convert message.", je);
			}
		}
	}

	private void _renameColumn(
		SQLiteDatabase database, String tableName, String createTableSQL) {

		String temp = "TEMP_" + tableName;

		StringBuilder alter = new StringBuilder();

		alter.append("ALTER TABLE ");
		alter.append(tableName);
		alter.append(" RENAME TO ");
		alter.append(temp);

		database.execSQL(alter.toString());
		database.execSQL(createTableSQL);

		StringBuilder copy = new StringBuilder();

		copy.append("INSERT INTO ");
		copy.append(tableName);
		copy.append(" SELECT * FROM ");
		copy.append(temp);

		database.execSQL(copy.toString());

		StringBuilder drop = new StringBuilder();

		drop.append("DROP TABLE ");
		drop.append(temp);

		database.execSQL(drop.toString());
	}

	private void _upgradeToVersion2(SQLiteDatabase database) {
		_renameColumn(
			database, AlertDAO.TABLE_NAME, new AlertDAO().getCreateTableSQL());

		_convertMessageToJSONObject(database);
	}

	private void _upgradeToVersion3(SQLiteDatabase database) {
		StringBuilder alter = new StringBuilder();

		alter.append("ALTER TABLE ");
		alter.append(AlertDAO.TABLE_NAME);
		alter.append(" ADD COLUMN ");
		alter.append(Alert.READ);
		alter.append(CharPool.SPACE);
		alter.append(TableColumn.INTEGER);
		alter.append(" DEFAULT 0");

		database.execSQL(alter.toString());
	}

	private static final String _DATABASE_NAME = "liferay-alerts.db";

	private static final int _DATABASE_VERSION = 3;

	private static final String _FOREIGN_KEYS_ON = "PRAGMA foreign_keys = ON;";

	private static final String _TAG = DatabaseHelper.class.getSimpleName();

	private static DatabaseHelper _instance;

}