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

package com.liferay.alerts.widget.card.inflater;

import android.content.res.Resources;

import android.widget.TextView;

import com.liferay.alerts.R;
import com.liferay.alerts.model.AlertType;

/**
 * @author Bruno Farache
 */
public class PollsInflater extends BaseCardInflater {

	@Override
	public int getLayoutId() {
		return R.layout.card_type_polls;
	}

	@Override
	public void setTypeBadge(TextView text, AlertType type) {
		super.setTypeBadge(text, type);

		Resources resources = text.getResources();

		int left = text.getPaddingLeft();
		int top = text.getPaddingTop();
		int right = resources.getDimensionPixelSize(
			R.dimen.type_polls_padding_right);

		int bottom = text.getPaddingBottom();

		text.setPadding(left, top, right, bottom);
	}

}