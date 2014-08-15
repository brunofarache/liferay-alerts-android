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

package com.liferay.alerts.util;

import android.util.Base64;
import android.util.Log;

import com.liferay.mobile.android.util.Validator;

import java.net.URLEncoder;

import java.security.MessageDigest;

/**
 * @author Bruno Farache
 */
public class PortraitUtil {

	public static String getPortraitURL(
		String server, String uuid, long portraitId) {

		StringBuilder sb = new StringBuilder();

		sb.append(server);
		sb.append("/image/user_male");

		sb.append("_portrait?img_id=");
		sb.append(portraitId);

		_appendToken(sb, uuid);

		return sb.toString();
	}

	private static void _appendToken(StringBuilder sb, String uuid) {
		if (Validator.isNull(uuid)) {
			return;
		}

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(uuid.getBytes());

			byte[] bytes = digest.digest();
			String token = Base64.encodeToString(bytes, Base64.NO_WRAP);

			sb.append("&img_id_token=");
			sb.append(URLEncoder.encode(token, "UTF-8"));
		}
		catch (Exception e) {
			Log.e(_TAG, "Couldn't generate portrait image token", e);
		}
	}

	private static final String _TAG = PortraitUtil.class.getSimpleName();

}