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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;

import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.liferay.alerts.R;
import com.liferay.alerts.database.AlertDAO;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.task.GCMRegistrationAsyncTask;
import com.liferay.alerts.util.GCMUtil;
import com.liferay.alerts.util.NotificationUtil;
import com.liferay.alerts.util.SettingsUtil;
import com.liferay.alerts.widget.CardView;
import com.liferay.mobile.android.util.Validator;

import java.util.ArrayList;
import java.util.List;

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
		_send = (TextView)findViewById(R.id.send);
		_userName = (TextView)findViewById(R.id.user_name);

		_send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(
					MainActivity.this, SendActivity.class);

				startActivity(intent);
			}

		});

		try {
			TextView version = (TextView)findViewById(R.id.version);
			PackageInfo info = getPackageManager().getPackageInfo(
				getPackageName(), 0);

			version.setText(info.versionName);
		}
		catch (PackageManager.NameNotFoundException nnfe) {
		}

		SettingsUtil.init(this);

		if (state != null) {
			_alerts = state.getParcelableArrayList(_ALERTS);
		}
		else {
			_alerts = AlertDAO.getInstance(this).getInstance(this).get();
		}

		for (Alert alert : _alerts) {
			_addCard(alert);
		}

		_addPushNotificationsDevice();
		_registerAddCardReceiver();
		_checkSendPermission();
	}

	@Override
	protected void onDestroy() {
		_getBroadcastManager().unregisterReceiver(_receiver);

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();

		_paused = true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		_paused = false;

		NotificationUtil.cancel(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);

		state.putParcelableArrayList(_ALERTS, _alerts);
	}

	private void _addCard(Alert alert) {
		_cardList.addView(new CardView(this, alert), 0);
		_userName.setText(alert.getUser(this).getFullName());
	}

	private void _addPushNotificationsDevice() {
		if (!SettingsUtil.isRegistered() &&
			GCMUtil.isGooglePlayServicesAvailable(this)) {

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

	private void _checkSendPermission() {
		String email = SettingsUtil.getEmail(this);
		String password = SettingsUtil.getPassword(this);

		if (Validator.isNull(email) || Validator.isNull(password)) {
			return;
		}

		_send.setVisibility(View.VISIBLE);
	}

	private LocalBroadcastManager _getBroadcastManager() {
		return LocalBroadcastManager.getInstance(this);
	}

	private void _registerAddCardReceiver() {
		_receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(final Context context, Intent intent) {
				final Alert alert = intent.getParcelableExtra(Alert.ALERT);
				_alerts.add(alert);

				_addCard(alert);

				if (_paused) {
					AlertDAO dao = AlertDAO.getInstance(context);
					List<Alert> alerts = dao.getUnread();

					NotificationUtil.notify(context, alerts);
				}
			}

		};

		_getBroadcastManager().registerReceiver(
			_receiver, new IntentFilter(ADD_CARD));
	}

	private static final String _ALERTS = "alerts";

	private ArrayList<Alert> _alerts;
	private LinearLayout _cardList;
	private boolean _paused;
	private BroadcastReceiver _receiver;
	private TextView _send;
	private TextView _userName;

}