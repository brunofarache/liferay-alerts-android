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

package com.liferay.alerts.widget.card;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;

import android.net.Uri;

import android.util.TypedValue;

import android.view.View;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liferay.alerts.R;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.AlertType;
import com.liferay.alerts.model.User;
import com.liferay.alerts.util.PortraitUtil;
import com.liferay.alerts.util.Validator;

import com.squareup.picasso.Picasso;

/**
 * @author Bruno Farache
 */
public class CardView extends LinearLayout implements View.OnClickListener {

	public CardView(Context context) {
		super(context);
	}

	public CardView(Context context, Alert alert) {
		this(context);

		inflate(context, R.layout.card, this);

		_card = (FrameLayout)findViewById(R.id.card);
		_card.addView(CardInflaterUtil.inflate(context, alert));

		TextView typeBadge = (TextView)findViewById(R.id.type_badge);
		CardInflaterUtil.setTypeBadge(typeBadge, alert.getType());

		User user = alert.getUser(context);

		if (user != null) {
			setPortrait(context, user.getUuid(), user.getPortraitId());
		}

		AlertType type = alert.getType();
		_url = alert.getUrl();

		if (Validator.isNotNull(_url)) {
			if (!_url.startsWith("http://") && !_url.startsWith("https://")) {
				_url = "http://" + _url;
			}

			if (type == AlertType.LINK) {
				setLink();
			}
			else if (type == AlertType.IMAGE) {
				setImage(context);
			}

			setOnClickListener(this);
		}

		setBackground();
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_url));
		getContext().startActivity(intent);
	}

	public void setBackground() {
		Resources resources = getResources();

		int backgroundColor = resources.getColor(R.color.card_background);
		int borderColor = resources.getColor(R.color.card_border);
		float arrowY = resources.getDimensionPixelSize(R.dimen.card_arrow_y);
		float arrowHeight = resources.getDimensionPixelSize(
				R.dimen.card_arrow_height);

		float arrowWidth = resources.getDimensionPixelSize(
			R.dimen.card_arrow_width);

		TypedValue value = new TypedValue();
		resources.getValue(R.dimen.card_border_width, value, true);
		float borderWidth = value.getFloat();

		value = new TypedValue();
		resources.getValue(R.dimen.card_corner_radius, value, true);
		float cornerRadius = value.getFloat();

		CardShape borderShape = new CardShape(
			Paint.Style.STROKE, borderColor, arrowY, arrowHeight, arrowWidth,
			borderWidth, cornerRadius);

		CardShape backgroundShape = new CardShape(
			Paint.Style.FILL_AND_STROKE, backgroundColor, arrowY, arrowHeight,
			arrowWidth, borderWidth, cornerRadius);

		Drawable[] layers = {
			new ShapeDrawable(borderShape), new ShapeDrawable(backgroundShape)
		};

		_card.setBackground(new LayerDrawable(layers));
	}

	public void setImage(Context context) {
		ImageView image = (ImageView)findViewById(R.id.image);

		Picasso.with(context).load(_url).into(image);
	}

	public void setLink() {
		TextView link = (TextView)findViewById(R.id.link);

		link.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		link.setText(_url);
	}

	protected void setPortrait(Context context, String uuid, long portraitId) {
		ImageView portrait = (ImageView)findViewById(R.id.portrait);

		if (isInEditMode()) {
			portrait.setImageResource(R.drawable.launcher);

			return;
		}

		PortraitUtil.setPortrait(context, uuid, portraitId, portrait);
	}

	private FrameLayout _card;
	private String _url;

}