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

package com.liferay.alerts.activity;

import android.app.Activity;

import android.content.Context;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import com.liferay.alerts.R;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.util.SettingsUtil;
import com.liferay.alerts.util.ToastUtil;
import com.liferay.mobile.android.PushNotificationsDeviceService;
import com.liferay.mobile.android.pushnotificationsdevice.PushNotificationsDeviceServiceImpl;
import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.service.SessionImpl;
import com.liferay.mobile.android.task.callback.typed.StringAsyncTaskCallback;

import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class SendActivity extends Activity {

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.send);

		_message = (EditText)findViewById(R.id.message);
		_send = (Button)findViewById(R.id.send);

		_send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Context context = getApplicationContext();

				String email = SettingsUtil.getEmail(context);
				String password = SettingsUtil.getPassword(context);
				String server = SettingsUtil.getServer(context);

				Session session = new SessionImpl(
					server, email, password, new StringAsyncTaskCallback() {

						@Override
						public void onFailure(Exception exception) {
							_handleException(exception);
						}

						@Override
						public void onSuccess(String result) {
							ToastUtil.show(
								SendActivity.this,
								R.string.send_notification_success, true);
						}

				});

				PushNotificationsDeviceService service =
					new PushNotificationsDeviceServiceImpl(session);

				try {
					String message = _message.getText().toString();

					JSONObject payload = new JSONObject();
					payload.put(Alert.MESSAGE, message);
					payload.put(Alert.TYPE, "text");

					service.sendPushNotification(payload.toString());
				}
				catch (Exception e) {
					_handleException(e);
				}
			}

		});
	}

	private void _handleException(Exception exception) {
		String message = getString(R.string.send_notification_failure);
		message = message + exception.getMessage();

		ToastUtil.show(this, message, true);
	}

	private EditText _message;
	private Button _send;

}