/*
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
import android.content.res.AssetManager;
import android.content.res.TypedArray;

import android.graphics.Typeface;

import android.util.AttributeSet;

import android.widget.TextView;

import com.liferay.alerts.R;

import java.util.HashMap;

/**
 * @author Silvio Santos
 * @author Bruno Farache
 */
public class FontUtil {

	public static final String ICONS = "Icons.ttf";

	public static final String ROBOTO_LIGHT = "Roboto-Light.ttf";

	public static final String ROBOTO_REGULAR = "Roboto-Regular.ttf";

	public static void setFont(TextView view, AttributeSet attributes) {
		if (view.isInEditMode()) {
			return;
		}

		Context context = view.getContext();
		String name = getName(context, attributes);
		Typeface font = _fonts.get(name);

		if (font == null) {
			font = getFont(context, name);

			_fonts.put(name, font);
		}

		view.setTypeface(font);
	}

	protected static Typeface getFont(Context context, String name) {
		AssetManager manager = context.getAssets();
		name = "fonts/" + name;

		return Typeface.createFromAsset(manager, name);
	}

	protected static String getName(Context context, AttributeSet attributes) {
		if (attributes == null) {
			return ROBOTO_LIGHT;
		}

		TypedArray typed = context.getTheme().obtainStyledAttributes(
			attributes, R.styleable.TextView, 0, 0);

		int font = typed.getInteger(R.styleable.TextView_font, 0);

		typed.recycle();

		if (font == 0) {
			return ROBOTO_LIGHT;
		}
		else if (font == 1) {
			return ROBOTO_REGULAR;
		}
		else {
			return ICONS;
		}
	}

	private static HashMap<String, Typeface> _fonts =
		new HashMap<String, Typeface>();

}