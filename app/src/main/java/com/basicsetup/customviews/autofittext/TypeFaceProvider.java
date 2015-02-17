package com.basicsetup.customviews.autofittext;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;

/**
 * 
 * @author Adarsha Nayak
 * 
 *         for creating, caching and using the type face
 * 
 */
public class TypeFaceProvider {

	private static final String TYPEFACE_FOLDER = "fonts";

	private static Hashtable<String, Typeface> sTypeFaces = new Hashtable<String, Typeface>(
			10);

	public static Typeface getTypeFace(Context context, String fileName) {

		if (fileName == null) {
			return null;
		}

		Typeface tempTypeface = sTypeFaces.get(fileName);

		if (tempTypeface == null) {
			String fontPath = new StringBuilder(TYPEFACE_FOLDER).append('/')
					.append(fileName).toString();
			tempTypeface = Typeface.createFromAsset(context.getAssets(),
					fontPath);
			sTypeFaces.put(fileName, tempTypeface);
		}

		return tempTypeface;
	}

}
