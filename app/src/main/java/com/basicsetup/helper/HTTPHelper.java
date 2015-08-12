package com.basicsetup.helper;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by pankaj on 13/2/15.
 */
public class HTTPHelper {

    public static class ResponseObject{

        private String strResponse;
        private int statusCode;

        public ResponseObject(String strResponse,int statusCode){
            this.strResponse=new String(strResponse);
            this.statusCode=statusCode;
        }

        public String getStrResponse() {
            return new String(strResponse);
        }

        public int getStatusCode() {
            return statusCode;
        }
    }

    /** The Constant CONNECTION_TIMEOUT. */
    public static final int CONNECTION_TIMEOUT = 10 * 1000;

    /** The Constant WAIT_RESPONSE_TIMEOUT. */
    public static final int WAIT_RESPONSE_TIMEOUT = 20 * 1000;

    /**  CONTENT TYPE **/
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_URLENCODE = "application/x-www-form-urlencoded";

    private String contentType = null;

    public HTTPHelper(String contentType){
        this.contentType=contentType;
    }

    public ResponseObject executeHttpPut(String url,ArrayList<String> urlParams,
                                  ContentValues params)
            throws URISyntaxException, UnsupportedEncodingException {

        ResponseObject responseObject = null;

        String strResponse = null;

        url+=formatUrlParams(urlParams);

        URL urlConn = null;
        try {

            byte[] postData = null;
            if(params!=null){

                if(contentType.equalsIgnoreCase(CONTENT_TYPE_JSON)){
                    postData = jsonOptionParams(params).getBytes(Charset.forName("UTF-8"));
                }else if(contentType.equalsIgnoreCase(CONTENT_TYPE_URLENCODE)){
                    postData = formatOptionParams(params).getBytes(Charset.forName("UTF-8"));
                }

            }

            Log.d("WASTE", "url:" + url);
            urlConn = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlConn.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", contentType);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(postData.length));

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.write(postData);
            os.flush();

            int statusCode = urlConnection.getResponseCode();
            Log.d("WASTE","statusCode:"+statusCode);

            InputStream inn = null;
            if(statusCode!=HttpURLConnection.HTTP_OK){
                inn = urlConnection.getErrorStream();
            }else{
                inn = urlConnection.getInputStream();

            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(inn));

            String inputLine;
            StringBuffer buffResponse = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                buffResponse.append(inputLine);
            }
            in.close();
            strResponse = buffResponse.toString();
            urlConnection.disconnect();

            responseObject = new ResponseObject(strResponse,statusCode);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public ResponseObject executeHttpPost(String url,ArrayList<String> urlParams,
                                   ContentValues params)
            throws URISyntaxException, UnsupportedEncodingException {

        String strResponse = null;
        ResponseObject responseObject=null;

        url+=formatUrlParams(urlParams);

        URL urlConn = null;
        try {

            byte[] postData = null;
            if(params!=null){

                if(contentType.equalsIgnoreCase(CONTENT_TYPE_JSON)){
                    postData = jsonOptionParams(params).getBytes(Charset.forName("UTF-8"));
                }else if(contentType.equalsIgnoreCase(CONTENT_TYPE_URLENCODE)){
                    postData = formatOptionParams(params).getBytes(Charset.forName("UTF-8"));
                }

            }


            Log.d("WASTE", "url:" + url);
            urlConn = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlConn.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", contentType);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(postData.length));

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.write(postData);
            os.flush();

            int responseCode = urlConnection.getResponseCode();

            Log.d("MESSAGE","Mess::"+urlConnection.getResponseMessage());

            InputStream inn = null;
            if(responseCode!=HttpURLConnection.HTTP_OK){
                inn = urlConnection.getErrorStream();
            }else{
                inn = urlConnection.getInputStream();

            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(inn));

            String inputLine;
            StringBuffer buffResponse = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                buffResponse.append(inputLine);
            }
            in.close();
            strResponse = buffResponse.toString();
            Log.d("WASTE", "PostREsponse:" + strResponse);
            urlConnection.disconnect();

            responseObject = new ResponseObject(strResponse,responseCode);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public ResponseObject executeHttpGet(String url, ArrayList<String> urlParams,
                                 ContentValues params)
            throws URISyntaxException, UnsupportedEncodingException {

        String strResponse = null;
        ResponseObject responseObject = null;

        url+=formatUrlParams(urlParams);

        if(params!=null){
            String strParams = "";
            if (params != null && params.size()>0) {
                if (!url.endsWith("?"))
                    url += "?";
                strParams = formatOptionParams(params);
                url += strParams;
            }
        }


        URL urlConn = null;
        try {

            urlConn = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlConn.openConnection();

            urlConnection.setConnectTimeout(30000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", contentType);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            int statusCode = urlConnection.getResponseCode();
            Log.d("WASTE", "url:"+url+"\n"+"responseCode:" + statusCode);

            InputStream inn = null;
            if(statusCode!=HttpURLConnection.HTTP_OK){
                inn = urlConnection.getErrorStream();
            }else{
                inn = urlConnection.getInputStream();

            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(inn));

            String inputLine;
            StringBuffer buffResponse = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                buffResponse.append(inputLine);
            }
            in.close();
            strResponse = buffResponse.toString();
            urlConnection.disconnect();

            responseObject = new ResponseObject(strResponse,statusCode);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public static String convertStreamToString(InputStream streamToConvert) {
        try {
            // uses default UTF-8 encoding format
            return new Scanner(streamToConvert).useDelimiter("\\A").next();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private static String formatOptionParams(ContentValues values) throws UnsupportedEncodingException {

        //If "Content-Type"="application/x-www-form-urlencoded" use this methode

        String formatedString = null;
        if(values!=null && values.size()>0){

            formatedString = "";

            Iterator<String> keySet = values.keySet().iterator();

            while (keySet.hasNext()){
                String key = keySet.next();
                String val = values.getAsString(key);
                formatedString+=URLEncoder.encode(key,"UTF-8")+"="+URLEncoder.encode(val,"UTF-8")+"&";
            }

            formatedString = formatedString.substring(0, formatedString.length() - 1);

        }
        return formatedString;
    }

    private static String formatUrlParams(ArrayList<String> urlParams) throws UnsupportedEncodingException {

        if(urlParams !=null){

            String urlParameters = StringUtils.getAppendedString(
                    urlParams.toArray(new String[urlParams.size()])
            )
                    .replaceAll(",", "/");

            if(!TextUtils.isEmpty(urlParameters)){
                return "/"+urlParameters;
            }
        }
    return "";
    }
    private static String jsonOptionParams(ContentValues values) throws UnsupportedEncodingException, JSONException {

        JSONObject jsonObject = new JSONObject();
        if(values!=null && values.size()>0){


            Iterator<String> keySet = values.keySet().iterator();

            while (keySet.hasNext()){
                String key = keySet.next();
                String val = values.getAsString(key);
                jsonObject.put(key,val);
            }

            Log.d("WASTE","jsonObject::"+jsonObject);

        }
        return jsonObject.toString();
    }
}

