package com.ess.ess.Utils;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

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
