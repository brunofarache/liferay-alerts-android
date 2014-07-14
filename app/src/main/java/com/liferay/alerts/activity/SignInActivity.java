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
import com.liferay.alerts.task.GCMRegistrationAsyncTask;
import com.liferay.alerts.util.PushNotificationsUtil;
import com.liferay.alerts.util.SettingsUtil;

/**
 * @author Bruno Farache
 */
public class SignInActivity extends Activity implements View.OnClickListener {

	public void enableSignInButton() {
		_button.setEnabled(true);
	}

	@Override
	public void onClick(View view) {
		String email = _email.getText().toString();
		String password = _password.getText().toString();
		String server = _server.getText().toString();
	}

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.sign_in);

		SettingsUtil.init(this);

		_button = (Button)findViewById(R.id.sign_in_button);
		_email = (EditText)findViewById(R.id.sign_in_email);
		_password = (EditText)findViewById(R.id.sign_in_password);
		_server = (EditText)findViewById(R.id.sign_in_server);

		_button.setOnClickListener(this);

		if (PushNotificationsUtil.isGooglePlayServicesAvailable(this)) {
			String registrationId = SettingsUtil.getRegistrationId();

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
	private EditText _email;
	private EditText _password;
	private EditText _server;

}