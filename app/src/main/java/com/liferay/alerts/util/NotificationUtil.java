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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

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

	public static void cancel(Context context) {
		_getNotificationManager(context).cancel(ALERTS_ID);

		try {
			AlertDAO.getInstance(context).markAllAsRead();
		}
		catch (DatabaseException de) {
		}
	}

	public static void notify(Context context, Alert alert) {
		List<Alert> alerts = AlertDAO.getInstance(context).getUnread();

		if ((alerts == null) || (alerts.size() == 0)) {
			return;
		}

		int size = alerts.size();
		Notification notification = null;

		if (size == 1) {
			notification = _buildSingleNotification(context, alert);
		}
		else if (size > 1) {
			notification = _buildGroupedNotification(context, alerts);
		}

		_getNotificationManager(context).notify(ALERTS_ID, notification);
	}

	private static Notification _buildGroupedNotification(
		Context context, List<Alert> alerts) {

		boolean sameUser = _sameUser(alerts);
		Alert alert = alerts.get(0);
		String message = alert.getMessage();
		User user = alert.getUser(context);
		String title;

		if (sameUser) {
			title = user.getFullName();
		}
		else {
			title = alerts.size() + CharPool.SPACE + context.getString(
				R.string.new_alerts);
		}

		PendingIntent intent = PendingIntent.getActivity(
			context, 0, new Intent(context, MainActivity.class),
			PendingIntent.FLAG_UPDATE_CURRENT);

		Builder builder = new NotificationCompat.Builder(context);

		builder.setContentIntent(intent);
		builder.setContentText(message);
		builder.setContentTitle(title);

		builder.setSmallIcon(R.drawable.launcher_small);

		if (sameUser) {
			_setPortrait(context, builder, user);
		}

		builder.setStyle(
			new NotificationCompat.BigTextStyle().bigText(message));

		return builder.build();
	}

	private static Notification _buildSingleNotification(
		Context context, Alert alert) {

		String message = alert.getMessage();
		User user = alert.getUser(context);
		String title = user.getFullName();

		PendingIntent intent = PendingIntent.getActivity(
			context, 0, new Intent(context, MainActivity.class),
			PendingIntent.FLAG_UPDATE_CURRENT);

		Builder builder = new NotificationCompat.Builder(context);

		builder.setContentIntent(intent);
		builder.setContentText(message);
		builder.setContentTitle(title);

		_setPortrait(context, builder, user);

		builder.setSmallIcon(R.drawable.launcher_small);
		builder.setStyle(
			new NotificationCompat.BigTextStyle().bigText(message));

		return builder.build();
	}

	private static NotificationManager _getNotificationManager(
		Context context) {

		return (NotificationManager)context.getSystemService(
			Context.NOTIFICATION_SERVICE);
	}

	private static boolean _sameUser(List<Alert> alerts) {
		long userId = alerts.get(0).getUserId();

		for (int i = 1; i < alerts.size(); i++) {
			if (userId != alerts.get(i).getUserId()) {
				return false;
			}
		}

		return true;
	}

	private static void _setPortrait(
		Context context, Builder builder, User user) {

		try {
			Bitmap largeIcon = PortraitUtil.getPortrait(context, user);
			builder.setLargeIcon(largeIcon);
		}
		catch (IOException ioe) {
		}
	}

}