/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 * TimeView.java
 * @Project:
 *		Rhythm
 * @Abstract:
 *		
 * @Copyright:
 *     		Copyright © 2014 Saregama India Ltd. All Rights Reserved
 *			Written under contract by Robosoft Technologies Pvt. Ltd.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
/*
 Created by pankaj and adarsha on 27-Mar-2014
 */

package com.basicsetup.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.basicsetup.BasicSetupApplication;
import com.basicsetup.R;
import com.basicsetup.customviews.autofittext.TypeFaceProvider;

public class CustomTimeView extends View {

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

	private float mProgressAngle = 0;

	private String mTime = "";

	private int v_width = 0;
	private int v_height = 0;

	// for painting the bitmap
	private Paint mProgressBarPaint = null;
	private Paint mSolidCirclePaint = null;
	private TextPaint mTextPaint = null;
	private Paint mProgressPathPaint = null;
	private Paint mProgressBarCapPaint = null;
	private Paint mBitmapPaint = null;

	// Used as a drawing primitive
	private RectF mArcCircle = null;
	private RectF mSolidCircle;

	// default values are added
	private int mProgressBarColor = 0xFF741d87;
	private int mProgressBarPathColor = 0xFF0fcfe5;
	private int mProgressCapColor = 0xAAa6edfe;
	private int mSolidCircleColor = 0xFF2760ab;
	private int mProgressTextRedColor = 0xFFe64c65;
	private int mTextColor = 0xFFefe0ef;

	private String mFontName = null;
	private Bitmap mBitmap = null;
	// text
	int mTextSize = 12;
	int mProgressBarThickness = 10;
	int mProgressBarPathThickness = 10;
	int mProgressBarCapRadius = 5;

