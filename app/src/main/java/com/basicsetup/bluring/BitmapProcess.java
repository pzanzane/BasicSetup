/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 * BitmapProcess.java
* @Project:
*		Demo
* @Abstract:
*		

* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
/*
Created by pankaj on 27-Mar-2014
 */

package com.basicsetup.bluring;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class BitmapProcess {

	public static Bitmap getBitmapWithWaterMark(Context context, String path,
			Bitmap watermark, int max_height, int max_width) {

		Bitmap bmpImage = getScaledBitmap(path, max_height, max_width);

		Bitmap bmOverlay = Bitmap.createBitmap(bmpImage.getWidth(),
				bmpImage.getHeight(), bmpImage.getConfig());

		Bitmap waterMark = Bitmap.createScaledBitmap(watermark,
				bmpImage.getWidth(), bmpImage.getHeight(), false);


		Rect rectSrc = new Rect(0, 0, bmpImage.getWidth(), bmpImage.getHeight());
		Rect rectDst = new Rect(rectSrc);

		Canvas canvas = new Canvas(bmOverlay);

		canvas.drawBitmap(bmpImage, rectSrc, rectDst, null);
		canvas.drawBitmap(waterMark, rectSrc, rectDst, null);

		bmpImage.recycle();

		return bmOverlay;
	}

	public static Bitmap getScaledBitmap(String path, int max_height,
			int max_width) {

		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, o);

		Log.d("WASTE", "image H: " + o.outHeight + " Image W: " + o.outWidth
				+ " max H: " + max_height + " max W: " + max_width);

		float imageHeight = o.outHeight, imageWidth = o.outWidth;

		float scale = 1;
		float factor = 2f;

		while (imageHeight > max_height && imageWidth > max_width) {
			scale *= factor;
			imageHeight /= factor;
			imageWidth /= factor;

			Log.d("WASTE", "imgHeight:" + imageHeight + " = " + "imgWidth:"
					+ imageWidth);

		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = (int) scale;

		return BitmapFactory.decodeFile(path, options);
	}

	public static Bitmap getScaledBitmap(Bitmap bmp, int max_height,
			int max_width) {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, o);

		Log.d("WASTE", "image H: " + o.outHeight + " Image W: " + o.outWidth
				+ " max H: " + max_height + " max W: " + max_width);

		float imageHeight = o.outHeight, imageWidth = o.outWidth;

		float scale = 1;
		float factor = 2f;

		while (imageHeight > max_height && imageWidth > max_width) {
			scale *= factor;
			imageHeight /= factor;
			imageWidth /= factor;

			Log.d("WASTE", "imgHeight:" + imageHeight + " = " + "imgWidth:"
					+ imageWidth);

		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = (int) scale;

		return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,
				options);
	}
}
