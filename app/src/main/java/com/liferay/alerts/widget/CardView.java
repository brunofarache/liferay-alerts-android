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
import android.content.Intent;
import android.content.res.TypedArray;

import android.net.Uri;

import android.util.AttributeSet;

import android.view.View;

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
public class CardView extends LinearLayout implements View.OnClickListener {

	public CardView(Context context) {
		this(context, (AttributeSet)null);
	}

	public CardView(Context context, Alert alert) {
		this(context);

		User user = alert.getUser(context);

		if (user != null) {
			setPortrait(context, user.getUuid(), user.getPortraitId());
		}

		setText(alert.getMessage());

		_url = alert.getUrl();

		if (_url != null) {
			if (!_url.startsWith("http://") && !_url.startsWith("https://")) {
				_url = "http://" + _url;
			}

			setOnClickListener(this);
		}

		setTimestamp(alert.getFormattedTimestamp());
	}

	public CardView(Context context, AttributeSet attributes) {
		this(context, attributes, 0);
	}

	public CardView(
		Context context, AttributeSet attributes, int defaultStyle) {

		super(context, attributes, defaultStyle);

		inflate(context, R.layout.card, this);

		_text = (TextView)findViewById(R.id.text);
		_timestamp = (TextView)findViewById(R.id.timestamp);

		TypedArray typed = context.getTheme().obtainStyledAttributes(
			attributes, R.styleable.CardView, 0, 0);

		String text = typed.getString(R.styleable.CardView_text);

		if (text != null) {
			setText(text);
		}

		int portraitId = typed.getInt(R.styleable.CardView_portraitId, 0);

		if (portraitId != 0) {
			setPortrait(context, null, portraitId);
		}

		setTimestamp(getResources().getString(R.string.release_date));

		typed.recycle();
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_url));
		getContext().startActivity(intent);
	}

	public void setText(String text) {
		_text.setText(text);
	}

	public void setTimestamp(String timestamp) {
		_timestamp.setText(timestamp);
	}

	protected void setPortrait(Context context, String uuid, long portraitId) {
		ImageView portrait = (ImageView)findViewById(R.id.portrait);

		if (isInEditMode()) {
			portrait.setImageResource(R.drawable.launcher);

			return;
		}

		String portraitURL = PortraitUtil.getPortraitURL(
			SettingsUtil.getServer(context), uuid, portraitId);

		Transformation transformation = new RoundedTransformation();
		Picasso.with(context).load(portraitURL).transform(transformation).into(
			portrait);
	}

	private TextView _text;
	private TextView _timestamp;
	private String _url;

}