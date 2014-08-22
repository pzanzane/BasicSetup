/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 * FacebookManager.java
 * @Project:
 *		Rhythm
 * @Abstract:
 *		
 * @Copyright:
 *     		Copyright © 2014 Saregama India Ltd. All Rights Reserved
 *			Written under contract by Robosoft Technologies Pvt. Ltd.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
/*
 Created by pankaj and adarsha on 27-Mar-2014
 */

package com.basicsetup.facebook;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.basicsetup.R;
import com.basicsetup.helper.PreferenceUtils;
import com.basicsetup.webservices.ParcelableNameValuePair;
import com.basicsetup.webservices.WebserviceConstants;
import com.basicsetup.webservices.WebserviceModel;
import com.basicsetup.webservices.WebserviceUtils;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

/**
 * 
 * Class FacebookManager
 * 
 * Acts as a wrapper class for facebook api's
 * 
 * @author adarsh
 * 
 * 
 */
public class FacebookManager implements Session.StatusCallback {

	private static final String MESSAGE = "message";

	private static FacebookManager mFacebookManager = null;
	private OnFBLogInListener mFbLogInListener = null;

	private FacebookManager() {
	}

	private static List<ParcelableNameValuePair> getParamsList() {
		List<ParcelableNameValuePair> params = new ArrayList<ParcelableNameValuePair>();
		params.add(new ParcelableNameValuePair("fields",
				"name,picture.type(square).width(200).height(200)"));
		params.add(new ParcelableNameValuePair(
				WebserviceConstants.ACCESS_TOKEN, getAccessToken()));
		return params;
	}

	public static FacebookManager getFacebookManager() {

		if (mFacebookManager == null) {
			mFacebookManager = new FacebookManager();
		}
		return mFacebookManager;

	}

	@Override
	public void call(Session session, SessionState state, Exception exception) {
		if (exception != null && mFbLogInListener != null) {
			mFbLogInListener.onError(exception);
		}
		if (session.isOpened() && mFbLogInListener != null) {
			mFbLogInListener.onSessionOpen();
		}
	}

	public static String getAccessToken() {
		Session session = Session.getActiveSession();
		if (session != null) {
			return session.getAccessToken();
		}
		return null;
	}

	/**
	 * set the listener to notify the login event
	 */
	public void setLoginListener(OnFBLogInListener listener) {
		mFbLogInListener = listener;
	}

	/**
	 * Add login behaviour (set the token)
	 */
	public static void addLoginBehaviour() {
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
	}

