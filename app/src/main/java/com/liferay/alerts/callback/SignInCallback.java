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

package com.liferay.alerts.callback;

import com.liferay.alerts.util.SettingsUtil;
import com.liferay.mobile.android.task.callback.typed.JSONObjectAsyncTaskCallback;

import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class SignInCallback extends JSONObjectAsyncTaskCallback {

	public SignInCallback(String server) {
		_server = server;
	}

	public void onFailure(Exception e) {
		e.printStackTrace();
	}

	public void onSuccess(JSONObject jsonObj) {
		SettingsUtil.setServer(_server);
	}

	private String _server;

}