package com.pacifyr.client.home;

import com.pacifyr.client.R;

/**
 * Created by developer on 16/11/17.
 */

public enum ModelObject {

    RED(com.pacifyrapp.utilitylibrary.R.string.message, R.layout.view_tour1),
    BLUE(com.pacifyrapp.utilitylibrary.R.string.message, R.layout.view_tour2),
    GREEN(com.pacifyrapp.utilitylibrary.R.string.message, R.layout.view_tour3);

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