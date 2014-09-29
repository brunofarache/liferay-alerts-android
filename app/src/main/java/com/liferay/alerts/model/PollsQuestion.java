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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class PollsQuestion {

	public static final String CHOICES = "choices";

	public static final String QUESTION = "question";

	public static final String QUESTION_ID = "questionId";

	public PollsQuestion(JSONObject question) throws JSONException {
		_questionId = question.getLong(QUESTION_ID);

		JSONArray choices = question.getJSONArray(CHOICES);

		for (int i = 0; i < choices.length(); i++) {
			_choices.add(new PollsChoice(choices.getJSONObject(i)));
		}
	}

	public List<PollsChoice> getChoices() {
		return _choices;
	}

	public long getQuestionId() {
		return _questionId;
	}

	public JSONObject toJSONObject(int choiceId) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put(QUESTION_ID, _questionId);

		JSONArray choicesJSONArray = new JSONArray();
		List<PollsChoice> choices = getChoices();

		for (PollsChoice choice : choices) {
			if (choice.getChoiceId() == choiceId) {
				choice.setChecked(true);
			}

			choicesJSONArray.put(choice.toJSONObject());
		}

		jsonObject.put(CHOICES, choicesJSONArray);

		return jsonObject;
	}

	public class PollsChoice {

		public static final String CHECKED = "checked";
		public static final String CHOICE_ID = "choiceId";
		public static final String DESCRIPTION = "description";

		public PollsChoice(JSONObject choice) throws JSONException {
			_choiceId = choice.getInt(CHOICE_ID);
			_description = choice.getString(DESCRIPTION);
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

		private boolean _checked;
		private int _choiceId;
		private String _description;

		public JSONObject toJSONObject() throws JSONException {
			JSONObject jsonObject = new JSONObject();

			jsonObject.put(CHOICE_ID, _choiceId);
			jsonObject.put(DESCRIPTION, _description);
			jsonObject.put(CHECKED, _checked);

			return jsonObject;
		}

	}

	private List<PollsChoice> _choices = new ArrayList<PollsChoice>();
	private long _questionId;

}