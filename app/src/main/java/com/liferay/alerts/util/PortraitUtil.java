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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import android.graphics.Bitmap;

import android.util.Base64;
import android.util.Log;

import android.widget.ImageView;

import com.liferay.alerts.R;
import com.liferay.alerts.model.User;
import com.liferay.alerts.widget.RoundedTransformation;
import com.liferay.mobile.android.util.Validator;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.IOException;

import java.net.URLEncoder;

import java.security.MessageDigest;

/**
 * @author Bruno Farache
 */
public class PortraitUtil {

	public static Bitmap getPortrait(Context context, User user)
		throws IOException {

		String uuid = user.getUuid();
		long portraitId = user.getPortraitId();

		String portraitURL = getPortraitURL(
			SettingsUtil.getServer(context), uuid, portraitId);

		Resources resources = context.getResources();

		int width = resources.getDimensionPixelSize(
			android.R.dimen.notification_large_icon_width);

		int height = resources.getDimensionPixelSize(
			android.R.dimen.notification_large_icon_width);

		return _getRequestCreator(context, portraitURL, width, height).get();
	}

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

	public static void setPortrait(
		Context context, String uuid, long portraitId, ImageView view) {

		String portraitURL = PortraitUtil.getPortraitURL(
			SettingsUtil.getServer(context), uuid, portraitId);

		StringBuilder sb = new StringBuilder();
		sb.append(portraitURL);

		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
				context.getPackageName(), 0);

			sb.append("&version=");
			sb.append(info.versionCode);
		}
		catch (PackageManager.NameNotFoundException nnfe) {
		}

		portraitURL = sb.toString();

		Resources resources = context.getResources();

		int diameter = resources.getDimensionPixelSize(
			R.dimen.portrait_diameter);

		Transformation transformation = new RoundedTransformation(context);

		_getRequestCreator(context, portraitURL, diameter, diameter).transform(
			transformation).into(view);
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

	private static RequestCreator _getRequestCreator(
		Context context, String portraitURL, int width, int height) {

		return Picasso.with(context).load(portraitURL).resize(
			width, height).centerCrop();
	}

	private static final String _TAG = PortraitUtil.class.getSimpleName();

}