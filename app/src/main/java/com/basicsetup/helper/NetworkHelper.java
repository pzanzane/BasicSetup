package com.basicsetup.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkHelper {

/**
 * Checks if is online.
 * If it returns false, show toast using NetworkHelper.noNetworkToast(Context) methode
 * @param cxt the cxt
 * @return true, if is online
 */
public static boolean isOnline(Context cxt) {
		ConnectivityManager cm = (ConnectivityManager) cxt
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo(); 
		
		if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) { 
			Log.d("", "mode is online");
			return true;
		}
		return false;
	}
	
	/**
	 *not Implemented yet 
	 */	
	public static void noNetworkDialog(){
		
	}
	public static void noNetworkToast(Context context){
		Toast.makeText(context, "No Connection Available, Please try again!", Toast.LENGTH_LONG).show();
	} 

}





















