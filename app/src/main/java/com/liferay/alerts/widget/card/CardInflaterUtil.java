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

import android.view.View;

import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.AlertType;
import com.liferay.alerts.widget.card.inflater.CardInflater;
import com.liferay.alerts.widget.card.inflater.ImageInflater;
import com.liferay.alerts.widget.card.inflater.LinkInflater;
import com.liferay.alerts.widget.card.inflater.PollsInflater;
import com.liferay.alerts.widget.card.inflater.TextInflater;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Farache
 */
public class CardInflaterUtil {

	public static View inflate(Context context, Alert alert) {
		CardInflater inflater = _inflaters.get(alert.getType());

		return inflater.inflate(context, alert);
	}

	private static Map<AlertType, CardInflater> _inflaters =
		new HashMap<AlertType, CardInflater>();

	static {
		_inflaters.put(AlertType.IMAGE, new ImageInflater());
		_inflaters.put(AlertType.LINK, new LinkInflater());
		_inflaters.put(AlertType.POLLS, new PollsInflater());
		_inflaters.put(AlertType.TEXT, new TextInflater());
	}

}