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

package com.liferay.alerts.callback;

import android.content.ContentValues;
import android.content.Context;

import android.util.Log;

import android.widget.RadioGroup;

import com.liferay.alerts.R;
import com.liferay.alerts.database.AlertDAO;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.PollsChoice;
import com.liferay.alerts.model.PollsQuestion;
import com.liferay.alerts.util.ToastUtil;
import com.liferay.alerts.widget.CardView;
import com.liferay.mobile.android.task.callback.typed.JSONObjectAsyncTaskCallback;

import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class AddVoteCallback extends JSONObjectAsyncTaskCallback {

	public AddVoteCallback(Context context) {
		this(context, null, null);
	}

	public AddVoteCallback(Context context, Alert alert, RadioGroup group) {
		_context = context.getApplicationContext();
		_alert = alert;
		_group = group;
	}

	@Override
	public void onFailure(Exception e) {
		ToastUtil.show(_context, R.string.vote_failure, true);
		CardView.setRadioGroupEnabled(_group, true);
	}

	@Override
	public void onSuccess(JSONObject vote) {
		ToastUtil.show(_context, R.string.vote_success, true);

		if (_alert == null) {
			return;
		}

		try {
			PollsQuestion question = _alert.getPollsQuestion();
			int choiceId = vote.getInt(PollsChoice.CHOICE_ID);

			_alert.setPollsQuestion(question.toJSONObject(choiceId));

			AlertDAO dao = AlertDAO.getInstance(_context);

			ContentValues values = new ContentValues();
			values.put(Alert.PAYLOAD, _alert.getPayload().toString());

			dao.update(_alert.getId(), values, true);
		}
		catch (Exception e) {
			Log.e(_TAG, "Could not update vote in database.", e);
		}
	}

	private static final String _TAG = AddVoteCallback.class.getSimpleName();

	private Alert _alert;
	private Context _context;
	private RadioGroup _group;

}