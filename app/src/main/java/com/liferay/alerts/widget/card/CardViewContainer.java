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
import android.content.res.Resources;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;

import android.util.TypedValue;

import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liferay.alerts.R;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.User;
import com.liferay.alerts.util.PortraitUtil;
import com.liferay.alerts.widget.card.inflater.CardView;

/**
 * @author Bruno Farache
 */
public class CardViewContainer extends LinearLayout {

	public CardViewContainer(Context context) {
		super(context);
	}

	public CardViewContainer(Context context, Alert alert) {
		this(context);

		inflate(context, R.layout.card, this);

		FrameLayout layout = (FrameLayout)findViewById(R.id.card);
		CardView card = CardViewFactory.create(context, alert);
		layout.addView(card);

		TextView typeBadge = (TextView)findViewById(R.id.type_badge);
		card.setTypeBadge(typeBadge);

		User user = alert.getUser(context);

		if (user != null) {
			setPortrait(context, user.getUuid(), user.getPortraitId());
		}

		setBackground(layout);
	}

	public void setBackground(ViewGroup card) {
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

		card.setBackground(new LayerDrawable(layers));
	}

	protected void setPortrait(Context context, String uuid, long portraitId) {
		ImageView portrait = (ImageView)findViewById(R.id.portrait);

		if (isInEditMode()) {
			portrait.setImageResource(R.drawable.launcher);

			return;
		}

		PortraitUtil.setPortrait(context, uuid, portraitId, portrait);
	}

}