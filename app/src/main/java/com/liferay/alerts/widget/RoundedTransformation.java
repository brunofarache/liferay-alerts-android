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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.liferay.alerts.R;

import com.squareup.picasso.Transformation;

/**
 * @author Bruno Farache
 */
public class RoundedTransformation implements Transformation {

	public RoundedTransformation(Context context) {
		_context = context.getApplicationContext();
	}

	@Override
	public String key() {
		return "rounded";
	}

	@Override
	public Bitmap transform(Bitmap bitmap) {
		BitmapShader shader = new BitmapShader(
			bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setShader(shader);

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float x = width / 2;
		float y = height / 2;
		float radius = width / 2;

		Bitmap output = Bitmap.createBitmap(
			width, height, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(output);
		canvas.drawCircle(x, y, radius, paint);

		Bitmap shadow = BitmapFactory.decodeResource(
			_context.getResources(), R.drawable.portrait_shadow);

		canvas.drawBitmap(shadow, 0, 0, paint);

		bitmap.recycle();

		return output;
	}

	private Context _context;

}