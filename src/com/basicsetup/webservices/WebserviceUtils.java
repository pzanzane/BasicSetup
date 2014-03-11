package com.basicsetup.webservices;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.basicsetup.helper.AndroidVersionUtil;
import com.basicsetup.helper.EncryptionUtils;
import com.basicsetup.helper.ParcelableNameValuePair;

public class WebserviceUtils {

	// to check network availability
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnected();
	}

	public static WebserviceModel createWebModel(int reqFor, int priority,
			int methodType, ArrayList<String> urlParams,
			List<ParcelableNameValuePair> optionalParams) {
		WebserviceModel webModel = new WebserviceModel();
		webModel.setPriority(priority);
		webModel.setRequestType(reqFor);
		webModel.setListParams(optionalParams);
		webModel.setUrlParams(urlParams);
		webModel.setMethodeType(methodType);
		return webModel;
	}

	public static void hitWebService(Context context, WebserviceModel webModel) {
		 
//			Intent intn = new Intent(context, ClientService.class);
//			intn.putExtra(WebserviceConstants.EXTRA_WEBSERVICE_MODEL, webModel);
//			context.startService(intn);
	 
	}

	public static String getSmartUrl(String inUrl) {
		String Url;

		String protocol = WebserviceConstants.PROTOCOL_HLS;
		if (AndroidVersionUtil.isBeforeHoneyComb())
			protocol = WebserviceConstants.PROTOCOL_RTSP;

		Url = MessageFormat.format("{0}{1}{2}{3}", inUrl,
				WebserviceConstants.SERVICE_ID, protocol,
				WebserviceConstants.SMART_OPTION);
		String md5Hash = EncryptionUtils.md5(WebserviceConstants.PUBLIC_KEY
				+ Url);
		String signedSmartUrl = MessageFormat.format("{0}{1}", Url, md5Hash);
		return signedSmartUrl;
	}

	public static String getUrl(WebserviceModel model) {
		switch (model.getRequestType()) {
 

		default:
			break;
		}
		return null;
	}
}
