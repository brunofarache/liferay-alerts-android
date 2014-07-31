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

import android.content.Context;

import com.liferay.alerts.R;
import com.liferay.alerts.util.ToastUtil;
import com.liferay.mobile.android.task.callback.typed.JSONObjectAsyncTaskCallback;

import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class RegistrationCallback extends JSONObjectAsyncTaskCallback {

	public RegistrationCallback(Context context) {
		_context = context.getApplicationContext();
	}

	public void onFailure(Exception e) {
		ToastUtil.show(_context, R.string.failed_to_register, true);
	}

	public void onSuccess(JSONObject jsonObj) {
		ToastUtil.show(_context, R.string.registered, true);
	}

	private Context _context;

}