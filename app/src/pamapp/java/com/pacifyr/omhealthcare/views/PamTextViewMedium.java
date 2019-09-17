package com.pacifyr.omhealthcare.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class PamTextViewMedium extends TextView {


    public PamTextViewMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PamTextViewMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PamTextViewMedium(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/EventTextViewMedium.ttf");
            setTypeface(tf);
        }
    }


}
