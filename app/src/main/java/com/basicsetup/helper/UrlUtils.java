package com.anisolutions.BeanLogin.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.anisolutions.BeanLogin.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlUtils {
    public static boolean isValidUrl(Context context, String Url) {
        boolean isValid = false;
        try {

            URL url = new URL(Url);
            executeReq(url);
            isValid = true;
            //Toast.makeText(context, "Webpage is available!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.err_invalid_app_url), Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    private static void executeReq(URL urlObject) throws IOException {
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) urlObject.openConnection();
        conn.setReadTimeout(30000);//milliseconds
        conn.setConnectTimeout(3500);//milliseconds
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        // Start connect
        conn.connect();
        InputStream response = conn.getInputStream();
       // Log.d("Response:", response.toString());
    }


}
