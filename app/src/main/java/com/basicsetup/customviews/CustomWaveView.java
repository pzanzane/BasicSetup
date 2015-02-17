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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.basicsetup.R;

public class CustomWaveView extends SurfaceView implements
		SurfaceHolder.Callback {

	public static class GraphCircleInfo {

		public int time = 0;
		public boolean isCorrect = false;
	}

	private DrawRunnable drawRunnable = null;
	private float width = 500, height = 100;

	private int mPaddingTop, mPaddingBotom;

	public CustomWaveView(Context context) {
		this(context, null);
	}

	public CustomWaveView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initializeView(context, attrs);
	}

	private void initializeView(Context context, AttributeSet attrs) {
		Log.d("NEW", "initializeView -> ");

		getHolder().addCallback(this);

		mPaddingTop = getPaddingTop();
		mPaddingBotom = getPaddingBottom();

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

	}

	public void doDraw(Canvas canvas) {

		Path path = makeWavePath();

		Paint paint = new Paint();
		paint.setStrokeWidth(3);
		paint.setPathEffect(null);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(path, paint);
 
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
		private CustomWaveView surfaceView = null;
		private boolean mRun = false;

		public void setRun(boolean mRun) {
			this.mRun = mRun;
		}

		DrawRunnable(SurfaceHolder holder, CustomWaveView surfaceView) {
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

	private Path makeWavePath(){
		
		RectF rectF = new RectF(50, 50, 50f, 50f);
		
		Path path = new Path();
		path.moveTo(0, getHeight()/2);
		path.lineTo(50, 50);
		path.addArc(rectF, 0, 100);
		path.lineTo(100, getHeight()/2);
		
		return path;
	}
	private void drawpath(Canvas canvas) {
		// Create a straight line
		Path path = new Path();
		path.moveTo(32, 32);
		path.lineTo(232, 32);

		// Stamp a concave arrow along the line
		PathEffect effect = new PathDashPathEffect(
				makeConvexArrow(24.0f, 14.0f), // "stamp"
				36.0f, // advance, or distance between two stamps
				0.0f, // phase, or offset before the first stamp
				PathDashPathEffect.Style.ROTATE); // how to transform each stamp

		// Apply the effect and draw the path
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setPathEffect(effect);
		canvas.drawPath(path, paint);

	}

	private Path makeConvexArrow(float length, float height) {
		Path p = new Path();
		p.moveTo(0.0f, -height / 2.0f);
		p.lineTo(length - height / 4.0f, -height / 2.0f);
		p.lineTo(length, 0.0f);
		p.lineTo(length - height / 4.0f, height / 2.0f);
		p.lineTo(0.0f, height / 2.0f);
		p.lineTo(0.0f + height / 4.0f, 0.0f);
		p.close();
		return p;
	}
}
