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
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Color;

import android.net.Uri;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.support.v4.app.NotificationCompat.Style;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.app.NotificationManagerCompat;

import android.util.Log;

import com.liferay.alerts.R;
import com.liferay.alerts.activity.MainActivity;
import com.liferay.alerts.database.AlertDAO;
import com.liferay.alerts.database.DatabaseException;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.AlertType;
import com.liferay.alerts.model.User;
import com.liferay.alerts.receiver.WearableVoteReceiver;

import java.io.IOException;

import java.util.List;

/**
 * @author Bruno Farache
 */
public class NotificationUtil {

	public static final int ALERTS_ID = 1;

	public static final String GROUP = "GROUP";

	public static final int MAX_ALERTS = 7;

	public static void cancel(Context context) {
		_getNotificationManager(context).cancel(ALERTS_ID);

		try {
			AlertDAO.getInstance(context).markAllAsRead();
		}
		catch (DatabaseException de) {
			Log.e(_TAG, "Couldn't mark alerts as read", de);
		}
	}

	public static void notify(Context context, List<Alert> alerts) {
		if ((alerts == null) || (alerts.size() == 0)) {
			return;
		}

		Notification notification = _buildNotification(context, alerts);
		_getNotificationManager(context).notify(ALERTS_ID, notification);
	}

	public static void notifyUnreadAlerts(Context context) {
		AlertDAO dao = AlertDAO.getInstance(context);
		List<Alert> alerts = dao.getUnread();

		notify(context, alerts);
	}

	protected static void addOpenInBrowserAction(
		Context context, Alert alert, Builder builder) {

		AlertType type = alert.getType();

		if ((type == AlertType.IMAGE) || (type == AlertType.LINK)) {
			String url = alert.getUrl();

			PendingIntent pendingIntent = PendingIntent.getActivity(
				context, 0, new Intent(Intent.ACTION_VIEW, Uri.parse(url)),
				PendingIntent.FLAG_UPDATE_CURRENT);

			builder.addAction(
				R.drawable.action_open_in,
				context.getString(R.string.open_in_browser), pendingIntent);
		}
	}

	private static Notification _buildNotification(
		Context context, List<Alert> alerts) {

		PendingIntent intent = PendingIntent.getActivity(
			context, 0, new Intent(context, MainActivity.class),
			PendingIntent.FLAG_UPDATE_CURRENT);

		String title;
		Alert alert = alerts.get(0);
		boolean singleUser = _fromSingleUser(alerts);
		int size = alerts.size();
		String summary = size + " " + context.getString(R.string.new_alerts);

		Builder builder = new NotificationCompat.Builder(context);

		if (singleUser) {
			builder.setNumber(size);

			User user = alert.getUser(context);
			_setPortrait(context, builder, user);
			title = user.getFullName();
		}
		else {
			title = summary;
		}

		builder.setAutoCancel(true);
		builder.setContentIntent(intent);
		builder.setContentTitle(title);
		builder.setSmallIcon(R.drawable.launcher_small);
		builder.setGroup(GROUP);
		builder.setGroupSummary(true);

		Style style;

		if (size == 1) {
			style = _getBigTextStyle(builder, alert);

			builder.setDefaults(Notification.DEFAULT_VIBRATE);
			builder.setLights(Color.YELLOW, 1000, 1000);

			addOpenInBrowserAction(context, alert, builder);
			WearableVoteReceiver.addPollsActions(context, alerts, builder);
		}
		else {
			style = _getInboxStyle(
				context, builder, alerts, summary, singleUser);
		}

		builder.setStyle(style);

		return builder.build();
	}

	private static boolean _fromSingleUser(List<Alert> alerts) {
		long userId = alerts.get(0).getUserId();

		for (int i = 1; i < alerts.size(); i++) {
			if (userId != alerts.get(i).getUserId()) {
				return false;
			}
		}

		return true;
	}

	private static Style _getBigTextStyle(Builder builder, Alert alert) {
		String message = alert.getMessage();
		builder.setContentText(message);

		return new NotificationCompat.BigTextStyle().bigText(message);
	}

	private static Style _getInboxStyle(
		Context context, Builder builder, List<Alert> alerts, String summary,
		boolean singleUser) {

		int size = alerts.size();

		InboxStyle style = new NotificationCompat.InboxStyle();

		int end = 0;

		if (size > MAX_ALERTS) {
			end = size - MAX_ALERTS;
		}

		for (int i = (size - 1); i >= end; i--) {
			Alert alert = alerts.get(i);
			String message = alert.getMessage();

			if (!singleUser) {
				User user = alert.getUser(context);
				message = user.getFullName() + ": " + message;
			}

			if (i == (size - 1)) {
				builder.setContentText(message);
			}

			style.addLine(message);
		}

		style.setSummaryText(summary);

		return style;
	}

	private static NotificationManagerCompat _getNotificationManager(
		Context context) {

		return NotificationManagerCompat.from(context);
	}

	private static void _setPortrait(
		Context context, Builder builder, User user) {

		try {
			Bitmap largeIcon = PortraitUtil.getPortrait(context, user);
			builder.setLargeIcon(largeIcon);

			WearableExtender extender = new WearableExtender();
			builder.extend(extender.setBackground(largeIcon));
		}
		catch (IOException ioe) {
			Log.e(_TAG, "Couldn't set user's portrait to notification", ioe);
		}
	}

	private static final String _TAG = NotificationUtil.class.getSimpleName();

}