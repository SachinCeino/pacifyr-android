package com.pacifyr.omhealthcare.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pacifyr.omhealthcare.R;


/**
 * Created by jp on 12/3/16.
 */
public class MainTextView extends androidx.appcompat.widget.AppCompatTextView {

    public MainTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MainTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + getResources().getString(R.string.main_font_medium));
            setTypeface(tf);
        }
    }

}
