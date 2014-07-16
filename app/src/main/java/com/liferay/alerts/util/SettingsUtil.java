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

package com.liferay.alerts.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.preference.PreferenceManager;

import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.service.SessionImpl;

/**
 * @author Bruno Farache
 */
public class SettingsUtil {

	public static String getPassword() {
		return _preferences.getString(_PASSWORD, "test");
	}

	public static String getServer() {
		return _preferences.getString(_SERVER, "http://10.0.2.2:8080");
	}

	public static Session getSession() {
		return new SessionImpl(getServer(), getUsername(), getPassword());
	}

	public static String getToken() {
		return _preferences.getString(_TOKEN, "");
	}

	public static String getUsername() {
		return _preferences.getString(_USERNAME, "test");
	}

	public static void init(Context context) {
		_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static void setCredentials(
		String username, String password, String server) {

		Editor editor = _preferences.edit();
		editor.putString(_USERNAME, username);
		editor.putString(_PASSWORD, password);
		editor.putString(_SERVER, server);
		editor.commit();
	}

	public static void setToken(String registrationId) {
		Editor editor = _preferences.edit();
		editor.putString(_TOKEN, registrationId);
		editor.commit();
	}

	private static final String _PASSWORD = "password";

	private static final String _SERVER = "server";

	private static final String _TOKEN = "token";

	private static final String _USERNAME = "username";

	private static SharedPreferences _preferences;

}