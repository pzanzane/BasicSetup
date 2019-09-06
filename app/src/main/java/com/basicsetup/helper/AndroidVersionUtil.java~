/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 *		STAAndroidVersionUtil.java
 * @Project:
 *		Stardom
 * @Abstract:
 *		
 * @Copyright:
 *     		Copyright © 2012-2013, Viacom 18 Media Pvt. Ltd 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/*! Revision history (Most recent first)
 Created by pankaj on 02-Jan-2014
 */

package com.basicsetup.helper;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * @author pankaj
 * 
 */
public class AndroidVersionUtil {

	public static int getAndroidVersion() {
		return Build.VERSION.SDK_INT;
	}

	public static boolean isBeforeHoneyComb() {
		return (Build.VERSION.SDK_INT < 11) ? true : false;
	}

	public static String getUniqueDeviceIdentification(Context context) {

		/*
		 * Combination of deviceId,AndroidId and SerialNumber
		 */
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		StringBuilder sb = new StringBuilder();

		sb.append(TextUtils.isEmpty(telephonyManager.getDeviceId()) ? ""
				: telephonyManager.getDeviceId());

		sb.append(TextUtils.isEmpty(Settings.Secure.getString(
				context.getContentResolver(), Settings.Secure.ANDROID_ID)) ? ""
				: Settings.Secure.getString(context.getContentResolver(),
						Settings.Secure.ANDROID_ID));

		sb.append(TextUtils.isEmpty(android.os.Build.SERIAL) ? ""
				: android.os.Build.SERIAL);

		return sb.toString();

	}
}
