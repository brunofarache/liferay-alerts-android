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

import android.util.Log;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.liferay.alerts.R;
import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.service.SessionImpl;

/**
 * @author Bruno Farache
 */
public class SignInActivity extends Activity implements View.OnClickListener {

	@Override
	public void onClick(View view) {
		String email = _email.getText().toString();
		String password = _password.getText().toString();
		String server = _server.getText().toString();

		Session session = new SessionImpl(server, email, password);
	}

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.sign_in);

		_email = (EditText)findViewById(R.id.sign_in_email);
		_password = (EditText)findViewById(R.id.sign_in_password);
		_server = (EditText)findViewById(R.id.sign_in_server);

		if (_isGooglePlayServicesAvailable()) {
			Button button = (Button)findViewById(R.id.sign_in_button);
			button.setOnClickListener(this);
		}
	}

	private boolean _isGooglePlayServicesAvailable() {
		int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (result == ConnectionResult.SUCCESS) {
			return true;
		}

		if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
			GooglePlayServicesUtil.getErrorDialog(result, this, 9000).show();
		}
		else {
			Log.i(TAG, "This device is not supported.");
			finish();
		}

		return false;
	}

	private static final String TAG = SignInActivity.class.getSimpleName();

	private EditText _email;
	private EditText _password;
	private EditText _server;

}