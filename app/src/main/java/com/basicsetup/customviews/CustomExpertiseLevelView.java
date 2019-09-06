/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 *		CustomProgressView.java
 * @Project:
 *		Rhythm
 * @Abstract:
 *		
 * @Copyright:
 *     		Copyright © 2014 Saregama India Ltd. All Rights Reserved
 *			Written under contract by Robosoft Technologies Pvt. Ltd.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* 
 *  Created by adarsh on 24-Apr-2014
 */

package com.basicsetup.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.basicsetup.R;
import com.basicsetup.helper.BitmapHelper;

/**
 * @author adarsh
 * 
 */
public class CustomExpertiseLevelView extends ImageView {

	private final int MAX_EXPERTISE_LEVEL = 6;
	private int expertiseLevel = 3;
	float gap = 5;

	float arc;

	private int v_width = 0;
	private int v_height = 0;

	private float START_ANGLE = -90f;
	private float mProgressAngle;

	// for painting the bitmap
	private Paint mProgressBarPaint = null;
	private Paint mProgressPathPaint = null;
	private Paint mSolidCirclePaint = null;
	private Paint mProgressBarCapPaint = null;

	// Used as a drawing primitive
	private RectF mSolidCircle = null;
	private RectF mArcCircle = null;

	// color fields with default color values
	private int mSolidCircleColor = android.R.color.transparent;
	private int mProgressBarColor = 0xFF01fdff;
	private int mProgressCapColor = 0xFF01feff;
	private int mProgressPathColor = 0x66000000;

	private int mProgressBarThickness = 10;
	private int mProgressCapRadius = 10;

	private Bitmap mBitmap = null;

	private Handler mProgressHandler = new Handler() {
		/**
		 * This is the code that will increment the progress variable and so
		 * spin the wheel
		 */
		@Override
		public void handleMessage(Message msg) {
			invalidate();
		}
	};

	public CustomExpertiseLevelView(Context context, AttributeSet attrs) {
		super(context, attrs);

		Log.d("audio", " coming here ");
		// parse the attributes in xml
		parse((context.obtainStyledAttributes(attrs, R.styleable.RhythmAttrib)));

	}