	public CustomTimeView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// parse the attributes in xml
		parse((context.obtainStyledAttributes(attrs, R.styleable.RhythmAttrib)));
	}

	private void parse(TypedArray array) {

		mTextSize = (int) array.getDimension(
				R.styleable.RhythmAttrib_text_size, mTextSize);

		mProgressBarThickness = (int) array.getDimension(
				R.styleable.RhythmAttrib_progressbar_thickness,
				mProgressBarThickness);

		mProgressBarPathThickness = (int) array.getDimension(
				R.styleable.RhythmAttrib_progressbar_path_thickness,
				mProgressBarPathThickness);

		mProgressBarCapRadius = (int) array.getDimension(
				R.styleable.RhythmAttrib_progressbar_cap_radius,
				mProgressBarCapRadius);

		mProgressBarColor = array.getColor(
				R.styleable.RhythmAttrib_progress_bar_color, mProgressBarColor);
		mProgressBarPathColor = array.getColor(
				R.styleable.RhythmAttrib_progress_path_color,
				mProgressBarPathColor);
		mProgressCapColor = array.getColor(
				R.styleable.RhythmAttrib_progressbar_cap_color,
				mProgressCapColor);
//		mSolidCircleColor = array
//				.getColor(R.styleable.RhythmAttrib_filled_circle_color,
//						mSolidCircleColor);
		mTextColor = array.getColor(R.styleable.RhythmAttrib_text_color,
				mTextColor);

		if (array.hasValue(R.styleable.RhythmAttrib_font_type))
			mFontName = array.getString(R.styleable.RhythmAttrib_font_type);

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
		setUpCircleBounds();
		setUpPaint();
		invalidate();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private void setUpPaint() {

		mProgressBarPaint = new Paint();
		mProgressBarPaint.setColor(mProgressBarColor);
		mProgressBarPaint.setAntiAlias(true);
		mProgressBarPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mProgressBarPaint.setStyle(Paint.Style.STROKE);
		mProgressBarPaint.setStrokeWidth(mProgressBarThickness);
		mProgressBarPaint.setStrokeCap(Paint.Cap.ROUND);

		mSolidCirclePaint = new Paint();		
		mSolidCirclePaint.setColor(mSolidCircleColor);
		mSolidCirclePaint.setAntiAlias(true);
		mSolidCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mSolidCirclePaint.setStyle(Paint.Style.FILL);	

		mProgressPathPaint = new Paint();
		mProgressPathPaint.setColor(mProgressBarPathColor);
		mProgressPathPaint.setAntiAlias(true);
		mProgressPathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mProgressPathPaint.setStyle(Paint.Style.STROKE);
		mProgressPathPaint.setStrokeWidth(mProgressBarThickness);
		mProgressPathPaint.setStrokeCap(Paint.Cap.BUTT);

		mProgressBarCapPaint = new Paint();
		mProgressBarCapPaint.setColor(mProgressCapColor);
		mProgressBarCapPaint.setAntiAlias(true);
		mProgressBarCapPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mProgressBarCapPaint.setStyle(Paint.Style.FILL);
		mProgressBarCapPaint.setStrokeCap(Paint.Cap.ROUND);

		mTextPaint = new TextPaint();
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(mTextColor);

		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

		if (mFontName != null) {
			Typeface tf = TypeFaceProvider.getTypeFace(getContext(), mFontName);
			mTextPaint.setTypeface(tf);
		}
	}

	private void setUpCircleBounds() {
		int minValue = Math.min(v_width, v_height);
		int xOffset = v_width - minValue;
		int yOffset = v_height - minValue;

		int paddingTop = this.getPaddingTop() + (yOffset / 2);
		int paddingBottom = this.getPaddingBottom() + (yOffset / 2);
		int paddingLeft = this.getPaddingLeft() + (xOffset / 2);
		int paddingRight = this.getPaddingRight() + (xOffset / 2);
		
		float arcCircleOffset = mProgressBarThickness
				+ mProgressBarCapRadius;
		mArcCircle = new RectF(paddingLeft + arcCircleOffset, paddingTop
				+ arcCircleOffset, this.getLayoutParams().width - paddingRight
				- arcCircleOffset, this.getLayoutParams().height
				- paddingBottom - arcCircleOffset);

		float solidCircleOffset = mProgressBarThickness + mProgressBarCapRadius;
		mSolidCircle = new RectF(paddingLeft + solidCircleOffset, paddingTop
				+ solidCircleOffset, this.getLayoutParams().width
				- paddingRight - solidCircleOffset,
				this.getLayoutParams().height - paddingBottom
						- solidCircleOffset);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		Rect src = new Rect();
		src.set(0, 0, mBitmap.getScaledWidth(BasicSetupApplication.getDisplayMatrix()), mBitmap
				.getScaledHeight(BasicSetupApplication.getDisplayMatrix()));

		Rect dest = new Rect();
		int progressBarThicknessByTwo = mProgressBarPathThickness / 2;
		dest.set(progressBarThicknessByTwo, progressBarThicknessByTwo, v_width
				- progressBarThicknessByTwo, v_height
				- progressBarThicknessByTwo);

		// canvas.drawBitmap(mBitmap, src, dest, mBitmapPaint);

		canvas.drawCircle(mSolidCircle.centerX(), mSolidCircle.centerY(),
				mSolidCircle.height() / 2, mSolidCirclePaint);
		
		canvas.drawCircle(mArcCircle.centerX(), mArcCircle.centerY(),
				mArcCircle.height() / 2, mProgressPathPaint);

		canvas.drawArc(mArcCircle, -90, 360 - mProgressAngle, false,
				mProgressBarPaint);

		double angle = (mProgressAngle - 90) * (Math.PI / 180d);
		float radius = (mArcCircle.height() / 2);

		float cx = (float) ((radius * Math.cos(angle)) + mArcCircle.centerX());
		float cy = (float) ((radius * Math.sin(angle)) + mArcCircle.centerY());

		// canvas.drawCircle(cx, cy, mProgressBarCapRadius,
		// mProgressBarCapPaint);

		float textHeight = mTextPaint.descent() - mTextPaint.ascent();
		float verticalTextOffset = (textHeight / 2) - mTextPaint.descent();

		if (Integer.parseInt(mTime) < 4) {
			mTextPaint.setColor(mProgressTextRedColor);
		} else {
			mTextPaint.setColor(mTextColor);
		}

		canvas.drawText(mTime,
				mSolidCircle.centerX() - mTextPaint.measureText(mTime) / 2,
				mSolidCircle.centerY() + verticalTextOffset, mTextPaint);

	}

	public void increament(float progressAngle, String timeRemaining) {
		mProgressAngle = mProgressAngle + progressAngle;
		mTime = timeRemaining;
		mProgressHandler.sendEmptyMessage(0);
	}

	public void resetProgress() {
		mProgressAngle = 0;
		mTime = "";
		mProgressHandler.sendEmptyMessage(0);
	}

	public void initTimeView() {
		// mTimeRemaining = timeRemaining;
		mProgressAngle = 0;
		mTime = "";
		invalidate();

	}
}
