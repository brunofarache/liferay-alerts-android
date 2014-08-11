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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;

import android.widget.LinearLayout;

import com.liferay.alerts.R;
import com.liferay.alerts.database.AlertDAO;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.task.GCMRegistrationAsyncTask;
import com.liferay.alerts.util.GCMUtil;
import com.liferay.alerts.util.SettingsUtil;
import com.liferay.alerts.widget.CardView;

import java.util.ArrayList;

/**
 * @author Bruno Farache
 */
public class MainActivity extends Activity {

	public static final String ADD_CARD = "add-card";

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.main);

		_cardList = (LinearLayout)findViewById(R.id.card_list);

		SettingsUtil.init(this);

		if (state != null) {
			_alerts = state.getParcelableArrayList(_ALERTS);
		}
		else {
			_alerts = AlertDAO.getInstance(this).get();
		}

		for (Alert alert : _alerts) {
			_addCard(alert.getMessage());
		}

		_addPushNotificationsDevice();
		_registerAddCardReceiver();
	}

	@Override
	protected void onDestroy() {
		_getBroadcastManager().unregisterReceiver(_receiver);

		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);

		state.putParcelableArrayList(_ALERTS, _alerts);
	}

	private void _addCard(String text) {
		_cardList.addView(new CardView(this, text));
	}

	private void _addPushNotificationsDevice() {
		if (GCMUtil.isGooglePlayServicesAvailable(this)) {
			String token = SettingsUtil.getToken();

			if (token.isEmpty()) {
				GCMRegistrationAsyncTask task = new GCMRegistrationAsyncTask(
					this);

				task.execute();
			}
			else {
				GCMUtil.addPushNotificationsDevice(this, token);
			}
		}
	}

	private LocalBroadcastManager _getBroadcastManager() {
		return LocalBroadcastManager.getInstance(this);
	}

	private void _registerAddCardReceiver() {
		_receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				_addCard(intent.getStringExtra(Alert.MESSAGE));
			}

		};

		_getBroadcastManager().registerReceiver(
			_receiver, new IntentFilter(ADD_CARD));
	}

	private static final String _ALERTS = "alerts";

	private ArrayList<Alert> _alerts;
	private LinearLayout _cardList;
	private BroadcastReceiver _receiver;

}