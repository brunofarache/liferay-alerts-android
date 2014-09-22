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

package com.liferay.alerts.widget;

import android.content.Context;

import android.util.AttributeSet;

import com.liferay.alerts.util.FontUtil;

/**
 * @author Bruno Farache
 */
public class TextView extends android.widget.TextView {

	public TextView(Context context) {
		super(context);

		FontUtil.setFont(this, null);
	}

	public TextView(Context context, AttributeSet attributes) {
		super(context, attributes);

		FontUtil.setFont(this, attributes);
	}

	public TextView(
		Context context, AttributeSet attributes, int defaultStyle) {

		super(context, attributes, defaultStyle);

		FontUtil.setFont(this, attributes);
	}

}