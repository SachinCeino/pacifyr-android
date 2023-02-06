package com.pacifyr.client.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pacifyr.client.R;

public class MainFontMedium extends TextView {


    public MainFontMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MainFontMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainFontMedium(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.main_font_medium));
            setTypeface(tf);
        }
    }


}
