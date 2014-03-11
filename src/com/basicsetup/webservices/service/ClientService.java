package com.basicsetup.webservices.service;

import java.util.Comparator;
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

	private int threadCount = 0;

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			WebserviceModel model = bundle
					.getParcelable(WebserviceConstants.EXTRA_WEBSERVICE_MODEL);
			startThread(model);

		}
		return START_STICKY;
	}

	private class DownloadAsyncTask extends
			AsyncTask<Void, Void, HolderBase> {
		private WebserviceModel webModel;

		public void setWebModel(WebserviceModel webModel) {
			this.webModel = webModel;
		}

		@Override
		protected HolderBase doInBackground(Void... params) {
			
			if(!WebserviceUtils.isOnline(BasicSetupApplication.getBasicSetupApplicationContext())){
				return null;
			}
			String response = WebServiceHelper.execute(getApplicationContext(),
					webModel, false);

			if (TextUtils.isEmpty(response))
				return null;

			Log.d("RESPONCE", "response : " + response);

			HolderBase result = null;
			IParser<? extends HolderBase> parser = getParser(webModel
					.getRequestType());
			// Parse Response
			if (parser != null && response != null)
				result = parser.parse(response);
			return result;
		}

		@Override
		protected void onPostExecute(HolderBase result) {
			super.onPostExecute(result);

			decrementThreadCount();
			Intent intent = null;
			switch (webModel.getRequestType()) {
//			Example
//			case WebserviceConstants.REQ_GET_QUESTIONS:
//				intent = new Intent(RhythmConstants.QUESTION_ACTION);
//				intent.putExtra(WebserviceConstants.EXTRA_QUE, result);
//				intent.putExtra(WebserviceConstants.EXTRA_FLAG, getFlag(result));
//				sendBroadcast(intent);
//				break;
 
			default:
				break;
			}

			executeNextTask();
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

	private void startThread(WebserviceModel model) {

		addToPriorityList(model);

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
			DownloadAsyncTask async = new DownloadAsyncTask();
			async.setWebModel(model);
			AsyncTaskUtils.execute(async, null);
		}

	}

	private int getFlag(HolderBase result) {

		if (result == null)
			return WebserviceConstants.RESULT_FLAG_NO_ITERNET;
		switch (result.getCode()) {
		case WebserviceConstants.RESULT_FLAG_SUCCESS:
		case WebserviceConstants.RESULT_FLAG_DUPLICATE_RECORD:
			return WebserviceConstants.RESULT_FLAG_SUCCESS;			

		case WebserviceConstants.RESULT_FLAG_JSON_EXCEPTION:
			return WebserviceConstants.RESULT_FLAG_JSON_EXCEPTION;
		default:
		return WebserviceConstants.RESULT_FLAG_UNKNOWN_ERROR;
		}

	}

	private IParser<? extends HolderBase> getParser(int req) {
		switch (req) {
//		Example		
//		case WebserviceConstants.REQ_GET_QUESTIONS:
//			return new ParserQuestions();
		
		default:
			return null;
		}
	}
}
