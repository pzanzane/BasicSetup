package com.basicsetup;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.basicsetup.db.DbConfiguration;
import com.basicsetup.db.DbHelper;

public class BasicSetupApplication extends Application {

	private static Context applicationContext;
	private static String PACKAGENAME = null;
	private static DisplayMetrics matrix = null;
	private static String externalFileDirPath = "";
	private static final String parentFileDir = "saregama";

	private static final String FOLDER_NAME = "downloads";
	private static final String FOLDER__AUDIO_NAME = "audio";
	private static final String FOLDER__RECORDING_NAME = "recording";

	public BasicSetupApplication() {
		applicationContext = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		PACKAGENAME = getPackageName();
		matrix = getResources().getDisplayMetrics();
		DbHelper.getInstance(applicationContext,
				DbConfiguration.getInstance(getApplicationContext()));
 

		if (isExternalStorageWritable()) {
			externalFileDirPath = getExternalFilesDir(null).getAbsolutePath()
					+ File.separator + parentFileDir;
		} else {
			externalFileDirPath = getFilesDir().getAbsolutePath()
					+ File.separator + parentFileDir;
		}
 
	} 
 

	public static Context getBasicApplicationContext() {
		return applicationContext;
	}

	public static String getBasicPackageName() {
		return PACKAGENAME;
	}

	public static DisplayMetrics getDisplayMatrix() {
		return matrix;
	}

	public static String getDownloadsPath() {
		return externalFileDirPath + File.separator + FOLDER_NAME;
	}

	public static String getGameAudioDownloadPath() {
		return externalFileDirPath + File.separator + FOLDER__AUDIO_NAME;
	}

	public static String getRecordingPath() {
		return externalFileDirPath+ File.separator + FOLDER__RECORDING_NAME;
	}

	/* Checks if external storage is available for read and write */
	private static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(state)) {
			return true;
		}
		return false;
	}
}
