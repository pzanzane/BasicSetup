package asia.rewardeagle.rewardeagle.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import asia.rewardeagle.rewardeagle.R;

/**
 * Created by pankaj on 6/8/15.
 */
public class TextViewFont extends TextView {

    public static Map<String,Typeface> mapType = new HashMap<String,Typeface>();
    private String mSelectedFontName = null,mDefaultFontName=null;

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
            mSelectedFontName = typedArray.getString(R.styleable.FontText_selectStateFontType);
            mDefaultFontName = fontAsset;
            setFontAsset(mDefaultFontName);
        }
    }

    private void setFontAsset(String fontName){
        if(!TextUtils.isEmpty(fontName)){
            Typeface typeface = mapType.get(fontName);
            if(typeface == null){
                typeface =  Typeface.createFromAsset(getContext().getAssets(),fontName);
                mapType.put(fontName,typeface);
            }
            setTypeface(typeface);
        }
    }
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        if(!TextUtils.isEmpty(mSelectedFontName)){

            if(selected){
                setFontAsset(mSelectedFontName);
            }else{
                setFontAsset(mDefaultFontName);
            }
            Log.d("FUKAT", "selected:" + selected + " " + getText());
        }
    }
}
