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

package com.liferay.mobile.android.v7.pushnotificationsdevice;

import com.liferay.mobile.android.service.BaseService;
import com.liferay.mobile.android.service.Session;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class PushNotificationsDeviceService extends BaseService
	implements com.liferay.mobile.android.PushNotificationsDeviceService {

	public PushNotificationsDeviceService(Session session) {
		super(session);
	}

	@Override
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

	@Override
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

	@Override
	public void sendPushNotification(String message) throws Exception {
		JSONObject _command = new JSONObject();

		try {
			JSONObject _params = new JSONObject();

			_params.put("message", message);

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