/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 * BluringActivity.java
* @Project:
*		Demo
* @Abstract:
*		

* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
/*
Created by pankaj on 27-Mar-2014
 */

package com.basicsetup.bluring;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.basicsetup.R;

public class BluringActivity extends Activity implements OnClickListener {

	private Button btnLogin;
	private Bitmap bmp;
	private DisplayMetrics metrics;
	private long timeInMillis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluring_activity);

		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		Log.d("WASTE", "Resolution = Hieght:" + metrics.heightPixels
				+ " width:" + metrics.widthPixels);

		btnLogin = (Button) findViewById(R.id.btnBlur);
		btnLogin.setOnClickListener(this);

	}

	class BlurAsync extends AsyncTask<Bitmap, Void, BitmapDrawable> {

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = ProgressDialog.show(BluringActivity.this, "", "wait ...");
		}

		@Override
		protected BitmapDrawable doInBackground(Bitmap... bmp) {

			BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
					Blur.fastblur(getApplicationContext(), bmp[0], 25));

			return bitmapDrawable;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(BitmapDrawable result) {
			super.onPostExecute(result);
			dialog.dismiss();

			if (VERSION.SDK_INT >= 16) {
				((ImageView) findViewById(R.id.imgBlur)).setBackground(result);
			} else {
				((ImageView) findViewById(R.id.imgBlur))
						.setBackgroundDrawable(result);
			}

			Toast.makeText(
					BluringActivity.this,
					(System.currentTimeMillis() - timeInMillis) / 1000
							+ " seconds", Toast.LENGTH_LONG).show();

		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void onClick(View view) {

		timeInMillis = System.currentTimeMillis();

		bmp = BitmapProcess.getScaledBitmap(
				Environment.getExternalStorageDirectory() + File.separator
						+ "tom_1920_1080.jpg", metrics.heightPixels,
				metrics.widthPixels);

		Log.d("WASTE",
				"Result = Hieght:" + bmp.getHeight() + " width:"
						+ bmp.getWidth());

		if (VERSION.SDK_INT >= 14) {
			((ImageView) findViewById(R.id.imgBlur))
					.setBackground(new BitmapDrawable(getResources(), bmp));
		} else {
			((ImageView) findViewById(R.id.imgBlur))
					.setBackgroundDrawable(new BitmapDrawable(getResources(),
							bmp));
		}

		BlurAsync async = new BlurAsync();
		async.execute(bmp);
	}
}
