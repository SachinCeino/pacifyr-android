package com.pacifyr.client.model;

import com.pacifyr.client.R;

/**

 */

public enum ModelObject {

    RED(R.string.message, R.layout.view_tour1),
    BLUE(R.string.message, R.layout.view_tour2),
    GREEN(R.string.message, R.layout.view_tour3),
    VIDEO(R.string.message, R.layout.view_video);



    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}