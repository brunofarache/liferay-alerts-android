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

package com.liferay.alerts.activity;

import android.app.Activity;

import android.os.Bundle;

import android.widget.LinearLayout;

import com.liferay.alerts.R;
import com.liferay.alerts.task.GCMRegistrationAsyncTask;
import com.liferay.alerts.util.PushNotificationsUtil;
import com.liferay.alerts.util.SettingsUtil;
import com.liferay.alerts.widget.CardView;

/**
 * @author Bruno Farache
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.main);

		_cardList = (LinearLayout)findViewById(R.id.card_list);

		SettingsUtil.init(this);

		if (PushNotificationsUtil.isGooglePlayServicesAvailable(this)) {
			String token = SettingsUtil.getToken();
			GCMRegistrationAsyncTask task = new GCMRegistrationAsyncTask(this);

			if (token.isEmpty()) {
				task.execute();
			}
			else {
				PushNotificationsUtil.register(this, token);
			}
		}
	}

	private void _addCard(String text) {
		CardView card = new CardView(this);

		card.setBackground(
			getResources().getDrawable(R.drawable.card_background));
		card.setText(text);

		_cardList.addView(card);
	}

	private LinearLayout _cardList;

}