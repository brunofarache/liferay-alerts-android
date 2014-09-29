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

package com.liferay.alerts.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;

import android.net.Uri;

import android.util.AttributeSet;
import android.util.TypedValue;

import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.liferay.alerts.R;
import com.liferay.alerts.callback.AddVoteCallback;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.AlertType;
import com.liferay.alerts.model.PollsQuestion;
import com.liferay.alerts.model.PollsQuestion.PollsChoice;
import com.liferay.alerts.model.User;
import com.liferay.alerts.util.FontUtil;
import com.liferay.alerts.util.PortraitUtil;
import com.liferay.alerts.util.SettingsUtil;
import com.liferay.alerts.util.ToastUtil;
import com.liferay.mobile.android.PushNotificationsDeviceService;
import com.liferay.mobile.android.pushnotificationsdevice.PushNotificationsDeviceServiceImpl;
import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.service.SessionImpl;
import com.liferay.mobile.android.task.callback.typed.JSONObjectAsyncTaskCallback;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author Bruno Farache
 */
public class CardView extends LinearLayout implements View.OnClickListener {

	public static void setRadioGroupEnabled(RadioGroup group, boolean enabled) {
		for (int i = 0; i < group.getChildCount(); i++) {
			group.getChildAt(i).setEnabled(enabled);
		}
	}

	public CardView(Context context) {
		this(context, (AttributeSet)null);
	}

	public CardView(Context context, Alert alert) {
		this(context);

		User user = alert.getUser(context);

		if (user != null) {
			setPortrait(context, user.getUuid(), user.getPortraitId());
		}

		setText(alert.getMessage());

		AlertType type = alert.getType();
		_url = alert.getUrl();

		if (_url != null) {
			if (!_url.startsWith("http://") && !_url.startsWith("https://")) {
				_url = "http://" + _url;
			}

			if (type == AlertType.LINK) {
				setOnClickListener(this);
				setLink();
			}
			else if (type == AlertType.IMAGE) {
				setImage(context);
			}
		}

		if (type == AlertType.POLLS) {
			setChoices(context, alert);
		}

		setTimestamp(alert.getFormattedTimestamp());
		setBackground();
		setType(alert.getType());
	}

	public CardView(Context context, AttributeSet attributes) {
		this(context, attributes, 0);
	}

