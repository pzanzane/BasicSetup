package com.tech20.mobiledelivery.retrofitclient;

import com.tech20.mobiledelivery.utils.LoggerFile;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

import static com.tech20.mobiledelivery.helpers.Constants.LogConstants.TAG_LATCH;



public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        StringBuilder builder = new StringBuilder();

        Request request = chain.request();

        long t1 = System.nanoTime();

        builder.append("Sending request "+request.url()+" on "+"\n"+"Headers:"+"\n"
        +request.headers());
        builder.append("\n");
        builder.append("Request: "+"\n" + stringifyRequestBody(request,request.method()));
        builder.append("\n");
        builder.append("-------------------");
        builder.append("\n");

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();


        builder.append("Received response for "+response.request().url()
                        +" in "+((t2 - t1)/1e6d+" ms")
                        +"\n"
                        +"status_code: "+response.code()
                        +"\n"
                        +"Headers: "
                        +"\n"
                        +response.headers());



        final String responseString = new String(response.body().bytes());

        builder.append(responseString);
        builder.append("\n"+"============================================================");
        builder.append("\n"+"============================================================");

        Logger.d(builder.toString());

        return  response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), responseString))
                .build();
    }

    private static String stringifyRequestBody(Request request,String method) {
        if("GET".equalsIgnoreCase(method)){
            return "No Body";
        }

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private static class Logger{

        private static final boolean LOG_ENABLE = true;
        private static final boolean DETAIL_ENABLE = false;

        private Logger() {
        }

        private static String buildMsg(String msg) {
            StringBuilder buffer = new StringBuilder();

            if (DETAIL_ENABLE) {
                final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];

                buffer.append("[ ");
                buffer.append(Thread.currentThread().getName());
                buffer.append(": ");
                buffer.append(stackTraceElement.getFileName());
                buffer.append(": ");
                buffer.append(stackTraceElement.getLineNumber());
                buffer.append(": ");
                buffer.append(stackTraceElement.getMethodName());
                buffer.append("() ] _____ ");
            }



            buffer.append(msg);

            return buffer.toString();
        }



        public static void d(String msg) {
            if (LOG_ENABLE) {
                LoggerFile.d(TAG_LATCH, buildMsg(msg));
            }
        }
    }
}
