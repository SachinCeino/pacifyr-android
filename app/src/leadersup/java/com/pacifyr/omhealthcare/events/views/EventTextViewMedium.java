package com.pacifyr.omhealthcare.events.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class EventTextViewMedium extends TextView {


    public EventTextViewMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EventTextViewMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventTextViewMedium(Context context) {
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
