package com.basicsetup;

import android.app.Application;
import android.content.Context;

import com.basicsetup.db.DbConfiguration;
import com.basicsetup.db.DbHelper;

public class BasicSetupApplication extends Application {

	private static Context applicationContext;

	public BasicSetupApplication() {
		applicationContext = this;
	}
	
	@Override
	public void onCreate() { 
		super.onCreate();		
		 DbHelper.getInstance(applicationContext,
		 DbConfiguration.getInstance(getApplicationContext()));
	}
	public static Context getBasicSetupApplicationContext() {
		return applicationContext;
	}
	
}
