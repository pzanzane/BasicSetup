package com.basicsetup.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.basicsetup.R;

public class CustomProgressBar extends ProgressBar {

	private int v_width = 0;
	private int v_height = 0;

	private float mProgressAngle = 0;

	// for painting the bitmap
	private Paint mProgressBarPaint = null;
	private Paint mProgressPathPaint = null;
	private Paint mSolidInnerCirclePaint = null;
	private Paint mSolidOuterCirclePaint = null;
	private Paint mProgressBarCapPaint = null;

	// Used as a drawing primitive
	private RectF mSolidInnerCircle = null;
	private RectF mSolidOuterCircle = null;
	private RectF mArcCircle = null;

	// color fields with default color values
	private int mSolidInnerCircleColor = 0xFF943999;
	private int mSolidOuterCircleColor = 0xFF5c2161;
	private int mProgressBarColor = 0xFF01fdff;
	private int mProgressCapStartColor = 0xA501feff;
	private int mProgressCapEndColor = 0x6801faff;
	private int mProgressPathColor = 0x5596005c;
	private int mProgressBarThickness = 10;
	private int mProgressCapRadius = 15;
	private int mOuterAndInnerCircleGap = 20;
	private int mProgressBarLength = 45;

	public CustomProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
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

		mProgressCapStartColor = array.getColor(
				R.styleable.RhythmAttrib_progressbar_cap_startcolor,
				mProgressCapStartColor);

		mProgressCapEndColor = array.getColor(
				R.styleable.RhythmAttrib_progressbar_cap_endcolor,
				mProgressCapEndColor);

		mSolidInnerCircleColor = array.getColor(
				R.styleable.RhythmAttrib_filled_circle_color,
				mSolidInnerCircleColor);
		mOuterAndInnerCircleGap = (int) array.getDimension(
				R.styleable.RhythmAttrib_progressbar_margin,
				mOuterAndInnerCircleGap);

		mProgressBarLength = (int) array
				.getDimension(R.styleable.RhythmAttrib_progressbar_length,
						mProgressBarLength);

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
		mProgressBarPaint.setStyle(Paint.Style.STROKE);
		mProgressBarPaint.setStrokeWidth(mProgressBarThickness);
		mProgressBarPaint.setStrokeCap(Paint.Cap.ROUND);

		mSolidInnerCirclePaint = new Paint();
		mSolidInnerCirclePaint.setColor(mSolidInnerCircleColor);
		mSolidInnerCirclePaint.setAntiAlias(true);
		mSolidInnerCirclePaint.setStyle(Paint.Style.FILL);

		mSolidOuterCirclePaint = new Paint();
		mSolidOuterCirclePaint.setColor(mSolidOuterCircleColor);
		mSolidOuterCirclePaint.setAntiAlias(true);
		mSolidOuterCirclePaint.setStyle(Paint.Style.FILL);

		mProgressPathPaint = new Paint();
		mProgressPathPaint.setColor(mProgressPathColor);
		mProgressPathPaint.setAntiAlias(true);
		mProgressPathPaint.setStyle(Paint.Style.STROKE);
		mProgressPathPaint.setStrokeWidth(mProgressBarThickness);
		mProgressPathPaint.setStrokeCap(Paint.Cap.BUTT);

		mProgressBarCapPaint = new Paint();
		mProgressBarCapPaint.setColor(mProgressCapStartColor);
		mProgressBarCapPaint.setAntiAlias(true);
		mProgressBarCapPaint.setStyle(Paint.Style.FILL);
		mProgressBarCapPaint.setStrokeCap(Paint.Cap.ROUND);
	}

	private void setUpCircleBounds() {
		int minValue = Math.min(v_width, v_height);
		int xOffset = v_width - minValue;
		int yOffset = v_height - minValue;

		int paddingTop = this.getPaddingTop() + (yOffset / 2);
		int paddingBottom = this.getPaddingBottom() + (yOffset / 2);
		int paddingLeft = this.getPaddingLeft() + (xOffset / 2);
		int paddingRight = this.getPaddingRight() + (xOffset / 2);

		mSolidOuterCircle = new RectF(paddingLeft, paddingTop,
				this.getLayoutParams().width - paddingRight,
				this.getLayoutParams().height - paddingBottom);

		mArcCircle = new RectF(paddingLeft + mOuterAndInnerCircleGap,
				paddingTop + mOuterAndInnerCircleGap,
				this.getLayoutParams().width - paddingRight
						- mOuterAndInnerCircleGap,
				this.getLayoutParams().height - paddingBottom
						- mOuterAndInnerCircleGap);

		int solidOuterCircleOffset = mOuterAndInnerCircleGap
				+ mProgressBarThickness/2;
		mSolidInnerCircle = new RectF(paddingLeft + solidOuterCircleOffset,
				paddingTop + solidOuterCircleOffset,
				this.getLayoutParams().width - paddingRight
						- solidOuterCircleOffset, this.getLayoutParams().height
						- paddingBottom - solidOuterCircleOffset);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		canvas.drawCircle(mSolidOuterCircle.centerX(),
				mSolidOuterCircle.centerY(), mSolidOuterCircle.height() / 2,
				mSolidOuterCirclePaint);

		canvas.drawCircle(mSolidInnerCircle.centerX(),
				mSolidInnerCircle.centerY(), mSolidInnerCircle.height() / 2,
				mSolidInnerCirclePaint);

		canvas.drawCircle(mArcCircle.centerX(), mArcCircle.centerY(),
				mArcCircle.height() / 2, mProgressPathPaint);

		canvas.drawArc(mArcCircle, mProgressAngle - 90, mProgressBarLength,
				false, mProgressBarPaint);		

		double angle = (mProgressBarLength + mProgressAngle - 90) * (Math.PI / 180d);
		float radius = (mArcCircle.height() / 2);

		float cx = (float) ((radius * Math.cos(angle)) + mArcCircle.centerX());
		float cy = (float) ((radius * Math.sin(angle)) + mArcCircle.centerY());

		LinearGradient linearGradient = new LinearGradient(cx
				- mProgressCapRadius, cy - mProgressCapRadius, cx
				+ mProgressCapRadius, cy + mProgressCapRadius,
				mProgressCapStartColor, mProgressCapEndColor,
				Shader.TileMode.CLAMP);
		mProgressBarCapPaint.setShader(linearGradient);
		canvas.drawCircle(cx, cy, mProgressCapRadius, mProgressBarCapPaint);

		if (mProgressAngle > 360) {
			mProgressAngle = 0;
		}
		mProgressAngle = mProgressAngle + 5;
	}
}
