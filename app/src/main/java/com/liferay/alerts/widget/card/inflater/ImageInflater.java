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

import android.view.View;

import android.widget.ImageView;

import com.liferay.alerts.R;
import com.liferay.alerts.model.Alert;

import com.squareup.picasso.Picasso;

/**
 * @author Bruno Farache
 */
public class ImageInflater extends BaseCardInflater {

	@Override
	public int getLayoutId() {
		return R.layout.card_type_image;
	}

	@Override
	public View inflate(Context context, Alert alert) {
		View view = super.inflate(context, alert);

		ImageView image = (ImageView)view.findViewById(R.id.image);
		Picasso.with(context).load(getUrl(context, alert)).into(image);

		return view;
	}

}