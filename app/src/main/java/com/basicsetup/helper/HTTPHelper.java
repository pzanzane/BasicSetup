package com.fact24poc.fact24poc.helpers;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by pankaj on 13/2/15.
 */
public class HTTPHelper {


    /** The Constant CONNECTION_TIMEOUT. */
    public static final int CONNECTION_TIMEOUT = 10 * 1000;

    /** The Constant WAIT_RESPONSE_TIMEOUT. */
    public static final int WAIT_RESPONSE_TIMEOUT = 20 * 1000;


    InputStream inputStream = null;
    HttpClient httpClient = null;
    HttpResponse httpResponse = null;

    public String executeHttpPost(String url,
                                   List<? extends NameValuePair> params)
            throws URISyntaxException, ClientProtocolException, IOException, JSONException {

        Log.d("WASTE", "executeHTTPPost");
        httpClient = getThreadSafeHttpClient();
        HttpPost httpPost = new HttpPost(new URI(url));

        httpPost.setHeader(HTTP.CONTENT_TYPE,"application/json" );

        JSONObject obj = new JSONObject();
        for(NameValuePair pair:params){
            obj.put(pair.getName(),pair.getValue());
        }
        StringEntity stringEntity = new StringEntity(obj.toString());

        httpPost.setEntity(stringEntity);
        httpResponse = httpClient.execute(httpPost);
        inputStream = httpResponse.getEntity().getContent();

        String strResponse = convertStreamToString(inputStream);

        return strResponse;
    }

    public String executeHttpPut(String url,
                                  List<? extends NameValuePair> params)
            throws URISyntaxException, ClientProtocolException, IOException {

        httpClient = getThreadSafeHttpClient();
        HttpPut httpPut = new HttpPut(new URI(url));
        httpPut.setEntity(new UrlEncodedFormEntity(params));
        httpResponse = httpClient.execute(httpPut);
        inputStream = httpResponse.getEntity().getContent();

        String strResponse = convertStreamToString(inputStream);

        return strResponse;
    }

    public String executeHttpGet(String url, ArrayList<String> urlParams,
                                  List<? extends NameValuePair> params)
            throws URISyntaxException, ClientProtocolException, IOException {

        String urlParameters = StringUtils.getAppendedString(
                urlParams.toArray(new String[urlParams.size()]))
                .replaceAll(",", "/");

        if(!TextUtils.isEmpty(urlParameters)){
            url+="/"+urlParameters;
        }

        String strParams = "";
        if (params != null && params.size()>0) {
            if (!url.endsWith("?"))
                url += "?";
            strParams = URLEncodedUtils.format(params, "utf-8");
        }
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

    public static String convertStreamToString(InputStream streamToConvert) {
        try {
            // uses default UTF-8 encoding format
            return new Scanner(streamToConvert).useDelimiter("\\A").next();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}