	public CardView(
		Context context, AttributeSet attributes, int defaultStyle) {

		super(context, attributes, defaultStyle);

		inflate(context, R.layout.card, this);

		_text = (TextView)findViewById(R.id.text);
		_timestamp = (TextView)findViewById(R.id.timestamp);
		_type = (com.liferay.alerts.widget.TextView)findViewById(R.id.type);

		TypedArray typed = context.getTheme().obtainStyledAttributes(
			attributes, R.styleable.CardView, 0, 0);

		String text = typed.getString(R.styleable.CardView_text);

		if (text != null) {
			setText(text);
		}

		int portraitId = typed.getInt(R.styleable.CardView_portraitId, 0);

		if (portraitId != 0) {
			setPortrait(context, null, portraitId);
		}

		setTimestamp(getResources().getString(R.string.release_date));
		setBackground();
		setType(AlertType.TEXT);

		typed.recycle();
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_url));
		getContext().startActivity(intent);
	}

	public void setBackground() {
		Resources resources = getResources();

		int backgroundColor = resources.getColor(R.color.card_background);
		int borderColor = resources.getColor(R.color.card_border);
		float arrowY = resources.getDimensionPixelSize(R.dimen.card_arrow_y);
		float arrowHeight = resources.getDimensionPixelSize(
				R.dimen.card_arrow_height);

		float arrowWidth = resources.getDimensionPixelSize(
			R.dimen.card_arrow_width);;

		TypedValue outValue = new TypedValue();
		getResources().getValue(R.dimen.card_corner_radius, outValue, true);
		float cornerRadius = outValue.getFloat();

		CardShape borderShape = new CardShape(
			Paint.Style.STROKE, borderColor, arrowY, arrowHeight, arrowWidth,
			cornerRadius);

		CardShape backgroundShape = new CardShape(
			Paint.Style.FILL, backgroundColor, arrowY, arrowHeight, arrowWidth,
			cornerRadius);

		Drawable[] layers = {
			new ShapeDrawable(borderShape), new ShapeDrawable(backgroundShape)
		};

		LinearLayout card = (LinearLayout)findViewById(R.id.card);
		card.setBackground(new LayerDrawable(layers));
	}

	public void setImage(Context context) {
		ImageView image = (ImageView)findViewById(R.id.image);
		image.setVisibility(View.VISIBLE);

		Picasso.with(context).load(_url).into(image);
	}

	public void setLink() {
		TextView link = (TextView)findViewById(R.id.link);

		link.setVisibility(View.VISIBLE);
		link.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		link.setText(_url);
	}

	public void setText(String text) {
		_text.setText(text);
	}

	public void setTimestamp(String timestamp) {
		_timestamp.setText(timestamp);
	}

	public void setType(AlertType type) {
		Resources resources = getResources();
		int left = _type.getPaddingLeft();
		int top = _type.getPaddingTop();
		int right = _type.getPaddingRight();
		int bottom = _type.getPaddingBottom();

		if (type == AlertType.TEXT) {
			left = resources.getDimensionPixelSize(
				R.dimen.type_text_padding_left);
		}
		else if (type == AlertType.LINK) {
			top = resources.getDimensionPixelSize(
				R.dimen.type_link_padding_top);

			left = resources.getDimensionPixelSize(
				R.dimen.type_link_padding_left);

			_type.setTextSize(
				TypedValue.COMPLEX_UNIT_PX,
				resources.getDimensionPixelSize(R.dimen.type_link_text));
		}

		_type.setPadding(left, top, right, bottom);
		_type.setText(type.getText());
		_type.setBackgroundResource(type.getBackground());
	}

	protected void setChoices(final Context context, final Alert alert) {
		try {
			PollsQuestion question = alert.getPollsQuestion();
			RadioGroup group = (RadioGroup)findViewById(R.id.choices);
			boolean enabled = true;

			List<PollsChoice> choices = question.getChoices();

			for (PollsChoice choice : choices) {
				RadioButton button = new RadioButton(context);
				Resources resources = getResources();

				button.setId(choice.getChoiceId());
				button.setText(choice.getDescription());
				button.setTextColor(resources.getColor(R.color.card_text));
				button.setTextSize(
					TypedValue.COMPLEX_UNIT_PX,
					resources.getDimensionPixelSize(R.dimen.card_text_size));

				button.setTypeface(
					FontUtil.getFont(context, FontUtil.ROBOTO_LIGHT));

				group.addView(button);

				if (choice.isChecked()) {
					button.setChecked(true);
					enabled = false;
				}
			}

			group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int choiceId) {
					setRadioGroupEnabled(group, false);

					long questionId = (Long)group.getTag();

					String server = SettingsUtil.getServer(context);
					Session session = new SessionImpl(server);
					JSONObjectAsyncTaskCallback callback = new AddVoteCallback(
						context, alert, group);

					session.setCallback(callback);

					PushNotificationsDeviceService service =
						new PushNotificationsDeviceServiceImpl(session);

					try {
						service.addVote(questionId, choiceId);
					}
					catch (Exception e) {
						ToastUtil.show(context, R.string.vote_failure, true);
						setRadioGroupEnabled(group, true);
					}
				}

			});

			group.setTag(question.getQuestionId());
			group.setVisibility(View.VISIBLE);
			setRadioGroupEnabled(group, enabled);
		}
		catch (Exception e) {
		}
	}

	protected void setPortrait(Context context, String uuid, long portraitId) {
		ImageView portrait = (ImageView)findViewById(R.id.portrait);

		if (isInEditMode()) {
			portrait.setImageResource(R.drawable.launcher);

			return;
		}

		PortraitUtil.setPortrait(context, uuid, portraitId, portrait);
	}

	private TextView _text;
	private TextView _timestamp;
	private com.liferay.alerts.widget.TextView _type;
	private String _url;

}