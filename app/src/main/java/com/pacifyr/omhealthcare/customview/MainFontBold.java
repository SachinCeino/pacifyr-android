package com.pacifyr.omhealthcare.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pacifyr.omhealthcare.R;

public class MainFontBold extends TextView {


    public MainFontBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MainFontBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainFontBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.main_font_bold));
            setTypeface(tf);
        }
    }


}
