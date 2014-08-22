package com.basicsetup.helper.imageloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    
    /**
     * Get Bitmap using InputStream and save it name card id
     * 
     * @param context
     * @param inputStream
     * @param id
     *            cardId
     * @return Bitmap
     */
    public static Bitmap getBitmap(Context context, InputStream inputStream/*,
      String id*/) {

     File cacheDir;

     // Find the dir to save cached images
     if (android.os.Environment.getExternalStorageState().equals(
       android.os.Environment.MEDIA_MOUNTED))
      cacheDir = new File(
        android.os.Environment.getExternalStorageDirectory(),
        "appname");
     else
      cacheDir = context.getCacheDir();
     if (!cacheDir.exists())
      cacheDir.mkdirs();

     File f = new File(cacheDir, "banner");

     // from SD cache
     // Bitmap b = decodeFile(f);
     // if (b != null)
     // return b;

     // from web
     try {
      Bitmap bitmap = null;
      OutputStream os = new FileOutputStream(f);
      CopyStream(inputStream, os);
      os.close();
      // bitmap = decodeFile(f);
      bitmap = decodeF(f);
      return bitmap;
     } catch (Exception ex) {
      ex.printStackTrace();
      return null;
     }
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
}