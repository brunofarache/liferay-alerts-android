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
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.liferay.alerts.R;
import com.liferay.alerts.activity.SignInActivity;
import com.liferay.alerts.receiver.PushNotificationReceiver;
import com.liferay.alerts.util.PushNotificationsUtil;

/**
 * @author Bruno Farache
 */
public class PushNotificationService extends IntentService {

	public PushNotificationService() {
		super(PushNotificationService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GoogleCloudMessaging gcm =
			PushNotificationsUtil.getGoogleCloudMessaging(this);

		String type = gcm.getMessageType(intent);
		Bundle extras = intent.getExtras();

		if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(type) &&
			!extras.isEmpty()) {

			String message = extras.getString("data");
			_showNotification(message);
		}

		PushNotificationReceiver.completeWakefulIntent(intent);
	}

	private void _showNotification(String message) {
		NotificationManager manager = (NotificationManager)getSystemService(
			Context.NOTIFICATION_SERVICE);

		PendingIntent intent = PendingIntent.getActivity(
			this, 0, new Intent(this, SignInActivity.class), 0);

		Builder builder = new Builder(this);

		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(getString(R.string.app_name));
		builder.setStyle(new BigTextStyle().bigText(message));
		builder.setContentText(message);
		builder.setContentIntent(intent);

		manager.notify(_NOTIFICATION_ID, builder.build());
	}

	private static final int _NOTIFICATION_ID = 1;

}