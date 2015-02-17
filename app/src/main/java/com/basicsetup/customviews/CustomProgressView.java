package com.basicsetup.customviews;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.basicsetup.BasicSetupApplication;
import com.basicsetup.R;

public class CustomProgressView extends View {

	private int v_width = 0;
	private int v_height = 0;

	private float mProgressAngle = 0;

	private int mInnerImageCounter = 0;

	// for painting the bitmap
	private Paint mProgressBarPaint = null;
	private Paint mProgressPathPaint = null;
	private Paint mSolidCirclePaint = null;
	private Paint mProgressBarCapPaint = null;

	// Used as a drawing primitive
	private RectF mSolidCircle = null;
	private RectF mArcCircle = null;

	// color fields with default color values
	private int mSolidCircleColor = 0xFF420e3c;
	private int mProgressBarColor = 0xFF01fdff;
	private int mProgressCapColor = 0xFF01feff;
	private int mProgressPathColor = 0x5596005c;

	private int mProgressBarThickness = 10, mProgressBarArcPadding = 0;
	private int mProgressCapRadius = 10;

	private Bitmap mOuterBitmap = null;
	private Bitmap mInnerBitmap = null;
	private Bitmap mImageBitmap = null;
	private Timer timer = null;
	private Handler handler = null;

	private int[] mImageDrawablesForFlipping = { R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher };

	public static interface IProgressViewFinished {
		void onProgressViewFinished();
	}

	private IProgressViewFinished mProgressFinsihed;

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

	public CustomProgressView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public CustomProgressView(Context context) {
		super(context);
	}

