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

package com.liferay.alerts.model;

import com.liferay.alerts.R;

/**
 * @author Bruno Farache
 */
public enum AlertType {

	IMAGE(R.string.icon_type_image, R.drawable.type_image_background),
	LINK(R.string.icon_type_link, R.drawable.type_link_background),
	MAP(R.string.icon_type_map, R.drawable.type_map_background),
	TEXT(R.string.icon_type_text, R.drawable.type_text_background),
	UNKNOW(R.string.icon_type_text, R.drawable.type_text_background);

	public static AlertType getType(String type) {
		for (AlertType value : AlertType.values()) {
			if (value.name().equalsIgnoreCase(type)) {
				return value;
			}
		}

		return UNKNOW;
	}

	public int getBackground() {
		return _background;
	}

	public int getText() {
		return _text;
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}

	private AlertType(int text, int background) {
		_text = text;
		_background = background;
	}

	private int _background;
	private int _text;

}