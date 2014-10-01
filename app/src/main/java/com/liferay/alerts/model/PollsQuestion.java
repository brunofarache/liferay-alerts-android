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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class PollsQuestion implements Serializable {

	public static final String CHOICES = "choices";

	public static final String QUESTION = "question";

	public static final String QUESTION_ID = "questionId";

	public PollsQuestion(int questionId, List<PollsChoice> choices) {
		_questionId = questionId;
		_choices = choices;
	}

	public PollsQuestion(JSONObject question) throws JSONException {
		_questionId = question.getInt(QUESTION_ID);

		JSONArray choices = question.getJSONArray(CHOICES);

		for (int i = 0; i < choices.length(); i++) {
			_choices.add(new PollsChoice(choices.getJSONObject(i)));
		}
	}

	public List<PollsChoice> getChoices() {
		return _choices;
	}

	public int getQuestionId() {
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

	private List<PollsChoice> _choices = new ArrayList<PollsChoice>();
	private int _questionId;

}