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

import android.widget.Toast;

/**
 * @author Bruno Farache
 */
public class ToastUtil {

	public static void show(Context context, CharSequence message) {
		show(context, message, false);
	}

	public static void show(
		Context context, CharSequence message, boolean longDuration) {

		int duration = Toast.LENGTH_SHORT;

		if (longDuration) {
			duration = Toast.LENGTH_LONG;
		}

		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}

	public static void show(Context context, int messageId) {
		show(context, messageId, false);
	}

	public static void show(
		Context context, int messageId, boolean longDuration) {

		CharSequence text = context.getResources().getText(messageId);
		show(context, text, longDuration);
	}

}