package com.basicsetup.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtils {
 
	private static String PREFERENCE_NAME = "MenuslatePreferences"; 
	public static String FACEBOOK_APP_TOKEN = "facebook_app_token";
	public static void putString(Context ctx, String key, String value) {

		SharedPreferences shared = ctx.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		Editor edt = shared.edit();
		edt.putString(key, value);
		edt.commit();
	}

	public static String getString(Context ctx, String key) {

		SharedPreferences shared = ctx.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		return shared.getString(key, null);
	}

	public static void putInteger(Context ctx, String key, Integer value) {
		SharedPreferences shared = ctx.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		Editor edt = shared.edit();
		if(value!=null){
			edt.putInt(key, value);
		}
		else{
			edt.remove(key);	
		}
		edt.commit();

	}

	public static Integer getInteger(Context ctx, String key) {

		SharedPreferences shared = ctx.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		return shared.getInt(key, 0);
	}

	public static void putLong(Context ctx, String key, Long value) {
		SharedPreferences shared = ctx.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		Editor edt = shared.edit();
		edt.putLong(key, value);
		edt.commit();

	}

	public static Long getLong(Context ctx, String key) {

		SharedPreferences shared = ctx.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		return shared.getLong(key, 0);
	} 
	
	public static void putFloat(Context ctx, String key, Float value) {
		SharedPreferences shared = ctx.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		Editor edt = shared.edit();
		edt.putFloat(key, value);
		edt.commit();

	}

	public static Float getFloat(Context ctx, String key) {

		SharedPreferences shared = ctx.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		return shared.getFloat(key, 0);
	}

	public static void putBoolean(Context ctx, String key, boolean value) {
		SharedPreferences shared = ctx.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		Editor edt = shared.edit();
		edt.putBoolean(key, value);
		edt.commit();

	}

	public static Boolean getBoolean(Context ctx, String key) {

		SharedPreferences shared = ctx.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		return shared.getBoolean(key, false);
	}
}
