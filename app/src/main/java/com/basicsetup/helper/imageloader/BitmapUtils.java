package com.decos.fixi.helpers.HelperImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapUtils {
   
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){ex.printStackTrace();}
    }

    private static Bitmap decodeF(File f) {
    	  Bitmap b = null;
    	  try {

    	   BitmapFactory.Options bfo = new BitmapFactory.Options();
    	   //bfo.inSampleSize = 2;
    	   //bfo.outWidth = 200;
    	   //bfo.outHeight = 200;

    	   FileInputStream fis = new FileInputStream(f);

    	   b = BitmapFactory.decodeStream(fis, null, bfo);
    	   fis.close();

    	  } catch (Exception e) {

    	   e.printStackTrace();

    	  }
    	  return b;

    	 }

    public static Bitmap bitmapFromInputStream(String url) throws IOException {

        URL imageUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) imageUrl
                .openConnection();
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setInstanceFollowRedirects(true);
        InputStream is = conn.getInputStream();

        if(is == null){
            return null;
        }
       return  BitmapFactory.decodeStream(is);
    }
}