package com.decos.fixi.helpers.HelperImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

	private MemoryCache memoryCache = null;
    private FileCache fileCache;
	private Map<ImageView, String> mapImageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService executorService;
    private int defaultImageId = -1;
    private Context context;
    private Handler handler;

    /*
     * Caching is Enabled By Default
     * Images Will be Compressed if caching is enabled
     */
    private boolean cachingEnabled = true;

    public static ImageLoader getSingleton(Context context,boolean isCacheEnabled,String cacheDir, int defaultImageId){
            return new ImageLoader(context,isCacheEnabled,cacheDir,defaultImageId);
    }

	private ImageLoader(Context context, String cacheDir,int defaultImageId) {
        memoryCache = new MemoryCache();
		fileCache = new FileCache(context,cacheDir);
		this.defaultImageId = defaultImageId;
		this.context = context;
		executorService = Executors.newFixedThreadPool(5);
		handler = new Handler();
	}
    private ImageLoader(Context context,boolean cachingEnabled,
                       String cacheDir,int defaultImageId) {
        this(context,cacheDir,defaultImageId);
        this.cachingEnabled=cachingEnabled;
    }

    public void displayImage(String url, ImageView imageView) {
		mapImageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			// Log.d(getClass().getCanonicalName(),
			// "bitmap found in memoroy chache.");
			imageView.setImageBitmap(bitmap);
            Log.d("IMAGELOADER","IMAGE From MemoryCache");
		} else {
			// Log.d(getClass().getCanonicalName(),
			// "bitmap NOT found in memoroy chache. checking file cache...");
			queuePhoto(url, imageView);
			imageView.setImageResource(defaultImageId);
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);
        if (b != null) {
            Log.d("IMAGELOADER","ImageFrom FileCache");
			// Log.d(getClass().getCanonicalName(),
			// "bitmap found in file cache.");
			return b;
		}

		// Log.d(getClass().getCanonicalName(),
		// "bitmap found in file cache. downloading from web...");
		// from web
		try {
			Bitmap bitmap = null;

            if(cachingEnabled){
                f=fetch(f,url);
                bitmap = decodeFile(f);
            }else{
                bitmap = BitmapUtils.bitmapFromInputStream(url);
            }
            Log.d("IMAGELOADER","ImageFrom Server");
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

    public static File fetch(File f, String url) throws IOException {

        URL imageUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) imageUrl
                .openConnection();
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setInstanceFollowRedirects(true);
        InputStream is = conn.getInputStream();

        OutputStream os = new FileOutputStream(f);
        BitmapUtils.CopyStream(is, os);
        os.close();

        return f;
    }


    // decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
                // decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(f), null, o);

                // Find the correct scale value. It should be the power of 2.
                final int REQUIRED_SIZE = 256;
                int width_tmp = o.outWidth, height_tmp = o.outHeight;
                int scale = 1;
                while (true) {
                    if (width_tmp / 2 < REQUIRED_SIZE
                            || height_tmp / 2 < REQUIRED_SIZE)
                        break;
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }


                    // decode with inSampleSize
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;
                    return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;
		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
            if(cachingEnabled) {
                memoryCache.put(photoToLoad.url, bmp);
            }

            if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// Log.d(getClass().getCanonicalName(),
			// "loading bitmap on UI thread.");
			// Activity a = (Activity) photoToLoad.imageView.getContext();
			// a.runOnUiThread(bd);

			handler.post(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = mapImageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageBitmap(BitmapFactory
						.decodeResource(context.getResources(), defaultImageId));
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}
