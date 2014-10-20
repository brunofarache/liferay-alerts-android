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

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * @author Bruno Farache
 */
public class ImageCardViewTransformation implements Transformation {

	public ImageCardViewTransformation(Context context) {
		_context = context.getApplicationContext();
	}

	@Override
	public String key() {
		return "card-image";
	}

	@Override
	public Bitmap transform(Bitmap bitmap) {
		BitmapShader shader = new BitmapShader(
			bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setShader(shader);

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Bitmap output = Bitmap.createBitmap(
			width, height, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(output);

		RectF rect = new RectF(16, 0, width, height);
		canvas.drawRoundRect(rect, 10, 10, paint);

		bitmap.recycle();

		return output;
	}

	private Context _context;

}