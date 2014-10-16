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

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.liferay.alerts.R;
import com.liferay.alerts.model.Alert;
import com.liferay.alerts.model.AlertType;
import com.liferay.alerts.model.PollsChoice;
import com.liferay.alerts.model.PollsQuestion;
import com.liferay.alerts.model.User;
import com.liferay.alerts.util.FontUtil;
import com.liferay.alerts.util.PortraitUtil;
import com.liferay.alerts.util.Validator;
import com.liferay.mobile.android.PushNotificationsDeviceService;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author Bruno Farache
 */
public class CardView extends LinearLayout implements View.OnClickListener {

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

		if (Validator.isNotNull(_url)) {
			if (!_url.startsWith("http://") && !_url.startsWith("https://")) {
				_url = "http://" + _url;
			}

			if (type == AlertType.LINK) {
				setLink();
			}
			else if (type == AlertType.IMAGE) {
				setImage(context);
			}

			setOnClickListener(this);
		}

		if (type == AlertType.POLLS) {
			setPolls(context.getApplicationContext(), alert);
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
			R.dimen.card_arrow_width);

		TypedValue value = new TypedValue();
		resources.getValue(R.dimen.card_border_width, value, true);
		float borderWidth = value.getFloat();

		value = new TypedValue();
		resources.getValue(R.dimen.card_corner_radius, value, true);
		float cornerRadius = value.getFloat();

		CardShape borderShape = new CardShape(
			Paint.Style.STROKE, borderColor, arrowY, arrowHeight, arrowWidth,
			borderWidth, cornerRadius);

		CardShape backgroundShape = new CardShape(
			Paint.Style.FILL_AND_STROKE, backgroundColor, arrowY, arrowHeight,
			arrowWidth, borderWidth, cornerRadius);

		Drawable[] layers = {
			new ShapeDrawable(borderShape), new ShapeDrawable(backgroundShape)
		};

		FrameLayout card = (FrameLayout)findViewById(R.id.card);
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

		if (type == AlertType.POLLS) {
			right = resources.getDimensionPixelSize(
				R.dimen.type_polls_padding_right);
		}
		else if (type == AlertType.TEXT) {
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

	protected void setPolls(Context context, Alert alert) {
		try {
			PollsQuestion question = alert.getPollsQuestion();
			RadioGroup group = (RadioGroup)findViewById(R.id.polls);

			List<PollsChoice> choices = question.getChoices();

			int c = (int)'A' - 1;

			for (PollsChoice choice : choices) {
				c++;

				RadioButton button = new RadioButton(context);
				Resources resources = getResources();

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

			final long alertId = alert.getId();

			group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int choiceId) {
					int questionId = group.getId();

					PushNotificationsDeviceService.addVote(
						getContext(), alertId, questionId, choiceId);
				}

			});

			group.setId(question.getQuestionId());
			group.setVisibility(View.VISIBLE);
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