	private void parse(TypedArray array) {

		mProgressBarThickness = (int) array.getDimension(
				R.styleable.RhythmAttrib_progressbar_thickness,
				mProgressBarThickness);

		mProgressCapRadius = (int) array.getDimension(
				R.styleable.RhythmAttrib_progressbar_cap_radius,
				mProgressCapRadius);

		mProgressBarColor = array.getColor(
				R.styleable.RhythmAttrib_progress_bar_color, mProgressBarColor);

		mProgressPathColor = array.getColor(
				R.styleable.RhythmAttrib_progress_path_color,
				mProgressPathColor);

		mProgressCapColor = array.getColor(
				R.styleable.RhythmAttrib_progressbar_cap_color,
				mProgressCapColor);

		mSolidCircleColor = array
				.getColor(R.styleable.RhythmAttrib_filled_circle_color,
						mSolidCircleColor);

		gap = array.getDimension(R.styleable.RhythmAttrib_progressbar_gap, gap);

		if (array.hasValue(R.styleable.RhythmAttrib_image_src)) {
			Drawable drawable = array
					.getDrawable(R.styleable.RhythmAttrib_image_src);

			if (mBitmap == null) {
				mBitmap = ((BitmapDrawable) drawable).getBitmap();
			}
		}

		array.recycle();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		v_width = w;
		v_height = h;
		arc = (360 / MAX_EXPERTISE_LEVEL) - gap;
		setUpCircleBounds();
		setUpPaint();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private void setUpPaint() {
		mProgressBarPaint = new Paint();
		mProgressBarPaint.setColor(mProgressBarColor);
		mProgressBarPaint.setAntiAlias(true);
		mProgressBarPaint.setStyle(Paint.Style.STROKE);
		mProgressBarPaint.setStrokeWidth(mProgressBarThickness);
		mProgressBarPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mProgressBarPaint.setStrokeCap(Paint.Cap.BUTT);

		mSolidCirclePaint = new Paint();
		mSolidCirclePaint.setColor(mSolidCircleColor);
		mSolidCirclePaint.setAntiAlias(true);
		mSolidCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mSolidCirclePaint.setStyle(Paint.Style.FILL);

		mProgressPathPaint = new Paint();
		mProgressPathPaint.setColor(mProgressPathColor);
		mProgressPathPaint.setAntiAlias(true);
		mProgressPathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mProgressPathPaint.setStyle(Paint.Style.STROKE);
		mProgressPathPaint.setStrokeWidth(mProgressBarThickness);
		mProgressPathPaint.setStrokeCap(Paint.Cap.BUTT);

		mProgressBarCapPaint = new Paint();
		mProgressBarCapPaint.setColor(mProgressCapColor);
		mProgressBarCapPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mProgressBarCapPaint.setAntiAlias(true);
		mProgressBarCapPaint.setStyle(Paint.Style.FILL);
		mProgressBarCapPaint.setStrokeCap(Paint.Cap.SQUARE);
	}

	public int getExpertiseLevel() {
		return expertiseLevel;
	}

	public void setExpertiseLevel(int expertiseLevel) {
		this.expertiseLevel = expertiseLevel;
	}

	private void setUpCircleBounds() {
		int minValue = Math.min(v_width, v_height);
		int xOffset = v_width - minValue;
		int yOffset = v_height - minValue;

		int paddingTop = this.getPaddingTop() + (yOffset / 2);
		int paddingBottom = this.getPaddingBottom() + (yOffset / 2);
		int paddingLeft = this.getPaddingLeft() + (xOffset / 2);
		int paddingRight = this.getPaddingRight() + (xOffset / 2);

		float arcCircleOffset = mProgressBarThickness / 2 + mProgressCapRadius;
		mArcCircle = new RectF(paddingLeft + arcCircleOffset, paddingTop
				+ arcCircleOffset, this.getLayoutParams().width - paddingRight
				- arcCircleOffset, this.getLayoutParams().height
				- paddingBottom - arcCircleOffset);

		float solidCircleOffset = mProgressBarThickness / 2
				+ mProgressCapRadius + (gap * 0.8f);
		mSolidCircle = new RectF(paddingLeft + solidCircleOffset, paddingTop
				+ solidCircleOffset, this.getLayoutParams().width
				- paddingRight - solidCircleOffset,
				this.getLayoutParams().height - paddingBottom
						- solidCircleOffset);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		mProgressAngle = START_ANGLE;
		Log.d("WASTE", "draw");

		if (mBitmap == null)
			return;

		mBitmap = getResizedBitmap(mBitmap,
				(int) (mSolidCircle.bottom - mSolidCircle.top),
				(int) (mSolidCircle.right - mSolidCircle.left));

		// canvas.drawCircle(mSolidCircle.centerX(), mSolidCircle.centerY(),
		// mSolidCircle.height() / 2, mSolidCirclePaint);

		canvas.drawCircle(mArcCircle.centerX(), mArcCircle.centerY(),
				mArcCircle.height() / 2, mProgressPathPaint);

		for (int i = 1; i <= expertiseLevel; i++) {
			Paint paint = getPaint(i);
			canvas.drawArc(mArcCircle, mProgressAngle, arc, false, paint);
			mProgressAngle = mProgressAngle + arc;

			Paint gapPaint = new Paint();
			gapPaint.setColor(mProgressPathColor);
			gapPaint.setAntiAlias(true);
			gapPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
			canvas.drawArc(mArcCircle, mProgressAngle, gap, false, gapPaint);
			mProgressAngle = mProgressAngle + gap;
		}

		double angle = (mProgressAngle - 90) * (Math.PI / 180d);
		float radius = (mArcCircle.height() / 2);

		float cx = (float) ((radius * Math.cos(angle)) + mArcCircle.centerX());
		float cy = (float) ((radius * Math.sin(angle)) + mArcCircle.centerY());

		canvas.drawCircle(cx, cy, mProgressCapRadius, mProgressBarCapPaint);

		float left = mSolidCircle.left;
		float top = mSolidCircle.top;

		Paint bitmapPaint = new Paint();
		bitmapPaint.setAntiAlias(true);
		bitmapPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawBitmap(mBitmap, left, top, bitmapPaint);
		// (mBitmap, left, top, null);

	}

	public void increament(float progressAngle) {
		if (mProgressAngle != 360f) {
			mProgressAngle = progressAngle;
			mProgressHandler.sendEmptyMessage(0);
		} else {
			resetProgress();
		}
	}

	public void resetProgress() {
		if (mProgressAngle != 0) {
			mProgressAngle = 0;
			mProgressHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		Log.d("WASTE", "setImageBitmap");
		mBitmap = bm;
		mProgressHandler.sendEmptyMessage(0);

	}

	@Override
	@Deprecated
	public void setBackgroundDrawable(Drawable background) {
		super.setBackgroundDrawable(background);
		Log.d("WASTE", "setBackgroundDrawable");
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		Log.d("WASTE", "setImageDrawable");
		if (drawable == null || mProgressHandler == null)
			return;
		mBitmap = BitmapHelper.drawableToBitmap(drawable);
		mProgressHandler.sendEmptyMessage(0);
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, true);
		return resizedBitmap;
	}

	private Paint getPaint(int expertise) {
		Paint mProgressBarPaint = new Paint();

		mProgressBarPaint.setAntiAlias(true);
		mProgressBarPaint.setStyle(Paint.Style.STROKE);
		mProgressBarPaint.setStrokeWidth(mProgressBarThickness);
		mProgressBarPaint.setStrokeCap(Paint.Cap.SQUARE);
		mProgressBarPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

		return mProgressBarPaint;
	}
}
