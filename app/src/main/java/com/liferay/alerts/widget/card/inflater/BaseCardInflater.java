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
import android.content.Intent;

import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

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

	public void setTypeBadge(TextView text, AlertType type) {
		text.setText(type.getText());
		text.setBackgroundResource(type.getBackground());
	}

	protected String getUrl(Context context, Alert alert) {
		String url = alert.getUrl();
		StringBuilder sb = new StringBuilder(url);

		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			sb.append("http://");
		}

		url = sb.toString();

		final Uri uri = Uri.parse(sb.toString());
		final Context applicationContext = context.getApplicationContext();

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				applicationContext.startActivity(intent);
			}

		});

		return url;
	}

	protected void setMessage(String message) {
		TextView text = (TextView)view.findViewById(R.id.message);
		text.setText(message);
	}

	protected void setTimestamp(String timestamp) {
		TextView text = (TextView)view.findViewById(R.id.timestamp);
		text.setText(timestamp);
	}

	protected View view;

}