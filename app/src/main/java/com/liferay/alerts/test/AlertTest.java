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
import com.liferay.alerts.model.PollsChoice;
import com.liferay.alerts.model.PollsQuestion;
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

	public static User getBruno() {
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

	public static Alert getPollsAlert(User user, String message) {
		Alert alert = null;

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(Alert.MESSAGE, message);
			jsonObject.put(Alert.TYPE, AlertType.POLLS);

			List<PollsChoice> choices = new ArrayList<PollsChoice>();

			choices.add(new PollsChoice(11408, "yes", false));
			choices.add(new PollsChoice(11408, "no", false));

			PollsQuestion question = new PollsQuestion(11406, choices);

			alert = new Alert(user, jsonObject.toString());
			alert.setPollsQuestion(question.toJSONObject(0));
		}
		catch (JSONException je) {
		}

		return alert;
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

	public static User getZeno() {
		User user = null;

		try {
			JSONObject jsonObject = new JSONObject();

			jsonObject.put("userId", 10200);
			jsonObject.put(User.UUID, "97ea1309-ec04-63bd-5592-76f8c5b4b45b");
			jsonObject.put(User.FULL_NAME, "Zeno Rocha");
			jsonObject.put(User.PORTRAIT_ID, 0);

			user = new User(jsonObject.toString());
		}
		catch (JSONException je) {
		}

		return user;
	}

	public static void notifyMultipleUsers(Context context) {
		User bruno = getBruno();
		User zeno = getZeno();

		List<Alert> alerts = new ArrayList<Alert>();

		alerts.add(getTextAlert(bruno, "one"));
		alerts.add(getTextAlert(zeno, "two"));
		alerts.add(getTextAlert(bruno, "three"));
		alerts.add(getTextAlert(zeno, "four"));
		alerts.add(getTextAlert(bruno, "five"));
		alerts.add(getTextAlert(zeno, "six"));
		alerts.add(getTextAlert(bruno, "seven"));
		alerts.add(getTextAlert(zeno, "eight"));

		notify(context, alerts);
	}

	public static void notifyPollsAlert(Context context) {
		User user = getBruno();

		List<Alert> alerts = new ArrayList<Alert>();
		alerts.add(getPollsAlert(user, "did you enjoy this talk?"));

		notify(context, alerts);
	}

	public static void notifySingleAlert(Context context) {
		User user = getBruno();

		List<Alert> alerts = new ArrayList<Alert>();

		alerts.add(
			getTextAlert(
				user,
				"This should be a very big text with more than 3 lines but it" +
					"is probably not as big as I imagined."));

		notify(context, alerts);
	}

	public static void notifySingleUser(Context context) {
		User user = getBruno();

		List<Alert> alerts = new ArrayList<Alert>();
		alerts.add(getTextAlert(user, "one"));
		alerts.add(getTextAlert(user, "two"));
		alerts.add(getTextAlert(user, "three"));
		alerts.add(getTextAlert(user, "four"));
		alerts.add(getTextAlert(user, "five"));
		alerts.add(getTextAlert(user, "six"));
		alerts.add(getTextAlert(user, "seven"));
		alerts.add(getTextAlert(user, "eight"));

		notify(context, alerts);
	}

	protected static void notify(
		final Context context, final List<Alert> alerts) {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				NotificationUtil.notify(context, alerts);
			}

		});

		thread.start();
	}

}