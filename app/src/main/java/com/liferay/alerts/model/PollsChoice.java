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

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class PollsChoice implements Serializable {

	public static final String CHECKED = "checked";

	public static final String CHOICE_ID = "choiceId";

	public static final String DESCRIPTION = "description";

	public PollsChoice(int choiceId, String description, boolean checked) {
		_choiceId = choiceId;
		_description = description;
		_checked = checked;
	}

	public PollsChoice(JSONObject choice) throws JSONException {
		_choiceId = choice.getInt(CHOICE_ID);
		_description = choice.getString(DESCRIPTION);
		_checked = choice.optBoolean(CHECKED);
	}

	public int getChoiceId() {
		return _choiceId;
	}

	public String getDescription() {
		return _description;
	}

	public boolean isChecked() {
		return _checked;
	}

	public void setChecked(boolean checked) {
		_checked = checked;
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put(CHOICE_ID, _choiceId);
		jsonObject.put(DESCRIPTION, _description);
		jsonObject.put(CHECKED, _checked);

		return jsonObject;
	}

	private boolean _checked;
	private int _choiceId;
	private String _description;

}