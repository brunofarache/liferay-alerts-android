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

import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import com.liferay.alerts.R;
import com.liferay.alerts.callback.SignInCallback;
import com.liferay.alerts.task.GCMRegistrationAsyncTask;
import com.liferay.alerts.util.PushNotificationsUtil;
import com.liferay.alerts.util.SettingsUtil;
import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.service.SessionImpl;
import com.liferay.mobile.android.v62.pushnotificationsdevice.PushNotificationsDeviceService;

/**
 * @author Bruno Farache
 */
public class SignInActivity extends Activity implements View.OnClickListener {

	public void enableSignInButton() {
		_button.setEnabled(true);
	}

	@Override
	public void onClick(View view) {
		String server = _server.getText().toString();

		Session session = new SessionImpl(server);
		session.setCallback(new SignInCallback(server));

		PushNotificationsDeviceService service =
			new PushNotificationsDeviceService(session);

		try {
			String token = SettingsUtil.getToken();
			service.addPushNotificationsDevice(token, "android");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.sign_in);

		SettingsUtil.init(this);

		_button = (Button)findViewById(R.id.sign_in_button);
		_server = (EditText)findViewById(R.id.sign_in_server);

		_button.setOnClickListener(this);

		if (PushNotificationsUtil.isGooglePlayServicesAvailable(this)) {
			String registrationId = SettingsUtil.getToken();

			if (registrationId.isEmpty()) {
				GCMRegistrationAsyncTask task = new GCMRegistrationAsyncTask(
					this);

				task.execute();
			}
			else {
				enableSignInButton();
			}
		}
	}

	private Button _button;
	private EditText _server;

}