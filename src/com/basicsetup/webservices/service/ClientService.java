/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 * ClientService.java
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

package com.basicsetup.webservices.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.basicsetup.BasicSetupApplication;
import com.basicsetup.helper.AsyncTaskUtils;
import com.basicsetup.webservices.WebServiceHelper;
import com.basicsetup.webservices.WebServiceSingASong;
import com.basicsetup.webservices.WebserviceConstants;
import com.basicsetup.webservices.WebserviceModel;
import com.basicsetup.webservices.WebserviceUtils;
import com.basicsetup.webservices.parser.IParser;
import com.basicsetup.webservices.parser.holder.HolderBase;

public class ClientService extends Service {

	private final int MAX_THREADS = 2;

	private PriorityComparator comparator = new PriorityComparator();
	private TreeSet<WebserviceModel> priorityList = new TreeSet<WebserviceModel>(
			comparator);
	private Map<Integer, DownloadAsyncTask> taskList = new HashMap<Integer, ClientService.DownloadAsyncTask>();

	private int threadCount = 0;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) { 
		Log.d("THREAD", "InStartCommand:" + intent);
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			boolean cancelThread = false;
			if (bundle.containsKey(WebserviceConstants.EXTRA_CANCEL_THREAD))
				cancelThread = bundle
						.getBoolean(WebserviceConstants.EXTRA_CANCEL_THREAD);

			WebserviceModel model = bundle
					.getParcelable(WebserviceConstants.EXTRA_WEBSERVICE_MODEL);

			Log.d("THREAD", "cancelThread:" + cancelThread);

			if (cancelThread) {

				DownloadAsyncTask async = taskList.get(model.getRequestType());

				Log.d("THREAD",
						"Contains AsyncTask:"
								+ taskList.get(model.getRequestType()) + " <> "
								+ taskList.size());
				if (async != null) {
					async.cancel(true);
					Log.d("THREAD", "Cancelling:" + model.getRequestType());
				}

			} else {
				addToPriorityList(model);
				startThread();
			}

		}
		return START_STICKY;
	}

	private class DownloadAsyncTask extends AsyncTask<Void, Void, HolderBase> {
		private WebserviceModel webModel;

		public void setWebModel(WebserviceModel webModel) {
			this.webModel = webModel;

		}

		@Override
		protected HolderBase doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			Log.d("request", "reqType:" + webModel.getRequestType() + "->"
					+ WebserviceUtils.getUrl(webModel));

			if (!WebserviceUtils.isOnline(BasicSetupApplication.getBasicApplicationContext())) {
				return null;
			}

			String response = null;

//			if (webModel.getRequestType() == WebserviceConstants.REQ_SING_SONG_SCORE) {
//				 
//				WebServiceSingASong singSongDownloader = new WebServiceSingASong();
//				response = singSongDownloader.execute(webModel);
//				 
//			} else {

				boolean retry = webModel.isSyncInBackground();

				response = WebServiceHelper.execute(getApplicationContext(),
						webModel, retry);
//			}

			Log.d("response", "request:" + webModel.getRequestType() + "->"
					+ WebserviceUtils.getUrl(webModel));
			Log.d("response", "response: " + response);

			if (TextUtils.isEmpty(response))
				return null;

			HolderBase result = null;
			IParser<? extends HolderBase> parser = getParser(webModel
					.getRequestType());
			// Parse Response
			if (parser != null && response != null && !isCancelled())
				result = parser.parse(response);

			return result;
		}

		@Override
		protected void onPostExecute(HolderBase result) {
			super.onPostExecute(result);

			Log.d("home", "on post execute");

			taskList.remove(webModel.getRequestType());
			decrementThreadCount();
			executeNextTask();

			Log.d("response", "isCancelled : " + isCancelled());

			if (isCancelled()) {
				return;
			}

			int flag = getFlag(result);
			String errorMsg = "";
			if (result != null)
				errorMsg = result.getmErrorMsg();
			Log.d("register", "flag : " + flag);

			if (retryWebservice(webModel, flag, isCancelled())) {
				Log.d("request", "retryCount : " + webModel.getRetryCount());
				return;
			}

			Intent intent = new Intent();
			intent.putExtras(webModel.getExtraData());
			intent.putExtra(WebserviceConstants.EXTRA_ERR_MSG, errorMsg);
			switch (webModel.getRequestType()) {
//			case WebserviceConstants.REQ_CALLERTUNE:
//				
//				break;			
//			case WebserviceConstants.REQ_GET_RECORDING_URL:
//			case WebserviceConstants.REQ_GET_MIX_URL:
//				intent.setAction(RhythmConstants.ACTION_REQ_REC_URL);
//				intent.putExtra(WebserviceConstants.EXTRA_REC_URL, result);
//				intent.putExtra(WebserviceConstants.EXTRA_FLAG, flag);
//				sendBroadcast(intent);				
//				break;
		 

			default:
				break;
			}
		}

		@Override
		public int hashCode() {
			Log.d("THREAD", "ReqType:" + webModel.getRequestType()
					+ "<>HashCode:" + webModel.hashCode());

			return webModel.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			Log.d("THREAD", "Equals:" + webModel.equals(obj));
			return webModel.equals(obj);
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private static class PriorityComparator implements
			Comparator<WebserviceModel> {

		@Override
		public int compare(WebserviceModel lhs, WebserviceModel rhs) {
			return lhs.getPriority() < rhs.getPriority() ? -1 : 1;
		}

	}

	private synchronized void increamentThreadCount() {
		threadCount++;
	}

	private synchronized void decrementThreadCount() {
		threadCount--;
	}

	private void addToPriorityList(WebserviceModel model) {
		priorityList.add(model);
	}

	private void startThread() {
		Log.d("THREAD", "In startThread");
		if (threadCount < MAX_THREADS) {
			
			increamentThreadCount();
			executeNextTask();
		}
	}

	private WebserviceModel nextThread() {
		return priorityList.pollFirst();
	}

	private void executeNextTask() {
		WebserviceModel model = nextThread();
		if (model != null) {
			Log.d("THREAD", "ExecutingNewTask");
			if (!model.isSyncInBackground()
					|| WebserviceUtils.isOnline(BasicSetupApplication.getBasicApplicationContext())) {
				DownloadAsyncTask async = new DownloadAsyncTask();
				async.setWebModel(model);
				Log.d("singsongresponse", "request*******");
				AsyncTaskUtils.execute(async, null);
				taskList.put(model.getRequestType(), async);

				executeNextTask();
			}else{
				Log.d("singsongresponse", "in Else");
				decrementThreadCount();
				addToPriorityList(model);
			}
		}
	}

	private boolean retryWebservice(WebserviceModel webModel, int flag,
			boolean isCancelled) {

		if (isCancelled) {
			return false;
		}

		if (!webModel.isSyncInBackground()
				|| WebserviceUtils.isOnline(BasicSetupApplication.getBasicApplicationContext())) {
			webModel.decrementRetryCount();
		}
		// If webservice has to sync in background without interacting with
		// activity
		// It has to retry till success
		if (webModel.isSyncInBackground()
				&& (flag != WebserviceConstants.RESULT_FLAG_SUCCESS)) {

			if ((webModel.getRetryCount() > 0)) {
				/*
				 * Adding to priorityList and let itexecute in future. This is
				 * only possible if application stays alive. If application is
				 * killed then data is lost
				 */
				addToPriorityList(webModel);
			}
			return true;
		}
		return false;
	}

	private int getFlag(HolderBase result) {

		if (result == null) {
			if (WebserviceUtils.isOnline(getApplicationContext())) {
				return WebserviceConstants.RESULT_FLAG_UNKNOWN_ERROR;
			} else {
				return WebserviceConstants.RESULT_FLAG_NO_ITERNET;
			}
		}

		switch (result.getCode()) {
		case WebserviceConstants.RESULT_FLAG_SUCCESS:
		case WebserviceConstants.RESULT_FLAG_DUPLICATE_RECORD:
			return WebserviceConstants.RESULT_FLAG_SUCCESS;

//		case WebserviceConstants.RESULT_FLAG_USER_NOT_FOUND:
//			return WebserviceConstants.RESULT_FLAG_USER_NOT_FOUND;

		case WebserviceConstants.RESULT_FLAG_JSON_EXCEPTION:
			return WebserviceConstants.RESULT_FLAG_JSON_EXCEPTION;
		default:
			return WebserviceConstants.RESULT_FLAG_UNKNOWN_ERROR;
		}

	}

	private IParser<? extends HolderBase> getParser(int req) {
		switch (req) {
//		case WebserviceConstants.REQ_GET_MIX_URL:
//			return new ParserRecordingUrl();
//		case WebserviceConstants.REQ_GET_RECORDING_URL:
//			return new ParserRecordingUrl();
	 
		default:
			return null;
		}
	}
}
