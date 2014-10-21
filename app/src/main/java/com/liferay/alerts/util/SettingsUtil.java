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

import com.liferay.alerts.R;

/**
 * @author Bruno Farache
 */
public class SettingsUtil {

	public static String getEmail(Context context) {
		return context.getString(R.string.email);
	}

	public static String getPassword(Context context) {
		return context.getString(R.string.password);
	}

	public static String getServer(Context context) {
		return context.getString(R.string.server);
	}

	public static String getToken() {
		return _preferences.getString(_TOKEN, StringPool.BLANK);
	}

	public static void init(Context context) {
		_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static boolean isRegistered() {
		return _preferences.getBoolean(_REGISTERED, false);
	}

	public static void setRegistered(boolean registered) {
		Editor editor = _preferences.edit();
		editor.putBoolean(_REGISTERED, registered);
		editor.commit();
	}

	public static void setToken(String registrationId) {
		Editor editor = _preferences.edit();
		editor.putString(_TOKEN, registrationId);
		editor.commit();
	}

	private static final String _REGISTERED = "registered";

	private static final String _TOKEN = "token";

	private static SharedPreferences _preferences;

}