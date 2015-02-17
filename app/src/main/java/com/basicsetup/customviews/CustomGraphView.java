/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 *		ThreadedImages.java
 * @Project:
 *		Demo
 * @Abstract:
 *		
 * @Copyright:
 * Copyright © 2012-2013, Fukat Public 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/*! Revision history (Most recent first)
 Created by pankaj on 14-May-2014
 */
package com.basicsetup.customviews;

import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.basicsetup.R;

public class CustomGraphView extends SurfaceView implements
		SurfaceHolder.Callback {

	private static class GraphDrawingInfo {

		private Rect arrayLineRect[] = new Rect[5];
		private int circleLeft[] = new int[5];
		private int circleTop[] = new int[5];
	}

	public static class GraphCircleInfo {

		public int time = 0;
		public boolean isCorrect = false;
	}

	private DrawRunnable drawRunnable = null;
	private float width = 500, height = 100;

	private int mSpaceBetweenBars, mPaddingTop, mPaddingBotom;
	private int mBarWidh = 2, mWhiteCircleRadius = 6,
			mIndicatorCircleRadius = 4, mConnectorWidth = 3,
			mNumberOfQuestions = 5, mTextSize = 10, mTextMarginTop = 15,
			mMaxTimeInTimer = (20000/1000);

	private GraphDrawingInfo graphInfo = null, graphInfo2 = null;

	private Map<Integer, GraphCircleInfo> hashMapParamsGraph1 = null,
			hashMapParamsGraph2 = null;

	public CustomGraphView(Context context) {
		this(context, null);
	}

	public CustomGraphView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		parse((context.obtainStyledAttributes(attrs, R.styleable.RhythmAttrib)));
		initializeView(context, attrs);
	}

	private void initializeView(Context context, AttributeSet attrs) {
		Log.d("NEW", "initializeView -> ");

		graphInfo = new GraphDrawingInfo();
		graphInfo2 = new GraphDrawingInfo();

		getHolder().addCallback(this);

		mPaddingTop = getPaddingTop();
		mPaddingBotom = getPaddingBottom();

	}

	private void parse(TypedArray array) {

		if (array.hasValue(R.styleable.RhythmAttrib_bar_width)) {
			mBarWidh = (int) array.getDimension(
					R.styleable.RhythmAttrib_bar_width, 0);
		}
		if (array.hasValue(R.styleable.RhythmAttrib_white_circle_radius)) {
			mWhiteCircleRadius = (int) array.getDimension(
					R.styleable.RhythmAttrib_white_circle_radius, 0);
		}

		if (array.hasValue(R.styleable.RhythmAttrib_indicator_circle_radius)) {
			mIndicatorCircleRadius = (int) array.getDimension(
					R.styleable.RhythmAttrib_indicator_circle_radius, 0);
		}
		if (array.hasValue(R.styleable.RhythmAttrib_question_number_text_size)) {
			mTextSize = (int) array.getDimension(
					R.styleable.RhythmAttrib_question_number_text_size, 0);
		}
		if (array.hasValue(R.styleable.RhythmAttrib_connector_width)) {
			mConnectorWidth = (int) array.getDimension(
					R.styleable.RhythmAttrib_connector_width, 0);
		}
		if (array
				.hasValue(R.styleable.RhythmAttrib_question_number_text_margin_top)) {
			mTextMarginTop = (int) array
					.getDimension(
							R.styleable.RhythmAttrib_question_number_text_margin_top,
							0);
		}
		array.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		Log.d("NEW", "onMeasure -> ");

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.d("NEW", "onSizeChanged -> ");

		width = getWidth();
		height = getHeight();

		Log.d("WASTE", "OnMeasure -> " + "width::" + width + " <> height::"
				+ height);
		mSpaceBetweenBars = (int) (width / 6);
		generateRectForGraph1();
		generateRectForGraph2();
	}

	private void generateRectForGraph1() {

		int left = 0, right = 0;
		int barBottom = (int) (height - (mPaddingBotom + mTextSize + mTextMarginTop));
		int barHeight = barBottom - mPaddingTop;

		if (hashMapParamsGraph1 != null) {
			for (int i = 0; i < hashMapParamsGraph1.size(); i++) {
				left = right + mSpaceBetweenBars;
				right = left + mBarWidh;

				graphInfo.arrayLineRect[i] = new Rect(left, mPaddingTop, right,
						barBottom);

				graphInfo.circleLeft[i] = left + (mBarWidh / 2);

				int time = hashMapParamsGraph1.get(i).time;
				graphInfo.circleTop[i] = (int) (mPaddingTop + (time * barHeight)
						/ mMaxTimeInTimer);
			}
		}

	}

	private void generateRectForGraph2() {

		int left = 0, right = 0;
		int barBottom = (int) (height - (mPaddingBotom + mTextSize + mTextMarginTop));
		int barHeight = barBottom - mPaddingTop;

		if (hashMapParamsGraph2 != null) {
			for (int i = 0; i < hashMapParamsGraph2.size(); i++) {
				left = right + mSpaceBetweenBars;
				right = left + mBarWidh;

				graphInfo2.arrayLineRect[i] = new Rect(left, mPaddingTop,
						right, barBottom);

				graphInfo2.circleLeft[i] = left + (mBarWidh / 2);

				int time = hashMapParamsGraph2.get(i).time;
				graphInfo2.circleTop[i] = (int) (mPaddingTop + (time * barHeight)
						/ mMaxTimeInTimer);

				Log.d("CHECK", "time:" + time + " barHeight:" + barBottom
						+ " CircleTop:" + graphInfo2.circleTop[i]);
			}
		}

	}

	private void drawBackground(Canvas canvas) {
		Paint paintBg = new Paint();
		paintBg.setAlpha(50);
		canvas.drawPaint(paintBg);
	}

	private void drawStripes(Canvas canvas) {

		Paint paintStripes = new Paint();
		paintStripes.setColor(Color.GRAY);

		Paint paintText = new Paint();
		paintText.setColor(Color.WHITE);
		paintText.setTextSize(mTextSize);

		if (hashMapParamsGraph1 != null) {
			for (int i = 0; i < hashMapParamsGraph1.size(); i++) {

				drawQuestionStripes(i, graphInfo, paintStripes, paintText,
						canvas);
			}
		}

	}

	private void drawGraph1(Canvas canvas) {

		/*
		 * Inner Correct Circle
		 */
		Paint paintRed = new Paint();
		paintRed.setAntiAlias(true);
		paintRed.setColor(Color.RED);

		/*
		 * Inner inCorrect Circle
		 */
		Paint paintGreen = new Paint();
		paintGreen.setAntiAlias(true);
		paintGreen.setColor(Color.GREEN);

		/*
		 * OuterCircle Circle
		 */
		Paint paintWhite = new Paint();
		paintWhite.setAntiAlias(true);
		paintWhite.setColor(Color.WHITE);

		/*
		 * LineConnector of Circles
		 */
		Paint paintyellow = new Paint();
		paintyellow.setColor(Color.YELLOW);
		paintyellow.setAntiAlias(true);
		paintyellow.setStyle(Paint.Style.STROKE);
		paintyellow.setStrokeJoin(Paint.Join.ROUND);
		paintyellow.setStrokeCap(Paint.Cap.ROUND);
		paintyellow.setStrokeWidth(mConnectorWidth);

		if (hashMapParamsGraph1 != null) {
			for (int i = 0; i < hashMapParamsGraph1.size(); i++) {

				drawCircleConnectorLines(i, graphInfo, paintyellow, canvas);

				if (hashMapParamsGraph1.get(i).isCorrect) {
					drawCircles(i, graphInfo, hashMapParamsGraph1.get(i),
							paintWhite, paintGreen, canvas);
				} else {
					drawCircles(i, graphInfo, hashMapParamsGraph1.get(i),
							paintWhite, paintRed, canvas);
				}

			}
		}
	}

	private void drawGraph2(Canvas canvas) {

		/*
		 * Inner Circle
		 */
		Paint paintGrey = new Paint();
		paintGrey.setAntiAlias(true);
		paintGrey.setColor(Color.GRAY);

		/*
		 * OuterCircle Circle
		 */
		Paint paintWhite = new Paint();
		paintWhite.setAntiAlias(true);
		paintWhite.setColor(Color.WHITE);

		/*
		 * LineConnector of Circles
		 */
		Paint paintWhiteSemiTrans = new Paint();
		paintWhiteSemiTrans.setColor(Color.WHITE);
		paintWhiteSemiTrans.setAntiAlias(true);
		paintWhiteSemiTrans.setStyle(Paint.Style.STROKE);
		paintWhiteSemiTrans.setStrokeJoin(Paint.Join.ROUND);
		paintWhiteSemiTrans.setStrokeCap(Paint.Cap.ROUND);
		paintWhiteSemiTrans.setStrokeWidth(mConnectorWidth);
		paintWhiteSemiTrans.setAlpha(100);

		Paint paintText = new Paint();
		paintText.setTextSize(mTextSize);

		if (hashMapParamsGraph2 != null) {
			for (int i = 0; i < hashMapParamsGraph2.size(); i++) {

				drawCircleConnectorLines(i, graphInfo2, paintWhiteSemiTrans,
						canvas);

				drawCircles(i, graphInfo2, hashMapParamsGraph2.get(i),
						paintWhite, paintGrey, canvas);

			}
		}
	}

	public void doDraw(Canvas canvas) {

		Log.d("NEW", "mWhiteCircleRadius:" + mWhiteCircleRadius);
		drawBackground(canvas);

		drawStripes(canvas);

		drawGraph1(canvas);

		drawGraph2(canvas);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("NEW", "surfaceCreated -> ");

		drawRunnable = new DrawRunnable(getHolder(), this);
		drawRunnable.setRun(true);
		new Thread(drawRunnable).start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	};

	private class DrawRunnable implements Runnable {

		private Canvas canvas = null;
		private SurfaceHolder holder = null;
		private CustomGraphView surfaceView = null;
		private boolean mRun = false;

		public void setRun(boolean mRun) {
			this.mRun = mRun;
		}

		DrawRunnable(SurfaceHolder holder, CustomGraphView surfaceView) {
			this.holder = holder;
			this.surfaceView = surfaceView;
		}

		@Override
		public void run() {

			canvas = holder.lockCanvas();
			if (canvas != null) {
				surfaceView.doDraw(canvas);
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	public void setParametersXY1(Map<Integer, GraphCircleInfo> hashMapParams) {
		this.hashMapParamsGraph1 = hashMapParams;
	}

	public void setParametersXY2(Map<Integer, GraphCircleInfo> hashMapParams) {
		this.hashMapParamsGraph2 = hashMapParams;
	}

	private void drawQuestionStripes(int pos, GraphDrawingInfo graphInfo,
			Paint paintStripes, Paint paintText, Canvas canvas) {

		canvas.drawRect(graphInfo.arrayLineRect[pos], paintStripes);

		canvas.drawText(String.valueOf(pos + 1),
				graphInfo.arrayLineRect[pos].left,
				graphInfo.arrayLineRect[pos].bottom + mTextMarginTop, paintText);
	}

	private void drawCircles(int pos, GraphDrawingInfo graphInfo,
			GraphCircleInfo graphCircleInfo, Paint paintOuterCircle,
			Paint paintInnerCircle, Canvas canvas) {

		canvas.drawCircle(graphInfo.circleLeft[pos], graphInfo.circleTop[pos],
				mWhiteCircleRadius, paintOuterCircle);
		canvas.drawCircle(graphInfo.circleLeft[pos], graphInfo.circleTop[pos],
				mIndicatorCircleRadius, paintInnerCircle);

	}

	private void drawCircleConnectorLines(int pos, GraphDrawingInfo graphInfo,
			Paint paint, Canvas canvas) {

		if (pos < mNumberOfQuestions - 1) {

			canvas.drawLine(graphInfo.circleLeft[pos],
					graphInfo.circleTop[pos], graphInfo.circleLeft[pos + 1],
					graphInfo.circleTop[pos + 1], paint);
		}

	}

}
