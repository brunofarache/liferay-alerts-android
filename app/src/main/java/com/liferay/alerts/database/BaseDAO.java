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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.liferay.alerts.model.BaseModel;
import com.liferay.alerts.util.CharPool;
import com.liferay.alerts.util.StringPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Silvio Santos
 * @author Bruno Farache
 */
public abstract class BaseDAO<M extends BaseModel> {

	public static final String NOT_IN = "NOT IN";

	public void delete(long id) throws DatabaseException {
		String[] values = { String.valueOf(id) };

		delete(getIdWhereClause(), values, true);
	}

	public ArrayList<M> get() {
		return get(StringPool.BLANK);
	}

	public M get(long id) {
		ArrayList<M> models = get(getIdWhereClause(), String.valueOf(id));

		if (models.isEmpty()) {
			return null;
		}

		return models.get(0);
	}

	public String getCreateTableSQL() {
		StringBuilder sb = new StringBuilder();

		sb.append("CREATE TABLE IF NOT EXISTS");
		sb.append(CharPool.SPACE);
		sb.append(getTableName());
		sb.append(CharPool.SPACE);

		sb.append(CharPool.OPEN_PARENTHESIS);

		TableColumn[] columns = getTableColumns();

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = columns[i];

			sb.append(getComma(i));
			sb.append(column.toString());
		}

		sb.append(CharPool.CLOSE_PARENTHESIS);

		return sb.toString();
	}

	public void insert(List<M> models, boolean commit)
		throws DatabaseException {

		SQLiteDatabase database = helper.getWritableDatabase();

		if (commit) {
			database.beginTransaction();
		}

		try {
			for (M model : models) {
				ContentValues values = model.toContentValues();

				database.insertOrThrow(getTableName(), null, values);
			}

			if (commit) {
				database.setTransactionSuccessful();
			}
		}
		catch (Exception e) {
			throw new DatabaseException("Couldn't insert models", e);
		}
		finally {
			if (commit) {
				database.endTransaction();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void insert(M model) throws DatabaseException {
		insert(Arrays.asList(model), true);
	}

	public void insertOrUpdate(M model, ContentValues values, boolean commit)
		throws DatabaseException {

		SQLiteDatabase database = helper.getWritableDatabase();

		if (commit) {
			database.beginTransaction();
		}

		try {
			String tableName = getTableName();
			String[] id = { String.valueOf(model.getId()) };

			int rowsCount = database.update(
				tableName, values, getIdWhereClause(), id);

			if (rowsCount == 0) {
				database.insertOrThrow(tableName, null, values);
			}

			if (commit) {
				database.setTransactionSuccessful();
			}
		}
		catch (Exception e) {
			throw new DatabaseException("Couldn't insert models", e);
		}
		finally {
			if (commit) {
				database.endTransaction();
			}
		}
	}

	protected void delete(String where, String[] values, boolean commit)
		throws DatabaseException {

		SQLiteDatabase database = helper.getWritableDatabase();

		if (commit) {
			database.beginTransaction();
		}

		try {
			database.delete(getTableName(), where, values);

			if (commit) {
				database.setTransactionSuccessful();
			}
		}
		catch (Exception e) {
			throw new DatabaseException("Couldn't delete models", e);
		}
		finally {
			if (commit) {
				database.endTransaction();
			}
		}
	}

	protected abstract M fromCursor(Cursor cursor);

	protected ArrayList<M> get(String where, String... values) {
		ArrayList<M> models = new ArrayList<M>();

		SQLiteDatabase database = helper.getWritableDatabase();

		Cursor cursor = database.query(
			getTableName(), getTableColumnNames(), where, values, null, null,
			null);

		while (cursor.moveToNext()) {
			M model = fromCursor(cursor);

			models.add(model);
		}

		cursor.close();

		return models;
	}

	protected String getComma(int index) {
		if (index == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		sb.append(CharPool.COMMA);
		sb.append(CharPool.SPACE);

		return sb.toString();
	}

	protected String getIdNotInWhereClause() {
		if (_idNotInWhereClause == null) {
			StringBuilder sb = new StringBuilder();

			sb.append(BaseModel.ID);
			sb.append(CharPool.SPACE);
			sb.append(NOT_IN);

			_idNotInWhereClause = sb.toString();
		}

		return _idNotInWhereClause;
	}

	protected String getIdWhereClause() {
		if (_idWhereClause == null) {
			StringBuilder sb = new StringBuilder();

			sb.append(BaseModel.ID);
			sb.append(CharPool.SPACE);
			sb.append("= ?");

			_idWhereClause = sb.toString();
		}

		return _idWhereClause;
	}

	protected String getParams(int length) {
		StringBuilder sb = new StringBuilder();

		sb.append(CharPool.OPEN_PARENTHESIS);

		for (int i = 0; i < length; i++) {
			sb.append(getComma(i));
			sb.append(CharPool.QUESTION);
		}

		sb.append(CharPool.CLOSE_PARENTHESIS);

		return sb.toString();
	}

	protected String[] getTableColumnNames() {
		TableColumn[] columns = getTableColumns();
		String[] names = new String[columns.length];

		for (int i = 0; i < columns.length; i++) {
			names[i] = columns[i].getName();
		}

		return names;
	}

	protected abstract TableColumn[] getTableColumns();

	protected abstract String getTableName();

	protected void update(ContentValues values, boolean commit)
		throws DatabaseException {

		update(null, values, commit);
	}

	protected void update(Long modelId, ContentValues values, boolean commit)
		throws DatabaseException {

		SQLiteDatabase database = helper.getWritableDatabase();

		if (commit) {
			database.beginTransaction();
		}

		try {
			String[] id = null;
			String idWhereClause = null;

			if (modelId != null) {
				id = new String[]{ String.valueOf(modelId) };
				idWhereClause = getIdWhereClause();
			}

			database.update(getTableName(), values, idWhereClause, id);

			if (commit) {
				database.setTransactionSuccessful();
			}
		}
		catch (Exception e) {
			throw new DatabaseException("Couldn't update model", e);
		}
		finally {
			if (commit) {
				database.endTransaction();
			}
		}
	}

	protected DatabaseHelper helper;

	private String _idNotInWhereClause;
	private String _idWhereClause;

}