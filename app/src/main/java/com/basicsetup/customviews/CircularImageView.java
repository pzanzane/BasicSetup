package com.basicsetup.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class CircularImageView extends ImageView {

	int vWidth = 0;
	int vHeight = 0;

	private Paint mPaint = null;
	private BitmapShader mShader = null;

	public CircularImageView(Context context) {
		this(context, null);
	}

	public CircularImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		vWidth = w;
		vHeight = h;
		invalidate();
		super.onSizeChanged(w, h, oldw, oldh);
	}

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

	@Override
	public void setImageBitmap(Bitmap bm) {

		vWidth = getLayoutParams().width;
		vHeight = getLayoutParams().height;
		if (bm != null && vWidth > 0 && vHeight > 0) {
			mShader = new BitmapShader(Bitmap.createScaledBitmap(bm, vWidth,
					vHeight, false), Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);
		} else {
			mShader = null;
		}

		mProgressHandler.sendEmptyMessage(0);
	}

	@Override
	public void setImageResource(int resId) {

		try {
			vWidth = getLayoutParams().width;
			vHeight = getLayoutParams().height;

			Drawable drawable = getResources().getDrawable(resId);
			Bitmap bm = ((BitmapDrawable) drawable).getBitmap();

			if (bm != null) {
				mShader = new BitmapShader(Bitmap.createScaledBitmap(bm,
						vWidth, vHeight, false), Shader.TileMode.CLAMP,
						Shader.TileMode.CLAMP);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (mShader != null) {
			mPaint.setShader(mShader);
			canvas.drawCircle(vWidth / 2, vHeight / 2, vWidth / 2, mPaint);
		}
	}
}
