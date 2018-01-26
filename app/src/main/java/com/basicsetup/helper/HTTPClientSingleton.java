package com.basicsetup.helper;

/**
 * Created by pankaj on 12/9/16.
 */

public class HTTPClientSingleton {

    private static HttpClient httpClient = null;
    public static HttpClient getHttpClient(){

        if(httpClient == null){
            httpClient = new DefaultHttpClient();
        }
        return httpClient;
    }
}
