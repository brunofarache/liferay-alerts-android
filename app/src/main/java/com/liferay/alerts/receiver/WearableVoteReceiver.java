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

package com.liferay.alerts.receiver;

import android.app.PendingIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.app.RemoteInput;

import com.liferay.alerts.R;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.AlertType;
import com.liferay.alerts.model.PollsChoice;
import com.liferay.alerts.model.PollsQuestion;

import java.util.List;

/**
 * @author Bruno Farache
 */
public class WearableVoteReceiver extends BroadcastReceiver {

	public static final String ACTION_VOTE = "ACTION_VOTE";

	public static final String EXTRA_CHOICE = "EXTRA_CHOICE";

	public static final String EXTRA_QUESTION = "EXTRA_QUESTION";

	public static void addPollsActions(
		Context context, List<Alert> alerts,
		NotificationCompat.Builder builder) {

		for (Alert alert : alerts) {
			if (AlertType.POLLS == alert.getType()) {
				builder.extend(getWearableExtender(context, alert));
			}
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		PollsQuestion question = (PollsQuestion)intent.getSerializableExtra(
			EXTRA_QUESTION);

		Bundle input = RemoteInput.getResultsFromIntent(intent);

		CharSequence choice = input.getCharSequence(EXTRA_CHOICE);
	}

	protected static PendingIntent getPendingIntent(
		Context context, PollsQuestion question) {

		Intent intent = new Intent(ACTION_VOTE);
		intent.setClass(context, WearableVoteReceiver.class);
		intent.putExtra(EXTRA_QUESTION, question);

		return PendingIntent.getBroadcast(
			context, 9313, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	protected static RemoteInput getRemoteInput(Alert alert) {
		PollsQuestion question = alert.getPollsQuestion();
		List<PollsChoice> pollsChoices = question.getChoices();

		String[] choices = new String[pollsChoices.size()];

		for (int i = 0; i < pollsChoices.size(); i++) {
			choices[i] = pollsChoices.get(i).getDescription();
		}

		RemoteInput.Builder builder = new RemoteInput.Builder(EXTRA_CHOICE);

		builder.setLabel(alert.getMessage());
		builder.setChoices(choices);
		builder.setAllowFreeFormInput(false);

		return builder.build();
	}

	protected static WearableExtender getWearableExtender(
		Context context, Alert alert) {

		PendingIntent pendingIntent = getPendingIntent(
			context, alert.getPollsQuestion());

		Action.Builder actionBuilder = new Action.Builder(
			R.drawable.launcher, context.getString(R.string.vote),
			pendingIntent);

		actionBuilder.addRemoteInput(getRemoteInput(alert));

		NotificationCompat.Action action = actionBuilder.build();

		WearableExtender extender = new WearableExtender();
		extender.addAction(action);

		return extender;
	}

}