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

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.TextView;

import com.liferay.alerts.R;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.AlertType;

/**
 * @author Bruno Farache
 */
public abstract class BaseCardInflater implements CardInflater {

	public abstract int getLayoutId();

	@Override
	public View inflate(Context context, Alert alert) {
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(getLayoutId(), null);

		setMessage(alert.getMessage());
		setTimestamp(alert.getFormattedTimestamp());

		return view;
	}

	public void setMessage(String message) {
		TextView text = (TextView)view.findViewById(R.id.message);
		text.setText(message);
	}

	public void setTimestamp(String timestamp) {
		TextView text = (TextView)view.findViewById(R.id.timestamp);
		text.setText(timestamp);
	}

	public void setTypeBadge(TextView text, AlertType type) {
		text.setText(type.getText());
		text.setBackgroundResource(type.getBackground());
	}

	protected View view;

}