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

package com.liferay.alerts.widget.card;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;

/**
 * @author Bruno Farache
 */
public class CardShape extends Shape {

	public CardShape(
		Style style, int color, float arrowY, float arrowHeight,
		float arrowWidth, float strokeWidth, float cornerRadius) {

		super();

		_style = style;
		_color = color;
		_strokeWidth = strokeWidth;
		_cornerRadius = cornerRadius;

		_arrowPoints = new PointF[] {
			new PointF(arrowHeight, (arrowY + arrowWidth)),
			new PointF(0, (arrowY + arrowWidth/2)),
			new PointF(arrowHeight, arrowY)
		};
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		paint.setAntiAlias(true);
		paint.setColor(_color);
		paint.setDither(true);
		paint.setStrokeWidth(_strokeWidth);
		paint.setStyle(_style);

		RectF roundRect = new RectF(
			_arrowPoints[0].x, 0, getWidth(), getHeight());

		Path arrow = new Path();

		arrow.moveTo(_arrowPoints[0].x, _arrowPoints[0].y);
		arrow.lineTo(_arrowPoints[1].x, _arrowPoints[1].y);
		arrow.lineTo(_arrowPoints[2].x, _arrowPoints[2].y);
		arrow.close();

		if (_style == Style.STROKE) {
			roundRect.inset(_strokeWidth, _strokeWidth);
			arrow.offset(1, 0);
		}
		else if (_style == Style.FILL_AND_STROKE) {
			float inset = _strokeWidth * 2;
			roundRect.inset(inset, inset);
			arrow.offset(inset, 0);
		}

		canvas.drawRoundRect(roundRect, _cornerRadius, _cornerRadius, paint);
		canvas.drawPath(arrow, paint);
	}

	private PointF[] _arrowPoints;
	private int _color;
	private float _cornerRadius;
	private float _strokeWidth;
	private Style _style;

}