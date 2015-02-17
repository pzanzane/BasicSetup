/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 * RHYFacebookLoginActivity.java
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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

public class RHYFacebookLoginActivity extends FragmentActivity {

	private UiLifecycleHelper uiHelper;
	private StatusCallback statusCallback = null;
	private int intentPurpose;

	private String type, name, desc, link, message, action, userId, postId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		statusCallback = new SessionStatusCallback();
		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		intentPurpose = bundle.getInt(RHYSocialWrapper.INTENT_PURPOSE);

		if (intentPurpose == RHYSocialWrapper.REQUEST_TYPE_LOGIN) {
			action = bundle.getString(RHYSocialWrapper.EXTRA_ACTION);
			facebookLogin(this, statusCallback);
		} else if (intentPurpose == RHYSocialWrapper.REQUEST_TYPE_LOGOUT) {
			facebookLogout();
		} else if (intentPurpose == RHYSocialWrapper.REQUEST_TYPE_SHARE) {

			type = bundle.getString(RHYSocialWrapper.BUNDLE_KEY_TYPE);
			name = bundle.getString(RHYSocialWrapper.BUNDLE_KEY_NAME);
			desc = bundle.getString(RHYSocialWrapper.BUNDLE_KEY_DESCR);
			link = bundle.getString(RHYSocialWrapper.BUNDLE_KEY_LINK);
			message = bundle.getString(RHYSocialWrapper.BUNDLE_KEY_MESSAGE);
			userId = bundle.getString(RHYSocialWrapper.BUNDLE_KEY_USERID);
			postId = bundle.getString(RHYSocialWrapper.BUNDLE_KEY_POSTID);

			callFacebookDialog();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d("WASTE", "onStart Facebook Login Activity:");
		onStartActivity(statusCallback);

	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("WASTE", "onStop Facebook Login Activity");
		uiHelper.onStop();
		onStopActivity(statusCallback);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("WASTE", "onActivityResult Facebook Login Activity:"
				+ requestCode + " :" + resultCode);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		onActivityResult(this, requestCode, resultCode, data);
	}

	private class SessionStatusCallback implements
			com.facebook.Session.StatusCallback {
		@Override
		public void call(com.facebook.Session session, SessionState state,
				Exception exception) {

			Intent intentResult = new Intent();

			Log.d("FB_LOGIN", "Status Callback to FacebookLoginActivity " + state);		

			if (state == SessionState.OPENED) {
				Log.d("WASTE", "SessionState OPENED");

				intentResult.putExtra(RHYSocialWrapper.EXTRA_TYPE,
						RHYSocialWrapper.TYPE_FACEBOOK);
				intentResult.putExtra(RHYSocialWrapper.EXTRA_ACCESS,
						session.getAccessToken());
				intentResult.putExtra(RHYSocialWrapper.EXTRA_STATE,
						SessionState.OPENED);

				setResult(RHYSocialWrapper.RES_FACEBOOK_LOGIN, intentResult);
				RHYFacebookLoginActivity.this.finish();
			} else if (state == SessionState.CLOSED) {

				Log.d("WASTE", "SessionState CLOSED");

				intentResult.putExtra(RHYSocialWrapper.EXTRA_TYPE,
						RHYSocialWrapper.TYPE_FACEBOOK);
				intentResult.putExtra(RHYSocialWrapper.EXTRA_ACCESS,
						session.getAccessToken());
				intentResult.putExtra(RHYSocialWrapper.EXTRA_STATE,
						SessionState.CLOSED);

				setResult(RHYSocialWrapper.RES_FACEBOOK_LOGOUT, intentResult);

				RHYFacebookLoginActivity.this.finish();
				Log.d("WASTE", "Trying to call onactivity Result");
			} else if (state == SessionState.CLOSED_LOGIN_FAILED) {
				Log.d("WASTE", "SessionState CLOSED_LOGIN_FAILED");

				intentResult.putExtra(RHYSocialWrapper.EXTRA_TYPE,
						RHYSocialWrapper.TYPE_FACEBOOK);
				intentResult.putExtra(RHYSocialWrapper.EXTRA_ACCESS,
						session.getAccessToken());
				intentResult.putExtra(RHYSocialWrapper.EXTRA_STATE,
						SessionState.CLOSED_LOGIN_FAILED);
				intentResult.putExtra(RHYSocialWrapper.EXTRA_ACTION, action);
				setResult(RHYSocialWrapper.RES_FACEBOOK_LOGIN, intentResult);
				RHYFacebookLoginActivity.this.finish();
			}

		}
	}

	private void callFacebookDialog() {

		if (TextUtils.isEmpty(link)) {
			link = "https://facebook.com/" + userId + "/posts/" + postId;
		}

		if (FacebookDialog.canPresentShareDialog(this,
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {

			FacebookDialog.ShareDialogBuilder shareDialog = new FacebookDialog.ShareDialogBuilder(
					this);

			shareDialog.setLink(link);

			Log.d("WASTE", "msg:" + message);
			shareDialog.setDescription(message);
			shareDialog.setName(name);
			shareDialog.setCaption(message);

			UiLifecycleHelper helper = new UiLifecycleHelper(this,
					statusCallback);
			helper.trackPendingDialogCall(shareDialog.build().present());
			finish();
		} else {

			String url = "https://www.facebook.com/sharer/sharer.php?u=" + link;

			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(url));
			startActivity(browserIntent);
			finish();
		}

	}

	private void facebookLogin(Activity activity,
			Session.StatusCallback statusCallback) {

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		Session session = Session.getActiveSession();
		if (session == null) {
			session = new Session(activity);
			Session.setActiveSession(session);
		}		

		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(activity)
					.setCallback(statusCallback));
		} else {
			Session.openActiveSession(activity, true, statusCallback);
		}
	}

	private void facebookLogout() {

		Session session = Session.getActiveSession();
		if (!session.isClosed()) {
			session.closeAndClearTokenInformation();
		}

	}

	private void onActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		Session.getActiveSession().onActivityResult(activity, requestCode,
				resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
		super.onSaveInstanceState(outState);
	}

	private void onStartActivity(Session.StatusCallback statusCallback) {
		Session.getActiveSession().addCallback(statusCallback);
	}

	private void onStopActivity(Session.StatusCallback statusCallback) {
		Session.getActiveSession().removeCallback(statusCallback);
	}
}
