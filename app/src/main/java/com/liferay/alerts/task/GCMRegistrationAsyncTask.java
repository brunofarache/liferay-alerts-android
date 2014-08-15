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

package com.liferay.alerts.task;

import android.content.Context;

import android.os.AsyncTask;

import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.liferay.alerts.util.GCMUtil;
import com.liferay.alerts.util.SettingsUtil;

/**
 * @author Bruno Farache
 */
public class GCMRegistrationAsyncTask extends AsyncTask<Void, Void, String> {

	public GCMRegistrationAsyncTask(Context context) {
		_context = context.getApplicationContext();
	}

	public String doInBackground(Void... params) {
		String token = null;

		try {
			GoogleCloudMessaging gcm = GCMUtil.getGoogleCloudMessaging(
				_context);

			token = gcm.register(_SENDER_ID);
			SettingsUtil.setToken(token);
		}
		catch (Exception e) {
			Log.e(_TAG, "Failed to get token from Google", e);
		}

		return token;
	}

	@Override
	public void onPostExecute(String token) {
		if (token != null) {
			GCMUtil.addPushNotificationsDevice(_context, token);
		}
	}

	private static final String _SENDER_ID = "248816535314";

	private static final String _TAG =
		GCMRegistrationAsyncTask.class.getSimpleName();

	private Context _context;

}