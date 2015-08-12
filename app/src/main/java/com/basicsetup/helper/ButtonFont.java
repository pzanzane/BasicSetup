package com.basicsetup.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;

import com.basicsetup.R;


/**
 * Created by pankaj on 6/8/15.
 */
public class ButtonFont extends Button {

    public ButtonFont(Context context) {
        this(context, null);
    }

    public ButtonFont(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(isInEditMode())return;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontText);

        if(typedArray!=null){

            String fontAsset = typedArray.getString(R.styleable.FontText_fontType);

            if(!TextUtils.isEmpty(fontAsset)){
                Typeface typeface = TextViewFont.mapType.get(fontAsset);
                if(typeface == null){
                    typeface =  Typeface.createFromAsset(context.getAssets(),fontAsset);
                    TextViewFont.mapType.put(fontAsset,typeface);
                }
                setTypeface(typeface);
            }
        }
    }
}