	public CustomProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);

		Log.d("audio", " coming here ");
		// parse the attributes in xml
		parse((context.obtainStyledAttributes(attrs, R.styleable.RhythmAttrib)));
		handler = new Handler();
	}

	private void parse(TypedArray array) {

		mProgressBarThickness = (int) array.getDimension(
				R.styleable.RhythmAttrib_progressbar_thickness,
				mProgressBarThickness);

		mProgressBarArcPadding = (int) array.getDimension(
				R.styleable.RhythmAttrib_progressbar_arc_padding,
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

		if (array.hasValue(R.styleable.RhythmAttrib_outer_image_src)) {
			Drawable drawable = array
					.getDrawable(R.styleable.RhythmAttrib_outer_image_src);

			if (mOuterBitmap == null) {
				mOuterBitmap = ((BitmapDrawable) drawable).getBitmap();
			}
		}

		if (array.hasValue(R.styleable.RhythmAttrib_inner_image_src)) {
			Drawable drawable = array
					.getDrawable(R.styleable.RhythmAttrib_inner_image_src);

			if (mInnerBitmap == null) {
				mInnerBitmap = ((BitmapDrawable) drawable).getBitmap();
			}
		}

		array.recycle();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (getLayoutParams().width != ViewGroup.LayoutParams.WRAP_CONTENT) {
			v_width = w;
			v_height = h;
		}
		setUpCircleBounds();
		setUpPaint();
		invalidate();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
			v_width = mOuterBitmap.getScaledWidth(BasicSetupApplication
					.getDisplayMatrix());
			v_height = mOuterBitmap.getScaledHeight(BasicSetupApplication
					.getDisplayMatrix());
			setMeasuredDimension(v_width, v_height);
		}
	}

	private void setUpPaint() {
		mProgressBarPaint = new Paint();
		mProgressBarPaint.setColor(mProgressBarColor);
		mProgressBarPaint.setAntiAlias(true);
		mProgressBarPaint.setStyle(Paint.Style.STROKE);
		mProgressBarPaint.setStrokeWidth(mProgressBarThickness);
		mProgressBarPaint.setStrokeCap(Paint.Cap.ROUND);

		mSolidCirclePaint = new Paint();
		mSolidCirclePaint.setColor(mSolidCircleColor);
		mSolidCirclePaint.setAntiAlias(true);
		mSolidCirclePaint.setStyle(Paint.Style.FILL);

		mProgressPathPaint = new Paint();
		mProgressPathPaint.setColor(mProgressPathColor);
		mProgressPathPaint.setAntiAlias(true);
		mProgressPathPaint.setStyle(Paint.Style.STROKE);
		mProgressPathPaint.setStrokeWidth(mProgressBarThickness);
		mProgressPathPaint.setStrokeCap(Paint.Cap.BUTT);

		mProgressBarCapPaint = new Paint();
		mProgressBarCapPaint.setColor(mProgressCapColor);
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

		float arcCircleOffset = mProgressBarThickness / 2 + mProgressCapRadius;
		mArcCircle = new RectF(paddingLeft + arcCircleOffset
				+ mProgressBarArcPadding, paddingTop + arcCircleOffset
				+ mProgressBarArcPadding, v_width
				- (paddingRight + mProgressBarArcPadding) - arcCircleOffset,
				v_height - (paddingBottom + mProgressBarArcPadding)
						- arcCircleOffset);

		float solidCircleOffset = mProgressBarThickness + mProgressCapRadius;
		mSolidCircle = new RectF(paddingLeft + solidCircleOffset, paddingTop
				+ solidCircleOffset, this.getLayoutParams().width
				- paddingRight - solidCircleOffset,
				this.getLayoutParams().height - paddingBottom
						- solidCircleOffset);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		Rect outerBitmapDestRect = new Rect();
		outerBitmapDestRect.set(0, 0, v_width, v_height);
		Rect outerBitmapSrcRect = new Rect();
		outerBitmapSrcRect.set(0, 0, v_width, v_height);
		canvas.drawBitmap(mOuterBitmap, outerBitmapSrcRect,
				outerBitmapDestRect, new Paint());

		if (mInnerBitmap != null) {
			int innerBitmapWidth = mInnerBitmap
					.getScaledWidth(BasicSetupApplication.getDisplayMatrix());
			int innerBitmapHeight = mInnerBitmap
					.getScaledHeight(BasicSetupApplication.getDisplayMatrix());
			Rect innerBitmapDestRect = new Rect();
			int halfWidthDiff = (v_width - innerBitmapWidth) / 2;
			int halfheightDifference = (v_height - innerBitmapHeight) / 2;
			innerBitmapDestRect.set(halfWidthDiff, halfheightDifference,
					halfWidthDiff + innerBitmapWidth, halfheightDifference
							+ innerBitmapHeight);
			Rect innerBitmapSrcRect = new Rect();
			innerBitmapSrcRect.set(0, 0, innerBitmapWidth, innerBitmapHeight);
			canvas.drawBitmap(mInnerBitmap, innerBitmapSrcRect,
					innerBitmapDestRect, new Paint());
		}

		canvas.drawCircle(mArcCircle.centerX(), mArcCircle.centerY(),
				(mArcCircle.height()) / 2, mProgressPathPaint);

		canvas.drawArc(mArcCircle, -90, mProgressAngle, false,
				mProgressBarPaint);

		double angle = (mProgressAngle - 90) * (Math.PI / 180d);
		float radius = (mArcCircle.height() / 2);

		float cx = (float) ((radius * Math.cos(angle)) + mArcCircle.centerX());
		float cy = (float) ((radius * Math.sin(angle)) + mArcCircle.centerY());

		canvas.drawCircle(cx, cy, mProgressCapRadius, mProgressBarCapPaint);

	}

	public void increament(float progressAngle) {
		if (mProgressAngle != 360f) {
			mProgressAngle = progressAngle;
			mProgressHandler.sendEmptyMessage(0);
		} else {
			resetProgress();
		}
	}

	public void changeInnerBitmap() {
		mInnerImageCounter += 1;

		if (mInnerImageCounter >= mImageDrawablesForFlipping.length) {
			mInnerImageCounter = 0;
		}

		Drawable drawable = getResources().getDrawable(
				mImageDrawablesForFlipping[mInnerImageCounter]);
		mInnerBitmap = ((BitmapDrawable) drawable).getBitmap();

		mProgressHandler.sendEmptyMessage(0);
		Log.d("AUDIO", "mInnerImageCounter:" + mInnerImageCounter);
	}

	public void resetProgress() {
		if (mProgressAngle != 0) {
			mProgressAngle = 0;
			mProgressHandler.sendEmptyMessage(0);
		}
	}

	private int finishDrawingInSeconds = 0, drawAfterEveryMillis = 100;
	private float angleToDraw = 0;

	public int getFinishDrawingInSeconds() {
		return finishDrawingInSeconds;
	}

	public void setFinishDrawingInSeconds(int finishDrawingInSeconds) {
		this.finishDrawingInSeconds = finishDrawingInSeconds;
	}

	public void start(int finishDrawingInSeconds) {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		resetProgress();
		timer = new Timer();
		this.finishDrawingInSeconds = finishDrawingInSeconds;
		angleToDraw = (360f / (finishDrawingInSeconds * (1000 / drawAfterEveryMillis)));

		timer.scheduleAtFixedRate(new Task(), 0, drawAfterEveryMillis);
		Log.d("WASTE", "drawAfterEveryMillis:" + drawAfterEveryMillis);
	}

	private class Task extends TimerTask {

		@Override
		public void run() {

			float angle = mProgressAngle + angleToDraw;
			if (angle > 370f) {
				// cancelTimer();
				timeExpired();
			} else {
				increament(angle);
				Log.d("WASTE", "angle:" + angle);
			}

		}
	};

	private void cancelTimer() {
		timer.cancel();
	}

	public void timeExpired() {

		if (mProgressFinsihed != null) {

			handler.post(new Runnable() {

				@Override
				public void run() {
					mProgressFinsihed.onProgressViewFinished();
				}
			});
		}
	}

	public void setmProgressFinsihed(IProgressViewFinished mProgressFinsihed) {
		this.mProgressFinsihed = mProgressFinsihed;
	}

}
