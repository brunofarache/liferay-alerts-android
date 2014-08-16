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
import android.content.res.TypedArray;

import android.util.AttributeSet;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liferay.alerts.R;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.User;
import com.liferay.alerts.util.PortraitUtil;
import com.liferay.alerts.util.SettingsUtil;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * @author Bruno Farache
 */
public class CardView extends LinearLayout {

	public CardView(Context context) {
		this(context, (AttributeSet)null);
	}

	public CardView(Context context, AttributeSet attributes) {
		this(context, attributes, 0);
	}

	public CardView(
		Context context, AttributeSet attributes, int defaultStyle) {

		super(context, attributes, defaultStyle);

		inflate(context, R.layout.card, this);

		_text = (TextView)findViewById(R.id.text);

		TypedArray typed = context.getTheme().obtainStyledAttributes(
			attributes, R.styleable.CardView, 0, 0);

		String text = typed.getString(R.styleable.CardView_text);

		if (text != null) {
			setText(text);
		}

		typed.recycle();
	}

	public CardView(Context context, User user, Alert alert) {
		this(context);

		if (user != null) {
			String portraitURL = PortraitUtil.getPortraitURL(
				SettingsUtil.getServer(context), user.getUuid(),
				user.getPortraitId());

			Transformation transformation = new RoundedTransformation();
			ImageView portrait = (ImageView)findViewById(R.id.portrait);
			Picasso.with(context).load(portraitURL).transform(
				transformation).into(portrait);
		}

		setText(alert.getMessage());
	}

	public void setText(String text) {
		_text.setText(text);
	}

	private TextView _text;

}