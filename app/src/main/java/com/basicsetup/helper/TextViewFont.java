package com.basicsetup.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.basicsetup.R;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by pankaj on 6/8/15.
 */
public class TextViewFont extends TextView {

    public static Map<String,Typeface> mapType = new HashMap<String,Typeface>();

    public TextViewFont(Context context) {
        this(context, null);
    }

    public TextViewFont(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public TextViewFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(isInEditMode())return;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontText);

        if(typedArray!=null){

            String fontAsset = typedArray.getString(R.styleable.FontText_fontType);

            if(!TextUtils.isEmpty(fontAsset)){
                Typeface typeface = mapType.get(fontAsset);
                if(typeface == null){
                    typeface =  Typeface.createFromAsset(context.getAssets(),fontAsset);
                    mapType.put(fontAsset,typeface);
                }
                setTypeface(typeface);
            }
        }
    }
}
