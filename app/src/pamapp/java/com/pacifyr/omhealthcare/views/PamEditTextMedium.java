package com.pacifyr.omhealthcare.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

public class PamEditTextMedium extends EditText {


    public PamEditTextMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PamEditTextMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PamEditTextMedium(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Helvetica_.ttf");
            setTypeface(tf);
        }
    }


}
