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

package com.liferay.alerts.widget.card.type;

import android.content.Context;
import android.content.res.Resources;

import android.graphics.Paint;

import android.util.TypedValue;

import android.widget.TextView;

import com.liferay.alerts.R;
import com.liferay.alerts.model.Alert;

/**
 * @author Bruno Farache
 */
public class LinkCardView extends CardView {

	public LinkCardView(Context context, Alert alert) {
		super(context, alert);

		TextView link = (TextView)findViewById(R.id.link);
		link.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

		String url = getUrl();
		link.setText(url);
	}

	@Override
	public int getLayoutId() {
		return R.layout.card_type_link;
	}

	@Override
	public void setTypeBadge(TextView text) {
		super.setTypeBadge(text);

		Resources resources = text.getResources();

		int left = resources.getDimensionPixelSize(
			R.dimen.type_link_padding_left);

		int top = resources.getDimensionPixelSize(
			R.dimen.type_link_padding_top);

		int right = text.getPaddingRight();
		int bottom = text.getPaddingBottom();

		text.setPadding(left, top, right, bottom);
		text.setTextSize(
			TypedValue.COMPLEX_UNIT_PX,
			resources.getDimensionPixelSize(R.dimen.type_link_text));
	}

}