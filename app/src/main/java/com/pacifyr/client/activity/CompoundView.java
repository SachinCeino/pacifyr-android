package com.pacifyr.client.activity;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyr.client.R;

/**

 */

public class CompoundView extends RelativeLayout {

    private DeviceFitImageView lef_top,left_bottom;
    private DeviceFitImageView right_top,right_bottom;
    DeviceFitImageView img;
    boolean myval=false;

    public CompoundView(Context context) {
        super(context);
        initializeViews(context);
    }

    public CompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public CompoundView(Context context,
                        AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.compund_item, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.
        lef_top = (DeviceFitImageView) this.findViewById(R.id.top_left);
        right_bottom = (DeviceFitImageView)this.findViewById(R.id.bottom_right);
        right_top = (DeviceFitImageView)this.findViewById(R.id.top_right);
        left_bottom = (DeviceFitImageView)this.findViewById(R.id.bottom_left);



        img=(DeviceFitImageView) findViewById(R.id.image) ;

        //img.setImageIcon(R.drawable.icon);
        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myval==false)
                {
                    lef_top.setVisibility(VISIBLE);
                    left_bottom.setVisibility(VISIBLE);
                    right_bottom.setVisibility(VISIBLE);
                    right_top.setVisibility(VISIBLE);
                    myval=true;
                }
                else{
                    lef_top.setVisibility(GONE);
                    left_bottom.setVisibility(GONE);
                    right_bottom.setVisibility(GONE);
                    right_top.setVisibility(GONE);
                    myval=false;
                }
            }
        });

        lef_top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        right_bottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        right_top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        left_bottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        



    }
}