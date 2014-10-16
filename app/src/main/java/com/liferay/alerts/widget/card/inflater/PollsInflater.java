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

package com.liferay.alerts.widget.card.inflater;

import android.content.Context;
import android.content.res.Resources;

import android.util.TypedValue;

import android.view.View;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.liferay.alerts.R;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.AlertType;
import com.liferay.alerts.model.PollsChoice;
import com.liferay.alerts.model.PollsQuestion;
import com.liferay.alerts.util.FontUtil;
import com.liferay.mobile.android.PushNotificationsDeviceService;

import java.util.List;

/**
 * @author Bruno Farache
 */
public class PollsInflater extends BaseCardInflater {

	@Override
	public int getLayoutId() {
		return R.layout.card_type_polls;
	}

	@Override
	public View inflate(Context context, Alert alert) {
		View view = super.inflate(context, alert);

		setChoices(context, alert);

		return view;
	}

	@Override
	public void setTypeBadge(TextView text, AlertType type) {
		super.setTypeBadge(text, type);

		Resources resources = text.getResources();

		int left = text.getPaddingLeft();
		int top = text.getPaddingTop();
		int right = resources.getDimensionPixelSize(
			R.dimen.type_polls_padding_right);

		int bottom = text.getPaddingBottom();

		text.setPadding(left, top, right, bottom);
	}

	protected void setChoices(Context context, Alert alert) {
		try {
			PollsQuestion question = alert.getPollsQuestion();
			RadioGroup group = (RadioGroup)view.findViewById(R.id.polls);

			List<PollsChoice> choices = question.getChoices();

			int c = (int)'A' - 1;

			for (PollsChoice choice : choices) {
				c++;

				RadioButton button = new RadioButton(context);
				Resources resources = view.getResources();

				int padding = resources.getDimensionPixelSize(
					R.dimen.radio_button_padding);

				button.setPadding(0, 0, 0, padding);

				button.setId(choice.getChoiceId());
				button.setButtonDrawable(
					resources.getDrawable(R.drawable.radio_button));

				button.setText((char)c + ". " + choice.getDescription());
				button.setTextColor(resources.getColor(R.color.card_text));
				button.setTextSize(
					TypedValue.COMPLEX_UNIT_PX,
					resources.getDimensionPixelSize(R.dimen.card_text_size));

				button.setTypeface(
					FontUtil.getFont(context, FontUtil.ROBOTO_LIGHT));

				group.addView(button);

				if (choice.isChecked()) {
					button.setChecked(true);

					for (int i = 0; i < group.getChildCount(); i++) {
						group.getChildAt(i).setEnabled(false);
					}
				}
			}

			final Context applicationContext = context.getApplicationContext();
			final long alertId = alert.getId();

			group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int choiceId) {
					int questionId = group.getId();

					PushNotificationsDeviceService.addVote(
						applicationContext, alertId, questionId, choiceId);
				}

			});

			group.setId(question.getQuestionId());
		}
		catch (Exception e) {
		}
	}

}