	/**
	 * To check whether fb is already signed in or not
	 * 
	 * @return boolean value true if already logged in else false
	 */
	public static boolean isLoggedIn() {
		Session session = Session.getActiveSession();
		Log.d("register", "session : " + session);

		if (session != null && session.isOpened()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public static void getUserInfo(Context context, Class callerClass) {

		Log.d("register", "coming here : getuserinfo");

		WebserviceModel webModel = WebserviceUtils.createWebModel(callerClass,
				WebserviceConstants.REQ_GET_USERS_FACEBOOK_INFO,
				WebserviceConstants.REQUEST_PRIORITY_HIGH,
				WebserviceConstants.METHODE_TYPE_GET, null, getParamsList(),
				false);
		WebserviceUtils.hitWebService(context, webModel);
	}

	/**
	 * 
	 * To list the friends of the logged in user
	 */
	public static void getFriendList(Context context, Class classname) {
		Log.d("register", "coming here : getFriendList");

		WebserviceModel webModel = WebserviceUtils.createWebModel(classname,
				WebserviceConstants.REQ_GET_USERS_FB_FRIENDS,
				WebserviceConstants.REQUEST_PRIORITY_HIGH,
				WebserviceConstants.METHODE_TYPE_GET, null, getParamsList(),
				false);
		WebserviceUtils.hitWebService(context, webModel);
	}

	/**
	 * To send notification or message to face book user
	 */
	public void sendInvitation(Activity activity) {
		Bundle params = new Bundle();
		params.putString(MESSAGE,
				"Hi there, Saregama is full of fun, Lets play it");
		Log.d("invite", "invitation sent");
		WebDialog requestDialog = new WebDialog.RequestsDialogBuilder(activity,
				Session.getActiveSession(), params).build();
		requestDialog.setOnCompleteListener((OnCompleteListener) activity);
		requestDialog.show();
	}

	public static void disconnect() {
		Session session = Session.getActiveSession();

		if (session != null) {
			session = null;
		}
	}

	/**
	 * to share or post the contents
	 * 
	 */
	public void share(Activity activity, String name, String points) {
		if (FacebookDialog.canPresentShareDialog(activity,
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			// Publish the post using the Share Dialog

			UiLifecycleHelper uiHelper = new UiLifecycleHelper(activity, null);
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
					activity).setName(name)
					.setDescription(name + " scored " + points)
					.setLink("https://developers.facebook.com/android").build();
			uiHelper.trackPendingDialogCall(shareDialog.present());

		} else {
			publishFeedDialog(activity, name, points);
		}
	}

	private void publishFeedDialog(final Activity activity, String name,
			String points) {
		Bundle params = new Bundle();
		params.putString("name", name);
		params.putString("caption", "Play antakshari with anu kapoor");
		params.putString("description", name + " scored " + points);
		params.putString("link", "https://developers.facebook.com/android");
		params.putString("picture", null);

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(activity,
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(activity,
										"Posted story, id: " + postId,
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								Toast.makeText(
										activity.getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(activity.getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							Toast.makeText(activity.getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}
					}

				}).build();
		feedDialog.show();
	}

	// send facebook id if its request for only one facebook id or send invite
	// all list
	public static void getAppAccessToken(Context context, Class callerClass,
			String facebookId, List<String> facebookIds) {
		List<ParcelableNameValuePair> params = new ArrayList<ParcelableNameValuePair>();
		params.add(new ParcelableNameValuePair("client_id", context
				.getString(R.string.app_id)));
		params.add(new ParcelableNameValuePair("client_secret", context
				.getString(R.string.app_secret)));
		params.add(new ParcelableNameValuePair("grant_type",
				"client_credentials"));

		WebserviceModel webModel = WebserviceUtils.createWebModel(callerClass,
				WebserviceConstants.REQ_GET_FACEBOOK_APP_TOKEN,
				WebserviceConstants.REQUEST_PRIORITY_HIGH,
				WebserviceConstants.METHODE_TYPE_GET, null, params, false);
		if (facebookId != null) {
			webModel.getExtraData().putString(WebserviceConstants.PARAM_FB_ID,
					facebookId);
		} else if (facebookIds != null) {
			webModel.getExtraData().putStringArrayList(
					WebserviceConstants.PARAM_INVITE_ALL,
					(ArrayList<String>) facebookIds);
		}

		WebserviceUtils.hitWebService(context, webModel);
	}

	public static void sendNotification(Context context, Class callerClass,
			String facebookId) {

		List<ParcelableNameValuePair> params = new ArrayList<ParcelableNameValuePair>();
		params.add(new ParcelableNameValuePair("href",
				"https://www.rhythm.robosoftin.com"));
		params.add(new ParcelableNameValuePair("access_token", PreferenceUtils
				.getString(context, PreferenceUtils.FACEBOOK_APP_TOKEN)));
		params.add(new ParcelableNameValuePair("template", MessageFormat
				.format("{0} has invited you to play rhythm", "basicprofile")));

		WebserviceModel webModel = WebserviceUtils.createWebModel(callerClass,
				WebserviceConstants.REQ_INVITE_FB_FRIEND,
				WebserviceConstants.REQUEST_PRIORITY_LOW,
				WebserviceConstants.METHODE_TYPE_POST, null, params, false);
		webModel.getExtraData().putString(WebserviceConstants.PARAM_FB_ID,
				facebookId);
		WebserviceUtils.hitWebService(context, webModel);
	}

	public static void sendNotification(Context context, Class callerClass,
			List<String> facebookIds) {

		for (String fbId : facebookIds) {
			sendNotification(context, callerClass, fbId);
		}
	}
}
