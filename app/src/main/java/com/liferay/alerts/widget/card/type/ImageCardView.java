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

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.liferay.alerts.R;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.util.Validator;
import com.liferay.alerts.widget.card.ImageCardViewTransformation;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * @author Bruno Farache
 */
public class ImageCardView extends CardView {

	public ImageCardView(Context context, Alert alert) {
		super(context, alert);

		String message = alert.getMessage();

		if (Validator.isNull(message)) {
			TextView text = (TextView)findViewById(R.id.message);
			text.setVisibility(View.GONE);
		}

		int color = getResources().getColor(R.color.card_timestamp_image);

		TextView timestampIcon = (TextView)findViewById(R.id.timestamp_icon);
		TextView timestamp = (TextView)findViewById(R.id.timestamp);

		timestampIcon.setTextColor(color);
		timestamp.setTextColor(color);

		ImageView image = (ImageView)findViewById(R.id.image);
		String url = getUrl();

		Transformation transformation = new ImageCardViewTransformation(
			context);

		Picasso.with(context).load(url).transform(transformation).into(image);
	}

	@Override
	public int getLayoutId() {
		return R.layout.card_type_image;
	}

}