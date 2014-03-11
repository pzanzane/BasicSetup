
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

import android.os.Build;

/**
 * @author pankaj
 *
 */
public class AndroidVersionUtil {

    public static int getAndroidVersion(){
	return Build.VERSION.SDK_INT;
    }
    
    public static boolean isBeforeHoneyComb(){
	return (Build.VERSION.SDK_INT < 11)?true:false;
    }
}
