package com.basicsetup.helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class WebServiceHelper {

	/** The Constant CONNECTION_TIMEOUT. */
	public static final int CONNECTION_TIMEOUT = 10 * 1000;

	/** The Constant WAIT_RESPONSE_TIMEOUT. */
	public static final int WAIT_RESPONSE_TIMEOUT = 20 * 1000;

	public static final int RETRY_COUNT = 1;

	private static final String SERVER_ERROR_STRING = "SERVER_ERROR",
			CONNECTION_ERROR_STRING = "CONNECTION_ERROR";

	/**
	 * Execute HTTP get.
	 * 
	 * @param url
	 *            the URL
	 * @param context
	 *            TODO
	 * @return the string
	 */
	public static String executeHttpGet(Context context, String url,
			boolean retry) {
		int count = RETRY_COUNT;
		HttpHelper httpHelper = new HttpHelper();

		String strResponce = null;
		do {
			Log.d("RETRY", "retrying :: " + url);

			try {
				strResponce = httpHelper.executeHttpGet(url, null);
				isJson(strResponce);

			} catch (SocketTimeoutException se) {

				se.printStackTrace();
				// Do something when socket timeout exception occurs
			} catch (Exception e) {
				// TODO: handle exception
				// Log.e("Error in executing HttpGet");
				// Log.e(e);
				// Do something when output is in appropriate occurs
				e.printStackTrace();
				strResponce = null;
			} finally {
				httpHelper.consumeClinet();
				if (strResponce != null)
					break;
			}
		} while (--count >= 0 && retry);

		return strResponce;
	}

	/**
	 * Execute HTTP get.
	 * 
	 * @param context
	 *            TODO
	 * @param url
	 *            The URL
	 * @param params
	 *            Use BasicNameValuePair to form List<NameValuePair>.<br/>
	 *            Pass null if no there are no parameters or use overloaded
	 *            version of the same.
	 * 
	 * @return The response string.
	 */
	public static String executeHttpGet(Context context, String url,
			List<NameValuePair> params) {

		String strResponse = null;
		HttpHelper httpHelper = null;

		String strParams = "";
		if (params != null) {
			strParams = URLEncodedUtils.format(params, "utf-8");
		}
		Log.d("test", " req : " + url + strParams);
		try {

			httpHelper = new HttpHelper();
			strResponse = httpHelper.executeHttpGet(url, params);
			isJson(strResponse);

		} catch (SocketTimeoutException se) {

			se.printStackTrace();
			// Do something when socket timeout exception occurs
		} catch (Exception e) {
			// TODO: handle exception
			// Log.e("Error in executing HttpGet");
			// Log.e(e);
			// Do something when output is in appropriate occurs
			e.printStackTrace();
			strResponse = null;
		} finally {
			httpHelper.consumeClinet();
		}

		return strResponse;
	}

	/**
	 * Execute HTTP post.
	 * 
	 * @param mContext
	 *            TODO
	 * @param url
	 *            The URL
	 * @param params
	 *            Use BasicNameValuePair to form List<NameValuePair>.<br/>
	 *            Pass null if no there are no parameters or use overloaded
	 *            version of the same.
	 * @return The response string.
	 */
	public static String executeHttpPost(Context context, String url,
			List<NameValuePair> params) {

		String strResponse = null;
		HttpHelper httpHelper = null;
		try {
			httpHelper = new HttpHelper();

			strResponse = httpHelper.executeHttpPost(url, params);
			isJson(strResponse);

		} catch (SocketTimeoutException se) {

			se.printStackTrace();
			// Do something when socket timeout exception occurs
		} catch (Exception e) {
			// TODO: handle exception
			// Log.e("Error in executing HttpGet");
			// Log.e(e);
			// Do something when output is in appropriate occurs
			e.printStackTrace();
			strResponse = null;
		} finally {

			httpHelper.consumeClinet();
		}

		return strResponse;
	}

	/**
	 * Execute HTTP post.
	 * 
	 * @param url
	 *            The URL
	 * @param params
	 *            Use BasicNameValuePair to form List<NameValuePair>.<br/>
	 *            Pass null if no there are no parameters or use overloaded
	 *            version of the same.
	 * @param mContext
	 *            TODO
	 * @return The response string.
	 */
	public static String executeHttpPost(String url,
			List<NameValuePair> params, boolean retry, Context context) {

		int count = RETRY_COUNT;
		HttpHelper httpHelper = new HttpHelper();

		String strResponce = null;
		do {
			Log.d("RETRY", "retrying :: " + url);

			try {
				strResponce = httpHelper.executeHttpPost(url, params);
				isJson(strResponce);
				
			} catch (SocketTimeoutException se) {

				se.printStackTrace();
				// Do something when socket timeout exception occurs
			} catch (Exception e) {
				// TODO: handle exception
				// Log.e("Error in executing HttpGet");
				// Log.e(e);
				// Do something when output is in appropriate occurs
				e.printStackTrace();
				strResponce = null;
			} finally {
				httpHelper.consumeClinet();
				if (strResponce != null)
					break;
			}
		} while (--count >= 0 && retry);

		return strResponce;
	}

	/**
	 * Convert stream to string.
	 * 
	 * @param streamToConvert
	 *            the InputStream to convert
	 * @return the string or null
	 */
	public static String convertStreamToString(InputStream streamToConvert) {
		try {
			return new Scanner(streamToConvert).useDelimiter("\\A").next();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	/**
	 * Gets the thread safe HttpClient.
	 * 
	 * @return the thread safe HttpClient
	 */
	private static HttpClient getThreadSafeHttpClient() {

		HttpClient client = new DefaultHttpClient();

		ClientConnectionManager mgr = client.getConnectionManager();

		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, WAIT_RESPONSE_TIMEOUT);
		HttpConnectionParams.setTcpNoDelay(params, true);

		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
				mgr.getSchemeRegistry()), params);

		return client;
	}

	/**
	 * Gets the input stream from URL.
	 * 
	 * @param url
	 *            the URL
	 * @return the input stream from URL
	 * @throws IllegalStateException
	 *             the illegal state exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws URISyntaxException
	 *             the uRI syntax exception
	 */
	private static InputStream getInputStreamFromUrl(String url)
			throws IllegalStateException, IOException, URISyntaxException {

		InputStream content = null;

		HttpClient httpclient = getThreadSafeHttpClient();

		HttpGet httpGet = new HttpGet(new URI(url));
		HttpResponse response = httpclient.execute(httpGet);

		content = response.getEntity().getContent();

		response.getEntity().consumeContent();
		httpclient.getConnectionManager().shutdown();

		return content;
	}

	private static class HttpHelper {

		InputStream inputStream = null;
		HttpClient httpClient = null;
		HttpResponse httpResponse = null;

		private String executeHttpPost(String url, List<NameValuePair> params)
				throws URISyntaxException, ClientProtocolException, IOException {

			httpClient = getThreadSafeHttpClient();
			HttpPost httpPost = new HttpPost(new URI(url));
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			httpResponse = httpClient.execute(httpPost);
			inputStream = httpResponse.getEntity().getContent();

			String strResponse = convertStreamToString(inputStream);

			return strResponse;
		}

		private String executeHttpGet(String url, List<NameValuePair> params)
				throws URISyntaxException, ClientProtocolException, IOException {

			String strParams = "";
			if (params != null) {
				strParams = URLEncodedUtils.format(params, "utf-8");
			}
			Log.d("test", " req : " + url + strParams);

			httpClient = getThreadSafeHttpClient();
			HttpGet httpGet = new HttpGet(new URI(url + strParams));
			httpResponse = httpClient.execute(httpGet);
			inputStream = httpResponse.getEntity().getContent();

			String strResponse = convertStreamToString(inputStream);

			return strResponse;
		}

		private void consumeClinet() {

			/*
			 * The entity needs to be consumed completely in order to re-use the
			 * connection with keep-alive.
			 */
			if (httpResponse != null) {
				try {
					httpResponse.getEntity().consumeContent();
					// Log.i("***** HTTP RESPONSE ENTITY CONSUMED *****");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// Log.e("***** ERROR CONSUMING HTTP RESPONSE ENTITY *****");
					// Log.e(e);
					e.printStackTrace();
				}
			}

			/*
			 * Shut down HttpClient
			 */
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
				// Log.i("***** HTTP CONNECTION SHUTDOWN *****");
			}

			/*
			 * Close input stream
			 */
			if (inputStream != null) {
				try {
					inputStream.close();
					// Log.i("***** INPUTSTREAM CLOSED *****");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// Log.e("ERROR CLOSING INPUTSTREAM");
					// Log.e(e);
					e.printStackTrace();
				}
			}

		}
	}
	private static boolean isJson(String responce){
		
		
		try {
			new JSONObject(responce);
			return true;
		} catch (JSONException e) { 
			e.printStackTrace();
			return false;
		}		
	}
}