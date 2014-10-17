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

import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.AlertType;
import com.liferay.alerts.widget.card.type.CardView;
import com.liferay.alerts.widget.card.type.ImageCardView;
import com.liferay.alerts.widget.card.type.LinkCardView;
import com.liferay.alerts.widget.card.type.PollsCardView;
import com.liferay.alerts.widget.card.type.TextCardView;

/**
 * @author Bruno Farache
 */
public class CardViewFactory {

	public static CardView create(Context context, Alert alert) {
		AlertType type = alert.getType();

		if (type == AlertType.IMAGE) {
			return new ImageCardView(context, alert);
		}
		else if (type == AlertType.LINK) {
			return new LinkCardView(context, alert);
		}
		else if (type == AlertType.POLLS) {
			return new PollsCardView(context, alert);
		}

		return new TextCardView(context, alert);
	}

}