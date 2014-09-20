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
		float arrowWidth, float cornerRadius) {

		super();

		_style = style;
		_color = color;
		_cornerRadius = cornerRadius;

		_arrowPoints = new PointF[] {
			new PointF(arrowHeight, (arrowY + arrowWidth)),
			new PointF(0, (arrowY + arrowWidth/2)),
			new PointF(arrowHeight, arrowY)
		};

		_arrowPath = new Path();
		_arrowPath.moveTo(_arrowPoints[0].x, _arrowPoints[0].y);
		_arrowPath.lineTo(_arrowPoints[1].x, _arrowPoints[1].y);
		_arrowPath.lineTo(_arrowPoints[2].x, _arrowPoints[2].y);
		_arrowPath.close();
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		paint.setStyle(_style);
		paint.setColor(_color);

		if (_style == Style.STROKE) {
			paint.setAntiAlias(true);
			paint.setDither(true);
			paint.setStrokeWidth(0);
		}

		canvas.drawRoundRect(_roundRect, _cornerRadius, _cornerRadius, paint);

		if (_style == Style.FILL) {
			canvas.drawPath(_arrowPath, paint);
		}
		else if (_style == Style.STROKE) {
			canvas.drawLine(
				_arrowPoints[0].x, _arrowPoints[0].y, _arrowPoints[1].x,
				_arrowPoints[1].y, paint);

			canvas.drawLine(
				_arrowPoints[1].x, _arrowPoints[1].y, _arrowPoints[2].x,
				_arrowPoints[2].y, paint);
		}
	}

	@Override
	public void onResize(float width, float height) {
		_roundRect = new RectF(_arrowPoints[0].x, 0, width, height);
	}

	private Path _arrowPath;
	private PointF[] _arrowPoints;
	private int _color;
	private float _cornerRadius;
	private RectF _roundRect;
	private Style _style;

}