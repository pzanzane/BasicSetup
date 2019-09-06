/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 *		ToastUtils.java
 * @Project:
 *		Rhythm
 * @Abstract:
 *		
 * @Copyright:
 *     		Copyright © 2014 Saregama India Ltd. All Rights Reserved
 *			Written under contract by Robosoft Technologies Pvt. Ltd.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* 
 *  Created by pankaj on 29-May-2014
 */

package com.basicsetup.helper;

import com.basicsetup.BasicSetupApplication;

import android.widget.Toast;

/**
 * @author pankaj
 * 
 */
public class ToastUtils {

	private static Toast toast = null;

	public static void showToast(String str) {

		if (toast == null)
			toast = Toast.makeText(
					BasicSetupApplication.getBasicApplicationContext(), str,
					Toast.LENGTH_SHORT);

		toast.setText(str);
		toast.show();
	}
}
