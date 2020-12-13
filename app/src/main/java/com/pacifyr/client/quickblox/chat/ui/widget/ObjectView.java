package com.pacifyr.client.quickblox.chat.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pacifyr.client.R;

public class ObjectView extends LinearLayout {
    private TextView mCaption;
    private ImageView mImage;

    public ObjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ObjectView, 0, 0);
        Drawable d = a.getDrawable(R.styleable.ObjectView_image);
        String s = a.getString(R.styleable.ObjectView_text);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.object_view, this, true);

        mCaption = findViewById(R.id.caption);
        mImage = findViewById(R.id.image);

        mImage.setImageDrawable(d);
        mCaption.setText(s);
        // mImage.setBackground(context.getDrawable(R.drawable.circle_backround));

    }

    public void updateCaption(String str) {
        mCaption.setText(str);
    }

    public void updateImage(Drawable d) {
        mImage.setImageDrawable(d);
    }

    public void setImgBackground(Drawable d) {
        mImage.setBackground(d);
    }
}

