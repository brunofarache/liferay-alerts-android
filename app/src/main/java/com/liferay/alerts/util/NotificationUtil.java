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

import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;

import android.support.v4.app.NotificationCompat;

import com.liferay.alerts.R;
import com.liferay.alerts.activity.MainActivity;
import com.liferay.alerts.database.AlertDAO;
import com.liferay.alerts.database.DatabaseException;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.User;

import java.io.IOException;

import java.util.List;

/**
 * @author Bruno Farache
 */
public class NotificationUtil {

	public static final int ALERTS_ID = 1;

	public static void cancel(Context context) throws DatabaseException {
		_getManager(context).cancel(ALERTS_ID);
		AlertDAO.getInstance(context).markAllAsRead();
	}

	public static void notify(Context context, User user, Alert alert) {
		String message = alert.getMessage();
		String fullName = user.getFullName();
		Bitmap largeIcon = null;

		try {
			largeIcon = PortraitUtil.getPortrait(context, user);
		}
		catch (IOException ioe) {
		}

		PendingIntent intent = PendingIntent.getActivity(
			context, 0, new Intent(context, MainActivity.class),
			PendingIntent.FLAG_UPDATE_CURRENT);

		List<Alert> alerts = AlertDAO.getInstance(context).getUnread();

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
			context);

		builder.setContentIntent(intent);
		builder.setContentText(message);
		builder.setContentTitle(fullName);

		if (largeIcon != null) {
			builder.setLargeIcon(largeIcon);
		}

		builder.setSmallIcon(R.drawable.launcher_small);
		builder.setStyle(
			new NotificationCompat.BigTextStyle().bigText(message));

		_getManager(context).notify(ALERTS_ID, builder.build());
	}

	private static NotificationManager _getManager(Context context) {
		return (NotificationManager)context.getSystemService(
			Context.NOTIFICATION_SERVICE);
	}

}