package com.basicsetup.helper;
 

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

public class HashKeyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		
		try {
		    PackageInfo info = getPackageManager().getPackageInfo("com.saregama.rhythm",
		                                PackageManager.GET_SIGNATURES);
		    for (Signature signature : info.signatures) {
		        MessageDigest md = MessageDigest.getInstance("SHA");
		        md.update(signature.toByteArray());
		        Log.i("WASTE", Base64.encodeToString(md.digest(), 0));
		    }
		} catch (NameNotFoundException e) {
		    Log.e("Test", e.getMessage());
		} catch (NoSuchAlgorithmException e) {
		    Log.e("Test", e.getMessage());
		}
	}
}
