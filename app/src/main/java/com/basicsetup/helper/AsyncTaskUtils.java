
/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 *		STAAsyncTaskUtils.java
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

import android.annotation.SuppressLint;
import android.os.AsyncTask;

/**
 * @author pankaj
 *
 */
public class AsyncTaskUtils {

    @SuppressLint("NewApi")
	public static void execute(AsyncTask task,Object obj[]){
	if(AndroidVersionUtil.getAndroidVersion()<14){
	    
	    task.execute(obj);
	
	}else{
	
	    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, obj);
	
	}
    }
}
