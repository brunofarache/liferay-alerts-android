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

package com.liferay.mobile.android;

import android.content.Context;

import com.liferay.alerts.R;
import com.liferay.alerts.activity.MainActivity;
import com.liferay.alerts.callback.AddVoteCallback;
import com.liferay.alerts.util.SettingsUtil;
import com.liferay.alerts.util.ToastUtil;
import com.liferay.mobile.android.service.BaseService;
import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.service.SessionImpl;
import com.liferay.mobile.android.task.callback.typed.JSONObjectAsyncTaskCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class PushNotificationsDeviceService extends BaseService {

	public static void addVote(
		Context context, long alertId, int questionId, int choiceId) {

		MainActivity.enablePollsQuestion(context, questionId, choiceId, false);

		String server = SettingsUtil.getServer(context);
		Session session = new SessionImpl(server);
		JSONObjectAsyncTaskCallback callback = new AddVoteCallback(
			context, alertId, questionId, choiceId);

		session.setCallback(callback);

		PushNotificationsDeviceService service =
			new PushNotificationsDeviceService(session);

		try {
			service.addVote(questionId, choiceId);
		}
		catch (Exception e) {
			ToastUtil.show(context, R.string.vote_failure, true);
			MainActivity.enablePollsQuestion(
				context, questionId, choiceId, true);
		}
	}

	public PushNotificationsDeviceService(Session session) {
		super(session);
	}

	public JSONObject addPushNotificationsDevice(String token, String platform)
		throws Exception {

		JSONObject _command = new JSONObject();

		try {
			JSONObject _params = new JSONObject();

			_params.put("token", token);
			_params.put("platform", platform);

			_command.put(
				"/push-notifications-portlet.pushnotificationsdevice/" +
					"add-push-notifications-device",
				_params);
		}
		catch (JSONException _je) {
			throw new Exception(_je);
		}

		return (JSONObject)session.invoke(_command);
	}

	public JSONObject addVote(long questionId, long choiceId) throws Exception {
		JSONObject _command = new JSONObject();

		try {
			JSONObject _params = new JSONObject();

			_params.put("questionId", questionId);
			_params.put("choiceId", choiceId);

			_command.put(
				"/push-notifications-portlet.pushnotificationsdevice/" +
					"add-vote",
				_params);
		}
		catch (JSONException _je) {
			throw new Exception(_je);
		}

		return (JSONObject)session.invoke(_command);
	}

	public JSONObject deletePushNotificationsDevice(String token)
		throws Exception {

		JSONObject _command = new JSONObject();

		try {
			JSONObject _params = new JSONObject();

			_params.put("token", token);

			_command.put(
				"/push-notifications-portlet.pushnotificationsdevice/" +
					"delete-push-notifications-device",
				_params);
		}
		catch (JSONException _je) {
			throw new Exception(_je);
		}

		return (JSONObject)session.invoke(_command);
	}

	public Boolean hasPermission(String actionId) throws Exception {
		JSONObject _command = new JSONObject();

		try {
			JSONObject _params = new JSONObject();

			_params.put("actionId", actionId);

			_command.put(
				"/push-notifications-portlet.pushnotificationsdevice/" +
					"has-permission",
				_params);
		}
		catch (JSONException _je) {
			throw new Exception(_je);
		}

		return (Boolean)session.invoke(_command);
	}

	public void sendPushNotification(String payload) throws Exception {
		JSONObject _command = new JSONObject();

		try {
			JSONObject _params = new JSONObject();

			_params.put("payload", payload);

			_command.put(
				"/push-notifications-portlet.pushnotificationsdevice/" +
					"send-push-notification",
				_params);
		}
		catch (JSONException _je) {
			throw new Exception(_je);
		}

		session.invoke(_command);
	}

}