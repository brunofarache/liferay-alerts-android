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

package com.liferay.alerts.service;

import android.app.IntentService;

import android.content.Intent;

import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;

import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.liferay.alerts.activity.MainActivity;
import com.liferay.alerts.database.AlertDAO;
import com.liferay.alerts.database.DatabaseException;
import com.liferay.alerts.database.UserDAO;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.User;
import com.liferay.alerts.receiver.PushNotificationReceiver;
import com.liferay.alerts.util.GCMUtil;
import com.liferay.alerts.util.NotificationUtil;

import org.json.JSONException;

/**
 * @author Bruno Farache
 */
public class PushNotificationService extends IntentService {

	public PushNotificationService() {
		super(PushNotificationService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GoogleCloudMessaging gcm = GCMUtil.getGoogleCloudMessaging(this);

		String type = gcm.getMessageType(intent);
		Bundle extras = intent.getExtras();

		if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(type) &&
			!extras.isEmpty()) {

			try {
				User user = new User(extras.getString("fromUser"));
				Alert alert = new Alert(user, extras.getString(Alert.PAYLOAD));

				_insert(user, alert);
				NotificationUtil.notify(this, user, alert);
				_addCard(alert);
			}
			catch (JSONException je) {
			}
		}

		PushNotificationReceiver.completeWakefulIntent(intent);
	}

	private void _addCard(Alert alert) {
		Intent intent = new Intent(MainActivity.ADD_CARD);
		intent.putExtra(Alert.ALERT, alert);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void _insert(User user, Alert alert) {
		try {
			UserDAO userDAO = UserDAO.getInstance(this);
			User existingUser = userDAO.get(user.getId());

			if (existingUser == null) {
				userDAO.insert(user);
			}

			AlertDAO.getInstance(this).insert(alert);
		}
		catch (DatabaseException de) {
			Log.e(_TAG, "Couldn't insert user or alert.", de);
		}
	}

	private static final String _TAG =
		PushNotificationService.class.getSimpleName();

}