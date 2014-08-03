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

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.liferay.alerts.R;
import com.liferay.alerts.activity.MainActivity;
import com.liferay.alerts.util.PushNotificationsUtil;
import com.liferay.alerts.util.SettingsUtil;
import com.liferay.alerts.util.ToastUtil;

/**
 * @author Bruno Farache
 */
public class GCMRegistrationAsyncTask extends AsyncTask<Void, Void, String> {

	public GCMRegistrationAsyncTask(MainActivity activity) {
		_context = activity.getApplicationContext();
	}

	public String doInBackground(Void... params) {
		String token = null;

		try {
			GoogleCloudMessaging gcm =
				PushNotificationsUtil.getGoogleCloudMessaging(_context);

			token = gcm.register(_SENDER_ID);
			SettingsUtil.setToken(token);
		}
		catch (Exception e) {
			ToastUtil.show(
				_context, R.string.failed_to_get_token_from_google, true);
		}

		return token;
	}

	@Override
	public void onPostExecute(String token) {
		if (token != null) {
			PushNotificationsUtil.register(_context, token);
		}
	}

	private static final String _SENDER_ID = "248816535314";

	private Context _context;

}