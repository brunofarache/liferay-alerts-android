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

package com.liferay.alerts.test;

import android.content.Context;

import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.AlertType;
import com.liferay.alerts.model.User;
import com.liferay.alerts.util.NotificationUtil;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class AlertTest {

	public static List<Alert> getAlerts() {
		List<Alert> alerts = new ArrayList<Alert>();

		User user = getUser();

		alerts.add(getTextAlert(user, "first message"));
		alerts.add(getTextAlert(user, "second message"));

		return alerts;
	}

	public static Alert getTextAlert(User user, String message) {
		Alert alert = null;

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(Alert.MESSAGE, message);
			jsonObject.put(Alert.TYPE, AlertType.TEXT);

			alert = new Alert(user, jsonObject.toString());
		}
		catch (JSONException je) {
		}

		return alert;
	}

	public static User getUser() {
		User user = null;

		try {
			JSONObject jsonObject = new JSONObject();

			jsonObject.put("userId", 10199);
			jsonObject.put(User.UUID, "97ea1309-ec04-63bd-5592-76f8c5b4b45b");
			jsonObject.put(User.FULL_NAME, "Bruno Farache");
			jsonObject.put(User.PORTRAIT_ID, 0);

			user = new User(jsonObject.toString());
		}
		catch (JSONException je) {
		}

		return user;
	}

	public static void notify(final Context context) {
		NotificationUtil.notify(context, getAlerts());
	}

}