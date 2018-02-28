package com.tech20.mobiledelivery.retrofitclient;

import android.util.Log;

import com.tech20.mobiledelivery.helpers.Constants;
import com.tech20.mobiledelivery.helpers.PreferenceUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fidel25 on 12/06/2017.
 */

public class RestClient {

    public static Converter<ResponseBody, ErrorMessage> getErrorConverter() {


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.UrlConstants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        Converter<ResponseBody, ErrorMessage> errorConverter =
                retrofit.responseBodyConverter(ErrorMessage.class, new Annotation[0]);

        return errorConverter;

    }

    public static <S> S createService(Class<S> serviceClass) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new LoggingInterceptor());
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.UrlConstants.SERVER_URL)
                .client(httpClient.build()).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceWithHeaders(Class<S> serviceClass, final PreferenceUtils preferenceUtils) {

        Log.d(Constants.LogConstants.TAG_WASTE, Constants.UrlConstants.HEADER_API_KEY + ":" + preferenceUtils.getString(Constants.PreferenceConstants.API_KEY) + "\n" +
                Constants.UrlConstants.HEADER_API_SECRET + ":" + preferenceUtils.getString(Constants.PreferenceConstants.API_SECRET) + "\n" +
                Constants.UrlConstants.HEADER_SESSION_ID + ":" + preferenceUtils.getString(Constants.PreferenceConstants.UNIQUE_INSTALLATION_ID) + "\n" +
                Constants.UrlConstants.HEADER_CONTENT_TYPE + ":" + "application/json"
        );
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                if (null == preferenceUtils)
                    Log.e("NULL", "NULL");
                if (null == preferenceUtils.getString(preferenceUtils.getString(Constants.PreferenceConstants.API_KEY)))
                Log.e("NULL", "NULL");

                final Request request = chain.request().newBuilder()
                        .addHeader(Constants.UrlConstants.HEADER_API_KEY, preferenceUtils.getString(Constants.PreferenceConstants.API_KEY))
                        .addHeader(Constants.UrlConstants.HEADER_API_SECRET, preferenceUtils.getString(Constants.PreferenceConstants.API_SECRET))
                        .addHeader(Constants.UrlConstants.HEADER_SESSION_ID, preferenceUtils.getString(Constants.PreferenceConstants.UNIQUE_INSTALLATION_ID))
                        .addHeader(Constants.UrlConstants.HEADER_CONTENT_TYPE, "application/json")
                        .build();

                return chain.proceed(request);
            }
        };
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);
        httpClient.addInterceptor(new LoggingInterceptor());
        OkHttpClient client = httpClient.build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.UrlConstants.SERVER_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

}
