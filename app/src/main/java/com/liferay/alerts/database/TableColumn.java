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

import com.liferay.alerts.util.CharPool;

/**
 * @author Bruno Farache
 */
public class TableColumn {

	public static final String BLOB = "BLOB";

	public static final String FOREIGN_KEY = "FOREIGN KEY";

	public static final String INTEGER = "INTEGER";

	public static final String ON_DELETE_CASCADE = "ON DELETE CASCADE";

	public static final String PRIMARY_KEY = "PRIMARY KEY";

	public static final String REAL = "REAL";

	public static final String REFERENCES = "REFERENCES";

	public static final String TEXT = "TEXT";

	public TableColumn(String name, String type) {
		this(name, type, false);
	}

	public TableColumn(String name, String type, boolean primaryKey) {
		this(name, type, primaryKey, null, null);
	}

	public TableColumn(
		String name, String type, boolean primaryKey, String foreignTable,
		String foreignColumn) {

		_name = name;
		_type = type;
		_primaryKey = primaryKey;
		_foreignTable = foreignTable;
		_foreignColumn = foreignColumn;
	}

	public TableColumn(
		String name, String type, String foreignTable, String foreignColumn) {

		this(name, type, false, foreignTable, foreignColumn);
	}

	public String getName() {
		return _name;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(_name);
		sb.append(CharPool.SPACE);
		sb.append(_type);

		if (_primaryKey) {
			sb.append(CharPool.SPACE);
			sb.append(PRIMARY_KEY);
		}
		else if (_foreignTable != null) {
			sb.append(CharPool.COMMA);
			sb.append(CharPool.SPACE);

			sb.append(FOREIGN_KEY);
			sb.append(CharPool.OPEN_PARENTHESIS);
			sb.append(_name);
			sb.append(CharPool.CLOSE_PARENTHESIS);

			sb.append(CharPool.SPACE);
			sb.append(REFERENCES);
			sb.append(CharPool.SPACE);
			sb.append(_foreignTable);
			sb.append(CharPool.OPEN_PARENTHESIS);
			sb.append(_foreignColumn);
			sb.append(CharPool.CLOSE_PARENTHESIS);

			sb.append(CharPool.SPACE);
			sb.append(ON_DELETE_CASCADE);
		}

		return sb.toString();
	}

	private String _foreignColumn;
	private String _foreignTable;
	private String _name;
	private boolean _primaryKey;
	private String _type;

}