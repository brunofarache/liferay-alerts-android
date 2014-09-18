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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.liferay.alerts.model.Alert;

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

		if (newVersion == 2) {
			_upgradeToVersion2(database);
		}
	}

	private DatabaseHelper(Context context) {
		super(context, _DATABASE_NAME, null, _DATABASE_VERSION);
	}

	private void _upgradeToVersion2(SQLiteDatabase database) {
		StringBuilder alterTable = new StringBuilder();

		alterTable.append("ALTER TABLE ");
		alterTable.append(AlertDAO.TABLE_NAME);
		alterTable.append(" RENAME TO ");
		alterTable.append("TEMP_" + AlertDAO.TABLE_NAME);

		database.execSQL(alterTable.toString());
		database.execSQL(new AlertDAO().getCreateTableSQL());

		StringBuilder copyValues = new StringBuilder();

		copyValues.append("INSERT INTO ");
		copyValues.append(AlertDAO.TABLE_NAME);
		copyValues.append(" SELECT * FROM ");
		copyValues.append("TEMP_" + AlertDAO.TABLE_NAME);

		database.execSQL(copyValues.toString());

		StringBuilder drop = new StringBuilder();

		drop.append("DROP TABLE ");
		drop.append("TEMP_" + AlertDAO.TABLE_NAME);

		database.execSQL(drop.toString());

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

				StringBuilder update = new StringBuilder();

				update.append("UPDATE ");
				update.append(AlertDAO.TABLE_NAME);
				update.append(" SET ");
				update.append(Alert.PAYLOAD);
				update.append(" = '");
				update.append(jsonObject.toString());
				update.append("' WHERE ");
				update.append(Alert.ID);
				update.append(" = ");
				update.append(id);

				database.execSQL(update.toString());
			}
			catch (JSONException je) {
				je.printStackTrace();
			}
		}
	}

	private static final String _DATABASE_NAME = "liferay-alerts.db";

	private static final int _DATABASE_VERSION = 2;

	private static final String _FOREIGN_KEYS_ON = "PRAGMA foreign_keys = ON;";

	private static DatabaseHelper _instance;

}