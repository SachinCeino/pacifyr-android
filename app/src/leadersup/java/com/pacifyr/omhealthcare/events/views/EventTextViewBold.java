package com.pacifyr.omhealthcare.events.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class EventTextViewBold extends TextView {


    public EventTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EventTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventTextViewBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/EventTextViewBold.ttf");
            setTypeface(tf);
        }
    }


}
