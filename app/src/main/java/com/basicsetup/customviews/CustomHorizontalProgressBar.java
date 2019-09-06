package com.basicsetup.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.basicsetup.R;

public class CustomHorizontalProgressBar extends View {

	private int vWidth = 0;
	private int vHeight = 0;

	private int mProgressPathColor = 0xFF000000;
	private int mProgressBarColor = 0xFFFFFFFF;

	private Paint mProgressPathPaint = null;
	private Paint mProgressPaint = null;
	private int mProgressBarThickness = 10;

	private float mMax = 100;
	private float mStatus = 0;

	public CustomHorizontalProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		parse((context.obtainStyledAttributes(attrs, R.styleable.RhythmAttrib)));
	}

	private void parse(TypedArray array) {

		mProgressBarThickness = (int) array.getDimension(
				R.styleable.RhythmAttrib_progressbar_thickness,
				mProgressBarThickness);

		mProgressBarColor = array.getColor(
				R.styleable.RhythmAttrib_progress_bar_color, mProgressBarColor);

		mProgressPathColor = array.getColor(
				R.styleable.RhythmAttrib_progress_path_color,
				mProgressPathColor);

		array.recycle();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		vWidth = w;
		vHeight = h;
		setUpPaint();
		invalidate();
	}

	private void setUpPaint() {
		mProgressPathPaint = new Paint();
		mProgressPathPaint.setColor(mProgressPathColor);
		mProgressPathPaint.setAntiAlias(true);
		mProgressPathPaint.setStyle(Paint.Style.STROKE);
		mProgressPathPaint.setStrokeWidth(mProgressBarThickness);
		mProgressPathPaint.setStrokeCap(Paint.Cap.ROUND);

		mProgressPaint = new Paint();
		mProgressPaint.setColor(mProgressBarColor);
		mProgressPaint.setAntiAlias(true);
		mProgressPaint.setStyle(Paint.Style.STROKE);
		mProgressPaint.setStrokeWidth(mProgressBarThickness);
		mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		setMeasuredDimension(widthMeasureSpec, mProgressBarThickness);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Rect pathRect = new Rect(0, 0, vWidth, vHeight);
		canvas.drawRect(0, 0, vWidth, vHeight, mProgressPathPaint);

		int progressWidth = (int) ((20 * vWidth) / mMax);
		Rect barRect = new Rect(0, 0, progressWidth, vHeight);
		canvas.drawRect(0, 0, progressWidth, vHeight, mProgressPaint);

	}

	public void setMax(float max) {
		mMax = max;
	}

	public void setStatus(float status) {
		mStatus = status;
	}
}
